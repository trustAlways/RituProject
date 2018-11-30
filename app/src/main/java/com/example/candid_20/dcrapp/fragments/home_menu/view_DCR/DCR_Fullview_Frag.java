package com.example.candid_20.dcrapp.fragments.home_menu.view_DCR;

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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.activity.MainActivity;
import com.example.candid_20.dcrapp.bean.DCR_REVIEW_DATA.dcr_review_chemist_visited;
import com.example.candid_20.dcrapp.bean.DCR_REVIEW_DATA.dcr_review_docter;
import com.example.candid_20.dcrapp.bean.DCR_REVIEW_DATA.dcr_review_docter_reminder;
import com.example.candid_20.dcrapp.bean.DCR_REVIEW_DATA.dcr_review_expenses;
import com.example.candid_20.dcrapp.bean.fullview_timeline_doctor_bean.TimeLineMainResult_Bean;
import com.example.candid_20.dcrapp.bean.fullview_timeline_doctor_bean.TimeLineResultDoctor_Bean;
import com.example.candid_20.dcrapp.bean.fullview_timeline_doctor_bean.TimeLineResultStockiest;
import com.example.candid_20.dcrapp.constant.Utils;
import com.example.candid_20.dcrapp.fragments.HomeFragments;
import com.example.candid_20.dcrapp.fragments.home_menu.TimeLineFragment;
import com.example.candid_20.dcrapp.fragments.home_menu.for_add_expenses.Expenses_Frag;
import com.example.candid_20.dcrapp.fragments.home_menu.for_view_timeline.TimeLine_FullView_Frag;
import com.example.candid_20.dcrapp.other.URLs;
import com.example.candid_20.dcrapp.storage.MySharedPref;
import com.example.candid_20.dcrapp.volleyconnector.AppSingleton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DCR_Fullview_Frag extends Fragment implements View.OnClickListener {
    View v;
    MySharedPref sp;
    String work_via;
    //for show like timeview
    TextView txt_selected_datee,txt_selected_strtfieldwork_att,txt_selected_strtfieldwork_locationn,
            txt_selected_citynamee,txt_selected_interiornamee,
            txt_selected_workingwithh,
            txt_selected_workingwithh_independentt,
            txt_selected_workingwithh_areamanger,txt_selected_workingwithh_marketingrepresentationn,
            txt_selected_workingwithh_regionalmangerr,
            txt_selected_workingwithh_salesmangerr,txt_selected_workingtypee,txt_strt_end_time_value,txt_date_report_error_msg,
            txt_end_time,txt_end_time_value,txt_work_strt_loc,txt_end_location,no_call_docter_availble,
            no_reminder_docter_availble,no_chemist_visit_docter_availble,no_doc_availble,txt_petrol_view;

    RelativeLayout rr_selected_date,rr_selected_strtfieldwork_at,rr_selected_strtfieldwork_location,
            rr_selected_cityname,rr_selected_interiorname,rr_main_workingwith,

    rr_selected_workingwith_independent,rr_selected_workingwith_areamanger,
            rr_selected_workingwith_regionalmanger,rr_selected_workingwith_salesmanger,
            rr_selected_workingtype,rr_selected_workingwith_marketingrepresentation,rr_main_full_descp,rr_todays_start_end_time,
            rr_end_time_layout,rr_work_strt_location,rr_end_work_location;

    //end of like timeline

    UserAdapter adapter;

    RecyclerView recyclerView_docters;
    RecyclerView recyclerView_reminder;
    RecyclerView recyclerView_chemist_visit;
    RecyclerView recyclerView_document_view;

    EditText edt_reminder,edt_telephone,edt_fwa,edt_sundary,edt_petrol,edt_printandsttnry,edt_remark;
    RelativeLayout rr_submit_dcr_checkout,full_layout_hidable,rr_slect_workvia,rr_document_show_lay;
    TextView txt_date_show,txt_call_hour,txt_title,txt_doc_calls,txt_docter_remind,txt_doc_chemist_visit,txt_error_msg,txt_value_workvia,
            txt_document_view;
    CustomSearch_Doctors_Adp customSearch_doctors_adp;
    CustomSearch_Chemist_Adp customSearch_chemist_adp;
    CustomDataset customDataset;
    //String Sharedpreferences Data
    String ldata,user_id4,token,today_date,company_name;
    boolean ischeckedpermission;
    ProgressBar loader;
    ArrayList<dcr_review_docter> arr_all__doctors_detail = new ArrayList<>();
    ArrayList<dcr_review_docter_reminder> arr_all__doctors_reminderr = new ArrayList<>();
    ArrayList<dcr_review_chemist_visited> arr_all__chemiss_visit= new ArrayList<>();
    ArrayList<dcr_review_expenses> arr_all__expenses= new ArrayList<>();
    ArrayList<String> arr_add_document= new ArrayList<>();
    Bundle b1;
    public static DCR_Fullview_Frag newInstance()
    {
        DCR_Fullview_Frag fragment = new DCR_Fullview_Frag();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.view_dcr, container, false);
        getBundleData2();

        // ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//
        getSaveddata();
        // ---------------------------- For Check Permission -------------------------------------------------------------------------------//
        checkForPermission();

        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();
        // ---------------------------- For  Bundle Data -------------------------------------------------------------------------------//
        getBundleData();


        return v;
    }
    // ---------------------------- For  Bundle Data -------------------------------------------------------------------------------//

    private void getBundleData2() {

        Bundle b1=this.getArguments();
        if (b1!=null)
        {
            today_date = b1.getString("today_date");
            System.out.println("Today Date###"+today_date);
            user_id4 = b1.getString("user_id");
        }
        else
        {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            today_date = date;
            //Set Text Title

        }


    }



    private void getBundleData() {

        b1=this.getArguments();
        if (b1!=null)
        {
             today_date = b1.getString("today_date");
             System.out.println("Today Date###"+today_date);
             txt_date_show.setText(today_date);
             rr_submit_dcr_checkout.setVisibility(View.GONE);
        }
        else
        {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            txt_date_show.setText(date);
            today_date = date;
            //Set Text Title
            txt_title.setText("Review & End Field Work");

        }

    }


    // ---------------------------- For Check Permission -------------------------------------------------------------------------------//
    private void checkForPermission() {

        //Check permission for Location
        int permissionCheckForReadcalender = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR);
        int permissionCheckForwritecalender = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR);


        if (permissionCheckForReadcalender != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForwritecalender != PackageManager.PERMISSION_GRANTED
                )
        {

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


        //Casting RecycleView for Search Doctor
        recyclerView_docters = (RecyclerView) v.findViewById(R.id.docter_detail);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_docters.setLayoutManager(linearLayoutManager);
        recyclerView_docters.setNestedScrollingEnabled(false);

        //Casting RecycleView for docter reminders
        recyclerView_reminder = (RecyclerView) v.findViewById(R.id.docter_reminder);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        recyclerView_reminder.setLayoutManager(linearLayoutManager2);
        recyclerView_reminder.setNestedScrollingEnabled(false);

        //Casting RecycleView for visited chemist
        recyclerView_chemist_visit = (RecyclerView) v.findViewById(R.id.chemistvisit);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext());
        recyclerView_chemist_visit.setLayoutManager(linearLayoutManager3);
        recyclerView_chemist_visit.setNestedScrollingEnabled(false);

        recyclerView_document_view = (RecyclerView) v.findViewById(R.id.document_list);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getContext());
        recyclerView_document_view.setLayoutManager(linearLayoutManager4);
        recyclerView_document_view.setNestedScrollingEnabled(false);

        rr_document_show_lay=(RelativeLayout)v.findViewById(R.id.rr_document_detail_lay);

        //Casting ProgressBar for loading
        loader=(ProgressBar)v.findViewById(R.id.progress23);
        //For  Search DoctorRecycle Create by these
        txt_date_show = (TextView)v.findViewById(R.id.txt_date);
        //txt_date_show.setText(String.valueOf(System.currentTimeMillis()));
        txt_call_hour = (TextView)v.findViewById(R.id.txt_calldurationdcr);

        //txt_date_show.setText(String.valueOf(System.currentTimeMillis()));
        txt_doc_calls = (TextView)v.findViewById(R.id.txt_docter_calls);
        //txt_date_show.setText(String.valueOf(System.currentTimeMillis()));
        txt_docter_remind = (TextView)v.findViewById(R.id.txt_docter_reminder);
        //txt_date_show.setText(String.valueOf(System.currentTimeMillis()));
        txt_doc_chemist_visit = (TextView)v.findViewById(R.id.txt_chemist_visited);
        txt_error_msg = (TextView)v.findViewById(R.id.error_for_dcr_report);

        no_call_docter_availble =(TextView) v.findViewById(R.id.no_docter_call_available);
        no_reminder_docter_availble =(TextView) v.findViewById(R.id.no_doc_reminder_available);
        no_chemist_visit_docter_availble =(TextView) v.findViewById(R.id.no_chemistvisit_available);
        no_doc_availble =(TextView) v.findViewById(R.id.no_doc_available);

        txt_petrol_view =(TextView) v.findViewById(R.id.txt_petrol);

        txt_document_view = (TextView)v.findViewById(R.id.txt_document);

        //all Edittext are initialized here
        edt_reminder = (EditText)v.findViewById(R.id.edt_reminderexpenses);
        edt_telephone = (EditText)v.findViewById(R.id.edt_telephone);
        edt_fwa = (EditText)v.findViewById(R.id.edt_fwa);
        edt_petrol = (EditText)v.findViewById(R.id.edt_petrol);

        edt_sundary = (EditText)v.findViewById(R.id.edt_sundary);
        edt_printandsttnry = (EditText)v.findViewById(R.id.edt_printingstationary);
        edt_remark = (EditText)v.findViewById(R.id.edt_remark);
        full_layout_hidable = (RelativeLayout)v.findViewById(R.id.full_lay_hide);
        full_layout_hidable.setVisibility(View.GONE);



        rr_submit_dcr_checkout = (RelativeLayout)v.findViewById(R.id.rr_submitdcr_checked_unchecked);
        rr_submit_dcr_checkout.setOnClickListener(this);
        callWebservicefor_getAllDCRReportData();

        // initialize data to show detail like timeline view

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
        txt_value_workvia=(TextView) v.findViewById(R.id.txt_value_workingvia);

        //Casting RelativeLayout for Start Field Working
        txt_selected_workingwithh =(TextView)v.findViewById(R.id.txt_selected_workingwithh);
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
        //Casting TextView for Start end time Representation
        txt_strt_end_time_value = (TextView)v.findViewById(R.id.start_end_time_value);

        rr_end_time_layout=(RelativeLayout)v.findViewById(R.id.end_time_relativelayout);
        txt_end_time = (TextView)v.findViewById(R.id.end_time);
        txt_end_time_value = (TextView)v.findViewById(R.id.end_time_value);

        rr_work_strt_location=(RelativeLayout)v.findViewById(R.id.rr_work_start_location);
        txt_work_strt_loc = (TextView)v.findViewById(R.id.work_strt_location);

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



    }
    // ---------------------------- WebService Call for Getall City-------------------------------------------------------------------------------//
    private void callWebservicefor_getAllDCRReportData() {


        if(Utils.isConnected(getActivity())) {
           //for get data like time line
            getData_ForSearchSuggestion(today_date);


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

                    if (error.equals("success")) {

                        Gson gson = new Gson();


                        String field_work_start_record = jObj.getString("field_work_start_record");
                        JSONObject jsonObject44=new JSONObject(field_work_start_record);
                        rr_main_workingwith.setVisibility(View.VISIBLE);


                        // txt_selected_datee.setText("Selected Date is: "+today_date);

                        String marketing_reperesentative=jsonObject44.getString("marketing_reperesentative");
                        System.out.println("Marketing Representative####"+marketing_reperesentative);

                        if(!marketing_reperesentative.equalsIgnoreCase(""))
                        {

                            JSONObject jsonObject33=new JSONObject(marketing_reperesentative);
                            String first_name=jsonObject33.getString("first_name");
                            String last_name=jsonObject33.getString("last_name");
                            String role_name=jsonObject33.getString("role_name");
                            rr_selected_workingwith_marketingrepresentation.setVisibility(View.VISIBLE);
                            txt_selected_workingwithh_marketingrepresentationn.setText(first_name+" "+last_name/*+","+role_name*/);
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
                            // rr_selected_workingwith_independent.setVisibility(View.VISIBLE);

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
                            txt_selected_citynamee.setText("City is: "+name);

                            //Set Visibility and Set Interior  name
                            rr_selected_interiorname.setVisibility(View.VISIBLE);
                            txt_selected_interiornamee.setText("Interior is: "+interior);*/

                            rr_selected_strtfieldwork_location.setVisibility(View.VISIBLE);
                            txt_selected_strtfieldwork_locationn.setText(working_at+" - "+interior);
                        }

                        String work_type=jsonObject44.getString("work_type");

                        rr_selected_workingtype.setVisibility(View.VISIBLE);
                        txt_selected_workingtypee.setText(work_type);

                        work_via=jsonObject44.getString("work_via");

                        if (!work_via.equalsIgnoreCase("null") && !work_via.equalsIgnoreCase(""))
                        {
                            rr_slect_workvia.setVisibility(View.VISIBLE);
                            txt_value_workvia.setText(work_via);
                        }

                        String work_strt_type = sp.getData(getActivity(),"dcr_work_strt_type","");
                        System.out.println("strt work type "+ working_at);

                        if (working_at.equalsIgnoreCase("interior") && !working_at.equalsIgnoreCase("null"))
                        {
                            if (work_via.equalsIgnoreCase("Car/Taxi")) {
                                txt_petrol_view.setText("Car/Taxi Expenses");
                            }
                            else
                            {
                                txt_petrol_view.setText("Petrol Expenses");
                            }
                        }
                        else
                        {
                            edt_petrol.setVisibility(View.GONE);
                            txt_petrol_view.setVisibility(View.GONE);

                        }



                        String start_lat=jsonObject44.getString("start_lat");
                        String start_long=jsonObject44.getString("start_long");

                        System.out.println("Start latitude is###"+start_lat);
                        System.out.println("Start longitude is###"+start_long);

                        try {
                            double dub_start_lat = Double.parseDouble(start_lat);
                            double dub_start_long = Double.parseDouble(start_long);

                            if (dub_start_lat != 0.0) {
                            String str_address = getAddressFromLatlong(dub_start_lat, dub_start_long);
                            if (!str_address.equalsIgnoreCase("")) {
                                rr_work_strt_location.setVisibility(View.VISIBLE);
                                txt_work_strt_loc.setText(str_address);
                            }
                        }
                        }
                        catch (Exception e)
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
                            if (dub_end_lat != 0.0) {
                                String str_address = getAddressFromLatlong(dub_end_lat, dub_end_long);
                                if (!str_address.equalsIgnoreCase("")) {
                                    rr_end_work_location.setVisibility(View.VISIBLE);
                                    txt_end_location.setText(str_address);
                                }
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }

                        String dcr_typee=jsonObject44.getString("dcr_type");
                        if (!dcr_typee.equalsIgnoreCase(""))
                        {
                            rr_selected_date.setVisibility(View.VISIBLE);
                            txt_selected_datee.setText(dcr_typee);
                        }

                        String start_time=jsonObject44.getString("start_time");
                        String end_time=jsonObject44.getString("end_time");

                        System.out.println("Start time is###"+start_time);
                        System.out.println("end time is###"+end_time);


                        if (!start_time.equalsIgnoreCase(""))
                        {
                            rr_todays_start_end_time.setVisibility(View.VISIBLE);
                            txt_strt_end_time_value.setText(start_time);
                        }
                        if (!end_time.equalsIgnoreCase(""))
                        {
                           rr_todays_start_end_time.setVisibility(View.VISIBLE);
                           rr_end_time_layout.setVisibility(View.VISIBLE);
                           txt_end_time_value.setText(end_time);
                        }





                        String errorMsg = jObj.getString("message");

                        if(getActivity()!=null) {
                         /*   txt_avail_pro.setVisibility(View.VISIBLE);
                              txt_avail_pro.setText("No products added yet.");*/
                            //  Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                        }

                        loader.setVisibility(View.GONE);
                        full_layout_hidable.setVisibility(View.VISIBLE);
                        //for get dcr data
                        getData_ForSearchSuggestion();

                        //rr_main_full_descp.setVisibility(View.VISIBLE);


                    } else {
                        rr_main_full_descp.setVisibility(View.GONE);

                        loader.setVisibility(View.GONE);

                        String errorMsg = jObj.getString("message");
                        if(getActivity()!=null) {

                            txt_error_msg.setVisibility(View.VISIBLE);
                            full_layout_hidable.setVisibility(View.GONE);
                            txt_error_msg.setText("For "+today_date+" the DCR Report is not submitted.");
                            //Toast.makeText(getActivity(), "For "+today_date+" the DCR Report is not submitted.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();

                    /*if(error.getMessage()!=null) {
                        JSONObject jsonObject22 = new JSONObject(error.getMessage());

                        String errorMsg = jsonObject22.getString("message");
                        //  JSONObject jsonObject33=new JSONObject(message);
                        //   String password=jsonObject33.getString("password");

                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                    }*/

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

                System.out.println("Search String###"+today_date);
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id4);
                params.put("today_date",today_date);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }

    // ---------------------------- For WebService Call for get Search Doctor Suggestion -------------------------------------------------------------------------------//
    private void getData_ForSearchSuggestion() {

        String url = "http://dailyreporting.in/"+company_name+"/api/all_performance/"+user_id4;
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        /*StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_FULL_DCRVIEW+"/"+user_id4,
                new com.android.volley.Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                            new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DCR Review", "review : " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    String user_field_work = jObj.getString("field_work");
                    if (user_field_work!=null)
                    {
                        txt_call_hour.setText(user_field_work+" Hrs");
                    }
                    if (error.equals("success")) {


                        loader.setVisibility(View.GONE);

                        JSONObject jsonObject = jObj.getJSONObject("result");
                        JSONArray jsonArray = jsonObject.getJSONArray("result_doctor");
                        System.out.println("docter array "+ jsonArray.length());
                        if (jsonArray.length()==0)
                        {
                            recyclerView_docters.setVisibility(View.GONE);
                            no_call_docter_availble.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String doc_name = jsonObject1.getString("doctor_name");
                            String call_duration = jsonObject1.getString("diff_calld");

                            String visit_type = jsonObject1.getString("visit");

                            String speciality = jsonObject1.getString("speciality");
                            String product1 = jsonObject1.getString("product1");
                            String product1_value = jsonObject1.getString("product1_value");
                            String product2 = jsonObject1.getString("product2");
                            String product2_value = jsonObject1.getString("product2_value");
                            String product3 = jsonObject1.getString("product3");
                            String product3_value = jsonObject1.getString("product3_value");
                            String gift = jsonObject1.getString("gift");
                            String gift_value = jsonObject1.getString("gift_value");
                            String city_name = jsonObject1.getString("city_name");

                            arr_all__doctors_detail.add(new dcr_review_docter(doc_name, speciality, product1, product1_value, product2
                                    , product2_value, product3, product3_value, gift, gift_value, call_duration,visit_type,city_name));

                            Log.d("doc nm", "review1 : " + arr_all__doctors_detail.size());
                            Log.d("DCR Review", "review1 : " + speciality);

                            customSearch_doctors_adp = new CustomSearch_Doctors_Adp(getActivity(), arr_all__doctors_detail);
                            Log.d("DCR Review", "adapter : ");
                            recyclerView_docters.setAdapter(customSearch_doctors_adp);
                            Log.d("doc nm", "docter detail size : " + arr_all__doctors_detail.size());

                            if (arr_all__doctors_detail.size() > 0) {
                                txt_doc_calls.setText("Doctor Calls " + "(" + arr_all__doctors_detail.size() + ")");
                            }
                            else {
                                recyclerView_docters.setVisibility(View.GONE);
                                no_call_docter_availble.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "No Doctor call was done.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        JSONArray jsonArray2 = jsonObject.getJSONArray("result_doctor_rem");
                        System.out.println("reminder array "+ jsonArray2.length());

                        if (jsonArray2.length()==0)
                        {
                            recyclerView_reminder.setVisibility(View.GONE);
                            no_reminder_docter_availble.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            JSONObject jsonObject1 = jsonArray2.getJSONObject(i);
                            String doctorRem_name = jsonObject1.getString("doctor_rem");
                            String specialiti = jsonObject1.getString("doctor_rem_speciality");
                            String rem_doc_gift = jsonObject1.getString("doctor_rem_gift");
                            String rem_gift_val_doc = jsonObject1.getString("doctor_rem_gift_val");

                            arr_all__doctors_reminderr.add(new dcr_review_docter_reminder(doctorRem_name, specialiti, rem_doc_gift,
                                    rem_gift_val_doc));

                            customDataset = new CustomDataset(getActivity(), arr_all__doctors_reminderr);
                            Log.d("DCR Review", "reminder adapter : ");
                            recyclerView_reminder.setAdapter(customDataset);


                            if (arr_all__doctors_reminderr.size() > 0) {
                                txt_docter_remind.setText("Docter Reminder " + "(" + arr_all__doctors_reminderr.size() + ")");
                            }

                        }
                        JSONArray jsonArray3 = jsonObject.getJSONArray("result_doctor_che_sto");
                        System.out.println("chemist array "+ jsonArray3.length());
                        if (jsonArray3.length()==0)
                        {
                            recyclerView_chemist_visit.setVisibility(View.GONE);
                            no_chemist_visit_docter_availble.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i < jsonArray3.length(); i++) {


                            JSONObject jsonObject1 = jsonArray3.getJSONObject(i);
                            String chemist_name = jsonObject1.getString("chemist_name");

                            String chemist_call_duration = jsonObject1.getString("diff_call_chemist");

                            String stockist1 = jsonObject1.getString("stockist1");
                            String stockist1_value = jsonObject1.getString("stockist1_value");
                            String stockist2 = jsonObject1.getString("stockist2");
                            String stockist2_value = jsonObject1.getString("stockist2_value");
                            String stockist3 = jsonObject1.getString("stockist3");
                            String stockist3_value = jsonObject1.getString("stockist3_value");

                            String pob_amount = jsonObject1.getString("pob");
                            String gift_name = jsonObject1.getString("gift");
                            String gift_value = jsonObject1.getString("gift_value");

                            arr_all__chemiss_visit.add(new dcr_review_chemist_visited(chemist_name, stockist1, stockist1_value, stockist2,
                                    stockist2_value, stockist3, stockist3_value, pob_amount, gift_name, gift_value,chemist_call_duration));

                            customSearch_chemist_adp = new CustomSearch_Chemist_Adp(getActivity(), arr_all__chemiss_visit);
                            Log.d("DCR Review", "chemist adapter : ");
                            recyclerView_chemist_visit.setAdapter(customSearch_chemist_adp);
                            if (arr_all__chemiss_visit.size() > 0) {
                                txt_doc_chemist_visit.setText("Chemist Visit " + "(" + arr_all__chemiss_visit.size() + ")");
                            }
                            else {
                                recyclerView_chemist_visit.setVisibility(View.GONE);
                                no_chemist_visit_docter_availble.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "No Chemist visit was done.", Toast.LENGTH_SHORT).show();
                            }

                        }

                        JSONArray jsonArray5 = jsonObject.getJSONArray("documents");
                        if (jsonArray5.length()==0)
                        {
                            recyclerView_document_view.setVisibility(View.GONE);
                            no_doc_availble.setVisibility(View.VISIBLE);
                        }

                        for (int i = 0; i < jsonArray5.length(); i++) {

                            System.out.println("data document ###" + jsonArray5.getString(i));
                            String name = jsonArray5.getString(i);
                            arr_add_document.add(name);

                            rr_document_show_lay.setVisibility(View.VISIBLE);
                            adapter = new  UserAdapter(getActivity(),arr_add_document);
                            recyclerView_document_view.setAdapter(adapter);

                            if (arr_add_document.size() > 0) {
                                txt_document_view.setText("Documents " + "(" + arr_add_document.size() + ")");
                            }
                            else {
                                recyclerView_document_view.setVisibility(View.GONE);
                                no_doc_availble.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "No Document's Available.", Toast.LENGTH_SHORT).show();
                            }

                        }

                       /* JSONArray jsonArray4 = jsonObject.getJSONArray("result_expense");
                        if (jsonArray4 != null) {
                        }
                        else {*/
                            JSONObject jsonObject1 = jsonObject.getJSONObject("result_expense");
                        try {
                            // String expense_reminder_name = jsonObject1.getString("expense_reminder");
                            String value_reminder = jsonObject1.getString("value_reminder");
                            // String expense_telephone_name = jsonObject1.getString("expense_telephone");
                            String value_telephone = jsonObject1.getString("value_telephone");
                            // String expense_expense_photocopy_name = jsonObject1.getString("expense_photocopy");
                            String value_expense_photocopy = jsonObject1.getString("value_photocopy");
                            /// String expense_value_fwa = jsonObject1.getString("expense_sundry");
                            String value_sundry = jsonObject1.getString("value_sundry");
                            //String expense_petrol = jsonObject1.getString("expense_petrol");
                            String value_petrol = jsonObject1.getString("value_petrol");

                            String car_taxi_expenses = jsonObject1.getString("value_car_taxi_expenses");
                            String value_fwa = jsonObject1.getString("value_fwa");
                            String remark = jsonObject1.getString("value_remark");

                            //arr_all__expenses.add(new dcr_review_expenses(expense_name,expense_value));
                            setdata(value_reminder, value_telephone, value_expense_photocopy, value_sundry, value_petrol, value_fwa, remark,car_taxi_expenses);


                            rr_main_full_descp.setVisibility(View.VISIBLE);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    else {
                        loader.setVisibility(View.GONE);

                        String errorMsg = jObj.getString("message");
                        if(getActivity()!=null) {
                            txt_error_msg.setVisibility(View.VISIBLE);
                            full_layout_hidable.setVisibility(View.GONE);
                            txt_error_msg.setText("For "+today_date+" the DCR Report is not submitted.");
                            //Toast.makeText(getActivity(), "For "+today_date+" the DCR Report is not submitted.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();

                    /*if(error.getMessage()!=null) {
                        JSONObject jsonObject22 = new JSONObject(error.getMessage());

                        String errorMsg = jsonObject22.getString("message");
                        //  JSONObject jsonObject33=new JSONObject(message);
                        //   String password=jsonObject33.getString("password");

                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                    }*/

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
                System.out.println("Search String###"+today_date);

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id4);
                params.put("today_date",today_date);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void setdata(String value_reminder, String value_telephone, String value_expense_photocopy, String sundry,
                         String valuePetrol, String value_fwa, String remark,String car_taxi_expense) {


        if (!value_fwa.equalsIgnoreCase(""))
        {
            edt_fwa.setText(value_fwa);
        }
        else
        {
            edt_fwa.setText("0");
        }

        if (!value_reminder.equalsIgnoreCase("null") && !value_reminder.equalsIgnoreCase(""))
        {
            edt_reminder.setText(value_reminder);
        }
        else
        {
            edt_reminder.setText("0");
        }

        if (!value_telephone.equalsIgnoreCase("null") && !value_telephone.equalsIgnoreCase(""))
        {
            edt_telephone.setText(value_telephone);
        }
        else
        {
            edt_telephone.setText("0");
        }

         if (!value_expense_photocopy.equalsIgnoreCase("null") && !value_expense_photocopy.equalsIgnoreCase(""))
         {
             edt_printandsttnry.setText(value_expense_photocopy);
         }
        else
        {
            edt_printandsttnry.setText("0");
        }



        if (!sundry.equalsIgnoreCase("null") && !sundry.equalsIgnoreCase(""))
                {
                    edt_sundary.setText(sundry);
                }
                else
                {
                    edt_sundary.setText("0");
                }



                if (!remark.equalsIgnoreCase("0"))
                {
                   edt_remark.setText(remark);
                }

               System.out.println("work via"+ work_via + car_taxi_expense);

              if (work_via.equalsIgnoreCase("Car/Taxi"))
              {
                  if (!car_taxi_expense.equalsIgnoreCase("") && !car_taxi_expense.equalsIgnoreCase("null"))
                  {
                      edt_petrol.setText(car_taxi_expense);
                  }
                  else
                  {
                      edt_petrol.setText("0");
                  }

              }
              else if (work_via.equalsIgnoreCase("Bike"))
              {
                  if (!valuePetrol.equalsIgnoreCase("null") && !valuePetrol.equalsIgnoreCase(""))
                  {
                      edt_petrol.setText(valuePetrol);
                  }
                  else
                  {
                      edt_petrol.setText("0");
                  }
              }

    }

    // ---------------------------- For WebService Call for get Search Doctor Suggestion -------------------------------------------------------------------------------//
    private void submit_todayDcrReport() {

        String url = "http://dailyreporting.in/"+company_name+"/api/submit_report";
        System.out.println("sout url"+ url);


        final String latitude = sp.getData(MySharedPref.KEY_Lat);
        final String longitude = sp.getData(MySharedPref.KEY_Long);
        System.out.println("lati# # "+latitude);
        System.out.println("long## "+longitude);

        loader.setVisibility(View.VISIBLE);
        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_DAY_DCR_REPORT,
                new com.android.volley.Response.Listener<String>() {*/
    StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DCR Review", "review : " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);
                        sp.getData(getActivity(), "work_time", "00:00:00");

                       /* getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFrame, new HomeFragments())
                                //.addToBackStack("0")
                                .commit();*/
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

                    }
                    else {
                        loader.setVisibility(View.GONE);

                        String errorMsg = jObj.getString("message");
                        if(getActivity()!=null) {

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
                    Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();

                    /*if(error.getMessage()!=null) {
                        JSONObject jsonObject22 = new JSONObject(error.getMessage());

                        String errorMsg = jsonObject22.getString("message");
                        //  JSONObject jsonObject33=new JSONObject(message);
                        //   String password=jsonObject33.getString("password");

                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                    }*/

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

                Map<String, String> params = new HashMap<String, String>();
                 params.put("user_id",user_id4);
                 params.put("end_lat",latitude);
                 params.put("end_long",longitude);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }


    // ---------------------------- For Get Stored Data -------------------------------------------------------------------------------//
    private void getSaveddata() {


        sp = new MySharedPref();

        company_name = sp.getData(getActivity(),"company_name","");
        System.out.println("company name  : "+company_name);

        ldata = sp.getData(getActivity(), "ldata", "null");
        Log.e("LdataHome", ldata);

        if (ldata != null && user_id4==null)

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

    @Override
    public void onClick(View v) {
        if(v==rr_submit_dcr_checkout)
        {
            if(Utils.isConnected(getActivity())) {
                showDialog();
            }
            else
            {
                Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog()
    {
        // initUi();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to submit and day end your field work? ")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        submit_todayDcrReport();
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


    // ---------------------------- Class for RecycleView for Search Doctor Adapter-------------------------------------------------------------------------------//
    private class CustomSearch_Doctors_Adp extends RecyclerView.Adapter<CustomSearch_Doctors_Adp.MyViewHolder> {

        Activity activity;

        private Context mContext;
        ArrayList<dcr_review_docter> arr_all_search_doctors;


        public CustomSearch_Doctors_Adp(Activity activity, ArrayList<dcr_review_docter> arr_all_search_doctors2) {

            this.activity = activity;

            this.arr_all_search_doctors = arr_all_search_doctors2;
            Log.d("DCR Review", "review343453 : ");

            System.out.println("List Size###" + arr_all_search_doctors.size());
        }


        @Override
        public CustomSearch_Doctors_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adpter_dcr_docter_detail, parent, false);

            return new CustomSearch_Doctors_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomSearch_Doctors_Adp.MyViewHolder holder, final int position) {


            String visit_typee = arr_all_search_doctors.get(position).getVisit();


            String p1 = arr_all_search_doctors.get(position).getProduct1();
            String p1_value = arr_all_search_doctors.get(position).getProduct1_value();
            String p2 = arr_all_search_doctors.get(position).getProduct2();
            String p2_value = arr_all_search_doctors.get(position).getProduct2_value();
            String p3 = arr_all_search_doctors.get(position).getProduct3();
            String p3_value = arr_all_search_doctors.get(position).getProduct3_value();
            String g1 = arr_all_search_doctors.get(position).getGift();
            String g1_value = arr_all_search_doctors.get(position).getGift_value();

            /*  Double dub_cinlat= Double.valueOf(arr_all_search_doctors.get(position).getDrCinLat());
            Double dub_cinlon= Double.valueOf(arr_all_search_doctors.get(position).getDrCinLon());

            String str_address=getAddressFromLatlong(dub_cinlat,dub_cinlon);
            System.out.println("Address Doctor###"+str_address);
            holder.txt_doc_address.setText(str_address);*/

          // String str_dr_cout_time= arr_all_search_doctors3.get(position).getDrCoutTime();
            //   holder.txt_doctor_time.setText(str_checkin_time+"-"+str_dr_cout_time);

            holder.txt_doctor_name.setText(arr_all_search_doctors.get(position).getDoc_name());
            holder.txt_call_durtion.setText(arr_all_search_doctors.get(position).getCall_duration());

           if (p1.equalsIgnoreCase("") && p1_value.equalsIgnoreCase("0"))
           {
              holder.relativeLayout_product_detail1.setVisibility(View.GONE);
           }
           else
           {
               holder.r_prodetail_nd_smdistributor.setVisibility(View.VISIBLE);
               holder.edt1.setText(arr_all_search_doctors.get(position).getProduct1());
               holder.edt1_qty.setText(arr_all_search_doctors.get(position).getProduct1_value());
           }

            if (p2.equalsIgnoreCase("") && p2_value.equalsIgnoreCase("0"))
            {
                holder.relay_product_detail2.setVisibility(View.GONE);

            }
            else
            {
                holder.r_prodetail_nd_smdistributor.setVisibility(View.VISIBLE);

                holder.edt2.setText(arr_all_search_doctors.get(position).getProduct2());
                holder.edt2_qty.setText(arr_all_search_doctors.get(position).getProduct2_value());
            }

            if (p3.equalsIgnoreCase("") && p3_value.equalsIgnoreCase("0"))
            {
                holder.relayLayout_product_detail3.setVisibility(View.GONE);

            }
            else
            {
                holder.r_prodetail_nd_smdistributor.setVisibility(View.VISIBLE);

                holder.edt3.setText(arr_all_search_doctors.get(position).getProduct3());
                holder.edt3_qty.setText(arr_all_search_doctors.get(position).getProduct3_value());
            }

            if (g1.equalsIgnoreCase("") && g1_value.equalsIgnoreCase("0"))
            {
                holder.rr_txt_giift.setVisibility(View.GONE);
                holder.rr_gift_lay.setVisibility(View.GONE);

            }
            else
            {
                holder.rr_txt_giift.setVisibility(View.VISIBLE);
                holder.rr_gift_lay.setVisibility(View.VISIBLE);

                holder.edt_gft1.setText(arr_all_search_doctors.get(position).getGift());
                holder.edt_edt_gift_qty.setText(arr_all_search_doctors.get(position).getGift_value());
            }
            /* LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            holder.recyclerView.setLayoutManager(layoutManager);
            CustomDataset customDataset = new CustomDataset(getActivity(),arr_all__doctors_detail);
            holder.recyclerView.setAdapter(customDataset);*/

            holder.arrow_down_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Search_Doctors_Submit_Fragment selectedFragment = Search_Doctors_Submit_Fragment.newInstance();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle b=new Bundle();
                    b.putString("my_String",txt_search_doctor.getText().toString());
                     selectedFragment.setArguments(b);
                    transaction.replace(R.id.contentFrame, selectedFragment);
                    transaction.addToBackStack("8");
                    transaction.commit();


                    txt_search_doctor.setText("");*/
                     // Toast.makeText(activity, position, Toast.LENGTH_SHORT).show();
                      holder.rr_current_loc_descp2.setVisibility(View.VISIBLE);
                      if(!arr_all__doctors_detail.get(position).getCity().equalsIgnoreCase("null"))
                      {
                          holder.txt_doc_address.setText(arr_all__doctors_detail.get(position).getCity());
                      }
                      holder.arrow_down_click.setVisibility(View.GONE);
                      holder.arrow_up_click.setVisibility(View.VISIBLE);
                      holder.txt_doctor_special.setText(arr_all__doctors_detail.get(position).getSpeciality());
                }
            });

            holder.arrow_up_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Search_Doctors_Submit_Fragment selectedFragment = Search_Doctors_Submit_Fragment.newInstance();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle b=new Bundle();
                    b.putString("my_String",txt_search_doctor.getText().toString());
                     selectedFragment.setArguments(b);
                    transaction.replace(R.id.contentFrame, selectedFragment);
                    transaction.addToBackStack("8");
                    transaction.commit();

                    txt_search_doctor.setText("");*/
                    // Toast.makeText(activity, position, Toast.LENGTH_SHORT).show();
                    holder.rr_current_loc_descp2.setVisibility(View.GONE);
                    holder.arrow_down_click.setVisibility(View.VISIBLE);
                    holder.arrow_up_click.setVisibility(View.GONE);
                    holder.txt_doctor_special.setText(arr_all__doctors_detail.get(position).getSpeciality());
                }
            });
        }

        @Override
        public int getItemCount() {
            return arr_all_search_doctors.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_doctor_special, txt_doctor_name,txt_doc_address,txt_call_durtion;
            RelativeLayout arrow_down_click,arrow_up_click,rr_current_loc_descp2,rr_full_view;
            EditText edt1,edt1_qty,edt2,edt2_qty,edt3,edt3_qty,edt_gft1,edt_edt_gift_qty;
            RelativeLayout relativeLayout_product_detail1,relay_product_detail2,relayLayout_product_detail3,r_prodetail_nd_smdistributor,
            rr_gift_lay,rr_txt_giift;
            public MyViewHolder(View itemView) {
                super(itemView);
                txt_call_durtion = (TextView)itemView.findViewById(R.id.calls_duratn);
                txt_doc_address = (TextView) itemView.findViewById(R.id.txt_doc_address);
                txt_doctor_special = (TextView) itemView.findViewById(R.id.doc_speciality);
                txt_doctor_name = (TextView) itemView.findViewById(R.id.rr_doc_name);
                arrow_down_click = (RelativeLayout) itemView.findViewById(R.id.Arrow_down);
                arrow_up_click = (RelativeLayout) itemView.findViewById(R.id.Arrow_up);

                rr_current_loc_descp2 = (RelativeLayout) itemView.findViewById(R.id.rr_full_descp_timedate2);
                rr_full_view = (RelativeLayout) itemView.findViewById(R.id.rr_dcr_txt_doc_detail);

                edt1= (EditText) itemView.findViewById(R.id.edt_product);
                edt1_qty= (EditText) itemView.findViewById(R.id.edt_product_qty);
                edt2= (EditText) itemView.findViewById(R.id.edt_product2);
                edt2_qty= (EditText) itemView.findViewById(R.id.edt_product_qty2);
                edt3= (EditText) itemView.findViewById(R.id.edt_product3);
                edt3_qty= (EditText) itemView.findViewById(R.id.edt_product_qty3);

                edt_gft1= (EditText) itemView.findViewById(R.id.gift_name);
                edt_edt_gift_qty= (EditText) itemView.findViewById(R.id.gift_product_qty);

                rr_gift_lay = (RelativeLayout) itemView.findViewById(R.id.rr_gift_relay);
                rr_txt_giift = (RelativeLayout) itemView.findViewById(R.id.rr_giftss);

                relativeLayout_product_detail1 = (RelativeLayout) itemView.findViewById(R.id.rr_InboxDetailRV1);
                relay_product_detail2 = (RelativeLayout) itemView.findViewById(R.id.rr_InboxDetailRV2);
                relayLayout_product_detail3 = (RelativeLayout) itemView.findViewById(R.id.rr_InboxDetailRV3);
                r_prodetail_nd_smdistributor= (RelativeLayout) itemView.findViewById(R.id.rr_prodetail_nd_smdistributor);
            }


        }

    }
    //---------------------------------- Class for RecycleView for set sample data-------------------------------------------------------------
    public class CustomDataset extends  RecyclerView.Adapter<CustomDataset.ViewHolder>{

        Activity activity;
        ArrayList<dcr_review_docter_reminder> arr_all_search_doctors_reminder;

        public CustomDataset(FragmentActivity activity, ArrayList<dcr_review_docter_reminder> arr_all_search_doctors_reminder)
        {
            this.activity = activity;
            this.arr_all_search_doctors_reminder = arr_all_search_doctors_reminder;
            Log.d("DCR Review", "review343453 : ");
            System.out.println("List Size###" + arr_all_search_doctors_reminder.size());
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.adpter_dcr_docter_reminder,null,false);

            return new CustomDataset.ViewHolder(view);
        }



        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.txt_dcr_name.setText(arr_all_search_doctors_reminder.get(position).getDoctorRem_name());
            holder.txt_dcr_dr_dpvlity.setText(arr_all_search_doctors_reminder.get(position).getSpecialiti());
            holder.edt_product.setText(arr_all_search_doctors_reminder.get(position).getRem_doc_gift());

            if (!arr_all_search_doctors_reminder.get(position).getRem_gift_val_doc().equalsIgnoreCase("0"))
            {
                holder.edt_product_qty.setText(arr_all_search_doctors_reminder.get(position).getRem_gift_val_doc());
            }
            else
            {
                holder.edt_product_qty.setText("");
            }
        }

        @Override
        public int getItemCount() {
            return arr_all_search_doctors_reminder.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            EditText edt_product,edt_product_qty;
            TextView txt_dcr_name,txt_dcr_dr_dpvlity;
            public ViewHolder(View itemView) {
                super(itemView);
                txt_dcr_name = (TextView)itemView.findViewById(R.id.rr_doc_name);
                txt_dcr_dr_dpvlity = (TextView)itemView.findViewById(R.id.doc_specialiti);

                edt_product= (EditText) itemView.findViewById(R.id.edt_product);
                edt_product_qty= (EditText) itemView.findViewById(R.id.edt_product_qty);
            }
        }
    }
        // ---------------------------- Class for RecycleView for Search Doctor Adapter-------------------------------------------------------------------------------//
        private class CustomSearch_Chemist_Adp extends RecyclerView.Adapter<CustomSearch_Chemist_Adp.MyViewHolder> {

            Activity activity;

            private Context mContext;

            List<dcr_review_chemist_visited> arSearchlist;

            public CustomSearch_Chemist_Adp(Activity activity, List<dcr_review_chemist_visited> arr_all_search_doctors2) {

                this.activity = activity;
                this.arSearchlist = arr_all_search_doctors2;
                System.out.println("List Size###" + arSearchlist.size());
            }


            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adpter_dcr_chemist_detail, parent, false);

                return new CustomSearch_Chemist_Adp.MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(final MyViewHolder holder, final int position) {

                holder.txt_doctor_namee.setText(arSearchlist.get(position).getChemist_name());
                holder.txt_call_durtn.setText(arSearchlist.get(position).getCall_duration());

               if (arSearchlist.get(position).getStockist1().equalsIgnoreCase(""))
               {
                   holder.r_stockiest_1.setVisibility(View.GONE);
               }
               else
               {
                   holder.edt_apn_product1.setText(arSearchlist.get(position).getStockist1());
                   holder.edt_spn_product_qty1.setText(arSearchlist.get(position).getStockist1_value());

               }
                if (arSearchlist.get(position).getStockist2().equalsIgnoreCase(""))
                {
                    holder.r_stockiest_2.setVisibility(View.GONE);
                }
                else
                {
                    holder.edt_apn_product2.setText(arSearchlist.get(position).getStockist2());
                    holder.edt_spn_product_qty2.setText(arSearchlist.get(position).getStockist2_value());

                }
                if (arSearchlist.get(position).getStockist3().equalsIgnoreCase(""))
                {
                    holder.r_stockiest_3.setVisibility(View.GONE);
                }
                else
                {
                    holder.edt_apn_product3.setText(arSearchlist.get(position).getStockist3());
                    holder.edt_spn_product_qty3.setText(arSearchlist.get(position).getStockist3_value());

                }
                if (arSearchlist.get(position).getGift_name().equalsIgnoreCase("") &&
                        arSearchlist.get(position).getGift_value()
                        .equalsIgnoreCase("0"))
                {
                    holder.r_gift.setVisibility(View.GONE);
                }
                else
                {
                    holder.edt_apn_product.setText(arSearchlist.get(position).getGift_name());
                    holder.edt_spn_product_qty.setText(arSearchlist.get(position).getGift_value());

                }
                if (arSearchlist.get(position).getPob_amount().equalsIgnoreCase("0"))
                {
                    holder.r_pobamount.setVisibility(View.GONE);
                }
                else
                {
                    holder.edt_apn_product_amount.setText(arSearchlist.get(position).getPob_amount());
                }


                holder.arrow_down_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    /*Search_Doctors_Submit_Fragment selectedFragment = Search_Doctors_Submit_Fragment.newInstance();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle b=new Bundle();
                    b.putString("my_String",txt_search_doctor.getText().toString());
                     selectedFragment.setArguments(b);
                    transaction.replace(R.id.contentFrame, selectedFragment);
                    transaction.addToBackStack("8");
                    transaction.commit();

                    txt_search_doctor.setText("");*/
                        // Toast.makeText(activity, position, Toast.LENGTH_SHORT).show();
                        holder.rr_current_loc_descp2.setVisibility(View.VISIBLE);
                        holder.arrow_down_click.setVisibility(View.GONE);
                        holder.arrow_up_click.setVisibility(View.VISIBLE);
                    }
                });

                holder.arrow_up_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    /*Search_Doctors_Submit_Fragment selectedFragment = Search_Doctors_Submit_Fragment.newInstance();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle b=new Bundle();
                    b.putString("my_String",txt_search_doctor.getText().toString());
                     selectedFragment.setArguments(b);
                    transaction.replace(R.id.contentFrame, selectedFragment);
                    transaction.addToBackStack("8");
                    transaction.commit();

                    txt_search_doctor.setText("");*/
                        // Toast.makeText(activity, position, Toast.LENGTH_SHORT).show();
                        holder.rr_current_loc_descp2.setVisibility(View.GONE);
                        holder.arrow_down_click.setVisibility(View.VISIBLE);
                        holder.arrow_up_click.setVisibility(View.GONE);
                    }
                });



                /*                holder.txt_doctor_namee.setText(arSearchlist.get(position).getChemist_name());

              try {
                  String upperString_str_interior_name = str_interior_name.substring(0, 1).toUpperCase() + str_interior_name.substring(1);
                  holder.txt_doctor_namee.setText(upperString_str_interior_name);
              }
              catch (StringIndexOutOfBoundsException e)
              {

              }*/


               /* Double dub_cinlat= Double.valueOf(arr_all_search_doctors3.get(position).getCheckinLat());
                Double dub_cinlon= Double.valueOf(arr_all_search_doctors3.get(position).getCheckinLong());

                String str_address=getAddressFromLatlong(dub_cinlat,dub_cinlon);
                System.out.println("Address Stockiest###"+str_address);

                holder.txt_doc_address.setText(str_address);*/

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
                return arSearchlist.size();
            }

            public class MyViewHolder extends RecyclerView.ViewHolder {
                TextView txt_doctor_namee, txt_doctor_time,txt_doc_address,txt_call_durtn;
                RelativeLayout arrow_down_click,arrow_up_click,rr_current_loc_descp2;
                EditText edt_apn_product,edt_spn_product_qty,edt_apn_product_amount,
                        edt_apn_product1,edt_spn_product_qty1,
                        edt_apn_product2,edt_spn_product_qty2,
                        edt_apn_product3,edt_spn_product_qty3;
                RelativeLayout r_stockiest_1,r_stockiest_2,r_stockiest_3,r_gift,r_pobamount;

                public MyViewHolder(View itemView) {
                    super(itemView);
                    txt_doctor_namee = (TextView) itemView.findViewById(R.id.rr_chemist_name);
                    txt_call_durtn = (TextView) itemView.findViewById(R.id.calls_duratn);

                    rr_current_loc_descp2 = (RelativeLayout) itemView.findViewById(R.id.rr_full_descp_timedate2);

                    edt_apn_product = (EditText)itemView.findViewById(R.id.spn_product);
                    edt_spn_product_qty = (EditText)itemView.findViewById(R.id.edt_spn_product_qty);
                    edt_apn_product_amount = (EditText)itemView.findViewById(R.id.spn_product_pobamount);

                    edt_apn_product1 = (EditText)itemView.findViewById(R.id.spn_stockiest1);
                    edt_spn_product_qty1= (EditText)itemView.findViewById(R.id.edt_stockiest1_qty);

                    edt_apn_product2 = (EditText)itemView.findViewById(R.id.spn_stockiest2);
                    edt_spn_product_qty2= (EditText)itemView.findViewById(R.id.edt_stockiest2_qty);

                    edt_apn_product3 = (EditText)itemView.findViewById(R.id.spn_stockiest3);
                    edt_spn_product_qty3= (EditText)itemView.findViewById(R.id.edt_stockiest3_qty);

                    arrow_down_click = (RelativeLayout) itemView.findViewById(R.id.Arrow_down);
                    arrow_up_click = (RelativeLayout) itemView.findViewById(R.id.Arrow_up);

                    r_stockiest_1 = (RelativeLayout) itemView.findViewById(R.id.rr_stockiest1);
                    r_stockiest_2 = (RelativeLayout) itemView.findViewById(R.id.rr_stockiest2);
                    r_stockiest_3 = (RelativeLayout) itemView.findViewById(R.id.rr_stockiest3);
                    r_gift = (RelativeLayout) itemView.findViewById(R.id.rr_gifts);
                    r_pobamount = (RelativeLayout) itemView.findViewById(R.id.rr_pobamount);


                }


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


        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }
//------------------------------------------------------adapter

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

        Context context;
        ArrayList<String> arrayList;

        public UserAdapter(FragmentActivity activity, ArrayList<String> arrayList)
        {
            this.context = activity;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public  ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.search_doctor_adp,null,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.txt_current_loc_descp1.setText(arrayList.get(position).toString());
           if (b1!=null)
           {
               holder.deletetxt.setVisibility(View.GONE);

           }
           else
           {
               holder.deletetxt.setText("Remove");
               holder.deletetxt.setTextColor(getResources().getColor(R.color.red));
           }


            holder.deletetxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   String doc_name = arrayList.get(position).toString();
                   deleteDocument(position,doc_name);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_current_loc_descp1,deletetxt;


            public ViewHolder(View itemView) {
                super(itemView);

                txt_current_loc_descp1 = (TextView)itemView.findViewById(R.id.txt_current_loc_descp);
                deletetxt = (TextView)itemView.findViewById(R.id.txt_profession);

            }
        }
    }

    // ---------------------------- For WebService Call for delete document from database -------------------------------------------------------------------------------//
    private void deleteDocument(final int position, final String doc_name) {

        final String table_insert_id = sp.getData(getActivity(),"dcr_table_insert_id","");

        String url = "http://dailyreporting.in/"+company_name+"/api/delete_document";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        /*StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_FULL_DCRVIEW+"/"+user_id4,
                new com.android.volley.Response.Listener<String>() {*/
        //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DCR submit expenses", "expenses : " + response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String error = jObj.getString("status");
                            String errorMsg = jObj.getString("message");

                            if (error.equals("success")) {
                                loader.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                                arr_add_document.remove(position);
                                adapter.notifyDataSetChanged();

                                if (arr_add_document.size() > 0) {
                                    txt_document_view.setText("Documents " + "(" + arr_add_document.size() + ")");
                                }
                                else {
                                    recyclerView_document_view.setVisibility(View.GONE);
                                    no_doc_availble.setVisibility(View.VISIBLE);
                                    txt_document_view.setText("Documents");

                                    //Toast.makeText(getActivity(), "No Document's Available.", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else {
                                loader.setVisibility(View.GONE);
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
                Log.e("AllCityListAccState", "All City List AccState Error: " + error.getMessage());
                try {
                    Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
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
                System.out.println("Search id###"+user_id4);
                Map<String, String> params = new HashMap<String, String>();
                System.out.println("sout doc_name "+ doc_name);
                System.out.println("sout table_insert_id "+ table_insert_id);

                params.put("dcr_id",table_insert_id);
                params.put("document_name",doc_name);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }




}
