package com.epic.common.persistance.repository;

import com.epic.common.persistance.entity.ShAlertTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface ShAlertTemplateRepo extends JpaRepository<ShAlertTemplate, BigDecimal> {

    ShAlertTemplate findTopByTxnType(int txnType);

}
