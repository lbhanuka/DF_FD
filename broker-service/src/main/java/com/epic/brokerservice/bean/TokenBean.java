/**
 * @date 6/10/2022
 * @auther Pakeetharan Balasubramaniam
 **/

package com.epic.brokerservice.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenBean {
    @NotNull
    public String grant_type;

    @NotNull
    public String client_id;

    @NotNull
    public String auth_type;

    @NotNull
    public String secret;
}
