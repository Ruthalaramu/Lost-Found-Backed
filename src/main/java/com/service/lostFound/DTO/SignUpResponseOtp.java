package com.service.lostFound.DTO;

import com.service.lostFound.Model.OtpStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponseOtp {
    private OtpStatus status;
    private String message;


    public SignUpResponseOtp(OtpStatus otpStatus, String otpMessage) {
    }

    public SignUpResponseOtp() {

    }
}
