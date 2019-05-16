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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "candidate_preference")
public class CandidatePreference implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "candidate_pref_id")
    private Long candidatePrefId;
	
	@JsonIgnore
    @ToString.Exclude
	@OneToOne(mappedBy="candidatePreference",cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
	private Candidate candidate;
	
	@ManyToMany
    @JoinTable(
        name = "candidate_pref_work_type",
        joinColumns = {@JoinColumn(name = "candi_pref_id", referencedColumnName = "candidate_pref_id")},
        inverseJoinColumns = {@JoinColumn(name = "candi_pref_work_type_id", referencedColumnName = "work_type_id")})
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
	private Set<WorkType> workTypes;
    
	@ManyToMany
    @JoinTable(
        name = "candidate_pref_work_type",
        joinColumns = {@JoinColumn(name = "candi_pref_id", referencedColumnName = "candidate_pref_id")},
        inverseJoinColumns = {@JoinColumn(name = "candi_pref_workg_mode_id", referencedColumnName = "working_mode_id")})
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
	private Set<WorkingMode> workingModes;
	
	
    @Column(name = "working_time")
	private String workingTime;
    
    @ManyToMany
    @JoinTable(
        name = "candidate_pref_loc_city",
        joinColumns = {@JoinColumn(name = "candidate_pref_id", referencedColumnName = "candidate_pref_id")},
        inverseJoinColumns = {@JoinColumn(name = "candidate_pref_loc_city_id", referencedColumnName = "city_id")})
    @BatchSize(size = 20)
	private Set<City> preferedLocations;
    
    
    @JsonIgnore
	@ManyToMany
    @JoinTable(
        name = "candidate_pref_pri_skill",
        joinColumns = {@JoinColumn(name = "candidate_pref_id", referencedColumnName = "candidate_pref_id")},
        inverseJoinColumns = {@JoinColumn(name = "candidate_pref_pri_skill_id", referencedColumnName = "skill_id")})
    @BatchSize(size = 20)
	private Set<Skills> prefPrimarySkills;
 @JsonIgnore
	@ManyToMany
    @JoinTable(
        name = "candidate_pref_sec_skill",
        joinColumns = {@JoinColumn(name = "candidate_pref_id", referencedColumnName = "candidate_pref_id")},
        inverseJoinColumns = {@JoinColumn(name = "candidate_pref_sec_skill_id", referencedColumnName = "skill_id")})
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
	private Set<Skills> prefSecondarySkills;
 
 @JsonIgnore
 @OneToMany(cascade = {CascadeType.ALL} ,mappedBy="candidatePreference")
 private Set<PrefIndsIndustryRole> prefIndsIndustryRoles;
 
 
 @Column(name = "is_ready_to_travel_international")
 private Boolean isReadyToTravelInternational;
 @Column(name = "is_ready_to_travel_domestic")
 private Boolean isReadyToTravelDomestic;
 
 @Lob
 @Type(type = "org.hibernate.type.TextType")
 @Column(name = "expect_from_new_work" )
 private String expectFromNewWork;
}
