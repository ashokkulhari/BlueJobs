package com.dufther.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "experience")
public class Experience implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "experience_id")
    private Long experienceId;
	
	@JsonIgnore
    @ToString.Exclude
	@OneToOne(mappedBy="experience",cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
	private Candidate candidate;
	
	@Column(name = "notice_period_days")
	private Integer noticePeriodDays;
	
	@Column(name = "type_of_candidate")
	private String typeOfCandidate;
	
	@Column(name = "years_of_exp")
	private Integer yearsOfExperience;
	
	@Column(name = "current_salary")
	private Double currentSalary;
	
	@Column(name = "is_currently_working")
	private Boolean isCurrentlyWorking;
	
	@Column(name = "last_two_companies_worked")
	private String lastTwoCompaniesWorked;
	
	@Column(name = "present_mgr_name")
	private String presentMgrName;
	
	@Column(name = "phy_fitness_certification")
	private Boolean phyFitnessCertification;
	
	@Column(name = "type_of_work_performed")
	private String typeOfWorkPerformed;
	
	
	@ManyToMany
    @JoinTable(
        name = "experience_location_city",
        joinColumns = {@JoinColumn(name = "experience_id", referencedColumnName = "experience_id")},
        inverseJoinColumns = {@JoinColumn(name = "exp_location_city_id", referencedColumnName = "city_id")})
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
	private Set<City> locationWorked;
	
	@Column(name = "previous_mgr_name")
	private String previousMgrName;
	
	@Column(name = "is_abroad_experience")
	private Boolean isAbroadExperience;
	
	@Column(name = "any_active_visa")
	private Boolean anyActiveVisa;
	
	@Column(name = "abroad_role_performed")
	private String abroadRolePerformed;
	@JsonIgnore
	@ManyToMany
    @JoinTable(
        name = "experience_act_visa_country",
        joinColumns = {@JoinColumn(name = "experience_id", referencedColumnName = "experience_id")},
        inverseJoinColumns = {@JoinColumn(name = "exp_act_visa_country_id", referencedColumnName = "country_id")})
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
	private Set<Country> activeVisaCountries;
	@JsonIgnore
	@ManyToMany
    @JoinTable(
        name = "experience_abroad_city",
        joinColumns = {@JoinColumn(name = "experience_id", referencedColumnName = "experience_id")},
        inverseJoinColumns = {@JoinColumn(name = "exp_abroad_city_id", referencedColumnName = "city_id")})
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
	private Set<City> abroadLocationWorked;
	@JsonIgnore
	 @ManyToMany
	    @JoinTable(
	        name = "experience_industry",
	        joinColumns = {@JoinColumn(name = "experience_id", referencedColumnName = "experience_id")},
	        inverseJoinColumns = {@JoinColumn(name = "exp_industry_id", referencedColumnName = "industry_id")})
//	    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	    @BatchSize(size = 20)
	private Set<Industry> industries;
	 @JsonIgnore
		@ManyToMany
	    @JoinTable(
	        name = "experience_primary_skill",
	        joinColumns = {@JoinColumn(name = "experience_id", referencedColumnName = "experience_id")},
	        inverseJoinColumns = {@JoinColumn(name = "exp_primary_skill_id", referencedColumnName = "skill_id")})
//	    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	    @BatchSize(size = 20)
		private Set<Skills> primarySkills;
	 @JsonIgnore
		@ManyToMany
	    @JoinTable(
	        name = "experience_secondary_skill",
	        joinColumns = {@JoinColumn(name = "experience_id", referencedColumnName = "experience_id")},
	        inverseJoinColumns = {@JoinColumn(name = "exp_secondary_skill_id", referencedColumnName = "skill_id")})
//	    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	    @BatchSize(size = 20)
		private Set<Skills> secondarySkills;

}