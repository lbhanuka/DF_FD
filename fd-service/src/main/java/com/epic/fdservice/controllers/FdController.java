package com.epic.fdservice.controllers;

import com.epic.fdservice.models.FdCreateRequestBean;
import com.epic.fdservice.models.FdDetailsFinacleRequestBean;
import com.epic.fdservice.models.FdDetailsRequestBean;
import com.epic.fdservice.models.FdProductRequestBean;
import com.epic.fdservice.services.FdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fd")
public class FdController {

    private static final Logger log = LoggerFactory.getLogger(FdController.class);

    @Autowired
    FdService fdService;

    @RequestMapping(value = "/details", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFdDetails(@RequestBody FdDetailsRequestBean requestBean){

        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Map<String, Object> map = fdService.getFdDetails(requestBean);

        if(map.get("STATUS").equals("UNAUTHORISED")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }else if (map.get("STATUS").equals("BAD REQUEST")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }else if (map.get("STATUS").equals("SUCCESS") || map.get("STATUS").equals("FAIL")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/details/finacle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFdDetailsFinacle(@RequestBody FdDetailsFinacleRequestBean request){
        log.info("get finacle FD request received by FD Service");
        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Map<String, Object> map = fdService.getFdDetailsFinacle(request);

        if(map.get("STATUS").equals("UNAUTHORISED")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }else if (map.get("STATUS").equals("BAD REQUEST")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }else if (map.get("STATUS").equals("SUCCESS") || map.get("STATUS").equals("FAILED")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/instructions/{language}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllActiveInstructions(@PathVariable("language") String language){
        log.info("Get FD Instructions request received by FD Service");
        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Map<String, Object> map = fdService.getAllActiveInstructions(language);

        if(map.get("STATUS").equals("UNAUTHORISED")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }else if (map.get("STATUS").equals("BAD REQUEST")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }else if (map.get("STATUS").equals("SUCCESS") || map.get("STATUS").equals("FAIL")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllowedFdProducts(@RequestBody FdProductRequestBean request){
        log.info("Get Allowed FD Products request received by FD Service");
        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Map<String, Object> map = fdService.getAllowedFdProducts(request);

        if(map.get("STATUS").equals("UNAUTHORISED")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }else if (map.get("STATUS").equals("BAD REQUEST")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }else if (map.get("STATUS").equals("SUCCESS") || map.get("STATUS").equals("FAIL")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/rates/{type}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFdRates(@PathVariable("type") String type){
        log.info("Get FD rates request received by FD Service");
        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Map<String, Object> map = fdService.getFdRates(type);

        if(map.get("STATUS").equals("UNAUTHORISED")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }else if (map.get("STATUS").equals("BAD REQUEST")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }else if (map.get("STATUS").equals("SUCCESS") || map.get("STATUS").equals("FAIL")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/instructionImages/{language}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getInstructionImages(@PathVariable("language") String language){
        log.info("Get Instruction Images request received by FD Service");
        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Map<String, Object> map = fdService.getFdInstructionImages(language);

        if(map.get("STATUS").equals("UNAUTHORISED")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }else if (map.get("STATUS").equals("BAD REQUEST")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }else if (map.get("STATUS").equals("SUCCESS") || map.get("STATUS").equals("FAIL")){
            responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        }

        return responseEntity;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFdAccount(@RequestBody FdCreateRequestBean request) {
        log.info("FD Create request received by FD Service");
        return fdService.createFdAccount(request);
    }

}
