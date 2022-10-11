package com.epic.common.controllers;

import com.epic.common.models.*;
import com.epic.common.services.CommonService;
import com.epic.common.services.EmailService;
import com.epic.common.services.SmsService;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/common")
public class CommonController {

    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    CommonService commonService;

    @Autowired
    EmailService emailService;

    @Autowired
    SmsService smsService;

    @RequestMapping(value = "/getparam", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getParamDetails(@RequestBody CommonParamRequestBean requestBean) throws Exception {

        log.info("Get APP Param details request received by Common Service");
        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Map<String, Object> map = commonService.getParams(requestBean);

        if(map.get("STATUS").equals("UNAUTHORISED")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }else if (map.get("STATUS").equals("BAD REQUEST")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }else if (map.get("STATUS").equals("SUCCESS") || map.get("STATUS").equals("FAIL")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/send/inapp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendInAppPushNotification(@RequestBody PushNotificationRequestBean requestBean){
        log.info("Send Push notification request received by Common Service");
        return commonService.sendInAppPushNotification(requestBean);
    }

    @RequestMapping(value = "/savings/details/{type}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSavingsListFromFinacle(@RequestBody SavingsDetailsFinacleRequestBean request, @PathVariable("type") String type){
        log.info("Get Finacle Savings details request received by Common Service");
        return commonService.getSavingsAccountList(request,type);
    }

    @RequestMapping(value = "/send/email", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendemail(@RequestBody EmailRequestBean request) throws MessagingException, IOException, URISyntaxException {
        log.info("Send email request received by Common Service");
        return emailService.sendEmail(request);
    }

    @RequestMapping(value = "/send/sms", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendSms(@RequestBody SMSRequestBean request) {
        log.info("Send SMS request received by Common Service");
        return smsService.sendSms(request);
    }

}
