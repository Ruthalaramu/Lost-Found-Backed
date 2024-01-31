package com.service.lostFound.Handler;

import com.service.lostFound.DTO.SignUpRequestOtp;
import com.service.lostFound.Service.SignUpProcessService;

public class SignUpProcessHandler {
    private SignUpProcessService signUpProcessService;

    public SignUpProcessHandler(SignUpProcessService signUpProcessService) {
        this.signUpProcessService = signUpProcessService;
    }

}
