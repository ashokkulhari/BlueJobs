package com.bluejob.domain;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "address")
public class Address implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
    private Long addressId;
	
	@Column(name = "address_line1")
	private String addressLine1;
	
	@Column(name = "address_line2")
	private String addressLine2;
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "city_id")
	private City presentLocation;
	
	@Column(name = "present_pincode")
	private Integer presentPincode;
	@ManyToOne
    @JoinColumn(name = "address_proof_type_id")
	private AdressProofType addressProofType;
	
	@Column(name = "is_stay_with_family")
	private Boolean isStayWithFamily;
	@JsonIgnore
	@OneToOne(mappedBy="address",cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
	private Candidate candidate;
}