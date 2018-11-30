package com.example.candid_20.dcrapp.bean.for_search_doctor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchDoctor_LxDetails_Bean {

    @SerializedName("doctor_id")
    @Expose
    private String doctorId;
    @SerializedName("doctor_name")
    @Expose
    private String doctorName;
    @SerializedName("speciality_id")
    @Expose
    private String specialityId;
    @SerializedName("speciality")
    @Expose
    private String speciality;
    @SerializedName("clinic_address")
    @Expose
    private String clinic_address;

    public String getAddress() {
        return clinic_address;
    }

    public void setAddress(String address) {
        this.clinic_address = address;
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

    public String getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(String specialityId) {
        this.specialityId = specialityId;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
