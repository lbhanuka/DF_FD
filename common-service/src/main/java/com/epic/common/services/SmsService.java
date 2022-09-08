package com.epic.common.services;

import com.epic.common.models.MifeSMSRequest;
import com.epic.common.models.OutboundSMSMessageRequest;
import com.epic.common.models.PushNotificationRequestBean;
import com.epic.common.models.SMSRequestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class SmsService {

    @Value("${mife.sms.url}")
    public String mifeSmsUrl;

    @Value("${mife.sms.bearertoken}")
    public String mifeSmsToken;

    @Value("${mife.sms.sender.address}")
    public String mifeSenderAddress;

    @Autowired
    @Qualifier("externalCalls")
    protected RestTemplate restTemplateExternal;

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

        MifeSMSRequest mifeSMSRequest = new MifeSMSRequest(outboundSMSMessageRequest);


        ResponseEntity<?> response = getResponseExternal(mifeSmsUrl, mifeSMSRequest, mifeSmsToken);
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
}
