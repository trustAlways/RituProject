package com.example.candid_20.dcrapp.bean.fullview_timeline_doctor_bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimeLineMainResult_Bean {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("result")
    @Expose
    private List<TimeLineResultDoctor_Bean> result;
    @SerializedName("message")
    @Expose
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

   /* public TimeLineMainResultDocstockiest_Bean getResult() {
        return result;
    }

    public void setResult(TimeLineMainResultDocstockiest_Bean result) {
        this.result = result;
    }
*/
    public List<TimeLineResultDoctor_Bean> getResult() {
        return result;
    }

    public void setResult(List<TimeLineResultDoctor_Bean> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }








}
