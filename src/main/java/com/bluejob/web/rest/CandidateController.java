package com.bluejob.web.rest;


import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bluejob.config.Constants;
import com.bluejob.domain.Candidate;
import com.bluejob.model.CandidateDTO;
import com.bluejob.security.AuthoritiesConstants;
import com.bluejob.security.SecurityUtils;
import com.bluejob.service.CandidateService;
import com.bluejob.web.rest.errors.EmailAlreadyUsedException;
import com.bluejob.web.rest.errors.InvalidPasswordException;
import com.bluejob.web.rest.errors.LoginAlreadyUsedException;
import com.bluejob.web.rest.util.CommonHelper;
import com.bluejob.web.rest.util.HeaderUtil;
import com.bluejob.web.rest.util.PaginationUtil;


@RestController
@RequestMapping("/api")
public class CandidateController {
	
	private final Logger log = LoggerFactory.getLogger(CandidateController.class);
	
	@Autowired
	private CandidateService candidateService;
	
	@PostMapping("/candidateregister")
    @ResponseStatus(HttpStatus.CREATED)
    public  ResponseEntity<?>  registerCandidate(@Valid @RequestBody CandidateDTO managedUserVM) {
        ResponseEntity<?> entity = null;
            try {
            	if (!CommonHelper.checkPasswordLength(managedUserVM.getPassword())) {
                    throw new InvalidPasswordException();
                }
            	Candidate candidate  =candidateService.registerCandidate(managedUserVM);
                entity=CommonHelper.createEntityResponse(candidate,"Success",HttpStatus.OK,managedUserVM.getUserId());
            } catch(LoginAlreadyUsedException e){
            	entity =CommonHelper.customResponse(false,"User already exist "+e.getMessage() , HttpStatus.FAILED_DEPENDENCY,managedUserVM.getUserId());
            }catch(EmailAlreadyUsedException e){
            	entity=CommonHelper.customResponse(false,"Email already exist "+e.getMessage() , HttpStatus.FAILED_DEPENDENCY,managedUserVM.getUserId());
            }catch (Exception e) {
            	entity=CommonHelper.customResponse(false,"Exception : "+e.getMessage() , HttpStatus.EXPECTATION_FAILED,managedUserVM.getUserId());
                e.printStackTrace();
            }
        
        return entity;
    }

	
	/**
     * DELETE /candidates/:userId : delete the "userId" Candidate.
     *
     * @param userId the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/candidates/{userId:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")") // or hasRole("ADMIN") and in DB its should have value ROLE_ADMIN
    public ResponseEntity<?> deleteCandidate(@PathVariable String userId) {
        log.debug("REST request to delete Candidate: {}", userId);
        ResponseEntity<?> entity=null;
        String msg="";
//    	if(SecurityUtils.isOwnerOfObjectByUserId(userId)){
    		candidateService.deleteCandidate(userId);
    		 msg="A candidate is deleted with identifier " + userId ;
    		 entity =ResponseEntity.ok().headers(HeaderUtil.createAlert( msg, userId)).build();
//    	}else{
//    		msg="You do not have access to delete candidate with identifier " + userId ;
//    		entity =ResponseEntity.status(HttpStatus.FORBIDDEN).headers(HeaderUtil.createAlert( msg, userId)).build();
//    	}
        return entity;
    }
    
    @PutMapping("/candidates")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.CANDIDATE + "\")") 
    public ResponseEntity<?> updateUser(@Valid @RequestBody CandidateDTO candidateDTO) {
        log.debug("REST request to update Candidate : {}", candidateDTO);
        Optional<Candidate> updatedCandidate = Optional.empty();
        ResponseEntity<?> entity=null;
        String msg="";
        	if(SecurityUtils.isOwnerOfObjectByUserId(candidateDTO.getUserId())){
        		 updatedCandidate = candidateService.updateCandidate(candidateDTO);
        		 if(updatedCandidate!=null && updatedCandidate.isPresent()){
        			 msg="A candidate is updated with identifier " + candidateDTO.getUserId() ;
            		 entity =ResponseEntity.ok()
         					.headers(HeaderUtil.createAlert( msg, candidateDTO.getUserId()))
         					.body(updatedCandidate);
            	 }
        	}else{
        		msg="You do not have access to update candidate with identifier " + candidateDTO.getUserId() ;
        		entity =ResponseEntity.status(HttpStatus.FORBIDDEN).headers(HeaderUtil.createAlert( msg, candidateDTO.getUserId())).build();
        	}
        return entity;
    }
    
    /**
     * GET /candidates : get all candidates from DB.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all candidates
     */
    @GetMapping("/mn/candidates")
    public ResponseEntity<List<CandidateDTO>> getAllMNCandidates(Pageable pageable) {
        final Page<CandidateDTO> page = candidateService.getAllManagedCandidates(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/candidates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET /candidates : get all candidates from ES.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all candidates
     */
    @GetMapping("/candidates")
    public ResponseEntity<List<CandidateDTO>> getAllCandidates(Pageable pageable) {
        final Page<CandidateDTO> page = candidateService.getAllESCandidates(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/candidates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * SEARCH /_search/candidates/:query : search for the Candidate corresponding
     * to the query.
     *
     * @param query the query to search
     * @return the result of the search
     */
    @GetMapping("/_search/candidates/{query}")
    public List<Candidate> search(@PathVariable String query) {
        return candidateService.search(query);
    }
}
