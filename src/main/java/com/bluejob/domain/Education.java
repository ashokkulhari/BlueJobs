package com.bluejob.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "education")
public class Education implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "education_id")
    private Long educationId;
	
	@Column(name = "university_or_board")
	private String universityOrBoard;
	
	@Column(name = "stream")
	private String stream;
	
	@Column(name = "year_of_passing")
	private Integer yearOfPassing;
	
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "specialization_id")
	private Specialization specialization;
	
	@Column(name = "is_highest_qualification")
	private Boolean isHighestQualification;
	
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "qualification_id")
	private Qualification qualification;
	
	
}