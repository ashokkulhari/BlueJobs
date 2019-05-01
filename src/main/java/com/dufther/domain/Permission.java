package com.dufther.domain;



import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "permission")
public class Permission {
	   @Id
	    @NotNull
	    @Size(max = 50)
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id",length = 50)
    private Long permissionId;
    @Column(name = "name")
    private String name ;
    @Column(name = "description")
    private String description;

    
    
   
   
}
