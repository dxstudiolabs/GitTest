package com.onestopsolutions.master.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Darpan on 26-Feb-18.
 */

public class NetIncome {

    @SerializedName("date")
    String mDate;
    @SerializedName("year")
    int mYear;
    @SerializedName("Net Income")
    float mNetIncome;

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int mYear) {
        this.mYear = mYear;
    }

    public float getNetIncome() {
        return mNetIncome;
    }

    public void setNetIncome(float mNetIncome) {
        this.mNetIncome = mNetIncome;
    }
}
