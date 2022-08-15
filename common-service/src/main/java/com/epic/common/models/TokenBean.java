/**
 * @date 6/10/2022
 * @auther Pakeetharan Balasubramaniam
 **/

package com.epic.common.models;

import javax.validation.constraints.NotNull;

public class TokenBean {
    @NotNull
    public String grant_type;

    @NotNull
    public String client_id;

    @NotNull
    public String client_secret;

    @NotNull
    public String auth_type;

    @NotNull
    public String secret;

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
