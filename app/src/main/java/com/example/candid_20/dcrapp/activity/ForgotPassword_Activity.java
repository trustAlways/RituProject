package com.example.candid_20.dcrapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.storage.MySharedPref;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ForgotPassword_Activity extends AppCompatActivity implements View.OnClickListener {
Button btn_login;
TextView txt_company_nm,txt_txt;
    ImageView img_logo;
    Bundle bundle;
MySharedPref sp;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);



        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUI();
        getBundleData2();

      bundle = getIntent().getExtras();
      System.out.println("sout "+bundle.getString("fogot"));
     if(bundle.getString("fogot").equalsIgnoreCase("1"))
     {
         getSavedData();
     }

    }

    private void getBundleData2() {

       bundle = getIntent().getExtras();
        System.out.println("sout "+bundle.getString("fogot"));
        if(bundle.getString("fogot").equalsIgnoreCase("0"))
        {
            txt_txt.setText("Forgot Company ID");
            txt_company_nm.setText("Daily Reporting");
        }
        else
        {
            txt_txt.setText("Forgot Password");

            img_logo.setVisibility(View.VISIBLE);
            txt_company_nm.setVisibility(View.VISIBLE);
        }
    }

    private void getSavedData()
    {
        sp = new MySharedPref();

        String companyname = sp.getData(getApplicationContext(),"company_name","");
        String  company_logo = sp.getData(getApplicationContext(),"company_logo","");

        String upperString_str_interior_name = companyname.substring(0, 1).toUpperCase() + companyname.substring(1);
        txt_company_nm.setText(upperString_str_interior_name);

        if (!company_logo.equalsIgnoreCase(""))
        {
            Glide.with(ForgotPassword_Activity.this).load(company_logo).error(R.drawable.default_img).into(img_logo);
        }

    }

    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
    private void initUI() {

        txt_company_nm = (TextView)findViewById(R.id.txt_comapny_name);
        txt_txt = (TextView)findViewById(R.id.txt);

        img_logo=(ImageView) findViewById(R.id.img_logo_fp);

        // ---------------------------- For Casting Elements -------------------------------------------------------------------------------//
//Casting Button for Login
        btn_login=(Button)findViewById(R.id.btn_login);
//casting textview for company name
        // ---------------------------- For  OnClickListener -------------------------------------------------------------------------------//
//For Button Ok
        btn_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==btn_login)
        {
            bundle = getIntent().getExtras();
            System.out.println("sout "+bundle.getString("fogot"));
            if(bundle.getString("fogot").equalsIgnoreCase("0"))
            {
                //Intent Call for Login
                Intent ii=new Intent(ForgotPassword_Activity.this,Comapany_Id_Login.class);
                startActivity(ii);
            }
            else
            {//Intent Call for Login
                Intent ii=new Intent(ForgotPassword_Activity.this,LoginActivity.class);
                startActivity(ii);
            }

        }
    }
}
