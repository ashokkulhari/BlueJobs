package com.dufther.security.services;

import com.dufther.domain.Authority;
import com.dufther.domain.Permission;
import com.dufther.domain.User;
import com.dufther.model.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserPrinciple implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    
    private String userId;
    
    

	private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(Long id,String userId, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
      //  this.name = name;
  		this.userId = userId;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrinciple build(User user) {
        System.out.println("UserPrinciple "+user + " user.getRoles() = "+user.getAuthorities() );
        
        ProfileStatus status  =ProfileStatus.active ;
        if (!user.getProfileStatus().getProfileStatusId().trim().equals(status.name())) {
            throw new UserNotActivatedException("User " + user.getUserId() + " was not activated");
        }
        
        Set<Authority> roles=user.getAuthorities();

        List<GrantedAuthority> grantedAuthorities =new ArrayList<>();
        for(Authority role:roles){
            Set<Permission> permissions=role.getPermissions();
            for(Permission permission:permissions){
                SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(permission.getName());
                grantedAuthorities.add(simpleGrantedAuthority);
            }
        }

      /*  List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority()
        ).collect(Collectors.toList());
*/
        return new UserPrinciple(
                user.getId(),
                user.getUserId(),
                user.getEmail(),
                user.getPassword(),
                grantedAuthorities
        );
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }
}