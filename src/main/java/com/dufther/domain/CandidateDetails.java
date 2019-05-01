package com.dufther.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "candidate_details")
public class CandidateDetails implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "candidate_details_id")
    private Long CandidateDetailsId;
	@JsonIgnore
	@ManyToMany
    @JoinTable(
        name = "candidate_details_known_lang",
        joinColumns = {@JoinColumn(name = "candiadte_details_id", referencedColumnName = "candidate_details_id")},
        inverseJoinColumns = {@JoinColumn(name = "cd_known_language_id", referencedColumnName = "known_language_id")})
    @BatchSize(size = 20)	
	private Set<KnownLanguage> knownLanguages;// KnownLanguages
	
	@Enumerated(EnumType.STRING)
    private PhysicalStatus physicalStatus;
	
	@Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
	
	@Column(name = "is_police_verify_done")
	private Boolean isPoliceVerifyDone;
	
	@Column(name = "is_bank_account_available")
	private Boolean isBankAccountAvailable;
	@Column(name = "religion")
	private String religion;
	@Column(name = "is_smart_phone_available")
	private Boolean isSmartPhoneAvailable;
	@Column(name = "whatsapp_number")
	private String whatsappNumber;
	
	@JsonIgnore
	@ManyToMany
    @JoinTable(
        name = "cand_details_adrs_proof_type",
        joinColumns = {@JoinColumn(name = "candiadte_details_id", referencedColumnName = "candidate_details_id")},
        inverseJoinColumns = {@JoinColumn(name = "cd_adrs_proof_type_id", referencedColumnName = "address_proof_type_id")})
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
	private Set<AdressProofType> adressProofTypes;
	
	@JsonIgnore
	@ManyToMany
    @JoinTable(
        name = "cand_details_id_proof_type",
        joinColumns = {@JoinColumn(name = "candiadte_details_id", referencedColumnName = "candidate_details_id")},
        inverseJoinColumns = {@JoinColumn(name = "cd_id_proof_type_id", referencedColumnName = "id_proof_type_id")})
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
	private Set<IdProofType> idProofTypes;
	
	@JsonIgnore
    @ToString.Exclude
	@OneToOne(mappedBy="candidateDetails",cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
	private Candidate candidate;
}