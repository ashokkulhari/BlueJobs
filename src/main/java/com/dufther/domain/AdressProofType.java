package com.dufther.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "address_proof_type")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdressProofType implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_proof_type_id")
    private Long adressProofTypeId;
	@NotNull
    @Size(min = 5, max = 254)
    @Column(name = "address_proof_type_name",length = 254, unique = true)
	private String adressProofTypeName;
	
	
}