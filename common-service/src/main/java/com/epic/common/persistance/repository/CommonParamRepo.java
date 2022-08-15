package com.epic.common.persistance.repository;

import com.epic.common.models.CommonParamBean;
import com.epic.common.models.MobileUserBean;
import com.epic.common.persistance.entity.ShCommonParam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommonParamRepo extends JpaRepository<ShCommonParam,String> {

    @Query("select new com.epic.common.models.CommonParamBean(s.id,s.paramValue,s.description) from ShCommonParam s where s.category = ?1")
    List<CommonParamBean> getUnderCategory(String category);

    @Query("select new com.epic.common.models.CommonParamBean(s.id,s.paramValue,s.description) from ShCommonParam s")
    List<CommonParamBean> getAll();

    @Query("select s.idnumber from ShMobileUserEntity s where s.deviceid = ?1")
    String getProductType(String deviceId);

    @Query("select new com.epic.common.models.MobileUserBean(e.idnumber,e.mobilenumber) from ShMobileUserEntity e where e.deviceid = ?1")
    MobileUserBean getNicByDeviceId(String deviceId);

}
