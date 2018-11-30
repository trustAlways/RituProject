package com.example.candid_20.dcrapp.bean.for_mr;

public class all_am {

    String user_id;
    String role_id;
    String f_name;
    String l_name;
    String e_code;

    public all_am(String user_id, String role_id, String f_name, String l_name, String e_code) {
        this.user_id = user_id;
        this.role_id = role_id;
        this.f_name = f_name;
        this.l_name = l_name;
        this.e_code = e_code;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getE_code() {
        return e_code;
    }

    public void setE_code(String e_code) {
        this.e_code = e_code;
    }
}
