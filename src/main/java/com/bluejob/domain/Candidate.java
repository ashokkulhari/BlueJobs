package com.dufther.domain;

import java.io.Serializable;
import java.time.Instant;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.annotation.Id;
import com.dufther.config.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "candidate")
@Document(indexName = "candidate", type = "candidate", shards = 1)
public class Candidate extends AbstractAuditingEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ToString.Exclude
	@OneToOne
    @JoinColumn(name = "user_id")
    private User user;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "middle_name")
	private String middleName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "dob")
    private Instant dob = null;
	
	@Column(name = "place_of_birth")
	private String placeOfBirth;
	
	@Column(name = "native_place")
	private String nativePlace;
	
	@Column(name = "mother_tongue")
	private String motherTongue;
	
	@Enumerated(EnumType.STRING)
    private Gender gender;
	
	@Column(name = "passport_number")
	private Integer passportNumber;
	
	@Column(name = "alternate_mobile_no")
	private String alternateMobileNo;
	@ToString.Exclude
	@JsonIgnore
	@OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "address_id")
	private Address address;
	@ToString.Exclude
	@OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "candidate_details_id")	
	private CandidateDetails candidateDetails;
	@ToString.Exclude
	@OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "candidate_pref_id")
	private CandidatePreference candidatePreference;
	@ToString.Exclude
	@OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "experience_id")
	private Experience experience;
	@ToString.Exclude
	@OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "education_id")
	private Set<Education> educations;
	@Column(name = "profile_pic_file_name")
	private String profilePicFileName;
	@Column(name = "is_profile_pic_uploaded")
	private Boolean isProfilePicUploaded;
//	photo_id
//	Iid_proof

	
}
