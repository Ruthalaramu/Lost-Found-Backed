package com.service.lostFound.Handler;

import com.service.lostFound.DTO.SignUpRequestOtp;
import com.service.lostFound.DTO.SignUpResponseOtp;
import com.service.lostFound.DTO.SignUpValidateOtp;
import com.service.lostFound.Service.OtpProcessService;
import org.springframework.beans.factory.annotation.Autowired;

public class OtpProcessHandler {
    public OtpProcessHandler() {
    }

    private OtpProcessService otpProcessService;

    public OtpProcessHandler(OtpProcessService otpProcessService) {
        this.otpProcessService = otpProcessService;
    }

    public SignUpResponseOtp sendingOtp(SignUpRequestOtp signUpRequestOtp) {

        return otpProcessService.sendingOtp(signUpRequestOtp);
    }
    public String validateOtp(SignUpValidateOtp signUpValidateOtp) {
        return otpProcessService.validateOtp(signUpValidateOtp);
    }
}
