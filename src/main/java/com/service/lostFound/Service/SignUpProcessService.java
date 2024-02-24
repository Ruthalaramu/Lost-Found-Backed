package com.service.lostFound.Service;

import com.service.lostFound.DTO.SignUpDto;
import com.service.lostFound.DTO.SignUpRequestOtp;
import com.service.lostFound.Model.SignUp;
import com.service.lostFound.Repository.SignUpProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

@Service
public class SignUpProcessService {
    public SignUpProcessService() {
    }

    @Autowired
    private SignUpProcessRepository signUpProcessRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<String> createAccount(SignUpDto signUpDto) {
        try {
            SignUp signUp= new SignUp();
            signUp.setMobileNumber(signUpDto.getMobileNumber());
            signUp.setEmail(signUpDto.getEmail());
            signUp.setFirstName(signUpDto.getFirstName());
            signUp.setLastName(signUpDto.getLastName());
            signUp.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            signUp.setIsActive(Boolean.TRUE);
            LocalDate today = LocalDate.now();
            if(signUpDto.getIsAdmin()) {
                signUp.setIsAdmin(Boolean.TRUE);
            }else{
                signUp.setIsAdmin(Boolean.FALSE);
            }
            signUpProcessRepository.save(signUp);
            return  new ResponseEntity<>("sucessfully saved", HttpStatus.CREATED);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
    }



}
