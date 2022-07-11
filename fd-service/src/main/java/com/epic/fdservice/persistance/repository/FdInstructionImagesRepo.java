package com.epic.fdservice.persistance.repository;

import com.epic.fdservice.models.FdInstructionImagesResponseBean;
import com.epic.fdservice.models.FdInstructionsResponseBean;
import com.epic.fdservice.persistance.entity.FdInstructionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FdInstructionImagesRepo  extends JpaRepository<FdInstructionsEntity,String> {

    @Query("select new com.epic.fdservice.models.FdInstructionImagesResponseBean(e.imageEnglish) from FdInstructionsImagesEntity e")
    FdInstructionImagesResponseBean getInstructionImageEnglish();

    @Query("select new com.epic.fdservice.models.FdInstructionImagesResponseBean(e.imageSinhala) from FdInstructionsImagesEntity e")
    FdInstructionImagesResponseBean getInstructionImageSinhala();

    @Query("select new com.epic.fdservice.models.FdInstructionImagesResponseBean(e.imageTamil) from FdInstructionsImagesEntity e")
    FdInstructionImagesResponseBean getInstructionImageTamil();

}
