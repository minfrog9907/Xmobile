package com.example.hp.xmoblie.Items;

/**
 * Created by HP on 2017-10-16.
 */

public class LoginItem {
    int status;
    String token;
    String username;
    int privilege;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;

    }

    public int getPrivilege() {
        return privilege;
    }

    public int getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
