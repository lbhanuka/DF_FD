package com.epic.brokerservice.controllers;

import com.epic.brokerservice.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

//@RestController
public class EmailController {

    /*@Autowired
    EmailService emailService;

    @PostMapping(value = "/email/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSavingsList(@RequestBody HashMap<String,String> request) {
        return emailService.getCustomerEmail(request);
    }*/
}
