package com.epic.brokerservice.bean;

import lombok.Data;

@Data
public class FinacleRequestBean {

    String app_id;
    Object request_data;
    String request_id;

}
