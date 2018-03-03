package com.onestopsolutions.master.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Darpan on 17-Feb-18.
 */

public class Order {
    @SerializedName("userID")
    private String mUserID;
    @SerializedName("Name")
    private String mUserName;
    @SerializedName("order_id")
    private String mOrderID;
    @SerializedName("order_status")
    private int mOrderStatus;
    @SerializedName("Date")
    private String mOrderDate;
    @SerializedName("name")
    private String mBookName;
    @SerializedName("book_type")
    private String mBookType;

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getOrderID() {
        return mOrderID;
    }

    public void setOrderID(String mOrderID) {
        this.mOrderID = mOrderID;
    }

    public int getOrderStatus() {
        return mOrderStatus;
    }

    public void setOrderStatus(int mOrderStatus) {
        this.mOrderStatus = mOrderStatus;
    }

    public String getOrderDate() {
        return mOrderDate;
    }

    public void setOrderDate(String mOrderDate) {
        this.mOrderDate = mOrderDate;
    }

    public String getBookName() {
        return mBookName;
    }

    public void setBookName(String mBookName) {
        this.mBookName = mBookName;
    }

    public String getBookType() {
        return mBookType;
    }

    public void setBookType(String mBookType) {
        this.mBookType = mBookType;
    }
}
