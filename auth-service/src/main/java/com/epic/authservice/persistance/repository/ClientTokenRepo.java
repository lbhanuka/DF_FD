package com.epic.authservice.persistance.repository;

import com.epic.authservice.persistance.entity.ShClientTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientTokenRepo extends JpaRepository<ShClientTokenEntity,String> {

    boolean existsByAccesstoken(String token);

    ShClientTokenEntity findByAccesstoken(String token);

}
