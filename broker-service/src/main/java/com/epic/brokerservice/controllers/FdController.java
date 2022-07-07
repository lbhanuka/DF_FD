package com.epic.brokerservice.controllers;

import com.epic.brokerservice.services.FdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class FdController {

    @Autowired
    FdService fdService;

    @PostMapping(value = "/savings/details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSavingsList(@RequestBody HashMap<String,String> request) {
        return fdService.getSavingsAccountList(request);
    }

    @PostMapping(value = "/fd/calculation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFdInterestCalculation(@RequestBody HashMap<String,String> request) {
        return fdService.getFdInterestCalculation(request);
    }

    @PostMapping(value = "/fd/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFdAccount(@RequestBody HashMap<String,String> request) {
        return fdService.createFdAccount(request);
    }
}
