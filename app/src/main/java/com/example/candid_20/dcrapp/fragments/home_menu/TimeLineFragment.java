package com.example.candid_20.dcrapp.fragments.home_menu;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.candid_20.dcrapp.activity.MainActivity;
import com.example.candid_20.dcrapp.bean.ItemModelArea;
import com.example.candid_20.dcrapp.bean.ItemModelArea_Selected;
import com.example.candid_20.dcrapp.constant.Utils;
import com.example.candid_20.dcrapp.customgrid.MyGridView;
import com.example.candid_20.dcrapp.fragments.HomeFragments;
import com.example.candid_20.dcrapp.fragments.home_menu.for_add_chemist.Add_Chemist_Fragment;
import com.example.candid_20.dcrapp.fragments.home_menu.for_add_expenses.Expenses_Frag;
import com.example.candid_20.dcrapp.fragments.home_menu.for_map_my_work.MapMyTeam_Work;
import com.example.candid_20.dcrapp.fragments.home_menu.for_map_my_work.Map_My_Work;
import com.example.candid_20.dcrapp.fragments.home_menu.today_dcr.Today_Dcr_Frag;
import com.example.candid_20.dcrapp.other.GPSTracker;
import com.example.candid_20.dcrapp.storage.MySharedPref;
import com.example.candid_20.dcrapp.volleyconnector.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

public class TimeLineFragment extends Fragment {

    View v;
    MyGridView grid_content;
    ArrayList<String> l1;
    ArrayList<Integer> images;

    TextView tct_call_hour,txt_callprocess,txt_callprocess_second;
    int count = -1;
    private long startTime = 0L;

    TextView txt_title;
    //------------------------------------ Initialise boolean-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    boolean isGPSEnabled = false;
    //------------------------------------ Initialise double-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    double currentlat,currentlong;
    public Handler customHandler = new Handler();
    MySharedPref sp;
    //String Sharedpreferences Data
    String ldata,user_id4,token,dcr_table_insert_id,field,role_id;

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    String doc_id,work_time,item1,item2,item3,company_name,

     dcr_work_location,dcr_interior_idd,dcr_city_idd,area_id;
    ProgressBar loader;

    //-----------------------
    ProgressDialog progressDialog;
    Dialog select_area_optional_dialog;
    Button btn_cncl, btn_dlt;
    RecyclerView select_area_recycle;
    TextView txt_show_error;
    CheckBox chk_left_checked;
    CustomGetProducts_Adp customGetProducts_adp;
   // AdapterForAreaInterior adapterForAreaInterior;
    //for select area local
    String idd;
    ArrayList<ItemModelArea> arrayListAreaLocal = new ArrayList<>();
    ArrayList<ItemModelArea_Selected> arrayListArealocalselected;


    public static TimeLineFragment newInstance()
    {
        TimeLineFragment fragment = new TimeLineFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.timeline, container, false);
// ---------------------------- For Get Current Location -------------------------------------------------------------------------------//
        getLocation();
        // ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//
        getSaveddata();
        // ---------------------------- For  Bundle Data -------------------------------------------------------------------------------//

        getBundleData();

        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();

        // ---------------------------- For area selected -------------------------------------------------------------------------------//


        return v;
    }

    // ---------------------------- For Get Current Location -------------------------------------------------------------------------------//
    private void getLocation() {
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled == false) {

            //Show Aleat Dialog for permission
            showGPSDisabledAlertToUser();
        }


//Call GPSTracker class for current latitiude and longitude
        GPSTracker gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {
            currentlat = gps.getLatitude();
            currentlong = gps.getLongitude();
            System.out.println("Current Latitude@@@" + currentlat);
            System.out.println("Current Longitude@@@" + currentlong);


            } else {
            gps.showSettingsAlert();
        }


    }

    // ---------------------------- For Navigating user to app Settings -------------------------------------------------------------------------------//
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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

    // ---------------------------- For Get Stored Data -------------------------------------------------------------------------------//
    private void getSaveddata() {


        sp = new MySharedPref();

        company_name = sp.getData(getActivity(),"company_name","");
        System.out.println("company name  : "+company_name);

        ldata = sp.getData(getActivity(), "ldata", "null");
        Log.e("LdataHome", ldata);
        doc_id=sp.getData(getActivity(),"doc_id",doc_id);
        System.out.println("Doctor Id###"+doc_id);


        if (ldata != null)

        {
            try {
                JSONObject jsonObject = new JSONObject(ldata);
                user_id4 = jsonObject.getString("user_id");
                role_id = jsonObject.getString("role_id");
                System.out.println("user role id ---- "+role_id);

                token = jsonObject.getString("token");

                dcr_table_insert_id = sp.getData(getActivity(), "dcr_table_insert_id", "null");

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

            } catch (Exception e) {
                e.printStackTrace();
            }



        }

    }

    // ---------------------------- For  Bundle Data -------------------------------------------------------------------------------//
    private void getBundleData() {

        Bundle b1=this.getArguments();
        if (b1!=null)
        {
            field = b1.getString("fied_known");
            System.out.println("field###"+field);

            if (field.equalsIgnoreCase("Start Field Work"))
            {
                MainActivity mDashboardActivity = (MainActivity) getActivity();
                if(mDashboardActivity!=null){
                    System.out.println("refresh Activity");
                    mDashboardActivity.refreshMyData();
                }
            }

        }


    }
    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//

    private void initUi() {

//Casting TextView for Title
        txt_title=(TextView)getActivity().findViewById(R.id.txt_title);

        //Set Text
        txt_title.setText("Field Work In Progress");

//Casting ProgressBar for Loader
        loader=(ProgressBar)v.findViewById(R.id.loader);
// ---------------------------- For Casting Elements -------------------------------------------------------------------------------//

        //Casting MyGridView for Set Content
        grid_content=(MyGridView) v.findViewById(R.id.grid_content);
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

// ---------------------------- ArrayList set -------------------------------------------------------------------------------//
           if (role_id.equalsIgnoreCase("5"))
           {
               l1=new ArrayList<>();
               l1.add("Add Doctor Call or Reminder");
               l1.add("Add Chemist Visit");
               l1.add("Add Expenses");
               l1.add("Today's Timeline");
               l1.add("Change Area");
               l1.add("Map My Work");
               l1.add("Back to Home");

               images=new ArrayList<>();
               images.add(R.drawable.doctor_reminder);
               images.add(R.drawable.chemist_visit);
               images.add(R.drawable.add_expenses);
               images.add(R.drawable.today_timeline);
               images.add(R.drawable.area_with_pins);
               images.add(R.drawable.area_with_pins);
               images.add(R.drawable.back_to_home);
           }
           else
           {
               l1=new ArrayList<>();
               l1.add("Add Doctor Call or Reminder");
               l1.add("Add Chemist Visit");
               l1.add("Add Expenses");
               l1.add("Today's Timeline");
               l1.add("Change Area");
               l1.add("Map My Work");
               l1.add("Map My Team");
               l1.add("Back to Home");

               images=new ArrayList<>();
               images.add(R.drawable.doctor_reminder);
               images.add(R.drawable.chemist_visit);
               images.add(R.drawable.add_expenses);
               images.add(R.drawable.today_timeline);
               images.add(R.drawable.area_with_pins);
               images.add(R.drawable.area_with_pins);
               images.add(R.drawable.area_with_pins);
               images.add(R.drawable.back_to_home);
           }


     /* For Stop

     timeSwapBuff += timeInMilliseconds;

     customHandler.removeCallbacks(updateTimerThread);*/

     //Displaying current time in 12 hour format with AM/PM
        DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
        String dateString = dateFormat.format(new Date()).toString();
      //  txt_callprocess.setText(dateString);
// ---------------------------- Set Custom Gridview and Adapter -------------------------------------------------------------------------------//
        grid_content.setExpanded(true);
        CustomGridviewForContent customGridviewForContent=new CustomGridviewForContent(getActivity(), l1,images);
        grid_content.setAdapter(customGridviewForContent);
        // ---------------------------- GridView OnClickListner -------------------------------------------------------------------------------//
        grid_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position==0)
                {
                    if(doc_id!=null)
                    {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFrame, new Search_Doctors_Fragment())
                                .addToBackStack("20")
                                .commit();
                      /*  CallStart_Fragment selectedFragment = CallStart_Fragment.newInstance();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();



                        Bundle b=new Bundle();
                        b.putString("doc_id",doc_id);


                        selectedFragment.setArguments(b);
                        transaction.replace(R.id.contentFrame, selectedFragment);
                        transaction.addToBackStack("20");
                        transaction.commit();*/
                    }

                    else {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFrame, new Search_Doctors_Fragment())
                                .addToBackStack("20")
                                .commit();
                    }
                }
                if(position==1)
                {
                    //Call WebService
                    callWebservicefor_startChemistvisit();

                }
                if(position==2)
                {
                    //Call WebService
                    callWebservicefor_for_Add_expeses_Startws();

                }

                if(position==3)
                {
                    //Call Fragment
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, new Today_Dcr_Frag())
                            .addToBackStack("20")
                            .commit();
                }

            }
        });

    }
  //----------------------------------set Dialog for open-----------------------------------------------------

    private void openDialogForArea(int length)
    {
        try {
            select_area_optional_dialog = new Dialog(getActivity());
            select_area_optional_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            select_area_optional_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            select_area_optional_dialog.setContentView(R.layout.select_area_popup_view);
            select_area_optional_dialog.setCancelable(true);

            select_area_recycle = (RecyclerView) select_area_optional_dialog.findViewById(R.id.select_recycler_view);
            //For  State Recycle Create by these
            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
            select_area_recycle.setLayoutManager(mLayoutManager2);
           // select_area_recycle.addItemDecoration(new StartFieldWork.GridSpacingItemDecoration(1, dpToPx(5), true));
            //     InboxDetailRV.setNestedScrollingEnabled(false);
            select_area_recycle.setItemAnimator(new DefaultItemAnimator());

            txt_show_error  = (TextView)select_area_optional_dialog.findViewById(R.id.alert_msg_error);
            btn_dlt = (Button) select_area_optional_dialog.findViewById(R.id.delete_ok);
            btn_cncl = (Button) select_area_optional_dialog.findViewById(R.id.delete_cancel);


            btn_dlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(arrayListAreaLocal!=null) {
                       /* System.out.println("Exception List is###" + MySharedPref.bean_list_area.size());
                        //MySharedPref.bean_list22 = new ArrayList<>();
                        for (int k = 0; k < MySharedPref.bean_list_area.size(); k++) {

                           try{
                               if (MySharedPref.bean_list_area.get(k).isSelected()) {
                                   System.out.println("Exception Selected List is###" + MySharedPref.bean_list_area.get(k).isSelected());

                                   System.out.println("Exception is###" + MySharedPref.bean_list_area.get(k).getArea_name());

                                   ItemModelAreaaa consultant_list_bean = new ItemModelAreaaa( MySharedPref.bean_list_area.get(k).getArea_id()
                                           , MySharedPref.bean_list_area.get(k).getArea_name(),
                                           MySharedPref.bean_list_area.get(k).isSelected());

                                   MySharedPref.bean_list_areaaa.add(consultant_list_bean);
                                   System.out.println("Exception is###" + MySharedPref.bean_list_areaaa.size());

                               }
                           }
                           catch(Exception e)
                           {
                               e.printStackTrace();
                           }
                        }*/
                        select_area_optional_dialog.dismiss();
                        setData("1");
                    }
                    else
                    {
                        select_area_optional_dialog.dismiss();
                    }

                }
            });

            btn_cncl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select_area_optional_dialog.dismiss();
                }
            });


            ArrayList<Integer> l1 = new ArrayList<Integer>();
           try{

               if (!area_id.equalsIgnoreCase("")) {
                String[] items = area_id.split(",");
                System.out.println("item length = " + items.length);
                l1.clear();
                for (String item : items) {
                    System.out.println("item = " + item);
                    l1.add(Integer.valueOf(item));
                }
                System.out.println("Int SIZEEE###" + l1.size() );


                for (int i = 0; i < l1.size(); i++) {
                String id = String.valueOf(l1.get(i));
                System.out.println("Int id###" + id + " 36543752 " + arrayListAreaLocal.get(i).getArea_id());
                if (id.equalsIgnoreCase(arrayListAreaLocal.get(i).getArea_id()))
                {
                    System.out.println("Int id###" + id + " 36543752 " + arrayListAreaLocal.get(i).getArea_id());
                    arrayListAreaLocal.get(i).setSelected(true);
                }
            }
         }

            setData("0");

            if (length==0)
            {
                select_area_recycle.setVisibility(View.GONE);
                txt_show_error.setVisibility(View.VISIBLE);
            }
            else
            {
                customGetProducts_adp = new CustomGetProducts_Adp(getActivity(),arrayListAreaLocal);
                select_area_recycle.setAdapter(customGetProducts_adp);
                select_area_recycle.setNestedScrollingEnabled(false);
            }
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }

    }
    catch (Exception e)
    {
        e.printStackTrace();
    }

        select_area_optional_dialog.show();

    }
//-----------------------------------fuction for set data-------------on popup------------
    private void setData(String i) {
        arrayListArealocalselected = new ArrayList<ItemModelArea_Selected>();
        arrayListArealocalselected.clear();
        arrayListArealocalselected.isEmpty();
        idd = "";
        System.out.println("Exception is###" + arrayListArealocalselected.size());

        try{
            if (arrayListAreaLocal != null) {
            for (int j = 0; j < arrayListAreaLocal.size(); j++)
            {
                if (arrayListAreaLocal.get(j).isSelected()) {

                    System.out.println("Exception is###" + arrayListAreaLocal.get(j).isSelected());

                    String id = arrayListAreaLocal.get(j).getArea_id();
                    String name = arrayListAreaLocal.get(j).getArea_name();
                    String region = arrayListAreaLocal.get(j).getArea_region();
                    String city = arrayListAreaLocal.get(j).getArea_city();
                    String code = arrayListAreaLocal.get(j).getArea_code();
                    String interior = arrayListAreaLocal.get(j).getArea_interiors();
                    boolean isSelected1 = arrayListAreaLocal.get(j).isSelected();

                   // select_area_local_relative_view.setVisibility(View.VISIBLE);
                   // show_select_area_list_local.append(name+ "\n");

                    if (!arrayListAreaLocal.get(j).getArea_id().equalsIgnoreCase("null"))
                    {
                        idd += arrayListAreaLocal.get(j).getArea_id()+",";
                    }

                    arrayListArealocalselected.add(new ItemModelArea_Selected(id, name,region,city,code,interior,isSelected1));
                }
            }

            if (arrayListArealocalselected.size()==0)
            {
                System.out.println("46#########333---- "+idd);
                //select_area_local_relative_view.setVisibility(View.GONE);
            }

            if (!i.equalsIgnoreCase("0"))
            {
                update_area();
            }
            System.out.println("46#########333---- "+idd);
        }

    }
    catch (Exception e)
    {
       e.printStackTrace();
    }
}

    //calling api for get dtaa
// ---------------------------- For WebService Call for get Search Doctor Suggestion -------------------------------------------------------------------------------//
private void getData_ForArea() {

    String url = "http://dailyreporting.in/" + company_name + "/api/all_area";
    System.out.println("sout url" + url);

    // String url = "http://192.168.1.5/pharma_webservices/api/all_area";
    // System.out.println("sout url" + url);

    loader.setVisibility(View.VISIBLE);

    // Tag used to cancel the request
    String cancel_req_tag = "area";
    StringRequest strReq = new StringRequest(Request.Method.POST, url,
            new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("User Allocate Products", "User Allocate Products response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);

                        String errorMsg = jObj.getString("message");
                        String status = jObj.getString("status");
                        if (status.equals("success")) {
                            loader.setVisibility(View.GONE);
                            JSONArray jsonArray = jObj.getJSONArray("result");
                            System.out.println("jSonArryA Length "+ jsonArray.length());

                            if (jsonArray.length()==0)
                            {
                                int json_length = jsonArray.length();
                                openDialogForArea(json_length);
                            }
                            else
                            {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String area_id = jsonObject1.getString("area_id");
                                    String area_type = jsonObject1.getString("area_type");
                                    String area_city = jsonObject1.getString("city");
                                    String area_code = jsonObject1.getString("area_code");
                                    String area_region = jsonObject1.getString("region");
                                    String area_interiors = jsonObject1.getString("interiors");
                                    String area_name = jsonObject1.getString("area_name");


                                    arrayListAreaLocal.clear();
                                    arrayListAreaLocal.add(new ItemModelArea(area_id,area_name,area_region,area_city
                                                ,area_code,area_interiors));

                                }

                                openDialogForArea(jsonArray.length());

                            }

                        }
                        else if (status.equals("Error")){
                            loader.setVisibility(View.GONE);
                           // select_area_recycle.setVisibility(View.GONE);
                           // txt_show_error.setVisibility(View.VISIBLE);
                            openDialogForArea(0);
                            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                            Log.e("errorMsg", errorMsg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            loader.setVisibility(View.GONE);
           // select_area_recycle.setVisibility(View.GONE);
           // txt_show_error.setVisibility(View.VISIBLE);
            openDialogForArea(0);

            Log.e("UserAllocateAreas", "User Allocate area Error: " + error.getMessage());
        }
    }) {


        //This is for Headers If You Needed
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("X-API-KEY", "TEST@123");
            return params;
        }

        @Override
        protected Map<String, String> getParams() {
            // Posting params to login url
            Map<String, String> params = new HashMap<String, String>();

            if (dcr_work_location.equalsIgnoreCase("1"))
            {
                params.put("interior",dcr_interior_idd);
                params.put("work_type","interior");
                System.out.println("work type interior id ###### "+dcr_interior_idd+" "+dcr_work_location);
            }
            else
            {
                params.put("city", dcr_city_idd);
                params.put("work_type","local");
                System.out.println("work type ## city_id  "+dcr_city_idd+" "+dcr_work_location);
            }
            return params;
        }
    };
    // Adding request to request queue
    AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
}
//-------------------------------------for update area ---------------------------------------


    // ---------------------------- For WebService Call for get Search Doctor Suggestion -------------------------------------------------------------------------------//
    private void update_area() {

        String url = "http://dailyreporting.in/" + company_name + "/api/update_area";
        System.out.println("sout url" + url);

        // String url = "http://192.168.1.5/pharma_webservices/api/all_area";
        // System.out.println("sout url" + url);
        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("User Update area", "User update area: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);

                            String errorMsg = jObj.getString("message");
                            String status = jObj.getString("status");
                            if (status.equals("success")) {
                                loader.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                            }
                            else if (status.equals("Error")){
                                loader.setVisibility(View.GONE);
                                // select_area_recycle.setVisibility(View.GONE);
                                // txt_show_error.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                                Log.e("errorMsg", errorMsg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.GONE);
                Log.e("UserAllocateAreas", "User Allocate area Error: " + error.getMessage());
            }
        }) {


            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-API-KEY", "TEST@123");
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();

                    params.put("user_id",user_id4);
                    params.put("area",idd);
                    System.out.println("user paramss ###### "+user_id4+" "+idd);

                    return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }




    // ---------------------------- Set Custom Gridview for Content -------------------------------------------------------------------------------//

    private class CustomGridviewForContent extends BaseAdapter
    {
        private Context mContext;
        ArrayList<String> arr_content2;
        ArrayList<Integer> images;

        public CustomGridviewForContent(Context c, ArrayList<String> arr_content, ArrayList<Integer> images) {
            mContext = c;
            this.images = images;
            this.arr_content2=arr_content;
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
                            if(doc_id!=null)
                            {
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.contentFrame, new Search_Doctors_Fragment())
                                        .addToBackStack("2")
                                        .commit();
                             /*   CallStart_Fragment selectedFragment = CallStart_Fragment.newInstance();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();



                                Bundle b=new Bundle();
                                b.putString("doc_id",doc_id);


                                selectedFragment.setArguments(b);
                                transaction.replace(R.id.contentFrame, selectedFragment);
                                transaction.addToBackStack("20");
                                transaction.commit();*/
                            }

                            else {
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.contentFrame, new Search_Doctors_Fragment())
                                        .addToBackStack("2")
                                        .commit();
                            }
                          /*  getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentFrame, new Search_Doctors_Fragment())
                                    .addToBackStack("20")
                                    .commit();*/

                        }
                        if(position==1)
                        {

                            //Call WebService
                            callWebservicefor_startChemistvisit();

                        }
                        if(position==2)
                        {
                         /*   getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentFrame, new Expenses_Frag())
                                    .commit();*/

                            //Call WebService
                            callWebservicefor_for_Add_expeses_Startws();

                        }

                        if(position==3)
                        {
                            //Call Fragment
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentFrame, new Today_Dcr_Frag())
                                    .addToBackStack("2")
                                    .commit();
                        }


                        if (position==4)
                        {
                            /*getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentFrame, new Map_My_Work())
                                    .addToBackStack(null)
                                    .commit();*/
                            getData_ForArea();


                        }
                        if(position==5)
                        {
                            //Call Fragment
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentFrame, new Map_My_Work())
                                    .addToBackStack(null)
                                    .commit();
                        }

                        if (role_id.equalsIgnoreCase("5"))
                        {
                            if(position==6)
                            {
                                //Call Fragment
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.contentFrame, new HomeFragments())
                                        .addToBackStack("1")
                                        .commit();
                            }
                        }
                        else
                        {

                            if(position==6)
                            {
                                //Toast.makeText(mContext, "6", Toast.LENGTH_SHORT).show();
                                //Call Fragment
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.contentFrame, new MapMyTeam_Work())
                                        .addToBackStack(null)
                                        .commit();
                            }

                            if(position==7)
                            {
                                //Toast.makeText(mContext, "7", Toast.LENGTH_SHORT).show();

                                //Call Fragment
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.contentFrame, new HomeFragments())
                                        .addToBackStack("1")
                                        .commit();
                            }
                        }




                    }
                });


            } else {
                grid = (View) convertView;
            }

            return grid;       }
    }

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
            get_Data_forGetStart();

           /* secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);

            *//*tct_call_hour.setText(""+ InHours);
            txt_callprocess.setText(""+ minutes);
            txt_callprocess_second.setText(String.format("%02d", secs));*/

            customHandler.postDelayed(this, 1000);

        }

    };
    // ---------------------------- WebService Call for Start Chemist Visited-------------------------------------------------------------------------------//
    private void callWebservicefor_startChemistvisit() {


        if(Utils.isConnected(getActivity())) {

            get_Data_forstartChemistVisit();
        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }

    // ---------------------------- WebService Call for Start Chemist Visited-------------------------------------------------------------------------------//
    private void callWebservicefor_for_Add_expeses_Startws() {


        if(Utils.isConnected(getActivity())) {

            for_Add_expeses_Startws();
        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }

    // ---------------------------- For  WebService Call Method for Start Chemist Visited--------------------------------------------------------------------------------//
    private void get_Data_forstartChemistVisit() {

        String url = "http://dailyreporting.in/"+company_name+"/api/start_chemist_visit";
        System.out.println("sout url"+ url);


        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_START_CHEMIST_VISIT, new Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("StartChemistVisit", "Start Chemist Visit: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);

                        String result=jObj.getString("result");
                        System.out.println("Result Start Chemist Visit***"+result);
                        String message=jObj.getString("message");

                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }


                        JSONObject jsonObject44=new JSONObject(result);
                        String dcr_chemist_stockist_live_table_id  =jsonObject44.getString("dcr_chemist_stockist_live_table_id");
                        MySharedPref sp=new MySharedPref();
                        sp.saveData(getActivity(),"dcr_chemist_stockist_live_table_id",dcr_chemist_stockist_live_table_id );

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFrame, new Add_Chemist_Fragment())
                                .addToBackStack("2")
                                .commit();

                    } else {

                        loader.setVisibility(View.GONE);
                        String errorMsg = jObj.getString("message");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.GONE);

                Log.e("StartChemistVisit", "Start Chemist Visit: " + error.getMessage());
                try {
                    Toast.makeText(getActivity(), "Server not responding.",Toast.LENGTH_SHORT).show();
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

                System.out.println("Dcr Doctor Live Table Insert id###"+String.valueOf(currentlat));
                System.out.println("Dcr Doctor Live Table Insert id###"+String.valueOf(currentlong));
                System.out.println("Dcr Doctor Live Table Insert id###"+dcr_table_insert_id);
                System.out.println("Dcr Doctor Live Table Insert id###"+user_id4);
                System.out.println("Dcr Doctor Live Table Insert id###"+doc_id);


                params.put("user_id",user_id4);
                params.put("dcr_table_insert_id",dcr_table_insert_id);
                params.put("checkin_lat", String.valueOf(currentlat));
                params.put("checkin_long", String.valueOf(currentlong));


                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }
    // ---------------------------- For  WebService Call Method for Add Expenses--------------------------------------------------------------------------------//
    private void for_Add_expeses_Startws() {
        final String region_id = sp.getData(getActivity(),"region_id","null");

        String url = "http://dailyreporting.in/"+company_name+"/api/day_e_dcr_rv_record";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_DAY_EDCRRVRECORD, new Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Expenses Record:", "ExpensesRecord Visit: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    String work_via  =jObj.getString("work_via");
                    System.out.println("work_via is###" + work_via);
                    sp.saveData(getActivity(),"work_via",work_via);

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);

                        String result=jObj.getString("result");
                        System.out.println("Result Expenses Record:***"+result);
                        String message=jObj.getString("message");

                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }


                        JSONObject jsonObject44=new JSONObject(result);
                        String field_work_allowence  =jsonObject44.getString("field_work_allowence");
                        MySharedPref sp=new MySharedPref();
                        sp.saveData(getActivity(),"field_work_allowence",field_work_allowence);

                try {

                    String petrol  =jsonObject44.getString("petrol");
                    System.out.println("petrol is###" + petrol);
                    sp.saveData(getActivity(),"petrol",petrol);


                    String work_via1  =jObj.getString("work_via");
                    System.out.println("work_via is###" + work_via1);
                    sp.saveData(getActivity(),"work_via",work_via1);

                }
                catch (Exception e)
                {
                   sp.saveData(getActivity(),"petrol","null");
                   System.out.println("Exception is###"+e);
                }

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFrame, new Expenses_Frag())
                                .addToBackStack("27")
                                .commit();


                    }
                    else {
                        loader.setVisibility(View.GONE);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFrame, new Expenses_Frag())
                                .addToBackStack("27")
                                .commit();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.GONE);

                Log.e("Expenses Record:", "ExpensesRecord:: " + error.getMessage());
                try {
                    Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
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

                System.out.println("Dcr Doctor Live Table Insert id###"+String.valueOf(currentlat));
                System.out.println("Dcr Doctor Live Table Insert id###"+String.valueOf(currentlong));
                System.out.println("Dcr Doctor Live Table Insert id###"+dcr_table_insert_id);
                System.out.println("Dcr Doctor Live Table Insert id###"+user_id4);
                System.out.println("Dcr Doctor Live Table Insert id###"+region_id +"role "+role_id);


                params.put("user_id",user_id4);
                params.put("role_id",role_id);
                params.put("region",region_id);

                params.put("dcr_table_insert_id",dcr_table_insert_id);
                params.put("checkin_lat", String.valueOf(currentlat));
                params.put("checkin_long", String.valueOf(currentlong));


                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }



 //-----------------------------------api for get field work time------------
 private void get_Data_forGetStart() {

     String url = "http://dailyreporting.in/"+company_name+"/api/check_user_start_fieldwork";
     System.out.println("sout url"+ url);


     // Tag used to cancel the request
     String cancel_req_tag = "area";
//     StringRequest strReq = new StringRequest(Request.Method.POST,
//             URLs.URL_CHECK_USER_START_FIELDWORK, new Response.Listener<String>() {
         //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

     StringRequest strReq = new StringRequest(Request.Method.POST,
            url, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
            Log.d("CheckUserFieldWork", "CheckUserFieldWork: " + response.toString());

             try {
                 JSONObject jObj = new JSONObject(response);
                 String error = jObj.getString("status");

                  String err_message = jObj.getString("message");
                  if (error.equals("error")) {

                       String result=jObj.getString("result");
                       System.out.println("Result CheckUserFieldWork***"+result);
                       String message=jObj.getString("message");

                       String dcr_table_insert_idd = jObj.getString("dcr_table_insert_id");
                       String dcr_live_table_insert_idd = jObj.getString("dcr_live_table_insert_id");

                       dcr_work_location = jObj.getString("work_location");
                       dcr_interior_idd = jObj.getString("interior_id");
                       dcr_city_idd = jObj.getString("city_id");
                       area_id = jObj.getString("area_id");
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
                     if (field_work_time!=null)
                     {
                         // sp.saveData(getActivity(),"work_time",field_work_time);
                         if (field_work_time != null) {
                             String[] items = field_work_time.split(":");
                             for (String item : items) {
                                 System.out.println("item = " + item);
                                 item1 = items[0];
                                 item2 = items[1];
                                 item3 = items[2];
                               //  System.out.println("item = " + item1);
                                // System.out.println("item = " + item2);
                                // System.out.println("item = " + item3);

                                 tct_call_hour.setText(""+ item1);
                                 txt_callprocess.setText(""+ item2);
                                 txt_callprocess_second.setText(""+item3);

                             }

                         }
                     }
                      if (field_work_time!=null)
                      {
                          sp.saveData(getActivity(),"work_time",field_work_time);
                      }
// ---------------------------- Set Custom Gridview and Adapter -------------------------------------------------------------------------------//
                 }


             } catch (Exception e) {
                 e.printStackTrace();
             }

         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {

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

    @Override
    public void onStop() {
        customHandler.removeCallbacks(updateTimerThread);
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        customHandler.removeCallbacksAndMessages(updateTimerThread);

    }
    @Override
    public void onPause() {
        customHandler.removeCallbacks(updateTimerThread);
        super.onPause();
    }
//------------------------------------------------------------------------------------------------------------------------------------------------
    private class CustomGetProducts_Adp extends RecyclerView.Adapter<CustomGetProducts_Adp.MyViewHolder> {

    Activity activity;
    int numberOfCheckboxesChecked = 0;
    ArrayList<Integer> l1;
    List<ItemModelArea> arr_all_search_doctors3;
    int in_id;

    public CustomGetProducts_Adp(FragmentActivity activity, ArrayList<ItemModelArea> arrayListAreaLocal) {

        this.activity = activity;
        this.arr_all_search_doctors3 = arrayListAreaLocal;
        l1 = new ArrayList<>();

       if (arrayListArealocalselected!= null) {
            if (arrayListArealocalselected.size() > 0) {
                l1 = new ArrayList<>();

                System.out.println("Size Checkbox Constructor###" + arrayListArealocalselected.size());
                for (int j = 0; j < arrayListArealocalselected.size(); j++) {
                    l1.add(Integer.valueOf(arrayListArealocalselected.get(j).getArea_id()));
                }
            }
        }
    }

    @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_areas_adpter_view, parent, false);

        return new CustomGetProducts_Adp.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            String str_doc_name = arr_all_search_doctors3.get(position).getArea_name();
            String upperString_str_doc_name = str_doc_name.substring(0, 1).toUpperCase() + str_doc_name.substring(1);
            holder.txt_product.setText(upperString_str_doc_name);

            if (arrayListArealocalselected != null) {
                if (arrayListArealocalselected.size() > 0) {
                    System.out.println("l1.size()" + arrayListArealocalselected.size());

                    for (int j = 0; j < arrayListArealocalselected.size(); j++) {
                        in_id = l1.get(j);
                        int id = Integer.parseInt(arrayListArealocalselected.get(position).getArea_id());

                        System.out.println("Int id###" + id + " 36543752 "+ in_id);
                        System.out.println("Size Checkbox_Position###" + arrayListArealocalselected.size());

                        if (id == in_id) {

                            System.out.println("##1323");
                            chk_left_checked.setChecked(arrayListArealocalselected.get(j).getSelected());
                            numberOfCheckboxesChecked = arrayListArealocalselected.size();
                            arrayListAreaLocal.get(position).setSelected(true);
                        }

                        System.out.println("Int id###" + numberOfCheckboxesChecked);
                        System.out.println("Size Checkbox_Position###" + position);
                    }
                }

            }



            chk_left_checked.setTag(new Integer(position));

            //CheckBox OnClickListner
            chk_left_checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Integer pos = (Integer) chk_left_checked.getTag();
                    // Toast.makeText(ctx, imageModelArrayList.get(pos).getAnimal() + " clicked!", Toast.LENGTH_SHORT).show();
                    if (cb.isChecked()) {
                        numberOfCheckboxesChecked++;
                        System.out.println("Int Selected###" + numberOfCheckboxesChecked);
                        //   notifyDataSetChanged();
                        arrayListAreaLocal.get(position).setSelected(true);

                    }
                    else {

                        numberOfCheckboxesChecked--;
                        System.out.println("Int UnSelected###" + numberOfCheckboxesChecked);
                        // qty_lst.remove(l1.get(position));
                        arrayListAreaLocal.get(position).setSelected(false);

                        //  customGetProducts_adp.notifyDataSetChanged();
                        //
                    }
                }
            });

        }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return  arr_all_search_doctors3.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_product;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_product = (TextView) itemView.findViewById(R.id.txt_area);
            chk_left_checked = (CheckBox) itemView.findViewById(R.id.chk_left_checked);
        }
    }
    }
}
