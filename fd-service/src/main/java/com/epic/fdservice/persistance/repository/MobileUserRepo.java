package com.epic.fdservice.persistance.repository;

import com.epic.fdservice.persistance.entity.ShMobileUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileUserRepo extends JpaRepository<ShMobileUserEntity,String> {

    boolean existsByDeviceid(String deviceId);

    @Query("select m.email from ShMobileUserEntity m where m.deviceid = ?1")
    String findEmailByDeviceid(String deviceId);

}
