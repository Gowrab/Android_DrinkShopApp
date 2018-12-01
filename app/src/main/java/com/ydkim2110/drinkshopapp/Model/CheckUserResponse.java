package com.ydkim2110.drinkshopapp.Model;

/**
 * Created by Kim Yongdae on 2018-11-30
 * email : ydkim2110@gmail.com
 */
public class CheckUserResponse {

    private boolean exists;
    private String error_msg;

    public CheckUserResponse() {
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
