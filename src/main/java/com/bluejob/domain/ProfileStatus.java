package com.bluejob.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "profile_status")
public class ProfileStatus {

	
	@Id
    @NotNull
    @Size(max = 50)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "profile_status_id",length = 50)
	 private String profileStatusId;
	    
	@Column(name = "profile_status_name")
	 private String profileStatusName ;
}
