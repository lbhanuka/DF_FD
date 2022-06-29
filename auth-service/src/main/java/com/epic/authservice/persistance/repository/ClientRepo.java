package com.epic.authservice.persistance.repository;

import com.epic.authservice.persistance.entity.ShClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepo extends JpaRepository<ShClientEntity,String> {

    boolean existsByClientidAndSecretAndAuthtype(String clientId, String secret, String authType);

    @Query("select e.scope from ShClientEntity e where e.clientid = ?1")
    String getScopeById(String clientId);
}
