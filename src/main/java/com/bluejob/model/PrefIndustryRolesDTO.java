package com.bluejob.model;

import java.util.Set;
import java.util.stream.Collectors;

import com.bluejob.domain.JobIndsIndustryRole;
import com.bluejob.domain.PrefIndsIndustryRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrefIndustryRolesDTO {

	private Long industryId;
	private Set<Long> industryRoleIds;
	
	public PrefIndustryRolesDTO(){}
	public PrefIndustryRolesDTO(PrefIndsIndustryRole prefIndustryRole){
		this.industryId =prefIndustryRole.getIndustry().getIndustryId();
		this.industryRoleIds =prefIndustryRole.getIndustryRoles().stream()
				.map(industryRole -> industryRole.getIndustryRoleId()).collect(Collectors.toSet());
		
	}
	public PrefIndustryRolesDTO(JobIndsIndustryRole prefIndustryRole){
		this.industryId =prefIndustryRole.getIndustry().getIndustryId();
		this.industryRoleIds =prefIndustryRole.getIndustryRoles().stream()
				.map(industryRole -> industryRole.getIndustryRoleId()).collect(Collectors.toSet());
		
	}
}
