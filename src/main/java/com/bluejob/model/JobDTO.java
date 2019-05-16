package com.dufther.model;



import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


import com.dufther.domain.Gender;
import com.dufther.domain.Job;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String jobTitle;
	private Set<PrefIndustryRolesDTO> prefIndustryRoleDTOs;
	private Integer noOfPositions;
	private Set<Long> workLocationIds;
	@Enumerated(EnumType.STRING)
    private Gender gender;
	private Set<Long> workTypeIds;
	private Set<Long> workingModeIds;
	private Boolean isAllowedDisabledCandi;
	private Set<Long> allowanceIds;
	private Set<Long> skillIds;
	private String workingTime;
	private Set<Long> languages;
	private Double minSalary;
	private Double maxSalary;
	private Set<Long> Certificates;
	private Boolean isPassportActive;
	private String jobDescription;
	private Set<Long> qualifications;
	private Set<Long> specializations;
	
	public JobDTO(){}
	public JobDTO(Job job){
		this.jobTitle=job.getJobTitle();
		this.noOfPositions=job.getNoOfPositions();
		this.gender=job.getGender();
		this.isAllowedDisabledCandi=job.getIsAllowedDisabledCandi();
		this.prefIndustryRoleDTOs = job.getJobIndsIndustryRoles().stream()
				.map(PrefIndustryRolesDTO::new).collect(Collectors.toSet());
		this.workLocationIds=job.getWorkLocations().stream().map(city -> city.getCityId()).collect(Collectors.toSet());
		this.workTypeIds=job.getWorkTypes().stream().map(workType -> workType.getWorkTypeId()).collect(Collectors.toSet());
		this.workingModeIds = job.getWorkingModes().stream().map(workingMode -> workingMode.getWorkingModeId()).collect(Collectors.toSet());
	    this.allowanceIds = job.getAllowances().stream().map(allowance -> allowance.getAllowanceId()).collect(Collectors.toSet());
	    this.skillIds = job.getSkills().stream().map(skill -> skill.getSkillId()).collect(Collectors.toSet());
	}
}
