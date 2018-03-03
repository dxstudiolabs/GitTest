package com.onestopsolutions.master.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Darpan on 17-Feb-18.
 */

public class LoginRequest {

    @SerializedName("userID")
    private String mUserId;
    @SerializedName("password")
    private String mPassword;
/*    @SerializedName("user_device_id")
    private String mUserDeviceId;
    @SerializedName("user_device_type")
    private String mUserDeviceType;*/

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
