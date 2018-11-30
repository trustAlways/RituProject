package com.example.candid_20.dcrapp.bean.DCR_REVIEW_DATA;

public class dcr_review_expenses {

    String expense;
    String value;

    public dcr_review_expenses(String expense, String value) {
        this.expense = expense;
        this.value = value;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
