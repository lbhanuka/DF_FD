package com.epic.common.controllers;

import com.epic.common.models.CommonParamRequestBean;
import com.epic.common.models.PushNotificationRequestBean;
import com.epic.common.models.SavingsDetailsFinacleRequestBean;
import com.epic.common.services.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    CommonService commonService;

    @RequestMapping(value = "/getparam", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getParamDetails(@RequestBody CommonParamRequestBean requestBean) throws Exception {

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
        return commonService.sendInAppPushNotification(requestBean);
    }

    @RequestMapping(value = "/savings/details", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSavingsListFromFinacle(@RequestBody SavingsDetailsFinacleRequestBean request){
        return commonService.getSavingsAccountList(request);
    }

}
