package com.service.lostFound.Service;

import com.service.lostFound.Config.TwilioConfig;
import com.service.lostFound.Model.OtpStatus;
import com.service.lostFound.DTO.SignUpRequestOtp;
import com.service.lostFound.DTO.SignUpResponseOtp;
import com.service.lostFound.DTO.SignUpValidateOtp;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpProcessService {
    @Autowired
    private TwilioConfig twilioConfig;
    Map<String, OtpData> otpMap = new ConcurrentHashMap<>();

    public SignUpResponseOtp sendingOtp(SignUpRequestOtp signUpRequestOtp) {

        SignUpResponseOtp signUpResponseOtp= new SignUpResponseOtp();
        try{
            PhoneNumber to= new PhoneNumber("+91"+signUpRequestOtp.getMobileNumber())  ;
            PhoneNumber from = new PhoneNumber(twilioConfig.getTrailNumber());
            String otp= generateOtp();
            System.out.println(otp);
            OtpData otpData = new OtpData(otp, System.currentTimeMillis());
            String  otpMessage="Welcome to the Lost&Found Services use this OTP for Sign-Up verification :"+otp;
            Message message = Message.creator(to, from, otpMessage).create();
            if( message.getBody()!=null && !message.getBody().isEmpty()){
                signUpResponseOtp.setMessage(otpMessage);
                signUpResponseOtp.setStatus(OtpStatus.DELIVERD);
                otpMap.put(signUpRequestOtp.getMobileNumber(),otpData);
                System.out.println(otpMap+"size:"+otpMap.size());
            }
            return  signUpResponseOtp ;
        }catch (Exception e){
            return  signUpResponseOtp = new SignUpResponseOtp(OtpStatus.FAILED,e.getMessage());
        }
    }
    private  String generateOtp(){
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
    public String validateOtp(SignUpValidateOtp signUpValidateOtp) {
        OtpData otpData = otpMap.get(signUpValidateOtp.getMobileNumber());

        if (otpData != null && otpData.isValid(signUpValidateOtp.getUserEnterOtp())) {
            otpMap.remove(signUpValidateOtp.getMobileNumber()); // Remove the OTP after successful validation
            return "Otp success";
        } else {
            // Check if the OTP exists but has expired
            if (otpData != null && otpData.hasExpired()) {
                otpMap.remove(signUpValidateOtp.getMobileNumber()); // Remove the expired OTP
                return "Expired Otp";
            } else {
                return "Invalid Otp";
            }
        }
    }

    public SignUpResponseOtp sendSmsToSignUpUser(String mobileNumber) {
        SignUpResponseOtp signUpResponseOtp;
        try {
            signUpResponseOtp = new SignUpResponseOtp();
            PhoneNumber to = new PhoneNumber("+91" + mobileNumber);
            PhoneNumber from = new PhoneNumber(twilioConfig.getTrailNumber());
            String otpMessage = "Thank you for using Lost&Found Services your account successfully created with us.";
            Message message = Message.creator(to, from, otpMessage).create();
            if (message.getBody() != null && !message.getBody().isEmpty()) {
                signUpResponseOtp.setStatus(OtpStatus.DELIVERD);
                signUpResponseOtp.setMessage("sent SMS");
            }
            return signUpResponseOtp;
        } catch (Exception e) {
            e.printStackTrace();
            return signUpResponseOtp = new SignUpResponseOtp(OtpStatus.FAILED, e.getMessage());
        }

    }

    private static class OtpData {
        private final String otp;
        private final long creationTime;
        public OtpData(String otp, long creationTime) {
            this.otp = otp;
            this.creationTime = creationTime;
        }
        public boolean isValid( String otpCheck) { //pending here
           // SignUpValidateOtp validUserOtp= new SignUpValidateOtp();
            //otpCheck=validUserOtp.getUserEnterOtp();
            long currentTime = System.currentTimeMillis();
            long otpAge = currentTime - creationTime;
            long otpExpirationPeriod = 2 * 60 * 1000 + 30 * 1000; // 2.5 minutes in milliseconds
            return otpAge <= otpExpirationPeriod && otpCheck.equals(otp);
        }
        public boolean hasExpired() {
            long currentTime = System.currentTimeMillis();
            long otpAge = currentTime - creationTime;
            long otpExpirationPeriod = 2 * 60 * 1000 + 30 * 1000; // 2.5 minutes in milliseconds
            return otpAge > otpExpirationPeriod;
        }
    }
}
