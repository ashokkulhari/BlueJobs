package com.bluejob.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bluejob.security.services.UserPrinciple;

public class AuditorAwareImpl implements AuditorAware<String> {

	
	@Override
	public Optional<String> getCurrentAuditor() {
		
//		User user  = ((UserAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId;
		System.out.println("authentication.getPrincipal()  = "+authentication.getPrincipal());
		Object principalObj= authentication.getPrincipal();
		if (authentication == null || !authentication.isAuthenticated() || !(principalObj instanceof UserPrinciple )) {
			userId = Constants.ANONYMOUS_USER;
		}else{
			UserPrinciple userdetails=(UserPrinciple) authentication.getPrincipal();
//	        System.out.println(userdetails.getUserId()+"  getCurrentAuditor "+authentication.getPrincipal().toString() 
//	        		+" authorities ="+authentication.getAuthorities());
			
			 userId = userdetails.getUserId();
		}
		
        
        return Optional.of(userId);
	}
    
}
