package com.epic.common.services;

import com.epic.common.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class SmsService {

    @Value("${mife.sms.url}")
    public String mifeSmsUrl;

    @Value("${mife.sms.token.url}")
    public String mifeTokenUrl;

    @Value("${mife.sms.basicAuthHeader}")
    public String mifeSmsToken;

    @Value("${mife.sms.sender.address}")
    public String mifeSenderAddress;

    @Value("${mife.sms.sender.name}")
    public String mifeSenderName;

    private String smsToken;

    @Autowired
    @Qualifier("externalCalls")
    protected RestTemplate restTemplateExternal;

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);

    public ResponseEntity<?> sendSms(SMSRequestBean request) {

        HashMap<String,String> outboundSMSTextMessage = new HashMap<>();
        outboundSMSTextMessage.put("message",request.getMessage());
        HashMap<String,String> receiptRequest = new HashMap<>();
        receiptRequest.put("notifyURL","");
        receiptRequest.put("callbackData","");

        OutboundSMSMessageRequest outboundSMSMessageRequest = new OutboundSMSMessageRequest();
        outboundSMSMessageRequest.setSenderAddress(mifeSenderAddress);
        outboundSMSMessageRequest.getAddress()[0] = request.getMobileNumber();
        outboundSMSMessageRequest.setOutboundSMSTextMessage(outboundSMSTextMessage);
        outboundSMSMessageRequest.setReceiptRequest(receiptRequest);
        outboundSMSMessageRequest.setSenderName(mifeSenderName);

        MifeSMSRequest mifeSMSRequest = new MifeSMSRequest(outboundSMSMessageRequest);

        log.info("Calling Mife-SMS service");
        ResponseEntity<?> response = getResponseExternal(mifeSmsUrl, mifeSMSRequest, this.smsToken);

        if(response.getStatusCode() == HttpStatus.UNAUTHORIZED){
            ResponseEntity<?> responseNew = getResponseExternal(mifeSmsUrl, mifeSMSRequest, this.getToken());
            return responseNew;
        }

        return response;
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

    public String getToken() {

        TokenResponseBean tokenResponseBean = new TokenResponseBean();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", mifeSmsToken);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type","client_credentials");

        restTemplateExternal.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<TokenResponseBean> responseFinacle = restTemplateExternal.postForEntity(mifeTokenUrl, entity, TokenResponseBean.class);

        //HttpEntity<Object> requestEntity = new HttpEntity<>(requestParam, headers);
        if(responseFinacle.getStatusCode() == HttpStatus.OK){
            tokenResponseBean = responseFinacle.getBody();
        }

        this.smsToken = "Bearer " + tokenResponseBean.getAccess_token();

        return smsToken;
    }
}
