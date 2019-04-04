package com.example.candid_20.dcrapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.constant.LocationMonitorService;
import com.example.candid_20.dcrapp.constant.Utils;
import com.example.candid_20.dcrapp.other.URLs;
import com.example.candid_20.dcrapp.storage.MySharedPref;
import com.example.candid_20.dcrapp.volleyconnector.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar loader;
    boolean ischeckedpermission;

     EditText edt_empid,edt_emppassword;
     Button btn_login;
     TextView txt_empid_error,txt_comapny_name,txt_emppassword_error;
     ImageView img_logo;
     RelativeLayout rr_forgot_password;
     String company_name,company_logo;
     MySharedPref sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        // ---------------------------- For Stored Data -------------------------------------------------------------------------------//
        getSaveddata();
        // ---------------------------- For get company logo Data -------------------------------------------------------------------------------//
        getCompanyLogo();
        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUI();

        // ---------------------------- For Check Permission -------------------------------------------------------------------------------//
        checkForPermission();
    }


    // ---------------------------- For Get Stored Data -------------------------------------------------------------------------------//
    private void getSaveddata() {


        sp = new MySharedPref();
       //Casting ProgressBar for loading
        loader=(ProgressBar)findViewById(R.id.loader);

        company_name = sp.getData(getApplicationContext(),"company_name","");
        company_logo = sp.getData(getApplicationContext(),"company_logo","");
        System.out.println("company name  : "+company_name + company_logo);

        //Casting ImageView for Company Logo
        img_logo=(ImageView) findViewById(R.id.img_logo_login);

        if (!company_logo.equalsIgnoreCase("") )
        {
            System.out.println("logo "+company_logo);
            Glide.with(LoginActivity.this).load(company_logo).error(R.drawable.default_img).into(img_logo);
        }
        else
        {
            img_logo.setImageResource(R.drawable.default_img);
        }

    }



    // ---------------------------- For Check Permission -------------------------------------------------------------------------------//
    private void checkForPermission() {

        //Check permission for Location
        int permissionCheckForFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheckForCorseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheckForReadcalender = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR);
        int permissionCheckForwritecalender = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);

        if (permissionCheckForFineLocation != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForCorseLocation != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForReadcalender != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForwritecalender != PackageManager.PERMISSION_GRANTED) {

            ischeckedpermission=false;

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_CALENDAR,
                            Manifest.permission.WRITE_CALENDAR

                    },
                    1001);

            // getLocation();


        }
        else
        {
            ischeckedpermission=true;
           /* getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, new HomeFragments())
                    .commit();*/

        }



    }


    private void initUI() {
// ---------------------------- For Casting Elements -------------------------------------------------------------------------------//


        //Casting Edittext for Employee Id
        edt_empid=(EditText)findViewById(R.id.edt_empid);
        //Casting Edittext for Employee password
        edt_emppassword=(EditText)findViewById(R.id.edt_emppassword);

        //Casting RelativeLayout for Employee Forgot Password
        rr_forgot_password=(RelativeLayout)findViewById(R.id.rr_forgot_password);
        //Casting TextView for Employee Id Error
        txt_empid_error=(TextView)findViewById(R.id.txt_empid_error);

        //Casting TextView for Employee Password Error
        txt_emppassword_error=(TextView)findViewById(R.id.txt_emppassword_error);

        //Casting TextView for Company Name
        txt_comapny_name=(TextView)findViewById(R.id.txt_comapny_name);
        String upperString_str_interior_name = company_name.substring(0, 1).toUpperCase() + company_name.substring(1);
        txt_comapny_name.setText(upperString_str_interior_name);


        //Casting Button for login
        btn_login=(Button) findViewById(R.id.btn_login);


        // ---------------------------- For  OnClickListener -------------------------------------------------------------------------------//

//For Button Login
        btn_login.setOnClickListener(this);

        //For RelativeLayout Forgot Password
        rr_forgot_password.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if(v==btn_login)
        {
            //For Validation
            Validate1();
        }

        if(v==rr_forgot_password)
        {

            //Intent Call for Forgot Password
            Intent ii=new Intent(LoginActivity.this,ForgotPassword_Activity.class);
            ii.putExtra("fogot","1");
            startActivity(ii);
        }
    }
    // ---------------------------- For  Validation -------------------------------------------------------------------------------//

    private void Validate1() {
        String str_empid = edt_empid.getText().toString();
        String str_emppassword = edt_emppassword.getText().toString();

        boolean iserror = false;

        if (str_empid.equalsIgnoreCase("")) {
            iserror = true;
//Set Employee Id Text Error Visibility Visible
            txt_empid_error.setVisibility(View.VISIBLE);
            txt_empid_error.setText("This field is required.");
        }
        else {
            //Set Employee Id Text Error Visibility Gone
            txt_empid_error.setVisibility(View.GONE);
        }
        if (str_emppassword.equalsIgnoreCase("")) {
            iserror = true;
//Set Employee Password Text Error Visibility Visible
            txt_emppassword_error.setVisibility(View.VISIBLE);
            txt_emppassword_error.setText("This field is required.");
        }

        else if(edt_emppassword.getText().length()<6)
        {
            iserror = true;

            txt_emppassword_error.setVisibility(View.VISIBLE);
            txt_emppassword_error.setText("Wrong employee code or Password.");
        }
        else {
            //Set Employee Password Text Error Visibility Gone
            txt_emppassword_error.setVisibility(View.GONE);
        }

        if(!iserror)

        {
            //Set All Error Visibility Gone
            txt_empid_error.setVisibility(View.GONE);
            txt_emppassword_error.setVisibility(View.GONE);

          /*  //For Intent Call
            Intent ii=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(ii);*/


            // Call WebService for SignUp
            callWebService_forEmployeeLogin(str_empid,str_emppassword);

        }


    }
    // ---------------------------- For  WebService Call Method -------------------------------------------------------------------------------//
    private void callWebService_forEmployeeLogin(String str_comid2,String str_emppassword2) {
        // Check Internet Connectivity
        if (Utils.isConnected(getApplicationContext())) {
            //Call WebService
            log_In(str_comid2,str_emppassword2);

        }

else
        {
            Toast.makeText(LoginActivity.this, "Please Check network conection..", Toast.LENGTH_SHORT).show();

        }
    }
    // ---------------------------- For  WebService Call Method of Login-------------------------------------------------------------------------------//
    private void log_In(final String str_comid3, final String str_emppassword3) {

        loader.setVisibility(View.VISIBLE);

         String url = "http://dailyreporting.in/"+company_name+"/api/login";
         System.out.println("sout url"+ url);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d("LogIn", "Log In response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {

                        String result=jObj.getString("result");
                        System.out.println("Result Login***"+result);
                        JSONObject jsonObject22=new JSONObject(result);
                        String message=jObj.getString("message");

                        Toast.makeText(LoginActivity.this, message,Toast.LENGTH_SHORT).show();

                        MySharedPref sp=new MySharedPref();
                        sp.saveData(getApplicationContext(),"ldata",result+"");

                        Intent ii=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(ii);

                        Intent iii=new Intent(LoginActivity.this, LocationMonitorService.class);
                        startService(iii);

                        loader.setVisibility(View.GONE);


                    } else {
                        loader.setVisibility(View.GONE);

                        String errorMsg = jObj.getString("message");
                        Toast.makeText(LoginActivity.this, errorMsg,Toast.LENGTH_SHORT).show();
                     //   Toast.makeText(LoginActivity.this,"You are deactivated by Admin.",Toast.LENGTH_SHORT).show();


                        Log.e("errorMsg", errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.GONE);
                Log.e("LogIn Response", "LogIn  Error: " + error.getMessage());
                try {
                    Toast.makeText(LoginActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }) {


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
                params.put("employee_code",str_comid3);
                params.put("password",str_emppassword3);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(LoginActivity.this).addToRequestQueue(strReq, cancel_req_tag);
    }

    // ---------------------------- OnBackPressed Click -------------------------------------------------------------------------------//
    @Override
    public void onBackPressed() {




            //  super.onBackPressed();
            AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this);
            dialog.setMessage("Are you sure you want to exit?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Log.e("hello >>>", "....");
                    Intent i = new Intent(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_HOME);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    //get gps
                }
            });
            dialog.setNegativeButton(LoginActivity.this.getString(R.string.no), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                }
            });
            dialog.show();
            //additional code

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("", "Permission callback called-------");
        switch (requestCode) {
            case 1001: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CALENDAR, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_CALENDAR, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission. ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION )== PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.READ_CALENDAR )== PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_CALENDAR )== PackageManager.PERMISSION_GRANTED)
                    {
                        Log.d("", "sms & location services permission granted");
                        ischeckedpermission=true;

                        // process the normal flow
                        //else any one or both the permissions are not granted


                    }
                    else {
                        Log.d("", "Some permissions are not granted ask again ");

                        ischeckedpermission=false;

                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALENDAR)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR))

                        {
                            showDialogOK("These Permission's must required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkForPermission();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    //dialog.dismiss();
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {

                            showDialogOK("These Permission's must required for this app",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ischeckedpermission = false;

                                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", getPackageName(), null)));

                                        }

                                    });


                        }



                        //                            //proceed with logic by disabling the related features or quit the app.

                    }
                }
            }
        }

    }


    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                //.setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    // ---------------------------- For Check Permission -------------------------------------------------------------------------------//
    // ---------------------------- For  WebService Call Method -------------------------------------------------------------------------------//
    private void getCompanyLogo() {

        loader.setVisibility(View.VISIBLE);

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

                        sp.saveData(getApplicationContext(),"company_logo", company_logo);

                        String message=jObj.getString("message");

                        loader.setVisibility(View.GONE);


                    }
                    else {
                      loader.setVisibility(View.GONE);

                        String errorMsg = jObj.getString("message");
                        Toast.makeText(LoginActivity.this, errorMsg,Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, "server not responding.",Toast.LENGTH_SHORT).show();

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
        AppSingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest,  "area");

    }


}
