package com.example.candid_20.dcrapp.fragments.home_menu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.candid_20.dcrapp.R;

import com.example.candid_20.dcrapp.bean.for_city_list.CityList_Bean;
import com.example.candid_20.dcrapp.bean.for_city_list.CityList_LxDetails_Bean;
import com.example.candid_20.dcrapp.bean.for_interior.InteriorList_Bean;
import com.example.candid_20.dcrapp.bean.for_interior.InteriorList_LxDetails_Bean;
import com.example.candid_20.dcrapp.bean.for_mr.all_am;
import com.example.candid_20.dcrapp.bean.for_mr.all_mr;
import com.example.candid_20.dcrapp.bean.for_mr.all_rm;
import com.example.candid_20.dcrapp.bean.for_state_list.StateList_Bean;
import com.example.candid_20.dcrapp.bean.for_state_list.StateList_LxDetails_Bean;
import com.example.candid_20.dcrapp.constant.Utils;
import com.example.candid_20.dcrapp.other.GPSTracker;
import com.example.candid_20.dcrapp.storage.MySharedPref;
import com.example.candid_20.dcrapp.volleyconnector.AppSingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

public class StartFieldWork extends Fragment implements View.OnClickListener {

    View v;
    //for local for an=rea manager select city
    RelativeLayout rr_local_select_teritorry_fullview,rr_local_select_region_view,rr_show_suggestion_region_view,rr_local_select_city_view,rr_show_suggestion_city_view,
                            rr_local_select_interior_view,rr_show_suggestion_interior_view;
    ProgressDialog progressDialog;

    EditText edt_select_region,edt_select_city_local,edt_select_interior_local;
    TextView txt_show_region_error,txt_show_city_error,txt_show_interior_error;
    RecyclerView recyclerView_for_show_region_local,recyclerView_for_show_cities_local,recyclerView_for_show_interior_local;

    RelativeLayout rr_local_checked_unchecked,rr_interior_checked_unchecked;
    ImageView img_local_checked_icon,img_local_unchecked_icon,img_car_checked_icon,img_car_unchecked_icon
            ,img_taxi_checked_icon,img_taxi_unchecked_icon,
            img_interior_checked_icon,img_interior_unchecked_icon;
    ImageView img_independent_unchecked_icon,img_independent_checked_icon,img_areamanger_checked_icon,
        img_areamanger_unchecked_icon,img_regionalmanger_checked_icon,img_regionalmanger_unchecked_icon,
        img_salesmanger_checked_icon,img_salesmanger_unchecked_icon

        ,img_independent_interior_checked_icon,img_independent_interior_unchecked_icon,
        img_areamanger_interior_checked_icon,img_areamanger_interior_unchecked_icon,
        img_regionalmanger_interior_checked_icon,img_regionalmanger_interior_unchecked_icon,
        img_salesmanger_interior_checked_icon,img_salesmanger_interior_unchecked_icon;

    TextView txt_independent,txt_areamanger,txt_regionalmanger,txt_salesmanger,txt_working_with_error,txt_working_with_error_interior,
            txt_working_with_errorr_interior;
    String sales_manager_user_id,regional_manager_user_id,area_manager_user_id;

    Spinner spinner_sales_manager,spinner_sales_manager_interior,spinner_area_manager,spinner_area_manager_interior,spinner_regional_manager,
            spinner_regionalmanger_interior;
    SpinnerAdapter adapter;

    RelativeLayout rr_local,rr_interior;
    TextView txt_current_loc_descp,Txt_current_loc_descp_interior,txt_current_loc_error,txt_independent_interior,
            txt_areamanger_interior,txt_regionalmanger_interior,txt_salesmanger_interior;
    //------------------------------------ Initialise boolean-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    boolean isGPSEnabled = false;
    Button btn_start_local,btn_start_interior;
    //------------------------------------ Initialise double-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    double currentlat,currentlong;
    // Sharedpreferences Class
    MySharedPref sp;
    //String Sharedpreferences Data
    String ldata,user_id4,token,empcode,role_nam;

    //ProgressBar for Loader
    ProgressBar loader,loader2;

    //RecycleView for Interior
    RecyclerView InboxDetailRV,InboxDetailRVv,InboxDetailRVvv;

    //String for Local
    String str_city_local_name,str_state_local_name,str_working_with_local_type="independent",
        str_workingwith_independent="",str_workingwith_area="",str_workingwith_regional="",
        str_workingwith_sales="";
  //String for interior
    String str_working_with_interior_type="independent"
    ,str_workingwith_independent_interior="",str_workingwith_area_interior="",str_workingwith_regional_interior="",
    str_workingwith_sales_interior="";

    //if login with area manager
    String  str_marketing_presentative_id,
            str_area_manger_id="",
            str_regional_manger_id="";

     //List Bean for State
     StateList_Bean stateList_bean;
     //List Bean for City
     CityList_Bean cityList_bean;

    //List Bean for Interior
    InteriorList_Bean interiorList_bean;

//Adapter Class for territory
    CustomStart_Field_Work_Interior_Slect_Territory customStart_field_work_interior_slect_territory;

    //Adapter Class for City
    CustomStart_Field_Work_Interior_Slect_City customStart_field_work_interior_slect_city;

    //Adapter Class for interior
    CustomStart_Field_Work_Interior_Slect_Interiror customStart_field_work_interior_slect_interiror;
    //RelativeLayout for territory,City
    RelativeLayout rr_territory_InboxDetailRV,rr_city_InboxDetailRVv,rr_interior_InboxDetailRVvv;

    //EditText for Select Territory,City,Interior
    EditText edt_selct_territory,edt_selct_city,edt_selct_interior;

    String str_state_id,str_city_id,str_interiror_id,str_local_interior="local", str_car_bike="Bike",role_id,company_name;
    //For Territory Error
    TextView txt_territory_error,txt_city_error,txt_title;
    RelativeLayout rr_selct_territory;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.start_field_work_home1, container, false);
        // ---------------------------- For Stored Data -------------------------------------------------------------------------------//
        getSaveddata();
        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();

        // ---------------------------- For Get Current Location -------------------------------------------------------------------------------//
        //getLocation();

        return v;
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
                role_nam = jsonObject.getString("role_name");
                empcode = jsonObject.getString("employee_code");
                role_id=jsonObject.getString("role_id");

                Log.e("RoleId is@@@",role_id);
                Log.e("Id is@@@",user_id4);
                Log.e("Id is@@@",role_nam);
                Log.e("Id is@@@",empcode);
                System.out.println("Id is***" + user_id4);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
    private void initUi() {
        // ---------------------------- Casting Element -------------------------------------------------------------------------------//
        //Casting RelativeLayout local for Check and uncheck
        rr_local_checked_unchecked=(RelativeLayout)v.findViewById(R.id.rr_local_checked_unchecked);
//Casting TextView for Title
        txt_title=(TextView)getActivity().findViewById(R.id.txt_title);

        //Set Text
        txt_title.setText("Start Field Work");

        //select territory for local for area manager
        rr_local_select_teritorry_fullview = (RelativeLayout)v.findViewById(R.id.rr_local_selectterritory);

        //for edittext where search region
        rr_local_select_region_view = (RelativeLayout)v.findViewById(R.id.rr_selct_region_local);
        edt_select_region = (EditText)v.findViewById(R.id.edt_selct_region);

        //for recycle view show suggestion of region
        rr_show_suggestion_region_view = (RelativeLayout)v.findViewById(R.id.rr_region_relative_layout);
        recyclerView_for_show_region_local = (RecyclerView) v.findViewById(R.id.show_region_suggestion_local);
        txt_show_region_error = (TextView)v.findViewById(R.id.txt_region_error);

        //for edittext where search city
        rr_local_select_city_view = (RelativeLayout)v.findViewById(R.id.rr_selct_city_local);
        edt_select_city_local = (EditText)v.findViewById(R.id.edt_selct_city_local);


        //for recycle view show suggestion of cities
        rr_show_suggestion_city_view = (RelativeLayout)v.findViewById(R.id.rr_InboxDetailRVv_show_suggestCity_local);
        recyclerView_for_show_cities_local = (RecyclerView)v.findViewById(R.id.show_city_suggestion_local);
        txt_show_city_error = (TextView)v.findViewById(R.id.txt_city_error_local);


        //for edittext where search city
        rr_local_select_interior_view = (RelativeLayout)v.findViewById(R.id.rr_select_interior_local);
        edt_select_interior_local = (EditText)v.findViewById(R.id.edt_selct_interior_local);

        //for recycle view show suggestion of cities
        rr_show_suggestion_interior_view = (RelativeLayout)v.findViewById(R.id.rr_InboxDetailRVvv_local);
        recyclerView_for_show_interior_local = (RecyclerView)v.findViewById(R.id.InboxDetailRVvv_local);
        txt_show_interior_error = (TextView)v.findViewById(R.id.txt_working_with_error_interior_local);


        if (role_id.equalsIgnoreCase("5"))
        {
            rr_local_select_teritorry_fullview.setVisibility(View.GONE);
        }

        //Casting RelativeLayout interior for Check and uncheck
        rr_interior_checked_unchecked=(RelativeLayout)v.findViewById(R.id.rr_interior_checked_unchecked);

        //For EditText Visible for Territory
        edt_selct_territory=(EditText) v.findViewById(R.id.edt_selct_territory);
        //For TextView for Territory Error
        txt_territory_error=(TextView)v.findViewById(R.id.txt_territory_error);
        //For EditText Visible for City
        edt_selct_city=(EditText) v.findViewById(R.id.edt_selct_city);
        //For TextView for City Error
        txt_city_error=(TextView)v.findViewById(R.id.txt_city_error);
        //For TextView for Working With Local Error
        txt_working_with_error=(TextView)v.findViewById(R.id.txt_working_with_error);
        //For RelativeLayout Visible for Territory
        rr_selct_territory=(RelativeLayout)v.findViewById(R.id.rr_selct_territory);
        //For EditText Visible for Interior
        edt_selct_interior=(EditText) v.findViewById(R.id.edt_selct_interior);


        //Casting ImageView Local Checked for Local
        img_local_checked_icon=(ImageView)v.findViewById(R.id.img_local_checked_icon);
        //Casting ImageView Local UnChecked
        img_local_unchecked_icon=(ImageView)v.findViewById(R.id.img_local_unchecked_icon);

        //Casting ImageView Interior Checked for Local
        img_interior_checked_icon=(ImageView)v.findViewById(R.id.img_interior_checked_icon);
        //Casting ImageView Local UnChecked
        img_interior_unchecked_icon=(ImageView)v.findViewById(R.id.img_interior_unchecked_icon);

        //Casting ImageView Independent Checked for Local
        img_independent_checked_icon=(ImageView)v.findViewById(R.id.img_independent_checked_icon);
        //Casting ImageView Independent UnChecked
        img_independent_unchecked_icon=(ImageView)v.findViewById(R.id.img_independent_unchecked_icon);

        //Casting ImageView AreaManger Checked for Local
        img_areamanger_checked_icon=(ImageView)v.findViewById(R.id.img_areamanger_checked_icon);
        //Casting ImageView AreaManger UnChecked
        img_areamanger_unchecked_icon=(ImageView)v.findViewById(R.id.img_areamanger_unchecked_icon);

        //Casting ImageView Regional Manager Checked for Local
        img_regionalmanger_checked_icon=(ImageView)v.findViewById(R.id.img_regionalmanger_checked_icon);
        //Casting ImageView Regional Manager UnChecked for Local
        img_regionalmanger_unchecked_icon=(ImageView)v.findViewById(R.id.img_regionalmanger_unchecked_icon);

        //Casting ImageView Salesmanger Checked for Local
        img_salesmanger_checked_icon=(ImageView)v.findViewById(R.id.img_salesmanger_checked_icon);
        //Casting ImageView Salesmanger UnChecked for Local
        img_salesmanger_unchecked_icon=(ImageView)v.findViewById(R.id.img_salesmanger_unchecked_icon);
        //Casting Button Start Local for Local
        btn_start_local=(Button)v.findViewById(R.id.btn_start_local);

        //Casting TextView for Current Location
        txt_current_loc_descp=(TextView)v.findViewById(R.id.txt_current_loc_descp);
        Txt_current_loc_descp_interior =(TextView)v.findViewById(R.id.txt_current_Interior_descp);
        //Casting TextView for Current Location Error
        txt_current_loc_error=(TextView)v.findViewById(R.id.txt_current_loc_error);

        //Casting TextView for  Independent Local
        txt_independent=(TextView)v.findViewById(R.id.txt_independent);
        //Casting TextView for  Salesmanger Local
        txt_salesmanger=(TextView)v.findViewById(R.id.txt_salesmanger);
        spinner_sales_manager = (Spinner)v.findViewById(R.id.spn_salesmanger);

        //Casting TextView for  RegionalManger Local
        txt_regionalmanger=(TextView)v.findViewById(R.id.txt_regionalmanger);
        spinner_regional_manager = (Spinner)v.findViewById(R.id.spn_regionalmanger);


        //Casting TextView for  Salesmanger Local
        txt_areamanger=(TextView)v.findViewById(R.id.txt_areamanger);
        spinner_area_manager = (Spinner)v.findViewById(R.id.spn_areamanger);


        //Casting ProgressBar for loading Local
        loader=(ProgressBar)v.findViewById(R.id.loader);
        //Casting ProgressBar for loading interior
        loader2=(ProgressBar)v.findViewById(R.id.progress_for_interior);



        /*** ---------------------------- For Interior -------------------------------------------------------------------------------***/
//For TextView for Independent Interior
txt_independent_interior=(TextView)v.findViewById(R.id.txt_working_with_error_interior);
//For TextView for AreaManger Interior
 txt_areamanger_interior=(TextView)v.findViewById(R.id.txt_areamanger_interior);
 spinner_area_manager_interior = (Spinner)v.findViewById(R.id.spn_areamanger_interior);

//For TextView for SalesManManger Interior
        txt_salesmanger_interior=(TextView)v.findViewById(R.id.txt_salesmanger_interior);
        spinner_sales_manager_interior = (Spinner)v.findViewById(R.id.spn_salesmanger_interior);
//For TextView for RegionalManger Interior
        txt_regionalmanger_interior=(TextView)v.findViewById(R.id.txt_regionalmanger_interior);
        spinner_regionalmanger_interior = (Spinner)v.findViewById(R.id.spn_regionalmanger_interior);

        //For TextView for Working With Interior Error Select
        txt_working_with_error_interior=(TextView)v.findViewById(R.id.txt_working_with_error_interior);
        //For TextView for Working With Interior Error
        txt_working_with_errorr_interior=(TextView)v.findViewById(R.id.txt_working_with_errorr_interior);

        //Casting ImageView Independent Checked for Interior
        img_independent_interior_checked_icon=(ImageView)v.findViewById(R.id.img_independent_interior_checked_icon);
        //Casting ImageView Independent UnChecked for Interior
        img_independent_interior_unchecked_icon=(ImageView)v.findViewById(R.id.img_independent_interior_unchecked_icon);

        //Casting ImageView AreaManger Checked for Interior
        img_areamanger_interior_checked_icon=(ImageView)v.findViewById(R.id.img_areamanger_interior_checked_icon);
        //Casting ImageView AreaManger UnChecked for Interior
        img_areamanger_interior_unchecked_icon=(ImageView)v.findViewById(R.id.img_areamanger_interior_unchecked_icon);

        //Casting ImageView Regional Manager Checked for Interior
        img_regionalmanger_interior_checked_icon=(ImageView)v.findViewById(R.id.img_regionalmanger_interior_checked_icon);
        //Casting ImageView Regional Manager UnChecked for Interior
        img_regionalmanger_interior_unchecked_icon=(ImageView)v.findViewById(R.id.img_regionalmanger_interior_unchecked_icon);

        //Casting ImageView Salesmanger Checked for Interior
        img_salesmanger_interior_checked_icon=(ImageView)v.findViewById(R.id.img_salesmanger_interior_checked_icon);
        //Casting ImageView Salesmanger UnChecked for Interior
        img_salesmanger_interior_unchecked_icon =(ImageView)v.findViewById(R.id.img_salesmanger_interior_unchecked_icon);
        //Casting Button Start for Interior
         btn_start_interior=(Button)v.findViewById(R.id.btn_start_interior);

        //Casting RelativeLayout Local Visibility
         rr_local=(RelativeLayout)v.findViewById(R.id.rr_local);
        //Casting RelativeLayout Interior Visibility
         rr_interior=(RelativeLayout)v.findViewById(R.id.rr_interior);

        //Casting ImageView Local Checked for interior
        img_car_checked_icon=(ImageView)v.findViewById(R.id.img_car_checked_icon);
        //Casting ImageView Local UnChecked
        img_car_unchecked_icon=(ImageView)v.findViewById(R.id.img_car_unchecked_icon);

        //Casting ImageView Interior Checked for interior
        img_taxi_checked_icon=(ImageView)v.findViewById(R.id.img_taxi_checked_icon);
        //Casting ImageView Local UnChecked
        img_taxi_unchecked_icon=(ImageView)v.findViewById(R.id.img_taxi_unchecked_icon);


        //Casting RecycleView for Search Territory
         InboxDetailRV = (RecyclerView) v.findViewById(R.id.InboxDetailRV);

        //Casting RecycleView for Search Interior
        InboxDetailRVv = (RecyclerView) v.findViewById(R.id.InboxDetailRVv);

        //Casting RecycleView for Search Interior
        InboxDetailRVvv = (RecyclerView) v.findViewById(R.id.InboxDetailRVvv);

        //For Relative Visible for Territory
        rr_territory_InboxDetailRV=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRV);
//For Relative Visible for City
        rr_city_InboxDetailRVv=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRVv);

        //For Relative Visible for Interior
        rr_interior_InboxDetailRVvv=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRVvv);

        //For  State Recycle Create by these
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        InboxDetailRV.setLayoutManager(mLayoutManager2);
        InboxDetailRV.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRV.setItemAnimator(new DefaultItemAnimator());

        //For  City Recycle Create by these
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity());
        InboxDetailRVv.setLayoutManager(mLayoutManager3);
        InboxDetailRVv.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRVv.setItemAnimator(new DefaultItemAnimator());



        //For  Interior Recycle Create by these
        RecyclerView.LayoutManager mLayoutManager4 = new LinearLayoutManager(getActivity());
        InboxDetailRVvv.setLayoutManager(mLayoutManager4);
        InboxDetailRVvv.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRVvv.setItemAnimator(new DefaultItemAnimator());


        //for local region  recycle view for area manager
        RecyclerView.LayoutManager mLayoutManager7 = new LinearLayoutManager(getActivity());
        recyclerView_for_show_region_local.setLayoutManager(mLayoutManager7);
        recyclerView_for_show_region_local.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        recyclerView_for_show_region_local.setItemAnimator(new DefaultItemAnimator());



        //for local city and interior recycle view for area manager
        RecyclerView.LayoutManager mLayoutManager5 = new LinearLayoutManager(getActivity());
        recyclerView_for_show_cities_local.setLayoutManager(mLayoutManager5);
        recyclerView_for_show_cities_local.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        recyclerView_for_show_cities_local.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager mLayoutManager6 = new LinearLayoutManager(getActivity());
        recyclerView_for_show_interior_local.setLayoutManager(mLayoutManager6);
        recyclerView_for_show_interior_local.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        recyclerView_for_show_interior_local.setItemAnimator(new DefaultItemAnimator());

        // ---------------------------- Set OnClickListner -------------------------------------------------------------------------------//

        //ImageView Local Checked onClicklistner
        img_local_checked_icon.setOnClickListener(this);
        //ImageView Local UnChecked onClicklistner
        img_local_unchecked_icon.setOnClickListener(this);

        //ImageView Interior Checked onClicklistner
        img_interior_checked_icon.setOnClickListener(this);
        //ImageView Interior UnChecked onClicklistner
        img_interior_unchecked_icon.setOnClickListener(this);



        //ImageView interior Checked onClicklistner
        img_car_checked_icon.setOnClickListener(this);
        //ImageView interior UnChecked onClicklistner
        img_car_unchecked_icon.setOnClickListener(this);

        //ImageView Interior Checked onClicklistner
        img_taxi_checked_icon.setOnClickListener(this);
        //ImageView Interior UnChecked onClicklistner
        img_taxi_unchecked_icon.setOnClickListener(this);

        //****  For  local Checked Uncheck****//

        //ImageView independent UnChecked onClicklistner
        img_independent_unchecked_icon.setOnClickListener(this);
        //ImageView AreaManger UnChecked onClicklistner
        img_areamanger_unchecked_icon.setOnClickListener(this);
        //ImageView RegionalManger UnChecked onClicklistner
        img_regionalmanger_unchecked_icon.setOnClickListener(this);
        //ImageView SalesManger UnChecked onClicklistner
        img_salesmanger_unchecked_icon.setOnClickListener(this);



        //ImageView independent  Checked onClicklistner
        img_independent_checked_icon.setOnClickListener(this);
        //ImageView AreaManger  Checked onClicklistner
        img_areamanger_checked_icon.setOnClickListener(this);
        //ImageView RegionalManger Checked onClicklistner
        img_regionalmanger_checked_icon.setOnClickListener(this);
        //ImageView SalesManger Checked onClicklistner
        img_salesmanger_checked_icon.setOnClickListener(this);

        //****  For  interiror Checked and uncheck****//
        //ImageView independent Interior UnChecked onClicklistner
        img_independent_interior_unchecked_icon.setOnClickListener(this);
        //ImageView AreaManger Interior UnChecked onClicklistner
        img_areamanger_interior_unchecked_icon.setOnClickListener(this);
        //ImageView RegionalManger Interior UnChecked onClicklistner
        img_regionalmanger_interior_unchecked_icon.setOnClickListener(this);
        //ImageView SalesManger Interior UnChecked onClicklistner
        img_salesmanger_interior_unchecked_icon.setOnClickListener(this);

        //ImageView independent Interior Checked onClicklistner
        img_independent_interior_checked_icon.setOnClickListener(this);
        //ImageView AreaManger Interior Checked onClicklistner
        img_areamanger_interior_checked_icon.setOnClickListener(this);
        //ImageView RegionalManger Interior Checked onClicklistner
        img_regionalmanger_interior_checked_icon.setOnClickListener(this);
        //ImageView SalesManger Interior Checked onClicklistner
        img_salesmanger_interior_checked_icon.setOnClickListener(this);


       //Button Start Local onClicklistner
        btn_start_local.setOnClickListener(this);
        //Button Start Interior onClicklistner
        btn_start_interior.setOnClickListener(this);


        //Call WebService
        callWebservicefor_WorkingwithUserRecord();

        //Call WebService for Get All Territory
        callWebservicefor_getAllTerritory();
        // ---------------------------- Set OnTextChangeListner -------------------------------------------------------------------------------//

        //For Territory
        edt_selct_territory.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                if (s.length() > 0) {

                    txt_territory_error.setVisibility(View.GONE);
                    System.out.println("In City State_id###" + str_state_id);

                    if (stateList_bean != null) {
                        System.out.println("stateList_bean###" + stateList_bean.getResult().size());

                        if (stateList_bean.getResult().size() >= 0) {

                            rr_territory_InboxDetailRV.setVisibility(View.VISIBLE);
                            InboxDetailRV.setVisibility(View.VISIBLE);


                            if(role_id.equalsIgnoreCase("3")) {

                                //  callWebservicefor_getAllCityAcctoState(str_state_id);
                                InboxDetailRV.setAdapter(customStart_field_work_interior_slect_territory);
                                customStart_field_work_interior_slect_territory.filter(s.toString());
                            }



                            if(role_id.equalsIgnoreCase("2")) {
                                InboxDetailRV.setAdapter(customStart_field_work_interior_slect_territory);

                                //  callWebservicefor_getAllCityAcctoState(str_state_id);
                            //    InboxDetailRV.setAdapter(customStart_field_work_interior_slect_territory);
                                customStart_field_work_interior_slect_territory.filter(s.toString());
                            }



                            //  customList1.notifyDataSetChanged();

                        }


                        // TODO Auto-generated method stub
                    }


                }
                else {

                    sp.saveData(getActivity(),"selected_city_id","");

                    rr_territory_InboxDetailRV.setVisibility(View.GONE);

                    InboxDetailRV.setVisibility(View.GONE);
                    //edt_selct_city.setText("");
                }

            }
        });

if(str_state_id!=null) {
    if (str_state_id.equalsIgnoreCase("")) {
        edt_selct_city.setFocusable(false);
    } else {
        edt_selct_city.setFocusable(true);
        }
}

    //For Territory
        edt_selct_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                if (s.length() > 0) {

                       txt_city_error.setVisibility(View.GONE);

                           System.out.println("In City State_id###" + str_state_id);
                                if (cityList_bean != null) {

                                    if (cityList_bean.getResult().size() >= 0) {

                                        rr_city_InboxDetailRVv.setVisibility(View.VISIBLE);
                                        InboxDetailRVv.setVisibility(View.VISIBLE);


                                        if(role_id.equalsIgnoreCase("4")) {

                                          //  callWebservicefor_getAllCityAcctoState(str_state_id);
                                            customStart_field_work_interior_slect_city.filter(s.toString());
                                            InboxDetailRVv.setAdapter(customStart_field_work_interior_slect_city);
                                        }


                                        if(role_id.equalsIgnoreCase("3")) {

                                            //  callWebservicefor_getAllCityAcctoState(str_state_id);
                                            InboxDetailRVv.setAdapter(customStart_field_work_interior_slect_city);
                                            customStart_field_work_interior_slect_city.filter(s.toString());
                                        }

                                        if(role_id.equalsIgnoreCase("2")) {

                                            //  callWebservicefor_getAllCityAcctoState(str_state_id);
                                            customStart_field_work_interior_slect_city.filter(s.toString());
                                            InboxDetailRVv.setAdapter(customStart_field_work_interior_slect_city);
                                        }
                                        //  customList1.notifyDataSetChanged();

                                    }


                                    // TODO Auto-generated method stub
                                }


                    }
                else {
                    sp.saveData(getActivity(),"selected_city_id","");

                    rr_city_InboxDetailRVv.setVisibility(View.GONE);

                    InboxDetailRVv.setVisibility(View.GONE);
                    //edt_selct_city.setText("");
                }

            }
        });

        //For Territory
        edt_selct_interior.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                if (s.length() > 0) {
                 //   if(edt_selct_interior.getText().length()>0) {

                            System.out.println("In City State_id###" + str_state_id);
                                if (interiorList_bean != null) {

                                    if (interiorList_bean.getResult().size() > 0) {

                                        rr_interior_InboxDetailRVvv.setVisibility(View.VISIBLE);
                                        InboxDetailRVvv.setVisibility(View.VISIBLE);


if(role_id.equalsIgnoreCase("5"))
{

    customStart_field_work_interior_slect_interiror.filter(s.toString());
    InboxDetailRVvv.setAdapter(customStart_field_work_interior_slect_interiror);

}
if(role_id.equalsIgnoreCase("4")) {

//    InboxDetailRVvv.setAdapter(customStart_field_work_interior_slect_interiror);
    customStart_field_work_interior_slect_interiror.filter(s.toString());
    InboxDetailRVvv.setAdapter(customStart_field_work_interior_slect_interiror);

}

                                        if(role_id.equalsIgnoreCase("3")) {

                                            //InboxDetailRVvv.setAdapter(customStart_field_work_interior_slect_interiror);
                                            customStart_field_work_interior_slect_interiror.filter(s.toString());
                                            InboxDetailRVvv.setAdapter(customStart_field_work_interior_slect_interiror);

                                        }

                                        if(role_id.equalsIgnoreCase("2")) {
                                            customStart_field_work_interior_slect_interiror.filter(s.toString());
                                            InboxDetailRVvv.setAdapter(customStart_field_work_interior_slect_interiror);

                                        }

                                        //  customList1.notifyDataSetChanged();

                                //    }


                                    // TODO Auto-generated method stub
                                }

                        if(interiorList_bean.getResult().size() == 0)
                        {
                            System.out.println("Interior Size Equal###"+interiorList_bean.getResult().size());

                            //    callWebservicefor_getAllInteriorAcctoCity("");
                            //Filter Data
                            customStart_field_work_interior_slect_interiror.filter(s.toString());

                        }
                            }



                        }


                else {
                    rr_interior_InboxDetailRVvv.setVisibility(View.GONE);

                    InboxDetailRVvv.setVisibility(View.GONE);
                    //edt_selct_city.setText("");
                }

            }
        });


    /*------------------------------------12453547576587468795678957976957908690587068----------------------------------------------------------------*/
    /*------------------------------------12453547576587468795678957976957908690587068----------------------------------------------------------------*/
    /*------------------------------------12453547576587468795678957976957908690587068----------------------------------------------------------------*/
  //for local area manager select city and interior


        //For Territory
        edt_select_region.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                if (s.length() > 0) {

                    System.out.println("In City State_id###" + str_state_id);

                    if (stateList_bean != null) {

                        System.out.println("statelist bean###" + stateList_bean.getResult().size());

                        if (stateList_bean.getResult().size() >= 0) {

                            rr_show_suggestion_region_view.setVisibility(View.VISIBLE);
                            recyclerView_for_show_region_local.setVisibility(View.VISIBLE);


                            if(role_id.equalsIgnoreCase("3")) {

                                //  callWebservicefor_getAllCityAcctoState(str_state_id);
                                recyclerView_for_show_region_local.setAdapter(customStart_field_work_interior_slect_territory);
                                customStart_field_work_interior_slect_territory.filter(s.toString());
                               // customStart_field_work_interior_slect_territory.notifyDataSetChanged();

                            }



                            if(role_id.equalsIgnoreCase("2")) {
                                recyclerView_for_show_region_local.setAdapter(customStart_field_work_interior_slect_territory);

                                //  callWebservicefor_getAllCityAcctoState(str_state_id);
                                //    InboxDetailRV.setAdapter(customStart_field_work_interior_slect_territory);
                                customStart_field_work_interior_slect_territory.filter(s.toString());
                            }



                            //  customList1.notifyDataSetChanged();

                        }


                        // TODO Auto-generated method stub
                    }


                }
                else {

                    rr_show_suggestion_region_view.setVisibility(View.GONE);
                    recyclerView_for_show_region_local.setVisibility(View.GONE);
                    //edt_selct_city.setText("");
                }

            }
        });



        if(str_state_id!=null) {
            if (str_state_id.equalsIgnoreCase("")) {
                edt_select_city_local.setFocusable(false);
            } else {
                edt_select_city_local.setFocusable(true);
            }
        }





        edt_select_city_local.addTextChangedListener(new TextWatcher() {
        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub


        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1,
        int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
        int arg3) {
            if (s.length() > 0) {

                txt_show_city_error.setVisibility(View.GONE);

                System.out.println("In City State_id###" + str_state_id);
                if (cityList_bean != null) {

                    if (cityList_bean.getResult().size() >= 0) {

                        rr_show_suggestion_city_view.setVisibility(View.VISIBLE);
                        recyclerView_for_show_cities_local.setVisibility(View.VISIBLE);


                        if(role_id.equalsIgnoreCase("4")) {

                            //  callWebservicefor_getAllCityAcctoState(str_state_id);
                            customStart_field_work_interior_slect_city.filter(s.toString());
                            recyclerView_for_show_cities_local.setAdapter(customStart_field_work_interior_slect_city);
                        }


                        if(role_id.equalsIgnoreCase("3")) {

                            //  callWebservicefor_getAllCityAcctoState(str_state_id);
                            recyclerView_for_show_cities_local.setAdapter(customStart_field_work_interior_slect_city);
                            customStart_field_work_interior_slect_city.filter(s.toString());
                        }

                        if(role_id.equalsIgnoreCase("2")) {

                            //  callWebservicefor_getAllCityAcctoState(str_state_id);
                            customStart_field_work_interior_slect_city.filter(s.toString());
                            recyclerView_for_show_cities_local.setAdapter(customStart_field_work_interior_slect_city);
                        }
                        //  customList1.notifyDataSetChanged();

                    }


                    // TODO Auto-generated method stub
                }


            }
            else {
                rr_show_suggestion_city_view.setVisibility(View.GONE);
                recyclerView_for_show_cities_local.setVisibility(View.GONE);
                //edt_selct_city.setText("");
            }

        }
    });

    //For Territory
        edt_select_interior_local.addTextChangedListener(new TextWatcher() {
        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub


        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1,
        int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
        int arg3) {
            if (s.length() > 0) {
                //   if(edt_selct_interior.getText().length()>0) {

                if (interiorList_bean != null) {

                    if (interiorList_bean.getResult().size() > 0) {

                        rr_show_suggestion_interior_view.setVisibility(View.VISIBLE);
                        recyclerView_for_show_interior_local.setVisibility(View.VISIBLE);


                        if(role_id.equalsIgnoreCase("5"))
                        {

                            customStart_field_work_interior_slect_interiror.filter(s.toString());
                            recyclerView_for_show_interior_local.setAdapter(customStart_field_work_interior_slect_interiror);

                        }
                        if(role_id.equalsIgnoreCase("4")) {

                           //    InboxDetailRVvv.setAdapter(customStart_field_work_interior_slect_interiror);
                            customStart_field_work_interior_slect_interiror.filter(s.toString());
                            recyclerView_for_show_interior_local.setAdapter(customStart_field_work_interior_slect_interiror);

                        }

                        if(role_id.equalsIgnoreCase("3")) {

                            //InboxDetailRVvv.setAdapter(customStart_field_work_interior_slect_interiror);
                            customStart_field_work_interior_slect_interiror.filter(s.toString());
                            recyclerView_for_show_interior_local.setAdapter(customStart_field_work_interior_slect_interiror);

                        }

                        if(role_id.equalsIgnoreCase("2")) {
                            customStart_field_work_interior_slect_interiror.filter(s.toString());
                            recyclerView_for_show_interior_local.setAdapter(customStart_field_work_interior_slect_interiror);

                        }

                        //  customList1.notifyDataSetChanged();

                        //    }


                        // TODO Auto-generated method stub
                    }

                    if(interiorList_bean.getResult().size() == 0)
                    {
                        System.out.println("Interior Size Equal###"+interiorList_bean.getResult().size());

                        //    callWebservicefor_getAllInteriorAcctoCity("");
                        //Filter Data
                        customStart_field_work_interior_slect_interiror.filter(s.toString());

                    }
                }



            }


            else {
                rr_show_suggestion_interior_view.setVisibility(View.GONE);
                recyclerView_for_show_interior_local.setVisibility(View.GONE);
                //edt_selct_city.setText("");
            }

        }
    });

    }

    // ---------------------------- For  WebService Call Method for Get Workingwith user record Data-------------------------------------------------------------------------------//
    private void callWebservicefor_WorkingwithUserRecord() {


        if(Utils.isConnected(getActivity())) {

        getData_WorkingwithUserRecord(user_id4);
    }
        else
    {
        Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
    }
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

    // ---------------------------- WebService Call for Getall Territory-------------------------------------------------------------------------------//
    private void callWebservicefor_getAllTerritory() {


        if(Utils.isConnected(getActivity())) {

            getData_ForTerritory(user_id4);
        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }

    // ---------------------------- WebService Call for Getall City-------------------------------------------------------------------------------//
    private void callWebservicefor_getAllCityAcctoState(String state_id2) {


        if(Utils.isConnected(getActivity())) {

            getData_ForCity(state_id2);
        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }

    // ---------------------------- WebService Call for Getall City-------------------------------------------------------------------------------//
    private void callWebservicefor_Start(final String field_area4,final String user_id4,final String location_region4,
                                         final String location_city4,final String work_type4
            ,final String interior_id4,
                                         final String independent4,final String am_id4,final String mr_id4,final String rm_id4,
                                         final  String sm_id4,final String ip_location4,
                                         final String start_lat4,final String start_long4) {


        if(Utils.isConnected(getActivity())) {

            start_Work_local(field_area4,user_id4,location_region4,location_city4,work_type4,interior_id4,
                    independent4,am_id4,mr_id4,rm_id4,sm_id4,ip_location4,start_lat4,start_long4);

        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }


    // ---------------------------- WebService Call for Getall City-------------------------------------------------------------------------------//
    private void callWebservicefor_StartInterior(final String field_area4,final String user_id4,final String location_region4,
                                         final String location_city4,final String work_type4
            ,final String interior_id4,
                                         final String independent4,final String am_id4,final String mr_id4,final String rm_id4,
                                         final  String sm_id4,final String ip_location4,
                                         final String start_lat4,final String start_long4) {


        if(Utils.isConnected(getActivity())) {

            start_Work_interior(field_area4,user_id4,location_region4,location_city4,work_type4,interior_id4,
                    independent4,am_id4,mr_id4,rm_id4,sm_id4,ip_location4,start_lat4,start_long4);
        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }

    // ---------------------------- WebService Call for Getall City-------------------------------------------------------------------------------//
    private void callWebservicefor_getAllInteriorAcctoCity(String city_id2) {

       if (getActivity()!=null)
       {
           if(Utils.isConnected(getActivity())) {

               getData_ForInterior(city_id2);
           }
           else
           {
               Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
           }
       }

    }




    // ---------------------------- For WebService Call for Get Workingwith user record Data-------------------------------------------------------------------------------//
    private void getData_ForTerritory(final String user_id4) {

        String url = "http://dailyreporting.in/"+company_name+"/api/user_assign_state";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_USERASSIGN_STATE,
                new com.android.volley.Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                            new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("All Sate List", "All State List response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);

                        Gson gson = new Gson();

                        stateList_bean=gson.fromJson(response,StateList_Bean.class);
                        System.out.println("List Size:"+stateList_bean.getResult().size());
                        /*System.out.println("Base URl Old^^^"+str_base_url);
                        String str_new_base_url=str_base_url.replace("candid-15-pc","192.168.1.2");
                        System.out.println("Base URl New^^^"+str_new_base_url);*/

                        if(stateList_bean.getResult().size()>0)
                        {
                            //   btn_add_products.setVisibility(View.VISIBLE);
                            System.out.println("state List Size:"+stateList_bean.getResult().size());
                            customStart_field_work_interior_slect_territory =
                                    new CustomStart_Field_Work_Interior_Slect_Territory(getActivity(), stateList_bean.getResult());

                             //   InboxDetailRV.setAdapter(customStart_field_work_interior_slect_territory);
                          //  customStart_field_work_interior_slect_territory.notifyDataSetChanged();



                            if(role_id.equalsIgnoreCase("5")) {
                                for (int i = 0; i < stateList_bean.getResult().size(); i++) {
                                    str_state_id = stateList_bean.getResult().get(i).getId();
                                    edt_selct_territory.setText(stateList_bean.getResult().get(i).getName());
                                    edt_selct_territory.setFocusable(false);
                                    edt_selct_territory.setEnabled(false);
                                }
                              if (str_state_id!=null)
                              {
                                  callWebservicefor_getAllCityAcctoState(str_state_id);
                              }

                                // callWebservicefor_getAllCityAcctoState(user_id4);
                            }


                            if(role_id.equalsIgnoreCase("4")) {
                                for (int i = 0; i < stateList_bean.getResult().size(); i++) {
                                    str_state_id = stateList_bean.getResult().get(i).getId();

                                    edt_select_region.setText(stateList_bean.getResult().get(i).getName());
                                    edt_select_region.setEnabled(false);
                                    edt_select_region.setFocusable(false);

                                    edt_selct_territory.setText(stateList_bean.getResult().get(i).getName());
                                    edt_selct_territory.setVisibility(View.VISIBLE);
                                    edt_selct_territory.setEnabled(false);
                                    edt_selct_territory.setFocusable(false);


                                }
                                callWebservicefor_getAllCityAcctoState(str_state_id);

                                // callWebservicefor_getAllCityAcctoState(user_id4);
                            }
                            if(role_id.equalsIgnoreCase("3")) {

                                InboxDetailRV.setAdapter(customStart_field_work_interior_slect_territory);

                                //recyclerView_for_show_region_local.setAdapter();
                                // callWebservicefor_getAllCityAcctoState(user_id4);
                            }
                            if(role_id.equalsIgnoreCase("2")) {

                                InboxDetailRV.setAdapter(customStart_field_work_interior_slect_territory);
                                // callWebservicefor_getAllCityAcctoState(user_id4);
                            }


                        }
                        String errorMsg = jObj.getString("message");
                        if(getActivity()!=null) {
                         /*   txt_avail_pro.setVisibility(View.VISIBLE);
                            txt_avail_pro.setText("No products added yet.");*/
                            //  Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                        }




                    } else {
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
                Log.e("State", "State Error: " + error.getMessage());

                try {

                    Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();

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
      //    params.put("Authorization","Bearer "+token);

                /*params.put("Authorization","Basic YWRtaW46MTIzNA==");
                params.put("Content-Type","application/x-www-form-urlencoded");*/

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

    // ---------------------------- For WebService Call for get City-------------------------------------------------------------------------------//
    private void getData_ForCity(final String stateid4) {

        String url = "http://dailyreporting.in/"+company_name+"/api/user_assign_city";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        /*StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_USERASSIGN_CITY,
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
                        loader.setVisibility(View.GONE);

                        Gson gson = new Gson();

                        cityList_bean=gson.fromJson(response,CityList_Bean.class);
                        System.out.println("List Size City:"+cityList_bean.getResult().size());
                        /*System.out.println("Base URl Old^^^"+str_base_url);
                        String str_new_base_url=str_base_url.replace("candid-15-pc","192.168.1.2");
                        System.out.println("Base URl New^^^"+str_new_base_url);*/

                        if(cityList_bean.getResult().size()>0)
                        {
                            //   btn_add_products.setVisibility(View.VISIBLE);

                            System.out.println("city List Size:"+cityList_bean.getResult().size());


                            customStart_field_work_interior_slect_city = new CustomStart_Field_Work_Interior_Slect_City(getActivity(),
                                    cityList_bean.getResult());
                         //   InboxDetailRVv.setAdapter(customStart_field_work_interior_slect_city);
                         //   customStart_field_work_interior_slect_city.notifyDataSetChanged();

                            if(role_id.equalsIgnoreCase("5")) {
                                for (int i = 0; i < cityList_bean.getResult().size(); i++) {
                                    str_city_id = cityList_bean.getResult().get(i).getId();

                                    System.out.println("City id###" + str_city_id);
                                    System.out.println("City id###" + str_local_interior);

                                    edt_selct_city.setText(cityList_bean.getResult().get(0).getName());
                                    callWebservicefor_getAllInteriorAcctoCity(cityList_bean.getResult().get(i).getId());

                                    sp.saveData(getActivity(),"selected_city_id",str_city_id);
                                    sp.saveData(getActivity(),"selected_city_id_local",str_city_id);


                                }

                                if(role_id.equalsIgnoreCase("4"))

                                {

                                    edt_selct_city.setFocusable(true);
                                    edt_selct_city.setClickable(true);

                                    InboxDetailRVv.setAdapter(customStart_field_work_interior_slect_city);


                                }

                                if(role_id.equalsIgnoreCase("3"))

                                {

                                    edt_selct_city.setFocusable(true);
                                    edt_selct_city.setClickable(true);

                                    InboxDetailRVv.setAdapter(customStart_field_work_interior_slect_city);

                                }
                                if(role_id.equalsIgnoreCase("2"))

                                {

                                    edt_selct_city.setFocusable(true);
                                    edt_selct_city.setClickable(true);

                                    InboxDetailRVv.setAdapter(customStart_field_work_interior_slect_city);


                                }
                            }
                        }
                        String errorMsg = jObj.getString("message");
                        if(getActivity()!=null) {
                         /*   txt_avail_pro.setVisibility(View.VISIBLE);
                            txt_avail_pro.setText("No products added yet.");*/
                            //  Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                        }




                    } else {
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
                    /*JSONObject jsonObject22=new JSONObject(error.getMessage());

                    String errorMsg=jsonObject22.getString("message");*/
                    //  JSONObject jsonObject33=new JSONObject(message);
                    //   String password=jsonObject33.getString("password");

                    Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();

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
                 Map<String, String> params = new HashMap<String, String>();
                 params.put("state_id",stateid4);
                 params.put("user_id",user_id4);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }
    // ---------------------------- For WebService Call for get -------------------------------------------------------------------------------//
    private void getData_ForInterior(final String cityid4) {

        String url = "http://dailyreporting.in/"+company_name+"/api/all_interior";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_CITY_ACCTO_CITY,
                new com.android.volley.Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("All interior List", "All interior List AccStatet response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);

                        Gson gson = new Gson();

                        interiorList_bean=gson.fromJson(response,InteriorList_Bean.class);
                        System.out.println("List Size interior:"+interiorList_bean.getResult().size());
                        /*System.out.println("Base URl Old^^^"+str_base_url);
                        String str_new_base_url=str_base_url.replace("candid-15-pc","192.168.1.2");
                        System.out.println("Base URl New^^^"+str_new_base_url);*/

                        if(interiorList_bean.getResult().size()>0)
                        {

                            //   btn_add_products.setVisibility(View.VISIBLE);
                            System.out.println("List Size:"+interiorList_bean.getResult().size());
                            customStart_field_work_interior_slect_interiror = new CustomStart_Field_Work_Interior_Slect_Interiror(getActivity(), interiorList_bean.getResult());

                           if(role_id.equalsIgnoreCase("5"))
                           {

                               InboxDetailRVvv.setAdapter(customStart_field_work_interior_slect_interiror);
                           }

                           if(role_id.equalsIgnoreCase("4"))
                           {
                               InboxDetailRVvv.setAdapter(customStart_field_work_interior_slect_interiror);

                           }

                            if(role_id.equalsIgnoreCase("3"))
                            {
                                InboxDetailRVvv.setAdapter(customStart_field_work_interior_slect_interiror);

                            }
                            if(role_id.equalsIgnoreCase("2"))
                            {
                                InboxDetailRVvv.setAdapter(customStart_field_work_interior_slect_interiror);

                            }

                            //   InboxDetailRVv.setAdapter(customStart_field_work_interior_slect_city);
                            //   customStart_field_work_interior_slect_city.notifyDataSetChanged();
                         /*   for(int i=0;i<interiorList_bean.getResult().size();i++)
                            {
                                str_interiror_id=interiorList_bean.getResult().get(i).getId();
                               String str_interior_name= interiorList_bean.getResult().get(0).getInterior();
                                String upperString_str_interior_name = str_interior_name.substring(0,1).toUpperCase() + str_interior_name.substring(1);

                              //  edt_selct_interior.setText(upperString_str_interior_name);

                            }*/
                        }
                        String errorMsg = jObj.getString("message");
                        if(getActivity()!=null) {
                         /*   txt_avail_pro.setVisibility(View.VISIBLE);
                            txt_avail_pro.setText("No products added yet.");*/
                            //  Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                        }




                    } else {
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

                    Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();

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
                Map<String, String> params = new HashMap<String, String>();
                System.out.println("city id"+ cityid4);
                params.put("city_id",cityid4);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }




    //------------------------------------call web service for get all rm---------------------------------------------------------------
    ArrayList<all_rm> rm_name_list = new ArrayList<>();

    private void getAllRmList(final String get_id, final String choosed_city_id)
    {
        String url = "http://dailyreporting.in/"+company_name+"/api/all_rm_by_sm_id";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_CITY_ACCTO_CITY,
                new com.android.volley.Response.Listener<String>() {*/
        //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("All rm List AccState", "All rm List AccStatet response: " + response.toString());


                        try {
                            JSONObject jObj = new JSONObject(response);
                            String error = jObj.getString("status");
                            String msg = jObj.getString("message");

                            if (error.equals("success")) {
                                loader.setVisibility(View.GONE);
                                rm_name_list.clear();
                                JSONArray jsonArray = jObj.getJSONArray("result");
                                for (int i=0; i< jsonArray.length();i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String user_id = jsonObject.getString("user_id");
                                    String role_id = jsonObject.getString("role_id");
                                    String firstNm = jsonObject.getString("first_name");
                                    String lastnm = jsonObject.getString("last_name");
                                    String employecode = jsonObject.getString("employee_code");


                                    rm_name_list.add(new all_rm(user_id,role_id,firstNm,lastnm,employecode));

                                }
                                adapter = new SpinAdapter3(getActivity(),
                                        android.R.layout.simple_spinner_item, rm_name_list);
                                if (str_local_interior!=null && str_local_interior.equalsIgnoreCase("interior"))
                                {
                                    spinner_sales_manager_interior.setAdapter(adapter);
                                }
                                else
                                {
                                    spinner_sales_manager.setAdapter(adapter);
                                }


                            }
                            else
                            {
                                if (str_local_interior!=null && str_local_interior.equalsIgnoreCase("interior"))
                                {
                                    spinner_sales_manager_interior.setVisibility(View.GONE);
                                    txt_salesmanger_interior.setVisibility(View.VISIBLE);
                                    txt_salesmanger_interior.setText("No Regional Manager");
                                    str_workingwith_sales_interior = "";
                                    Toast.makeText(getActivity(), "No Regional Manager Available", Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    spinner_sales_manager.setVisibility(View.GONE);
                                    txt_salesmanger.setVisibility(View.VISIBLE);
                                    txt_salesmanger.setText("No Regional Manager");
                                    str_workingwith_sales="";
                                    Toast.makeText(getActivity(), "No Regional Manager Available", Toast.LENGTH_SHORT).show();

                                }


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
                    Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                if (!get_id.equalsIgnoreCase(""))
                {
                    System.out.println("sm"+get_id);
                    System.out.println("user choosed_city_id"+choosed_city_id);

                    params.put("sm_id",get_id);
                   // params.put("city_id",choosed_city_id);
                }


                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }

    //------------------------------------call web service for get all mr---------------------------------------------------------------
    ArrayList<all_mr> name_list = new ArrayList<>();

    private void getAllMrList(final String get_id, final String chosed_city_id)
    {

        final String city_id = sp.getData(getActivity(),"selected_city_id","null");
        System.out.println("user choosed city ####"+city_id);

        String url = "http://dailyreporting.in/"+company_name+"/api/all_mr_user_id";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_CITY_ACCTO_CITY,
                new com.android.volley.Response.Listener<String>() {*/
        //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("All Mr List AccState", "All Mr List AccStatet response: " + response.toString());


                        try {
                            JSONObject jObj = new JSONObject(response);
                            String error = jObj.getString("status");
                            String msg = jObj.getString("message");

                            if (error.equals("success")) {
                                loader.setVisibility(View.GONE);
                                name_list.clear();
                                JSONArray jsonArray = jObj.getJSONArray("result");
                                for (int i=0; i< jsonArray.length();i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String user_id = jsonObject.getString("user_id");
                                    String role_id = jsonObject.getString("role_id");
                                    String firstNm = jsonObject.getString("first_name");
                                    String lastnm = jsonObject.getString("last_name");
                                    String employecode = jsonObject.getString("employee_code");


                                    name_list.add(new all_mr(user_id,role_id,firstNm,lastnm,employecode));

                                }
                                adapter = new SpinAdapter(getActivity(),
                                        android.R.layout.simple_spinner_item, name_list);
                                if (str_local_interior!=null && str_local_interior.equalsIgnoreCase("interior"))
                                {
                                    spinner_area_manager_interior.setAdapter(adapter);
                                    // spinner_area_manager_interior.setVisibility(View.VISIBLE);
                                    if (role_id.equalsIgnoreCase("3") || role_id.equalsIgnoreCase("2"))
                                    {
                                        if (img_areamanger_interior_checked_icon.getVisibility()==View.VISIBLE)
                                        {
                                            txt_areamanger_interior.setVisibility(View.GONE);
                                            spinner_area_manager_interior.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }
                                else
                                {
                                    spinner_area_manager.setAdapter(adapter);
                                    if (role_id.equalsIgnoreCase("3") || role_id.equalsIgnoreCase("2"))
                                    {
                                        if (img_areamanger_checked_icon.getVisibility()==View.VISIBLE)
                                        {
                                            txt_areamanger.setVisibility(View.GONE);
                                            spinner_area_manager.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }
                            }
                            else
                            {
                                loader.setVisibility(View.GONE);

                                if (str_local_interior!=null && str_local_interior.equalsIgnoreCase("interior"))
                                {
                                    spinner_area_manager_interior.setVisibility(View.GONE);
                                    txt_areamanger_interior.setVisibility(View.VISIBLE);
                                    txt_areamanger_interior.setText("No MR");
                                    str_workingwith_area_interior="";
                                    Toast.makeText(getActivity(), "No MR Available", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    spinner_area_manager.setVisibility(View.GONE);
                                    txt_areamanger.setVisibility(View.VISIBLE);
                                    txt_areamanger.setText("No MR");
                                    str_workingwith_area="";
                                    Toast.makeText(getActivity(), "No MR Available", Toast.LENGTH_SHORT).show();

                                }


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
                    Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                if (!get_id.equalsIgnoreCase(""))
                {
                    System.out.println("am"+get_id);
                    System.out.println("chosed_city_id "+chosed_city_id);

                    params.put("am_id",get_id);
                    params.put("city_id",chosed_city_id);
                }
             /*else
             {
                 System.out.println("rm id"+user_id4);
                 params.put("am_id",user_id4);
             }*/

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }


    //------------------------------------call web service for get all mr by sm id---------------------------------------------------------------
    ArrayList<all_mr> am_by_sm_name_list = new ArrayList<>();

    private void getAllMrBySmList(final String get_id, final String chossed_city_id)
    {

        final String city_id = sp.getData(getActivity(),"selected_city_id","null");
        System.out.println("user choosed city ####"+city_id);

        String url = "http://dailyreporting.in/"+company_name+"/api/all_mr_by_sm_id";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_CITY_ACCTO_CITY,
                new com.android.volley.Response.Listener<String>() {*/
        //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("All Mr List AccState", "All Mr List AccStatet response: " + response.toString());


                        try {
                            JSONObject jObj = new JSONObject(response);
                            String error = jObj.getString("status");
                            String msg = jObj.getString("message");

                            if (error.equals("success")) {
                                loader.setVisibility(View.GONE);
                                am_by_sm_name_list.clear();
                                JSONArray jsonArray = jObj.getJSONArray("result");
                                for (int i=0; i< jsonArray.length();i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String user_id = jsonObject.getString("user_id");
                                    String role_id = jsonObject.getString("role_id");
                                    String firstNm = jsonObject.getString("first_name");
                                    String lastnm = jsonObject.getString("last_name");
                                    String employecode = jsonObject.getString("employee_code");


                                    am_by_sm_name_list.add(new all_mr(user_id,role_id,firstNm,lastnm,employecode));

                                }
                                adapter = new SpinAdapter(getActivity(),
                                        android.R.layout.simple_spinner_item, am_by_sm_name_list);
                                if (str_local_interior!=null && str_local_interior.equalsIgnoreCase("interior"))
                                {
                                    spinner_area_manager_interior.setAdapter(adapter);
                                    // spinner_area_manager_interior.setVisibility(View.VISIBLE);
                                    if (role_id.equalsIgnoreCase("3") || role_id.equalsIgnoreCase("2"))
                                    {
                                        if (img_areamanger_interior_checked_icon.getVisibility()==View.VISIBLE)
                                        {
                                            txt_areamanger_interior.setVisibility(View.GONE);
                                            spinner_area_manager_interior.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }
                                else
                                {
                                    spinner_area_manager.setAdapter(adapter);
                                    if (role_id.equalsIgnoreCase("3") || role_id.equalsIgnoreCase("2"))
                                    {
                                        if (img_areamanger_checked_icon.getVisibility()==View.VISIBLE)
                                        {
                                            txt_areamanger.setVisibility(View.GONE);
                                            spinner_area_manager.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }
                            }
                            else
                            {
                                loader.setVisibility(View.GONE);


                                if (str_local_interior!=null && str_local_interior.equalsIgnoreCase("interior"))
                                {
                                    spinner_area_manager_interior.setVisibility(View.GONE);
                                    txt_areamanger_interior.setVisibility(View.VISIBLE);
                                    txt_areamanger_interior.setText("No MR");
                                    str_workingwith_area_interior="";
                                    Toast.makeText(getActivity(), "No MR Available", Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    spinner_area_manager.setVisibility(View.GONE);
                                    txt_areamanger.setVisibility(View.VISIBLE);
                                    txt_areamanger.setText("No MR");
                                    str_workingwith_area="";
                                    Toast.makeText(getActivity(), "No MR Available", Toast.LENGTH_SHORT).show();

                                }


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
                    Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                if (!get_id.equalsIgnoreCase(""))
                {
                    System.out.println("sm"+get_id);
                    System.out.println("chossed_city_id "+chossed_city_id);

                    params.put("sm_id",get_id);
                    params.put("city_id",chossed_city_id);
                }
             /*else
             {
                 System.out.println("rm id"+user_id4);
                 params.put("am_id",user_id4);
             }*/

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }


    //------------------------------------call web service for get all mr by rm id---------------------------------------------------------------
   ArrayList<all_mr> am_by_rm_name_list = new ArrayList<>();

   private void getAllMrByRmList(final String get_id, final String choosed_city_id)
  {

      //final String city_id = sp.getData(getActivity(),"selected_city_id","null");
     // System.out.println("user choosed city ####"+city_id);

     String url = "http://dailyreporting.in/"+company_name+"/api/all_mr_by_rm_id";
     System.out.println("sout url"+ url);

     loader.setVisibility(View.VISIBLE);

     // Tag used to cancel the request
     String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_CITY_ACCTO_CITY,
                new com.android.volley.Response.Listener<String>() {*/
     //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

     StringRequest strReq = new StringRequest(Request.Method.POST, url,
             new com.android.volley.Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {
                     Log.d("All Mr List AccState", "All Mr List AccStatet response: " + response.toString());


                     try {
                         JSONObject jObj = new JSONObject(response);
                         String error = jObj.getString("status");
                         String msg = jObj.getString("message");

                         if (error.equals("success")) {
                             loader.setVisibility(View.GONE);
                             am_by_rm_name_list.clear();
                             JSONArray jsonArray = jObj.getJSONArray("result");
                             for (int i=0; i< jsonArray.length();i++)
                             {
                                 JSONObject jsonObject = jsonArray.getJSONObject(i);
                                 String user_id = jsonObject.getString("user_id");
                                 String role_id = jsonObject.getString("role_id");
                                 String firstNm = jsonObject.getString("first_name");
                                 String lastnm = jsonObject.getString("last_name");
                                 String employecode = jsonObject.getString("employee_code");


                                 am_by_rm_name_list.add(new all_mr(user_id,role_id,firstNm,lastnm,employecode));

                             }
                             adapter = new SpinAdapter(getActivity(),
                                     android.R.layout.simple_spinner_item, am_by_rm_name_list);
                             if (str_local_interior!=null && str_local_interior.equalsIgnoreCase("interior"))
                             {
                                 spinner_area_manager_interior.setAdapter(adapter);
                                // spinner_area_manager_interior.setVisibility(View.VISIBLE);
                                 if (role_id.equalsIgnoreCase("3") || role_id.equalsIgnoreCase("2"))
                                 {
                                     if (img_areamanger_interior_checked_icon.getVisibility()==View.VISIBLE)
                                     {
                                         txt_areamanger_interior.setVisibility(View.GONE);
                                         spinner_area_manager_interior.setVisibility(View.VISIBLE);
                                     }
                                 }

                             }
                             else
                             {
                                 spinner_area_manager.setAdapter(adapter);
                                 if (role_id.equalsIgnoreCase("3") || role_id.equalsIgnoreCase("2"))
                                 {
                                     if (img_areamanger_checked_icon.getVisibility()==View.VISIBLE)
                                     {
                                         txt_areamanger.setVisibility(View.GONE);
                                         spinner_area_manager.setVisibility(View.VISIBLE);
                                     }
                                 }

                             }
                         }
                         else
                         {
                             loader.setVisibility(View.GONE);

                             if (str_local_interior!=null && str_local_interior.equalsIgnoreCase("interior"))
                             {
                                 spinner_area_manager_interior.setVisibility(View.GONE);
                                 txt_areamanger_interior.setVisibility(View.VISIBLE);
                                 txt_areamanger_interior.setText("No MR");
                                 str_workingwith_area_interior="";
                                 Toast.makeText(getActivity(), "No MR Available", Toast.LENGTH_SHORT).show();

                             }
                             else
                             {
                                 spinner_area_manager.setVisibility(View.GONE);
                                 txt_areamanger.setVisibility(View.VISIBLE);
                                 txt_areamanger.setText("No MR");
                                 str_workingwith_area="";
                                 Toast.makeText(getActivity(), "No MR Available", Toast.LENGTH_SHORT).show();

                             }


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
                 Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
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
         protected Map<String, String> getParams() throws AuthFailureError {
             Map<String, String> params = new HashMap<String, String>();

             if (!get_id.equalsIgnoreCase(""))
             {
                 System.out.println("rm "+get_id);
                 System.out.println("choosed_city_id "+choosed_city_id);

                 params.put("rm_id",get_id);
                 params.put("city_id",choosed_city_id);


             }
             /*else
             {
                 System.out.println("rm id"+user_id4);
                 params.put("am_id",user_id4);
             }*/

             return params;
         }
     };
     // Adding request to request queue
     AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
 }

    /*--------------------------------------web service for get all area manager by rm--------------------------------------*/

    ArrayList<all_am> am_name_list_by_sm = new ArrayList<>();

    private void getAllAmListbtSm(final String get_id,final String choosed_city)
    {
        String url = "http://dailyreporting.in/"+company_name+"/api/all_am_by_sm_id";//for get all area manager according to rm.
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_CITY_ACCTO_CITY,
                new com.android.volley.Response.Listener<String>() {*/
        //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("All am List AccState", "All am List AccStatet response: " + response.toString());


                        try {
                            JSONObject jObj = new JSONObject(response);
                            String error = jObj.getString("status");

                            if (error.equals("success")) {
                                loader.setVisibility(View.GONE);
                                am_name_list_by_sm.clear();
                                JSONArray jsonArray = jObj.getJSONArray("result");
                                for (int i=0; i< jsonArray.length();i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String user_id = jsonObject.getString("user_id");
                                    String role_id = jsonObject.getString("role_id");
                                    String firstNm = jsonObject.getString("first_name");
                                    String lastnm = jsonObject.getString("last_name");
                                    String employecode = jsonObject.getString("employee_code");

                                    am_name_list_by_sm.add(new all_am(user_id,role_id,firstNm,lastnm,employecode));

                                }
                                adapter = new SpinAdapter2(getActivity(),
                                        android.R.layout.simple_spinner_item, am_name_list_by_sm);
                                if (str_local_interior!=null && str_local_interior.equalsIgnoreCase("interior"))
                                {
                                    spinner_regionalmanger_interior.setAdapter(adapter);

                                    // spinner_area_manager_interior.setVisibility(View.VISIBLE);
                                    if (role_id.equalsIgnoreCase("2"))
                                    {
                                        if (img_regionalmanger_interior_checked_icon.getVisibility()==View.VISIBLE)
                                        {
                                            txt_regionalmanger_interior.setVisibility(View.GONE);
                                            spinner_regionalmanger_interior.setVisibility(View.VISIBLE);
                                        }
                                    }



                                }
                                else
                                {
                                    spinner_regional_manager.setAdapter(adapter);

                                    if (role_id.equalsIgnoreCase("2"))
                                    {
                                        if (img_regionalmanger_checked_icon.getVisibility()==View.VISIBLE)
                                        {
                                            txt_regionalmanger.setVisibility(View.GONE);
                                            spinner_regional_manager.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }



                            }
                            else
                            {
                                loader.setVisibility(View.GONE);

                                if (str_local_interior!=null && str_local_interior.equalsIgnoreCase("interior"))
                                {
                                    spinner_regionalmanger_interior.setVisibility(View.GONE);
                                    txt_regionalmanger_interior.setVisibility(View.VISIBLE);
                                    txt_regionalmanger_interior.setText("No Area Manager");
                                    str_workingwith_regional_interior="";
                                    Toast.makeText(getActivity(), "No Area Manager Available", Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    spinner_regional_manager.setVisibility(View.GONE);
                                    txt_regionalmanger.setVisibility(View.VISIBLE);
                                    txt_regionalmanger.setText("No Area Manager");
                                    str_workingwith_regional="";
                                    Toast.makeText(getActivity(), "No Area Manager Available", Toast.LENGTH_SHORT).show();

                                }


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
                    Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                if (!get_id.equalsIgnoreCase(""))
                {
                    System.out.println("sm get id"+get_id);
                    params.put("sm_id",get_id);
                    params.put("city_id",choosed_city);
                }
                /*else
                {
                    System.out.println("regional  id"+user_id4);
                    params.put("rm_id",user_id4);
                }
*/
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }


    /*--------------------------------------web service for get all area manager by rm--------------------------------------*/

    ArrayList<all_am> am_name_list = new ArrayList<>();

    private void getAllAmList(final String get_id,final String choosed_city)
    {
        String url = "http://dailyreporting.in/"+company_name+"/api/all_am_by_rm_id";//for get all area manager according to rm.
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_CITY_ACCTO_CITY,
                new com.android.volley.Response.Listener<String>() {*/
        //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("All am List AccState", "All am List AccStatet response: " + response.toString());


                        try {
                            JSONObject jObj = new JSONObject(response);
                            String error = jObj.getString("status");

                            if (error.equals("success")) {
                                loader.setVisibility(View.GONE);
                                am_name_list.clear();
                                JSONArray jsonArray = jObj.getJSONArray("result");
                                for (int i=0; i< jsonArray.length();i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String user_id = jsonObject.getString("user_id");
                                    String role_id = jsonObject.getString("role_id");
                                    String firstNm = jsonObject.getString("first_name");
                                    String lastnm = jsonObject.getString("last_name");
                                    String employecode = jsonObject.getString("employee_code");

                                    am_name_list.add(new all_am(user_id,role_id,firstNm,lastnm,employecode));

                                }
                                adapter = new SpinAdapter2(getActivity(),
                                        android.R.layout.simple_spinner_item, am_name_list);
                                if (str_local_interior!=null && str_local_interior.equalsIgnoreCase("interior"))
                                {
                                    spinner_regionalmanger_interior.setAdapter(adapter);

                                    // spinner_area_manager_interior.setVisibility(View.VISIBLE);
                                    if (role_id.equalsIgnoreCase("2"))
                                    {
                                            if (img_regionalmanger_interior_checked_icon.getVisibility()==View.VISIBLE)
                                            {
                                                txt_regionalmanger_interior.setVisibility(View.GONE);
                                                spinner_regionalmanger_interior.setVisibility(View.VISIBLE);
                                            }
                                    }



                                }
                                else
                                {
                                    spinner_regional_manager.setAdapter(adapter);

                                    if (role_id.equalsIgnoreCase("2"))
                                    {
                                        if (img_regionalmanger_checked_icon.getVisibility()==View.VISIBLE)
                                        {
                                            txt_regionalmanger.setVisibility(View.GONE);
                                            spinner_regional_manager.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }



                            }
                            else
                            {
                                loader.setVisibility(View.GONE);

                                if (str_local_interior!=null && str_local_interior.equalsIgnoreCase("interior"))
                                {
                                    spinner_regionalmanger_interior.setVisibility(View.GONE);
                                    txt_regionalmanger_interior.setVisibility(View.VISIBLE);
                                    txt_regionalmanger_interior.setText("No Area Manager");
                                    str_workingwith_regional_interior="";
                                    Toast.makeText(getActivity(), "No Area Manager Available", Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    spinner_regional_manager.setVisibility(View.GONE);
                                    txt_regionalmanger.setVisibility(View.VISIBLE);
                                    txt_regionalmanger.setText("No Area Manager");
                                    str_workingwith_regional="";
                                    Toast.makeText(getActivity(), "No Area Manager Available", Toast.LENGTH_SHORT).show();

                                }


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
                    Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                if (!get_id.equalsIgnoreCase(""))
                {
                    System.out.println("regional get id"+get_id);
                    System.out.println("choosed_city id"+choosed_city);

                    params.put("rm_id",get_id);
                    params.put("city_id",choosed_city);

                }
                /*else
                {
                    System.out.println("regional  id"+user_id4);
                    params.put("rm_id",user_id4);
                }
*/
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }


    // ---------------------------- Start work Data for Interior------------------------------------------------------------------------------//
    private void start_Work_interior(final String field_area4,final String user_id4,final String location_region4,
                                  final String location_city4,final String work_type4
            ,final String interior_id4,
                                  final String independent4,final String am_id4,final String mr_id4,final String rm_id4,
                                  final  String sm_id4,final String ip_location4,
                                  final String start_lat4,final String start_long4) {

        String url = "http://dailyreporting.in/"+company_name+"/api/start_field_work";
        System.out.println("sout url"+ url);

        loader2.setVisibility(View.VISIBLE);


        // Tag used to cancel the request
        String cancel_req_tag = "area";
        /*StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_START_FIELD_WORK,
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

                        sp.saveData(getActivity(),"selected_city_id_local","");
                        sp.saveData(getActivity(),"mr_id",mr_id4);

                        loader2.setVisibility(View.GONE);
                        String result = jObj.getString("result");

                        JSONObject jsonObject44=new JSONObject(result);
                        String dcr_live_table_insert_id=jsonObject44.getString("dcr_live_table_insert_id");
                        String dcr_table_insert_id=jsonObject44.getString("dcr_table_insert_id");

                        System.out.println("Dcr id is###"+dcr_table_insert_id);

                        MySharedPref sp=new MySharedPref();
                          sp.saveData(getActivity(),"dcr_table_insert_id",dcr_table_insert_id);
                          sp.saveData(getActivity(),"dcr_live_table_insert_id",dcr_live_table_insert_id);

                        /*getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFrame, new TimeLineFragment())
                                .addToBackStack("21")
                                .commit();*/

                        TimeLineFragment timeLineFragment = TimeLineFragment.newInstance();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                        Bundle b = new Bundle();
                        b.putString("fied_known","Start Field Work");
                        timeLineFragment.setArguments(b);
                        transaction.replace(R.id.contentFrame, timeLineFragment);
                        transaction.addToBackStack("21");
                        transaction.commit();


                    } else {


                            loader2.setVisibility(View.GONE);


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

                    loader2.setVisibility(View.GONE);

                Log.e("AllCityListAccState", "All City List AccState Error: " + error.getMessage());
                try {

                    //  JSONObject jsonObject33=new JSONObject(message);
                    //   String password=jsonObject33.getString("password");

                    Toast.makeText(getActivity(), "Something went wrong.",Toast.LENGTH_SHORT).show();

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
                Map<String, String> params = new HashMap<String, String>();

                System.out.println("Field Area###"+field_area4);
                System.out.println("Field Area###"+user_id4);
                System.out.println("Field Area###"+location_region4);
                System.out.println("Field Area###"+location_city4);
                System.out.println("Field Area###"+work_type4);
                System.out.println("Field Area###"+interior_id4);
                System.out.println("Field Area###"+independent4);
                System.out.println("Field Area###"+am_id4);
                System.out.println("Field Area###"+mr_id4);
                System.out.println("Field Area###"+rm_id4);
                System.out.println("Field Area###"+sm_id4);
                System.out.println("Field Area###"+ip_location4);
                System.out.println("Field Area###"+start_lat4);
                System.out.println("Field Area###"+start_long4);
                System.out.println("work by Area###"+str_car_bike);


                params.put("field_area",field_area4);
                params.put("user_id",user_id4);
                params.put("location_region",location_region4);
                params.put("location_city",location_city4);
                params.put("work_type",work_type4);
                params.put("interior_id",interior_id4);
                params.put("independent",independent4);
                params.put("am_id",am_id4);
                params.put("mr_id",mr_id4);
                params.put("rm_id",rm_id4);
                params.put("sm_id",sm_id4);
                params.put("ip_location",ip_location4);
                params.put("start_lat",start_lat4);
                params.put("start_long",start_long4);
                params.put("work_via",str_car_bike);
                params.put("location","1");

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }
    // ---------------------------- Start work Data for local------------------------------------------------------------------------------//
    private void start_Work_local(final String field_area4,final String user_id4,final String location_region4,
                                  final String location_city4,final String work_type4
            ,final String interior_id4,
                                  final String independent4,final String am_id4,final String mr_id4,final String rm_id4,
                                  final  String sm_id4,final String ip_location4,
                                  final String start_lat4,final String start_long4) {


        String url = "http://dailyreporting.in/"+company_name+"/api/start_field_work";
        System.out.println("sout url"+ url);



        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        /*StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_START_FIELD_WORK,
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


                        sp.saveData(getActivity(),"selected_city_id","");
                        sp.saveData(getActivity(),"mr_id",mr_id4);

                        loader.setVisibility(View.GONE);
                        String result = jObj.getString("result");

                        JSONObject jsonObject44=new JSONObject(result);
                        String dcr_table_insert_id=jsonObject44.getString("dcr_table_insert_id");
                        String dcr_live_table_insert_id=jsonObject44.getString("dcr_live_table_insert_id");

                        System.out.println("Dcr id is###"+dcr_table_insert_id);
                        System.out.println("Dcr Live id is###"+dcr_live_table_insert_id);


                        MySharedPref sp=new MySharedPref();
                        sp.saveData(getActivity(),"dcr_table_insert_id",dcr_table_insert_id);
                        sp.saveData(getActivity(),"dcr_live_table_insert_id",dcr_live_table_insert_id);

                       /* getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFrame, new TimeLineFragment())
                                .addToBackStack("21")
                                .commit();*/

                        TimeLineFragment timeLineFragment = TimeLineFragment.newInstance();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                        Bundle b = new Bundle();
                        b.putString("fied_known","Start Field Work");
                        timeLineFragment.setArguments(b);
                        transaction.replace(R.id.contentFrame, timeLineFragment);
                        transaction.addToBackStack("21");
                        transaction.commit();


                    }
                    else {

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
                    Toast.makeText(getActivity(), "Something went wrong.",Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = new HashMap<String, String>();
                System.out.println("Field Area###"+field_area4);
                System.out.println("Field Area###"+user_id4);
                System.out.println("Field Area###"+location_region4);
                System.out.println("Field Area###"+location_city4);
                System.out.println("Field Area###"+work_type4);
                System.out.println("Field Area###"+interior_id4);
                System.out.println("Field Area###"+independent4);
                System.out.println("Field Area###"+am_id4);
                System.out.println("Field Area###"+mr_id4);
                System.out.println("Field Area###"+rm_id4);
                System.out.println("Field Area###"+sm_id4);
                System.out.println("Field Area###"+ip_location4);
                System.out.println("Field Area###"+start_lat4);
                System.out.println("Field Area###"+start_long4);

                params.put("field_area",field_area4);
                params.put("user_id",user_id4);
                params.put("location_region",location_region4);
                params.put("location_city",location_city4);
                params.put("work_type",work_type4);
                params.put("interior_id",interior_id4);
                params.put("independent",independent4);
                params.put("am_id",am_id4);
                params.put("mr_id",mr_id4);
                params.put("rm_id",rm_id4);
                params.put("sm_id",sm_id4);
                params.put("ip_location",ip_location4);
                params.put("start_lat",start_lat4);
                params.put("start_long",start_long4);
                params.put("location","0");


                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }
    // ---------------------------- For WebService Call for Get Workingwith user record Data-------------------------------------------------------------------------------//
    private void getData_WorkingwithUserRecord(final String user_id4) {

        String url = "http://dailyreporting.in/"+company_name+"/api/working_with_user_record";
        System.out.println("sout url"+ url);


        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        /*StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_WORKING_WITH_USER_RECORD, new Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("WorkingWithUserRecord", "WorkingWithUserRecord response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);



                        String result=jObj.getString("result");
                        System.out.println("Result WorkingWithUserRecord***"+result);
                        JSONObject jsonObject22=new JSONObject(result);

                        if(role_id.equalsIgnoreCase("5")) {

                            rr_selct_territory.setVisibility(View.VISIBLE);
                            edt_selct_city.setFocusable(false);
                            edt_selct_city.setClickable(false);

                            //For Salesman Manger
                            String sales_manager_record = jsonObject22.getString("sales_manager_record");
                            JSONObject jsonObject33 = new JSONObject(sales_manager_record);
                            String sales_manager_name = jsonObject33.getString("sales_manager_name");
                            System.out.println("Sales Manger Name###"+sales_manager_name);
                            String sales_manager_designation = jsonObject33.getString("sales_manager_designation");
                            sales_manager_user_id = jsonObject33.getString("sales_manager_user_id");

                            //For Regional Manger
                            String regional_manager_record = jsonObject22.getString("regional_manager_record");
                            System.out.println("Regional Manger Record###"+regional_manager_record);
                            JSONObject jsonObject44 = new JSONObject(regional_manager_record);
                            String regional_manager_name = jsonObject44.getString("regional_manager_name");
                            System.out.println("Regional Manger Name###"+regional_manager_name);
                            String regional_manager_designation = jsonObject44.getString("regional_manager_designation");
                            regional_manager_user_id = jsonObject44.getString("regional_manager_user_id");

                            //For Area Manger
                            String area_manager_record = jsonObject22.getString("area_manager_record");
                            JSONObject jsonObject55 = new JSONObject(area_manager_record);
                            String area_manager_name = jsonObject55.getString("area_manager_name");
                            String area_manager_designation = jsonObject55.getString("area_manager_designation");
                            area_manager_user_id = jsonObject55.getString("area_manager_user_id");




                            //Set For SalesMan Manger
                            if(sales_manager_name.equalsIgnoreCase(""))
                            {

                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText("No Sales Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText("No Sales Manager");

                            }


                            else  if(sales_manager_name.equalsIgnoreCase(" "))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText("No Sales Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText("No Sales Manager");
                            }
                        else  if(sales_manager_name.equalsIgnoreCase("null"))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText("No Sales Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText("No Sales Manager");
                            }

                            else {
                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText(sales_manager_name + ", " + sales_manager_designation);
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText(sales_manager_name + ", " + sales_manager_designation);
                            }


                            //Set For Regional Manger
                            if(regional_manager_name.equalsIgnoreCase(""))
                            {

                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText("No Regional Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText("No Regional Manager");

                            }


                            else  if(regional_manager_name.equalsIgnoreCase(" "))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText("No Regional Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText("No Regional Manager");

                            }

                            else  if(regional_manager_name.equalsIgnoreCase("null"))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText("No Regional Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText("No Regional Manager");

                            }

                            else {
                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText(regional_manager_name + ", " + regional_manager_designation);
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText(regional_manager_name + ", " + regional_manager_designation);

                            }

                            System.out.println("Area Manger Name###"+area_manager_name);

                            //Set For Area Manger
                            if(area_manager_name.equalsIgnoreCase(""))
                            {

                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText("No Area Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText("No Area Manager");

                            }


                            else  if(area_manager_name.equalsIgnoreCase(" "))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText("No Area Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText("No Area Manager");

                            }
                            else  if(area_manager_name.equalsIgnoreCase("null"))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText("No Area Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText("No Area Manager");

                            }

                            else {
                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText(area_manager_name + ", " + area_manager_designation);
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText(area_manager_name + ", " + area_manager_designation);

                            }




                        }

                        if(role_id.equalsIgnoreCase("4")) {

                            rr_selct_territory.setVisibility(View.VISIBLE);
                            edt_selct_city.setFocusable(true);

                            edt_selct_city.setClickable(true);

                            //For Salesman Manger
                            String sales_manager_record = jsonObject22.getString("sales_manager_record");
                            JSONObject jsonObject33 = new JSONObject(sales_manager_record);
                            String sales_manager_name = jsonObject33.getString("sales_manager_name");
                            System.out.println("Sales Manger Name###"+sales_manager_name);
                            String sales_manager_designation = jsonObject33.getString("sales_manager_designation");
                            sales_manager_user_id = jsonObject33.getString("sales_manager_user_id");

                            //For Regional Manger
                            String regional_manager_record = jsonObject22.getString("regional_manager_record");
                            System.out.println("Regional Manger Record###"+regional_manager_record);
                            JSONObject jsonObject44 = new JSONObject(regional_manager_record);
                            String regional_manager_name = jsonObject44.getString("regional_manager_name");
                            System.out.println("Regional Manger Name###"+regional_manager_name);
                            String regional_manager_designation = jsonObject44.getString("regional_manager_designation");
                            regional_manager_user_id = jsonObject44.getString("regional_manager_user_id");

                            //For Area Manger
                            String area_manager_record = jsonObject22.getString("area_manager_record");
                            JSONObject jsonObject55 = new JSONObject(area_manager_record);
                            String area_manager_name = jsonObject55.getString("area_manager_name");
                            String area_manager_designation = jsonObject55.getString("area_manager_designation");
                            area_manager_user_id = jsonObject55.getString("area_manager_user_id");




                            //Set For SalesMan Manger
                            if(sales_manager_name.equalsIgnoreCase(""))
                            {

                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText("No Sales Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText("No Sales Manager");

                            }


                            else  if(sales_manager_name.equalsIgnoreCase(" "))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText("No Sales Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText("No Sales Manager");
                            }
                            else  if(sales_manager_name.equalsIgnoreCase("null"))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText("No Sales Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText("No Sales Manager");
                            }

                            else {
                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText(sales_manager_name + ", " + sales_manager_designation);
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText(sales_manager_name + ", " + sales_manager_designation);
                            }


                            //Set For Regional Manger
                            if(regional_manager_name.equalsIgnoreCase(""))
                            {

                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText("No Regional Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText("No Regional Manager");

                            }


                            else  if(regional_manager_name.equalsIgnoreCase(" "))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText("No Regional Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText("No Regional Manager");

                            }

                            else  if(regional_manager_name.equalsIgnoreCase("null"))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText("No Regional Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText("No Regional Manager");

                            }

                            else {
                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText(regional_manager_name + ", " + regional_manager_designation);
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText(regional_manager_name + ", " + regional_manager_designation);

                            }

                            System.out.println("Area Manger Name###"+area_manager_name);

                            //Set For Area Manger
                            if(area_manager_name.equalsIgnoreCase(""))
                            {

                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText("Select MR");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText("Select MR");

                            }


                            else  if(area_manager_name.equalsIgnoreCase(" "))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText("Select MR");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText("Select MR");

                            }
                            else  if(area_manager_name.equalsIgnoreCase("null"))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText("Select MR");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText("Select MR");

                            }

                            else {
                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText(area_manager_name + ", " + area_manager_designation);
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText(area_manager_name + ", " + area_manager_designation);

                            }


                        }

                        if(role_id.equalsIgnoreCase("3")) {

                            rr_selct_territory.setVisibility(View.VISIBLE);
                            edt_selct_city.setFocusable(true);

                            edt_selct_city.setClickable(true);

                            //For Salesman Manger
                            String sales_manager_record = jsonObject22.getString("sales_manager_record");
                            JSONObject jsonObject33 = new JSONObject(sales_manager_record);
                            String sales_manager_name = jsonObject33.getString("sales_manager_name");
                            System.out.println("Sales Manger Name###"+sales_manager_name);
                            String sales_manager_designation = jsonObject33.getString("sales_manager_designation");
                            sales_manager_user_id = jsonObject33.getString("sales_manager_user_id");

                            //For Regional Manger
                            String regional_manager_record = jsonObject22.getString("regional_manager_record");
                            System.out.println("Regional Manger Record###"+regional_manager_record);
                            JSONObject jsonObject44 = new JSONObject(regional_manager_record);
                            String regional_manager_name = jsonObject44.getString("regional_manager_name");
                            System.out.println("Regional Manger Name###"+regional_manager_name);
                            String regional_manager_designation = jsonObject44.getString("regional_manager_designation");
                            regional_manager_user_id = jsonObject44.getString("regional_manager_user_id");

                            //For Area Manger
                            String area_manager_record = jsonObject22.getString("area_manager_record");
                            JSONObject jsonObject55 = new JSONObject(area_manager_record);
                            String area_manager_name = jsonObject55.getString("area_manager_name");
                            String area_manager_designation = jsonObject55.getString("area_manager_designation");
                            area_manager_user_id = jsonObject55.getString("area_manager_user_id");




                            //Set For SalesMan Manger
                            if(sales_manager_name.equalsIgnoreCase(""))
                            {

                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText("No Sales Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText("No Sales Manager");

                            }


                            else  if(sales_manager_name.equalsIgnoreCase(" "))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText("No Sales Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText("No Sales Manager");
                            }
                            else  if(sales_manager_name.equalsIgnoreCase("null"))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText("No Sales Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText("No Sales Manager");
                            }

                            else {
                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText(sales_manager_name + ", " + sales_manager_designation);
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText(sales_manager_name + ", " + sales_manager_designation);
                            }


                            //Set For Regional Manger
                            if(regional_manager_name.equalsIgnoreCase(""))
                            {

                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText("Select Area Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText("Select Area Manager");

                            }


                            else  if(regional_manager_name.equalsIgnoreCase(" "))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText("Select Area Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText("Select Area Manager");

                            }

                            else  if(regional_manager_name.equalsIgnoreCase("null"))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText("Select Area Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText("Select Area Manager");

                            }

                            else {
                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText(regional_manager_name + ", " + regional_manager_designation);
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText(regional_manager_name + ", " + regional_manager_designation);

                            }

                            System.out.println("Area Manger Name###"+area_manager_name);

                            //Set For Area Manger
                            if(area_manager_name.equalsIgnoreCase(""))
                            {

                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText("Select MR");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText("Select MR");

                            }


                            else  if(area_manager_name.equalsIgnoreCase(" "))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText("Select MR");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText("Select MR");

                            }
                            else  if(area_manager_name.equalsIgnoreCase("null"))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText("Select MR");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText("Select MR");

                            }

                            else {
                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText(area_manager_name + ", " + area_manager_designation);
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText(area_manager_name + ", " + area_manager_designation);
                                }



                        }
                        if(role_id.equalsIgnoreCase("2")) {

                            rr_selct_territory.setVisibility(View.VISIBLE);
                            edt_selct_city.setFocusable(true);

                            edt_selct_city.setClickable(true);

                            //For Salesman Manger
                            String sales_manager_record = jsonObject22.getString("sales_manager_record");
                            JSONObject jsonObject33 = new JSONObject(sales_manager_record);
                            String sales_manager_name = jsonObject33.getString("sales_manager_name");
                            System.out.println("Sales Manger Name###"+sales_manager_name);
                            String sales_manager_designation = jsonObject33.getString("sales_manager_designation");
                            sales_manager_user_id = jsonObject33.getString("sales_manager_user_id");

                            //For Regional Manger
                            String regional_manager_record = jsonObject22.getString("regional_manager_record");
                            System.out.println("Regional Manger Record###"+regional_manager_record);
                            JSONObject jsonObject44 = new JSONObject(regional_manager_record);
                            String regional_manager_name = jsonObject44.getString("regional_manager_name");
                            System.out.println("Regional Manger Name###"+regional_manager_name);
                            String regional_manager_designation = jsonObject44.getString("regional_manager_designation");
                            regional_manager_user_id = jsonObject44.getString("regional_manager_user_id");

                            //For Area Manger
                            String area_manager_record = jsonObject22.getString("area_manager_record");
                            JSONObject jsonObject55 = new JSONObject(area_manager_record);
                            String area_manager_name = jsonObject55.getString("area_manager_name");
                            String area_manager_designation = jsonObject55.getString("area_manager_designation");
                            area_manager_user_id = jsonObject55.getString("area_manager_user_id");




                            //Set For SalesMan Manger
                            if(sales_manager_name.equalsIgnoreCase(""))
                            {

                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText("Select Regional Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText("Select Regional Manager");

                            }


                            else  if(sales_manager_name.equalsIgnoreCase(" "))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText("Select Regional Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText("Select Regional Manager");
                            }
                            else  if(sales_manager_name.equalsIgnoreCase("null"))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText("Select Regional Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText("Select Regional Manager");
                            }

                            else {
                                //Set Text for area,regional and Salesman Manager
                                txt_salesmanger.setText(sales_manager_name + ", " + sales_manager_designation);
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_salesmanger_interior.setText(sales_manager_name + ", " + sales_manager_designation);
                            }


                            //Set For Regional Manger
                            if(regional_manager_name.equalsIgnoreCase(""))
                            {

                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText("Select Area Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText("Select Area Manager");

                            }


                            else  if(regional_manager_name.equalsIgnoreCase(" "))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText("Select Area Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText("Select Area Manager");

                            }

                            else  if(regional_manager_name.equalsIgnoreCase("null"))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText("Select Area Manager");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText("Select Area Manager");

                            }

                            else {
                                //Set Text for area,regional and Salesman Manager
                                txt_regionalmanger.setText(regional_manager_name + ", " + regional_manager_designation);
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_regionalmanger_interior.setText(regional_manager_name + ", " + regional_manager_designation);

                            }

                            System.out.println("Area Manger Name###"+area_manager_name);

                            //Set For Area Manger
                            if(area_manager_name.equalsIgnoreCase(""))
                            {

                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText("Select MR");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText("Select MR");

                            }


                            else  if(area_manager_name.equalsIgnoreCase(" "))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText("Select MR");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText("Select MR");

                            }
                            else  if(area_manager_name.equalsIgnoreCase("null"))
                            {
                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText("Select MR");
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText("Select MR");

                            }

                            else {
                                //Set Text for area,regional and Salesman Manager
                                txt_areamanger.setText(area_manager_name + ", " + area_manager_designation);
                                //Set Text for area,regional and Salesman Manager Interior
                                txt_areamanger_interior.setText(area_manager_name + ", " + area_manager_designation);

                            }



                    /*        //Set Text for area,regional and Salesman Manager
                            txt_salesmanger.setText(sales_manager_name + ", " + sales_manager_designation);
                            txt_regionalmanger.setText(regional_manager_name + ", " + regional_manager_designation);
                            txt_areamanger.setText(area_manager_name + ", " + area_manager_designation);

                            //Set Text for area,regional and Salesman Manager Interior
                            txt_salesmanger_interior.setText(sales_manager_name + ", " + sales_manager_designation);
                            txt_regionalmanger_interior.setText(regional_manager_name + ", " + regional_manager_designation);
                            txt_areamanger_interior.setText(area_manager_name + ", " + area_manager_designation);*/

                        }



                    }

                    else {
                        loader.setVisibility(View.GONE);

                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getActivity(), errorMsg,Toast.LENGTH_SHORT).show();

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

                Log.e("WorkingWithUserRecord", "WorkingWithUserRecord  Error: " + error.getMessage());
                try {
                   /* JSONObject jsonObject22=new JSONObject(error.getMessage());

                    String errorMsg=jsonObject22.getString("message");*/
                    //  JSONObject jsonObject33=new JSONObject(message);
                    //   String password=jsonObject33.getString("password");

                    Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();

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


    // ---------------------------- For Get Current Location -------------------------------------------------------------------------------//
    private void getLocation() {
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
        GPSTracker gps = new GPSTracker(getActivity());

        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled == false) {

            //Show Aleat Dialog for permission
            //showGPSDisabledAlertToUser();
            gps.showSettingsAlert();
        }
        else
        {
            if (gps.canGetLocation()) {
                currentlat = gps.getLatitude();
                currentlong = gps.getLongitude();
                System.out.println("Current Latitude@@@" + currentlat);
                System.out.println("Current Longitude@@@" + currentlong);
                if(currentlat!=0.0 && currentlong!=0.0) {
                   //Get  Address from Current latitude and Longitude
                    str_city_local_name = getAddressFromLatlong(currentlat, currentlong);
                    System.out.println("City Name&&&" + str_city_local_name);
                }
                else
                {
                    if (progressDialog == null) {
                        progressDialog = createProgressDialog(getActivity());
                        progressDialog.show();
                    }
                    else {
                        progressDialog.show();
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("on run");
                            progressDialog.dismiss();
                            getLocation();
                        }
                    }, 3000);

                    txt_current_loc_descp.setText("Fetching Your Current Location..");
                    Txt_current_loc_descp_interior.setText("Fetching Your Current Location..");
                    //Toast.makeText(getActivity(), "Fetching Your Current Location..", Toast.LENGTH_LONG).show();

                }



            } /*else {
                gps.showSettingsAlert();
            }  */
        }




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


    // ---------------------------- For Address from Current latitude and Longitude -------------------------------------------------------------------------------//
    public String getAddressFromLatlong(double currentlat2, double currentlong2)
    {
        Geocoder geocoder;
        String city = null;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(currentlat2, currentlong2, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            str_state_local_name = addresses.get(0).getCountryName();

            System.out.println("Address$$$"+address);

            System.out.println("State Name***"+str_state_local_name);

            //Set Address in TextView
            txt_current_loc_descp.setText(address);
            Txt_current_loc_descp_interior.setText(address);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return city;
    }
    @Override
    public void onClick(View v) {

        if(v==img_local_checked_icon)
        {
            img_local_checked_icon.setVisibility(View.GONE);
            img_local_unchecked_icon.setVisibility(View.VISIBLE);
            img_interior_checked_icon.setVisibility(View.VISIBLE);
            img_interior_unchecked_icon.setVisibility(View.GONE);

            rr_local.setVisibility(View.GONE);
            rr_interior.setVisibility(View.VISIBLE);
            str_local_interior="interior";
            }
        if(v==img_local_unchecked_icon)
        {
            img_local_checked_icon.setVisibility(View.VISIBLE);
            img_local_unchecked_icon.setVisibility(View.GONE);
            img_interior_checked_icon.setVisibility(View.GONE);
            img_interior_unchecked_icon.setVisibility(View.VISIBLE);

            rr_local.setVisibility(View.VISIBLE);
            rr_interior.setVisibility(View.GONE);
            str_local_interior="local";

        }
        if(v==img_interior_checked_icon)
        {
            img_local_checked_icon.setVisibility(View.VISIBLE);
            img_local_unchecked_icon.setVisibility(View.GONE);
            img_interior_checked_icon.setVisibility(View.GONE);
            img_interior_unchecked_icon.setVisibility(View.VISIBLE);

            rr_local.setVisibility(View.VISIBLE);
            rr_interior.setVisibility(View.GONE);

            str_local_interior="local";


        }
        if(v==img_interior_unchecked_icon)
        {
            img_local_checked_icon.setVisibility(View.GONE);
            img_local_unchecked_icon.setVisibility(View.VISIBLE);
            img_interior_checked_icon.setVisibility(View.VISIBLE);
            img_interior_unchecked_icon.setVisibility(View.GONE);

            rr_local.setVisibility(View.GONE);
            rr_interior.setVisibility(View.VISIBLE);
            str_local_interior="interior";

        }


        if(v==img_car_checked_icon)
        {
            img_car_checked_icon.setVisibility(View.GONE);
            img_car_unchecked_icon.setVisibility(View.VISIBLE);
            img_taxi_checked_icon.setVisibility(View.VISIBLE);
            img_taxi_unchecked_icon.setVisibility(View.GONE);

            str_car_bike="Car/Taxi";
        }
        if(v==img_car_unchecked_icon)
        {
            img_car_checked_icon.setVisibility(View.VISIBLE);
            img_car_unchecked_icon.setVisibility(View.GONE);
            img_taxi_checked_icon.setVisibility(View.GONE);
            img_taxi_unchecked_icon.setVisibility(View.VISIBLE);


            str_car_bike="Bike";

        }

        if(v==img_taxi_checked_icon)
        {
            img_car_checked_icon.setVisibility(View.VISIBLE);
            img_car_unchecked_icon.setVisibility(View.GONE);
            img_taxi_checked_icon.setVisibility(View.GONE);
            img_taxi_unchecked_icon.setVisibility(View.VISIBLE);


            str_car_bike="Bike";


        }
        if(v==img_taxi_unchecked_icon)
        {
            img_car_checked_icon.setVisibility(View.GONE);
            img_car_unchecked_icon.setVisibility(View.VISIBLE);
            img_taxi_checked_icon.setVisibility(View.VISIBLE);
            img_taxi_unchecked_icon.setVisibility(View.GONE);


            str_car_bike="Car/Taxi";

        }



        if(v==img_independent_checked_icon)
        {
            //For Independent
            img_independent_checked_icon.setVisibility(View.GONE);
            img_independent_unchecked_icon.setVisibility(View.VISIBLE);
            str_working_with_local_type="";

        }

        if(v==img_independent_unchecked_icon)
        {
            //For Independent
            img_independent_checked_icon.setVisibility(View.VISIBLE);
            img_independent_unchecked_icon.setVisibility(View.GONE);

            //For AreaManger
            img_areamanger_checked_icon.setVisibility(View.GONE);
            img_areamanger_unchecked_icon.setVisibility(View.VISIBLE);

            //For RegionalManger
            img_regionalmanger_checked_icon.setVisibility(View.GONE);
            img_regionalmanger_unchecked_icon.setVisibility(View.VISIBLE);

            //For SalesManger
            img_salesmanger_checked_icon.setVisibility(View.GONE);
            img_salesmanger_unchecked_icon.setVisibility(View.VISIBLE);

            str_working_with_local_type="independent";
            str_workingwith_independent="independent";
        }


        if(v==img_areamanger_checked_icon)
        {
            //For Independent
            img_independent_checked_icon.setVisibility(View.GONE);
            img_independent_unchecked_icon.setVisibility(View.VISIBLE);

            //For AreaManger
            img_areamanger_checked_icon.setVisibility(View.GONE);
            img_areamanger_unchecked_icon.setVisibility(View.VISIBLE);


            if (role_id.equalsIgnoreCase("4"))
            {
                txt_areamanger.setVisibility(View.VISIBLE);
                spinner_area_manager.setVisibility(View.GONE);

                str_working_with_local_type="";
                str_workingwith_area="";
            }
            else  if (role_id.equalsIgnoreCase("3"))
            {
                 txt_areamanger.setVisibility(View.VISIBLE);
                spinner_area_manager.setVisibility(View.GONE);
                str_working_with_local_type="";
                str_workingwith_area="";
            }
            else  if (role_id.equalsIgnoreCase("2"))
            {
                str_marketing_presentative_id="";

                txt_areamanger.setVisibility(View.VISIBLE);
                spinner_area_manager.setVisibility(View.GONE);

                str_working_with_local_type="";
                str_workingwith_area="";
            }
            else
            {
                str_working_with_local_type="";
                str_workingwith_area="";
            }


           /* //For RegionalManger
            img_regionalmanger_checked_icon.setVisibility(View.GONE);
            img_regionalmanger_unchecked_icon.setVisibility(View.VISIBLE);

            //For SalesManger
            img_salesmanger_checked_icon.setVisibility(View.GONE);
            img_salesmanger_unchecked_icon.setVisibility(View.VISIBLE);  */
        }


        if(v==img_areamanger_unchecked_icon)
        {
            //For Independent
            img_independent_checked_icon.setVisibility(View.GONE);
            img_independent_unchecked_icon.setVisibility(View.VISIBLE);

            //For AreaManger
            img_areamanger_checked_icon.setVisibility(View.VISIBLE);
            img_areamanger_unchecked_icon.setVisibility(View.GONE);

            if (role_id.equalsIgnoreCase("4"))
            {

                final String city_id = sp.getData(getActivity(),"selected_city_id_local","null");
                System.out.println("user choosed city ####"+city_id);
                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllMrList(user_id4,city_id);
                    txt_areamanger.setVisibility(View.GONE);
                    spinner_area_manager.setVisibility(View.VISIBLE);
                }
                else
                {
                    txt_show_city_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please select the city.", Toast.LENGTH_SHORT).show();
                }



                str_working_with_local_type="areamanger";
                str_workingwith_area="areamanger";

            }
           else if (role_id.equalsIgnoreCase("3"))
            {

                if (!str_area_manger_id.equalsIgnoreCase(""))
                {
                    final String city_id = sp.getData(getActivity(),"selected_city_id_local","null");
                    System.out.println("user choosed city ####"+city_id);
                    System.out.println("arm id "+str_area_manger_id);

                    if (!city_id.equalsIgnoreCase(""))
                    {
                        getAllMrList(str_area_manger_id,city_id);
                        txt_areamanger.setVisibility(View.GONE);
                        spinner_area_manager.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        txt_show_city_error.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    final String city_id = sp.getData(getActivity(),"selected_city_id_local","null");
                    System.out.println("user choosed city ####"+city_id);
                    System.out.println("rem id "+user_id4);

                    if (!city_id.equalsIgnoreCase(""))
                    {
                        getAllMrByRmList(user_id4, city_id);
                        txt_areamanger.setVisibility(View.GONE);
                        spinner_area_manager.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        txt_show_city_error.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
                    }
                }
                str_working_with_local_type="areamanger";
                str_workingwith_area="areamanger";

            }

            else if (role_id.equalsIgnoreCase("2"))
            {
                if (!str_area_manger_id.equalsIgnoreCase(""))
                {
                    final String city_id = sp.getData(getActivity(),"selected_city_id_local","null");
                    System.out.println("user choosed city ####"+city_id);
                    if (!city_id.equalsIgnoreCase(""))
                    {
                        getAllMrList(str_area_manger_id,city_id);
                        txt_areamanger.setVisibility(View.GONE);
                        spinner_area_manager.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        txt_show_city_error.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please select the city.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    final String city_id = sp.getData(getActivity(),"selected_city_id_local","null");
                    System.out.println("user choosed city ####"+city_id);
                    System.out.println("rem id "+user_id4);

                    if (!city_id.equalsIgnoreCase(""))
                    {
                        getAllMrBySmList(user_id4, city_id);
                        txt_areamanger.setVisibility(View.GONE);
                        spinner_area_manager.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        txt_show_city_error.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
                    }
                }

                str_working_with_local_type="areamanger";
                str_workingwith_area="areamanger";

            }
            else
            {
                str_working_with_local_type="areamanger";
                str_workingwith_area="areamanger";
            }

            /* //For RegionalManger
            img_regionalmanger_checked_icon.setVisibility(View.GONE);
            img_regionalmanger_unchecked_icon.setVisibility(View.VISIBLE);

            //For SalesManger
            img_salesmanger_checked_icon.setVisibility(View.GONE);
            img_salesmanger_unchecked_icon.setVisibility(View.VISIBLE);  */
        }

        if(v==img_regionalmanger_unchecked_icon)
        {
            //For Independent
            img_independent_checked_icon.setVisibility(View.GONE);
            img_independent_unchecked_icon.setVisibility(View.VISIBLE);

       /*     //For AreaManger
            img_areamanger_checked_icon.setVisibility(View.GONE);
            img_areamanger_unchecked_icon.setVisibility(View.VISIBLE);*/

            //For RegionalManger
            img_regionalmanger_checked_icon.setVisibility(View.VISIBLE);
            img_regionalmanger_unchecked_icon.setVisibility(View.GONE);

        if (role_id.equalsIgnoreCase("3"))
        {
            final String city_id = sp.getData(getActivity(),"selected_city_id_local","null");
            System.out.println("user choosed city ####"+city_id);
            if (!city_id.equalsIgnoreCase(""))
            {
                getAllAmList(user_id4,city_id);

                txt_regionalmanger.setVisibility(View.GONE);
                spinner_regional_manager.setVisibility(View.VISIBLE);
            }
            else
            {
                txt_show_city_error.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(), "Please select the city.", Toast.LENGTH_SHORT).show();
            }


            str_working_with_local_type="regionalmanger";
            str_workingwith_regional="regionalmanger";
        }
        else if (role_id.equalsIgnoreCase("2"))
        {

            if (!str_regional_manger_id.equalsIgnoreCase(""))
            {
                final String city_id = sp.getData(getActivity(),"selected_city_id_local","null");
                System.out.println("user choosed city ####"+city_id);
                System.out.println("user str_regional_manger_id ####"+str_regional_manger_id);

                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllAmList(str_regional_manger_id,city_id);
                    txt_regionalmanger.setVisibility(View.GONE);
                    spinner_regional_manager.setVisibility(View.VISIBLE);
                }
                else
                {
                    txt_show_city_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please select the city.", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                final String city_id = sp.getData(getActivity(),"selected_city_id_local","null");
                System.out.println("user choosed city ####"+city_id);
                System.out.println("user user_id4####"+user_id4);

                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllAmListbtSm(user_id4,city_id);
                    txt_regionalmanger.setVisibility(View.GONE);
                    spinner_regional_manager.setVisibility(View.VISIBLE);
                }
                else
                {
                    txt_show_city_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please select the city.", Toast.LENGTH_SHORT).show();
                }

            }
            str_working_with_local_type="regionalmanger";
            str_workingwith_regional="regionalmanger";
        }
        else
        {
            str_working_with_local_type="regionalmanger";
            str_workingwith_regional="regionalmanger";
        }


/*
            //For SalesManger
            img_salesmanger_checked_icon.setVisibility(View.GONE);
            img_salesmanger_unchecked_icon.setVisibility(View.VISIBLE);*/
        }


        if(v==img_regionalmanger_checked_icon)
        {
            //For Independent
            img_independent_checked_icon.setVisibility(View.GONE);
            img_independent_unchecked_icon.setVisibility(View.VISIBLE);

       /*     //For AreaManger
            img_areamanger_checked_icon.setVisibility(View.GONE);
            img_areamanger_unchecked_icon.setVisibility(View.VISIBLE);*/

            //For RegionalManger
            img_regionalmanger_checked_icon.setVisibility(View.GONE);
            img_regionalmanger_unchecked_icon.setVisibility(View.VISIBLE);

            if (role_id.equalsIgnoreCase("3"))
            {
                str_area_manger_id="";

                final String city_id = sp.getData(getActivity(),"selected_city_id_local","null");
                System.out.println("user choosed city ####"+city_id);
                System.out.println("arm id "+str_area_manger_id);

                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllMrByRmList(user_id4, city_id);
                    //txt_areamanger.setVisibility(View.GONE);

                    txt_regionalmanger.setVisibility(View.VISIBLE);
                    spinner_regional_manager.setVisibility(View.GONE);
                }
                else
                {
                    txt_show_city_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
                }

                str_working_with_local_type="";
                str_workingwith_regional="";
            }
            else  if (role_id.equalsIgnoreCase("2"))
            {

                str_area_manger_id="";

                final String city_id = sp.getData(getActivity(),"selected_city_id_local","null");
                System.out.println("user choosed city ####"+city_id);
                System.out.println("arm id "+str_area_manger_id);

                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllMrBySmList(user_id4,city_id);

                    txt_regionalmanger.setVisibility(View.VISIBLE);
                    spinner_regional_manager.setVisibility(View.GONE);
                }
                else
                {
                    txt_show_city_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
                }



                str_working_with_local_type="";
                str_workingwith_regional="";
            }
            else
            {
                str_working_with_local_type="";
                str_workingwith_regional="";
            }




/*
            //For SalesManger
            img_salesmanger_checked_icon.setVisibility(View.GONE);
            img_salesmanger_unchecked_icon.setVisibility(View.VISIBLE);*/
        }


        if(v==img_salesmanger_unchecked_icon)
        {
            //For Independent
            img_independent_checked_icon.setVisibility(View.GONE);
            img_independent_unchecked_icon.setVisibility(View.VISIBLE);

           /* //For AreaManger
            img_areamanger_checked_icon.setVisibility(View.GONE);
            img_areamanger_unchecked_icon.setVisibility(View.VISIBLE);

            //For RegionalManger
            img_regionalmanger_checked_icon.setVisibility(View.GONE);
            img_regionalmanger_unchecked_icon.setVisibility(View.VISIBLE);*/

            //For SalesManger
            img_salesmanger_checked_icon.setVisibility(View.VISIBLE);
            img_salesmanger_unchecked_icon.setVisibility(View.GONE);

            if (role_id.equalsIgnoreCase("2"))
            {
                final String city_id = sp.getData(getActivity(),"selected_city_id_local","null");
                System.out.println("user choosed city ####"+city_id);
                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllRmList(user_id4,city_id);
                    txt_salesmanger.setVisibility(View.GONE);
                    spinner_sales_manager.setVisibility(View.VISIBLE);

                }
                else
                {
                    txt_show_city_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please select the city.", Toast.LENGTH_SHORT).show();
                }

                str_working_with_local_type="salesmanmanger";
                str_workingwith_sales="salesmanmanger";
            }
            else
            {
                str_working_with_local_type="salesmanmanger";
                str_workingwith_sales="salesmanmanger";
            }



        }




        if(v==img_salesmanger_checked_icon)
        {
          //For Independent
            img_independent_checked_icon.setVisibility(View.GONE);
            img_independent_unchecked_icon.setVisibility(View.VISIBLE);

           /* //For AreaManger
            img_areamanger_checked_icon.setVisibility(View.GONE);
            img_areamanger_unchecked_icon.setVisibility(View.VISIBLE);

            //For RegionalManger
            img_regionalmanger_checked_icon.setVisibility(View.GONE);
            img_regionalmanger_unchecked_icon.setVisibility(View.VISIBLE);*/

            //For SalesManger
            img_salesmanger_checked_icon.setVisibility(View.GONE);
            img_salesmanger_unchecked_icon.setVisibility(View.VISIBLE);

            if (role_id.equalsIgnoreCase("2"))
            {
                str_regional_manger_id="";

                final String city_id = sp.getData(getActivity(),"selected_city_id_local","null");
                System.out.println("user choosed city ####"+city_id);
                System.out.println("sm id "+user_id4);

                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllAmListbtSm(user_id4,city_id);
                    txt_salesmanger.setVisibility(View.VISIBLE);
                    spinner_sales_manager.setVisibility(View.GONE);
                }
                else
                {
                    txt_show_city_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
                }

                str_working_with_local_type="";
                str_workingwith_sales="";
            }
            else
            {
                str_working_with_local_type="";
                str_workingwith_sales="";
            }



        }

        if(v==img_independent_interior_unchecked_icon)
        {
            //For Independent interior
            img_independent_interior_checked_icon.setVisibility(View.VISIBLE);
            img_independent_interior_unchecked_icon.setVisibility(View.GONE);

            //For AreaManger interior
            img_areamanger_interior_checked_icon.setVisibility(View.GONE);
            img_areamanger_interior_unchecked_icon.setVisibility(View.VISIBLE);

            //For RegionalManger interior
            img_regionalmanger_interior_checked_icon.setVisibility(View.GONE);
            img_regionalmanger_interior_unchecked_icon.setVisibility(View.VISIBLE);

            //For SalesManger interior
            img_salesmanger_interior_checked_icon.setVisibility(View.GONE);
            img_salesmanger_interior_unchecked_icon.setVisibility(View.VISIBLE);

            str_working_with_interior_type="independent";
            str_workingwith_independent_interior="independent";

        }

        if(v==img_areamanger_interior_unchecked_icon)
        {
            //For Independent interior
            img_independent_interior_checked_icon.setVisibility(View.GONE);
            img_independent_interior_unchecked_icon.setVisibility(View.VISIBLE);

            //For AreaManger interior
            img_areamanger_interior_checked_icon.setVisibility(View.VISIBLE);
            img_areamanger_interior_unchecked_icon.setVisibility(View.GONE);

            if (role_id.equalsIgnoreCase("4"))
            {

                final String city_id = sp.getData(getActivity(),"selected_city_id","null");
                System.out.println("user choosed city ####"+city_id);

                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllMrList(user_id4,city_id);
                    txt_areamanger_interior.setVisibility(View.GONE);
                    spinner_area_manager_interior.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(getActivity(), "Please select the city.", Toast.LENGTH_SHORT).show();
                }


                str_working_with_interior_type="areamangerinterior";
                str_workingwith_area_interior="areamanger";
            }
            else if (role_id.equalsIgnoreCase("3")) {

                if (!str_area_manger_id.equalsIgnoreCase(""))
                {
                    final String city_id = sp.getData(getActivity(),"selected_city_id","null");
                    System.out.println("user choosed city ####"+city_id);
                    System.out.println("arm id "+str_area_manger_id);

                    if (!city_id.equalsIgnoreCase(""))
                    {
                        getAllMrList(str_area_manger_id,city_id);
                        txt_areamanger_interior.setVisibility(View.GONE);
                        spinner_area_manager_interior.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                        txt_city_error.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    final String city_id = sp.getData(getActivity(),"selected_city_id","null");
                    System.out.println("user choosed city ####"+city_id);
                    System.out.println("rem id "+user_id4);

                    if (!city_id.equalsIgnoreCase(""))
                    {
                        getAllMrByRmList(user_id4,city_id);
                        txt_areamanger_interior.setVisibility(View.GONE);
                        spinner_area_manager_interior.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        txt_city_error.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
                    }
                }



                str_working_with_interior_type="areamangerinterior";
                str_workingwith_area_interior="areamanger";
            }
            else if (role_id.equalsIgnoreCase("2")) {

                if (!str_area_manger_id.equalsIgnoreCase(""))
                {
                    final String city_id = sp.getData(getActivity(),"selected_city_id","null");
                    System.out.println("user choosed city ####"+city_id);
                    if (!city_id.equalsIgnoreCase(""))
                    {
                        getAllMrList(str_area_manger_id, city_id);
                        txt_areamanger_interior.setVisibility(View.GONE);
                        spinner_area_manager_interior.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        txt_city_error.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please select the city.", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    final String city_id = sp.getData(getActivity(),"selected_city_id","null");
                    System.out.println("user choosed city12 ####"+city_id);
                    System.out.println("sem id "+user_id4);

                    if (!city_id.equalsIgnoreCase(""))
                    {
                        getAllMrBySmList(user_id4, city_id);
                        txt_areamanger_interior.setVisibility(View.GONE);
                        spinner_area_manager_interior.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        txt_city_error.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
                    }
                }

                str_working_with_interior_type="areamangerinterior";
                str_workingwith_area_interior="areamanger";
            }

            else
            {
                str_working_with_interior_type="areamangerinterior";
                str_workingwith_area_interior="areamanger";
            }


            /*//For RegionalManger interior
            img_regionalmanger_interior_checked_icon.setVisibility(View.GONE);
            img_regionalmanger_interior_unchecked_icon.setVisibility(View.VISIBLE);

            //For SalesManger interior
            img_salesmanger_interior_checked_icon.setVisibility(View.GONE);
            img_salesmanger_interior_checked_icon.setVisibility(View.VISIBLE);*/
        }


        if(v==img_areamanger_interior_checked_icon)
        {
            //For Independent interior
            img_independent_interior_checked_icon.setVisibility(View.GONE);
            img_independent_interior_unchecked_icon.setVisibility(View.VISIBLE);

            //For AreaManger interior
            img_areamanger_interior_checked_icon.setVisibility(View.GONE);
            img_areamanger_interior_unchecked_icon.setVisibility(View.VISIBLE);

            if (role_id.equalsIgnoreCase("4"))
            {

                txt_areamanger_interior.setVisibility(View.VISIBLE);
                spinner_area_manager_interior.setVisibility(View.GONE);
                str_working_with_interior_type="";
                str_workingwith_area_interior="";
            }

            else if (role_id.equalsIgnoreCase("3")) {


                txt_areamanger_interior.setVisibility(View.VISIBLE);
                spinner_area_manager_interior.setVisibility(View.GONE);
                str_working_with_interior_type="";
                str_workingwith_area_interior="";
            }
            else if (role_id.equalsIgnoreCase("2")) {

                str_marketing_presentative_id="";

                /*final String city_id = sp.getData(getActivity(),"selected_city_id","null");
                System.out.println("user choosed city ####"+city_id);
                System.out.println("rem id "+user_id4);

                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllMrBySmList(user_id4, city_id);
                    txt_areamanger_interior.setVisibility(View.VISIBLE);
                    spinner_area_manager_interior.setVisibility(View.GONE);
                }
                else
                {
                    txt_city_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
                }*/
                txt_areamanger_interior.setVisibility(View.VISIBLE);
                spinner_area_manager_interior.setVisibility(View.GONE);

                str_working_with_interior_type="";
                str_workingwith_area_interior="";

            }
            else
            {
                str_working_with_interior_type="";
                str_workingwith_area_interior="";
            }






            /*//For RegionalManger interior
            img_regionalmanger_interior_checked_icon.setVisibility(View.GONE);
            img_regionalmanger_interior_unchecked_icon.setVisibility(View.VISIBLE);

            //For SalesManger interior
            img_salesmanger_interior_checked_icon.setVisibility(View.GONE);
            img_salesmanger_interior_checked_icon.setVisibility(View.VISIBLE);*/
        }



        if(v==img_regionalmanger_interior_unchecked_icon)
        {
            //For Independent interior
            img_independent_interior_checked_icon.setVisibility(View.GONE);
            img_independent_interior_unchecked_icon.setVisibility(View.VISIBLE);

  /*          //For AreaManger interior
            img_areamanger_interior_checked_icon.setVisibility(View.GONE);
            img_areamanger_interior_unchecked_icon.setVisibility(View.VISIBLE);*/

            //For RegionalManger interior
            img_regionalmanger_interior_checked_icon.setVisibility(View.VISIBLE);
            img_regionalmanger_interior_unchecked_icon.setVisibility(View.GONE);


            if (role_id.equalsIgnoreCase("3"))
            {

                final String city_id = sp.getData(getActivity(),"selected_city_id","null");
                System.out.println("user choosed city ####"+city_id);
                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllAmList(user_id4,city_id);
                    txt_regionalmanger_interior.setVisibility(View.GONE);
                    spinner_regionalmanger_interior.setVisibility(View.VISIBLE);
                }
                else
                {
                    txt_city_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please select the city.", Toast.LENGTH_SHORT).show();
                }

                str_working_with_interior_type="regionalmangerinterior";
                str_workingwith_regional_interior="regionalmanger";
            }
            else if (role_id.equalsIgnoreCase("2"))
            {

                if (!str_regional_manger_id.equalsIgnoreCase(""))
                {
                    final String city_id = sp.getData(getActivity(),"selected_city_id","null");
                    System.out.println("user choosed city ####"+city_id);
                    System.out.println("user str_regional_manger_id ####"+str_regional_manger_id);

                    if (!city_id.equalsIgnoreCase(""))
                    {
                        getAllAmList(str_regional_manger_id,city_id);
                        txt_regionalmanger_interior.setVisibility(View.GONE);
                        spinner_regionalmanger_interior.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        txt_city_error.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please select the city.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    final String city_id = sp.getData(getActivity(),"selected_city_id","null");
                    System.out.println("user choosed city ####"+city_id);
                    System.out.println("user user_id4####"+user_id4);

                    if (!city_id.equalsIgnoreCase(""))
                    {
                        getAllAmListbtSm(user_id4,city_id);
                        txt_regionalmanger_interior.setVisibility(View.GONE);
                        spinner_regionalmanger_interior.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        txt_city_error.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please select the city.", Toast.LENGTH_SHORT).show();
                    }

                }

                str_working_with_interior_type="regionalmangerinterior";
                str_workingwith_regional_interior="regionalmanger";
            }
            else
            {
                str_working_with_interior_type="regionalmangerinterior";
                str_workingwith_regional_interior="regionalmanger";
            }


           // str_working_with_interior_type="regionalmangerinterior";
            //str_workingwith_regional_interior="regionalmanger";

        /*    //For SalesManger interior
            img_salesmanger_interior_checked_icon.setVisibility(View.GONE);
            img_salesmanger_interior_checked_icon.setVisibility(View.VISIBLE);*/
        }


        if(v==img_regionalmanger_interior_checked_icon)
        {
            //For Independent interior
            img_independent_interior_checked_icon.setVisibility(View.GONE);
            img_independent_interior_unchecked_icon.setVisibility(View.VISIBLE);

  /*          //For AreaManger interior
            img_areamanger_interior_checked_icon.setVisibility(View.GONE);
            img_areamanger_interior_unchecked_icon.setVisibility(View.VISIBLE);*/

            //For RegionalManger interior
            img_regionalmanger_interior_checked_icon.setVisibility(View.GONE);
            img_regionalmanger_interior_unchecked_icon.setVisibility(View.VISIBLE);

            if (role_id.equalsIgnoreCase("3"))
            {
                str_area_manger_id="";

                final String city_id = sp.getData(getActivity(),"selected_city_id","null");
                System.out.println("user choosed city ####"+city_id);
                System.out.println("null arm id "+str_area_manger_id);

                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllMrByRmList(user_id4, city_id);
                    txt_areamanger_interior.setVisibility(View.GONE);
                    spinner_area_manager_interior.setVisibility(View.VISIBLE);
                }
                else
                {
                    txt_city_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
                }

                txt_regionalmanger_interior.setVisibility(View.VISIBLE);
                spinner_regionalmanger_interior.setVisibility(View.GONE);


                str_working_with_interior_type="";
                str_workingwith_regional_interior="";
            }

            if (role_id.equalsIgnoreCase("2"))
            {
                str_area_manger_id="";

                final String city_id = sp.getData(getActivity(),"selected_city_id","null");
                System.out.println("user choosed city ####"+city_id);
                System.out.println("null arm id "+str_area_manger_id);

                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllMrBySmList(user_id4, city_id);
                    txt_areamanger_interior.setVisibility(View.GONE);
                    spinner_area_manager_interior.setVisibility(View.VISIBLE);
                }
                else
                {
                    txt_city_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
                }
                txt_regionalmanger_interior.setVisibility(View.VISIBLE);
                spinner_regionalmanger_interior.setVisibility(View.GONE);

                str_working_with_interior_type="";
                str_workingwith_regional_interior="";


            }
            else
            {
                str_working_with_interior_type="";
                str_workingwith_regional_interior="";
            }
            //str_working_with_interior_type="";
            //str_workingwith_regional_interior="";

        /*    //For SalesManger interior
            img_salesmanger_interior_checked_icon.setVisibility(View.GONE);
            img_salesmanger_interior_checked_icon.setVisibility(View.VISIBLE);*/
        }


        if(v==img_salesmanger_interior_unchecked_icon)
        {
            //For Independent interior
            img_independent_interior_checked_icon.setVisibility(View.GONE);
            img_independent_interior_unchecked_icon.setVisibility(View.VISIBLE);

         /*   //For AreaManger interior
            img_areamanger_interior_checked_icon.setVisibility(View.GONE);
            img_areamanger_interior_unchecked_icon.setVisibility(View.VISIBLE);

            //For RegionalManger interior
            img_regionalmanger_interior_checked_icon.setVisibility(View.GONE);
            img_regionalmanger_interior_unchecked_icon.setVisibility(View.VISIBLE);  */

            //For SalesManger interior
            img_salesmanger_interior_checked_icon.setVisibility(View.VISIBLE);
            img_salesmanger_interior_unchecked_icon.setVisibility(View.GONE);

            if (role_id.equalsIgnoreCase("2"))
            {
                final String city_id = sp.getData(getActivity(),"selected_city_id","null");
                System.out.println("user choosed city ####"+city_id);
                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllRmList(user_id4,city_id);
                    txt_salesmanger_interior.setVisibility(View.GONE);
                    spinner_sales_manager_interior.setVisibility(View.VISIBLE);
                }
                else
                {
                    txt_city_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please select the city.", Toast.LENGTH_SHORT).show();
                }

                str_working_with_interior_type="salesmanmangerinterior";
                str_workingwith_sales_interior="salesmanger";
            }
            else
            {
                str_working_with_interior_type="salesmanmangerinterior";
                str_workingwith_sales_interior="salesmanger";
            }



        }

        if(v==img_independent_interior_checked_icon)
        {
            //For Independent interior
            img_independent_interior_checked_icon.setVisibility(View.GONE);
            img_independent_interior_unchecked_icon.setVisibility(View.VISIBLE);

           /* //For AreaManger interior
            img_areamanger_interior_checked_icon.setVisibility(View.GONE);
            img_areamanger_interior_unchecked_icon.setVisibility(View.VISIBLE);

            //For RegionalManger interior
            img_regionalmanger_interior_checked_icon.setVisibility(View.GONE);
            img_regionalmanger_interior_unchecked_icon.setVisibility(View.VISIBLE);

            //For SalesManger interior
            img_salesmanger_interior_checked_icon.setVisibility(View.GONE);
            img_salesmanger_interior_unchecked_icon.setVisibility(View.VISIBLE);*/

            str_working_with_interior_type="";
            str_workingwith_independent_interior="";

        }



        if(v==img_salesmanger_interior_checked_icon)
        {
            //For Independent interior
            img_independent_interior_checked_icon.setVisibility(View.GONE);
            img_independent_interior_unchecked_icon.setVisibility(View.VISIBLE);

        /*    //For AreaManger interior
            img_areamanger_interior_checked_icon.setVisibility(View.GONE);
            img_areamanger_interior_unchecked_icon.setVisibility(View.VISIBLE);

            //For RegionalManger interior
            img_regionalmanger_interior_checked_icon.setVisibility(View.GONE);
            img_regionalmanger_interior_unchecked_icon.setVisibility(View.VISIBLE);  */

            //For SalesManger interior
            img_salesmanger_interior_checked_icon.setVisibility(View.GONE);
            img_salesmanger_interior_unchecked_icon.setVisibility(View.VISIBLE);

            if (role_id.equalsIgnoreCase("2"))
            {
                str_regional_manger_id="";

                final String city_id = sp.getData(getActivity(),"selected_city_id","null");
                System.out.println("user choosed city ####"+city_id);
                System.out.println("rem id "+user_id4);

                if (!city_id.equalsIgnoreCase(""))
                {
                    getAllAmListbtSm(user_id4,city_id);
                    txt_salesmanger_interior.setVisibility(View.VISIBLE);
                    spinner_sales_manager_interior.setVisibility(View.GONE);
                }
                else
                {
                    txt_city_error.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please select city.", Toast.LENGTH_SHORT).show();
                }

                str_working_with_interior_type="";
                str_workingwith_sales_interior="";
            }
            else
            {
                str_working_with_interior_type="";
                str_workingwith_sales_interior="";
            }


        }

        if(v==btn_start_local)
        {

            //For Validation
            Validate1();

         /*   getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, new TimeLineFragment())
                    .addToBackStack("21")
                    .commit();*/
        }

        if(v==btn_start_interior)
        {

            Validate2();
           /* getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, new TimeLineFragment())
                    .addToBackStack("21")
                    .commit();*/
        }


    }

    // ---------------------------- For  Validation -------------------------------------------------------------------------------//

    private void Validate1() {

        String str_selct_city = edt_select_city_local.getText().toString();
        String str_selct_interior = edt_select_interior_local.getText().toString();
        boolean iserror = false;

        System.out.println("local type area###" + str_working_with_local_type + str_workingwith_regional);

    if (role_id.equalsIgnoreCase("5")) {

     if (!str_workingwith_regional.equalsIgnoreCase("") && regional_manager_user_id.equalsIgnoreCase("0"))
       {
           iserror=true;
           Toast.makeText(getActivity(), "No Regional Manager", Toast.LENGTH_SHORT).show();
       }
       else if (!str_workingwith_sales.equalsIgnoreCase("") && sales_manager_user_id.equalsIgnoreCase("0"))
       {
           iserror=true;
           Toast.makeText(getActivity(), "No Sales Manager", Toast.LENGTH_SHORT).show();
       }
       else if (!str_workingwith_area.equalsIgnoreCase("") && area_manager_user_id.equalsIgnoreCase("0"))
       {
           iserror=true;
           Toast.makeText(getActivity(), "No Area Manager", Toast.LENGTH_SHORT).show();
       }
    }

       if (role_id.equalsIgnoreCase("4"))
       {
           System.out.println("soutt ++"+str_area_manger_id);
           if(str_selct_city.equalsIgnoreCase(""))
           {
               iserror=true;
               txt_show_city_error.setVisibility(View.VISIBLE);
           }
           else
           {
               txt_show_city_error.setVisibility(View.GONE);
           }

           if (!str_workingwith_regional.equalsIgnoreCase("") && regional_manager_user_id.equalsIgnoreCase("0"))
           {
               iserror=true;
               Toast.makeText(getActivity(), "No Regional Manager", Toast.LENGTH_SHORT).show();
           }
           else  if (!str_workingwith_sales.equalsIgnoreCase("") && sales_manager_user_id.equalsIgnoreCase("0"))
           {
               iserror=true;
               Toast.makeText(getActivity(), "No Sales Manager", Toast.LENGTH_SHORT).show();
           }
           else if (!str_workingwith_area.equalsIgnoreCase("") && str_area_manger_id.equalsIgnoreCase("")
                   && str_area_manger_id.equalsIgnoreCase("0"))
           {
               iserror=true;
               Toast.makeText(getActivity(), "No MR", Toast.LENGTH_SHORT).show();
           }

       }

       if (role_id.equalsIgnoreCase("3"))
       {

         if (!str_workingwith_sales_interior.equalsIgnoreCase("") && sales_manager_user_id.equalsIgnoreCase("0"))
         {
           iserror=true;
           Toast.makeText(getActivity(), "No Sales Manager", Toast.LENGTH_SHORT).show();
         }

          if(str_selct_city.equalsIgnoreCase(""))
           {
               iserror=true;
               txt_show_city_error.setVisibility(View.VISIBLE);
           }


           else
           {
               txt_show_city_error.setVisibility(View.GONE);
           }


          /* if(str_selct_interior.equalsIgnoreCase(""))
           {
               iserror=true;
               txt_show_interior_error.setVisibility(View.VISIBLE);
           }


           else
           {
               txt_show_interior_error.setVisibility(View.GONE);

           }*/
       }
        if (role_id.equalsIgnoreCase("2")) {

            System.out.println("local sale type###" + sales_manager_user_id + str_regional_manger_id + str_area_manger_id);


            if (str_selct_city.equalsIgnoreCase("")) {
                iserror = true;
                txt_show_city_error.setVisibility(View.VISIBLE);
            }
            else {
                txt_show_city_error.setVisibility(View.GONE);
            }


            if (!str_workingwith_regional.equalsIgnoreCase("") && str_area_manger_id.equalsIgnoreCase("0"))
            {
                iserror=true;
                Toast.makeText(getActivity(), "No Area Manager", Toast.LENGTH_SHORT).show();
            }
            else  if (!str_workingwith_sales.equalsIgnoreCase("") && str_regional_manger_id.equalsIgnoreCase("0"))
            {
                iserror=true;
                Toast.makeText(getActivity(), "No Regional Manager", Toast.LENGTH_SHORT).show();
            }
            else if (!str_workingwith_area.equalsIgnoreCase("") && str_marketing_presentative_id.equalsIgnoreCase("0"))
            {
                iserror=true;
                Toast.makeText(getActivity(), "No MR", Toast.LENGTH_SHORT).show();
            }
        }


if(str_state_local_name==null)
{
      iserror=true;
    txt_current_loc_error.setVisibility(View.VISIBLE);
    txt_current_loc_error.setText("State and City name is required.");
}


else  if(str_state_local_name.equalsIgnoreCase(""))
        {
          iserror=true;
            txt_current_loc_error.setVisibility(View.VISIBLE);
            txt_current_loc_error.setText("State and City name is required.");

        }

        else
        {
            txt_current_loc_error.setVisibility(View.GONE);

        }


  if(str_working_with_local_type.equalsIgnoreCase("") &&
          str_workingwith_area.equalsIgnoreCase("")
          && str_workingwith_sales.equalsIgnoreCase("")
          && str_workingwith_regional.equalsIgnoreCase(""))
  {
     iserror=true;
     txt_working_with_error.setVisibility(View.VISIBLE);
     txt_working_with_error.setText("Please Select One Field");
  }
    else
    {
       txt_working_with_error.setVisibility(View.GONE);
    }


        if(!iserror)
        {
            txt_current_loc_error.setVisibility(View.GONE);
            txt_working_with_error.setVisibility(View.GONE);

            System.out.println("Working area###"+str_workingwith_area);
            System.out.println("Working regional###"+str_workingwith_regional);
            System.out.println("Working Sales###"+str_workingwith_sales);
            System.out.println("mr id ###"+str_marketing_presentative_id);

          //For Independent
            if(str_working_with_local_type.equalsIgnoreCase("independent"))
            {

                callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "1", "", "", "",
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

            }

                //For Multiple Check

               if (!str_workingwith_area.equalsIgnoreCase("") &&
                        !str_workingwith_regional.equalsIgnoreCase("") &&
                        !str_workingwith_sales.equalsIgnoreCase("")) {

                   if (role_id.equalsIgnoreCase("4")) {

                       System.out.println("cittttttt" + str_city_id);
                       callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", "", str_marketing_presentative_id, regional_manager_user_id,
                               sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                   }
                  else if (role_id.equalsIgnoreCase("3")) {

                       System.out.println("cittttttt" + str_city_id);
                       callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", str_area_manger_id, str_marketing_presentative_id, "",
                               sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                   }
                   else if (role_id.equalsIgnoreCase("2")) {

                       System.out.println("cittttttt" + regional_manager_user_id);
                       callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", str_area_manger_id, str_marketing_presentative_id, str_regional_manger_id,
                               "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                   }
                   else {
                       callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", area_manager_user_id, "", regional_manager_user_id,
                               sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                   }
                   /* callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", area_manager_user_id, "", regional_manager_user_id,
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }*/
               }

                // For Two Check

                if (str_workingwith_area.equalsIgnoreCase("") &&
                        !str_workingwith_regional.equalsIgnoreCase("") &&
                        !str_workingwith_sales.equalsIgnoreCase("")) {


                    if (role_id.equalsIgnoreCase("3"))
                    {
                        callWebservicefor_Start("local", user_id4, str_state_id, str_city_id,
                                "Field Work", "0", "0", str_area_manger_id, "",
                                "",
                                sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                    }
                    else if (role_id.equalsIgnoreCase("2"))
                    {
                        callWebservicefor_Start("local", user_id4, str_state_id, str_city_id,
                                "Field Work", "0", "0", str_area_manger_id, "",
                                str_regional_manger_id,
                                "", txt_current_loc_descp.getText().toString(),
                                String.valueOf(currentlat), String.valueOf(currentlong));

                    }
                    else
                    {
                        callWebservicefor_Start("local", user_id4, str_state_id, str_city_id,
                                "Field Work", "0", "0", "", "", regional_manager_user_id,
                                sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                    }

                }




            if (!str_workingwith_area.equalsIgnoreCase("") &&
                        str_workingwith_regional.equalsIgnoreCase("") &&
                        !str_workingwith_sales.equalsIgnoreCase("")) {

                if (role_id.equalsIgnoreCase("4")) {
                    callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id,
                            "Field Work", "0", "0", "", str_marketing_presentative_id, "",
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                }
               else if (role_id.equalsIgnoreCase("3")) {
                    callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id,
                            "Field Work", "0", "0", "", str_marketing_presentative_id, "",
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                }

                else if (role_id.equalsIgnoreCase("2")) {
                    callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id,
                            "Field Work", "0", "0", "", str_marketing_presentative_id, regional_manager_user_id,
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                }

                else {

                    callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id,
                            "Field Work", "0", "0", area_manager_user_id, "", "",
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat),
                            String.valueOf(currentlong));
                }
            }





                if (!str_workingwith_area.equalsIgnoreCase("") &&
                        !str_workingwith_regional.equalsIgnoreCase("") &&
                        str_workingwith_sales.equalsIgnoreCase("")) {

                    if (role_id.equalsIgnoreCase("4")) {
                        callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", "", str_marketing_presentative_id, regional_manager_user_id,
                                "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                    }
                    else if (role_id.equalsIgnoreCase("3")) {
                        callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0",
                                "0", str_area_manger_id, str_marketing_presentative_id, "",
                                "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                    }
                    else if (role_id.equalsIgnoreCase("2")) {
                        callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0",
                                "0", str_area_manger_id, str_marketing_presentative_id, "",
                                "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                    }

                    else {

                        callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", area_manager_user_id, "", regional_manager_user_id,
                                "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                    }
                }


            //For Single Check

                if (!str_workingwith_area.equalsIgnoreCase("") &&
                        str_workingwith_regional.equalsIgnoreCase("") &&
                        str_workingwith_sales.equalsIgnoreCase("")) {

                    if (role_id.equalsIgnoreCase("4")) {
                        callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", "", str_marketing_presentative_id, "",
                                "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                    }

                   else if (role_id.equalsIgnoreCase("3")) {
                        callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", "", str_marketing_presentative_id, "",
                                "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                    }
                    else if (role_id.equalsIgnoreCase("2")) {
                        callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", "", str_marketing_presentative_id, "",
                                "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                    }

                    else {
                        callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", area_manager_user_id, "", "",
                                "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                    }

                }

                if (str_workingwith_area.equalsIgnoreCase("") &&
                        !str_workingwith_regional.equalsIgnoreCase("") &&
                        str_workingwith_sales.equalsIgnoreCase("")) {

                   if (role_id.equalsIgnoreCase("3"))
                   {
                       callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", str_area_manger_id, "", "",
                               "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                   }
                   else if (role_id.equalsIgnoreCase("2"))
                   {
                       callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", str_area_manger_id, "", "",
                               "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                   }
                   else
                   {
                       callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", "", "", regional_manager_user_id,
                               "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                   }


                }


                System.out.println("SalesMan###"+str_workingwith_sales);
                if (str_workingwith_area.equalsIgnoreCase("") &&
                        str_workingwith_regional.equalsIgnoreCase("") &&
                        !str_workingwith_sales.equalsIgnoreCase("")) {

                    if (role_id.equalsIgnoreCase("2"))
                    {
                        callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", "", "", regional_manager_user_id,
                                "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                    }
                    else
                    {
                        callWebservicefor_Start("Local", user_id4, str_state_id, str_city_id, "Field Work", "0", "0", "", "", "",
                                sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                    }



            }



        }



    }


    // ---------------------------- For  Validation2 -------------------------------------------------------------------------------//

    private void Validate2() {
        String str_selct_territory=edt_selct_territory.getText().toString();
        String str_selct_city=edt_selct_city.getText().toString();
        String str_selct_interior=edt_selct_interior.getText().toString();

        boolean iserror=false;

        if (role_id.equalsIgnoreCase("5")) {

            if (!str_workingwith_regional_interior.equalsIgnoreCase("") && regional_manager_user_id.equalsIgnoreCase("0"))
            {
                iserror=true;
                Toast.makeText(getActivity(), "No Regional Manager", Toast.LENGTH_SHORT).show();
            }
            else  if (!str_workingwith_sales_interior.equalsIgnoreCase("") && sales_manager_user_id.equalsIgnoreCase("0"))
            {
                iserror=true;
                Toast.makeText(getActivity(), "No Sales Manager", Toast.LENGTH_SHORT).show();
            }
            else if (!str_workingwith_area_interior.equalsIgnoreCase("") && area_manager_user_id.equalsIgnoreCase("0"))
            {
                iserror=true;
                Toast.makeText(getActivity(), "No Area Manager", Toast.LENGTH_SHORT).show();
            }
        }


        if (role_id.equalsIgnoreCase("4"))
        {
            if (!str_workingwith_regional_interior.equalsIgnoreCase("") && regional_manager_user_id.equalsIgnoreCase("0"))
            {
                iserror=true;
                Toast.makeText(getActivity(), "No Regional Manager", Toast.LENGTH_SHORT).show();
            }
            else  if (!str_workingwith_sales_interior.equalsIgnoreCase("") && sales_manager_user_id.equalsIgnoreCase("0"))
            {
                iserror=true;
                Toast.makeText(getActivity(), "No Sales Manager", Toast.LENGTH_SHORT).show();
            }
           /*else if (!str_workingwith_area.equalsIgnoreCase("") && area_manager_user_id.equalsIgnoreCase("0"))
           {
               iserror=true;
               Toast.makeText(getActivity(), "No MR", Toast.LENGTH_SHORT).show();
           }*/

        }

        if (role_id.equalsIgnoreCase("3")) {

            if (!str_workingwith_sales_interior.equalsIgnoreCase("") && sales_manager_user_id.equalsIgnoreCase("0")) {
                iserror = true;
                Toast.makeText(getActivity(), "No Sales Manager", Toast.LENGTH_SHORT).show();
            }

        }

        if (role_id.equalsIgnoreCase("2")) {

            if (!str_workingwith_regional_interior.equalsIgnoreCase("") && str_area_manger_id.equalsIgnoreCase("0"))
            {
                iserror=true;
                Toast.makeText(getActivity(), "No Area Manager", Toast.LENGTH_SHORT).show();
            }
            else  if (!str_workingwith_sales_interior.equalsIgnoreCase("") && str_regional_manger_id.equalsIgnoreCase("0"))
            {
                iserror=true;
                Toast.makeText(getActivity(), "No Regional Manager", Toast.LENGTH_SHORT).show();
            }
            else if (!str_workingwith_area_interior.equalsIgnoreCase("") && str_marketing_presentative_id.equalsIgnoreCase("0"))
            {
                iserror=true;
                Toast.makeText(getActivity(), "No MR", Toast.LENGTH_SHORT).show();
            }
        }



   /*     if(str_selct_territory.equalsIgnoreCase(""))
        {
            iserror=true;
            txt_territory_error.setVisibility(View.VISIBLE);

        }
        else
        {
            txt_territory_error.setVisibility(View.GONE);
        }*/

        if(str_selct_city.equalsIgnoreCase(""))
        {
            iserror=true;
            txt_city_error.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_city_error.setVisibility(View.GONE);

        }

        if(str_selct_interior.equalsIgnoreCase(""))
        {
            iserror=true;
            txt_working_with_error_interior.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_working_with_error_interior.setVisibility(View.GONE);

        }




        if(str_working_with_interior_type.equalsIgnoreCase("")
                && str_workingwith_area_interior.equalsIgnoreCase("")
                && str_workingwith_regional_interior.equalsIgnoreCase("")
                && str_workingwith_sales_interior.equalsIgnoreCase(""))
        {
            iserror=true;
            txt_working_with_errorr_interior.setVisibility(View.VISIBLE);
            txt_working_with_errorr_interior.setText("Please Select One Field");

        }

        else
        {
            txt_working_with_errorr_interior.setVisibility(View.GONE);
        }


        if(!iserror)
        {
            txt_working_with_errorr_interior.setVisibility(View.GONE);
            txt_city_error.setVisibility(View.GONE);
            txt_working_with_error_interior.setVisibility(View.GONE);

            System.out.println("Working area###"+str_workingwith_area_interior);
            System.out.println("Working regional###"+str_workingwith_regional_interior);
            System.out.println("Working Sales###"+str_workingwith_sales_interior);

            System.out.println("mr id ###"+str_marketing_presentative_id);

//For Independent
            if(str_working_with_interior_type.equalsIgnoreCase("independent"))
            {

                callWebservicefor_StartInterior("Interior" ,user_id4,str_state_id,str_city_id,"Field Work",str_interiror_id,"1","","","",
                        "",txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
            }

            //For Multiple Check

            if (!str_workingwith_area_interior.equalsIgnoreCase("") &&
                    !str_workingwith_regional_interior.equalsIgnoreCase("") &&
                    !str_workingwith_sales_interior.equalsIgnoreCase("")) {


                if (role_id.equalsIgnoreCase("4"))
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", "", str_marketing_presentative_id, regional_manager_user_id,
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }
                else if (role_id.equalsIgnoreCase("2")) {

                    System.out.println("cittttttt" + regional_manager_user_id);
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", str_area_manger_id, str_marketing_presentative_id, str_regional_manger_id,
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                }
                else if (role_id.equalsIgnoreCase("3")) {

                    System.out.println("cittttttt" + str_city_id);
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", str_area_manger_id, str_marketing_presentative_id, "",
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                }
                else
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", area_manager_user_id, "", regional_manager_user_id,
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }

            }




            // For Two Check

            if (str_workingwith_area_interior.equalsIgnoreCase("") &&
                    !str_workingwith_regional_interior.equalsIgnoreCase("") &&
                    !str_workingwith_sales_interior.equalsIgnoreCase("")) {


                if (role_id.equalsIgnoreCase("3"))
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id,
                            "Field Work", str_interiror_id, "0", str_area_manger_id, "",
                            "",
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                }
                else if (role_id.equalsIgnoreCase("2"))
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id,
                            "Field Work", str_interiror_id, "0", str_area_manger_id, "",
                            str_regional_manger_id,
                            "", txt_current_loc_descp.getText().toString(),
                            String.valueOf(currentlat), String.valueOf(currentlong));

                }
                else
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id,
                            "Field Work", str_interiror_id, "0", "", "", regional_manager_user_id,
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }


            }




            if (!str_workingwith_area_interior.equalsIgnoreCase("") &&
                    str_workingwith_regional_interior.equalsIgnoreCase("") &&
                    !str_workingwith_sales_interior.equalsIgnoreCase("")) {

                if (role_id.equalsIgnoreCase("4"))
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id,
                            "Field Work", str_interiror_id, "0", "", str_marketing_presentative_id, "",
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }
                else if (role_id.equalsIgnoreCase("3")) {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id,
                            "Field Work", str_interiror_id, "0", "", str_marketing_presentative_id, "",
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                }
                else if (role_id.equalsIgnoreCase("2")) {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id,
                            "Field Work", str_interiror_id, "0", "", str_marketing_presentative_id, regional_manager_user_id,
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                }
                else
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id,
                            "Field Work", str_interiror_id, "0", area_manager_user_id, "", "",
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }


            }



            if (!str_workingwith_area_interior.equalsIgnoreCase("") &&
                    !str_workingwith_regional_interior.equalsIgnoreCase("") &&
                    str_workingwith_sales_interior.equalsIgnoreCase("")) {

                if (role_id.equalsIgnoreCase("4")) {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", "", str_marketing_presentative_id, regional_manager_user_id,
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }
                else if (role_id.equalsIgnoreCase("3")) {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id,
                            "0", str_area_manger_id, str_marketing_presentative_id, "",
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }
                else if (role_id.equalsIgnoreCase("2")) {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id,
                            "0", str_area_manger_id, str_marketing_presentative_id, "",
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }
                else {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", area_manager_user_id, "", regional_manager_user_id,
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }
            }


            //For Single Check

            if (!str_workingwith_area_interior.equalsIgnoreCase("") &&
                    str_workingwith_regional_interior.equalsIgnoreCase("") &&
                    str_workingwith_sales_interior.equalsIgnoreCase("")) {

                if (role_id.equalsIgnoreCase("4")) {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", "", str_marketing_presentative_id, "",
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }
                else if (role_id.equalsIgnoreCase("3"))
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id,
                            "0", "", str_marketing_presentative_id, "",
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }
                else if (role_id.equalsIgnoreCase("2"))
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id,
                            "0", "", str_marketing_presentative_id, "",
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }
                else
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", area_manager_user_id, "", "",
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }

            }



            if (str_workingwith_area_interior.equalsIgnoreCase("") &&
                    !str_workingwith_regional_interior.equalsIgnoreCase("") &&
                    str_workingwith_sales_interior.equalsIgnoreCase("")) {

                if (role_id.equalsIgnoreCase("3"))
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", str_area_manger_id, "", "",
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }
                if (role_id.equalsIgnoreCase("2"))
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", str_area_manger_id, "", "",
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }
                else
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", "", "", regional_manager_user_id,
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }


            }


            System.out.println("SalesMan###"+str_workingwith_sales);
            if (str_workingwith_area_interior.equalsIgnoreCase("") &&
                    str_workingwith_regional_interior.equalsIgnoreCase("") &&
                    !str_workingwith_sales_interior.equalsIgnoreCase("")) {


                if (role_id.equalsIgnoreCase("3"))
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", "", "", "",
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }
                if (role_id.equalsIgnoreCase("2"))
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", "", "", regional_manager_user_id,
                            "", txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));
                }
                else
                {
                    callWebservicefor_StartInterior("Interior", user_id4, str_state_id, str_city_id, "Field Work", str_interiror_id, "0", "", "", "",
                            sales_manager_user_id, txt_current_loc_descp.getText().toString(), String.valueOf(currentlat), String.valueOf(currentlong));

                }

            }



        }



    }
    // ---------------------------- RecycleView For Select Territory -------------------------------------------------------------------------------//
    private class CustomStart_Field_Work_Interior_Slect_Territory extends RecyclerView.Adapter<CustomStart_Field_Work_Interior_Slect_Territory.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<StateList_LxDetails_Bean> arr_statelist_lxdetails3;
        List<StateList_LxDetails_Bean> arrayList = new ArrayList<>();
        List<StateList_LxDetails_Bean> arSearchlist;

        public CustomStart_Field_Work_Interior_Slect_Territory(Activity activity, List<StateList_LxDetails_Bean> arr_statelist_lxdetails2) {

            this.activity = activity;

            this.arr_statelist_lxdetails3 =arr_statelist_lxdetails2;

            this.arrayList = arr_statelist_lxdetails2;
            this.arSearchlist = new ArrayList<StateList_LxDetails_Bean>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size###"+arr_statelist_lxdetails2.size());
        }


        @Override
        public CustomStart_Field_Work_Interior_Slect_Territory.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.searc_result, parent, false);

            return new CustomStart_Field_Work_Interior_Slect_Territory.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomStart_Field_Work_Interior_Slect_Territory.MyViewHolder holder, final int position) {


            System.out.println("List Size in Bind###"+arrayList.size());
            holder.txt_territory_name.setText(arrayList.get(position).getName());
            str_state_id= String.valueOf(arrayList.get(position).getId());

            holder.rr_first_descp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if (str_local_interior.equalsIgnoreCase("interior"))
        {
            edt_selct_territory.setText(arrayList.get(position).getName());
            rr_territory_InboxDetailRV.setVisibility(View.GONE);
            InboxDetailRV.setVisibility(View.GONE);
            try
            {
                str_state_id= arrayList.get(position).getId();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            edt_selct_city.setFocusable(true);
            edt_selct_city.setClickable(true);
            System.out.println("State id is###"+str_state_id);
            callWebservicefor_getAllCityAcctoState(str_state_id);
        }
        else
        {
            edt_select_region.setText(arrayList.get(position).getName());
            rr_show_suggestion_region_view.setVisibility(View.GONE);
            recyclerView_for_show_region_local.setVisibility(View.GONE);

            try
            {
                str_state_id= arrayList.get(position).getId();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            edt_select_city_local.setFocusable(true);
            edt_select_city_local.setClickable(true);
            System.out.println("State id is###"+str_state_id);
            callWebservicefor_getAllCityAcctoState(str_state_id);
        }


    }
});


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_territory_name;
            RelativeLayout rr_first_descp;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt_territory_name = (TextView) itemView.findViewById(R.id.cuisineslistitem);
                rr_first_descp=(RelativeLayout)itemView.findViewById(R.id.rr_first_descp);

            }


        }

        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###"+charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (StateList_LxDetails_Bean wp : arSearchlist) {
                    if (wp.getName().toLowerCase().startsWith(charText))
                    {
                        arrayList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }

    }
    // ---------------------------- RecycleView For Select City -------------------------------------------------------------------------------//
    private class CustomStart_Field_Work_Interior_Slect_City extends RecyclerView.Adapter<CustomStart_Field_Work_Interior_Slect_City.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<CityList_LxDetails_Bean> arr_city_list_deatils;
        List<CityList_LxDetails_Bean> arrayList = new ArrayList<>();
        List<CityList_LxDetails_Bean> arSearchlist;

        public CustomStart_Field_Work_Interior_Slect_City(Activity activity, List<CityList_LxDetails_Bean> arr_city_list_deatils2) {

            this.activity = activity;

            this.arr_city_list_deatils =arr_city_list_deatils2;

            this.arrayList = arr_city_list_deatils2;
            this.arSearchlist = new ArrayList<CityList_LxDetails_Bean>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size City###"+arr_city_list_deatils2.size());
        }


        @Override
        public CustomStart_Field_Work_Interior_Slect_City.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.searc_result, parent, false);

            return new CustomStart_Field_Work_Interior_Slect_City.MyViewHolder(itemView);

            }

        @Override
        public void onBindViewHolder(final CustomStart_Field_Work_Interior_Slect_City.MyViewHolder holder, final int position) {
            System.out.println("List Size in Bind###"+arrayList.size());
            holder.txt_city_name.setText(arrayList.get(position).getName());

            holder.rr_first_descp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (str_local_interior.equalsIgnoreCase("interior"))
                    {
                        str_city_id= arrayList.get(position).getId();

                        edt_selct_city.setText(arrayList.get(position).getName());
                        rr_city_InboxDetailRVv.setVisibility(View.GONE);
                        InboxDetailRVv.setVisibility(View.GONE);
                        sp.saveData(getActivity(),"selected_city_id",str_city_id);
                    }
                    else
                    {
                        str_city_id= arrayList.get(position).getId();

                        //for area manager
                        edt_select_city_local.setText(arrayList.get(position).getName());
                        rr_show_suggestion_city_view.setVisibility(View.GONE);
                        recyclerView_for_show_cities_local.setVisibility(View.GONE);
                        sp.saveData(getActivity(),"selected_city_id_local",str_city_id);
                    }



                   // System.out.println("List Id in OnClick###"+arrayList.get(position).getId());


                    edt_selct_interior.setFocusable(true);
                    edt_selct_interior.setClickable(true);

                    edt_select_interior_local.setFocusable(true);
                    edt_select_interior_local.setClickable(true);

                    System.out.println("State id is###"+str_city_id);
                    callWebservicefor_getAllInteriorAcctoCity(str_city_id);

                }
            });
            }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_city_name;
            RelativeLayout rr_first_descp;


            public MyViewHolder(View itemView) {
                super(itemView);
                txt_city_name = (TextView) itemView.findViewById(R.id.cuisineslistitem);
                rr_first_descp=(RelativeLayout)itemView.findViewById(R.id.rr_first_descp);
            }

                }

        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###"+charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (CityList_LxDetails_Bean wp : arSearchlist) {
                    if (wp.getName().toLowerCase().startsWith(charText))
                    {
                        arrayList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    // ---------------------------- RecycleView For Select Interior -------------------------------------------------------------------------------//
    private class CustomStart_Field_Work_Interior_Slect_Interiror extends RecyclerView.Adapter<CustomStart_Field_Work_Interior_Slect_Interiror.MyViewHolder> {

        Activity activity;

        private Context mContext;

        List<InteriorList_LxDetails_Bean> arr_city_list_deatils;
        List<InteriorList_LxDetails_Bean> arrayList = new ArrayList<>();
        List<InteriorList_LxDetails_Bean> arSearchlist;


        public CustomStart_Field_Work_Interior_Slect_Interiror(Activity activity, List<InteriorList_LxDetails_Bean> arr_all_interior_search) {

            this.activity = activity;

            this.arr_city_list_deatils =arr_all_interior_search;

            this.arrayList = arr_all_interior_search;
            this.arSearchlist = new ArrayList<InteriorList_LxDetails_Bean>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size interior###"+arrayList.size());
        }


        @Override
        public CustomStart_Field_Work_Interior_Slect_Interiror.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.searc_result, parent, false);

            return new CustomStart_Field_Work_Interior_Slect_Interiror.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomStart_Field_Work_Interior_Slect_Interiror.MyViewHolder holder, final int position) {
            System.out.println("List Size in Bind###"+arrayList.size());

            holder.txt_interior_name.setText(arrayList.get(position).getInterior());
            holder.rr_first_descp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        if (str_local_interior.equalsIgnoreCase("interior"))
                        {
                            edt_selct_interior.setText(arrayList.get(position).getInterior());
                            rr_interior_InboxDetailRVvv.setVisibility(View.GONE);
                            InboxDetailRVvv.setVisibility(View.GONE);

                            try
                            {
                                str_interiror_id= arrayList.get(position).getId();
                                System.out.println("List str selected interior id###"+str_interiror_id);
                                sp.saveData(getActivity(),"selected_interior_id",str_interiror_id);
                            }
                            catch (IndexOutOfBoundsException e)
                            {
                                e.printStackTrace();
                            }


                        }
                        else
                        {
                            //for area manager
                            edt_select_interior_local.setText(arrayList.get(position).getInterior());
                            rr_show_suggestion_interior_view.setVisibility(View.GONE);
                            recyclerView_for_show_interior_local.setVisibility(View.GONE);

                            str_interiror_id= String.valueOf(arrayList.get(position).getId());
                            System.out.println("List str id###"+str_interiror_id);
                        }



                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                }
            });


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_interior_name;
            RelativeLayout rr_first_descp;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt_interior_name = (TextView) itemView.findViewById(R.id.cuisineslistitem);
                rr_first_descp=(RelativeLayout)itemView.findViewById(R.id.rr_first_descp);
            }




        }

        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###"+charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (InteriorList_LxDetails_Bean wp : arSearchlist) {
                    if (wp.getInterior().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        arrayList.add(wp);
                    }
                }
            }
            customStart_field_work_interior_slect_interiror.notifyDataSetChanged();
        }

    }

//--------------------------------------------adapter for all_mr_list-------------------------------------------------

    public class SpinAdapter extends ArrayAdapter<all_mr>{

        // Your sent context
        private Context context;
        // Your custom values for the spinner (User)
        private ArrayList<all_mr> list;

        public SpinAdapter(FragmentActivity activity, int simple_spinner_item, ArrayList<all_mr> name_list) {
            super(activity,simple_spinner_item,name_list);

            context = activity;
            list = name_list;
        }


        @Override
        public int getCount(){
            return list.size();
        }

        @Nullable
        @Override
        public all_mr getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }


        // And the "magic" goes here
        // This is for the "passive" state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(getResources().getColor(R.color.colorPrimary));
            label.setTextSize(getResources().getDimension(R.dimen.mrg00));
            label.setPadding(5,5,5,5);

            // Then you can get the current item using the values array (Users array) and the current position
            // You can NOW reference each method you has created in your bean object (User class)
            label.setText(list.get(position).getF_name()+" "+list.get(position).getL_name());
            str_marketing_presentative_id = list.get(position).getUser_id();

            // And finally return your dynamic (or custom) view for each spinner item
            return label;
        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(final int position, View convertView,
                                    ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setBackground(getResources().getDrawable(R.drawable.x_et_bg));

            label.setTextColor(getResources().getColor(R.color.colorPrimary));
            label.setTextSize(getResources().getDimension(R.dimen.mrg00));

            label.setPadding(5,5,5,5);
            label.setText(list.get(position).getF_name()+" "+list.get(position).getL_name());
            return label;
        }
    }
//-------------------------------------------adpter for all am list---------------------------

    public class SpinAdapter2 extends ArrayAdapter<all_am>{

        // Your sent context
        private Context context;
        // Your custom values for the spinner (User)
        private ArrayList<all_am> list;

        public SpinAdapter2(FragmentActivity activity, int simple_spinner_item, ArrayList<all_am> am_name_list) {
            super(activity,simple_spinner_item,am_name_list);

            context = activity;
            list = am_name_list;
        }


        @Override
        public int getCount(){
            return list.size();
        }

        @Nullable
        @Override
        public all_am getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }


        // And the "magic" goes here
        // This is for the "passive" state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(getResources().getColor(R.color.colorPrimary));
            label.setTextSize(getResources().getDimension(R.dimen.mrg00));
            label.setPadding(5,5,5,5);

            // Then you can get the current item using the values array (Users array) and the current position
            // You can NOW reference each method you has created in your bean object (User class)
            label.setText(list.get(position).getF_name()+" "+list.get(position).getL_name());
            str_area_manger_id = list.get(position).getUser_id();


            //if (str_area_manger_id!=null)
           // {
             //   getAllMrList(str_area_manger_id, city_id);
           // }


            // And finally return your dynamic (or custom) view for each spinner item
            return label;
        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(final int position, View convertView,
                                    ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setBackground(getResources().getDrawable(R.drawable.x_et_bg));

            label.setTextColor(getResources().getColor(R.color.colorPrimary));
            label.setTextSize(getResources().getDimension(R.dimen.mrg00));

            label.setPadding(5,5,5,5);
            label.setText(list.get(position).getF_name()+" "+list.get(position).getL_name());
            return label;
        }
    }

    //-------------------------------------------adpter for all Rm list---------------------------

    public class SpinAdapter3 extends ArrayAdapter<all_rm>{

        // Your sent context
        private Context context;
        // Your custom values for the spinner (User)
        private ArrayList<all_rm> list;

        public SpinAdapter3(FragmentActivity activity, int simple_spinner_item, ArrayList<all_rm> rm_name_list) {
            super(activity,simple_spinner_item,rm_name_list);

            context = activity;
            list = rm_name_list;
        }


        @Override
        public int getCount(){
            return list.size();
        }

        @Nullable
        @Override
        public all_rm getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }


        // And the "magic" goes here
        // This is for the "passive" state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(getResources().getColor(R.color.colorPrimary));
            label.setTextSize(getResources().getDimension(R.dimen.mrg00));
            label.setPadding(5,5,5,5);

            // Then you can get the current item using the values array (Users array) and the current position
            // You can NOW reference each method you has created in your bean object (User class)
            label.setText(list.get(position).getF_name()+" "+list.get(position).getL_name());
            str_regional_manger_id = list.get(position).getUser_id();

           // if (regional_manager_user_id!=null)
          //  {
          //      getAllAmList(regional_manager_user_id);
           // }
            // And finally return your dynamic (or custom) view for each spinner item
            return label;
        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(final int position, View convertView,
                                    ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setBackground(getResources().getDrawable(R.drawable.x_et_bg));
            label.setTextColor(getResources().getColor(R.color.colorPrimary));
            label.setTextSize(getResources().getDimension(R.dimen.mrg00));
            label.setPadding(5,5,5,5);
            label.setText(list.get(position).getF_name()+" "+list.get(position).getL_name());
            return label;
        }
    }
//-----------------------------------------------------------------------------------------------------


    @Override
    public void onStart() {
        getLocation();
        super.onStart();
    }
}
