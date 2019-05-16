package com.bluejob.model;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.bluejob.domain.Candidate;
import com.bluejob.domain.Gender;
import com.bluejob.domain.PhysicalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateDTO  extends UserDTO{

	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "UTC")
	private Date dob ;
	private String motherTongue;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Enumerated(EnumType.STRING)
	private PhysicalStatus physicalStatus;
	private Set<Long> industryIds;
	private Long locationId;//cityId
	private Set<Long>  primarySkillIds;
	
	public CandidateDTO (){	}
	public CandidateDTO (Candidate candidate){
		this.firstName=candidate.getFirstName();
		this.lastName =candidate.getLastName();
		this.dob=Date.from(candidate.getDob());
		this.motherTongue=candidate.getMotherTongue();
		this.gender=candidate.getGender();
		this.physicalStatus=candidate.getCandidateDetails().getPhysicalStatus();
		this.industryIds=candidate.getExperience().getIndustries().stream().map(industryId->industryId.getIndustryId()).collect(Collectors.toSet());
		this.locationId=candidate.getAddress().getPresentLocation().getCityId();
		this.primarySkillIds =candidate.getExperience().getPrimarySkills().stream().map(primarySkill -> primarySkill.getSkillId()).collect(Collectors.toSet());
		this.createdBy=candidate.getCreatedBy();
		this.createdDate=candidate.getCreatedDate();
		this.lastModifiedBy=candidate.getLastModifiedBy();
		this.lastModifiedDate=candidate.getLastModifiedDate();
		this.userId =candidate.getUser().getUserId();
		this.email =candidate.getUser().getEmail();
		this.mobileNumber=candidate.getUser().getMobileNumber();
		this.status=candidate.getUser().getProfileStatus().getProfileStatusId();
		this.langKey=candidate.getUser().getLangKey();
		this.roleId=candidate.getUser().getAuthorities().stream().map(roleId -> roleId.getId()).collect(Collectors.toSet());
		
	}
}
