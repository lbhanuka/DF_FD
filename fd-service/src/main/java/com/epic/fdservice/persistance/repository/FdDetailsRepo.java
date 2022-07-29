package com.epic.fdservice.persistance.repository;

import com.epic.fdservice.models.CrmFdDetailsBean;
import com.epic.fdservice.persistance.entity.FdDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FdDetailsRepo extends JpaRepository<FdDetailsEntity,String> {

    @Query("select new com.epic.fdservice.models.CrmFdDetailsBean(e.fdaccountnumber,e.amount,e.createdtime,e.tenure,e.rate,rn.description,e.interestcreditaccount,e.maturitycreditaccount) from FdDetailsEntity e left join FdRenewalTypesEntity rn on e.renewalinstruction = rn.renewaltypecode where e.nic = ?1")
    List<CrmFdDetailsBean> findByNic(String nic);

    @Query("select new com.epic.fdservice.models.CrmFdDetailsBean(e.fdaccountnumber,e.amount,e.createdtime,e.tenure,e.rate,rn.description,e.interestcreditaccount,e.maturitycreditaccount) from FdDetailsEntity e left join FdRenewalTypesEntity rn on e.renewalinstruction = rn.renewaltypecode where e.cif = ?1")
    List<CrmFdDetailsBean>  findByCif(String cif);

    @Query("select max(e.requestid) from FdDetailsEntity e")
    String getMaxId();

    @Query("select e.idnumber from ShMobileUserEntity e where e.deviceid = ?1")
    String getNicByDeviceId(String deviceId);
}
