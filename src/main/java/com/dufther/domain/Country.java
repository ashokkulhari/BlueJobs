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

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "country")
public class Country implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "country_id")
    private Long countryId;
	@NotNull
    @Size(min = 5, max = 254)
    @Column(name = "country_name",length = 254, unique = true)
	private String countryName;
}
