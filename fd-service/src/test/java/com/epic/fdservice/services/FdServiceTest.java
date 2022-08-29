package com.epic.fdservice.services;

import com.epic.fdservice.models.FdDetailsFinacleRequestBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class FdServiceTest {

    @Autowired
    FdService fdService;

    @Test
    void testCallToFinacleFDDetails(){

        FdDetailsFinacleRequestBean requestBean = new FdDetailsFinacleRequestBean();
        requestBean.setInqType("DEVID");
        requestBean.setInqValue("EA01DC29-8B17-40D8-BE93-53DBC98FD792");


        Map<String, Object> map = fdService.getFdDetailsFinacle(requestBean);

        System.out.println(map.toString());
    }
}