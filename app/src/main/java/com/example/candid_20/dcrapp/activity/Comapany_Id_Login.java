package com.example.candid_20.dcrapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.other.URLs;
import com.example.candid_20.dcrapp.storage.MySharedPref;
import com.example.candid_20.dcrapp.volleyconnector.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Comapany_Id_Login extends AppCompatActivity implements View.OnClickListener {


    ProgressBar loader;
    EditText edt_comid;
    RelativeLayout rr_submit, rr_submit_after;
    TextView txt_comid_error,forgot_company_id;
    MySharedPref sp;

    String ldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companyid);

        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
         //initUI1();
       /* if(ldata==null)

        {
            Intent ii = new Intent(Comapany_Id_Login.this, LoginActivity.class);
            startActivity(ii);
        }

        else if(ldata.equalsIgnoreCase("null"))
        {
            Intent ii = new Intent(Comapany_Id_Login.this, LoginActivity.class);
            startActivity(ii);
        }

        else if(ldata.equalsIgnoreCase(""))
        {
            Intent ii = new Intent(Comapany_Id_Login.this, LoginActivity.class);
            startActivity(ii);
        }

        else

        {
            Intent ii = new Intent(Comapany_Id_Login.this, MainActivity.class);
            startActivity(ii);
        }*/
         /*if(ldata!=null) {
            if (!ldata.equalsIgnoreCase("null")) {
                Intent ii = new Intent(Comapany_Id_Login.this, MainActivity.class);
                startActivity(ii);
            } else {
                Intent ii = new Intent(Comapany_Id_Login.this, LoginActivity.class);
                startActivity(ii);
            }
        }
        else {
            Intent ii = new Intent(Comapany_Id_Login.this, LoginActivity.class);
            startActivity(ii);

        }*/


        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
            initUI();
    }



    private void initUI1() {


        sp=new MySharedPref();
        ldata=sp.getData(getApplicationContext(),"ldata","null");

        System.out.println("Ldatasplash"+ldata);
        if(ldata!=null)
        {
            try {
                JSONObject jsonObject = new JSONObject(ldata);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
         /* final Thread t = new Thread() {
        @Override
        public void run() {

            try {

                sleep(3 * 1000);

                if (ldata.equals("null")) {
                    Intent ii = new Intent(Comapany_Id_Login.this, LoginActivity.class);
                    startActivity(ii);
                }
                else
                    {
                      Intent ii = new Intent(Comapany_Id_Login.this, MainActivity.class);
                      startActivity(ii);

                    }



                } catch (Exception e) {
                e.printStackTrace();
                System.out.println("$$Exception**=" + e);
            }

        }
    };
        t.start();*/
}

    private void initUI() {
// ---------------------------- For Casting Elements -------------------------------------------------------------------------------//
        sp=new MySharedPref();
//Casting Edittext for Comany Id
        edt_comid=(EditText)findViewById(R.id.edt_comid);

        //Casting TextView for Comany Id Error
        txt_comid_error=(TextView)findViewById(R.id.txt_comid_error);
        forgot_company_id=(TextView)findViewById(R.id.txt_forgort_companyid);

        //Casting ProgressBar for loading
        loader=(ProgressBar)findViewById(R.id.loader);

        //Casting RelativeLayout for Submit
        rr_submit=(RelativeLayout)findViewById(R.id.rr_submit);

        //Casting RelativeLayout for Submit After
        rr_submit_after=(RelativeLayout)findViewById(R.id.rr_submit_after);


        // ---------------------------- For  OnClickListener -------------------------------------------------------------------------------//
         //For Submit Button
        rr_submit.setOnClickListener(this);
        forgot_company_id.setOnClickListener(this);

        //For Submit After  Button
        rr_submit_after.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if(v==rr_submit)
        {
            Validate1();
        }

        if (v==forgot_company_id)
        {
            Intent intent = new Intent(this,ForgotPassword_Activity.class);
            intent.putExtra("fogot","0");
            startActivity(intent);
        }

    }
    // ---------------------------- For  validation -------------------------------------------------------------------------------//

    private void Validate1() {
        String str_comid = edt_comid.getText().toString();

        boolean iserror = false;

        if (str_comid.equalsIgnoreCase("")) {
            iserror = true;
            //Set Company Id Text Error Visibility Visible
            txt_comid_error.setVisibility(View.VISIBLE);
            txt_comid_error.setText("This field is required.");
        }
        else {
            //Set Company Id Text Error Visibility Gone
            txt_comid_error.setVisibility(View.GONE);
            }

        if(!iserror)

        {
            txt_comid_error.setVisibility(View.GONE);
            // Call WebService for SignUp
            callWebService_forCompanyIdLogin(str_comid);

        }


    }
    // ---------------------------- For  WebService Call Method -------------------------------------------------------------------------------//
    private void callWebService_forCompanyIdLogin(final String str_comid2) {

        loader.setVisibility(View.VISIBLE);


       StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_COMPANY_ID, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               Log.d("company LogIn", "Company Log In response: " + response.toString());

               try{
                   JSONObject jObj = new JSONObject(response);
                   String error = jObj.getString("status");

                   if (error.equals("success")) {

                       String result=jObj.getString("result");
                     //  String company_logo=jObj.getString("logo");

                       System.out.println("Result Login***"+result);

                       sp.saveData(getApplicationContext(),"company_name", result);
                       sp.saveData(getApplicationContext(),"company_id", str_comid2);

                       String message=jObj.getString("message");
                       Toast.makeText(Comapany_Id_Login.this, message,Toast.LENGTH_SHORT).show();

                       getCompanyLogo();



                       loader.setVisibility(View.GONE);


                   }
                   else {
                       loader.setVisibility(View.GONE);

                       String errorMsg = jObj.getString("message");
                       Toast.makeText(Comapany_Id_Login.this, errorMsg,Toast.LENGTH_SHORT).show();
                       //   Toast.makeText(LoginActivity.this,"You are deactivated by Admin.",Toast.LENGTH_SHORT).show();


                       Log.e("errorMsg", errorMsg);
                   }

               }
               catch (Exception e)
               {
                   e.printStackTrace();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(Comapany_Id_Login.this, "server not responding.",Toast.LENGTH_SHORT).show();

           }
       })
       {

           //This is for Headers If You Needed
           @Override
           public Map<String, String> getHeaders() throws AuthFailureError {
               Map<String, String> params = new HashMap<String, String>();
               params.put("X-API-KEY","TEST@123");
               return params;
           }

           @Override
           protected Map<String, String> getParams() {
               // Posting params to login url
               Map<String, String> params = new HashMap<String, String>();
               params.put("company_id",str_comid2);
               return params;
           }
       };
        // Adding request to request queue
        AppSingleton.getInstance(Comapany_Id_Login.this).addToRequestQueue(stringRequest,  "area");

    }

    // ---------------------------- For  WebService Call Method -------------------------------------------------------------------------------//
    private void getCompanyLogo() {

        loader.setVisibility(View.VISIBLE);

        String company_name = sp.getData(getApplicationContext(),"company_name","");
        final String id =  sp.getData(getApplicationContext(),"company_id", "");

        String url = "http://dailyreporting.in/"+company_name+"/api/get_companylogo";
        System.out.println("sout url, "+url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("company LogIn", "Company Log In response: " + response.toString());

                try{
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {

                        String company_logo=jObj.getString("result");
                        Log.d("company LogIn", "Company Log In response: " + company_logo);

                        sp.saveData(Comapany_Id_Login.this,"company_logo", company_logo);

                        String message=jObj.getString("message");

                        loader.setVisibility(View.GONE);

                        Intent ii=new Intent(Comapany_Id_Login.this,LoginActivity.class);
                        startActivity(ii);

                    }
                    else {
                        loader.setVisibility(View.GONE);

                        Intent ii=new Intent(Comapany_Id_Login.this,LoginActivity.class);
                        startActivity(ii);

                        String errorMsg = jObj.getString("message");
                        Toast.makeText(Comapany_Id_Login.this, errorMsg,Toast.LENGTH_SHORT).show();
                        //   Toast.makeText(LoginActivity.this,"You are deactivated by Admin.",Toast.LENGTH_SHORT).show();


                        Log.e("errorMsg", errorMsg);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Comapany_Id_Login.this, "server not responding.",Toast.LENGTH_SHORT).show();

            }
        })
        {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-API-KEY","TEST@123");
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("company_id",id);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(Comapany_Id_Login.this).addToRequestQueue(stringRequest,  "area");

    }



}
