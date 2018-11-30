package com.example.candid_20.dcrapp.bean.DCR_REVIEW_DATA;

public class dcr_review_chemist_visited
{
    String chemist_name;
    String stockist1;
    String stockist1_value;
    String stockist2;
    String stockist2_value;
    String stockist3;
    String stockist3_value;
    String pob_amount;
    String gift_name;
    String gift_value;
    String call_duration;

    public dcr_review_chemist_visited(String chemist_name, String stockist1, String stockist1_value,
                                      String stockist2, String stockist2_value, String stockist3,
                                      String stockist3_value, String pob_amount, String gift_name, String gift_value,String call_duration) {
        this.chemist_name = chemist_name;
        this.stockist1 = stockist1;
        this.stockist1_value = stockist1_value;
        this.stockist2 = stockist2;
        this.stockist2_value = stockist2_value;
        this.stockist3 = stockist3;
        this.stockist3_value = stockist3_value;
        this.pob_amount = pob_amount;
        this.gift_name = gift_name;
        this.gift_value = gift_value;
        this.call_duration = call_duration;
    }

    public String getChemist_name() {
        return chemist_name;
    }

    public void setChemist_name(String chemist_name) {
        this.chemist_name = chemist_name;
    }

    public String getStockist1() {
        return stockist1;
    }

    public void setStockist1(String stockist1) {
        this.stockist1 = stockist1;
    }

    public String getStockist1_value() {
        return stockist1_value;
    }

    public void setStockist1_value(String stockist1_value) {
        this.stockist1_value = stockist1_value;
    }

    public String getStockist2() {
        return stockist2;
    }

    public void setStockist2(String stockist2) {
        this.stockist2 = stockist2;
    }

    public String getStockist2_value() {
        return stockist2_value;
    }

    public void setStockist2_value(String stockist2_value) {
        this.stockist2_value = stockist2_value;
    }

    public String getStockist3() {
        return stockist3;
    }

    public void setStockist3(String stockist3) {
        this.stockist3 = stockist3;
    }

    public String getStockist3_value() {
        return stockist3_value;
    }

    public void setStockist3_value(String stockist3_value) {
        this.stockist3_value = stockist3_value;
    }

    public String getPob_amount() {
        return pob_amount;
    }

    public void setPob_amount(String pob_amount) {
        this.pob_amount = pob_amount;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
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
}
