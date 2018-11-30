package com.example.candid_20.dcrapp.fragments.home_menu.search_doctor_after;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
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
import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.bean.ItemModel;
import com.example.candid_20.dcrapp.bean.ItemModel_Selected;
import com.example.candid_20.dcrapp.bean.for_dcr_products.Dcr_Products_Bean;
import com.example.candid_20.dcrapp.bean.for_dcr_products.Dcr_Products_LxDetails_Bean;
import com.example.candid_20.dcrapp.bean.for_gift.ItemModel_Selected_Gift;
import com.example.candid_20.dcrapp.bean.for_gift_list.Gift_List_Bean;
import com.example.candid_20.dcrapp.bean.for_gift_list.Gift_List_Lxdetails_Bean;
import com.example.candid_20.dcrapp.bean.for_search_doctor.SearchDoctor_LxDetails_Bean;
import com.example.candid_20.dcrapp.constant.Utils;
import com.example.candid_20.dcrapp.fragments.home_menu.Search_Doctors_Fragment;
import com.example.candid_20.dcrapp.fragments.home_menu.Search_Doctors_Submit_Fragment;
import com.example.candid_20.dcrapp.fragments.home_menu.TimeLineFragment;
import com.example.candid_20.dcrapp.other.GPSTracker;
import com.example.candid_20.dcrapp.other.URLs;
import com.example.candid_20.dcrapp.storage.MySharedPref;
import com.example.candid_20.dcrapp.volleyconnector.AppSingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

public class Doctors_Reminder_Fragment extends Fragment implements View.OnClickListener {

    View v;
    MySharedPref sp;
    String ldata,user_id4,token;
    String doc_id,speciality_id;
    TextView txt_title,txt_doctor_namee,txt_degree,txt_doc_address;
    ProgressBar loader;
    RelativeLayout rr_InboxDetailRV;
    RecyclerView InboxDetailRV;
    EditText edt_product;
    Gift_List_Bean dcr_products_bean;
    CustomSearch_Doctors_Adp customGetProducts_adp;
    EditText edt_product_qty;
    RelativeLayout rr_add_checked_unchecked;
    TextView edt_product_txt;
    int str_proquant;
    String str_id,company_name;
    String dcr_live_table_insert_id,dcr_table_insert_id,str_product_id="";
    //------------------------------------ Initialise boolean-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    boolean isGPSEnabled = false;
    double currentlat,currentlong;
    public static Doctors_Reminder_Fragment newInstance() {
        Doctors_Reminder_Fragment fragment = new Doctors_Reminder_Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.doctors_reminder, container, false);

        getLocation();
        // ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//

        getSaveddata();
        // ---------------------------- For  Get Bundle Data -------------------------------------------------------------------------------//
        getBundleData();
        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();

        // ---------------------------- For Get Current Location -------------------------------------------------------------------------------//
    //    getLocation();
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

    // ---------------------------- For get Bundle Data -------------------------------------------------------------------------------//
    private void getBundleData() {

        Bundle b1=this.getArguments();
        doc_id = b1.getString("doc_id");
        System.out.println("Doctor id Reminder###"+doc_id);
        }
    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
    private void initUi() {
        // ---------------------------- Casting Element -------------------------------------------------------------------------------//
        //Casting TextView for Title
        txt_title=(TextView)getActivity().findViewById(R.id.txt_title);
        //Set Text Title
        txt_title.setText("Doctor Reminder");

        //Casting EditText for Product Quantity
        edt_product_qty=(EditText)v.findViewById(R.id.edt_product_qty);
        //Casting TextView for Product Name Error
        edt_product_txt=(TextView) v.findViewById(R.id.edt_product_txt);
        //Casting TextView for Doctor Name
        txt_doctor_namee=(TextView)v.findViewById(R.id.txt_doctor_namee);

        //Casting RelativeLayout for Add Product
        rr_add_checked_unchecked=(RelativeLayout)v.findViewById(R.id.rr_add_checked_unchecked);

        //Casting TextView for Doctor Degree
        txt_degree=(TextView)v.findViewById(R.id.txt_degree);
        //Casting TextView for Doctor Address
        txt_doc_address=(TextView)v.findViewById(R.id.txt_distributor_namee);
        //Casting RelativeLayout for Product Get
        rr_InboxDetailRV=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRV);
        //Casting RecyclerView for Product Get
        InboxDetailRV=(RecyclerView)v.findViewById(R.id.InboxDetailRV);
        //Casting EditText for Product Get
        edt_product=(EditText)v.findViewById(R.id.edt_product);

        //Casting ProgressBar for Loader
        loader=(ProgressBar)v.findViewById(R.id.loader);
        //For  Set Products Create by these
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        InboxDetailRV.setLayoutManager(mLayoutManager2);
        InboxDetailRV.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRV.setItemAnimator(new DefaultItemAnimator());

        //Call WebService for Get Doctor Details
        callWebservicefor_getAllInteriorAcctoCity(doc_id);
// ---------------------------- setOnTextChange Listner-------------------------------------------------------------------------------//

        //For Search Product
        edt_product.addTextChangedListener(new TextWatcher() {
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

                    edt_product_txt.setVisibility(View.GONE);
                    // TODO Auto-generated method stub

//callWebservicefor_getAllInteriorAcctoCity(s.toString());

                    if(dcr_products_bean!=null) {

                        if (dcr_products_bean.getResult().size() > 0) {
                            System.out.println("Search Doctor Size Above###"+dcr_products_bean.getResult().size());

                            //Set Visiblity of RelativeLayout and RecyclerView
                            rr_InboxDetailRV.setVisibility(View.VISIBLE);
                            InboxDetailRV.setVisibility(View.VISIBLE);

                            //Set Adaptetr
                            // InboxDetailRV.setAdapter(customSearch_doctors_adp);
                            //Filter Data
                            customGetProducts_adp.filter(s.toString());

                        }

                        if(dcr_products_bean.getResult().size() == 0)
                        {
                            System.out.println("Search Doctor Size Equal###"+dcr_products_bean.getResult().size());

                            //    callWebservicefor_getAllInteriorAcctoCity("");
                            //Filter Data
                            customGetProducts_adp.filter(s.toString());



                        }
//                      stokiest_add_products_adp.notifyDataSetChanged();
                    }

                    else
                    {
               /*         System.out.println("Search Doctor Size Other###"+searchDoctor_bean.getResult().size());

                        callWebservicefor_getAllInteriorAcctoCity("");*/

                    }
                }


                else {
                    rr_InboxDetailRV.setVisibility(View.GONE);

                    InboxDetailRV.setVisibility(View.GONE);
                    //edt_selct_city.setText("");
                }

                //   edt_selct_territory.setText("");


            }
        });

        // ---------------------------- setOnClicklistner-------------------------------------------------------------------------------//
        rr_add_checked_unchecked.setOnClickListener(this);

    }

    // ---------------------------- WebService Call for Getall Allocated Products-------------------------------------------------------------------------------//
    private void callWebservicefor_getAllInteriorAcctoCity(String city_id2) {

        if (getActivity()!=null){

            if(Utils.isConnected(getActivity())) {

                get_Data_forGetStart(city_id2);
            }
            else
            {
                Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
            }
        }

    }
    // ---------------------------- For  WebService Call Method of Get Doctor Details-------------------------------------------------------------------------------//
    private void get_Data_forGetStart(final String str_docid3) {

        String url = "http://dailyreporting.in/"+company_name+"/api/doctor_single_record";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        /*StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_DOCTOR_SINGLE_RECORD, new Response.Listener<String>() {*/
        //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("DOCTORSINGLERECORD", "DOCTOR SINGLE RECORD: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);

                        String result=jObj.getString("result");
                        System.out.println("Result DOCTOR SINGLE RECORD***"+result);
                        String message=jObj.getString("message");

                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                        JSONObject jsonObject33=new JSONObject(result);
                        String doctor_name=jsonObject33.getString("doctor_name");
                        String speciality=jsonObject33.getString("speciality");
                        String clinic_address=jsonObject33.getString("clinic_address");
                        speciality_id=jsonObject33.getString("speciality_id");

                        //For Doctor name
                        if (!doctor_name.equalsIgnoreCase(""))
                        {
                            String upperString_str_doc_name = doctor_name.substring(0,1).toUpperCase() + doctor_name.substring(1);
                            txt_doctor_namee.setText(upperString_str_doc_name);
                        }


                        //For Doctor Speciality
                        /*if (!speciality.equalsIgnoreCase(""))
                        {

                        }*/
                        String upper_speciality = speciality.substring(0,1).toUpperCase() + speciality.substring(1);
                        txt_degree.setText(upper_speciality);

                        //For Doctor Address
                        if (!clinic_address.equalsIgnoreCase(""))
                        {
                            String upperString_clinic_address = clinic_address.substring(0,1).toUpperCase() + clinic_address.substring(1);
                            txt_doc_address.setText(upperString_clinic_address);
                        }
                        else
                        {
                            txt_doc_address.setText("Not Provided");
                        }

                        //Call WebService for Get Products Details
                        callWebservicefor_getAllInteriorAcctoCity1(user_id4);

                    } else {
                        loader.setVisibility(View.GONE);

                        String errorMsg = jObj.getString("message");

                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                            Log.e("errorMsg", errorMsg);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.GONE);

                Log.e("DOCTORSINGLERECORD", "DOCTOR SINGLE RECORD: " + error.getMessage());
                try {
                    JSONObject jsonObject22=new JSONObject(error.getMessage());

                    String errorMsg=jsonObject22.getString("message");
                    //  JSONObject jsonObject33=new JSONObject(message);
                    //   String password=jsonObject33.getString("password");

                    Toast.makeText(getActivity(), errorMsg,Toast.LENGTH_SHORT).show();



                    // ---------------------------- Set Custom Gridview and Adapter -------------------------------------------------------------------------------//


                } catch (JSONException e) {
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
                params.put("doctor_id",str_docid3);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }

    // ---------------------------- WebService Call for Getall Allocated Products-------------------------------------------------------------------------------//
    private void callWebservicefor_getAllInteriorAcctoCity1(String city_id2) {


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

        String url = "http://dailyreporting.in/"+company_name+"/api/user_allocate_gift";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_USER_ALLOCATE_GIFTS,
                new com.android.volley.Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("User Allocate Products", "User Allocate Products response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {

                        MySharedPref.bean_list= new ArrayList<ItemModel>();
                        loader.setVisibility(View.GONE);

                        Gson gson = new Gson();

                        dcr_products_bean=gson.fromJson(response,Gift_List_Bean.class);
                        System.out.println("List Size interior:"+dcr_products_bean.getResult().size());
                        /*System.out.println("Base URl Old^^^"+str_base_url);
                        String str_new_base_url=str_base_url.replace("candid-15-pc","192.168.1.2");
                        System.out.println("Base URl New^^^"+str_new_base_url);*/

                        if(dcr_products_bean.getResult().size()>0)
                        {
                            // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//
                            customGetProducts_adp = new CustomSearch_Doctors_Adp(getActivity(),dcr_products_bean.getResult());
                            InboxDetailRV.setAdapter(customGetProducts_adp);

                            }
                            else
                        {
                            rr_InboxDetailRV.setVisibility(View.GONE);
                            InboxDetailRV.setVisibility(View.GONE);
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
                Log.e("UserAllocateProducts", "User Allocate Products Error: " + error.getMessage());
                try {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();


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
                params.put("user_id",user_id4);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }

    @Override
    public void onClick(View v) {

        if(v==rr_add_checked_unchecked) {
            String str_edt_product = edt_product.getText().toString();
            String str_edt_product_qty = edt_product_qty.getText().toString();
            int in_pro_qty = 0;
            try {
                in_pro_qty = Integer.parseInt(edt_product_qty.getText().toString());

            } catch (NumberFormatException e) {

            }
            boolean iserror = false;

            if (!str_edt_product.equalsIgnoreCase("")) {
                //iserror=true;
                //edt_product_txt.setVisibility(View.VISIBLE);
           /* } else {
                edt_product_txt.setVisibility(View.GONE);

            }

*/
           if (str_id==null)
           {
               iserror=true;
               edt_product.setError("Please select allocated product");
           }
                if (str_edt_product_qty.equalsIgnoreCase("")) {
                    iserror = true;
                    edt_product_qty.setError("This Field is Required");
                } else if (in_pro_qty == 0) {
                    iserror = true;
                    edt_product_qty.setError("You can't enter 0 quantity");
                } else if (str_proquant < in_pro_qty) {
                    iserror = true;
                    edt_product_qty.setError("You can't enter more quantity");
                }
            }

            if (!iserror) {
                edt_product_txt.setVisibility(View.GONE);
                //Call WebService
                callWebservicefor_getAllInteriorAcctoCity2();
            }



        }
    }

    // ---------------------------- WebService Call for Getall Allocated Products-------------------------------------------------------------------------------//
    private void callWebservicefor_getAllInteriorAcctoCity2( ) {


        if (Utils.isConnected(getActivity())) {

            get_Data_forGetCallStart();
        } else {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }
    // ---------------------------- For  WebService Call Method for Call Start-------------------------------------------------------------------------------//
    private void get_Data_forGetCallStart() {

        String url = "http://dailyreporting.in/"+company_name+"/api/doctor_reminder_submit";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        /*StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_DOCTOR_REMINDER_SUBMIT, new Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
               url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DoctorReminder", "Doctor Reminder: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);

                        String result=jObj.getString("result");
                        System.out.println("Result Doctor Reminder***"+result);
                        String message=jObj.getString("message");

                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }


                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFrame, new TimeLineFragment())
                                .addToBackStack("20")
                                .commit();
                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            Log.e("errorMsg", message);

                        }


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
                Log.e("DCRCallWork", "DCR Call Work: " + error.getMessage());
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
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();

                try{
                params.put("dcr_table_insert_id",dcr_table_insert_id);
                params.put("user_id",user_id4);
                params.put("doctor_rem",doc_id);
                params.put("doctor_rem_speciality",speciality_id);
                params.put("doctor_rem_gift",str_product_id);
                params.put("checkout_lat", String.valueOf(currentlat));
                params.put("checkout_long", String.valueOf(currentlong));
                params.put("doctor_rem_gift_val",edt_product_qty.getText().toString());

                    Log.e("errorMsg", dcr_table_insert_id);
                    Log.e("errorMsg", user_id4);
                    Log.e("errorMsg", doc_id);
                    Log.e("errorMsg", speciality_id);
                    Log.e("errorMsg", str_product_id);


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
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
                dcr_live_table_insert_id=sp.getData(getActivity(),"dcr_live_table_insert_id","null");
                dcr_table_insert_id=sp.getData(getActivity(),"dcr_table_insert_id","null");

                Log.e("Id is@@@",user_id4);
                System.out.println("Id is***" + user_id4);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
    // ---------------------------- Class for RecycleView for Search Doctor Adapter-------------------------------------------------------------------------------//
    private class CustomSearch_Doctors_Adp extends RecyclerView.Adapter<CustomSearch_Doctors_Adp.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<Gift_List_Lxdetails_Bean> arr_all_search_doctors3;
        List<Gift_List_Lxdetails_Bean> arrayList = new ArrayList<>();
        List<Gift_List_Lxdetails_Bean> arSearchlist;

        public CustomSearch_Doctors_Adp(Activity activity, List<Gift_List_Lxdetails_Bean> arr_all_search_doctors2) {

            this.activity = activity;

            this.arr_all_search_doctors3 = arr_all_search_doctors2;

            this.arrayList = arr_all_search_doctors2;
            this.arSearchlist = new ArrayList<Gift_List_Lxdetails_Bean>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size###"+arr_all_search_doctors3.size());
        }


        @Override
        public CustomSearch_Doctors_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_doctor_adp, parent, false);

            return new CustomSearch_Doctors_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomSearch_Doctors_Adp.MyViewHolder holder, final int position) {



            holder.img_next.setVisibility(View.GONE);
            holder.txt_profession.setVisibility(View.GONE);
            String str_interior_name= arr_all_search_doctors3.get(position).getGname();

            String upperString_str_interior_name = str_interior_name.substring(0,1).toUpperCase() + str_interior_name.substring(1);
            holder.txt_current_loc_descp.setText(upperString_str_interior_name);



            holder.rr_current_loc_descp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        edt_product.setText(arr_all_search_doctors3.get(position).getGname());
                        edt_product_qty.setEnabled(true);


                        str_product_id=arr_all_search_doctors3.get(position).getGiftTableId();
                        rr_InboxDetailRV.setVisibility(View.GONE);
                        InboxDetailRV.setVisibility(View.GONE);
                        str_proquant= Integer.parseInt(arr_all_search_doctors3.get(position).getTotalQty());
                        str_id= arr_all_search_doctors3.get(position).getGiftTableId();
                        System.out.println("str id is###"+str_id);

                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        System.out.println("Exception is###"+e);
                    }

                    //edt_product.setText("");

                }
            });
        }

        @Override
        public int getItemCount() {
            return arr_all_search_doctors3.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_current_loc_descp,txt_profession;
            RelativeLayout rr_current_loc_descp;
ImageView img_next;
            public MyViewHolder(View itemView) {
                super(itemView);
                txt_current_loc_descp = (TextView) itemView.findViewById(R.id.txt_current_loc_descp);
                txt_profession= (TextView) itemView.findViewById(R.id.txt_profession);
                rr_current_loc_descp= (RelativeLayout) itemView.findViewById(R.id.rr_current_loc_descp);
                img_next=(ImageView)itemView.findViewById(R.id.img_next);
            }


        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###"+charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (Gift_List_Lxdetails_Bean wp : arSearchlist) {
                    if (wp.getGname().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        arrayList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }


}
