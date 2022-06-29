package com.epic.authservice.controllers;

import com.epic.authservice.bean.*;
import com.epic.authservice.services.AuthService;
import com.epic.authservice.util.logs.LogFileCreator;
import com.epic.authservice.services.CommonService;
import com.epic.authservice.util.varlist.MessageVarList;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Environment env;

    @Autowired
    private CommonService commonService;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/token", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppAccessToken(@RequestBody @Valid AccessTokenRequestBean requestBean){

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
    public ResponseEntity<?> checkUserAvailibility(@RequestBody UserAvailibilityRequestBean requestBean,
                                                   @PathVariable("service") String serviceType){

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

        ResponseEntity<?> responseEntity;

        String result = authService.checkTokenValidity(authString);

        responseEntity = new ResponseEntity<>(result, HttpStatus.OK);

        return responseEntity;
    }

    @RequestMapping(value = "/obtain/jwt", method = RequestMethod.POST)
    public ResponseBean getJsonWebToken(@RequestHeader(value = "authorization") String authString){
        System.out.println("------------------------------------------------>> Token Controller.getJsonWebToken");
        String token;
        ResponseBean responseBean = new ResponseBean();

        try {

            if (commonService.isAuthorisedAccess(authString)){
                token = commonService.getJWT(authString);
                Token tokenBean = new Token();
                tokenBean.setToken(token);
                responseBean.setResponse(MessageVarList.RSP_SUCCESS);
                responseBean.setContent(tokenBean);

            }else {
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
    public ResponseBean validateJsonWebToken(@RequestHeader(value = "authorization") String authString){

        HashMap<String,String> clientBean;
        ResponseBean responseBean = new ResponseBean();

        try {
            clientBean = commonService.validateJWT(authString);
            if(clientBean.get("isauthorised") != null && clientBean.get("isauthorised").equals("1")){
                responseBean.setResponse(MessageVarList.RSP_SUCCESS);
                responseBean.setContent(clientBean);
            }else {
                responseBean.setResponse(MessageVarList.RSP_NOT_AUTHORISED);
            }

        }catch (ExpiredJwtException ex){
            responseBean.setResponse(MessageVarList.RSP_TOKEN_EXPIRED);
            responseBean.setContent(null);
            LogFileCreator.writeErrorTologs(ex);
        }catch (SignatureException | MalformedJwtException | StringIndexOutOfBoundsException ex){
            responseBean.setResponse(MessageVarList.RSP_TOKEN_INVALID);
            responseBean.setContent(null);
            LogFileCreator.writeErrorTologs(ex);
        }catch (Exception ex){
            responseBean.setResponse(MessageVarList.RSP_ERROR);
            responseBean.setContent(null);
            LogFileCreator.writeErrorTologs(ex);
        }

        return responseBean;
    }
}
