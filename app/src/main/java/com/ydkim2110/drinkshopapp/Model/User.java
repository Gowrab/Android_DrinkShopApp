package com.ydkim2110.drinkshopapp.Model;

/**
 * Created by Kim Yongdae on 2018-11-30
 * email : ydkim2110@gmail.com
 */
public class User {

    private String phone;
    private String name;
    private String birthdate;
    private String address;
    private String error_msg; // It will empty if user return object

    public User() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
