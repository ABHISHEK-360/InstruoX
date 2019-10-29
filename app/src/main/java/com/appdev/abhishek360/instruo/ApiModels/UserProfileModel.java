package com.appdev.abhishek360.instruo.ApiModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileModel {
    @Expose
    @SerializedName("success")
    private boolean success = true;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("college")
    private String college;
    @Expose
    @SerializedName("contact")
    private String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }
}
