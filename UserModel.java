package com.gmail.hafid.projekuas;

/**
 * Created by PRET-5 on 31/12/2017.
 */

public class UserModel {
    private int uid;
    private String username;

    public UserModel(Integer uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getUid() {
        return uid;
    }
}
