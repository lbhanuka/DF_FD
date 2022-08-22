package com.epic.brokerservice.services;

import com.epic.brokerservice.bean.FinacleRequestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

//@Service
public class EmailService {

    /*@Autowired
    FdService fdService;

    public ResponseEntity<?> getCustomerDetails(HashMap<String, String> request) {

        FinacleRequestBean requestBean = new FinacleRequestBean();
        requestBean.setApp_id("APP");
        requestBean.setRequest_id("TDA_CUS_INQ");

        HashMap<String,String> requestData = new HashMap<>();

        requestData.put("ReqType","INQ");
        requestData.put("InqType",request.get("inqType"));
        requestData.put("InqVal",request.get("inqValue"));

        requestBean.setRequest_data(requestData);

        ResponseEntity<?> response = fdService.getResponse(fdService.finacleURL, requestBean, fdService.finacleToken);

        if(response.getStatusCode() == HttpStatus.UNAUTHORIZED){
            ResponseEntity<?> responseNew = fdService.getResponse(fdService.finacleURL, requestBean, fdService.getToken());
            return responseNew;
        }

        return response;
    }*/

}
