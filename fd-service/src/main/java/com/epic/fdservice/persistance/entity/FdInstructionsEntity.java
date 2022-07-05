package com.epic.fdservice.persistance.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "FD_INSTRUCTIONS")
public class FdInstructionsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "INSTRUCTIONID")
    private String instructionid;
    @Basic
    @Column(name = "SORTKEY")
    private int sortkey;
    @Basic
    @Column(name = "INSTRUCTION_ENGLISH")
    private String instructionEnglish;
    @Basic
    @Column(name = "INSTRUCTION_TAMIL")
    private String instructionTamil;
    @Basic
    @Column(name = "INSTRUCTION_SINHALA")
    private String instructionSinhala;
    @Basic
    @Column(name = "STATUS")
    private String status;
    @Basic
    @Column(name = "CREATEDTIME")
    private Timestamp createdtime;
    @Basic
    @Column(name = "LASTUPDATEDTIME")
    private Timestamp lastupdatedtime;
    @Basic
    @Column(name = "LASTUPDATEDUSER")
    private String lastupdateduser;

    public String getInstructionid() {
        return instructionid;
    }

    public void setInstructionid(String instructionid) {
        this.instructionid = instructionid;
    }

    public int getSortkey() {
        return sortkey;
    }

    public void setSortkey(int sortkey) {
        this.sortkey = sortkey;
    }

    public String getInstructionEnglish() {
        return instructionEnglish;
    }

    public void setInstructionEnglish(String instructionEnglish) {
        this.instructionEnglish = instructionEnglish;
    }

    public String getInstructionTamil() {
        return instructionTamil;
    }

    public void setInstructionTamil(String instructionTamil) {
        this.instructionTamil = instructionTamil;
    }

    public String getInstructionSinhala() {
        return instructionSinhala;
    }

    public void setInstructionSinhala(String instructionSinhala) {
        this.instructionSinhala = instructionSinhala;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Timestamp createdtime) {
        this.createdtime = createdtime;
    }

    public Timestamp getLastupdatedtime() {
        return lastupdatedtime;
    }

    public void setLastupdatedtime(Timestamp lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
    }

    public String getLastupdateduser() {
        return lastupdateduser;
    }

    public void setLastupdateduser(String lastupdateduser) {
        this.lastupdateduser = lastupdateduser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FdInstructionsEntity that = (FdInstructionsEntity) o;
        return sortkey == that.sortkey && Objects.equals(instructionid, that.instructionid) && Objects.equals(instructionEnglish, that.instructionEnglish) && Objects.equals(instructionTamil, that.instructionTamil) && Objects.equals(instructionSinhala, that.instructionSinhala) && Objects.equals(status, that.status) && Objects.equals(createdtime, that.createdtime) && Objects.equals(lastupdatedtime, that.lastupdatedtime) && Objects.equals(lastupdateduser, that.lastupdateduser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instructionid, sortkey, instructionEnglish, instructionTamil, instructionSinhala, status, createdtime, lastupdatedtime, lastupdateduser);
    }
}
