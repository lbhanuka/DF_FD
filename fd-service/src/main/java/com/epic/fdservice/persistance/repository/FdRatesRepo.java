package com.epic.fdservice.persistance.repository;

import com.epic.fdservice.models.FdInstructionsResponseBean;
import com.epic.fdservice.models.FdRatesResponseBean;
import com.epic.fdservice.persistance.entity.FdInstructionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FdRatesRepo extends JpaRepository<FdInstructionsEntity,String> {

    @Query("select new com.epic.fdservice.models.FdRatesResponseBean(e.id.period,e.monthly,e.maturity) from FdRateEntity e where e.status = 'ACT' and  e.id.fdType = ?1")
    List<FdRatesResponseBean> getFdRatesByType(String type);

}
