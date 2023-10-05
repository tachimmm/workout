package com.firstProject.usefulltools.form;

import lombok.Data;

@Data
public class RegisterChangeForm {

    private String loginId;

    private String password;

    private String newPassword; 

    private String confirmPassword; 
}
