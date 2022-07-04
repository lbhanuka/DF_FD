package com.epic.common.services;

import com.epic.common.models.CommonParamBean;
import com.epic.common.models.CommonParamRequestBean;
import com.epic.common.persistance.repository.CommonParamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CommonService {

    @Autowired
    CommonParamRepo commonService;

    @Autowired
    Validator validator;
    public Map<String, Object> getParams(CommonParamRequestBean requestBean) {

        Map<String, Object> map = new HashMap<>();
        List<CommonParamBean> resultList = null;

        Set<ConstraintViolation<CommonParamRequestBean>> violations = validator.validate(requestBean);

        if (!violations.isEmpty()) {
            StringBuffer Msg = new StringBuffer();
            for(ConstraintViolation<CommonParamRequestBean> violation : violations){
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

        if(!requestBean.getParamKey().equals("")){

        }else if(requestBean.getCategory().equals("FD") && requestBean.getParamKey().equals("")){
            resultList = commonService.getUnderCategory(requestBean.getCategory());
        }else if (requestBean.getCategory().equals("SP") && requestBean.getParamKey().equals("")){
            resultList = commonService.getUnderCategory(requestBean.getCategory());
        }

        if(resultList != null && !resultList.isEmpty()){
            map.put("MESSAGE","COMMON PARAM FETCHED");
            map.put("STATUS","SUCCESS");
            map.put("DATA",resultList);
        } else {
            map.put("MESSAGE","NO COMMON PARAM FOUND");
            map.put("STATUS","FAIL");
        }

        return map;
    }
}
