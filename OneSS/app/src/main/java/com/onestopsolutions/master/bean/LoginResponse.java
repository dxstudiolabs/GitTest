package com.onestopsolutions.master.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Darpan on 17-Feb-18.
 */

public class LoginResponse {

    @SerializedName("userID")
    private String mUserId;
    @SerializedName("role")
    private String mRole;
    @SerializedName("error")
    private Boolean mError;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("OneSS_TOKEN")
    private String mToken;

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }


    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getRole() {
        return mRole;
    }

    public void setRole(String mRole) {
        this.mRole = mRole;
    }


    public boolean getError() {
        return mError;
    }

    public void setError(Boolean error) {
        mError = error;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}
