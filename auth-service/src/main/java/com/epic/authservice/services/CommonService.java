package com.epic.authservice.services;

import com.epic.authservice.persistance.repository.MobileUserRepo;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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

    public HashMap<String,String> validateJWT(String jwt){
        boolean isAuthorised = false;
        HashMap<String,String> client = new HashMap<>();
        String ip = null;
        String username = null;

        //pars and authenticate jwt
        jwt = jwt.substring(7);
        Jws<Claims> claims;
        try {
            claims = Jwts.parser().setSigningKey(synthima.getBytes("UTF-8")).parseClaimsJws(jwt);

            ip = (String) claims.getBody().get("user_ip");
            username = (String) claims.getBody().get("username");

            client.put("username",username);
            client.put("clientip",ip);
            client.put("isauthorised","1");

        } catch (UnsupportedEncodingException | ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            e.printStackTrace();
            client.put("isauthorised","0");
        }

        return client;
    }
}
