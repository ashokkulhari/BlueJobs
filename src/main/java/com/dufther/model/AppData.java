package com.dufther.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppData {
    private String email;
    private String phone;
    private String password;
    private Long roleId;
    private Long permissionId;
}
