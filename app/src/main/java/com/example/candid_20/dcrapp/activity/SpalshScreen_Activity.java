package com.example.candid_20.dcrapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.constant.LocationMonitorService;
import com.example.candid_20.dcrapp.storage.MySharedPref;

import org.json.JSONException;
import org.json.JSONObject;

public class SpalshScreen_Activity extends AppCompatActivity {
    MySharedPref sp;
    String ldata, company_name;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalshscreen_activity);

        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUI1();
    }

    private void initUI1() {

        context = SpalshScreen_Activity.this;
        sp=new MySharedPref();

        ldata=sp.getData(getApplicationContext(),"ldata","null");
        company_name =sp.getData(getApplicationContext(),"company_name","null");

    //Toast.makeText(getApplicationContext(),"Ldatasplash"+ldata,Toast.LENGTH_SHORT).show();

        System.out.println("Ldatasplash"+ldata);
        System.out.println("Ldatasplash"+company_name);

        /*if(ldata!=null)

        {
            try {
               // JSONObject jsonObject = new JSONObject(ldata);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        final Thread t = new Thread() {
            @Override
            public void run() {

                try {

                    sleep(3 * 1000);

                    if (ldata.equalsIgnoreCase("null") && !company_name.equalsIgnoreCase("null")) {

                        Intent ii = new Intent(SpalshScreen_Activity.this, LoginActivity.class);
                        startActivity(ii);
                    }
                    else if (!ldata.equalsIgnoreCase("null") && !company_name.equalsIgnoreCase("null"))
                    {
                        Intent ii = new Intent(SpalshScreen_Activity.this, MainActivity.class);
                        startActivity(ii);

                        context.startService(new Intent(SpalshScreen_Activity.this,LocationMonitorService.class));
                        System.out.println("start service");
                    }
                    else
                    {
                         Intent ii = new Intent(SpalshScreen_Activity.this, Comapany_Id_Login.class);
                         startActivity(ii);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("$$Exception**=" + e);
                }

            }
        };
        t.start();
    }
}
