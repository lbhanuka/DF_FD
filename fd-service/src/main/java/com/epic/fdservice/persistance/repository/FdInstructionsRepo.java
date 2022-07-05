package com.epic.fdservice.persistance.repository;

import com.epic.fdservice.models.FdInstructionsResponseBean;
import com.epic.fdservice.persistance.entity.FdInstructionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FdInstructionsRepo extends JpaRepository<FdInstructionsEntity,String> {

    @Query("select new com.epic.fdservice.models.FdInstructionsResponseBean(e.instructionEnglish,e.sortkey) from FdInstructionsEntity e where e.status = ?1")
    List<FdInstructionsResponseBean> getAllInstructionsEnglish(String status);

    @Query("select new com.epic.fdservice.models.FdInstructionsResponseBean(e.instructionSinhala,e.sortkey) from FdInstructionsEntity e where e.status = ?1")
    List<FdInstructionsResponseBean> getAllInstructionsSinhala(String status);

    @Query("select new com.epic.fdservice.models.FdInstructionsResponseBean(e.instructionTamil,e.sortkey) from FdInstructionsEntity e where e.status = ?1")
    List<FdInstructionsResponseBean> getAllInstructionsTamil(String status);
}
