package com.service.lostFound.Controller;

import com.service.lostFound.DTO.SignUpDto;
import com.service.lostFound.DTO.SignUpRequestOtp;
import com.service.lostFound.Handler.OtpProcessHandler;
import com.service.lostFound.Handler.SignUpProcessHandler;
import com.service.lostFound.Model.SignUp;
import com.service.lostFound.Repository.SignUpProcessRepository;
import com.service.lostFound.Service.SignUpProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signUpProcess")
@CrossOrigin(origins = "http://localhost:3000")
public class SignUpProcessController {

    public SignUpProcessController() {
    }

    @Autowired
    private SignUpProcessRepository signUpProcessRepository;
    @Autowired
    private  SignUpProcessService signUpProcessService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/validationCheck")
    public  ResponseEntity<String> validationCheck(@RequestBody SignUpDto signUpDto){
        System.out.println("User Captcha: " + signUpDto.getUserCaptcha());
        System.out.println("Generated Captcha: " + signUpDto.getGeneratedCaptcha());
        System.out.println("mobile:"+signUpDto.getMobileNumber());
        SignUp SignUp = signUpProcessRepository.findByMobileNumber(signUpDto.getMobileNumber());
        if(SignUp==null){
            if(signUpDto.getUserCaptcha().equals(signUpDto.getGeneratedCaptcha())){
                return  ResponseEntity.status(HttpStatus.OK).body("user verfied");
            }else{
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid captcha");
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user already register");
        }
    }
    @PostMapping("/signUp")
    public ResponseEntity<String> createAccount(@RequestBody SignUpDto signUpDto){
        System.out.println(signUpDto.getMobileNumber());
        System.out.println(signUpDto.getPassword());
        SignUp SignUp = signUpProcessRepository.findByMobileNumber(signUpDto.getMobileNumber());
        if(SignUp==null ||SignUp.getMobileNumber()==null || SignUp.getMobileNumber().isEmpty()){
            return  signUpProcessService.createAccount(signUpDto);
        }else{
            return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User Already Register");
        }

    }
    @PostMapping("/logIn")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity <String> login(@RequestBody SignUp loginRequest){
        SignUp signUp = signUpProcessRepository.findByMobileNumber(loginRequest.getMobileNumber());
        if(signUp!=null){
            if (signUp != null && passwordEncoder.matches(loginRequest.getPassword(), signUp.getPassword())) {
                // Successfully authenticated
                return ResponseEntity.ok("Login Successful");
            } else {
                // Authentication failed
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid password");
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not found please signup first");
        }

    }

}


