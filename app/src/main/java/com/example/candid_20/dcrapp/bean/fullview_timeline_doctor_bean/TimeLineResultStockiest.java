package com.example.candid_20.dcrapp.bean.fullview_timeline_doctor_bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeLineResultStockiest {
    @SerializedName("dcr_chemist_stockist_live_table_id")
    @Expose
    private String dcrChemistStockistLiveTableId;
    @SerializedName("checkin_time")
    @Expose
    private String checkinTime;
    @SerializedName("checkout_time")
    @Expose
    private String checkoutTime;
    @SerializedName("checkin_lat")
    @Expose
    private String checkinLat;
    @SerializedName("checkin_long")
    @Expose
    private String checkinLong;
    @SerializedName("checkout_lat")
    @Expose
    private String checkoutLat;
    @SerializedName("checkout_long")
    @Expose
    private String checkoutLong;
    @SerializedName("stockist_name")
    @Expose
    private String stockistName;

    @SerializedName("chemist")
    @Expose
    private String chemist;

    public String getDcrChemistStockistLiveTableId() {
        return dcrChemistStockistLiveTableId;
    }

    public void setDcrChemistStockistLiveTableId(String dcrChemistStockistLiveTableId) {
        this.dcrChemistStockistLiveTableId = dcrChemistStockistLiveTableId;
    }

    public String getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public String getCheckinLat() {
        return checkinLat;
    }

    public void setCheckinLat(String checkinLat) {
        this.checkinLat = checkinLat;
    }

    public String getCheckinLong() {
        return checkinLong;
    }

    public void setCheckinLong(String checkinLong) {
        this.checkinLong = checkinLong;
    }

    public String getCheckoutLat() {
        return checkoutLat;
    }

    public void setCheckoutLat(String checkoutLat) {
        this.checkoutLat = checkoutLat;
    }

    public String getCheckoutLong() {
        return checkoutLong;
    }

    public void setCheckoutLong(String checkoutLong) {
        this.checkoutLong = checkoutLong;
    }

    public String getStockistName() {
        return stockistName;
    }

    public void setStockistName(String stockistName) {
        this.stockistName = stockistName;
    }

    public String getChemist() {
        return chemist;
    }

    public void setChemist(String chemist) {
        this.chemist = chemist;
    }
}
