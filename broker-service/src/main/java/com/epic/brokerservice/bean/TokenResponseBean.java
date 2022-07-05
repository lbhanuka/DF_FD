/**
 * @date 6/10/2022
 * @auther Pakeetharan Balasubramaniam
 **/

package com.epic.brokerservice.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseBean {
    public String access_token;
    public String token_type;
    public String refresh_token;
    public int expires_in;
    public String scope;
}
