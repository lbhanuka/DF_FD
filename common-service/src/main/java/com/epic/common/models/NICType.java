package com.epic.common.models;

/**
 * @author gayan_s
 * @version V1.0.0
 * @created 19/11/2021 - 8:30 AM
 * @project SWITCH
 */
public enum NICType
{

    NEW("New NIC") , OLD("Old NIC");

    private String description;

    NICType(String description)
    {

        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
