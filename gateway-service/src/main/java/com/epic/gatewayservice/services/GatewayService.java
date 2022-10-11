package com.epic.gatewayservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class GatewayService {

    private static final Logger log = LoggerFactory.getLogger(GatewayService.class);

    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Value("${config.synthima}")
    public String synthima;

    public ResponseEntity<?> getResponse(String url, Object requestParam, String authString) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", authString);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestParam, headers);
        log.info("Passing request to backend service on URL: " + url);
        try {
            ResponseEntity<String> responseFromService = restTemplate.postForEntity(url, requestEntity, String.class);

            ResponseEntity<?> response = new ResponseEntity<>(responseFromService.getBody(),HttpStatus.OK);
            log.info("Backend service respond OK");
            return response;
        } catch(HttpStatusCodeException e) {
            log.error("Backend service communication error: INVALID HTTP STATUS: " + e.getMessage());
            ResponseEntity<?> response = new ResponseEntity<>(e.getResponseBodyAsString(),e.getStatusCode());
            return response;
        }
    }

    public ResponseEntity<?> getResponseSecure(String url, Object requestParam, String authString, String authUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", authString);
        Map<String, Object> map = new HashMap<>();
        int authStatus = validateToken(authString, authUrl);

        if(authStatus == 1){
            log.warn("JWT is invalid");
            map.put("MESSAGE","INVALID TOKEN");
            map.put("STATUS","UNAUTHORISED");
            ResponseEntity<?> response = new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);
            return response;
        }else if(authStatus == 2){
            log.warn("JWT is expired");
            map.put("MESSAGE","EXPIRED TOKEN");
            map.put("STATUS","UNAUTHORISED");
            ResponseEntity<?> response = new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);
            return response;
        }else if(authStatus == 0){
            log.info("JWT is OK");
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestParam, headers);
            try {
                log.info("Passing request to backend service on URL: " + url);
                ResponseEntity<String> responseFromService = restTemplate.postForEntity(url, requestEntity, String.class);
                ResponseEntity<?> response = new ResponseEntity<>(responseFromService.getBody(),HttpStatus.OK);
                return response;
            } catch(HttpStatusCodeException e) {
                log.error("Error from backend service. INVALID HTTP STATUS : " + e.getMessage());
                ResponseEntity<?> response = new ResponseEntity<>(e.getResponseBodyAsString(),e.getStatusCode());
                return response;
            }
        } else {
            log.warn("Unexpected error while validating JWT");
            map.put("MESSAGE","UNEXPECTED ERROR");
            map.put("STATUS","ERROR");
            ResponseEntity<?> response = new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }

    }

    public int validateToken(String authString, String authUrl) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", authString);

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        String url = authUrl;
        log.info("Passing request to AUTH-SERVICE for token validation on URL: " + url);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if(response.getBody().equals("INVALID")){
                return 1;//invalid token
            }else if (response.getBody().equals("EXPIRED")){
                return 2;//expired token
            } else if (response.getBody().equals("SUCCESS")){
                return 0;//token validated
            } else {
                return 3;
            }
        } catch(HttpStatusCodeException e) {
            log.error("JWT validation error: INVALID HTTP STATUS: " + e.getMessage());
            ResponseEntity<?> response = new ResponseEntity<>(e.getResponseBodyAsString(),e.getStatusCode());
            return 3;//unexpected error
        }
    }

    public String getSessionId(String jwt){
        String session_id = null;
        Base64.Decoder decoder = Base64.getUrlDecoder();
        //pars and authenticate jwt
        jwt = jwt.substring(7);
        try {
            String[] chunks = jwt.split("\\.");
            //String header = new String(decoder.decode(chunks[0]));
            String payload = new String(decoder.decode(chunks[1]));
            Map<String, Object> response = new ObjectMapper().readValue(payload, HashMap.class);

            session_id = response.get("session_id").toString();

            return session_id;
        } catch (Exception e){
            log.error("Error while extracting session ID from JWT :" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
