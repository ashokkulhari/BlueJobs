package com.dufther.security.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

import com.dufther.domain.Authority;
import com.dufther.domain.Permission;
import com.dufther.domain.User;
import com.dufther.model.ProfileStatus;
import com.dufther.repository.UserJPARepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserJPARepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId)
            throws UsernameNotFoundException {
        System.out.println("userId  ="+userId);
        User user = userRepository.findOneByUserId(userId).get();

        return UserPrinciple.build(user);
    }
    
    
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(final String login) {
//        log.debug("Authenticating {}", login);
//
//        if (new EmailValidator().isValid(login, null)) {
//            return userRepository.findOneWithAuthoritiesByEmail(login)
//                .map(user -> UserPrinciple.build(user))
//                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
//        }
//
//        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
//        return userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin)
//            .map(user -> createSpringSecurityUser(lowercaseLogin, user))
//            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
//
//    }
//    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
//        if (!user.getProfileStatus().getProfileStatusId().equals(ProfileStatus.active)) {
//            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
//        }
//        Set<Authority> roles=user.getAuthorities();
//        List<GrantedAuthority> grantedAuthorities =new ArrayList<>();
//        for(Authority role:roles){
//            Set<Permission> permissions=role.getPermissions();
//            for(Permission permission:permissions){
//                SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(permission.getName());
//                grantedAuthorities.add(simpleGrantedAuthority);
//            }
//        }
////        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
////            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
////            .collect(Collectors.toList());
//        return new org.springframework.security.core.userdetails.User(user.getUserId(),
//            user.getPassword(),
//            grantedAuthorities);
//    }
//    
}