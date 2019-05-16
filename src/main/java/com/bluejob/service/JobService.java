package com.dufther.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dufther.domain.Industry;
import com.dufther.domain.IndustryRole;
import com.dufther.domain.Job;
import com.dufther.domain.JobIndsIndustryRole;
import com.dufther.domain.PrefIndsIndustryRole;
import com.dufther.model.CandidateDTO;
import com.dufther.model.JobDTO;
import com.dufther.repository.AllowanceRepository;
import com.dufther.repository.CertificateRepository;
import com.dufther.repository.CityRepository;
import com.dufther.repository.IndustryRepository;
import com.dufther.repository.IndustryRoleRepository;
import com.dufther.repository.JobJPARepository;
import com.dufther.repository.LanguageRepository;
import com.dufther.repository.QualificationRepository;
import com.dufther.repository.SkillsRepository;
import com.dufther.repository.SpecializationRepository;
import com.dufther.repository.WorkTypeRepository;
import com.dufther.repository.WorkingModeRepository;
import com.dufther.searchrepository.JobSearchRepository;


@Service
public class JobService {
	 private final Logger log = LoggerFactory.getLogger(this.getClass());
	 
	 @Autowired
	 private JobJPARepository jobJPARepository;
	 
	 @Autowired
	 private JobSearchRepository jobSearchRepository;
	 @Autowired
	 private IndustryRepository industryRepository;

	 @Autowired
	 private IndustryRoleRepository industryRoleRepository;
	 @Autowired
	 private CityRepository cityRepository;
	 
	 @Autowired
	 private WorkTypeRepository workTypeRepository;
	 @Autowired
	 private WorkingModeRepository workingModeRepository;
	 
	 @Autowired
	 private AllowanceRepository allowanceRepository;
	 
	 @Autowired
	 private SkillsRepository skillsRepository;
	 
	 @Autowired
	 private QualificationRepository qualificationRepository;
	 
	 @Autowired
	 private LanguageRepository languageRepository;
	 
	 @Autowired
	 private SpecializationRepository specializationRepository;
	 
	 @Autowired
	 private CertificateRepository certificateRepository;
	 
	 @Transactional
	public Job createJob(@Valid JobDTO jobDTO) {
		Job job = new Job();
		
		setJobProp(jobDTO, job);
		job=jobJPARepository.save(job);
		jobSearchRepository.save(job);
		
		return job;
	}
	 @Transactional
	public Job updateJob(@Valid JobDTO jobDTO, Long jobId) {
		Optional<Job> jobs = jobJPARepository.findById(jobId);
		Job job =null;
		if(jobs.isPresent()){
			job =jobs.get();
			setJobProp(jobDTO, job);
			job=jobJPARepository.save(job);
			jobSearchRepository.save(job);
		}
		return job;
	}
	 @Transactional
	public HttpStatus deleteJob( Long jobId) {
		Optional<Job> jobs = jobJPARepository.findById(jobId);
		if(jobs.isPresent()){
			Job job =jobs.get();
			jobJPARepository.delete(job);
			jobSearchRepository.delete(job);
			log.debug("Deleted Job: {}", job);
			return HttpStatus.OK;
		}else{
			log.info("While deleting not found Job: {}", jobId);
			return HttpStatus.NOT_FOUND;
		}
	}
	
	public void setJobProp(JobDTO jobDTO, Job job) {
		if(jobDTO.getJobTitle()!=null ){job.setJobTitle(jobDTO.getJobTitle());	}

		if(jobDTO.getPrefIndustryRoleDTOs()!=null && jobDTO.getPrefIndustryRoleDTOs().size()>0){
			Set<JobIndsIndustryRole> jonIndsIndustryRoles =new HashSet<>();
			
			
			
			jobDTO.getPrefIndustryRoleDTOs().stream().forEach(
					prefIndustryRolesDTO ->
						{
							JobIndsIndustryRole jobIndsIndustryRole = new JobIndsIndustryRole();
							jobIndsIndustryRole.setIndustryRoles(
									industryRoleRepository.
									findAllById(
											prefIndustryRolesDTO.getIndustryRoleIds().stream().collect(Collectors.toList())
									)
									.stream().collect(Collectors.toSet()) 
								 );
							jobIndsIndustryRole.setIndustry(industryRepository.findById(prefIndustryRolesDTO.getIndustryId()).get());
							jobIndsIndustryRole.setJob(job);
							jonIndsIndustryRoles.add(jobIndsIndustryRole);
						}
					);
			job.setJobIndsIndustryRoles(jonIndsIndustryRoles);
		}
		if(jobDTO.getNoOfPositions()!=null ){job.setNoOfPositions(jobDTO.getNoOfPositions());}
		if(jobDTO.getWorkLocationIds()!=null){
			job.setWorkLocations(jobDTO.getWorkLocationIds().stream()
			.map(cityId -> cityRepository.findById(cityId)
					.get()).collect(Collectors.toSet()));
		}
		if(jobDTO.getGender()!=null ){job.setGender(jobDTO.getGender());}
		
		if(jobDTO.getWorkTypeIds()!=null){
			job.setWorkTypes(jobDTO.getWorkTypeIds().stream()
			.map(workTypeId -> workTypeRepository.findById(workTypeId)
					.get()).collect(Collectors.toSet()));
		}
		if(jobDTO.getWorkingModeIds()!=null){
			job.setWorkingModes(jobDTO.getWorkingModeIds().stream()
			.map(workingModeId -> workingModeRepository.findById(workingModeId)
					.get()).collect(Collectors.toSet()));
		}
		if(jobDTO.getIsAllowedDisabledCandi()!=null){job.setIsAllowedDisabledCandi(jobDTO.getIsAllowedDisabledCandi());}
		if(jobDTO.getAllowanceIds()!=null){
			job.setAllowances(jobDTO.getAllowanceIds().stream()
			.map(allowanceId -> allowanceRepository.findById(allowanceId)
					.get()).collect(Collectors.toSet()));
		}
		
		if(jobDTO.getSkillIds()!=null){
			job.setSkills(jobDTO.getSkillIds().stream()
			.map(skillId -> skillsRepository.findById(skillId)
					.get()).collect(Collectors.toSet()));
		}
		
		if(jobDTO.getWorkingTime()!=null ){job.setWorkingTime(jobDTO.getWorkingTime());}
		if(jobDTO.getLanguages() !=null){
			job.setLanguages(jobDTO.getLanguages().stream()
			.map(languageId -> languageRepository.findById(languageId)
					.get()).collect(Collectors.toSet()));
		}
		if(jobDTO.getMinSalary()!=null ){job.setMinSalary(jobDTO.getMinSalary());}
		if(jobDTO.getMaxSalary()!=null ){job.setMaxSalary(jobDTO.getMaxSalary());}
		
		if(jobDTO.getCertificates() !=null){
			job.setCertificates(jobDTO.getCertificates().stream()
			.map(certificateId -> certificateRepository.findById(certificateId)
					.get()).collect(Collectors.toSet()));
		}
		if(jobDTO.getIsPassportActive()!=null ){job.setIsPassportActive(jobDTO.getIsPassportActive());}
		if(jobDTO.getJobDescription()!=null ){job.setJobDescription(jobDTO.getJobDescription());}
		
		if(jobDTO.getQualifications() !=null){
			job.setQualifications(jobDTO.getQualifications().stream()
			.map(qualificationId -> qualificationRepository.findById(qualificationId)
					.get()).collect(Collectors.toSet()));
		}
		if(jobDTO.getSpecializations() !=null){
			job.setSpecializations(jobDTO.getSpecializations().stream()
			.map(specializationId -> specializationRepository.findById(specializationId)
					.get()).collect(Collectors.toSet()));
		}
	}
	
	public Page<Job> getAllESJobs(Pageable pageable) {
		return jobSearchRepository.findAll(pageable);
	}
	public Job getJob(Long jobId) {
		return jobSearchRepository.findById(jobId).get();
	}
	public List<Job> search(String query) {
		return StreamSupport
	            .stream(jobSearchRepository.search(queryStringQuery(query)).spliterator(), false)
	            .collect(Collectors.toList());
	}
}
