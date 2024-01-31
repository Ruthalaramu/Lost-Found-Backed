package com.service.lostFound.Controller;

import com.service.lostFound.Handler.OtpProcessHandler;
import com.service.lostFound.DTO.SignUpRequestOtp;
import com.service.lostFound.DTO.SignUpResponseOtp;
import com.service.lostFound.DTO.SignUpValidateOtp;
import com.service.lostFound.Model.SignUp;
import com.service.lostFound.Repository.SignUpProcessRepository;
import com.service.lostFound.Service.OtpProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign-Up")
public class OtpProcessController {
    public OtpProcessController() {
    }

    private OtpProcessHandler otpProcessHandler;

    public OtpProcessController(OtpProcessHandler otpProcessHandler) {
        this.otpProcessHandler = otpProcessHandler;
    }
    @Autowired
    private OtpProcessService otpProcessService;
    @Autowired
    private SignUpProcessRepository signUpProcessRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @PostMapping("/sendOtp")
    public ResponseEntity<String> sendOtp(@RequestBody SignUpRequestOtp signUpRequestOtp){
        SignUpResponseOtp signUpResponseOtp= otpProcessService.sendingOtp(signUpRequestOtp) ;
        if(signUpResponseOtp!=null){
            return ResponseEntity.status(HttpStatus.OK).body("otp success");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");

        }

    }
    @PostMapping("/logInRequestValidation")
    public  ResponseEntity<String> validationRequestOtp(@RequestBody SignUpRequestOtp signUpRequestOtp){
        SignUp SignUp = signUpProcessRepository.findByMobileNumber(signUpRequestOtp.getMobileNumber());
        if(SignUp!=null){
            if(passwordEncoder.matches(signUpRequestOtp.getPassword(), SignUp.getPassword())){
                //SignUpResponseOtp signUpResponseOtp= otpProcessService.sendingOtp(signUpRequestOtp) ;
                return ResponseEntity.status(HttpStatus.OK).body("user verified");
//                if(signUpResponseOtp!=null){
//                    return ResponseEntity.status(HttpStatus.OK).body("otp success");
//                }else{
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("otp sending Fail");
//                }

            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("incorrect password");
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user not found");
        }
    }
    @PostMapping("/validateOtp")
    public  ResponseEntity<String >validateOtp(@RequestBody SignUpValidateOtp signUpValidateOtp){
        String otpStatus= otpProcessService.validateOtp(signUpValidateOtp);
        if(otpStatus!=null && !otpStatus.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(otpStatus);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("otp process failed");
        }

    }

}
