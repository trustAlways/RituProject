package com.example.candid_20.dcrapp.bean.for_stockiest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class For_StockiestList_Lxdetails_Bean {
    @SerializedName("stockist_id")
    @Expose
    private String stockistId;
    @SerializedName("stockist_name")
    @Expose
    private String stockistName;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("residence_address")
    @Expose
    private String residenceAddress;
    @SerializedName("residence_phone")
    @Expose
    private String residencePhone;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_phone")
    @Expose
    private String shopPhone;
    @SerializedName("shop_address")
    @Expose
    private String shopAddress;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("active_status")
    @Expose
    private String activeStatus;
    @SerializedName("create_by")
    @Expose
    private String createBy;
    @SerializedName("approve")
    @Expose
    private String approve;

    public String getStockistId() {
        return stockistId;
    }

    public void setStockistId(String stockistId) {
        this.stockistId = stockistId;
    }

    public String getStockistName() {
        return stockistName;
    }

    public void setStockistName(String stockistName) {
        this.stockistName = stockistName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(String residenceAddress) {
        this.residenceAddress = residenceAddress;
    }

    public String getResidencePhone() {
        return residencePhone;
    }

    public void setResidencePhone(String residencePhone) {
        this.residencePhone = residencePhone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }
}
