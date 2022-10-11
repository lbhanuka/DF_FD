package com.epic.brokerservice.services;

import com.epic.brokerservice.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

@Service
public class FdService {

    private static final Logger log = LoggerFactory.getLogger(FdService.class);

    @Value("${config.finacle.auth_type}")
    public String authType;

    @Value("${config.finacle.client_id}")
    public String clientId;

    @Value("${config.finacle.secret}")
    public String secret;

    @Value("${config.finacle.grant_type}")
    public String grantType;

    @Value("${config.finacle.url}")
    public String finacleURL;

    @Value("${config.finacle.token.url}")
    public String finacleTokenURL;

    private final WebClient webClient;

    public String finacleToken;

    @Autowired
    protected RestTemplate restTemplate;

    public FdService() {
        this.webClient = WebClient.create("https://stgezsavings.ezloan.lk");
    }

    /*public Mono<TokenResponseBean> getToken(String name) {
        return this.webClient.get().uri("/ezsavings/api/uaa/oauth/token", name)
                .retrieve().bodyToMono(TokenResponseBean.class);
    }*/

    public String getToken() {

        log.info("Requesting new Finacle token");
        TokenBean bean = new TokenBean();

        bean.setAuth_type(authType);
        bean.setClient_id(clientId);
        bean.setSecret(secret);
        bean.setGrant_type(grantType);

        TokenResponseBean tokenResponseBean = new TokenResponseBean();

        //ResponseEntity<?> response = this.getResponse(url, bean, null);

        /*Mono<TokenResponseBean> responseBeanMono = webClient.post()
                .uri("/ezsavings/api/uaa/oauth/token")
                .body(Mono.just(bean), TokenBean.class)
                .retrieve()
                .bodyToMono(TokenResponseBean.class);

        tokenResponseBean = responseBeanMono.block();*/
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpEntity<Object> requestEntity = new HttpEntity<>(bean);
        ResponseEntity<TokenResponseBean> responseFinacle = restTemplate.postForEntity(finacleTokenURL, requestEntity, TokenResponseBean.class);

        //HttpEntity<Object> requestEntity = new HttpEntity<>(requestParam, headers);
        if(responseFinacle.getStatusCode() == HttpStatus.OK){
            log.info("New Finacle token received");
            tokenResponseBean = responseFinacle.getBody();
        }else {
            log.error("Error response while receiving new Finacle token. HTTP CODE: " + responseFinacle.getStatusCode());
        }

        this.finacleToken = "Bearer " + tokenResponseBean.getAccess_token();

        return finacleToken;
    }

    public ResponseEntity<?> getSavingsAccountList(HashMap<String,String> inputs){

        FinacleRequestBean requestBean = new FinacleRequestBean();
        requestBean.setApp_id("APP");
        requestBean.setRequest_id("TDA_SAV_ACCT_INQ");

        HashMap<String,String> requestData = new HashMap<>();

        requestData.put("ReqType","INQ");
        requestData.put("InqType",inputs.get("inqType"));
        requestData.put("InqVal",inputs.get("inqValue"));

        requestBean.setRequest_data(requestData);
        log.info("Calling Finacle savings details API");
        log.info("Finacle savings details API request data : " +  requestData);
        ResponseEntity<?> response = getResponse(finacleURL, requestBean, this.finacleToken);

        if(response.getStatusCode() == HttpStatus.UNAUTHORIZED){
            log.info("Finacle responded with : 401 UNAUTHORIZED");
            log.debug("Calling Finacle savings details API on : " + finacleURL + " with new token");
            ResponseEntity<?> responseNew = getResponse(finacleURL, requestBean, this.getToken());
            log.info("Finacle savings details API response : " +  responseNew.getBody());
            return responseNew;
        }

        return response;
    }

    public ResponseEntity<?> getFDAccountList(HashMap<String,String> inputs){

        FinacleRequestBean requestBean = new FinacleRequestBean();
        requestBean.setApp_id("APP");
        requestBean.setRequest_id("FD_INQ");

        HashMap<String,String> requestData = new HashMap<>();

        requestData.put("ReqType","INQ");
        requestData.put("InqType",inputs.get("inqType"));
        requestData.put("InqVal",inputs.get("inqValue"));

        requestBean.setRequest_data(requestData);

        log.info("Calling Finacle FD details API");
        log.info("Finacle FD details API request data : " +  requestData);
        ResponseEntity<?> response = getResponse(finacleURL, requestBean, this.finacleToken);

        if(response.getStatusCode() == HttpStatus.UNAUTHORIZED){
            log.info("Finacle responded with : 401 UNAUTHORIZED");
            log.debug("Calling Finacle FD details API on : " + finacleURL + " with new token");
            ResponseEntity<?> responseNew = getResponse(finacleURL, requestBean, this.getToken());
            log.info("Finacle FD details API response : " +  responseNew.getBody());
            return responseNew;
        }

        return response;
    }

    public ResponseEntity<?> getResponse(String url, Object requestParam, String authString) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", authString);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestParam, headers);

        try {
            ResponseEntity<String> responseFromService = restTemplate.postForEntity(url, requestEntity, String.class);

            ResponseEntity<?> response = new ResponseEntity<>(responseFromService.getBody(),HttpStatus.OK);
            log.info("Finacle call OK");
            return response;
        } catch(HttpStatusCodeException e) {
            log.error("Finacle call INVALID HTTP CODE: " + e.getMessage());
            ResponseEntity<?> response = new ResponseEntity<>(e.getResponseBodyAsString(),e.getStatusCode());
            return response;
        }
    }

    public ResponseEntity<?> getFdInterestCalculation(HashMap<String, String> inputs) {
        FinacleRequestBean requestBean = new FinacleRequestBean();
        requestBean.setApp_id("APP");
        requestBean.setRequest_id("FD_Calc");

        HashMap<String,String> requestData = new HashMap<>();

        requestData.put("DepositAmt",inputs.get("depositAmt"));
        requestData.put("ProductCode",inputs.get("productCode"));
        requestData.put("Period",inputs.get("period"));
        requestData.put("IntMethod",inputs.get("intMethod"));

        requestBean.setRequest_data(requestData);

        log.info("Calling Finacle FD calculation API");
        ResponseEntity<?> response = getResponse(finacleURL, requestBean, this.finacleToken);

        if(response.getStatusCode() == HttpStatus.UNAUTHORIZED){
            log.info("Finacle responded with : 401 UNAUTHORIZED");
            ResponseEntity<?> responseNew = getResponse(finacleURL, requestBean, this.getToken());
            return responseNew;
        }

        return response;
    }

    public ResponseEntity<?> createFdAccount(HashMap<String, String> inputs) {
        FinacleRequestBean requestBean = new FinacleRequestBean();
        requestBean.setApp_id("APP");
        requestBean.setRequest_id("FDA_Open");

        HashMap<String,String> requestData = new HashMap<>();

        requestData.put("schmCode",inputs.get("schmCode"));
        requestData.put("mainCif",inputs.get("mainCif"));
        requestData.put("IntrestRate",inputs.get("intrestRate"));
        requestData.put("depAmnt",inputs.get("depAmnt"));
        requestData.put("depPerdInMths",inputs.get("depPerdInMths"));
        requestData.put("depPerdInDays",inputs.get("depPerdInDays"));
        requestData.put("autoRenewalFlg",inputs.get("autoRenewalFlg"));
        requestData.put("autoRenewalMethod",inputs.get("autoRenewalMethod"));
        requestData.put("autoRenewPerdMths",inputs.get("autoRenewPerdMths"));
        requestData.put("autoRenewPerdDays",inputs.get("autoRenewPerdDays"));
        requestData.put("operativeAcid",inputs.get("operativeAcid"));
        requestData.put("repaymentAcid",inputs.get("repaymentAcid"));
        requestData.put("freeTextField1",inputs.get("freeTextField1"));

        requestBean.setRequest_data(requestData);

        log.info("Calling Finacle FD create API");
        ResponseEntity<?> response = getResponse(finacleURL, requestBean, this.finacleToken);

        if(response.getStatusCode() == HttpStatus.UNAUTHORIZED){
            log.info("Finacle responded with : 401 UNAUTHORIZED");
            ResponseEntity<?> responseNew = getResponse(finacleURL, requestBean, this.getToken());
            return responseNew;
        }

        return response;
    }

    public ResponseEntity<?> getFinacleCustomerDetails(HashMap<String, String> request) {

        FinacleRequestBean requestBean = new FinacleRequestBean();
        requestBean.setApp_id("APP");
        requestBean.setRequest_id("TDA_CUS_INQ");

        HashMap<String,String> requestData = new HashMap<>();

        requestData.put("ReqType","INQ");
        requestData.put("InqType",request.get("inqType"));
        requestData.put("InqVal",request.get("inqValue"));

        requestBean.setRequest_data(requestData);

        log.info("Calling Finacle customer details API");
        ResponseEntity<?> response = getResponse(finacleURL, requestBean, this.finacleToken);

        if(response.getStatusCode() == HttpStatus.UNAUTHORIZED){
            log.info("Finacle responded with : 401 UNAUTHORIZED");
            ResponseEntity<?> responseNew = getResponse(finacleURL, requestBean, this.getToken());
            return responseNew;
        }

        return response;
    }
}
