package com.epic.fdservice.persistance.repository;

import com.epic.fdservice.persistance.entity.ShAlertTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface ShAlertTemplateRepo extends JpaRepository<ShAlertTemplateEntity, BigDecimal> {

    ShAlertTemplateEntity findTopByTxnType(int txnType);

}
