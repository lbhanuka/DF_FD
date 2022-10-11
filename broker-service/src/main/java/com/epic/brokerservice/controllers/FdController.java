package com.epic.brokerservice.controllers;

import com.epic.brokerservice.services.FdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(FdController.class);

    @PostMapping(value = "/savings/details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSavingsList(@RequestBody HashMap<String,String> request) {
        log.info("Get Finacle savings details request received by Broker Service");
        return fdService.getSavingsAccountList(request);
    }

    @PostMapping(value = "/fd/details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFdList(@RequestBody HashMap<String,String> request) {
        log.info("Get Finacle FD details request received by Broker Service");
        return fdService.getFDAccountList(request);
    }

    @PostMapping(value = "/fd/calculation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFdInterestCalculation(@RequestBody HashMap<String,String> request) {
        log.info("FD Interest calculation request received by Broker Service");
        return fdService.getFdInterestCalculation(request);
    }

    @PostMapping(value = "/customer/details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFinacleCustomerDetails(@RequestBody HashMap<String,String> request) {
        log.info("Get Finacle customer details request received by Broker Service");
        return fdService.getFinacleCustomerDetails(request);
    }

    @PostMapping(value = "/fd/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFdAccount(@RequestBody HashMap<String,String> request) {
        log.info("Finacle FD Create request received by Broker Service");
        return fdService.createFdAccount(request);
    }
}
