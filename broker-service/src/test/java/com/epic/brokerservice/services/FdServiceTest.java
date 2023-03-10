package com.epic.brokerservice.services;


import com.epic.brokerservice.bean.TokenBean;
import com.epic.brokerservice.bean.TokenResponseBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.util.HashMap;


@SpringBootTest
public class FdServiceTest {

    @Autowired
    FdService fdService;

    @Test
    void testSomeRestCall(){

        /*TokenBean bean = new TokenBean();

        bean.setAuth_type("pas_admin");
        bean.setClient_id("pas_admin_client");
        bean.setSecret("3258094a-b5e4-11ea-b3de-0242ac130004");
        bean.setGrant_type("password");*/

        String token = fdService.getToken();

        System.out.println("FINACLE TOKEN -----------------------------> " + token);
    }

    @Test
    void getSavingsAccountListTest(){

        String inqType = "NIC";
        String inqValue = "199336600982";

        HashMap<String,String> inputs = new HashMap<>();
        inputs.put("inqType",inqType);
        inputs.put("inqValue",inqValue);

        fdService.getSavingsAccountList(inputs);

    }
}
