package com.epic.fdservice.persistance.repository;

import com.epic.fdservice.models.FdProduct;
import com.epic.fdservice.persistance.entity.FdProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FdProductsRepo extends JpaRepository<FdProductsEntity,String> {

    @Query("select new com.epic.fdservice.models.FdProduct(e.productCode,e.interestType,e.productType,e.allowedviaapp) from FdProductsEntity e where e.allowedviaapp = :allowedViaApp and (:interestType is null or e.interestType = :interestType) and (:productType is null or e.productType = :productType)")
    List<FdProduct> getAllFdProducts(String allowedViaApp, String interestType, String productType);

    FdProductsEntity findByProductCode(String schemeCode);

}
