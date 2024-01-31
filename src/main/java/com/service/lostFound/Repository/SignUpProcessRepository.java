package com.service.lostFound.Repository;

import com.service.lostFound.Model.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignUpProcessRepository  extends JpaRepository<SignUp,Integer> {

    @Query(value = "select * from  user where mobile_number=:mobileNumber and is_active=1",nativeQuery=true)
    SignUp findByMobileNumber(String mobileNumber);
}
