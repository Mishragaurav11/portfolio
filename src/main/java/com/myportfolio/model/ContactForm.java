package com.myportfolio.model;

import lombok.Data;

@Data
public class ContactForm {

    private String name;
    private String email;
    private String subject;
    private String message;
    
    // Honeypot field
    private String company;
}

