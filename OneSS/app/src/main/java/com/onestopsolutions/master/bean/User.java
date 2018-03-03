package com.onestopsolutions.master.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Darpan on 17-Feb-18.
 */

public class User {
    @SerializedName("userID")
    String mUserID;
    @SerializedName("Name")
    String mName;
    @SerializedName("email")
    String mEmail;
    @SerializedName("last_modified")
    String mLastModified;

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getLastModified() {
        return mLastModified;
    }

    public void setLastModified(String mLastModified) {
        this.mLastModified = mLastModified;
    }
}
