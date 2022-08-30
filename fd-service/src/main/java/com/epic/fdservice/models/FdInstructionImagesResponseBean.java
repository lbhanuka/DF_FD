package com.epic.fdservice.models;

public class FdInstructionImagesResponseBean {

    byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public FdInstructionImagesResponseBean(byte[] image) {
        this.image = image;
    }
}

