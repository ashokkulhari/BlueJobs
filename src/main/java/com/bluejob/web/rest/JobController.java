package com.bluejob.web.rest;



import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluejob.domain.Job;
import com.bluejob.model.JobDTO;
import com.bluejob.security.AuthoritiesConstants;
import com.bluejob.security.SecurityUtils;
import com.bluejob.service.JobService;
import com.bluejob.web.rest.util.CommonHelper;
import com.bluejob.web.rest.util.PaginationUtil;

@RestController
@RequestMapping("/api")
public class JobController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JobService jobService;
	
	@PostMapping("/create/job")
	@PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\" , \"" + AuthoritiesConstants.CANDIDATE + "\")")
    public  ResponseEntity<?>  createJob(@Valid @RequestBody JobDTO jobDTO) {
		log.debug("REST request to createJob : {}", jobDTO.getJobTitle(),SecurityUtils.getCurrentUserLogin());
        return CommonHelper.createEntityResponse(jobService.createJob(jobDTO),"Job",HttpStatus.CREATED,jobDTO.getJobTitle());
    }
	@PutMapping("/jobs/{jobId}")
	@PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\" , \"" + AuthoritiesConstants.CANDIDATE + "\")")
    public  ResponseEntity<?>  updateJob(@Valid @RequestBody JobDTO jobDTO,@PathVariable Long jobId) {
		log.debug("REST request to updateJob : {}", jobDTO.getJobTitle(),SecurityUtils.getCurrentUserLogin());
        return CommonHelper.updateEntityResponse(jobService.updateJob(jobDTO,jobId),"Job",HttpStatus.OK,jobDTO.getJobTitle());
    }
	@DeleteMapping("/jobs/{jobId}")
	@PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\" , \"" + AuthoritiesConstants.CANDIDATE + "\")")
    public  ResponseEntity<?>  deleteJob(@PathVariable Long jobId) {
		log.debug("REST request to deleteJob : {}", jobId,SecurityUtils.getCurrentUserLogin());
		
		HttpStatus delStatus=jobService.deleteJob(jobId);
		if(delStatus.equals(HttpStatus.OK)){
			return CommonHelper.deleteEntityResponse(true,"Job",HttpStatus.OK,jobId.toString());
		}else{
			return CommonHelper.customResponse(false,"Fail to delete Job",delStatus,jobId.toString());
		}
        
    }
	
	@GetMapping("/jobs/{jobId}")
	@PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\" , \"" + AuthoritiesConstants.CANDIDATE + "\")")
    public  ResponseEntity<?>  getJob(@PathVariable Long jobId) {
		log.debug("REST request to getJob : {}", jobId,SecurityUtils.getCurrentUserLogin());
        return CommonHelper.customResponse(jobService.getJob(jobId),"get Job",HttpStatus.OK,jobId.toString());
    }
	
	@GetMapping("/jobs")
    public ResponseEntity<List<Job>> getAllJobs(Pageable pageable) {
        final Page<Job> page = jobService.getAllESJobs(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jobs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
	
	 /**
     * SEARCH /_search/jobs/:query : search for the Job corresponding
     * to the query.
     *
     * @param query the query to search
     * @return the result of the search
     */
	//gender:MALE OR industryRoleName: Plumber
	//{"query": { "match": {"name" : "Rajesh" } }}
	//{"query": { "match": {"industryRoleName" : "Plumber" } }}
    @GetMapping("/_search/jobs/{query}")
    public List<Job> search(@PathVariable String query) {
        return jobService.search(query);
    }
    
}
