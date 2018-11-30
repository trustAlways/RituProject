package com.example.candid_20.dcrapp.bean.for_dcr_products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dcr_Products_LxDetails_Bean {

    @SerializedName("index")
    @Expose
    private String index;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("e_p_i_table_id")
    @Expose
    private String ePITableId;
    @SerializedName("pname")
    @Expose
    private String pname;
    @SerializedName("total_qty")
    @Expose
    private String totalQty;
    @SerializedName("select_product_value")
    @Expose
    private String selectProductValue;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }



    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getEPITableId() {
        return ePITableId;
    }

    public void setEPITableId(String ePITableId) {
        this.ePITableId = ePITableId;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getSelectProductValue() {
        return selectProductValue;
    }

    public void setSelectProductValue(String selectProductValue) {
        this.selectProductValue = selectProductValue;
    }
}
