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
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.example.candid_20.dcrapp.constant.Utils;
import com.example.candid_20.dcrapp.fragments.home_menu.TimeLineFragment;
import com.example.candid_20.dcrapp.other.GPSTracker;
import com.example.candid_20.dcrapp.other.URLs;
import com.example.candid_20.dcrapp.storage.MySharedPref;
import com.example.candid_20.dcrapp.volleyconnector.AppSingleton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

public class CallStart_Fragment extends Fragment implements View.OnClickListener {

    View v;
    MySharedPref sp;
    String ldata,user_id4,token;
    TextView txt_title,txt_doctor_namee,txt_degree,txt_doc_address,txt_callstarttime,txt_addproducts,txt_add_gifts,txt_addproducts_error,
            txt_addgifts_error;
    ProgressBar loader;
String doc_id;
    //------------------------------------ Initialise boolean-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    boolean isGPSEnabled = false;
    //------------------------------------ Initialise double-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    double currentlat,currentlong;

    RelativeLayout rr_InboxDetailRV,rr_InboxDetailRVv;
    RecyclerView InboxDetailRV,InboxDetailRVv;
    Dcr_Products_Bean dcr_products_bean;
    CustomCall_Start_Adp customCallStartAdp;
    CustomCall_Start_Gifts_Adp customCall_start_gifts_adp;
RelativeLayout rr_add_products_checked_unchecked,rr_add_gifts_checked_unchecked,rr_add_gifts_checked_unchecked2,rr_callcmplte_checked_unchecked;
String dcr_live_table_insert_id,dcr_table_insert_id,dcr_doctor_live_table_insert_id;
String str_product_one="",str_product_two="",str_product_three="",str_product_one_qty="",
        str_product_two_qty="",str_product_three_qty="";
String str_gift_one="",str_gift_one_qty="",company_name;

public static CallStart_Fragment newInstance() {
        CallStart_Fragment fragment = new CallStart_Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.submit_dcr, container, false);

        // ---------------------------- For Get Current Location -------------------------------------------------------------------------------//
        getLocation();

        // ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//
        getSaveddata();
        // ---------------------------- For  Get Bundle Data -------------------------------------------------------------------------------//
        getBundleData();
        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();


        return v;
    }
    // ---------------------------- For get Bundle Data -------------------------------------------------------------------------------//
    private void getBundleData() {

        Bundle b1=this.getArguments();
        doc_id = b1.getString("doc_id");

    }


    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
    private void initUi() {


        // ---------------------------- Casting Element -------------------------------------------------------------------------------//
    //Casting TextView for Title
        txt_title=(TextView)getActivity().findViewById(R.id.txt_title);
        //Set Text Title
        txt_title.setText("Add Doctor Call");

        //Casting RelativeLayout for Add Products
        rr_add_products_checked_unchecked=(RelativeLayout)v.findViewById(R.id.rr_add_products_checked_unchecked);
        //Casting RelativeLayout for Add Gifts
        rr_add_gifts_checked_unchecked=(RelativeLayout)v.findViewById(R.id.rr_add_gifts_checked_unchecked);
        rr_add_gifts_checked_unchecked2=(RelativeLayout)v.findViewById(R.id.rr_add_gifts_checked_unchecked2);

        //Casting RelativeLayout for Call Complete
        rr_callcmplte_checked_unchecked=(RelativeLayout)v.findViewById(R.id.rr_callcmplte_checked_unchecked);

        //Casting TextView for Call Start
        txt_callstarttime=(TextView)v.findViewById(R.id.txt_callstarttime);
        //Casting TextView for Add Product Error
        txt_addproducts_error=(TextView)v.findViewById(R.id.txt_addproducts_error);
        //Casting TextView for Add Gift Error
        txt_addgifts_error=(TextView)v.findViewById(R.id.txt_addgifts_error);

        //Casting ProgressBar for Loader
        loader=(ProgressBar)v.findViewById(R.id.loader);

        //Casting TextView for Doctor Name
        txt_doctor_namee=(TextView)v.findViewById(R.id.txt_doctor_namee);
        //Casting TextView for Add Gifts
        txt_add_gifts=(TextView)v.findViewById(R.id.txt_add_gifts);
        //Casting TextView for Add Products
        txt_addproducts=(TextView)v.findViewById(R.id.txt_addproducts);
        //Casting TextView for Doctor Degree
        txt_degree=(TextView)v.findViewById(R.id.txt_degree);
        //Casting TextView for Doctor Address
        txt_doc_address=(TextView)v.findViewById(R.id.txt_doc_address);
        //Casting RelativeLayout for Product Get
        rr_InboxDetailRV=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRV);
        //Casting RecyclerView for Product Get
        InboxDetailRV=(RecyclerView)v.findViewById(R.id.InboxDetailRV);
        //Casting RelativeLayout for Gifts Get
        rr_InboxDetailRVv=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRVv);
        //Casting RecyclerView for Gifts Get
        InboxDetailRVv=(RecyclerView)v.findViewById(R.id.InboxDetailRVv) ;

        //Displaying current time in 12 hour format with AM/PM
        DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
        String dateString = dateFormat.format(new Date()).toString();
        txt_callstarttime.setText(dateString);


        //For  Set Products Create by these
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        InboxDetailRV.setLayoutManager(mLayoutManager2);
        InboxDetailRV.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRV.setItemAnimator(new DefaultItemAnimator());


        //For  Set Gifts Create by these
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity());
        InboxDetailRVv.setLayoutManager(mLayoutManager3);
        InboxDetailRVv.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRVv.setItemAnimator(new DefaultItemAnimator());

         //Call WebService for Get Doctor Details
        callWebservicefor_getAllInteriorAcctoCity(doc_id);

        // ---------------------------- set OnClickListner-------------------------------------------------------------------------------//

       //Add Product
        rr_add_products_checked_unchecked.setOnClickListener(this);
    //Add Gifts
        rr_add_gifts_checked_unchecked.setOnClickListener(this);
        rr_add_gifts_checked_unchecked2.setOnClickListener(this);

        //Call Complete
        rr_callcmplte_checked_unchecked.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        if(v==rr_add_products_checked_unchecked)
        {

            MySharedPref sp=new MySharedPref();
            sp.saveData(getActivity(),"doc_idd",doc_id);
            Add_Products_Fragment selectedFragment = Add_Products_Fragment.newInstance();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentFrame, selectedFragment);
            transaction.addToBackStack("8");
            transaction.commit();
        }

        if(v==rr_add_gifts_checked_unchecked)
        {
            MySharedPref sp=new MySharedPref();
            sp.saveData(getActivity(),"doc_idd",doc_id);
            Add_Gifts_Fragment selectedFragment = Add_Gifts_Fragment.newInstance();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentFrame, selectedFragment);
            transaction.addToBackStack("8");
            transaction.commit();
        }
        if(v==rr_add_gifts_checked_unchecked2)
        {
            MySharedPref sp=new MySharedPref();
            sp.saveData(getActivity(),"doc_idd",doc_id);
            Add_Gifts_Fragment selectedFragment = Add_Gifts_Fragment.newInstance();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentFrame, selectedFragment);
            transaction.addToBackStack("8");
            transaction.commit();
        }

        if(v==rr_callcmplte_checked_unchecked)
        {
    // ---------------------------- Call Webservice for call Complete-------------------------------------------------------------------------------//
             callWebservicefor_get();

     /*       boolean iserror=false;

if(MySharedPref.bean_list2==null) {

    iserror = true;
    txt_addproducts_error.setVisibility(View.VISIBLE);
    txt_addproducts_error.setText("Please Add Product for Call Complete");
}
 else if (MySharedPref.bean_list2.size() == 0) {
        iserror = true;
        txt_addproducts_error.setVisibility(View.VISIBLE);
        txt_addproducts_error.setText("Please Add Product for Call Complete");
      //  Toast.makeText(getActivity(), "Please Select ", Toast.LENGTH_SHORT).show();
    }
    else
    {
        txt_addproducts_error.setVisibility(View.GONE);

    }


            if(MySharedPref.bean_list4==null) {

                iserror = true;
                txt_addgifts_error.setVisibility(View.VISIBLE);
                txt_addgifts_error.setText("Please Add Gift for Call Complete");
            }
               else if (MySharedPref.bean_list4.size() == 0) {

    iserror=true;
                    txt_addgifts_error.setVisibility(View.VISIBLE);
                    txt_addgifts_error.setText("Please Add Gift for Call Complete");
                     //   Toast.makeText(getActivity(),"Please Select ",Toast.LENGTH_SHORT).show();
                    }
            else {
                    txt_addgifts_error.setVisibility(View.GONE);

                    }


if(!iserror)
{
    // ---------------------------- Call Webservice for call Complete-------------------------------------------------------------------------------//
    callWebservicefor_get();
}
*/



        }
    }
    // ---------------------------- WebService Call for Get Call Complete-------------------------------------------------------------------------------//
    private void callWebservicefor_get() {


        if(Utils.isConnected(getActivity())) {

            get_Data_forGetCallComplete();
        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }

    // ---------------------------- For  WebService Call Method for Call Complete-------------------------------------------------------------------------------//
    private void get_Data_forGetCallComplete() {


        String url = "http://dailyreporting.in/"+company_name+"/api/dcr_call_complete";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_DCR_CALL_COMPLETE, new Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DCRCallComplete", "DCR Call Complete: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);

                        JSONObject result=jObj.getJSONObject("result");
                        System.out.println("Result DCR Call Complete***"+result);
                        String message=jObj.getString("message");

                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                        String checkouttime=result.getString("checkout_time");
                        System.out.println("checkout time###" + checkouttime);
                        sp.saveData(getActivity(),"check_out_time","0");

                        getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentFrame, new TimeLineFragment())
                                     .addToBackStack("23")
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

                Log.e("DCRCallComplete", "DCR Call Complete: " + error.getMessage());
                try {
                    Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


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

                System.out.println("Dcr Doctor Live Table Insert id###"+dcr_doctor_live_table_insert_id);
               /* System.out.println("Dcr Doctor Live Table Insert id###"+str_product_one);
                System.out.println("Dcr Doctor Live Table Insert id###"+str_product_two);
                System.out.println("Dcr Doctor Live Table Insert id###"+str_product_three);
                System.out.println("Dcr Doctor Live Table Insert id###"+String.valueOf(currentlat));
                System.out.println("Dcr Doctor Live Table Insert id###"+String.valueOf(currentlong));
                System.out.println("Dcr Doctor Live Table Insert id###"+str_gift_one);*/
                System.out.println("Dcr Doctor Live Table Insert id###"+dcr_table_insert_id);
                System.out.println("Dcr Doctor Live Table Insert id###"+user_id4);
                System.out.println("Dcr Doctor Live Table Insert id###"+doc_id);
                System.out.println("bean list###"+MySharedPref.bean_list2.size());

                params.put("dcr_doctor_live_table_insert_id",dcr_doctor_live_table_insert_id);




                if(MySharedPref.bean_list2!=null) {

            if (MySharedPref.bean_list2.size() >0)
          {



              if(str_product_one.equalsIgnoreCase("null") || str_product_one_qty==null) {
                    params.put("product1",str_product_one);
                    params.put("product1_value","");
                }
                else
                {
                    params.put("product1",str_product_one);
                    params.put("product1_value",str_product_one_qty);

                    System.out.println("Dcr Doctor Live 1###"+str_product_one);
                    System.out.println("Dcr Doctor Live 1###"+str_product_one_qty);

                }
                if(str_product_two.equalsIgnoreCase("") || str_product_two_qty==null) {
                    params.put("product2",str_product_two);
                    params.put("product2_value","");
                    }
                else
                {
                    params.put("product2",str_product_two);
                    params.put("product2_value",str_product_two_qty);

                    System.out.println("Dcr Doctor Live 2###"+str_product_two);
                    System.out.println("Dcr Doctor Live 2###"+str_product_two_qty);
                }

                if(str_product_three.equalsIgnoreCase("") || str_product_three_qty==null) {
                    params.put("product3",str_product_three);
                    params.put("product3_value","");

                }
                else
                {
                    params.put("product3",str_product_three);
                    params.put("product3_value",str_product_three_qty);

                    System.out.println("Dcr Doctor Live 3###"+str_product_three);
                    System.out.println("Dcr Doctor Live 3###"+str_product_three_qty);
                    }

                }
                            else
                        {
                              params.put("product1","");
                    params.put("product1_value","");
                            params.put("product2","");
                    params.put("product2_value","");
                       params.put("product3",str_product_three);
                    params.put("product3_value",str_product_three_qty);
                        }

                    }



                    else
                {
                    params.put("product1","");
                    params.put("product1_value","");
                    params.put("product2","");
                    params.put("product2_value","");
                    params.put("product3",str_product_three);
                    params.put("product3_value",str_product_three_qty);
               }


                params.put("checkout_lat", String.valueOf(currentlat));
                params.put("checkout_long", String.valueOf(currentlong));

                if(MySharedPref.bean_list4!=null) {

             if (MySharedPref.bean_list4.size() >0)
                {

                    params.put("gift",str_gift_one);
                    params.put("gift_value",str_gift_one_qty);

                }
                else
                    {
                         params.put("gift","");
                         params.put("gift_value","");
                    }
                }
     else
            {
                params.put("gift","");
                params.put("gift_value","");
            }
                params.put("dcr_doctor_table_insert_id",dcr_table_insert_id);
                params.put("user_id",user_id4);
                params.put("doctor",doc_id);

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

    // ---------------------------- WebService Call for Getall City-------------------------------------------------------------------------------//
    private void callWebservicefor_getAllInteriorAcctoCity(String city_id2) {


        if(Utils.isConnected(getActivity())) {

            get_Data_forGetStart(city_id2);
        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
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
                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                        JSONObject jsonObject33=new JSONObject(result);
                        String doctor_name=jsonObject33.getString("doctor_name");
                        String speciality=jsonObject33.getString("speciality");
                        String clinic_address=jsonObject33.getString("clinic_address");

                        sp.saveData(getActivity(),"docter_name",doctor_name);
                        sp.saveData(getActivity(),"speciality",speciality);
                        sp.saveData(getActivity(),"clinic adress",clinic_address);

                        //For Doctor name
                        if (!doctor_name.equalsIgnoreCase(""))
                        {
                            String upperString_str_doc_name = doctor_name.substring(0,1).toUpperCase() + doctor_name.substring(1);
                            txt_doctor_namee.setText(upperString_str_doc_name);
                        }

                        //For Doctor Speciality
                        if (!speciality.equalsIgnoreCase(""))
                        {
                            String upper_speciality = speciality.substring(0,1).toUpperCase() + speciality.substring(1);
                            txt_degree.setText(upper_speciality);
                        }

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

                        // ---------------------------- Set Custom RecycleView and Adapter for Products -------------------------------------------------------------------------------//

                        MySharedPref.bean_list2 = new ArrayList<ItemModel_Selected>();
                        if(MySharedPref.bean_list!=null) {


                            for (int j = 0; j < MySharedPref.bean_list.size(); j++)

                            {
                                if (MySharedPref.bean_list.get(j).isSelected()) {


                                    String id = MySharedPref.bean_list.get(j).getId();
                                    String index1 = MySharedPref.bean_list.get(j).getIndex();

                                    String product_name1 = MySharedPref.bean_list.get(j).getProduct_name();
                                    String edt_proquanty1 = MySharedPref.bean_list.get(j).getEdt_product_quantity();

                                    String pro_quantity1 = MySharedPref.bean_list.get(j).getProduct_quantity();
                                    boolean isSelected1 = MySharedPref.bean_list.get(j).isSelected();

                                    //  boolean   is_selected=MySharedPref.bean_list.get(i).isSelected();
                                    ItemModel_Selected consultant_list_bean = new ItemModel_Selected(id, index1, edt_proquanty1, product_name1, pro_quantity1, isSelected1);
                                    MySharedPref.bean_list2.add(consultant_list_bean);

                                    /* if(edt_proquanty1!=null) {

                                        if (!edt_proquanty1.equalsIgnoreCase("null")) {
                                            ItemModel_Selected consultant_list_bean = new ItemModel_Selected(id, index1, edt_proquanty1, product_name1, pro_quantity1, isSelected1);
                                            MySharedPref.bean_list2.add(consultant_list_bean);
                                            System.out.println("MyShared Size###" + MySharedPref.bean_list2.size());
                                        }
                                    }*/



                         /*       System.out.println("MyShared Size###" + MySharedPref.bean_list2.get(j).isSelected());
                                System.out.println("MyShared Name###" + MySharedPref.bean_list2.get(j).getProduct_name());
                                System.out.println("MyShared Id###" + MySharedPref.bean_list2.get(j).getId());
                                System.out.println("MyShared Quantity###" + MySharedPref.bean_list2.get(j).getProduct_quantity());*/
                                    if(MySharedPref.bean_list2!=null) {
                                        MySharedPref sp=new MySharedPref();

                                        String doc_idd=sp.getData(getActivity(),"doc_idd","null");
                                        String checked_userid=sp.getData(getActivity(),"checked_userid","null");
                                        System.out.println("Doc Idd&&&"+doc_idd);
                                        System.out.println("Checked UserIdd&&&"+checked_userid);

                                        if(doc_idd!=null) {
                                            System.out.println("Doc Id***" + doc_id);

                                            if (doc_idd.equalsIgnoreCase(doc_id) && checked_userid.equalsIgnoreCase(user_id4)) {
                                                if (MySharedPref.bean_list2.size() > 0) {

                                                    txt_addproducts.setText("Edit Products");
                                                    rr_InboxDetailRV.setVisibility(View.VISIBLE);
                                                    InboxDetailRV.setVisibility(View.VISIBLE);
if(MySharedPref.bean_list2.size()==1) {
    str_product_one = MySharedPref.bean_list2.get(0).getId();
    str_product_two="";
    str_product_three="";


    System.out.println("0 product qty"+ MySharedPref.bean_list2.get(0).getEdt_product_quantity());
    str_product_one_qty=MySharedPref.bean_list2.get(0).getEdt_product_quantity();
    str_product_two_qty="";
    str_product_three_qty="";
}
if(MySharedPref.bean_list2.size()==2)
{
    str_product_one = MySharedPref.bean_list2.get(0).getId();
    str_product_two=MySharedPref.bean_list2.get(1).getId();
    str_product_three="";

    System.out.println("0 product qty"+ MySharedPref.bean_list2.get(0).getEdt_product_quantity());
    System.out.println("1 product qty"+ MySharedPref.bean_list2.get(1).getEdt_product_quantity());

    str_product_one_qty=MySharedPref.bean_list2.get(0).getEdt_product_quantity();
    str_product_two_qty=MySharedPref.bean_list2.get(1).getEdt_product_quantity();
    str_product_three_qty="";

}
if(MySharedPref.bean_list2.size()==3) {
                                                        str_product_one = MySharedPref.bean_list2.get(0).getId();
                                                        str_product_two=MySharedPref.bean_list2.get(1).getId();
                                                        str_product_three=MySharedPref.bean_list2.get(2).getId();

    System.out.println("0 product qty"+ MySharedPref.bean_list2.get(0).getEdt_product_quantity());
    System.out.println("1 product qty"+ MySharedPref.bean_list2.get(1).getEdt_product_quantity());
    System.out.println("2 product qty"+ MySharedPref.bean_list2.get(0).getEdt_product_quantity());


    str_product_one_qty=MySharedPref.bean_list2.get(0).getEdt_product_quantity();
    str_product_two_qty=MySharedPref.bean_list2.get(1).getEdt_product_quantity();
                                                        str_product_three_qty=MySharedPref.bean_list2.get(2).getEdt_product_quantity();

                                                    }

                                                    customCallStartAdp = new CustomCall_Start_Adp(getActivity(), MySharedPref.bean_list2);
                                                    InboxDetailRV.setAdapter(customCallStartAdp);
                                                }
                                            }
                                        }
                                    }


                                }
                            }
                        }
                            // ---------------------------- Set Custom RecycleView and Adapter for Gifts -------------------------------------------------------------------------------//

                            MySharedPref.bean_list4 = new ArrayList<ItemModel_Selected_Gift>();


                            if(MySharedPref.bean_list3!=null) {


                                for (int j = 0; j < MySharedPref.bean_list3.size(); j++)

                                {
                                    if (MySharedPref.bean_list3.get(j).isSelected()) {

                                        System.out.println("MyShared Size Selected###" + MySharedPref.bean_list3.size());

                                        String id = MySharedPref.bean_list3.get(j).getId();
                                        String product_name1 = MySharedPref.bean_list3.get(j).getProduct_name();
                                        String pro_quantity1 = MySharedPref.bean_list3.get(j).getProduct_quantity();
                                        boolean isSelected1 = MySharedPref.bean_list3.get(j).isSelected();
                                        String index1 = MySharedPref.bean_list3.get(j).getIndex();
                                        String edt_proquanty1 = MySharedPref.bean_list3.get(j).getEdt_product_quantity();

                                        //  boolean   is_selected=MySharedPref.bean_list.get(i).isSelected();
                                        System.out.println("Edit Quantity"+edt_proquanty1);
if(edt_proquanty1!=null) {

    if (!edt_proquanty1.equalsIgnoreCase("null")) {
        ItemModel_Selected_Gift consultant_list_bean = new ItemModel_Selected_Gift(id, index1, edt_proquanty1, product_name1, pro_quantity1, isSelected1);
        MySharedPref.bean_list4.add(consultant_list_bean);
        System.out.println("MyShared Size Gifts###" + MySharedPref.bean_list4.size());
    }
}

                         /*       System.out.println("MyShared Size###" + MySharedPref.bean_list2.get(j).isSelected());
                                System.out.println("MyShared Name###" + MySharedPref.bean_list2.get(j).getProduct_name());
                                System.out.println("MyShared Id###" + MySharedPref.bean_list2.get(j).getId());
                                System.out.println("MyShared Quantity###" + MySharedPref.bean_list2.get(j).getProduct_quantity());*/


                                            if(MySharedPref.bean_list4!=null) {
                                                MySharedPref sp = new MySharedPref();

                                                String doc_idd = sp.getData(getActivity(), "doc_idd_selected_gift", "null");
                                                String checked_userid=sp.getData(getActivity(),"checked_userid","null");
                                                 System.out.println("Checked UserIdd&&&"+checked_userid);

                                                System.out.println("Doc Idd Gift&&&" + doc_idd);

                                                if (doc_idd != null) {
                                                    System.out.println("Doc Id Gift***" + doc_id);
                                            if (doc_idd.equalsIgnoreCase(doc_id) && checked_userid.equalsIgnoreCase(user_id4)) {
if(MySharedPref.bean_list4.size()>0) {
    txt_add_gifts.setText("Edit Gifts");
    rr_add_gifts_checked_unchecked2.setVisibility(View.GONE);
    rr_add_gifts_checked_unchecked.setVisibility(View.VISIBLE);
    rr_InboxDetailRVv.setVisibility(View.VISIBLE);
    InboxDetailRVv.setVisibility(View.VISIBLE);

    if (MySharedPref.bean_list4.size() == 1) {
        str_gift_one = MySharedPref.bean_list4.get(0).getId();
        str_gift_one_qty = MySharedPref.bean_list4.get(0).getEdt_product_quantity();
    }

    customCall_start_gifts_adp = new CustomCall_Start_Gifts_Adp(getActivity(), MySharedPref.bean_list4);
    InboxDetailRVv.setAdapter(customCall_start_gifts_adp);
}
}
                                                }

                                        }
                                    }
                                }
                            }


                                // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//




//Call WebService for Get Products Details
                      //  callWebservicefor_getAllInteriorAcctoCity(doc_id);

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
                    /*JSONObject jsonObject22=new JSONObject(error.getMessage());

                    String errorMsg=jsonObject22.getString("message");*/
                    //  JSONObject jsonObject33=new JSONObject(message);
                    //   String password=jsonObject33.getString("password");

                    Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();



                    // ---------------------------- Set Custom Gridview and Adapter -------------------------------------------------------------------------------//


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
                params.put("doctor_id",str_docid3);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }
    // ---------------------------- For WebService Call for get Search Doctor Suggestion -------------------------------------------------------------------------------//
    private void getData_ForSearchSuggestion(final String cityid4) {

        String url = "http://dailyreporting.in/"+company_name+"/api/user_allocate_product";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_USER_ALLOCATE_PRODUCT,
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
                        loader.setVisibility(View.GONE);

                        Gson gson = new Gson();

                        dcr_products_bean=gson.fromJson(response,Dcr_Products_Bean.class);
                        System.out.println("List Size interior:"+dcr_products_bean.getResult().size());
                        /*System.out.println("Base URl Old^^^"+str_base_url);
                        String str_new_base_url=str_base_url.replace("candid-15-pc","192.168.1.2");
                        System.out.println("Base URl New^^^"+str_new_base_url);*/

                        if(dcr_products_bean.getResult().size()>0)
                        {

                            //   btn_add_products.setVisibility(View.VISIBLE);

                            // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//

                      /*      customCallStartAdp = new CustomCall_Start_Adp(getActivity(),dcr_products_bean.getResult());
                            InboxDetailRV.setAdapter(customCallStartAdp);
*/


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

                    if(error.getMessage()!=null) {
                        JSONObject jsonObject22 = new JSONObject(error.getMessage());

                        String errorMsg = jsonObject22.getString("message");
                        //  JSONObject jsonObject33=new JSONObject(message);
                        //   String password=jsonObject33.getString("password");

                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                    }

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

        if (ldata != null)

        {
            try {
                JSONObject jsonObject = new JSONObject(ldata);
                user_id4 = jsonObject.getString("user_id");
                token = jsonObject.getString("token");


                dcr_live_table_insert_id=sp.getData(getActivity(),"dcr_live_table_insert_id","null");
                dcr_doctor_live_table_insert_id=sp.getData(getActivity(),"dcr_doctor_live_table_insert_id","null");
                dcr_table_insert_id=sp.getData(getActivity(),"dcr_doctor_table_insert_id","null");

                Log.e("Id is@@@",user_id4);
                System.out.println("Id is***" + user_id4);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    // ---------------------------- Class for RecycleView for Call Start Adapter-------------------------------------------------------------------------------//
    private class CustomCall_Start_Adp extends RecyclerView.Adapter<CustomCall_Start_Adp.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<ItemModel_Selected> arr_all_search_doctors3;
        List<ItemModel_Selected> arrayList = new ArrayList<>();
        List<ItemModel_Selected> arSearchlist;

        public CustomCall_Start_Adp(Activity activity, ArrayList<ItemModel_Selected> arr_all_search_doctors2) {

            this.activity = activity;

            this.arr_all_search_doctors3 = arr_all_search_doctors2;

            this.arrayList = arr_all_search_doctors2;
            this.arSearchlist = new ArrayList<ItemModel_Selected>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size###"+arr_all_search_doctors3.size());
        }


        @Override
        public CustomCall_Start_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.submit_dcr_product_adp, parent, false);

            return new CustomCall_Start_Adp.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final CustomCall_Start_Adp.MyViewHolder holder, final int position) {
            holder.edt_product.setText(MySharedPref.bean_list2.get(position).getProduct_name());
            holder.edt_product_qty.setText(MySharedPref.bean_list2.get(position).getEdt_product_quantity());

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
            return MySharedPref.bean_list2.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
           EditText edt_product,edt_product_qty;
            public MyViewHolder(View itemView) {
                super(itemView);
                edt_product= (EditText) itemView.findViewById(R.id.edt_product);
                edt_product_qty= (EditText) itemView.findViewById(R.id.edt_product_qty);

            }


        }

        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###"+charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (ItemModel_Selected wp : arSearchlist) {
                    if (wp.getProduct_name().toLowerCase().startsWith(charText))
                    {
                        arrayList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
    // ---------------------------- Class for RecycleView for Call Start Adapter-------------------------------------------------------------------------------//
    private class CustomCall_Start_Gifts_Adp extends RecyclerView.Adapter<CustomCall_Start_Gifts_Adp.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<ItemModel_Selected_Gift> arr_all_search_doctors3;
        List<ItemModel_Selected_Gift> arrayList = new ArrayList<>();
        List<ItemModel_Selected_Gift> arSearchlist;

        public CustomCall_Start_Gifts_Adp(Activity activity, ArrayList<ItemModel_Selected_Gift> arr_all_search_doctors2) {

            this.activity = activity;

            this.arr_all_search_doctors3 = arr_all_search_doctors2;

            this.arrayList = arr_all_search_doctors2;
            this.arSearchlist = new ArrayList<ItemModel_Selected_Gift>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size###"+arr_all_search_doctors3.size());
        }


        @Override
        public CustomCall_Start_Gifts_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.submit_dcr_product_adp, parent, false);

            return new CustomCall_Start_Gifts_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomCall_Start_Gifts_Adp.MyViewHolder holder, final int position) {
            holder.edt_product.setText(MySharedPref.bean_list4.get(position).getProduct_name());
            holder.edt_product_qty.setText(MySharedPref.bean_list4.get(position).getEdt_product_quantity());

        }

        @Override
        public int getItemCount() {
            return MySharedPref.bean_list4.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            EditText edt_product,edt_product_qty;
            public MyViewHolder(View itemView) {
                super(itemView);
                edt_product= (EditText) itemView.findViewById(R.id.edt_product);
                edt_product_qty= (EditText) itemView.findViewById(R.id.edt_product_qty);

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
                for (ItemModel_Selected_Gift wp : arSearchlist) {
                    if (wp.getProduct_name().toLowerCase().startsWith(charText))
                    {
                        arrayList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
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



}
