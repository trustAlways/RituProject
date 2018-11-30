package com.example.candid_20.dcrapp.bean.DCR_REVIEW_DATA;

public class dcr_review_docter
{
    String doc_name;
    String speciality;
    String product1;
    String product1_value;
    String product2;
    String product2_value;
    String product3;
    String product3_value;
    String gift;
    String gift_value;
    String call_duration;
    String visit;
    String city;

    public dcr_review_docter(String doc_name, String speciality, String product1,
                             String product1_value, String product2, String product2_value, String product3,
                             String product3_value, String gift, String gift_value, String call_duration,String visit, String city) {
        this.doc_name = doc_name;
        this.speciality = speciality;
        this.product1 = product1;
        this.product1_value = product1_value;
        this.product2 = product2;
        this.product2_value = product2_value;
        this.product3 = product3;
        this.product3_value = product3_value;
        this.gift = gift;
        this.gift_value = gift_value;
        this.call_duration = call_duration;
        this.visit = visit;
        this.city = city;

    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getProduct1() {
        return product1;
    }

    public void setProduct1(String product1) {
        this.product1 = product1;
    }

    public String getProduct1_value() {
        return product1_value;
    }

    public void setProduct1_value(String product1_value) {
        this.product1_value = product1_value;
    }

    public String getProduct2() {
        return product2;
    }

    public void setProduct2(String product2) {
        this.product2 = product2;
    }

    public String getProduct2_value() {
        return product2_value;
    }

    public void setProduct2_value(String product2_value) {
        this.product2_value = product2_value;
    }

    public String getProduct3() {
        return product3;
    }

    public void setProduct3(String product3) {
        this.product3 = product3;
    }

    public String getProduct3_value() {
        return product3_value;
    }

    public void setProduct3_value(String product3_value) {
        this.product3_value = product3_value;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public String getGift_value() {
        return gift_value;
    }

    public void setGift_value(String gift_value) {
        this.gift_value = gift_value;
    }

    public String getCall_duration() {
        return call_duration;
    }

    public void setCall_duration(String call_duration) {
        this.call_duration = call_duration;
    }

    public String getVisit() {
        return visit;
    }

    public void setVisit(String visit) {
        this.visit = visit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
