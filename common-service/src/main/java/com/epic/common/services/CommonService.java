package com.epic.common.services;

import com.epic.common.models.*;
import com.epic.common.persistance.repository.CommonParamRepo;
import com.epic.common.util.NicValidations;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.*;

@Service
public class CommonService {

    @Autowired
    CommonParamRepo commonParamRepo;

    @Autowired
    Validator validator;

    @Autowired
    @Qualifier("internalCalls")
    protected RestTemplate restTemplateInternal;

    @Autowired
    @Qualifier("externalCalls")
    protected RestTemplate restTemplateExternal;

    @Value("${config.push.auth_type}")
    public String authType;

    @Value("${config.push.client_id}")
    public String clientId;

    @Value("${config.push.client_secret}")
    public String clientSecret;

    @Value("${config.push.secret}")
    public String secret;

    @Value("${config.push.grant_type}")
    public String grantType;

    @Value("${config.push.url}")
    public String pushURL;

    @Value("${config.push.token.url}")
    public String pushTokenURL;

    private String pushToken;

    public Map<String, Object> getParams(CommonParamRequestBean requestBean) throws Exception {

        Map<String, Object> map = new HashMap<>();
        List<CommonParamBean> resultList = null;

        Set<ConstraintViolation<CommonParamRequestBean>> violations = validator.validate(requestBean);

        if (!violations.isEmpty()) {
            StringBuffer Msg = new StringBuffer();
            for (ConstraintViolation<CommonParamRequestBean> violation : violations) {
                //Msg.violation.getMessage());
                if (!Msg.toString().isEmpty()) {
                    Msg.append("|");
                }
                Msg.append(violation.getMessage().replace("{value}", violation.getInvalidValue() == null ? "null" : violation.getInvalidValue().toString()));
            }
            map.put("MESSAGE", Msg);
            map.put("STATUS", "BAD REQUEST");
            return map;
        }

        if (requestBean.getCategory().equals("FD")) {
            resultList = commonParamRepo.getUnderCategory(requestBean.getCategory());
        } else if (requestBean.getCategory().equals("SP")) {
            resultList = commonParamRepo.getUnderCategory(requestBean.getCategory());
        } else if (requestBean.getCategory().equals("ALL")) {
            resultList = commonParamRepo.getAll();
        }


        if (requestBean.getDeviceId() != null && !requestBean.getDeviceId().equals("") && (requestBean.getCategory().equals("ALL") || requestBean.getCategory().equals("FD"))) {

            if (resultList != null && !resultList.isEmpty()) {

                String nic = commonParamRepo.getProductType(requestBean.getDeviceId());

                if(nic != null){
                    NICType nicType = NicValidations.checkNICType(nic);

                    if (Objects.equals(nicType, NICType.OLD)) {

                        String generatedNic = NicValidations.convertOldNicTONewNic(nic);
                        setProductType(map, resultList, generatedNic);

                    } else if (Objects.equals(nicType, NICType.NEW)) {

                        setProductType(map, resultList, nic);

                    } else {
                        map.put("MESSAGE", "CANNOT VALIDATE NIC FOR DEVICE ID");
                        map.put("STATUS", "FAIL");
                    }
                }else {
                    map.put("MESSAGE", "DEVICE ID NOT VALID");
                    map.put("STATUS", "FAIL");
                }

            }else {
                map.put("MESSAGE", "NO COMMON PARAM FOUND");
                map.put("STATUS", "FAIL");
            }

            return map;
        } else {

            if (resultList != null && !resultList.isEmpty()) {
                map.put("MESSAGE", "COMMON PARAM FETCHED");
                map.put("STATUS", "SUCCESS");
                map.put("DATA", resultList);
            } else {
                map.put("MESSAGE", "NO COMMON PARAM FOUND");
                map.put("STATUS", "FAIL");
            }

            return map;
        }
    }

    private void setProductType(Map<String, Object> map, List<CommonParamBean> resultList, String nic) {
        NicValidations.NicDetails nicDetails = new NicValidations.NicDetails(nic);

        int seniorCitizenAge = 60;

        for(CommonParamBean bean : resultList){
            if (bean.getKey().equals("SENIOR_CITIZEN_AGE"))
                seniorCitizenAge = Integer.parseInt(bean.getValue());
        }

        if(NicValidations.comparePeriod(LocalDate.now(), nicDetails.getDateOfBirth(), seniorCitizenAge)){
            for (int i = 0; i < resultList.size(); i++) {
                if(resultList.get(i).getKey().equals("ALLOWED_PRODUCT")){
                    resultList.get(i).setValue("SENIOR_CITIZEN");
                }else{
                    CommonParamBean bean = new CommonParamBean("ALLOWED_PRODUCT","SENIOR_CITIZEN","DIGITAL or SENIOR_CITIZEN");
                    resultList.add(bean);
                }
            }
        }else{
            for (int i = 0; i < resultList.size(); i++) {
                if(resultList.get(i).getKey().equals("ALLOWED_PRODUCT")){
                    resultList.get(i).setValue("DIGITAL");
                }else{
                    CommonParamBean bean = new CommonParamBean("ALLOWED_PRODUCT","DIGITAL","DIGITAL or SENIOR_CITIZEN");
                    resultList.add(bean);
                }
            }
        }
        map.put("MESSAGE", "COMMON PARAM FETCHED");
        map.put("STATUS", "SUCCESS");
        map.put("DATA", resultList);
    }


    public String getToken() {

        TokenBean bean = new TokenBean();

        bean.setAuth_type(authType);
        bean.setClient_id(clientId);
        bean.setSecret(secret);
        bean.setGrant_type(grantType);
        bean.setClient_secret(clientSecret);

        TokenResponseBean tokenResponseBean = new TokenResponseBean();

        restTemplateExternal.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpEntity<Object> requestEntity = new HttpEntity<>(bean);
        ResponseEntity<TokenResponseBean> responseFinacle = restTemplateExternal.postForEntity(pushTokenURL, requestEntity, TokenResponseBean.class);

        //HttpEntity<Object> requestEntity = new HttpEntity<>(requestParam, headers);
        if(responseFinacle.getStatusCode() == HttpStatus.OK){
            tokenResponseBean = responseFinacle.getBody();
        }

        this.pushToken = "Bearer " + tokenResponseBean.getAccess_token();

        return pushToken;
    }

    public ResponseEntity<?> sendInAppPushNotification(PushNotificationRequestBean requestBean){

        ResponseEntity<?> response = getResponseExternal(pushURL, requestBean, this.pushToken);

        if(response.getStatusCode() == HttpStatus.UNAUTHORIZED){
            ResponseEntity<?> responseNew = getResponseExternal(pushURL, requestBean, this.getToken());
            return responseNew;
        }

        return response;
    }

    public ResponseEntity<?> getResponse(String url, Object requestParam, String authString) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", authString);
        restTemplateInternal.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestParam, headers);

        try {
            ResponseEntity<String> responseFromService = restTemplateInternal.postForEntity(url, requestEntity, String.class);

            ResponseEntity<?> response = new ResponseEntity<>(responseFromService.getBody(),HttpStatus.OK);
            return response;
        } catch(HttpStatusCodeException e) {
            ResponseEntity<?> response = new ResponseEntity<>(e.getResponseBodyAsString(),e.getStatusCode());
            return response;
        }
    }

    public ResponseEntity<?> getSavingsAccountList(SavingsDetailsFinacleRequestBean request, String type) {
        ResponseEntity<?>  responseFromService = null;
        ResponseEntity<?>  responseToReturn = null;
        ArrayList selectedAccountList = new ArrayList();
        if(type.equals("FD")){
            String validSavingsProducts = commonParamRepo.findParamValueById("FD_ALLOWED_SAVINGS_PRODUCTS");
            String[] validSavingsProductList = validSavingsProducts.split("\\|");

            responseFromService = this.callToBrokerService(request);

            Object  response = responseFromService.getBody();
            ObjectMapper mapper = new ObjectMapper();
            FinacleSavingsDetailsResponseBean responseBean = mapper.convertValue(response, FinacleSavingsDetailsResponseBean.class);

            if(responseBean.getRESPONSE_DATA() != null){
                ArrayList accountList = (ArrayList) responseBean.getRESPONSE_DATA().get("AccountInfo");

                for (Object sv1 : accountList){
                    LinkedHashMap account = (LinkedHashMap) sv1;
                    for (String product : validSavingsProductList){
                        if(account.get("ProductCode").equals(product)){
                            selectedAccountList.add(account);
                        }
                    }
                }
            }


            LinkedHashMap<String,Object> responseMap = new LinkedHashMap<>();
            responseMap.put("STATUS",responseBean.getSTATUS());
            responseMap.put("MESSAGE",responseBean.getMESSAGE());

            LinkedHashMap<String,Object> responseData = new LinkedHashMap<>();
            responseData.put("AccountInfo",selectedAccountList);
            responseMap.put("RESPONSE_DATA",responseData);

            responseToReturn = new ResponseEntity<>(responseMap,responseFromService.getStatusCode());


        }

        return responseToReturn;
    }

    public ResponseEntity<?> getResponseExternal(String url, Object requestParam, String authString) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", authString);
        restTemplateExternal.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestParam, headers);

        try {
            ResponseEntity<String> responseFromService = restTemplateExternal.postForEntity(url, requestEntity, String.class);

            ResponseEntity<?> response = new ResponseEntity<>(responseFromService.getBody(),HttpStatus.OK);
            return response;
        } catch(HttpStatusCodeException e) {
            ResponseEntity<?> response = new ResponseEntity<>(e.getResponseBodyAsString(),e.getStatusCode());
            return response;
        }
    }

    private ResponseEntity<?>callToBrokerService(SavingsDetailsFinacleRequestBean request){

        Map<String, Object> response = new HashMap<>();
        HashMap<String,String> requestData = new HashMap<>();

        if(request.getInqType().equals("DEVID")){
            MobileUserBean mobileUserBean = commonParamRepo.getNicByDeviceId(request.getInqValue());
            if(mobileUserBean != null){
                requestData.put("inqType","NIC");
                requestData.put("inqValue",mobileUserBean.getNic());
            }else {
                response.put("MESSAGE","INVALID DEVICE ID");
                response.put("STATUS","BAD REQUEST");
                ResponseEntity<?> responseEntity = new ResponseEntity<>(response,HttpStatus.OK);
                return responseEntity;
            }
        } else if (request.getInqType().equals("NIC")) {
            requestData.put("inqType",request.getInqType());
            requestData.put("inqValue",request.getInqValue());
        } else {
            response.put("MESSAGE","INVALID INQ TYPE");
            response.put("STATUS","BAD REQUEST");
            ResponseEntity<?> responseEntity = new ResponseEntity<>(response,HttpStatus.OK);
            return responseEntity;
        }

        String url = "http://BROKER-SERVICE/savings/details";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplateInternal.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestData, headers);

        ResponseEntity<FinacleSavingsDetailsResponseBean>  responseFromService = restTemplateInternal.postForEntity(url, requestEntity, FinacleSavingsDetailsResponseBean.class);
        return responseFromService;
    }

}


