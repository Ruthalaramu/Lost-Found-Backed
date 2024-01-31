package com.service.lostFound.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "user")
public class SignUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String mobileNumber;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
   // private Date createdDate;
   // private Date lastModifiedDate;
    private Boolean isActive;
    private Boolean isAdmin;



}
