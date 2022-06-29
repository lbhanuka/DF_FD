package com.epic.gatewayservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GatewayService {

    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    public ResponseEntity<?> getResponse(String url, Object requestParam, String authString) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", authString);

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestParam, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return response;
        } catch(HttpStatusCodeException e) {
            ResponseEntity<?> response = new ResponseEntity<>(e.getResponseBodyAsString(),e.getStatusCode());
            return response;
        }
    }

    public ResponseEntity<?> getResponseSecure(String url, Object requestParam, String authString) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", authString);
        Map<String, Object> map = new HashMap<>();

        if(validateToken(authString) == 1){
            map.put("MESSAGE","INVALID TOKEN");
            map.put("STATUS","UNAUTHORISED");
            ResponseEntity<?> response = new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);
            return response;
        }else if(validateToken(authString) == 2){
            map.put("MESSAGE","EXPIRED TOKEN");
            map.put("STATUS","UNAUTHORISED");
            ResponseEntity<?> response = new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);
            return response;
        }else if(validateToken(authString) == 0){
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestParam, headers);
            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                return response;
            } catch(HttpStatusCodeException e) {
                ResponseEntity<?> response = new ResponseEntity<>(e.getResponseBodyAsString(),e.getStatusCode());
                return response;
            }
        } else {
            map.put("MESSAGE","UNEXPECTED ERROR");
            map.put("STATUS","ERROR");
            ResponseEntity<?> response = new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }

    }

    public int validateToken(String authString) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", authString);

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        String url = "http://AUTH-SERVICE/auth/validate";

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
            ResponseEntity<?> response = new ResponseEntity<>(e.getResponseBodyAsString(),e.getStatusCode());
            return 3;//unexpected error
        }
    }
}
