package com.epic.gatewayservice.controllers;

import com.epic.gatewayservice.services.GatewayService;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.internal.EncodingUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;


@RestController
public class GatewayController {

    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);

    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Autowired
    GatewayService gatewayService;

    @PostMapping(value = "/fd/details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFdDetails(@RequestHeader(value = "authorization") String authString,@RequestBody Object request) {
        log.info("CRM FD Details request received by Gateway Service for session ID : " + gatewayService.getSessionId(authString));
        String url = "http://FD-SERVICE/fd/details";
        String authUrl = "http://AUTH-SERVICE/auth/validate";
        return gatewayService.getResponseSecure(url,request,authString, authUrl);
    }

    @PostMapping(value = "/fd/details/finacle", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFdDetailsMobile(@RequestHeader(value = "authorization") String authString,@RequestBody Object request) {
        log.info("Mobile FD Details request received by Gateway Service for session ID : " + gatewayService.getSessionId(authString));
        String url = "http://FD-SERVICE/fd/details/finacle";
        String authUrl = "http://AUTH-SERVICE/auth/validate/jwt";
        return gatewayService.getResponseSecure(url,request,authString, authUrl);
    }

    @PostMapping(value = "/fd/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFdProducts(@RequestHeader(value = "authorization") String authString,@RequestBody Object request) {
        log.info("Mobile FD Products List request received by Gateway Service for session ID : " + gatewayService.getSessionId(authString));
        String url = "http://FD-SERVICE/fd/products";
        String authUrl = "http://AUTH-SERVICE/auth/validate/jwt";
        return gatewayService.getResponseSecure(url,request,authString, authUrl);
    }

    @PostMapping(value = "/savings/details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSavingsDetails(@RequestHeader(value = "authorization") String authString,@RequestBody Object request) {
        log.info("Savings Account Details request received by Gateway Service for session ID : " + gatewayService.getSessionId(authString));
        String url = "http://COMMON-SERVICE/common/savings/details/FD";
        String authUrl = "http://AUTH-SERVICE/auth/validate/jwt";
        return gatewayService.getResponseSecure(url,request,authString, authUrl);
    }

    @PostMapping(value = "/fd/calculation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFdInterestCalculation(@RequestHeader(value = "authorization") String authString,@RequestBody Object request) {
        log.info("FD Calculation request received by Gateway Service for session ID : " + gatewayService.getSessionId(authString));
        String url = "http://BROKER-SERVICE/fd/calculation";
        String authUrl = "http://AUTH-SERVICE/auth/validate/jwt";
        return gatewayService.getResponseSecure(url,request,authString, authUrl);
    }

    @PostMapping(value = "/fd/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFdAccount(@RequestHeader(value = "authorization") String authString,@RequestBody Object request) {
        log.info("FD Create request received by Gateway Service for session ID : " + gatewayService.getSessionId(authString));
        String url = "http://FD-SERVICE/fd/create";
        String authUrl = "http://AUTH-SERVICE/auth/validate/jwt";
        return gatewayService.getResponseSecure(url,request,authString, authUrl);
    }

    @GetMapping(value = "/fd/instructions/{language}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllActiveInstructions(@RequestHeader(value = "authorization") String authString, @PathVariable("language") String language) {
        log.info("FD Instructions request received by Gateway Service for session ID : " + gatewayService.getSessionId(authString));
        String url = "http://FD-SERVICE/fd/instructions/" + language;
        String authUrl = "http://AUTH-SERVICE/auth/validate/jwt";
        return gatewayService.getResponseSecure(url,null,authString,authUrl);
    }

    @GetMapping(value = "/fd/rates/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFdRates(@RequestHeader(value = "authorization") String authString, @PathVariable("type") String type) {
        log.info("FD Rates request received by Gateway Service for session ID : " + gatewayService.getSessionId(authString));
        String url = "http://FD-SERVICE/fd/rates/" + type;
        String authUrl = "http://AUTH-SERVICE/auth/validate/jwt";
        return gatewayService.getResponseSecure(url,null,authString,authUrl);
    }

    @GetMapping(value = "/fd/instructionImages/{language}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFdInstructionImages(@RequestHeader(value = "authorization") String authString, @PathVariable("language") String language) {
        log.info("FD Instruction Image request received by Gateway Service for session ID : " + gatewayService.getSessionId(authString));
        String url = "http://FD-SERVICE/fd/instructionImages/" + language;
        String authUrl = "http://AUTH-SERVICE/auth/validate/jwt";
        return gatewayService.getResponseSecure(url,null,authString,authUrl);
    }

    @PostMapping(value = "/auth/token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppAccessToken(@RequestBody Object request) {
        log.info("Backend App Access token - Eligibility - request received by Gateway Service");
        String url = "http://AUTH-SERVICE/auth/token";
        return gatewayService.getResponse(url,request,null);
    }

    @PostMapping(value = "/auth/obtain/jwt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getJwt(@RequestHeader(value = "authorization") String authString, @RequestBody Object request) {
        UUID uuid= UUID.randomUUID();
        log.info("App Access token - JWT - request received by Gateway Service");
        log.info("Session ID generated : " + uuid);
        log.debug("Auth header received as : ", authString);
        String url = "http://AUTH-SERVICE/auth/obtain/jwt/" + uuid;
        return gatewayService.getResponse(url,request,authString);
    }

    @PostMapping(value = "/auth/availability/{service}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkUserAvailibility(@RequestHeader(value = "authorization") String authString,@RequestBody Object request, @PathVariable("service") String serviceType) {
        log.info("Availability request received by Gateway Service");
        String url = "http://AUTH-SERVICE/auth/availability/" + serviceType;
        String authUrl = "http://AUTH-SERVICE/auth/validate";
        return gatewayService.getResponseSecure(url,request,authString,authUrl);
    }

    @PostMapping(value = "/common/getparam", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getParamDetails(@RequestHeader(value = "authorization") String authString,@RequestBody Object request) {
        log.info("Common Parameter Details request received by Gateway Service for session ID : " + gatewayService.getSessionId(authString));
        String url = "http://COMMON-SERVICE/common/getparam";
        String authUrl = "http://AUTH-SERVICE/auth/validate/jwt";
        return gatewayService.getResponseSecure(url,request,authString,authUrl);
    }
}
