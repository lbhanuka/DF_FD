package com.epic.fdservice.util;

public class MessageVarList {

    public static final String JSON_ERROR               = "{\"error\":\"UNKOWN ERROR\"}";
    public static final String JSON_INVALID             = "{\"error\":\"INVALID INPUT\"}";
    public static final String JSON_NOTAUTHORISED       = "{\"error\":\"NOT AUTHORISED\"}";
    public static final String JSON_JWT_EXPIRED         = "{\"error\":\"TOKEN EXPIRED\"}";


    //Response Codes
    public static final String RSP_SUCCESS              = "00";
    public static final String RSP_NO_DATA_FOUND        = "01";
    public static final String RSP_NOT_AUTHORISED       = "02";
    public static final String RSP_TOKEN_EXPIRED        = "03";
    public static final String RSP_TOKEN_INVALID        = "04";
    public static final String RSP_ERROR                = "05";
    public static final String RSP_FAIL                 = "10";
    public static final String RSP_REQUEST_INVALID      = "06";

}