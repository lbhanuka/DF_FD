package com.epic.authservice.services;

import com.epic.authservice.bean.FinacleCustomerDetailsResponseBean;
import com.epic.authservice.bean.JwtRequestBean;
import com.epic.authservice.bean.UserAvailibilityRequestBean;
import com.epic.authservice.persistance.entity.ShMobileUserEntity;
import com.epic.authservice.persistance.repository.MobileUserRepo;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by bhanuka_t on 2/15/2018.
 */
@Service
public class CommonService {

    static final long ONE_MINUTE_IN_MILLIS=60000;

    @Value("${config.synthima}")
    public String synthima;

    @Value("${config.security.username}")
    public String username;

    @Value("${config.security.password}")
    public String password;

    @Value("${config.tokenExpTimeInMinutes}")
    public int tokenExpTimeInMinutes;

    @Autowired
    MobileUserRepo mobileUserRepo;

    @Autowired
    protected RestTemplate restTemplate;

    public boolean isAuthorisedAccess(String httpBacicAuthHeader, String deviceId){
        boolean isAuthorised = false;

        String[] credentials = this.getCredentials(httpBacicAuthHeader);

        if(credentials == null || credentials.length == 0){
            return isAuthorised;
        }

        String username = credentials[0];
        String password = credentials[1];

        //Check if username with this password and ip has access to the service code
        if (this.isAuthorised(username, password, deviceId)){
            isAuthorised = true;
        }
        return isAuthorised;
    }

    private String[] getCredentials(String authString){
        // remove "Basic " from auth string
        authString = authString.substring(6);

        byte[] decodedBytes = Base64.getDecoder().decode(authString);
        String decodedString = new String(decodedBytes);
        String[] credentials = decodedString.split(":");

        return credentials;
    }

    public boolean isAuthorised(String username, String password, String deviceId){
        boolean authorised = false;
        boolean isRealDeviceId = mobileUserRepo.existsByDeviceid(deviceId);
        if(isRealDeviceId && username != null && password != null && username.equals(this.username) && password.equals(this.password)){
            authorised = true;
        }

        return authorised;
    }

    public String getJWT(String authString, String deviceId) throws Exception{
        String token;

        String[] credentials = this.getCredentials(authString);
        String username = credentials[0];

        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();
        Date afterAddingTousandMins = new Date(t + (tokenExpTimeInMinutes * ONE_MINUTE_IN_MILLIS));

        token = Jwts.builder()
                .setSubject("EPIC_DF_SERVICE_HANDLER_TOKEN")
                .setExpiration(afterAddingTousandMins)
                .claim("username", username)
                .claim("device_id", deviceId)
                .signWith(
                        SignatureAlgorithm.HS256,
                        synthima.getBytes("UTF-8")
                )
                .compact();
        return token;
    }

    public String validateJWT(String jwt){
        boolean isAuthorised = false;
        HashMap<String,String> client = new HashMap<>();
        String device_id = null;
        String username = null;

        //pars and authenticate jwt
        jwt = jwt.substring(7);
        Jws<Claims> claims;
        try {
            claims = Jwts.parser().setSigningKey(synthima.getBytes("UTF-8")).parseClaimsJws(jwt);

            device_id = (String) claims.getBody().get("device_id");
            username = (String) claims.getBody().get("username");

            return "SUCCESS";

        } catch (UnsupportedEncodingException | ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            e.printStackTrace();
            //client.put("isauthorised","0");
            return "INVALID";
        }
    }

    public boolean updateMobileUser(JwtRequestBean requestBean){

        String email = this.getEmailAddress(requestBean.getCustomerNic());

        if(email == null) return false;

        ShMobileUserEntity entity = new ShMobileUserEntity();

        entity.setDeviceid(requestBean.getDeviceId());
        entity.setIdnumber(requestBean.getCustomerNic());
        entity.setMobilenumber(requestBean.getMobileNumber());
        entity.setEmail(email);
        entity.setLanguage("E");

        mobileUserRepo.save(entity);

        return true;
    }

    private String getEmailAddress(String nic){
        Map<String, Object> response = new HashMap<>();
        HashMap<String,String> requestData = new HashMap<>();

        requestData.put("inqType","NIC");
        requestData.put("inqValue",nic);

        String url = "http://BROKER-SERVICE/customer/details";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestData, headers);

        try {
            ResponseEntity<FinacleCustomerDetailsResponseBean> responseFromService = restTemplate.postForEntity(url, requestEntity, FinacleCustomerDetailsResponseBean.class);
            //ResponseEntity<String> responseFromService = restTemplate.postForEntity(url, requestEntity, String.class);
            return responseFromService.getBody().getRESPONSE_DATA().get("Email").toString();
        } catch(HttpStatusCodeException e) {
            response.put("STATUS","FAILED");
            response.put("MESSAGE",e.getResponseBodyAsString());
            return null;
        }
    }
}
