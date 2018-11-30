package com.example.candid_20.dcrapp.bean.for_gift_list;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Gift_List_Lxdetails_Bean {

    @SerializedName("index")
    @Expose
    private String index;
    @SerializedName("gift_table_id")
    @Expose
    private String giftTableId;
    @SerializedName("gname")
    @Expose
    private String gname;
    @SerializedName("total_qty")
    @Expose
    private String totalQty;
    @SerializedName("e_g_i_t_id")
    @Expose
    private String eGITId;

    public String getGiftTableId() {
        return giftTableId;
    }

    public void setGiftTableId(String giftTableId) {
        this.giftTableId = giftTableId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getEGITId() {
        return eGITId;
    }

    public void setEGITId(String eGITId) {
        this.eGITId = eGITId;
    }
}
