package com.onestopsolutions.master.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Darpan on 26-Feb-18.
 */

public class Tax {
    @SerializedName("date")
    String mDate;
    @SerializedName("year")
    int mYear;
    @SerializedName("Tax")
    float mTax;

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

    public float getTax() {
        return mTax;
    }

    public void setTax(float mTax) {
        this.mTax = mTax;
    }
}
