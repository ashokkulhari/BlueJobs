package com.bluejob.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "industry")
public class Industry implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "industry_id")
    private Long industryId;
	@NotNull
    @Size(min = 5, max = 254)
    @Column(name = "industry_name",length = 254, unique = true)
	private String industryName;

	@ManyToMany
    @JoinTable(
        name = "industry_industry_role",
        joinColumns = {@JoinColumn(name = "industry_id", referencedColumnName = "industry_id")},
        inverseJoinColumns = {@JoinColumn(name = "industry_role_id", referencedColumnName = "industry_role_id")})
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
	private Set<IndustryRole> industryRoles;
}
