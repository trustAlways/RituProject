package com.example.candid_20.dcrapp.bean.fullview_timeline_doctor_bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeLineResultDoctor_Bean {
    @SerializedName("dcr_doctor_live_table_id")
    @Expose
    private String dcrDoctorLiveTableId;
    @SerializedName("dr_cin_t")
    @Expose
    private String drCinT;
    @SerializedName("dr_cout_t")
    @Expose
    private String drCoutT;
    @SerializedName("dr_cin_lat")
    @Expose
    private String drCinLat;
    @SerializedName("dr_cin_lon")
    @Expose
    private String drCinLon;
    @SerializedName("dr_cout_lat")
    @Expose
    private String drCoutLat;
    @SerializedName("dr_cout_lon")
    @Expose
    private String drCoutLon;
    @SerializedName("doctor_id")
    @Expose
    private String doctorId;
    @SerializedName("doctor_name")
    @Expose
    private String doctorName;
    @SerializedName("visit_type")
    @Expose
    private String visitType;

    public String getDcrDoctorLiveTableId() {
        return dcrDoctorLiveTableId;
    }

    public void setDcrDoctorLiveTableId(String dcrDoctorLiveTableId) {
        this.dcrDoctorLiveTableId = dcrDoctorLiveTableId;
    }

    public String getDrCinT() {
        return drCinT;
    }

    public void setDrCinT(String drCinT) {
        this.drCinT = drCinT;
    }

    public String getDrCoutT() {
        return drCoutT;
    }

    public void setDrCoutT(String drCoutT) {
        this.drCoutT = drCoutT;
    }

    public String getDrCinLat() {
        return drCinLat;
    }

    public void setDrCinLat(String drCinLat) {
        this.drCinLat = drCinLat;
    }

    public String getDrCinLon() {
        return drCinLon;
    }

    public void setDrCinLon(String drCinLon) {
        this.drCinLon = drCinLon;
    }

    public String getDrCoutLat() {
        return drCoutLat;
    }

    public void setDrCoutLat(String drCoutLat) {
        this.drCoutLat = drCoutLat;
    }

    public String getDrCoutLon() {
        return drCoutLon;
    }

    public void setDrCoutLon(String drCoutLon) {
        this.drCoutLon = drCoutLon;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }
}
