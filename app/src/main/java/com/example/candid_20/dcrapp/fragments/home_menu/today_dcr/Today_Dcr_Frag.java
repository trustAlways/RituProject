package com.example.candid_20.dcrapp.fragments.home_menu.today_dcr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.bean.fullview_timeline_doctor_bean.TimeLineMainResult_Bean;
import com.example.candid_20.dcrapp.bean.fullview_timeline_doctor_bean.TimeLineResultDoctor_Bean;
import com.example.candid_20.dcrapp.bean.fullview_timeline_doctor_bean.TimeLineResultStockiest;
import com.example.candid_20.dcrapp.constant.Utils;
import com.example.candid_20.dcrapp.fragments.home_menu.for_view_timeline.TimeLine_FullView_Frag;
import com.example.candid_20.dcrapp.other.URLs;
import com.example.candid_20.dcrapp.storage.MySharedPref;
import com.example.candid_20.dcrapp.volleyconnector.AppSingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Today_Dcr_Frag extends Fragment {
    View v;
    MySharedPref sp;
    RelativeLayout rr_InboxDetailRVv,rr_InboxDetailRV;
    RelativeLayout rr_selected_date,rr_selected_strtfieldwork_at,rr_selected_strtfieldwork_location,
            rr_selected_cityname,rr_selected_interiorname,rr_slect_workvia,rr_main_workingwith,
            rr_selected_workingwith_independent,rr_selected_workingwith_areamanger,
            rr_selected_workingwith_regionalmanger,rr_selected_workingwith_salesmanger,
            rr_selected_workingtype,rr_selected_workingwith_marketingrepresentation,rr_main_full_descp,rr_todays_start_end_time,rr_end_time_relative,
            rr_start_work_location,rr_end_work_location;
    RecyclerView InboxDetailRV,InboxDetailRVv;
    TimeLineMainResult_Bean timeLineMainResult_bean;
    TextView txt_title,txt_selected_datee,txt_selected_strtfieldwork_att,txt_selected_strtfieldwork_locationn,
            txt_selected_citynamee,txt_selected_interiornamee,
            txt_selected_workingwithh,
            txt_value_workvia,
              txt_selected_workingwithh_independentt,
            txt_selected_workingwithh_areamanger,txt_selected_workingwithh_marketingrepresentationn,
            txt_selected_workingwithh_regionalmangerr,
            txt_selected_workingwithh_salesmangerr,txt_selected_workingtypee,txt_strt_end_time_value,txt_end_work_time,txt_strt_location,
            txt_end_location,no_data_available;

    TodayDcr_Doctors_Adp todayDcr_doctors_adp;
    TodayDCR_Stockiest_Adp todayDCR_stockiest_adp;
    //String Sharedpreferences Data
    String ldata,user_id4,token,today_date,current_date,company_name;
    boolean ischeckedpermission;
    ProgressBar loader;

    public static Today_Dcr_Frag newInstance()
    {
        Today_Dcr_Frag fragment = new Today_Dcr_Frag();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.full_viewtimeline, container, false);
        // ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//
        getSaveddata();

        // ---------------------------- For Check Permission -------------------------------------------------------------------------------//
        checkForPermission();

        // ---------------------------- For  Bundle Data -------------------------------------------------------------------------------//
      //  getBundleData();

        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();
        return v;
    }
    // ---------------------------- For  Bundle Data -------------------------------------------------------------------------------//
    private void getBundleData() {

        Bundle b1=this.getArguments();
        today_date = b1.getString("today_date");
        System.out.println("Today Date###"+today_date);

    }


    // ---------------------------- For Check Permission -------------------------------------------------------------------------------//
    private void checkForPermission() {

        //Check permission for Location
        int permissionCheckForReadcalender = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR);
        int permissionCheckForwritecalender = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR);


        if (permissionCheckForReadcalender != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForwritecalender != PackageManager.PERMISSION_GRANTED
                ) {

            ischeckedpermission=false;

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CALENDAR,
                            Manifest.permission.WRITE_CALENDAR

                    },
                    1001);


        }
        else
        {
            ischeckedpermission=true;

   /*         getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, new HomeFragments())
                    .commit();*/

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

                perms.put(Manifest.permission.READ_CALENDAR, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_CALENDAR, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.READ_CALENDAR )== PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_CALENDAR )== PackageManager.PERMISSION_GRANTED

                            )
                    {
                        Log.d("", "sms & location services permission granted");
                        ischeckedpermission=true;


// process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("", "Some permissions are not granted ask again ");

                        ischeckedpermission=false;

                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CALENDAR)
                                || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_CALENDAR))
                        {
                            showDialogOK("Permission must required for this app",
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

                            showDialogOK("Permission must required for this app",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ischeckedpermission = false;

                                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,

                                                    Uri.fromParts("package", getActivity().getPackageName(), null)));

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
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                //.setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
    private void initUi() {
        // ---------------------------- Casting Element -------------------------------------------------------------------------------//
        //Casting TextView for Title
        txt_title=(TextView)getActivity().findViewById(R.id.txt_title);

        no_data_available =(TextView) v.findViewById(R.id.no_data);

        //Casting RelativeLayout for Date
        rr_selected_date=(RelativeLayout)v.findViewById(R.id.rr_selected_date);
        //Casting RelativeLayout for Start Field Work at
        rr_selected_strtfieldwork_at=(RelativeLayout)v.findViewById(R.id.rr_selected_strtfieldwork_at);
        //Casting RelativeLayout for Main Full Description
        rr_main_full_descp=(RelativeLayout)v.findViewById(R.id.rr_main_full_descp);
        //Casting RelativeLayout for Start Field Work Location
        rr_selected_strtfieldwork_location=(RelativeLayout)v.findViewById(R.id.rr_selected_strtfieldwork_location);
        //Casting RelativeLayout for Start Field Work City Name
        rr_selected_cityname=(RelativeLayout)v.findViewById(R.id.rr_selected_cityname);
        //Casting RelativeLayout for Start Field Work Interior Name
        rr_selected_interiorname=(RelativeLayout)v.findViewById(R.id.rr_selected_interiorname);
        //Casting RelativeLayout for Start Field Working with
        rr_main_workingwith=(RelativeLayout)v.findViewById(R.id.rr_main_workingwith);

        //Casting RelativeLayout for work via
        rr_slect_workvia=(RelativeLayout)v.findViewById(R.id.rr_selected_workingvia);
        txt_value_workvia = (TextView)v.findViewById(R.id.txt_value_workingvia);

        //Casting RelativeLayout for Start Field Working with Independent
        rr_selected_workingwith_independent=(RelativeLayout)v.findViewById(R.id.rr_selected_workingwith_independent);
        //Casting RelativeLayout for Start Field Working with AreaManger
        rr_selected_workingwith_areamanger=(RelativeLayout)v.findViewById(R.id.rr_selected_workingwith_areamanger);
        //Casting RelativeLayout for Start Field Working with RegionalManger
        rr_selected_workingwith_regionalmanger=(RelativeLayout)v.findViewById(R.id.rr_selected_workingwith_regionalmanger);
        //Casting RelativeLayout for Start Field Working with SalesMamManger
        rr_selected_workingwith_salesmanger=(RelativeLayout)v.findViewById(R.id.rr_selected_workingwith_salesmanger);
        //Casting RelativeLayout for Start Field Working Type
        rr_selected_workingtype=(RelativeLayout)v.findViewById(R.id.rr_selected_workingtype);
        //Casting RelativeLayout for Start Field Marketing Representation
        rr_selected_workingwith_marketingrepresentation=(RelativeLayout)v.findViewById(R.id.rr_selected_workingwith_marketingrepresentation);
        //Casting RelativeLayout for Start end timeRepresentation
        rr_todays_start_end_time=(RelativeLayout)v.findViewById(R.id.today_start_end_time);
        rr_end_time_relative=(RelativeLayout)v.findViewById(R.id.end_time_relativelayout);

        //Casting RelativeLayout for strt work time location
        rr_start_work_location=(RelativeLayout)v.findViewById(R.id.rr_work_start_location);
        txt_strt_location =(TextView) v.findViewById(R.id.work_strt_location);

        rr_end_work_location=(RelativeLayout)v.findViewById(R.id.rr_work_end_location);
        txt_end_location =(TextView) v.findViewById(R.id.work_end_location);
                //Casting TextView for Date
        txt_selected_datee=(TextView)v.findViewById(R.id.txt_selected_datee);
        //Casting TextView for Start Field Work at
        txt_selected_strtfieldwork_att=(TextView)v.findViewById(R.id.txt_selected_strtfieldwork_att);
        //Casting TextView for Start Field Work Location
        txt_selected_strtfieldwork_locationn=(TextView)v.findViewById(R.id.txt_selected_strtfieldwork_locationn);
        //Casting TextView for Start Field Work City Name Interior
        txt_selected_citynamee=(TextView)v.findViewById(R.id.txt_selected_citynamee);

        //Casting TextView for Start Field Work Interior Name
        txt_selected_interiornamee=(TextView)v.findViewById(R.id.txt_selected_interiornamee);
        //Casting TextView for Start Field Working With
        txt_selected_workingwithh=(TextView)v.findViewById(R.id.txt_selected_workingwithh);
        //Casting TextView for Start Field Working With Independent
        txt_selected_workingwithh_independentt=(TextView)v.findViewById(R.id.txt_selected_workingwithh_independentt);
        //Casting TextView for Start Field Working With AreaManager
        txt_selected_workingwithh_areamanger=(TextView)v.findViewById(R.id.txt_selected_workingwithh_areamanger);
        //Casting TextView for Start Field Working With RegionalManager
        txt_selected_workingwithh_regionalmangerr=(TextView)v.findViewById(R.id.txt_selected_workingwithh_regionalmangerr);
        //Casting TextView for Start Field Working With SalesManager
        txt_selected_workingwithh_salesmangerr=(TextView)v.findViewById(R.id.txt_selected_workingwithh_salesmangerr);
        //Casting TextView for Start Field Working Type
        txt_selected_workingtypee=(TextView)v.findViewById(R.id.txt_selected_workingtypee);
        //Casting TextView for Start Field Marketing Representation
        txt_selected_workingwithh_marketingrepresentationn=(TextView)v.findViewById(R.id.txt_selected_workingwithh_marketingrepresentationn);
        //Casting TextView for Start end time Representation
        txt_strt_end_time_value = (TextView)v.findViewById(R.id.start_end_time_value);
        txt_end_work_time = (TextView)v.findViewById(R.id.end_time_value);

        //Casting RelativeLayout for Search Doctor Click Search icon
        rr_InboxDetailRVv=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRVv);
        //Casting RelativeLayout for Search Doctor
        rr_InboxDetailRV=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRV);
        //Casting RecycleView for Search Doctor
        InboxDetailRV = (RecyclerView) v.findViewById(R.id.InboxDetailRV);

        //Casting RecycleView for Search Doctor Click Search icon
        InboxDetailRVv = (RecyclerView) v.findViewById(R.id.InboxDetailRVv);

        //Casting ProgressBar for loading
        loader=(ProgressBar)v.findViewById(R.id.loader);
//For  Search DoctorRecycle Create by these

        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        InboxDetailRV.setLayoutManager(mLayoutManager2);
        InboxDetailRV.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRV.setItemAnimator(new DefaultItemAnimator());

        //DoctorRecycle Create by these for Search Doctor Click Search icon

        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity());
        InboxDetailRVv.setLayoutManager(mLayoutManager3);
        InboxDetailRVv.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRVv.setItemAnimator(new DefaultItemAnimator());

//For get Current Date
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        today_date  = df.format(c);
        System.out.println("Today Date is###"+today_date);

        //Set Text Title
        txt_title.setText("Timeline : "+today_date);
      //Call Webservice
        callWebservicefor_getAllInteriorAcctoCity(today_date);




    }
    // ---------------------------- WebService Call for Getall City-------------------------------------------------------------------------------//
    private void callWebservicefor_getAllInteriorAcctoCity(String city_id2) {


        if(Utils.isConnected(getActivity())) {

            getData_ForSearchSuggestion(city_id2);
        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }

    // ---------------------------- For WebService Call for get Search Doctor Suggestion -------------------------------------------------------------------------------//
    private void getData_ForSearchSuggestion(final String cityid4) {

        String url = "http://dailyreporting.in/"+company_name+"/api/view_timeline";
        System.out.println("sout url"+ url);


        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_VIEW_TIMELINE,
                new com.android.volley.Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("All City List AccState", "All City List AccStatet response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    String result = jObj.getString("result");

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);

                        if (!result.equalsIgnoreCase("")) {

                            Gson gson = new Gson();

                            timeLineMainResult_bean = gson.fromJson(response, TimeLineMainResult_Bean.class);
                            System.out.println("List Size interior:" + timeLineMainResult_bean.getResult().size());
                        /*System.out.println("Base URl Old^^^"+str_base_url);
                        String str_new_base_url=str_base_url.replace("candid-15-pc","192.168.1.2");
                        System.out.println("Base URl New^^^"+str_new_base_url);*/

                            if (timeLineMainResult_bean.getResult().size() > 0) {


                                //   btn_add_products.setVisibility(View.VISIBLE);


                                Collections.sort(timeLineMainResult_bean.getResult(), new Comparator<TimeLineResultDoctor_Bean>() {
                                    DateFormat f = new SimpleDateFormat("hh:mm a");
                                    @Override
                                    public int compare(TimeLineResultDoctor_Bean lhs, TimeLineResultDoctor_Bean rhs) {
                                        try {
                                            return f.parse(lhs.getDrCinT()).compareTo(f.parse(rhs.getDrCinT()));
                                        } catch (ParseException e) {
                                            throw new IllegalArgumentException(e);
                                        }

                                    }
                                });

                                Collections.reverse(timeLineMainResult_bean.getResult());

                                // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//
                                todayDcr_doctors_adp = new TodayDcr_Doctors_Adp(getActivity(), timeLineMainResult_bean.getResult());
                                InboxDetailRV.setAdapter(todayDcr_doctors_adp);
                                rr_InboxDetailRV.setVisibility(View.VISIBLE);
                                InboxDetailRV.setVisibility(View.VISIBLE);

                            } else {
                                rr_InboxDetailRV.setVisibility(View.GONE);
                                InboxDetailRV.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), " No Doctor call, Reminder and chemist Visted till now", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            no_data_available.setVisibility(View.VISIBLE);

                        }
                        /*if(timeLineMainResult_bean.getResult().getResultStockist().size()>0) {
                            // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//
                            Collections.reverse(timeLineMainResult_bean.getResult().getResultStockist());

                            todayDCR_stockiest_adp = new TodayDCR_Stockiest_Adp(getActivity(),timeLineMainResult_bean.getResult().getResultStockist());
                            InboxDetailRVv.setAdapter(todayDCR_stockiest_adp);
                            rr_InboxDetailRVv.setVisibility(View.VISIBLE);
                            InboxDetailRVv.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            rr_InboxDetailRVv.setVisibility(View.GONE);
                            InboxDetailRVv.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), " No Chemist Visit Available", Toast.LENGTH_SHORT).show();

                        }*/
                        rr_main_full_descp.setVisibility(View.VISIBLE);
                        String field_work_start_record = jObj.getString("field_work_start_record");
                        JSONObject jsonObject44=new JSONObject(field_work_start_record);
                        rr_main_workingwith.setVisibility(View.VISIBLE);
                       // rr_selected_date.setVisibility(View.VISIBLE);
                        //txt_selected_datee.setText("Selected Date is: "+today_date);

                        String marketing_reperesentative=jsonObject44.getString("marketing_reperesentative");
                        System.out.println("Marketing Representative####"+marketing_reperesentative);

                        if(!marketing_reperesentative.equalsIgnoreCase(""))
                        {

                            JSONObject jsonObject33=new JSONObject(marketing_reperesentative);
                            String first_name=jsonObject33.getString("first_name");
                            String last_name=jsonObject33.getString("last_name");
                            String role_name=jsonObject33.getString("role_name");
                            rr_selected_workingwith_marketingrepresentation.setVisibility(View.VISIBLE);
                            txt_selected_workingwithh_marketingrepresentationn.setText(" "+first_name+" "+last_name/*+","+role_name*/);
                        }

                        String area_manager=jsonObject44.getString("area_manager");
                        if(!area_manager.equalsIgnoreCase(""))
                        {
                            System.out.println("Area Manger:"+area_manager);

                            JSONObject jsonObject55=new JSONObject(area_manager);
                            String first_name=jsonObject55.getString("first_name");
                            String last_name=jsonObject55.getString("last_name");
                            String role_name=jsonObject55.getString("role_name");
                            System.out.println("Area First name:"+first_name);

                            rr_selected_workingwith_areamanger.setVisibility(View.VISIBLE);
                            txt_selected_workingwithh_areamanger.setText(first_name+" "+last_name/*+","+role_name*/);
                        }

                        String regional_manager=jsonObject44.getString("regional_manager");
                        if(!regional_manager.equalsIgnoreCase(""))
                        {
                            JSONObject jsonObject66=new JSONObject(regional_manager);
                            String first_name=jsonObject66.getString("first_name");
                            String last_name=jsonObject66.getString("last_name");
                            String role_name=jsonObject66.getString("role_name");

                            rr_selected_workingwith_regionalmanger.setVisibility(View.VISIBLE);
                            txt_selected_workingwithh_regionalmangerr.setText(" "+first_name+" "+last_name/*+","+role_name*/);
                        }
                        String sales_manager=jsonObject44.getString("sales_manager");
                        if(!sales_manager.equalsIgnoreCase(""))
                        {
                            JSONObject jsonObject77=new JSONObject(sales_manager);
                            String first_name=jsonObject77.getString("first_name");
                            String last_name=jsonObject77.getString("last_name");
                            String role_name=jsonObject77.getString("role_name");

                            rr_selected_workingwith_salesmanger.setVisibility(View.VISIBLE);
                            txt_selected_workingwithh_salesmangerr.setText(" "+first_name+" "+last_name/*+","+role_name*/);
                        }
                        String independent=jsonObject44.getString("independent");
                        if(!independent.equalsIgnoreCase(""))
                        {
                            //rr_selected_workingwith_independent.setVisibility(View.VISIBLE);
                            txt_selected_workingwithh.setText(independent);
                        }
                        else
                        {
                            txt_selected_workingwithh.setText("Joint");
                        }

                        String working_at=jsonObject44.getString("working_at");
                        System.out.println("Working At###"+working_at);
                        //rr_selected_strtfieldwork_at.setVisibility(View.VISIBLE);
                       // txt_selected_strtfieldwork_att.setText(working_at);

                        if(working_at.equalsIgnoreCase("Local"))
                        {
                            String city_name=jsonObject44.getString("city_name");
                            JSONObject jsonObject99=new JSONObject(city_name);
                            String name=jsonObject99.getString("name");

                            rr_selected_strtfieldwork_location.setVisibility(View.VISIBLE);
                            txt_selected_strtfieldwork_locationn.setText(working_at+" - "+name);
                        }

                        String area=jsonObject44.getString("area_name");
                        System.out.println("Working area###"+area);
                        //rr_selected_strtfieldwork_at.setVisibility(View.VISIBLE);
                        // txt_selected_strtfieldwork_att.setText(working_at);
                        if(!area.equalsIgnoreCase(""))
                        {
                            rr_selected_strtfieldwork_at.setVisibility(View.VISIBLE);
                            txt_selected_strtfieldwork_att.setText(area);
                        }


                        if(working_at.equalsIgnoreCase("Interior"))
                        {
                            //For Interior name
                            String interior_name=jsonObject44.getString("interior_name");
                            JSONObject jsonObject88=new JSONObject(interior_name);
                            String interior=jsonObject88.getString("interior");

                            //For City name
                            String city_name=jsonObject44.getString("city_name");
                            JSONObject jsonObject99=new JSONObject(city_name);
                            String name=jsonObject99.getString("name");
                            //For Region name
                            String region_name=jsonObject44.getString("region_name");
                            JSONObject jsonObject10=new JSONObject(region_name);
                            String name1=jsonObject10.getString("name");

                           /* //Set Visibility and Set City and Region name
                            rr_selected_cityname.setVisibility(View.VISIBLE);
                            txt_selected_citynamee.setText("City is: "+name*//*+" ,"+"Region is: "+name1*//*);

                            //Set Visibility and Set Interior  name
                            rr_selected_interiorname.setVisibility(View.VISIBLE);
                            txt_selected_interiornamee.setText("Interior is: "+interior);
*/
                            rr_selected_strtfieldwork_location.setVisibility(View.VISIBLE);
                            txt_selected_strtfieldwork_locationn.setText(working_at+" - "+interior);
                        }

                        String work_type=jsonObject44.getString("work_type");

                        rr_selected_workingtype.setVisibility(View.VISIBLE);
                        txt_selected_workingtypee.setText(work_type);

                        String work_via=jsonObject44.getString("work_via");

                       if (!work_via.equalsIgnoreCase("null") && !work_via.equalsIgnoreCase(""))
                        {
                            rr_slect_workvia.setVisibility(View.VISIBLE);
                            txt_value_workvia.setText(work_via);
                        }
                        String start_lat=jsonObject44.getString("start_lat");
                        String start_long=jsonObject44.getString("start_long");

                        System.out.println("Start latitude is###"+start_lat);
                        System.out.println("Start longitude is###"+start_long);


                        try{
                            double dub_start_lat= Double.parseDouble(start_lat);
                            double dub_start_long= Double.parseDouble(start_long);
                            String str_address=getAddressFromLatlong(dub_start_lat,dub_start_long);

                            if(!str_address.equalsIgnoreCase(""))
                            {
                                 rr_start_work_location.setVisibility(View.VISIBLE);
                                 txt_strt_location.setText(str_address);
                            }
                        }
                          catch(Exception e)
                        {
                            e.printStackTrace();
                        }


                        String end_lat=jsonObject44.getString("end_lat");
                        String end_long=jsonObject44.getString("end_long");

                        System.out.println("end latitude is###"+end_lat);
                        System.out.println("end longitude is###"+end_long);


                        try{
                            double dub_end_lat= Double.parseDouble(end_lat);
                            double dub_end_long= Double.parseDouble(end_long);
                            if (dub_end_lat!=0.0)
                            {
                                String str_address=getAddressFromLatlong(dub_end_lat,dub_end_long);
                                if(!str_address.equalsIgnoreCase(""))
                            {
                                rr_end_work_location.setVisibility(View.VISIBLE);
                                txt_end_location.setText(str_address);
                            }
                        }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }


                        String start_time=jsonObject44.getString("start_time");
                        String end_time=jsonObject44.getString("end_time");

                        System.out.println("Start latitude is###"+start_time);
                        System.out.println("Start longitude is###"+end_time);

                          if (!start_time.equalsIgnoreCase(""))
                            {
                                rr_todays_start_end_time.setVisibility(View.VISIBLE);
                                txt_strt_end_time_value.setText(start_time);
                            }


                            if (!end_time.equalsIgnoreCase(""))
                            {
                                rr_end_time_relative.setVisibility(View.VISIBLE);
                                txt_end_work_time.setText(end_time);
                            }


                        String errorMsg = jObj.getString("message");

                        if(getActivity()!=null) {
                         /*   txt_avail_pro.setVisibility(View.VISIBLE);
                            txt_avail_pro.setText("No products added yet.");*/
                            //  Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        rr_main_full_descp.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);

                        String errorMsg = jObj.getString("message");
                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                        }

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
                Log.e("AllCityListAccState", "All City List AccState Error: " + error.getMessage());
                try {
                    rr_main_full_descp.setVisibility(View.GONE);

                    if(error.getMessage()!=null) {
                        /*JSONObject jsonObject22 = new JSONObject(error.getMessage());

                        String errorMsg = jsonObject22.getString("message");*/
                        //  JSONObject jsonObject33=new JSONObject(message);
                        //   String password=jsonObject33.getString("password");

                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

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
                //      params.put("Authorization","Bearer "+token);

                /*params.put("Authorization","Basic YWRtaW46MTIzNA==");
                params.put("Content-Type","application/x-www-form-urlencoded");*/

                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                System.out.println("Search id###"+user_id4);

                System.out.println("Search String###"+cityid4);
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id4);
                params.put("today_date",today_date);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }

    // ---------------------------- Class for RecycleView GridSpacing-------------------------------------------------------------------------------//

    class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
    // ---------------------------- Method Dp to Px converter-------------------------------------------------------------------------------//
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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


                Log.e("Id is@@@",user_id4);
                System.out.println("Id is***" + user_id4);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    // ---------------------------- Class for RecycleView for Search Doctor Adapter-------------------------------------------------------------------------------//
    private class TodayDcr_Doctors_Adp extends RecyclerView.Adapter<TodayDcr_Doctors_Adp.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<TimeLineResultDoctor_Bean> arr_all_search_doctors3;
        List<TimeLineResultDoctor_Bean> arrayList = new ArrayList<>();
        List<TimeLineResultDoctor_Bean> arSearchlist;

        public TodayDcr_Doctors_Adp(Activity activity, List<TimeLineResultDoctor_Bean> arr_all_search_doctors2) {

            this.activity = activity;

            this.arr_all_search_doctors3 = arr_all_search_doctors2;

            this.arrayList = arr_all_search_doctors2;
            this.arSearchlist = new ArrayList<TimeLineResultDoctor_Bean>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size###" + arr_all_search_doctors3.size());
        }


        @Override
        public TodayDcr_Doctors_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.full_viewtimeline_adp, parent, false);

            return new TodayDcr_Doctors_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final TodayDcr_Doctors_Adp.MyViewHolder holder, final int position) {


            String str_interior_name = arr_all_search_doctors3.get(position).getDoctorName();

            if (!str_interior_name.equalsIgnoreCase(""))
            {
                String upperString_str_interior_name = str_interior_name.substring(0, 1).toUpperCase() + str_interior_name.substring(1);
                holder.txt_doctor_namee.setText(upperString_str_interior_name);

            }
            else
            {
                holder.txt_doctor_namee.setText("Name Not Mentioned");
            }

            String str_checkin_time = arr_all_search_doctors3.get(position).getDrCinT();
            String str_checkout_time = arr_all_search_doctors3.get(position).getDrCoutT();

            String str_visit_type = arr_all_search_doctors3.get(position).getVisitType();
            System.out.println("Address Doctor###"+str_visit_type);

            System.out.println("Address Doctor###"+str_checkin_time+" "+str_checkout_time);

            if (str_visit_type.equalsIgnoreCase("0"))
            {
                holder.txt_doc_type.setText("Doctor Call");
            }
            else if (str_visit_type.equalsIgnoreCase("1"))
            {
                holder.txt_doc_type.setText("Reminder");
            }
            else
            {
                holder.txt_doc_type.setText("Chemist Visit");
            }


            if (str_checkout_time.equalsIgnoreCase(""))
            {
                holder.txt_doctor_time.setText(str_checkin_time+" - "+"In Progress");
            }
            else
            {
                holder.txt_doctor_time.setText(str_checkin_time+" - "+str_checkout_time);
            }

            Double dub_cinlat= Double.valueOf(arr_all_search_doctors3.get(position).getDrCinLat());
            Double dub_cinlon= Double.valueOf(arr_all_search_doctors3.get(position).getDrCinLon());

            if (dub_cinlat!=0.0)
            {
                String str_address=getAddressFromLatlong(dub_cinlat,dub_cinlon);
                System.out.println("Address Doctor###"+str_address);
                holder.txt_doc_address.setText(str_address);
            }



            // String str_dr_cout_time= arr_all_search_doctors3.get(position).getDrCoutTime();

            //   holder.txt_doctor_time.setText(str_checkin_time+"-"+str_dr_cout_time);
/*
            holder.rr_current_loc_descp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txt_search_doctor.setText(arr_all_search_doctors3.get(position).getDoctorName());
                    Search_Doctors_Submit_Fragment selectedFragment = Search_Doctors_Submit_Fragment.newInstance();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle b=new Bundle();
                    b.putString("my_String",txt_search_doctor.getText().toString());


                    selectedFragment.setArguments(b);
                    transaction.replace(R.id.contentFrame, selectedFragment);
                    transaction.addToBackStack("8");
                    transaction.commit();

                    txt_search_doctor.setText("");

                }
            });*/
        }

        @Override
        public int getItemCount() {
            return arr_all_search_doctors3.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_doctor_namee, txt_doctor_time,txt_doc_address,txt_doc_type;
            RelativeLayout rr_current_loc_descp;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt_doc_address = (TextView) itemView.findViewById(R.id.txt_doc_address);
                txt_doctor_namee = (TextView) itemView.findViewById(R.id.txt_doctor_namee);
                txt_doctor_time = (TextView) itemView.findViewById(R.id.txt_doctor_time);
                rr_current_loc_descp = (RelativeLayout) itemView.findViewById(R.id.rr_current_loc_descp);
                txt_doc_type = (TextView)itemView.findViewById(R.id.txt_doctor_type);
            }


        }

        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###" + charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (TimeLineResultDoctor_Bean wp : arSearchlist) {
                    if (wp.getDoctorName().toLowerCase().startsWith(charText)) {
                        arrayList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }

    }
    // ---------------------------- Class for RecycleView for Search Doctor Adapter-------------------------------------------------------------------------------//
    private class TodayDCR_Stockiest_Adp extends RecyclerView.Adapter<TodayDCR_Stockiest_Adp.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<TimeLineResultStockiest> arr_all_search_doctors3;
        List<TimeLineResultStockiest> arrayList = new ArrayList<>();
        List<TimeLineResultStockiest> arSearchlist;

        public TodayDCR_Stockiest_Adp(Activity activity, List<TimeLineResultStockiest> arr_all_search_doctors2) {

            this.activity = activity;

            this.arr_all_search_doctors3 = arr_all_search_doctors2;

            this.arrayList = arr_all_search_doctors2;
            this.arSearchlist = new ArrayList<TimeLineResultStockiest>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size###" + arr_all_search_doctors3.size());
        }


        @Override
        public TodayDCR_Stockiest_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.full_viewtimeline_adp, parent, false);

            return new TodayDCR_Stockiest_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final TodayDCR_Stockiest_Adp.MyViewHolder holder, final int position) {


            String str_interior_name = arr_all_search_doctors3.get(position).getChemist();
/*
              try {
                  String upperString_str_interior_name = str_interior_name.substring(0, 1).toUpperCase() + str_interior_name.substring(1);
                  holder.txt_doctor_namee.setText(upperString_str_interior_name);
              }
              catch (StringIndexOutOfBoundsException e)
              {

              }*/
            holder.txt_doctor_namee.setText(str_interior_name);



            String str_checkin_time = arr_all_search_doctors3.get(position).getCheckinTime();

            String str_dr_cout_time = arr_all_search_doctors3.get(position).getCheckoutTime();

            holder.txt_doctor_time.setText(str_checkin_time + "-" + str_dr_cout_time);

            Double dub_cinlat= Double.valueOf(arr_all_search_doctors3.get(position).getCheckinLat());
            Double dub_cinlon= Double.valueOf(arr_all_search_doctors3.get(position).getCheckinLong());

            String str_address=getAddressFromLatlong(dub_cinlat,dub_cinlon);
            System.out.println("Address Stockiest###"+str_address);

            holder.txt_type.setText("Chemist Visit");
            holder.txt_doc_address.setText(str_address);

/*
            holder.rr_current_loc_descp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txt_search_doctor.setText(arr_all_search_doctors3.get(position).getDoctorName());
                    Search_Doctors_Submit_Fragment selectedFragment = Search_Doctors_Submit_Fragment.newInstance();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle b=new Bundle();
                    b.putString("my_String",txt_search_doctor.getText().toString());


                    selectedFragment.setArguments(b);
                    transaction.replace(R.id.contentFrame, selectedFragment);
                    transaction.addToBackStack("8");
                    transaction.commit();

                    txt_search_doctor.setText("");

                }
            });*/
        }

        @Override
        public int getItemCount() {
            return arr_all_search_doctors3.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_doctor_namee, txt_doctor_time,txt_doc_address,txt_type;
            RelativeLayout rr_current_loc_descp;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt_doc_address= (TextView) itemView.findViewById(R.id.txt_doc_address);
                txt_doctor_namee = (TextView) itemView.findViewById(R.id.txt_doctor_namee);
                txt_doctor_time = (TextView) itemView.findViewById(R.id.txt_doctor_time);
                rr_current_loc_descp = (RelativeLayout) itemView.findViewById(R.id.rr_current_loc_descp);
                txt_type   = (TextView)itemView.findViewById(R.id.txt_doctor_type);
            }


        }

        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###" + charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (TimeLineResultStockiest wp : arSearchlist) {
                    if (wp.getStockistName().toLowerCase().startsWith(charText)) {
                        arrayList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }


    // ---------------------------- For Address from Current latitude and Longitude -------------------------------------------------------------------------------//
    public String getAddressFromLatlong(double currentlat2, double currentlong2)
    {
        Geocoder geocoder;
        String city = null,address=null;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(currentlat2, currentlong2, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            System.out.println("Address$$$"+addresses);

            System.out.println("Address***"+address);

            //Set Address in TextView


        } catch (Exception e) {
            e.printStackTrace();
        }

        return address;
    }

}
