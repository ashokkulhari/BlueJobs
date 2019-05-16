package com.bluejob.config;


import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@MappedSuperclass
@Audited
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable implements Serializable{
	private static final long serialVersionUID = 1L;
	@CreatedBy
    @JsonIgnore
    protected Long createdBy;

    @CreatedDate
    @JsonIgnore
    protected Instant createdDate = Instant.now();

    @LastModifiedBy
    @JsonIgnore
    protected Long lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonIgnore
    protected Instant lastModifiedDate = Instant.now();

	
	

   
}


//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
//public abstract class Auditable<U> {
//    @CreatedBy
//    protected U createdBy;
//    @CreatedDate
//    protected Timestamp creationDate;
//    @LastModifiedBy
//    protected U lastModifiedBy;
//    @LastModifiedDate
//    protected Timestamp lastModifiedDate;
//}
