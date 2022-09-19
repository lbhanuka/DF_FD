package com.epic.fdservice.services;

import com.epic.fdservice.models.CrmFdDetailsBean;
import com.epic.fdservice.models.FdDetailsRequestBean;
import com.epic.fdservice.models.FdInstructionsResponseBean;
import com.epic.fdservice.models.FdRatesResponseBean;
import com.epic.fdservice.models.*;
import com.epic.fdservice.persistance.entity.FdDetailsEntity;
import com.epic.fdservice.persistance.entity.FdProductsEntity;
import com.epic.fdservice.persistance.entity.ShMobileUserEntity;
import com.epic.fdservice.persistance.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang.WordUtils.capitalize;

@Service
public class FdService {

    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Autowired
    FdDetailsRepo fdDetailsRepo;

    @Autowired
    FdInstructionsRepo fdInstructionsRepo;

    @Autowired
    FdRatesRepo fdRatesRepo;

    @Autowired
    FdInstructionImagesRepo fdInstructionImagesRepo;

    @Autowired
    FdProductsRepo fdProductsRepo;

    @Autowired
    MobileUserRepo mobileUserRepo;

    @Autowired
    CommonParamRepo commonParamRepo;

    @Autowired
    Validator validator;

    final String FINACLE_DOB_FORMAT = "dd-MMM-yyyy";
    final String PDF_PASSWORD_DOB_FORMAT = "ddMMyyyy";


    private static final Logger log = LoggerFactory.getLogger(FdService.class);

    public Map<String, Object> getFdDetails(FdDetailsRequestBean requestBean) {
        Map<String, Object> map = new HashMap<>();
        List<CrmFdDetailsBean> resultList = null;

        Set<ConstraintViolation<FdDetailsRequestBean>> violations = validator.validate(requestBean);

        if (!violations.isEmpty()) {
            StringBuffer Msg = new StringBuffer();
            for(ConstraintViolation<FdDetailsRequestBean> violation : violations){
                //Msg.violation.getMessage());
                if(!Msg.toString().isEmpty()){
                    Msg.append("|");
                }

                Msg.append(violation.getMessage().replace("{value}", violation.getInvalidValue() == null ? "null" : violation.getInvalidValue().toString()));
            }
            map.put("MESSAGE",Msg);
            map.put("STATUS","BAD REQUEST");
            return map;
        }

        if(requestBean.getIdentificationType().equals("NIC")){
            resultList = fdDetailsRepo.findByNic(requestBean.getIdentificationNumber());
        }else if (requestBean.getIdentificationType().equals("CIF")){
            resultList = fdDetailsRepo.findByCif(requestBean.getIdentificationNumber());
        }

        if(resultList != null && !resultList.isEmpty()){

            map.put("MESSAGE","FD DETAILS FETCHED");
            map.put("STATUS","SUCCESS");
            map.put("DATA",resultList);
        } else {
            map.put("MESSAGE","NO FD DETAILS FOUND");
            map.put("STATUS","FAIL");
        }

        return map;
    }

    public Map<String, Object> getFdDetailsFinacle(FdDetailsFinacleRequestBean requestBean) {
        Map<String, Object> map = new HashMap<>();
        List<CrmFdDetailsBean> resultList = null;

        Set<ConstraintViolation<FdDetailsFinacleRequestBean>> violations = validator.validate(requestBean);

        if (!violations.isEmpty()) {
            StringBuffer Msg = new StringBuffer();
            for(ConstraintViolation<FdDetailsFinacleRequestBean> violation : violations){
                //Msg.violation.getMessage());
                if(!Msg.toString().isEmpty()){
                    Msg.append("|");
                }

                Msg.append(violation.getMessage().replace("{value}", violation.getInvalidValue() == null ? "null" : violation.getInvalidValue().toString()));
            }
            map.put("MESSAGE",Msg);
            map.put("STATUS","BAD REQUEST");
            return map;
        }

        return callToBrokerService(requestBean);
    }

    private Map<String, Object> callToBrokerService(FdDetailsFinacleRequestBean request){

        Map<String, Object> response = new HashMap<>();
        HashMap<String,String> requestData = new HashMap<>();

        if(request.getInqType().equals("DEVID")){
            MobileUserBean mobileUserBean = fdDetailsRepo.getNicByDeviceId(request.getInqValue());
            if(mobileUserBean != null){
                requestData.put("inqType","NIC");
                requestData.put("inqValue",mobileUserBean.getNic());
            }else {
                response.put("MESSAGE","INVALID DEVICE ID");
                response.put("STATUS","BAD REQUEST");
                return response;
            }
        } else if (request.getInqType().equals("NIC") || request.getInqType().equals("CIF")) {
            requestData.put("inqType",request.getInqType());
            requestData.put("inqValue",request.getInqValue());
        } else {
            response.put("MESSAGE","INVALID INQ TYPE");
            response.put("STATUS","BAD REQUEST");
            return response;
        }

        String url = "http://BROKER-SERVICE/fd/details";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestData, headers);

        try {
            ResponseEntity<FinacleFdDetailsResponseBean> responseFromService = restTemplate.postForEntity(url, requestEntity, FinacleFdDetailsResponseBean.class);
            //ResponseEntity<String> responseFromService = restTemplate.postForEntity(url, requestEntity, String.class);
            response.put("STATUS","SUCCESS");
            response.put("DATA",responseFromService.getBody().getRESPONSE_DATA());
            response.put("MESSAGE","FD DETAILS FETCHED");

            if(responseFromService.getBody().getSTATUS() != null && responseFromService.getBody().getSTATUS().equals("SUCCESS")){
                response.put("STATUS","SUCCESS");
                response.put("DATA",responseFromService.getBody().getRESPONSE_DATA());
                response.put("MESSAGE","FD DETAILS FETCHED");
            } else {
                response.put("STATUS","FAILED");
                response.put("DATA",null);
                response.put("MESSAGE","FINACLE MSG: " + responseFromService.getBody().getMESSAGE());
            }
            return response;
        } catch(HttpStatusCodeException e) {
            response.put("STATUS","FAILED");
            response.put("MESSAGE",e.getResponseBodyAsString());
            return response;
        }
    }

    public Map<String, Object> getAllActiveInstructions(String language) {
        Map<String, Object> map = new HashMap<>();
        List<FdInstructionsResponseBean> resultList = null;

        if(language != null && language.equals("E")){
            resultList = fdInstructionsRepo.getAllInstructionsEnglish("ACT");//get all active instructions
        }else if (language != null && language.equals("S")){
            resultList = fdInstructionsRepo.getAllInstructionsSinhala("ACT");//get all active instructions
        }else if (language != null && language.equals("T")) {
            resultList = fdInstructionsRepo.getAllInstructionsTamil("ACT");//get all active instructions
        }else {
            map.put("MESSAGE", "INVALID LANGUAGE");
            map.put("STATUS","BAD REQUEST");
            return map;
        }

        if(resultList != null && !resultList.isEmpty()){

            map.put("MESSAGE","FD OPENING INSTRUCTION DETAILS FETCHED");
            map.put("STATUS","SUCCESS");
            map.put("DATA",resultList);
        } else {
            map.put("MESSAGE","NO ACTIVE FD OPENING INSTRUCTIONS FOUND");
            map.put("STATUS","FAIL");
        }

        return map;
    }

    public Map<String, Object> getFdRates(String type) {

        Map<String, Object> map = new HashMap<>();
        List<FdRatesResponseBean> resultList = null;

        if(type != null && type.equals("DIGITAL")){
            resultList = fdRatesRepo.getFdRatesByType("DIGITAL");
        }else if (type != null && type.equals("SENIOR_CITIZEN")){
            resultList = fdRatesRepo.getFdRatesByType("SENIOR_CITIZEN");
        }else {
            map.put("MESSAGE", "INVALID TYPE");
            map.put("STATUS","BAD REQUEST");
            return map;
        }

        if(resultList != null && !resultList.isEmpty()){

            Collections.sort(resultList, new Comparator<FdRatesResponseBean>() {
                        @Override
                        public int compare(FdRatesResponseBean o1, FdRatesResponseBean o2) {
                            int compare = Integer.compare(Integer.parseInt(o1.getPeriod()), Integer.parseInt(o2.getPeriod()));
                            return compare;
                        }
                    }
            );

            map.put("MESSAGE","FD RATES DETAILS FETCHED");
            map.put("STATUS","SUCCESS");
            map.put("DATA",resultList);
        } else {
            map.put("MESSAGE","NO ACTIVE FD RATES FOUND");
            map.put("STATUS","FAIL");
        }

        return map;

    }

    public ResponseEntity<?> createFdAccount(FdCreateRequestBean request) {
        FdDetailsEntity entity = new FdDetailsEntity();
        ResponseEntity<?> response = null;
        ShMobileUserEntity mobileUserEntity = new ShMobileUserEntity();
        PushNotificationRequestBean pushNotificationRequestBean = new PushNotificationRequestBean();
        try {
            mobileUserEntity = mobileUserRepo.findByDeviceid(request.getDeviceId());
            entity.setAmount(new BigDecimal(request.getDepAmnt()));
            entity.setCif(request.getMainCif());
            entity.setInterestcreditaccount(request.getRepaymentAcid());
            entity.setMaturitycreditaccount(request.getRepaymentAcid());
            entity.setRate(request.getIntrestRate());
            entity.setProductcode(request.getSchmCode());
            entity.setSchemecode(request.getSchmCode());
            entity.setRenewalinstruction(request.getRenewInstructions());
            entity.setTenure(Integer.parseInt(request.getDepPerdInMths()));
            entity.setTermversion(request.getTCVersion());
            entity.setStatus("FDFAIL");

            entity.setNic(mobileUserEntity.getIdnumber());
            String maxId = fdDetailsRepo.getMaxId();
            Integer nextId = Integer.parseInt(maxId) + 1;
            entity.setRequestid(nextId.toString());
            fdDetailsRepo.save(entity);

            HashMap<String,String> finacleResponse = callToBrokerService(request);

            if(finacleResponse.get("STATUS").equals("SUCCESS")){
                //send push notification
                entity.setFdaccountnumber(finacleResponse.get("ACCOUNTNO"));
                entity.setStatus("FDSUCCESS");
                fdDetailsRepo.save(entity);

                pushNotificationRequestBean.setMobileNumber(mobileUserEntity.getMobilenumber());
                pushNotificationRequestBean.setMessageType("FD_CREATE");

                this.perfomePostFDFunctions(request,pushNotificationRequestBean,mobileUserEntity,finacleResponse.get("ACCOUNTNO"),entity);

            } else {
                HashMap<String,String> messageParams = new HashMap<>();
                String retryCount = commonParamRepo.getValueByID("FD_RESEND_TIMEOUT_IN_MINUTES");
                messageParams.put("count",retryCount);
                pushNotificationRequestBean.setMobileNumber(mobileUserEntity.getMobilenumber());
                pushNotificationRequestBean.setMessageParams(messageParams);
                pushNotificationRequestBean.setMessageType("FD_CREATE_FAIL");
                this.sendPushNotification(pushNotificationRequestBean);
                this.sendSms(mobileUserEntity.getMobilenumber(),prepareFDFailSms(retryCount));
                this.sendFailureEmail(request,mobileUserEntity);
            }
            response = new ResponseEntity<>(finacleResponse,HttpStatus.OK);

        } catch (Exception ex){
            log.info("Error while processing FD creation request for CIF: " + request.getMainCif());
            this.sendFailureEmail(request,mobileUserEntity);
            log.info("Failure email sent to DF Support Team for CIF: " + request.getMainCif());
            HashMap<String,String> responseBody = new HashMap<>();
            responseBody.put("STATUS","FAILED");
            responseBody.put("MESSAGE","FD CREATION FAILED");
            response = new ResponseEntity<>(responseBody,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    private String prepareFDSuccessSms(String accountNumber){
        String smsBody = "Congratulations! Your Fixed Deposit is successfully opened. Your Account No is " + accountNumber +
                ". Take control of your account and enjoy many other benefits via the Genie App";
        log.info("FD Creation SMS content : " + smsBody);
        return smsBody;
    }

    private String prepareFDFailSms( String retryCount){
        String smsBody = "Dear Customer, your request for FD account creation failed due to a technical issue. Please try again after " + retryCount + " minutes. Thank you.";
        log.info("FD Fail SMS content : " + smsBody);
        return smsBody;
    }

    @Async
    public void sendSms(String mobileNumber, String message) {
        String url = "http://COMMON-SERVICE/common/send/sms";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HashMap<String,String> request = new HashMap<>();
        request.put("mobileNumber",this.prepareMobileNumberForSMS(mobileNumber));
        request.put("message",message);

        HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<PushNotificationResponseBean> responseFromService = restTemplate.postForEntity(url, requestEntity, PushNotificationResponseBean.class);
            //ResponseEntity<String> responseFromService = restTemplate.postForEntity(url, requestEntity, String.class);
            log.info("SMS notification response status : " + responseFromService.getBody().getSTATUS());
            log.debug(responseFromService.getBody().toString());
        } catch(HttpStatusCodeException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private String prepareMobileNumberForSMS(String mobileNumber) {
        String result = "tel:+94";
        if(mobileNumber != null && mobileNumber.length() > 8){
            result = result + mobileNumber.substring(mobileNumber.length() - 9);
            log.info("Mobile number for SMS notification: " + result);
        } else {
            log.info("Invalid mobile number received for SMS notification as: " + mobileNumber);
        }
        return result;
    }

    @Async
    public void sendEmail(FdCreateRequestBean request, String accountNo, ShMobileUserEntity mobileUserEntity, LinkedHashMap<String,String> fd) throws ParseException {

        EmailRequestBean emailRequestBean = new EmailRequestBean();

        HashMap<String,String> parameters = new HashMap<>();

        parameters.put("customerName", mobileUserEntity.getName());
        parameters.put("customerNic",mobileUserEntity.getIdnumber());
        parameters.put("customerAddress",prepareAddress(mobileUserEntity));
        parameters.put("fdAccountNumber",fd.get("AccountNo"));
        parameters.put("depositAmount",new BigDecimal(fd.get("CurrDepositAmt")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        parameters.put("depositPeriod",fd.get("DepositPeriodMnths"));
        parameters.put("interestRate",new BigDecimal(fd.get("IntRate")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        parameters.put("monthlyMaturity",fd.get("Intpayfreq"));
        parameters.put("openDate",fd.get("AcctOpnDate"));
        parameters.put("maturityDate",fd.get("MaturityDate"));
        parameters.put("maturityAmount",new BigDecimal(fd.get("MaturityAmt")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        String newDateString;

        SimpleDateFormat sdf = new SimpleDateFormat(FINACLE_DOB_FORMAT);
        Date d = sdf.parse(mobileUserEntity.getDateOfBirth());
        sdf.applyPattern(PDF_PASSWORD_DOB_FORMAT);
        newDateString = sdf.format(d);

        parameters.put("dateOfBirth",newDateString);
        emailRequestBean.setEmailType("FD_CREATION_SUCCESS_CUSTOMER");
        emailRequestBean.setEmailTo(mobileUserRepo.findEmailByDeviceid(request.getDeviceId()));
        emailRequestBean.setParameters(parameters);
        log.info("Sending FD Creation Email request to common-service for email address: " + emailRequestBean.getEmailTo());
        this.callToCommonService(emailRequestBean);

    }

    private String prepareAddress(ShMobileUserEntity mobileUserEntity) {
        String address1 = mobileUserEntity.getAddress1() != null ? mobileUserEntity.getAddress1()+", " : "";
        String address2 = mobileUserEntity.getAddress2() != null ? mobileUserEntity.getAddress2()+", " : "";
        String address3 = mobileUserEntity.getAddress3() != null ? mobileUserEntity.getAddress3()+"." : "";

        String address = address1 + address2 + address3;

        return capitalize(address);
    }

    @Async
    public void sendFailureEmail(FdCreateRequestBean request,ShMobileUserEntity mobileUserEntity) {

        EmailRequestBean emailRequestBean = new EmailRequestBean();
        HashMap<String,String> parameters = new HashMap<>();
        FdProductsEntity productEntity = fdProductsRepo.findByProductCode(request.getSchmCode());

        parameters.put("customerNic",mobileUserEntity.getIdnumber());
        parameters.put("CIF",request.getMainCif());
        parameters.put("savingsAccount",request.getRepaymentAcid());
        if(productEntity.getProductType().equals("NSENIOR")){
            parameters.put("productType","NON-SENIOR");
        }else {
            parameters.put("productType","SENIOR");
        }
        parameters.put("schemeCode",request.getSchmCode());
        parameters.put("mobileNumber",mobileUserEntity.getMobilenumber());
        parameters.put("customerName",mobileUserEntity.getName());
        parameters.put("depositAmount",new BigDecimal(request.getDepAmnt()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        parameters.put("interestRate",new BigDecimal(request.getIntrestRate()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        parameters.put("depositPeriod",request.getDepPerdInMths());
        parameters.put("interestPayableMode",productEntity.getInterestType());
        parameters.put("monthlyOrMaturityInterest","--");

        emailRequestBean.setEmailType("FD_CREATION_FAILURE_IT");
        emailRequestBean.setEmailTo(commonParamRepo.getValueByID("CM_IT_EMAIL"));
        emailRequestBean.setParameters(parameters);
        log.info("Sending FD Failure Email request to common-service for CIF: " + request.getMainCif());
        this.callToCommonService(emailRequestBean);

    }

    private Map<String, Object> callToCommonService(EmailRequestBean request){

        Map<String, Object> response = new HashMap<>();

        String url = "http://COMMON-SERVICE/common/send/email";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> responseFromService = restTemplate.postForEntity(url, requestEntity, String.class);
            response.put("STATUS","SUCCESS");
            log.info("Common-service call status: SUCCESS");
            return response;
        } catch(HttpStatusCodeException e) {
            log.info("Common-service call status: FAILED");
            response.put("STATUS","FAILED");
            response.put("MESSAGE",e.getResponseBodyAsString());
            return response;
        }
    }

    @Async
    public void perfomePostFDFunctions(FdCreateRequestBean request, PushNotificationRequestBean pushNotificationRequestBean, ShMobileUserEntity mobileUserEntity, String accountno, FdDetailsEntity entity) throws ParseException {

        FdDetailsFinacleRequestBean finacleRequestBean = new FdDetailsFinacleRequestBean();

        finacleRequestBean.setInqValue(request.getMainCif());
        finacleRequestBean.setInqType("CIF");
        Map<String, Object> fdListResponse = callToBrokerService(finacleRequestBean);
        LinkedHashMap objectList = (LinkedHashMap) fdListResponse.get("DATA");
        log.info("Finacle response status for FD List :" + objectList.toString());
        ArrayList accountList = (ArrayList) objectList.get("AccountList");

        LinkedHashMap<String,String> fd = new LinkedHashMap<>();

        for (Object fd1 : accountList){
            LinkedHashMap account = (LinkedHashMap) fd1;
            if(account.get("AccountNo").equals(accountno)){
                fd = account;
                break;
            }
        }

        this.sendPushNotification(pushNotificationRequestBean);
        this.sendSms(mobileUserEntity.getMobilenumber(),prepareFDSuccessSms(accountno));
        this.sendEmail(request,accountno,mobileUserEntity,fd);

        entity.setMaturityvalue(new BigDecimal(fd.get("MaturityAmt")));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date parsedDate = dateFormat.parse(fd.get("MaturityDate"));
        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
        entity.setMaturitydate(timestamp);

        fdDetailsRepo.save(entity);
    }

    @Async
    public void sendPushNotification(PushNotificationRequestBean requestBean) {
        String url = "http://COMMON-SERVICE/common/send/inapp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBean, headers);

        try {
            ResponseEntity<PushNotificationResponseBean> responseFromService = restTemplate.postForEntity(url, requestEntity, PushNotificationResponseBean.class);
            //ResponseEntity<String> responseFromService = restTemplate.postForEntity(url, requestEntity, String.class);
            log.info("Push notification response status : " + responseFromService.getBody().getSTATUS());
            log.debug(responseFromService.getBody().toString());
        } catch(HttpStatusCodeException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private HashMap<String,String> callToBrokerService(FdCreateRequestBean request){

        HashMap<String,String> response = new HashMap<>();
        HashMap<String,String> requestData = new HashMap<>();

        requestData.put("schmCode",request.getSchmCode());
        requestData.put("mainCif",request.getMainCif());
        requestData.put("intrestRate",request.getIntrestRate());
        requestData.put("depAmnt",request.getDepAmnt());
        requestData.put("depPerdInMths",request.getDepPerdInMths());
        requestData.put("depPerdInDays","0");
        if(request.getRenewInstructions().equals("1")){ //1 - Renew capital only
            requestData.put("autoRenewalFlg","Y");
            requestData.put("autoRenewPerdMths",request.getDepPerdInMths());
            requestData.put("autoRenewalMethod","P");
        }else if(request.getRenewInstructions().equals("3")){ // 3 - Renew capital with interest
            requestData.put("autoRenewalFlg","Y");
            requestData.put("autoRenewPerdMths",request.getDepPerdInMths());
            requestData.put("autoRenewalMethod","M");
        }else { // 2 - Do not renew
            requestData.put("autoRenewalFlg","N");
            requestData.put("autoRenewPerdMths","0");
        }
        requestData.put("autoRenewPerdDays","0");
        requestData.put("operativeAcid",request.getOperativeAcid());
        requestData.put("repaymentAcid",request.getRepaymentAcid());
        requestData.put("freeTextField1","GENIE DIGITAL FD");

        String url = "http://BROKER-SERVICE/fd/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestData, headers);

        try {
            ResponseEntity<FdCreatResponseBean> responseFromService = restTemplate.postForEntity(url, requestEntity, FdCreatResponseBean.class);
            //ResponseEntity<String> responseFromService = restTemplate.postForEntity(url, requestEntity, String.class);
            log.debug(responseFromService.getBody().toString());
            response.put("STATUS", responseFromService.getBody().getSTATUS());
            if (responseFromService.getBody().getSTATUS().equals("SUCCESS") && responseFromService.getBody().getRESPONSE_DATA().get("STATUS").equals("SUCCESS")) {
                response.put("STATUS", "SUCCESS");
                response.put("ACCOUNTNO", responseFromService.getBody().getRESPONSE_DATA().get("TDACCOUT"));
                response.put("MESSAGE", "FD CREATION SUCCESS");
            } else if (responseFromService.getBody().getSTATUS().equals("SUCCESS") && responseFromService.getBody().getRESPONSE_DATA().get("STATUS").equals("FAILED")){
                response.put("STATUS", "FAILED");
                response.put("MESSAGE", "FINACLE MSG: " + responseFromService.getBody().getRESPONSE_DATA().get("ERRMSG"));
            } else {
                response.put("STATUS","FAILED");
                response.put("MESSAGE","FD CREATION FAILED");
            }
            return response;
        } catch(HttpStatusCodeException e) {
            log.error(e.getMessage());
            response.put("STATUS","FAILED");
            response.put("MESSAGE",e.getResponseBodyAsString());
            return response;
        } catch(Exception e) {
            log.error(e.getMessage());
            response.put("STATUS","FAILED");
            response.put("MESSAGE",e.getMessage());
            return response;
        }
    }

    public Map<String, Object> getFdInstructionImages(String language) {

        Map<String, Object> map = new HashMap<>();
        FdInstructionImagesResponseBean responseBean = null;

        if(language != null && language.equals("E")){
            responseBean = fdInstructionImagesRepo.getInstructionImageEnglish();
        }else if (language != null && language.equals("S")){
            responseBean = fdInstructionImagesRepo.getInstructionImageSinhala();
        }else if (language != null && language.equals("T")){
            responseBean = fdInstructionImagesRepo.getInstructionImageTamil();
        }else {
            map.put("MESSAGE", "INVALID TYPE");
            map.put("STATUS","BAD REQUEST");
            return map;
        }

        if(responseBean != null && responseBean.getImage()!=null){

            map.put("MESSAGE","FD INSTRUCTION IMAGES FETCHED");
            map.put("STATUS","SUCCESS");
            map.put("DATA",responseBean);
        } else {
            map.put("MESSAGE","NO ACTIVE FD INSTRUCTION IMAGES FOUND");
            map.put("STATUS","FAIL");
        }

        return map;

    }

    public Map<String, Object> getAllowedFdProducts(FdProductRequestBean request) {
        Map<String, Object> map = new HashMap<>();
        List<FdProduct> resultList = null;

        resultList = fdProductsRepo.getAllFdProducts("YES",request.getInterestType(), request.getProductType());

        /*if(language != null && language.equals("E")){
            resultList = fdInstructionsRepo.getAllInstructionsEnglish("ACT");//get all active instructions
        }else if (language != null && language.equals("S")){
            resultList = fdInstructionsRepo.getAllInstructionsSinhala("ACT");//get all active instructions
        }else if (language != null && language.equals("T")) {
            resultList = fdInstructionsRepo.getAllInstructionsTamil("ACT");//get all active instructions
        }else {
            map.put("MESSAGE", "INVALID LANGUAGE");
            map.put("STATUS","BAD REQUEST");
            return map;
        }*/

        if(resultList != null && !resultList.isEmpty()){

            map.put("MESSAGE","FD OPENING INSTRUCTION DETAILS FETCHED");
            map.put("STATUS","SUCCESS");
            map.put("DATA",resultList);
        } else {
            map.put("MESSAGE","NO ALLOWED FD PRODUCTS FOUND");
            map.put("STATUS","FAIL");
        }

        return map;
    }
}
