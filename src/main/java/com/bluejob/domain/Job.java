package com.dufther.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;
import org.springframework.data.elasticsearch.annotations.Document;

import com.dufther.config.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
//@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "job")
@Document(indexName = "job", type = "job", shards = 1)
public class Job extends AbstractAuditingEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "job_title")
	private String jobTitle;
	
	
//	@JsonIgnore
	@OneToMany(cascade = {CascadeType.ALL} ,mappedBy="job")
	 private Set<JobIndsIndustryRole> jobIndsIndustryRoles;
	
	
	
	@Column(name = "no_of_positions")
	private Integer noOfPositions;
	
	@ToString.Exclude
	@ManyToMany
    @JoinTable(
        name = "job_loc_city",
        joinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "job_loc_city_id", referencedColumnName = "city_id")})
    @BatchSize(size = 20)
	private Set<City> workLocations;
	
	@Enumerated(EnumType.STRING)
    private Gender gender;
	@ToString.Exclude
	@ManyToMany
    @JoinTable(
        name = "job_work_type",
        joinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "job_work_type_id", referencedColumnName = "work_type_id")})
    @BatchSize(size = 20)
	private Set<WorkType> workTypes;
	@ToString.Exclude
	@ManyToMany
    @JoinTable(
        name = "job_working_mode",
        joinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "job_workg_mode_id", referencedColumnName = "working_mode_id")})
    @BatchSize(size = 20)
	private Set<WorkingMode> workingModes;
	
	@Column(name = "is_allowed_disabled_candi")
	private Boolean isAllowedDisabledCandi;
	@ToString.Exclude
	@ManyToMany
    @JoinTable(
        name = "job_allowance",
        joinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "job_allowance_id", referencedColumnName = "allowance_id")})
    @BatchSize(size = 20)
	private Set<Allowance> allowances;
	@ToString.Exclude
	@JsonIgnore
	@ManyToMany
    @JoinTable(
        name = "job_skill",
        		joinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "job_skill_id", referencedColumnName = "skill_id")})
    @BatchSize(size = 20)
	private Set<Skills> skills;
	
	
	@Column(name = "working_time")
	private String workingTime;
	@ToString.Exclude
	@JsonIgnore
	@ManyToMany
    @JoinTable(
        name = "job_language",
        joinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "job_language_id", referencedColumnName = "language_id")})
    @BatchSize(size = 20)	
	private Set<Language> languages;
	
	@Column(name = "min_salary")
	private Double minSalary;
	
	@Column(name = "max_salary")
	private Double maxSalary;
	@ToString.Exclude
	@JsonIgnore
	@ManyToMany
    @JoinTable(
        name = "job_certificate",
        joinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "job_certificate_id", referencedColumnName = "certificate_id")})
    @BatchSize(size = 20)	
	private Set<Certificate> Certificates;
	
	@Column(name = "is_passport_active")
	private Boolean isPassportActive;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "job_description" )
	private String jobDescription;
	
	@ToString.Exclude
	@JsonIgnore
	@ManyToMany
    @JoinTable(
        name = "job_qualification",
        joinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "job_qualification_id", referencedColumnName = "qualification_id")})
    @BatchSize(size = 20)	
	private Set<Qualification> qualifications;
	@ToString.Exclude
	@JsonIgnore
	@ManyToMany
    @JoinTable(
        name = "job_specialization",
        joinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "job_specialization_id", referencedColumnName = "specialization_id")})
    @BatchSize(size = 20)	
	private Set<Specialization> specializations;
	
	
}
