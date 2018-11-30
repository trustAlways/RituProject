package com.example.candid_20.dcrapp.bean.DCR_REVIEW_DATA;

public class dcr_review_docter_reminder
{
    String doctorRem_name;
    String specialiti;
    String rem_doc_gift;
    String rem_gift_val_doc;

    public dcr_review_docter_reminder(String doctorRem_name, String specialiti, String rem_doc_gift, String rem_gift_val_doc) {
        this.doctorRem_name = doctorRem_name;
        this.specialiti = specialiti;
        this.rem_doc_gift = rem_doc_gift;
        this.rem_gift_val_doc = rem_gift_val_doc;
    }

    public String getDoctorRem_name() {
        return doctorRem_name;
    }

    public void setDoctorRem_name(String doctorRem_name) {
        this.doctorRem_name = doctorRem_name;
    }

    public String getSpecialiti() {
        return specialiti;
    }

    public void setSpecialiti(String specialiti) {
        this.specialiti = specialiti;
    }

    public String getRem_doc_gift() {
        return rem_doc_gift;
    }

    public void setRem_doc_gift(String rem_doc_gift) {
        this.rem_doc_gift = rem_doc_gift;
    }

    public String getRem_gift_val_doc() {
        return rem_gift_val_doc;
    }

    public void setRem_gift_val_doc(String rem_gift_val_doc) {
        this.rem_gift_val_doc = rem_gift_val_doc;
    }
}
