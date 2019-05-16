package com.dufther.domain;

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
@Table(name = "industry_role")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IndustryRole implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "industry_role_id")
    private Long industryRoleId;
	@NotNull
    @Size(min = 5, max = 254)
    @Column(name = "industry_role_name",length = 254, unique = true)
	private String industryRoleName;
	
	
}