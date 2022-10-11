package com.epic.authservice.controllers;

import com.epic.authservice.bean.*;
import com.epic.authservice.services.AuthService;
import com.epic.authservice.util.logs.LogFileCreator;
import com.epic.authservice.services.CommonService;
import com.epic.authservice.util.varlist.MessageVarList;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private Environment env;

    @Autowired
    private CommonService commonService;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/token", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppAccessToken(@RequestBody @Valid AccessTokenRequestBean requestBean){
        log.info("Get App access token request received by Auth Service for client ID : " + requestBean.getClient_id());
        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Map<String, Object> map = authService.getAppAccessToken(requestBean);

        if(map.containsKey("error")){
            if(map.get("error").equals("unsupported_grant_type")){
                responseEntity = new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }else if (map.get("error").equals("invalid_grant")){
                responseEntity = new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
            }
        } else {
            responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        }

        return responseEntity;
    };

    @RequestMapping(value = "/availability/{service}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkUserAvailability(@RequestBody UserAvailibilityRequestBean requestBean,
                                                   @PathVariable("service") String serviceType){

        log.info("Availability request received by Auth Service");
        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Map<String, Object> map = authService.checkUserAvailibility(requestBean, serviceType);

        if(map.get("STATUS").equals("UNAUTHORISED")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }else if (map.get("STATUS").equals("BAD REQUEST")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }else if (map.get("STATUS").equals("SUCCESS")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkTokenValidity(@RequestHeader(value = "authorization") String authString){
        log.info("Token validate request received by Auth Service");
        ResponseEntity<?> responseEntity;

        String result = authService.checkTokenValidity(authString);

        responseEntity = new ResponseEntity<>(result, HttpStatus.OK);

        return responseEntity;
    }

    @RequestMapping(value = "/obtain/jwt/{sessionId}", method = RequestMethod.POST)
    public ResponseBean getJsonWebToken(@RequestHeader(value = "authorization") String authString,
                                        @RequestBody JwtRequestBean requestBean,
                                        @PathVariable("sessionId") String sessionId){
        log.info("JWT request received by Auth Service for mobile : " + requestBean.getMobileNumber() + " with DEVID : " + requestBean.getDeviceId());
        log.info("Session ID : " + sessionId + " assigned for DEVID : " + requestBean.getDeviceId());
        String token;
        ResponseBean responseBean = new ResponseBean();

        try {

            if (commonService.isAuthorisedAccess(authString, requestBean.getDeviceId())){
                log.info("Obtain JWT request authorised: OK");
                boolean success = commonService.updateMobileUser(requestBean);
                if(success){
                    log.info("Obtain JWT request mobile user update: OK");
                    token = commonService.getJWT(authString, requestBean.getDeviceId(), sessionId);
                    Token tokenBean = new Token();
                    tokenBean.setToken(token);
                    responseBean.setResponse(MessageVarList.RSP_SUCCESS);
                    responseBean.setContent(tokenBean);
                } else {
                    log.info("Obtain JWT request mobile user update: FAIL");
                    responseBean.setResponse(MessageVarList.RSP_FAIL);
                    responseBean.setContent(null);
                }

            }else {
                log.info("Obtain JWT request authorised: FAIL");
                responseBean.setResponse(MessageVarList.RSP_NOT_AUTHORISED);
                responseBean.setContent(null);
            }

            //DB log creation

        }catch (Exception ex){
            responseBean.setResponse(MessageVarList.RSP_ERROR);
            responseBean.setContent(null);
            //DB log creation
            //commonManager.createRequestLog(clientIp,serviceCode,CommonVarList.REQUEST_POST,authString,"", CommonVarList.ROSPONSE_ERROR);
            //LogFileCreator.writeErrorTologs(ex);
            ex.printStackTrace();
        }

        //responseEntity = new ResponseEntity<>("OBTAIN JWT CALL SUCCESS!!! ", HttpStatus.OK);
        return responseBean;
    }

    @RequestMapping(value = "/validate/jwt", method = RequestMethod.POST)
    public ResponseEntity<?> validateJsonWebToken(@RequestHeader(value = "authorization") String authString){
        log.info("Validate JWT request received by Auth Service");
        ResponseEntity<?> responseEntity;

        String result = commonService.validateJWT(authString);

        responseEntity = new ResponseEntity<>(result, HttpStatus.OK);

        return responseEntity;
    }
}
