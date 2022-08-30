package com.epic.authservice.services;

import com.epic.authservice.bean.AccessTokenRequestBean;
import com.epic.authservice.bean.UserAvailibilityRequestBean;
import com.epic.authservice.persistance.entity.ShClientEntity;
import com.epic.authservice.persistance.entity.ShClientTokenEntity;
import com.epic.authservice.persistance.entity.ShMobileUserEntity;
import com.epic.authservice.persistance.repository.ClientRepo;
import com.epic.authservice.persistance.repository.ClientTokenRepo;
import com.epic.authservice.persistance.repository.FdDetailsRepo;
import com.epic.authservice.persistance.repository.MobileUserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.*;

@Service
public class AuthService {

    @Value("${config.synthima}")
    private String synthima;

    @Value("${config.client.tokenExpTimeInSeconds}")
    private int tokenExpTimeInSeconds;

    @Value("${config.mobileuser.tokenExpTimeInSeconds}")
    private int userTokenExpTimeInSeconds;

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    ClientTokenRepo clientTokenRepo;

    @Autowired
    MobileUserRepo mobileUserRepo;

    @Autowired
    FdDetailsRepo fdDetailsRepo;

    @Autowired
    Validator validator;

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public Map<String, Object> getAppAccessToken(AccessTokenRequestBean requestBean) {

        Map<String, Object> map = new HashMap<>();

        if(requestBean.getGrant_type() == null || !requestBean.getGrant_type().equals("password")){
            map.put("error","unsupported_grant_type");
            map.put("error_description","Unsupported grant type: " + requestBean.getGrant_type());
            return map;
        }

        if (this.validateClient(requestBean)){

            ShClientEntity entity = clientRepo.getReferenceById(requestBean.getClient_id());

            Calendar date = Calendar.getInstance();
            date.add(Calendar.SECOND,(int) entity.getTokenexpiretime());
            long t= date.getTimeInMillis();

            map.put("access_token",this.generateNewToken());
            map.put("refresh_token",this.generateNewToken());
            map.put("token_type","bearer");
            map.put("expires_in",entity.getTokenexpiretime());
            map.put("scope",entity.getScope());

            insertIntoClientToken(requestBean, map, t);

        } else {
            map.put("error","invalid_grant");
            map.put("error_description","Bad client credentials");
        }

        return map;
    }

    private boolean validateClient(AccessTokenRequestBean requestBean){
        return clientRepo.existsByClientidAndSecretAndAuthtype(requestBean.getClient_id(),requestBean.getSecret(),requestBean.getAuth_type());
    }

    private void insertIntoClientToken(AccessTokenRequestBean requestBean, Map<String, Object> map, long tokenExpTimeInMilis){

        ShClientTokenEntity entity = new ShClientTokenEntity();

        entity.setClientid(requestBean.getClient_id());
        entity.setAccesstoken(map.get("access_token").toString());
        entity.setRefreshtoken(map.get("refresh_token").toString());
        entity.setExpiration(tokenExpTimeInMilis);

        clientTokenRepo.save(entity);
    }

    public Map<String, Object> checkUserAvailibility(UserAvailibilityRequestBean requestBean, String serviceType){

        Map<String, Object> map = new HashMap<>();
        Calendar date = Calendar.getInstance();


        Set<ConstraintViolation<UserAvailibilityRequestBean>> violations = validator.validate(requestBean);

        if (!violations.isEmpty()) {
            StringBuffer Msg = new StringBuffer();
            for(ConstraintViolation<UserAvailibilityRequestBean> violation : violations){
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

        date.add(Calendar.SECOND, userTokenExpTimeInSeconds);
        long t= date.getTimeInMillis();

        String token = this.generateNewToken();

        Map<String, Object> productDetails = new HashMap<>();
        productDetails.put("reference","");
        //productDetails.put("balance",fdDetailsRepo.getTotalBalanceByUserNIC(requestBean.getCustomerNic()));
        productDetails.put("balance",fdDetailsRepo.getTotalBalanceByUserNIC(requestBean.getCustomerNic()).setScale(2, RoundingMode.HALF_EVEN));

        Map<String, Object> tokenResponse = new HashMap<>();
        tokenResponse.put("access_token",token);
        tokenResponse.put("token_type","bearer");
        tokenResponse.put("expires_in",userTokenExpTimeInSeconds);
        tokenResponse.put("scope","read");

        Map<String, Object> data = new HashMap<>();
        data.put("productExists",fdDetailsRepo.existsByNic(requestBean.getCustomerNic()));
        data.put("webAppLocation",null);
        data.put("productDetails",productDetails);
        data.put("tokenResponse",tokenResponse);

        map.put("MESSAGE","AVAILABILITY_SUCCESS");
        map.put("STATUS","SUCCESS");
        map.put("DATA",data);

        this.insertIntoMobileUser(requestBean,t,token);

        return map;
    }

    private void insertIntoMobileUser(UserAvailibilityRequestBean requestBean, long tokenExpTimeInMilis, String token){

        ShMobileUserEntity entity = new ShMobileUserEntity();

        entity.setDeviceid(requestBean.getDeviceId());
        entity.setIdnumberApp(requestBean.getCustomerNic());
        entity.setMobilenumber(requestBean.getMobileNumber());
        entity.setLanguage(requestBean.getLanguage());
        entity.setToken(token);
        entity.setTokenexpiration(tokenExpTimeInMilis);
        entity.setLastupdatedtime(new Timestamp(System.currentTimeMillis()));

        mobileUserRepo.save(entity);
    }

    public String checkTokenValidity(String authString) {
        Calendar date = Calendar.getInstance();
        long currentTime= date.getTimeInMillis();

        try {
            String clientToken = authString.substring(7);
            ShClientTokenEntity entity = clientTokenRepo.findByAccesstoken(clientToken);

            if(entity == null){
                return "INVALID";
            } else if(currentTime > entity.getExpiration()){
                return "EXPIRED";
            }
        } catch (StringIndexOutOfBoundsException ex){
            return "INVALID";
        }

        return "SUCCESS";
    }
}
