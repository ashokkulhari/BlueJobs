package com.bluejob.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "skills")
public class Skills implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "skill_id")
    private Long skillId;
	@Column(name = "skill_name")
	private String skillName;
//	@JsonIgnore
//    @ToString.Exclude
//	@ManyToMany(mappedBy="primarySkills",fetch = FetchType.LAZY)
//	private Set<Experience> experience;
//	
//	@JsonIgnore
//    @ToString.Exclude
//	@ManyToMany(mappedBy="secondarySkills",fetch = FetchType.LAZY)
//	private Set<Experience> experience;
	
}