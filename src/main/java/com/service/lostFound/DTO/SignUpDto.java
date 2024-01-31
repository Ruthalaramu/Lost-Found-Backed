package com.service.lostFound.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDto {
    private  int id;
    private String mobileNumber;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String userCaptcha;
    private String generatedCaptcha;
}
