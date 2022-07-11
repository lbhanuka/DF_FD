package com.epic.fdservice.persistance.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "FD_INSTRUCTIONS_IMAGES")
public class FdInstructionsImagesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "IMAGE_ENGLISH")
    private byte[] imageEnglish;

    @Column(name = "IMAGE_TAMIL")
    private byte[] imageTamil;

    @Column(name = "IMAGE_SINHALA")
    private byte[] imageSinhala;

    @Column(name = "CREATEDTIME", nullable = false)
    private Instant createdtime;

    @Column(name = "LASTUPDATEDTIME", nullable = false)
    private Instant lastupdatedtime;

    @Column(name = "LASTUPDATEDUSER", nullable = false, length = 64)
    private String lastupdateduser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageEnglish() {
        return imageEnglish;
    }

    public void setImageEnglish(byte[] imageEnglish) {
        this.imageEnglish = imageEnglish;
    }

    public byte[] getImageTamil() {
        return imageTamil;
    }

    public void setImageTamil(byte[] imageTamil) {
        this.imageTamil = imageTamil;
    }

    public byte[] getImageSinhala() {
        return imageSinhala;
    }

    public void setImageSinhala(byte[] imageSinhala) {
        this.imageSinhala = imageSinhala;
    }

    public Instant getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Instant createdtime) {
        this.createdtime = createdtime;
    }

    public Instant getLastupdatedtime() {
        return lastupdatedtime;
    }

    public void setLastupdatedtime(Instant lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
    }

    public String getLastupdateduser() {
        return lastupdateduser;
    }

    public void setLastupdateduser(String lastupdateduser) {
        this.lastupdateduser = lastupdateduser;
    }

}