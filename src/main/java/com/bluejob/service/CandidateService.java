package com.dufther.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dufther.domain.Address;
import com.dufther.domain.Candidate;
import com.dufther.domain.CandidateDetails;
import com.dufther.domain.City;
import com.dufther.domain.Experience;
import com.dufther.domain.Industry;
import com.dufther.domain.Skills;
import com.dufther.domain.User;
import com.dufther.domain.UserType;
import com.dufther.model.CandidateDTO;
import com.dufther.repository.CandidateJPARepository;
import com.dufther.repository.CityRepository;
import com.dufther.repository.IndustryRepository;
import com.dufther.repository.SkillsRepository;
import com.dufther.searchrepository.CandidateSearchRepository;

@Service
public class CandidateService {
	 private final Logger log = LoggerFactory.getLogger(CandidateService.class);
	@Autowired
	private CandidateJPARepository candidateJPARepository;
	@Autowired
	private CandidateSearchRepository candidateSearchRepository;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private IndustryRepository industryRepository;
	@Autowired
	private SkillsRepository skillsRepository;
	
	@Autowired
    private  UserService userService;
	@Autowired
    private  MailService mailService;
	
	public Candidate saveCandidate(CandidateDTO candidateDTO , User user){
		
		Candidate candidate = new Candidate();
		
		candidate.setUser(user);
		setCandidateProps(candidateDTO, candidate);
		candidateJPARepository.save(candidate);
		candidateSearchRepository.save(candidate);
		log.debug("Created Information for Candidate: {}", candidate);
		return candidate;
	}

	public void setCandidateProps(CandidateDTO candidateDTO, Candidate candidate) {
		if(candidateDTO.getFirstName()!=null){candidate.setFirstName(candidateDTO.getFirstName());}
		if(candidateDTO.getLastName()!=null){candidate.setLastName(candidateDTO.getLastName());}
		if(candidateDTO.getDob()!=null){candidate.setDob(candidateDTO.getDob().toInstant());}
		if(candidateDTO.getMotherTongue()!=null){candidate.setMotherTongue(candidateDTO.getMotherTongue());}
		if(candidateDTO.getGender()!=null){candidate.setGender(candidateDTO.getGender());}
		
		if(candidateDTO.getLocationId()!=null){
			Address address =candidate.getAddress();
			if(address==null){
				address = new Address();
			}
			Long locationId =candidateDTO.getLocationId();
			City currentLocationCity =cityRepository.getOne(locationId);
			address.setPresentLocation(currentLocationCity);
			candidate.setAddress(address);
		}
		if(candidateDTO.getPhysicalStatus()!=null){
			CandidateDetails candidateDetails  = candidate.getCandidateDetails();
			if(candidateDetails==null){
				candidateDetails = new CandidateDetails();
			}
			candidateDetails.setPhysicalStatus(candidateDTO.getPhysicalStatus());
			candidate.setCandidateDetails(candidateDetails);
		}
		if(candidateDTO.getIndustryIds()!=null){
			Set<Long> industryIds =candidateDTO.getIndustryIds();
			Set<Industry> industries=new HashSet<>();
			for (Iterator iterator = industryIds.iterator(); iterator.hasNext();) {
				Long industryId = (Long) iterator.next();
				Industry industry  = industryRepository.getOne(industryId);
				industries.add(industry);
			}
			Experience experience = candidate.getExperience();
			if(experience==null){
				experience = new Experience();
			}
			experience.setIndustries(industries);
			
			Set<Long>  primarySkillIds=candidateDTO.getPrimarySkillIds();		
			Set<Skills> primarySkills=new HashSet<>();
			for (Iterator iterator = primarySkillIds.iterator(); iterator.hasNext();) {
				Long skillId = (Long) iterator.next();
				Skills skill  = skillsRepository.getOne(skillId);
				primarySkills.add(skill);
			}
			experience.setPrimarySkills(primarySkills);
			candidate.setExperience(experience);
		}
		
	}
	
	@Transactional
	public Candidate registerCandidate(CandidateDTO candidateDTO) {
		Candidate candidate =null;
		candidateDTO.setUserType(UserType.CANDIDATE);
		User user = userService.saveUser(candidateDTO);
		candidate = saveCandidate(candidateDTO, user);
		mailService.sendActivationEmail(user);
		return candidate;
	}
	@Transactional
	public boolean deleteCandidate(String userId) {
		boolean isDeleted=false;
		Optional<Candidate> candidates = candidateJPARepository.findOneByUserUserId(userId);
		if(candidates.isPresent()){
			Candidate candidate =candidates.get();
			candidateJPARepository.delete(candidate);
			candidateSearchRepository.delete(candidate);
			userService.deleteUser(userId);
			isDeleted=true;
			log.debug("Deleted Candidate: {}", candidate);
		}
			
		return isDeleted;
	}	
	
	/**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
	@Transactional
    public Optional<Candidate> updateCandidate(CandidateDTO candidateDTO) {
		System.out.println("..updateCandidate..");
    	Candidate candidate=null;
    	Optional<Candidate> candidates =candidateJPARepository.findOneByUserUserId(candidateDTO.getUserId());
    	if(candidates.isPresent()){
    		 candidate = candidates.get();
    		setCandidateProps(candidateDTO, candidate);
            candidateJPARepository.save(candidate);
            candidateSearchRepository.save(candidate);
            log.debug("Changed Information for Candidate: {}", candidate);
    	}else{
    		log.info("No candidate found with user Id : {}", candidateDTO.getUserId());
    	}
       return candidates;
    }
	
	@Transactional
    public boolean updateCandidateProfilePic(String userId, String fileName) {
		System.out.println("..updateCandidateProfilePic..");
    	Boolean isUpdated=false;
    	Optional<Candidate> candidates =candidateJPARepository.findOneByUserUserId(userId);
    	if(candidates.isPresent()){
    		Candidate candidate = candidates.get();
    		 candidate.setIsProfilePicUploaded(true);
    		 candidate.setProfilePicFileName(fileName);
            candidateJPARepository.save(candidate);
            candidateSearchRepository.save(candidate);
            isUpdated=true;
            log.debug("Changed Information for Candidate: {}", candidate);
    	}else{
    		log.info("No candidate found with user Id : {}", userId);
    	}
       return isUpdated;
    }

	   public Page<CandidateDTO> getAllManagedCandidates(Pageable pageable) {	  
	       return candidateJPARepository.findAll(pageable).map(CandidateDTO::new);
	   }
    
	   public Page<CandidateDTO> getAllESCandidates(Pageable pageable) {	  
	       return candidateSearchRepository.findAll(pageable).map(CandidateDTO::new);
	   }
	   
	   public List<Candidate> search(String query) {
	        return StreamSupport
	            .stream(candidateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
	            .collect(Collectors.toList());
	    }
	   
}
