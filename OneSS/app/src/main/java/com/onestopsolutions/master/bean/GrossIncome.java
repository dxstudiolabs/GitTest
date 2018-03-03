package com.onestopsolutions.master.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Darpan on 26-Feb-18.
 */

public class GrossIncome {

    @SerializedName("date")
    String mDate;
    @SerializedName("year")
    int mYear;
    @SerializedName("Gross Income")
    float mGrossIncome;

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

    public float getGrossIncome() {
        return mGrossIncome;
    }

    public void setGrossIncome(float mGrossIncome) {
        this.mGrossIncome = mGrossIncome;
    }

}
