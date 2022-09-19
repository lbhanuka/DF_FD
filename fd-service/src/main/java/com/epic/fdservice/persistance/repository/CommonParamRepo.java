package com.epic.fdservice.persistance.repository;

import com.epic.fdservice.persistance.entity.ShCommonParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommonParamRepo extends JpaRepository<ShCommonParam,String> {

    @Query("select s.paramValue from ShCommonParam s where s.id = ?1")
    String getValueByID(String id);

}
