package com.dufther.config;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class AuditListener {

	
	 	@PrePersist
	    private void beforeAnyOperation(Object object) { 
		 System.out.println("beforeAnyOperation..........."+object.getClass()  + 
				 ""+object.getClass().getName());
	    }
	 	
	 	 @PreUpdate	 	 
		 @PreRemove
		 private void afterAnyOperation(Object object) { 
			 System.out.println("beforeAnyOperation..........."+object.getClass()  + 
					 ""+object.getClass().getName());
		    }  
}
