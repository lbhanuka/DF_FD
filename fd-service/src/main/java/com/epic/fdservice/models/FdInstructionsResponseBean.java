package com.epic.fdservice.models;

public class FdInstructionsResponseBean {

    String instruction;
    int sortKey;

    public FdInstructionsResponseBean(String instruction, int sortKey) {
        this.instruction = instruction;
        this.sortKey = sortKey;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public int getSortKey() {
        return sortKey;
    }

    public void setSortKey(int sortKey) {
        this.sortKey = sortKey;
    }
}
