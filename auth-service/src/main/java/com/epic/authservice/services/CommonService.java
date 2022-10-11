package com.epic.authservice.services;

import com.epic.authservice.bean.FinacleCustomerDetailsResponseBean;
import com.epic.authservice.bean.JwtRequestBean;
import com.epic.authservice.persistance.entity.ShMobileUserEntity;
import com.epic.authservice.persistance.repository.MobileUserRepo;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.sql.Timestamp;
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

    private static final Logger log = LoggerFactory.getLogger(CommonService.class);

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

    public String getJWT(String authString, String deviceId, String sessionId) throws Exception{
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
                .claim("session_id", sessionId)
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

        Map<String, Object> customerDetails = this.getCustomerDetails(requestBean.getCustomerNic());
        String email = null, name = null, dateOfBirth = null, address1 = null, address2 = null, address3 = null;

        email = customerDetails.get("email") != null ? customerDetails.get("email").toString() : null;
        name = customerDetails.get("name") != null ? customerDetails.get("name").toString() : null;
        dateOfBirth = customerDetails.get("DOB") != null ? customerDetails.get("DOB").toString() : null;
        address1 = customerDetails.get("address1") != null ? customerDetails.get("address1").toString() : null;
        address2 = customerDetails.get("address2") != null ? customerDetails.get("address2").toString() : null;
        address3 = customerDetails.get("address3") != null ? customerDetails.get("address3").toString() : null;

        ShMobileUserEntity entity = mobileUserRepo.findByDeviceid(requestBean.getDeviceId());

        //entity.setDeviceid(requestBean.getDeviceId());
        if(entity.getIdnumber() != null && entity.getIdnumber().equals(requestBean.getCustomerNic())) { //device ID has got the same customer.
            log.info("Current customer data is available to use");
            if(entity.getEmail() != null && email == null){
                log.debug("Setting customer email address as : " + entity.getEmail());
                email = entity.getEmail();
            }
            if(entity.getDateOfBirth() != null && dateOfBirth == null){
                log.debug("Setting customer DOB as : " + entity.getDateOfBirth());
                dateOfBirth = entity.getDateOfBirth();
            }
            if(entity.getName() != null && name == null){
                log.debug("Setting customer name as : " + entity.getName());
                name = entity.getName();
            }
            if(entity.getAddress1() != null && address1 == null){
                log.debug("Setting address1 as : " + entity.getAddress1());
                address1 = entity.getAddress1();
            }
            if(entity.getAddress2() != null && address2 == null){
                log.debug("Setting address2 as : " + entity.getAddress2());
                address2 = entity.getAddress2();
            }
            if(entity.getAddress3() != null && address2 == null){
                log.debug("Setting address3 as : " + entity.getAddress3());
                address3 = entity.getAddress3();
            }
        }

        if(email == null) {
            log.error("Cannot resolve email address for NIC " + requestBean.getCustomerNic());
            //return false; - no need to stop the process if there is no email address
        }
        if(name == null) {
            log.error("Cannot resolve customer name for NIC " + requestBean.getCustomerNic());
        }
        if(dateOfBirth == null) {
            log.error("Cannot resolve DOB for NIC " + requestBean.getCustomerNic());
        }

        entity.setIdnumber(requestBean.getCustomerNic());
        entity.setMobilenumber(requestBean.getMobileNumber());
        entity.setEmail(email);
        entity.setName(name);
        entity.setDateOfBirth(dateOfBirth);
        entity.setAddress1(address1);
        entity.setAddress2(address2);
        entity.setAddress3(address3);
        entity.setLanguage("E");
        entity.setLastupdatedtime(new Timestamp(System.currentTimeMillis()));

        mobileUserRepo.save(entity);

        return true;
    }

    private Map<String, Object> getCustomerDetails(String nic){
        Map<String, Object> response = new HashMap<>();
        HashMap<String,String> requestData = new HashMap<>();

        requestData.put("inqType","NIC");
        requestData.put("inqValue",nic);

        String url = "http://BROKER-SERVICE/customer/details";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestData, headers);
        log.info("Passing request to backend service on URL: " + url);
        try {
            ResponseEntity<FinacleCustomerDetailsResponseBean> responseFromService = restTemplate.postForEntity(url, requestEntity, FinacleCustomerDetailsResponseBean.class);
            //ResponseEntity<String> responseFromService = restTemplate.postForEntity(url, requestEntity, String.class);
            log.info("Customer inquiry response status : " + responseFromService.getBody().getSTATUS());
            log.debug(responseFromService.getBody().toString());
            response.put("STATUS","SUCCESS");
            response.put("email",responseFromService.getBody().getRESPONSE_DATA().get("Email").toString());
            response.put("name",responseFromService.getBody().getRESPONSE_DATA().get("Salutation").toString() + " " +responseFromService.getBody().getRESPONSE_DATA().get("CusName").toString());
            response.put("DOB",responseFromService.getBody().getRESPONSE_DATA().get("DOB").toString());
            response.put("address1",responseFromService.getBody().getRESPONSE_DATA().get("Address1").toString());
            response.put("address2",responseFromService.getBody().getRESPONSE_DATA().get("Address2").toString());
            response.put("address3",responseFromService.getBody().getRESPONSE_DATA().get("Address3").toString());
            return response;
        } catch(HttpStatusCodeException e) {
            log.info("Customer inquiry failed : http status code exception");
            log.error(e.getMessage());
            response.put("STATUS","FAILED");
            response.put("MESSAGE",e.getResponseBodyAsString());
            return response;
        } catch(Exception e) {
            log.info("Customer inquiry failed : other exception");
            log.error(e.getMessage());
            response.put("STATUS","FAILED");
            response.put("MESSAGE",e.getMessage());
            return response;
        }
    }
}
