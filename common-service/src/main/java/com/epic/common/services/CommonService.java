package com.epic.common.services;

import com.epic.common.models.CommonParamBean;
import com.epic.common.models.CommonParamRequestBean;
import com.epic.common.models.NICType;
import com.epic.common.persistance.repository.CommonParamRepo;
import com.epic.common.util.NicValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.*;

@Service
public class CommonService {

    @Autowired
    CommonParamRepo commonService;

    @Autowired
    Validator validator;

    public Map<String, Object> getParams(CommonParamRequestBean requestBean) throws Exception {

        Map<String, Object> map = new HashMap<>();
        List<CommonParamBean> resultList = null;

        Set<ConstraintViolation<CommonParamRequestBean>> violations = validator.validate(requestBean);

        if (!violations.isEmpty()) {
            StringBuffer Msg = new StringBuffer();
            for (ConstraintViolation<CommonParamRequestBean> violation : violations) {
                //Msg.violation.getMessage());
                if (!Msg.toString().isEmpty()) {
                    Msg.append("|");
                }
                Msg.append(violation.getMessage().replace("{value}", violation.getInvalidValue() == null ? "null" : violation.getInvalidValue().toString()));
            }
            map.put("MESSAGE", Msg);
            map.put("STATUS", "BAD REQUEST");
            return map;
        }

        if (requestBean.getCategory().equals("FD")) {
            resultList = commonService.getUnderCategory(requestBean.getCategory());
        } else if (requestBean.getCategory().equals("SP")) {
            resultList = commonService.getUnderCategory(requestBean.getCategory());
        } else if (requestBean.getCategory().equals("ALL")) {
            resultList = commonService.getAll();
        }


        if (requestBean.getDeviceId() != null && !requestBean.getDeviceId().equals("") && (requestBean.getCategory().equals("ALL") || requestBean.getCategory().equals("FD"))) {

            if (resultList != null && !resultList.isEmpty()) {

                String nic = commonService.getProductType(requestBean.getDeviceId());

                if(nic != null){
                    NICType nicType = NicValidations.checkNICType(nic);

                    if (Objects.equals(nicType, NICType.OLD)) {

                        String generatedNic = NicValidations.convertOldNicTONewNic(nic);
                        setProductType(map, resultList, generatedNic);

                    } else if (Objects.equals(nicType, NICType.NEW)) {

                        setProductType(map, resultList, nic);

                    } else {
                        map.put("MESSAGE", "CANNOT VALIDATE NIC FOR DEVICE ID");
                        map.put("STATUS", "FAIL");
                    }
                }else {
                    map.put("MESSAGE", "DEVICE ID NOT VALID");
                    map.put("STATUS", "FAIL");
                }

            }else {
                map.put("MESSAGE", "NO COMMON PARAM FOUND");
                map.put("STATUS", "FAIL");
            }

            return map;
        } else {

            if (resultList != null && !resultList.isEmpty()) {
                map.put("MESSAGE", "COMMON PARAM FETCHED");
                map.put("STATUS", "SUCCESS");
                map.put("DATA", resultList);
            } else {
                map.put("MESSAGE", "NO COMMON PARAM FOUND");
                map.put("STATUS", "FAIL");
            }

            return map;
        }
    }

    private void setProductType(Map<String, Object> map, List<CommonParamBean> resultList, String nic) {
        NicValidations.NicDetails nicDetails = new NicValidations.NicDetails(nic);

        int seniorCitizenAge = 60;

        for(CommonParamBean bean : resultList){
            if (bean.getKey().equals("SENIOR_CITIZEN_AGE"))
                seniorCitizenAge = Integer.parseInt(bean.getValue());
        }

        if(NicValidations.comparePeriod(LocalDate.now(), nicDetails.getDateOfBirth(), seniorCitizenAge)){
            for (int i = 0; i < resultList.size(); i++) {
                if(resultList.get(i).getKey().equals("ALLOWED_PRODUCT")){
                    resultList.get(i).setValue("SENIOR_CITIZEN");
                }else{
                    CommonParamBean bean = new CommonParamBean("ALLOWED_PRODUCT","SENIOR_CITIZEN","DIGITAL or SENIOR_CITIZEN");
                    resultList.add(bean);
                }
            }
        }else{
            for (int i = 0; i < resultList.size(); i++) {
                if(resultList.get(i).getKey().equals("ALLOWED_PRODUCT")){
                    resultList.get(i).setValue("DIGITAL");
                }else{
                    CommonParamBean bean = new CommonParamBean("ALLOWED_PRODUCT","DIGITAL","DIGITAL or SENIOR_CITIZEN");
                    resultList.add(bean);
                }
            }
        }
        map.put("MESSAGE", "COMMON PARAM FETCHED");
        map.put("STATUS", "SUCCESS");
        map.put("DATA", resultList);
    }


}


