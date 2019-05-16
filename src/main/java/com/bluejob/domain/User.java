package com.bluejob.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.BatchSize;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;
import java.time.Instant;

/**
 * A user.
 */
@Entity
@Getter
@Setter
@Table(name = "user")
@Document(indexName = "user", type = "user", shards = 1)
public class User extends AbstractAuditingEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull
//    @Pattern(regexp = Constants.LOGIN_REGEX)
//    @Size(min = 1, max = 50)
//    @Column(length = 50, unique = true, nullable = false)
//    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @NotNull
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String userId;
    
    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @Size(min = 5, max = 20)
    @Column(length = 20, unique = true)
    private String mobileNumber;
    
    @Size(min = 2, max = 6)
    @Column(name = "lang_key", length = 6)
    private String langKey;

    @Enumerated(EnumType.STRING)
    private UserType userType;
    
    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;
    
    @Column(name = "last_login_date")
    private Instant lastLoginDate = null;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
        name = "user_authority",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities ;
    
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "profile_status_id")
    private ProfileStatus profileStatus;
       
    @JsonIgnore
    @ToString.Exclude
	@OneToOne(mappedBy="user",cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
	private Candidate candidate;
}
