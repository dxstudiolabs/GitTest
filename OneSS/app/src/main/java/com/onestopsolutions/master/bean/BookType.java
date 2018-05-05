package com.onestopsolutions.master.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Darpan on 05-May-18.
 */

public class BookType {
    @SerializedName("book_type")
    String mBookType;

    public String getBookType() {
        return mBookType;
    }

    public void setBookType(String mBookType) {
        this.mBookType = mBookType;
    }
}
