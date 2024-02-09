package com.service.lostFound.Controller;

import com.service.lostFound.DTO.SignUpDto;
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
public class SignUpProcessController {

    public SignUpProcessController() {
    }

    @Autowired
    private SignUpProcessRepository signUpProcessRepository;
    @Autowired
    private  SignUpProcessService signUpProcessService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
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
        int isAdmin=0;
        if(signUpDto!=null && signUpDto.getIsAdmin()){
            isAdmin=1;
        }
        SignUp SignUp = signUpProcessRepository.findByMobileNumber(signUpDto.getMobileNumber());
        if(SignUp==null ||SignUp.getMobileNumber()==null || SignUp.getMobileNumber().isEmpty()){
            return  signUpProcessService.createAccount(signUpDto);
        }else{
            return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User Already Register");
        }

    }
    @PostMapping("/logIn")
    public ResponseEntity <String> login(@RequestBody SignUp loginRequest){
        int isAdmin=0;
        if(loginRequest!=null && loginRequest.getIsAdmin()){
            isAdmin=1;
        }
        SignUp signUp = signUpProcessRepository.findByMobileNumber(loginRequest.getMobileNumber());
        if(signUp!=null){
            if (signUp != null && passwordEncoder.matches(loginRequest.getPassword(), signUp.getPassword())) {
                // Successfully authenticated
                return ResponseEntity.ok("Login Successful");
            } else {
                // Authentication failed
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid password");
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not found please signup first");
        }

    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> forgotPasswordUserCheck(@RequestBody SignUpDto signUpDto ){
        int isAdmin=0;
        if(signUpDto!=null && signUpDto.getIsAdmin()){
             isAdmin=1;
        }
        SignUp SignUp = signUpProcessRepository.findByMobileNumber(signUpDto.getMobileNumber());
        if(SignUp!=null){
            if(signUpDto.getPassword()!=null && !signUpDto.getPassword().isEmpty()){
                SignUp.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
                SignUp updateSignUp  = signUpProcessRepository.save(SignUp);
                return  new ResponseEntity<>("password changed", HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data Not avilable");
            }

        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user not found");
        }

    }
//    @PostMapping("/changePassword")
//    public ResponseEntity<String> updatePassword(@RequestBody SignUpDto signUpDto ){
//        try{
//            SignUp SignUp = signUpProcessRepository.findByMobileNumber(signUpDto.getMobileNumber());
//            if(SignUp!=null){
//                if(signUpDto.getPassword()!=null && !signUpDto.getPassword().isEmpty()){
//                    SignUp.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
//                    SignUp updateSignUp  = signUpProcessRepository.save(SignUp);
//                    return  new ResponseEntity<>("password changed", HttpStatus.CREATED);
//                }else{
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data Not avilable");
//                }
//
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("updation fail");
//    }

}


