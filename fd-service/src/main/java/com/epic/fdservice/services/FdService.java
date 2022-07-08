package com.epic.fdservice.services;

import com.epic.fdservice.models.*;
import com.epic.fdservice.persistance.entity.FdDetailsEntity;
import com.epic.fdservice.persistance.repository.FdDetailsRepo;
import com.epic.fdservice.persistance.repository.FdInstructionsRepo;
import com.epic.fdservice.persistance.repository.FdRatesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    Validator validator;

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

        entity.setAmount(new BigDecimal(request.getDepAmnt()));
        entity.setCif(request.getMainCif());
        entity.setInterestcreditaccount(request.getRepaymentAcid());
        entity.setMaturitycreditaccount(request.getRepaymentAcid());
        entity.setRate(request.getIntrestRate());
        entity.setProductcode(request.getSchmCode());
        entity.setSchemecode(request.getSchmCode());
        entity.setRenewalinstruction(request.getRenewInstructions());
        entity.setTenure(Integer.parseInt(request.getDepPerdInMths()));

        HashMap<String,String> finacleResponse = callToBrokerService(request);

        if(finacleResponse.get("STATUS").equals("SUCCESS")){
            entity.setFdaccountnumber(finacleResponse.get("ACCOUNTNO"));
            String maxId = fdDetailsRepo.getMaxId();
            Integer nextId = Integer.parseInt(maxId) + 1;
            entity.setRequestid(nextId.toString());
            fdDetailsRepo.save(entity);
        }
        ResponseEntity<?> response = new ResponseEntity<>(finacleResponse,HttpStatus.OK);

        return response;
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
        if(request.getRenewInstructions().equals("1")){
            requestData.put("autoRenewalFlg","Y");
            requestData.put("autoRenewPerdMths",request.getDepPerdInMths());
        } else {
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
            response.put("STATUS","SUCCESS");
            response.put("ACCOUNTNO",responseFromService.getBody().getRESPONSE_DATA().get("TDACCOUT"));
            response.put("MESSAGE","FD CREATION SUCCESS");
            return response;
        } catch(HttpStatusCodeException e) {
            response.put("STATUS","FAILED");
            response.put("MESSAGE",e.getResponseBodyAsString());
            return response;
        }
    }
}
