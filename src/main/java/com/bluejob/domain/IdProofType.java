package com.bluejob.domain;

import java.io.Serializable;

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
@Table(name = "id_proof_type")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IdProofType  implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_proof_type_id")
    private Long idProofTypeId;
	@NotNull
    @Size(min = 5, max = 254)
    @Column(name = "id_proof_type_name",length = 254, unique = true)
	private String idProofTypeName;
	
	
}