package com.onestopsolutions.master.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Darpan on 09-Mar-18.
 */

public class Subject {
    @SerializedName("id")
    int id;
    @SerializedName("subject_img_loc")
    String imgLocation;
    @SerializedName("name")
    String subjectName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgLocation() {
        return imgLocation;
    }

    public void setImgLocation(String imgLocation) {
        this.imgLocation = imgLocation;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}



