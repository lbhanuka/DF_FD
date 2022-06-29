package com.epic.fdservice.services;

import com.epic.fdservice.models.CrmFdDetailsBean;
import com.epic.fdservice.models.FdDetailsRequestBean;
import com.epic.fdservice.persistance.entity.FdDetailsEntity;
import com.epic.fdservice.persistance.repository.FdDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FdService {

    @Autowired
    FdDetailsRepo fdDetailsRepo;

    @Autowired
    Validator validator;

    public Map<String, Object> getFdDetails(FdDetailsRequestBean requestBean) {
        Map<String, Object> map = new HashMap<>();
        List<CrmFdDetailsBean> resultList = null;

        Set<ConstraintViolation<FdDetailsRequestBean>> violations = validator.validate(requestBean);

        if (!violations.isEmpty()) {
            StringBuffer Msg = new StringBuffer();
            for(ConstraintViolation<FdDetailsRequestBean> violation : violations){
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

        if(requestBean.getIdentificationType().equals("NIC")){
            resultList = fdDetailsRepo.findByNic(requestBean.getIdentificationNumber());
        }else if (requestBean.getIdentificationType().equals("CIF")){
            resultList = fdDetailsRepo.findByCif(requestBean.getIdentificationNumber());
        }

        if(resultList != null && !resultList.isEmpty()){

            map.put("MESSAGE","FD DETAILS FETCHED");
            map.put("STATUS","SUCCESS");
            map.put("DATA",resultList);
        } else {
            map.put("MESSAGE","NO FD DETAILS FOUND");
            map.put("STATUS","FAIL");
        }

        return map;
    }
}
