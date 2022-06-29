package com.epic.authservice.persistance.repository;

import com.epic.authservice.persistance.entity.FdDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface FdDetailsRepo extends JpaRepository<FdDetailsEntity,String> {

    @Query("select coalesce(sum(e.amount),0) from FdDetailsEntity e where e.nic = ?1")
    BigDecimal getTotalBalanceByUserNIC(String nic);

    boolean existsByNic(String nic);

}
