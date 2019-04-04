package com.example.candid_20.dcrapp.bean;

public class ItemModel_Selected {

    private String id;
    private String product_name;
    private String product_quantity;
    private String edt_product_quantity;
    private boolean isSelected;
    private String index;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
    public String getEdt_product_quantity() {
        return edt_product_quantity;
    }

    public void setEdt_product_quantity(String edt_product_quantity) {
        this.edt_product_quantity = edt_product_quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ItemModel_Selected(String id1, String product_name1, String product_quantity1)
    {


        this.id=id1;
        this.product_name=product_name1;

        this.product_quantity=product_quantity1;


    }

    public ItemModel_Selected(String id1,String index1,
                              String edt_product_quantity1,
                              String product_name1,
                              String product_quantity1,
                              boolean isSelected1)
    {


        this.id=id1;
        this.index=index1;
        this.edt_product_quantity=edt_product_quantity1;

        this.product_name=product_name1;

        this.product_quantity=product_quantity1;
        this.isSelected=isSelected1;



    }
}
