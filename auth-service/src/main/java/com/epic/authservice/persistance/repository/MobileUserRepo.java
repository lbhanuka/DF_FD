package com.epic.authservice.persistance.repository;

import com.epic.authservice.persistance.entity.ShMobileUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileUserRepo extends JpaRepository<ShMobileUserEntity,String> {

    boolean existsByDeviceid(String deviceId);

    ShMobileUserEntity findByDeviceid(String deviceId);

}
