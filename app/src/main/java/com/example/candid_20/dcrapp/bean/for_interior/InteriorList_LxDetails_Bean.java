package com.example.candid_20.dcrapp.bean.for_interior;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InteriorList_LxDetails_Bean {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("interior")
    @Expose
    private String interior;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }
}
