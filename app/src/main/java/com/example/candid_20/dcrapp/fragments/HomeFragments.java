package com.example.candid_20.dcrapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.activity.LoginActivity;
import com.example.candid_20.dcrapp.activity.MainActivity;
import com.example.candid_20.dcrapp.constant.LocationMonitorService;
import com.example.candid_20.dcrapp.constant.Utils;
import com.example.candid_20.dcrapp.customgrid.MyGridView;
import com.example.candid_20.dcrapp.fragments.home_menu.StartFieldWork;
import com.example.candid_20.dcrapp.fragments.home_menu.TimeLineFragment;
import com.example.candid_20.dcrapp.fragments.home_menu.for_doctor_list.MyDoctor_List_Frag;
import com.example.candid_20.dcrapp.fragments.home_menu.for_view_timeline.ViewTimeline_Frag;
import com.example.candid_20.dcrapp.fragments.home_menu.view_DCR.DCR_Fullview_Frag;
import com.example.candid_20.dcrapp.fragments.home_menu.view_DCR.ViewDCRTimeline_Frag;
import com.example.candid_20.dcrapp.other.GPSTracker;
import com.example.candid_20.dcrapp.storage.MySharedPref;
import com.example.candid_20.dcrapp.volleyconnector.AppSingleton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static android.support.constraint.Constraints.TAG;

public class HomeFragments extends Fragment implements View.OnClickListener {
    MyGridView grid_content;
    View v;
    boolean ischeckedpermission;
    TextView txt_current_loc_descp;
    ArrayList<String> l1;
    ArrayList<Integer> images;
    Dialog dialog;
    String currentVersion, latestVersion;
    private final static int INTERVAL = 1000 * 60 * 15; //15 minutes

    Handler mHandler = new Handler();
    public Handler customHandler = new Handler();

    Runnable runnable;

    TextView tct_call_hour,txt_callprocess,txt_callprocess_second;
    String company_name;
    //------------------------------------ Initialise boolean-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    boolean isGPSEnabled = false;
    //------------------------------------ Initialise double-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    double currentlat,currentlong;

    Button btn_logout;

    ProgressBar loader;
    ProgressDialog progressDialog;
    MySharedPref sp;
    //String Sharedpreferences Data
    String work_time,item1,item2,item3;

    String ldata,user_id4,token,first_name,last_name;
    TextView txt_title;
    ImageView img_curent_location;
    private String err_message;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.home_fragment, container, false);

        // ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//
        getSaveddata();
        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();
        startLocationUpdates();
        // ---------------------------- For Get Current Location -------------------------------------------------------------------------------//
       // getLocation();
        getUpdate();
        return v;
    }
    // ---------------------------- For Get Current Location -------------------------------------------------------------------------------//

    private void getLocation() {

        if (mCurrentLocation!=null) {

            try {
                currentlat = mCurrentLocation.getLatitude();
                currentlong = mCurrentLocation.getLongitude();
                System.out.println("Current Latitude@@@" + currentlat);
                System.out.println("Current Longitude@@@" + currentlong);


                if (currentlat != 0.0 && currentlong != 0.0) {

                    //Get Address from Current latitude and Longitude
                    String city_name = getAddressFromLatlong(currentlat, currentlong);
                    System.out.println("City Name&&&" + city_name);

                    loader.setVisibility(View.GONE);
                    txt_current_loc_descp.setText(city_name);
                    img_curent_location.clearAnimation();
                    sp.setData(getActivity(), MySharedPref.KEY_Lat, String.valueOf(currentlat));
                    sp.setData(getActivity(), MySharedPref.KEY_Long, String.valueOf(currentlong));

                }
                else {



                    if (progressDialog == null) {
                        progressDialog = createProgressDialog(getActivity());
                        progressDialog.show();
                    }
                    else {
                        progressDialog.show();
                    }

                  /*  final ProgressDialog pd = new ProgressDialog(getActivity());
                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pd.setCancelable(false);
                    pd.show();*/

                    // getLocation();
                    Animation animBlink = AnimationUtils.loadAnimation(getActivity(), R.anim.pulse);
                    txt_current_loc_descp.setText("we are fetching your location.");
                    img_curent_location.startAnimation(animBlink);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("on run");
                            progressDialog.dismiss();
                            getLocation();
                        }
                    }, 3000);

                    //Get  Address from Dummy latitude and Longitude
                    // String city_name = getAddressFromLatlong(22.719569, 75.857726);
                }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        }
        else {
            Animation animBlink = AnimationUtils.loadAnimation(getActivity(), R.anim.pulse);

            if (progressDialog == null) {
                progressDialog = createProgressDialog(getActivity());
                progressDialog.show();
            }
            else {
                progressDialog.show();
            }

                  /*  final ProgressDialog pd = new ProgressDialog(getActivity());
                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pd.setCancelable(false);
                    pd.show();*/

            // getLocation();
            txt_current_loc_descp.setText("we are fetching your location.");
            img_curent_location.startAnimation(animBlink);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    System.out.println("on run");
                    progressDialog.dismiss();
                   // getLocation();
                }
            }, 3000);

            //Get  Address from Dummy latitude and Longitude
            // String city_name = getAddressFromLatlong(22.719569, 75.857726);
        }


      /* else
            {
                gps.showSettingsAlert();
            }*/



      /*  //loader.setVisibility(View.VISIBLE);
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
        GPSTracker gps = new GPSTracker(getActivity());
        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        System.out.println("gps status "+isGPSEnabled + gps.canGetLocation());
        if (isGPSEnabled == false) {
            //Show Aleat Dialog for permission
           gps.showSettingsAlert();
        }
        else
        {
            //Call GPSTracker class for current latitiude and longitude
            Animation animBlink = AnimationUtils.loadAnimation(getActivity(), R.anim.pulse);
            if (gps.canGetLocation()) {


                currentlat = gps.getLatitude();
                currentlong = gps.getLongitude();
                System.out.println("Current Latitude@@@" + currentlat);
                System.out.println("Current Longitude@@@" + currentlong);

                if (currentlat != 0.0 && currentlong != 0.0) {

                    //Get Address from Current latitude and Longitude
                    String city_name = getAddressFromLatlong(currentlat, currentlong);
                    System.out.println("City Name&&&" + city_name);

                    loader.setVisibility(View.GONE);
                    txt_current_loc_descp.setText(city_name);
                    img_curent_location.clearAnimation();
                    sp.setData(getActivity(), MySharedPref.KEY_Lat, String.valueOf(currentlat));
                    sp.setData(getActivity(), MySharedPref.KEY_Long, String.valueOf(currentlong));

                }
                else {

                    if (progressDialog == null) {
                        progressDialog = createProgressDialog(getActivity());
                        progressDialog.show();
                    }
                    else {
                        progressDialog.show();
                    }

                  *//*  final ProgressDialog pd = new ProgressDialog(getActivity());
                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pd.setCancelable(false);
                    pd.show();*//*

                    // getLocation();
                    txt_current_loc_descp.setText("we are featching your location.");
                    img_curent_location.startAnimation(animBlink);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("on run");
                            progressDialog.dismiss();
                            getLocation();
                        }
                    }, 8000);

                    //Get  Address from Dummy latitude and Longitude
                    // String city_name = getAddressFromLatlong(22.719569, 75.857726);
                }


            }

      *//* else
            {
                gps.showSettingsAlert();
            }*//*
        }*/
    }
    // ---------------------------- For Navigating user to app Settings -------------------------------------------------------------------------------//
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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


    // ---------------------------- For Address from Current latitude and Longitude -------------------------------------------------------------------------------//
    public String getAddressFromLatlong(double currentlat2, double currentlong2)
    {
        Geocoder geocoder;
        String city = null;
        List<Address> addresses;

        try {
            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            addresses = geocoder.getFromLocation(currentlat2, currentlong2, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getLocality(); // If any additional address line present than only,
            // check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getAddressLine(0);
            System.out.println("Address$$$"+addresses);
            System.out.println("Address***"+address);

            //Set Address in TextView
           // txt_current_loc_descp.setText(address);
           // img_curent_location.clearAnimation();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return city;
    }

    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
    private void initUi() {
// ---------------------------- For Casting Elements -------------------------------------------------------------------------------//

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                System.out.println("Current Latitude@@@" + mCurrentLocation.getLatitude());
                System.out.println("Current Longitude@@@" + mCurrentLocation.getLongitude());

                getLocation();
            }
        };
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();


        //Casting MyGridView for Set Content
        grid_content=(MyGridView) v.findViewById(R.id.grid_content);
        //Casting ProgressBar for Loading
        loader=(ProgressBar)v.findViewById(R.id.loader);
//Casting TextView for Title
        txt_title=(TextView)getActivity().findViewById(R.id.txt_title);

        img_curent_location=(ImageView)v.findViewById(R.id.img_curent_location);
        Animation pulse = AnimationUtils.loadAnimation(getActivity(), R.anim.pulse);
        img_curent_location.startAnimation(pulse);
//Set Text
        if (first_name!=null && last_name!=null)
        {
            String upperString_f_name = first_name.substring(0, 1).toUpperCase() + first_name.substring(1);
            String upperString_l_name = last_name.substring(0, 1).toUpperCase() + last_name.substring(1);

            txt_title.setText("Welcome "+upperString_f_name+" "+upperString_l_name);
        }

        //Casting TextView for Current Location
        txt_current_loc_descp=(TextView)v.findViewById(R.id.txt_current_loc_descp);

        //Casting TextView for Set working hours
        tct_call_hour = (TextView)v.findViewById(R.id.txt_callprocess_hour);
        //Casting TextView for Set Current Time
        txt_callprocess=(TextView) v.findViewById(R.id.txt_callprocess);
        //Casting TextView for Set Minutes
        txt_callprocess_second=(TextView) v.findViewById(R.id.txt_callprocess_second);

        if (item1!=null)
        {
            tct_call_hour.setText("00");
            txt_callprocess.setText("00");
            txt_callprocess_second.setText("00");

        }
        else
        {
            tct_call_hour.setText(""+ item1);
            txt_callprocess.setText(""+ item2);
            txt_callprocess_second.setText(""+ item3);
        }


          customHandler.postDelayed(updateTimerThread, 1000);

      /*  if (item1.equalsIgnoreCase("null"))
        {
            tct_call_hour.setText("00");
        }
        else
        {
        }
        if (item2.equalsIgnoreCase("null"))
        {
            txt_callprocess.setText(00);
        }
        else
        {
        }
        if (item3.equalsIgnoreCase("null"))
        {
            txt_callprocess_second.setText("00");
        }
        else
        {
        }
*/
        callWebservicefor_getAllInteriorAcctoCity(user_id4);



//btn_logout=(Button)v.findViewById(R.id.btn_logout);
        // ---------------------------- GridView OnClickListner -------------------------------------------------------------------------------//
  grid_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

   if(position==0)
    {

     if(l1.get(position).equalsIgnoreCase("Field Work in Process")) {
       /* getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, new TimeLineFragment())
                .addToBackStack("25")
                .commit();
*/
        TimeLineFragment timeLineFragment = TimeLineFragment.newInstance();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        Bundle b = new Bundle();
        b.putString("fied_known",l1.get(position));
        timeLineFragment.setArguments(b);
        transaction.replace(R.id.contentFrame, timeLineFragment);
        transaction.addToBackStack("1");
        transaction.commit();
    }
    if(l1.get(position).equalsIgnoreCase("Start Field Work")) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, new StartFieldWork())
                .addToBackStack("1")
                .commit();
    }

        if(l1.get(position).equalsIgnoreCase("Your Field Work For Today is Completed")) {
            Toast.makeText(getActivity(), "Your Field Work For Today is Completed", Toast.LENGTH_SHORT).show();
        }
}

                if(position==1)
                {
                    if(l1.get(0).equalsIgnoreCase("Field Work in Process")) {

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFrame, new DCR_Fullview_Frag())
                                .addToBackStack("1")
                                .commit();

                    }

                    if(l1.get(0).equalsIgnoreCase("Start Field Work")) {

                        Toast.makeText(getActivity(),"Please submit the DCR report",Toast.LENGTH_SHORT).show();
                    }

                }

                if (position==2)
                {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, new ViewDCRTimeline_Frag())
                            .addToBackStack("1")
                            .commit();
                }


        if(position==3)
        {
             getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, new ViewTimeline_Frag())
                .addToBackStack("1")
                .commit();
        }
                if(position==4)
                {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, new MyDoctor_List_Frag())
                            .addToBackStack("1")
                            .commit();
                }


            }
        });

       /* mHandler.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
              //  getLocation();
                Toast.makeText(getActivity(), "Location", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(runnable, INTERVAL);
            }
        }, INTERVAL);*/

       // btn_logout.setOnClickListener(this);
    }


    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //Toast.makeText(getActivity(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        getLocation();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                   // rae.startResolutionForResult(getActivity(), 100);
                                    startIntentSenderForResult(rae.getResolution().getIntentSender(), 100, null, 0, 0, 0, null);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                GPSTracker gpsTracker = new GPSTracker(getActivity());
                                gpsTracker.showSettingsAlert();

                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                        }

                       // updateLocationUI();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult()", Integer.toString(resultCode));

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case 100:
                Log.e(TAG, "User agreed to make required location settings changes." + requestCode);
                switch (resultCode) {
                    case Activity.RESULT_OK:
                    {
                        Log.e(TAG, "User agreed to make required location settings changes." + requestCode);
                        Toast.makeText(getActivity(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        startLocationUpdates();
                        break;
                    }

                    case Activity.RESULT_CANCELED:
                    {
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    }
                    default:
                    {
                        break;
                    }

                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
       /* if(v==btn_logout)
        {
            MySharedPref sp=new MySharedPref();
            sp.saveData(getActivity(),"ldata","");

            Intent ii=new Intent(getActivity(), LoginActivity.class);
            startActivity(ii);
        }*/
    }

// ---------------------------- Set Custom Gridview for Content -------------------------------------------------------------------------------//

   private class CustomGridviewForContent extends BaseAdapter
   {
       private Context mContext;
       ArrayList<String> arr_content2;
       ArrayList<Integer> images;

       public CustomGridviewForContent(Context c, ArrayList<String> arr_content, ArrayList<Integer> images) {
           mContext = c;
           this.arr_content2=arr_content;
           this.images = images;
       }
       @Override
       public int getCount() {
           return arr_content2.size();
       }

       @Override
       public Object getItem(int position) {
           return null;
       }

       @Override
       public long getItemId(int position) {
           return 0;
       }

       @Override
       public View getView(final int position, View convertView, ViewGroup parent) {
           // TODO Auto-generated method stub
           View grid;
           LayoutInflater inflater = (LayoutInflater) mContext
                   .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

           if (convertView == null) {

               grid = new View(mContext);
               grid = inflater.inflate(R.layout.grid_home_adp, null);
               TextView cuisineslistitem = (TextView)grid.findViewById(R.id.cuisineslistitem);
               ImageView icons = (ImageView) grid.findViewById(R.id.icon_show_process);

               RelativeLayout rr_first_descp=(RelativeLayout)grid.findViewById(R.id.rr_first_descp);
               String str_image_name2=arr_content2.get(position);

               cuisineslistitem.setText(arr_content2.get(position));
               icons.setImageResource(images.get(position));

               rr_first_descp.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       if(position==0)
                       {

                           if(arr_content2.get(position).equalsIgnoreCase("Field Work in Process")) {
                             /*  getActivity().getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.contentFrame, new TimeLineFragment())
                                       .addToBackStack("20")
                                       .commit();
*/
                               TimeLineFragment timeLineFragment = TimeLineFragment.newInstance();
                               FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                               Bundle b = new Bundle();
                               b.putString("fied_known",arr_content2.get(position));
                               timeLineFragment.setArguments(b);
                               transaction.replace(R.id.contentFrame, timeLineFragment);
                               transaction.addToBackStack("1");
                               transaction.commit();
                           }

                           if(arr_content2.get(position).equalsIgnoreCase("Start Field Work")) {
                               getActivity().getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.contentFrame, new StartFieldWork())
                                       .addToBackStack("1")
                                       .commit();
                           }
                           if(l1.get(position).equalsIgnoreCase("Today's Work is Completed")) {
                               Toast.makeText(getActivity(), "Your Field Work For Today is Completed", Toast.LENGTH_SHORT).show();
                           }

                       }

                       if(position==1)
                       {
                           if(arr_content2.get(0).equalsIgnoreCase("Field Work in Process")) {

                               getActivity().getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.contentFrame, new DCR_Fullview_Frag())
                                       .addToBackStack("1")
                                       .commit();

                           }

                           if(arr_content2.get(0).equalsIgnoreCase("Start Field Work")) {
                               Toast.makeText(getActivity(),"Please submit the DCR report",Toast.LENGTH_SHORT).show();
                           }

                           if(arr_content2.get(0).equalsIgnoreCase("Today's Work is Completed")) {
                               Toast.makeText(getActivity(),"Todays DCR is submited.",Toast.LENGTH_SHORT).show();
                           }


                       }

                       if (position==2)
                       {
                           getActivity().getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.contentFrame, new ViewDCRTimeline_Frag())
                                   .addToBackStack("1")
                                   .commit();
                       }


                       if(position==3)
                       {
                           getActivity().getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.contentFrame, new ViewTimeline_Frag())
                                   .addToBackStack("1")
                                   .commit();
                       }

                       if(position==4)
                       {
                           getActivity().getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.contentFrame, new MyDoctor_List_Frag())
                                   .addToBackStack("1")
                                   .commit();
                       }

                       if(position==5)
                       {
                           MySharedPref sp=new MySharedPref();
                           sp.saveData(getActivity(),"ldata","");
                           Intent ii=new Intent(getActivity(), LoginActivity.class);
                           startActivity(ii);
                       }
                   }
               });


           }
           else {
               grid = (View) convertView;
           }

           return grid;
       }
   }

    // ---------------------------- For Get Stored Data -------------------------------------------------------------------------------//
    private void getSaveddata() {


        sp = new MySharedPref();

        company_name = sp.getData(getActivity(),"company_name","");
        System.out.println("company name  : "+company_name);

        ldata = sp.getData(getActivity(), "ldata", "null");
        Log.e("LdataHome", ldata);

        if (ldata != null)

        {
            try {
                JSONObject jsonObject = new JSONObject(ldata);
                user_id4 = jsonObject.getString("user_id");
                token = jsonObject.getString("token");
                first_name = jsonObject.getString("first_name");
                last_name = jsonObject.getString("last_name");

                work_time = sp.getData(getActivity(), "work_time", "null");

                System.out.println("work time###" + work_time);
                Log.e("Id is@@@", user_id4);
                System.out.println("Id is***" + user_id4);

                if (!work_time.equalsIgnoreCase("null")) {
                    String[] items = work_time.split(":");
                    for (String item : items) {
                        System.out.println("item = " + item);
                        item1 = items[0];
                        item2 = items[1];
                        item3 = items[2];

                        System.out.println("item = " + item1);
                        System.out.println("item = " + item2);
                        System.out.println("item = " + item3);

                    }

                }
                else
                {
                    item1 = "00";
                    item2 = "00";
                    item3 = "00";
                }


                Log.e("Id is@@@",user_id4);
                System.out.println("Id is***" + user_id4);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    //--------------------------------handler for handle task

    public Runnable updateTimerThread = new Runnable() {
        public void run() {

          /*  timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);

            int mins = secs / 60;

            // compute  minutes
            int minutes = mins % 60 ;

            // throw away minutes used in previous statement and convert to hours
            int  InHours = mins / 60 ;
*/
          get_Data_forTime();

           /* secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);

            *//*tct_call_hour.setText(""+ InHours);
            txt_callprocess.setText(""+ minutes);
            txt_callprocess_second.setText(String.format("%02d", secs));*/

            customHandler.postDelayed(this, 1000);

        }

    };




    // ---------------------------- WebService Call for Getall City-------------------------------------------------------------------------------//
    private void callWebservicefor_getAllInteriorAcctoCity(String city_id2) {


        if(Utils.isConnected(getActivity())) {

            get_Data_forGetStart();
        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }
    // ---------------------------- For  WebService Call Method of Login-------------------------------------------------------------------------------//
    private void get_Data_forGetStart() {

        String url = "http://dailyreporting.in/"+company_name+"/api/check_user_start_fieldwork";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        /*StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_CHECK_USER_START_FIELDWORK, new Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CheckUserFieldWork", "CheckUserFieldWork: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);

                    String error = jObj.getString("status");
                    err_message = jObj.getString("message");

                    String dcr_table_insert_idd = jObj.getString("dcr_table_insert_id");
                    String dcr_live_table_insert_idd = jObj.getString("dcr_live_table_insert_id");

                    String dcr_work_location = jObj.getString("work_location");
                    String dcr_interior_idd = jObj.getString("interior_id");
                    String dcr_city_idd = jObj.getString("city_id");
                    String dcr_mr_idd = jObj.getString("mrid");
                    String dcr_region_idd = jObj.getString("region");

                    if (!dcr_table_insert_idd.equalsIgnoreCase("") && !dcr_live_table_insert_idd.equalsIgnoreCase(""))
                    {
                        sp.saveData(getActivity(),"dcr_work_strt_type",dcr_work_location);

                        sp.saveData(getActivity(),"dcr_table_insert_id",dcr_table_insert_idd);
                        sp.saveData(getActivity(),"dcr_live_table_insert_id",dcr_live_table_insert_idd);
                        sp.saveData(getActivity(),"selected_interior_id",dcr_interior_idd);
                        sp.saveData(getActivity(),"mr_id",dcr_mr_idd);
                        sp.saveData(getActivity(),"region_id",dcr_region_idd);

                        if (dcr_work_location.equalsIgnoreCase("1"))
                        {
                            sp.saveData(getActivity(),"selected_city_id",dcr_city_idd);
                        }
                        else
                        {
                            sp.saveData(getActivity(),"selected_city_id_local",dcr_city_idd);
                        }


                    }

                    String field_work_time = jObj.getString("fieldwork");

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);

                        String result=jObj.getString("result");
                        System.out.println("Result CheckUserFieldWork***"+result);
                        String message=jObj.getString("message");


                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
// ---------------------------- ArrayList set -------------------------------------------------------------------------------//
                        l1=new ArrayList<>();
                        l1.add("Start Field Work");
                        l1.add("Review & End Field Work");
                        l1.add("View DCR");
                        l1.add("Timeline");
                        l1.add("My Doctors List");

                        images=new ArrayList<>();
                        images.add(R.drawable.start_work);
                        images.add(R.drawable.end_work);
                        images.add(R.drawable.dcr);
                        images.add(R.drawable.timeline);
                        images.add(R.drawable.doctor_list);

                        customHandler.removeCallbacks(updateTimerThread);

// ---------------------------- Set Custom Gridview and Adapter -------------------------------------------------------------------------------//
                        grid_content.setExpanded(true);


                        CustomGridviewForContent customGridviewForContent=new CustomGridviewForContent(getActivity(), l1,images);
                        grid_content.setAdapter(customGridviewForContent);


                        tct_call_hour.setText("00");
                        txt_callprocess.setText("00");
                        txt_callprocess_second.setText("00");

                        sp.saveData(getActivity(),"selected_city_id","");
                        sp.saveData(getActivity(),"selected_city_id_local","");
                        sp.saveData(getActivity(),"selected_interior_id","");

                       /* try{
                            //getLocation();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }*/

                    }

                    else if (err_message.equalsIgnoreCase("Your Field Work For Today is Completed"))
                    {
                        loader.setVisibility(View.GONE);

                        String result=jObj.getString("result");
                        System.out.println("Result CheckUserFieldWork***"+result);
                        String message=jObj.getString("message");

                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
// ---------------------------- ArrayList set -------------------------------------------------------------------------------//
                        l1=new ArrayList<>();
                        l1.add("Today's Work Is Completed");
                        l1.add("Review & End Field Work");
                        l1.add("View DCR");
                        l1.add("Timeline");
                        l1.add("My Doctors List");

                        images=new ArrayList<>();
                        images.add(R.drawable.stop);
                        images.add(R.drawable.end_work);
                        images.add(R.drawable.dcr);
                        images.add(R.drawable.timeline);
                        images.add(R.drawable.doctor_list);

                        customHandler.removeCallbacks(updateTimerThread);

// ---------------------------- Set Custom Gridview and Adapter -------------------------------------------------------------------------------//
                        grid_content.setExpanded(true);

                        CustomGridviewForContent customGridviewForContent=new CustomGridviewForContent(getActivity(), l1,images);
                        grid_content.setAdapter(customGridviewForContent);

                        tct_call_hour.setText("00");
                        txt_callprocess.setText("00");
                        txt_callprocess_second.setText("00");


                        /*try{
                            getLocation();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }*/
                    }
                else {
                        loader.setVisibility(View.GONE);

                        String errorMsg = jObj.getString("message");

                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();

                            Log.e("errorMsg", errorMsg);
                            // ---------------------------- ArrayList set -------------------------------------------------------------------------------//
                            l1=new ArrayList<>();
                            l1.add("Field Work in Process");
                            l1.add("Review & End Field Work");
                            l1.add("View DCR");
                            l1.add("Timeline");
                            l1.add("My Doctors List");

                            images=new ArrayList<>();
                            images.add(R.drawable.process_wok);
                            images.add(R.drawable.end_work);
                            images.add(R.drawable.dcr);
                            images.add(R.drawable.timeline);
                            images.add(R.drawable.doctor_list);

                            // ---------------------------- Set Custom Gridview and Adapter -------------------------------------------------------------------------------//
                            grid_content.setExpanded(true);

                            CustomGridviewForContent customGridviewForContent=new CustomGridviewForContent(getActivity(), l1,images);
                            grid_content.setAdapter(customGridviewForContent);

                           /* try{
                                getLocation();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }*/
                        }
                    }


                    if (!field_work_time.equalsIgnoreCase(""))
                    {
                        sp.saveData(getActivity(),"work_time",field_work_time);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();

                Log.e("CheckUserFieldWork", "CheckUserFieldWork: " + error.getMessage());
                try {

                    //getLocation();
                    // ---------------------------- ArrayList set -------------------------------------------------------------------------------//
                    l1=new ArrayList<>();
                    l1.add("Field Work in Process");
                    l1.add("Review & End Field Work");
                    l1.add("View DCR");
                    l1.add("Timeline");
                    l1.add("My Doctors List");

                    images=new ArrayList<>();
                    images.add(R.drawable.process_wok);
                    images.add(R.drawable.end_work);
                    images.add(R.drawable.dcr);
                    images.add(R.drawable.timeline);
                    images.add(R.drawable.doctor_list);

                    // ---------------------------- Set Custom Gridview and Adapter -------------------------------------------------------------------------------//
                    grid_content.setExpanded(true);

                    CustomGridviewForContent customGridviewForContent=new CustomGridviewForContent(getActivity(), l1 ,images);
                    grid_content.setAdapter(customGridviewForContent);

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
                params.put("user_id",user_id4);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }


    // ---------------------------- For  WebService Call Method of Login-------------------------------------------------------------------------------//
    private void get_Data_forTime() {
        // Tag used to cancel the request
        String url = "http://dailyreporting.in/"+company_name+"/api/check_userfieldwork";
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
                    String field_work_time = jObj.getString("fieldwork");
                    if (!field_work_time.equalsIgnoreCase(""))
                    {
                        // sp.saveData(getActivity(),"work_time",field_work_time);
                        if (field_work_time != null) {
                            String[] items = field_work_time.split(":");
                            for (String item : items) {
                                System.out.println("item = " + item);
                                item1 = items[0];
                                item2 = items[1];
                                item3 = items[2];
                                System.out.println("item = " + item1);
                                System.out.println("item = " + item2);
                                System.out.println("item = " + item3);

                                tct_call_hour.setText(""+ item1);
                                txt_callprocess.setText(""+ item2);
                                txt_callprocess_second.setText(""+item3);

                            }

                        }
                        if (!field_work_time.equalsIgnoreCase(""))
                        {
                            sp.saveData(getActivity(),"work_time",field_work_time);
                        }
                    }
                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);
                        String result=jObj.getString("result");
                        System.out.println("Result CheckUserFieldWork***"+result);
                    }
                    else
                    {
                        loader.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.GONE);
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
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }
    //---------------------------------------------update user current location for db--------------------------
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
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }
//----------------------------------------------------getUpdate for upgrade version -------------------------------------------------------------

    public void getUpdate()
    {

        try {
            currentVersion = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            System.out.println("current version "+currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
       new GetVersionCode().execute();
    }



  class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName()  + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }


        @Override

        protected void onPostExecute(String onlineVersion) {

            super.onPostExecute(onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                   showUpdateDialog();
                }

            }

            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);

        }
    }


    private void showUpdateDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        dialog = builder.show();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //startLocationUpdates();
        System.out.println("on attach");

    }

    @Override
    public void onStart() {
       // startLocationUpdates();
        super.onStart();
        System.out.println("on strt");
    }

   /* private void runUpdate()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("on run");
                 getLocation();
                }
        }, 2000);
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        customHandler.removeCallbacks(updateTimerThread);
        System.out.println("on detach");
    }

    @Override
    public void onDestroy() {
        customHandler.removeCallbacks(updateTimerThread);
        super.onDestroy();
        System.out.println("on deastroy");
    }

    @Override
    public void onStop() {
        customHandler.removeCallbacks(updateTimerThread);
        super.onStop();
        System.out.println("on stop");

    }

    @Override
    public void onPause() {
        customHandler.removeCallbacks(updateTimerThread);
        super.onPause();
    }

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_dialog);
        // dialog.setMessage(Message);
        return dialog;
    }
}
