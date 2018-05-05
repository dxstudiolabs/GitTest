package com.onestopsolutions.master.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Darpan on 09-Mar-18.
 */

public class Book {
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @SerializedName("name")
    String bookName;

}
