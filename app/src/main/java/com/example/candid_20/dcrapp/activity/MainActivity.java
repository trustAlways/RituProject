package com.example.candid_20.dcrapp.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.candid_20.dcrapp.constant.ReceiverCall;
import com.example.candid_20.dcrapp.constant.Utils;
import com.example.candid_20.dcrapp.fragments.HomeFragments;
import com.example.candid_20.dcrapp.fragments.home_menu.for_doctor_list.MyDoctor_List_Frag;
import com.example.candid_20.dcrapp.fragments.home_menu.for_view_timeline.ViewTimeline_Frag;
import com.example.candid_20.dcrapp.fragments.home_menu.view_DCR.DCR_Fullview_Frag;
import com.example.candid_20.dcrapp.fragments.home_menu.view_DCR.ViewDCRTimeline_Frag;
import com.example.candid_20.dcrapp.other.GPSTracker;
import com.example.candid_20.dcrapp.other.URLs;
import com.example.candid_20.dcrapp.storage.MySharedPref;
import com.example.candid_20.dcrapp.volleyconnector.AppSingleton;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    boolean ischeckedpermission;
    Context context;
    //------------------------------------ Initialise boolean-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    boolean isGPSEnabled = false;
    //------------------------------------ Initialise double-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    double currentlat,currentlong;
    ImageView img_slider,img_logo,img_back;
    TextView user_name_eid_role,employ_code;
    RelativeLayout rr_left;
    NavigationView navigationview;
    DrawerLayout drawer;
    MySharedPref sp;
    String err_message;
    String company_name,company_logo;
    //String Sharedpreferences Data
    String ldata,user_id4,token,first_name,last_name,empcode,role_nam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


// ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//
        getSaveddata();


        // ---------------------------- For Check Permission -------------------------------------------------------------------------------//
        checkForPermission();

        get_Data_forGetStart();
        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();

        // ---------------------------- For Get Current Location -------------------------------------------------------------------------------//
        //getLocation();

       /* LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        System.out.println("updated onReceive") ;

                        String latitude = intent.getStringExtra(LocationMonitorService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(LocationMonitorService.EXTRA_LONGITUDE);

                        if (latitude != null && longitude != null) {
                            System.out.println("updated lat long ## broadcast"+ latitude + longitude) ;
                            Update_Data_for_LatLong(latitude,longitude);
                        }
                    }
                }, new IntentFilter(LocationMonitorService.ACTION_LOCATION_BROADCAST)
        );*/


    }


    // ---------------------------- For Get Stored Data -------------------------------------------------------------------------------//
    private void getSaveddata() {


        sp = new MySharedPref();

        company_name = sp.getData(getApplicationContext(),"company_name","");

        company_logo = sp.getData(getApplicationContext(),"company_logo","null");
        System.out.println("company name  : "+company_name + company_logo);

        ldata = sp.getData(getApplicationContext(), "ldata", "null");
        Log.e("LdataHome", ldata);

        if (ldata != null)

        {
            try {
                JSONObject jsonObject = new JSONObject(ldata);
                user_id4 = jsonObject.getString("user_id");
                token = jsonObject.getString("token");
                first_name = jsonObject.getString("first_name");
                last_name = jsonObject.getString("last_name");
                role_nam = jsonObject.getString("role_name");
                empcode = jsonObject.getString("employee_code");

                Log.e("Id is@@@",user_id4);
                System.out.println("Id is***" + user_id4);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    public void refreshMyData(){
        // do your operations here.
        get_Data_forGetStart();
        System.out.println("refresh Activity main");
    }
    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
    private void initUi() {
        context = MainActivity.this;
// ---------------------------- For Casting Elements -------------------------------------------------------------------------------//
       //Casting ImageView for Slider
        img_slider=(ImageView) findViewById(R.id.img_slider);
      //Casting ImageView for Back
        img_back=(ImageView) findViewById(R.id.img_back);

        //Casting RelativeLayout for Slider
        rr_left=(RelativeLayout) findViewById(R.id.rr_left);

        //Casting NavigationView for drawer
        navigationview=(NavigationView)findViewById(R.id.navigationview1);

        //Casting DrawerLayout for drawer
        drawer=(DrawerLayout)findViewById(R.id.drawer_layout);

        // ---------------------------- Set Header-------------------------------------------------------------------------------//
        View header = View.inflate(MainActivity.this, R.layout.nav_header_main, null);
            //ImageView logo

        img_logo=(ImageView) header.findViewById(R.id.img_logo_main);
        user_name_eid_role=(TextView) header.findViewById(R.id.nameroleeid);
        employ_code=(TextView) header.findViewById(R.id.employcode);

        String upperString_f_name = first_name.substring(0, 1).toUpperCase() + first_name.substring(1);
        String upperString_l_name = last_name.substring(0, 1).toUpperCase() + last_name.substring(1);
        String upperString_r_name = role_nam.substring(0, 1).toUpperCase() + role_nam.substring(1);

        user_name_eid_role.setText(upperString_f_name+" "+upperString_l_name+"\n"+"("+upperString_r_name+")");
        employ_code.setText("Employee Code : "+ empcode);

        if (!company_logo.equalsIgnoreCase(""))
        {
            System.out.println("logo "+company_logo);
            Glide.with(MainActivity.this).load(company_logo).error(R.drawable.default_img).into(img_logo);
        }
        else
        {
            img_logo.setImageResource(R.drawable.default_img);
        }

        //Add header in navigationview
        navigationview.addHeaderView(header);

        // ---------------------------- Set Footor-------------------------------------------------------------------------------//
       /* ListView listView = (ListView) navigationview.getChildAt(0);
         View header2 = View.inflate(MainActivity.this, R.layout.nav_footor, null);
        listView.addFooterView(header2, null, false);*/

        //------------------------------------ Set  ImageView Visibility-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
        img_slider.setVisibility(View.VISIBLE);
        img_back.setVisibility(View.GONE);

        // ---------------------------- Call First Fragment-------------------------------------------------------------------------------//

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, new HomeFragments())
                //.addToBackStack("0")
                .commit();
        // ---------------------------- Set OnClickListner-------------------------------------------------------------------------------//
                //Navigationview setOnClick
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                 @Override
                 public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                   switch (item.getItemId()) {

                     case  R.id.home_icon1:

                         //------------------------------------ Set  ImageView Visibility-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
                         img_slider.setVisibility(View.VISIBLE);
                         img_back.setVisibility(View.GONE);
                         getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFrame, new HomeFragments())
                                .addToBackStack(String.valueOf(0))
                                .commit();
                        break;

                       case  R.id.reviewdayend_icon1 :
                           img_slider.setVisibility(View.VISIBLE);
                           img_back.setVisibility(View.GONE);
                           Boolean internet = Utils.isConnected(MainActivity.this);
                           if (internet) {
                               if (err_message.equalsIgnoreCase("You have already started the Field work for today.")) {

                                   getSupportFragmentManager().beginTransaction()
                                           .replace(R.id.contentFrame, new DCR_Fullview_Frag())
                                           .addToBackStack("0")
                                           .commit();

                               } else if (err_message.equalsIgnoreCase("Your Field Work For Today is Completed")) {
                                   Toast.makeText(getApplicationContext(), "Your Field Work For Today is Completed", Toast.LENGTH_SHORT).show();
                               } else {
                                   Toast.makeText(getApplicationContext(), "Please submit the DCR report", Toast.LENGTH_SHORT).show();
                               }
                           }

                       else
                       {
                           Toast.makeText(MainActivity.this, "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
                       }


/*
                           DCR_Fullview_Frag selectedFragment = DCR_Fullview_Frag.newInstance();
                           FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                           String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                           Bundle b = new Bundle();
                           b.putString("today_date",date);
                           b.putString("user_id",user_id4);
                           selectedFragment.setArguments(b);
                           transaction.replace(R.id.contentFrame, selectedFragment);
                           transaction.addToBackStack("0");
                           transaction.commit();*/
                           break;

                       case R.id.viewdcr_icon1:
                           getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.contentFrame, new ViewDCRTimeline_Frag())
                                   .addToBackStack("0")
                                   .commit();

                           break;


                       case R.id.view_timeline:
                           getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.contentFrame, new ViewTimeline_Frag())
                                   .addToBackStack("0")
                                   .commit();

                           break;


                       case R.id.view_doc_list:
                           getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.contentFrame, new MyDoctor_List_Frag())
                                   .addToBackStack("0")
                                   .commit();

                           break;


                       case  R.id.logout_icon1:
                        img_back.setVisibility(View.GONE);
                        img_slider.setVisibility(View.VISIBLE);
                  /*      // signOut();
                        MySharedPref sp=new MySharedPref();
                        sp.saveData(getApplicationContext(),"ldata","null");
                        sp.saveData(getApplicationContext(),"token","null");

                        Toast.makeText(getApplicationContext(),"You have logged-out successfully",Toast.LENGTH_SHORT).show();*/
                         MySharedPref sp=new MySharedPref();
                         sp.saveData(MainActivity.this,"ldata","null");
                         sp.saveData(MainActivity.this,"petrol","");

                         sp.saveData(MainActivity.this,"selected_city_id","");
                         sp.saveData(MainActivity.this,"selected_city_id_local","");
                         sp.saveData(MainActivity.this,"selected_interior_id","");
                         sp.saveData(MainActivity.this,"mr_id","");
                         sp.saveData(MainActivity.this,"dcr_work_strt_type","");
                         sp.saveData(MainActivity.this,"region_id","");

                         Intent iii=new Intent(MainActivity.this, LocationMonitorService.class);
                         context.stopService(iii);

                         Intent i1 = new Intent(MainActivity.this, LoginActivity.class);
                         startActivity(i1);
                        break;


                }

                drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // RelativeLayout Slide onClicklistener
        rr_left.setOnClickListener(this);

        // ImageView Slide onClicklistener
        img_slider.setOnClickListener(this);


       /* Intent iii=new Intent(MainActivity.this, LocationMonitorService.class);
        context.startService(iii);*/
    }
    // ---------------------------- For Get Current Location -------------------------------------------------------------------------------//
    private void getLocation() {


        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled == false) {
            //Show Aleat Dialog for permission
            showGPSDisabledAlertToUser();
        }


       //Call GPSTracker class for current latitiude and longitude
        GPSTracker gps = new GPSTracker(MainActivity.this);
        if (gps.canGetLocation()) {
            currentlat = gps.getLatitude();
            currentlong = gps.getLongitude();
            System.out.println("Current Latitude@@@" + currentlat);
            System.out.println("Current Longitude@@@" + currentlong);
            if(currentlat!=0.0 && currentlong!=0.0) {
        //Get  Address from Current latitude and Longitude
                String city_name = getAddressFromLatlong(currentlat, currentlong);
                System.out.println("City Name&&&" + city_name);
            }
            else
            {

               //Get  Address from Dummy latitude and Longitude
                String city_name = getAddressFromLatlong(22.719569, 75.857726);

            }



        }
        else {
            gps.showSettingsAlert();
        }

    }
    // ---------------------------- For Address from Current latitude and Longitude -------------------------------------------------------------------------------//
    public String getAddressFromLatlong(double currentlat2, double currentlong2)
    {
        Geocoder geocoder;
        String city = null;
        List<Address> addresses;
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(currentlat2, currentlong2, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            System.out.println("City Name***"+city);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return city;
    }


    // ---------------------------- For Navigating user to app Settings -------------------------------------------------------------------------------//
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);


                                dialog.dismiss();

                                //finish();
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        showGPSDisabledAlertToUser();

                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }



    // ---------------------------- For Check Permission -------------------------------------------------------------------------------//
    private void checkForPermission() {

        //Check permission for Location
        int permissionCheckForFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheckForCorseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);


        if (permissionCheckForFineLocation != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForCorseLocation != PackageManager.PERMISSION_GRANTED) {

            ischeckedpermission=false;

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE

                    },
                    1001);

           // getLocation();


        }
        else
        {
            ischeckedpermission=true;
           // getLocation();
           /* getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, new HomeFragments())
                    .commit();*/

        }



    }

    @Override
    public void onClick(View v) {
if(v==img_slider)
{
    if (drawer.isDrawerOpen(GravityCompat.START)) {
        drawer.closeDrawer(GravityCompat.START);
    } else {
        drawer.openDrawer(GravityCompat.START);
    }
}
if(v==rr_left)
{
    if (drawer.isDrawerOpen(GravityCompat.START)) {
        drawer.closeDrawer(GravityCompat.START);
    } else {
        drawer.openDrawer(GravityCompat.START);
    }
}

    }

    // ---------------------------- OnBackPressed Click -------------------------------------------------------------------------------//
    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        System.out.println("Count%%%" + count);

        if (count == 0) {

           // initUi();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(Intent.ACTION_MAIN);
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            // finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        else {
            getSupportFragmentManager().popBackStack();
        }

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


                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission. ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION )== PackageManager.PERMISSION_GRANTED


                            )
                    {
                        Log.d("", "sms & location services permission granted");
                        ischeckedpermission=true;
                        //getLocation();

                         // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("", "Some permissions are not granted ask again ");

                        ischeckedpermission=false;

                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))

                        {
                            showDialogOK("Location Permission must required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkForPermission();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {

                            showDialogOK("Location Permission must required for this app",
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


    // ---------------------------- Permorm action OnRestart-------------------------------------------------------------------------------//
    @Override
    protected void onRestart() {
        super.onRestart();
        // ---------------------------- For Check Permission -------------------------------------------------------------------------------//
           checkForPermission();
        }

    // ---------------------------- For  WebService Call Method of Login-------------------------------------------------------------------------------//
    private void get_Data_forGetStart() {

        //loader.setVisibility(View.VISIBLE);
        String url = "http://dailyreporting.in/"+company_name+"/api/check_user_start_fieldwork";
        System.out.println("sout url"+ url);
        // Tag used to cancel the request
        String cancel_req_tag = "area";
        /*StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_CHECK_USER_START_FIELDWORK, new Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CheckUserFieldWork", "CheckUserFieldWork: 324344545 " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    err_message = jObj.getString("message");

                    if (error.equals("success")) {
                        String result=jObj.getString("result");
                        System.out.println("Result CheckUserFieldWork***"+result);
                        String message=jObj.getString("message");
                        err_message = message;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // lo.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("CheckUserFieldWork", "CheckUserFieldWork: " + error.getMessage());

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
                params.put("user_id",user_id4);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    // ---------------------------- For  WebService Call Method of Login-------------------------------------------------------------------------------//
    private void Update_Data_for_LatLong(final String lat, final String longi) {
        // Tag used to cancel the request
        String url = "http://dailyreporting.in/"+company_name+"/api/update_current_location";
        System.out.println("sout url"+ url);

        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_CHECK_USER_TIME_DURATION, new Response.Listener<String>() {*/
        //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CheckUserFieldWork", "CheckUserFieldWork: 35345445 " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    if (error.equals("success")) {
                        String result=jObj.getString("result");
                        System.out.println("Result CheckUserFieldWork***"+result);
                    }
                    else
                    {
                        Log.e("CheckUserFieldWork", "CheckUserFieldWork: " + error);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("CheckUserFieldWork", "CheckUserFieldWork: " + error.getMessage());

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
                params.put("user_id",user_id4);
                params.put("current_lat",lat);
                params.put("current_long",longi);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(MainActivity.this).addToRequestQueue(strReq, cancel_req_tag);
    }

}
