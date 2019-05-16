package com.dufther.model;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.dufther.config.Auditable;
import com.dufther.config.Constants;
import com.dufther.domain.AbstractAuditingEntity;
import com.dufther.domain.Authority;
import com.dufther.domain.User;
import com.dufther.domain.UserType;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
public class UserDTO extends AbstractAuditingEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	protected Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    protected String userId;

    @Email
    @Size(min = 5, max = 254)
    protected String email;

    protected String mobileNumber;

    protected String status;

    protected String langKey;

    protected String password;

    protected Set<Long> roleId;
   
    @Enumerated(EnumType.STRING)
    protected UserType userType;
    
    public UserDTO() {
    	
    }
    public UserDTO(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.status = user.getProfileStatus().getProfileStatusId();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.roleId = user.getAuthorities().stream()
            .map(Authority::getId)
            .collect(Collectors.toSet());
    }
    
}
