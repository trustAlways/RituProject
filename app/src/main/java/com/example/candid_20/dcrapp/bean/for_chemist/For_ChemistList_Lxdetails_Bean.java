package com.example.candid_20.dcrapp.bean.for_chemist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class For_ChemistList_Lxdetails_Bean {
    @SerializedName("chemist_id")
    @Expose
    private String chemistId;
    @SerializedName("chemist_name")
    @Expose
    private String chemistName;
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
    private Integer region;
    @SerializedName("city")
    @Expose
    private Integer city;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_phone")
    @Expose
    private String shopPhone;
    @SerializedName("shop_address")
    @Expose
    private String shopAddress;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("active_status")
    @Expose
    private String activeStatus;
    @SerializedName("create_by")
    @Expose
    private String createBy;
    @SerializedName("approve")
    @Expose
    private String approve;

    public String getChemistId() {
        return chemistId;
    }

    public void setChemistId(String chemistId) {
        this.chemistId = chemistId;
    }

    public String getChemistName() {
        return chemistName;
    }

    public void setChemistName(String chemistName) {
        this.chemistName = chemistName;
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

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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
