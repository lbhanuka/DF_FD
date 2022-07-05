package com.epic.gatewayservice.controllers;

import com.epic.gatewayservice.services.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
public class GatewayController {

    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Autowired
    GatewayService gatewayService;

    @PostMapping(value = "/fd/details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFdDetails(@RequestHeader(value = "authorization") String authString,@RequestBody Object request) {
        String url = "http://FD-SERVICE/fd/details";
        return gatewayService.getResponseSecure(url,request,authString);
    }

    @PostMapping(value = "/auth/token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppAccessToken(@RequestBody Object request) {
        String url = "http://AUTH-SERVICE/auth/token";
        return gatewayService.getResponse(url,request,null);
    }

    @PostMapping(value = "/auth/obtain/jwt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getJwt(@RequestBody Object request) {
        String url = "http://AUTH-SERVICE/auth/obtain/jwt";
        return gatewayService.getResponse(url,request,null);
    }

    @PostMapping(value = "/auth/availability/{service}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkUserAvailibility(@RequestHeader(value = "authorization") String authString,@RequestBody Object request, @PathVariable("service") String serviceType) {
        String url = "http://AUTH-SERVICE/auth/availability/" + serviceType;
        return gatewayService.getResponseSecure(url,request,authString);
    }

    @PostMapping(value = "/common/getparam", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getParamDetails(@RequestHeader(value = "authorization") String authString,@RequestBody Object request) {
        String url = "http://COMMON-SERVICE/common/getparam";
        return gatewayService.getResponseSecure(url,request,authString);
    }
}
