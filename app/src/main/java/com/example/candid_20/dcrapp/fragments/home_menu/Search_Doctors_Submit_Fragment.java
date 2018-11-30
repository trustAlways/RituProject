package com.example.candid_20.dcrapp.fragments.home_menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Build;
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
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.bean.for_search_doctor.SearchDoctor_Bean;
import com.example.candid_20.dcrapp.bean.for_search_doctor.SearchDoctor_LxDetails_Bean;
import com.example.candid_20.dcrapp.bean.for_search_doctor_after_submit.SearchDoctorSubmit_Bean;
import com.example.candid_20.dcrapp.bean.for_search_doctor_after_submit.SearchDoctorSubmit_LxDetails_Bean;
import com.example.candid_20.dcrapp.constant.Utils;
import com.example.candid_20.dcrapp.fragments.home_menu.search_doctor_after.CallStart_Fragment;
import com.example.candid_20.dcrapp.fragments.home_menu.search_doctor_after.Doctors_Reminder_Fragment;
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

public class Search_Doctors_Submit_Fragment extends Fragment implements View.OnClickListener {



    View v;
    RelativeLayout rr_search,rr_InboxDetailRVv,rr_InboxDetailRV,
            rr_docreminder_checked_unchecked,rr_cllstrt_docreminder;

    RecyclerView InboxDetailRV,InboxDetailRVv;
    ArrayList<String> l1;
    EditText txt_search_doctor;
    ProgressBar loader;
    // Sharedpreferences Class
    MySharedPref sp;
    //String Sharedpreferences Data
    String ldata,user_id4,token,company_name,role_id,mr_id;

    SearchDoctorSubmit_Bean searchDoctorSubmit_bean;
    CustomSearch_Doctors_Adp customSearch_doctors_adp;
    CustomSearch_Search_Doctors_Adp customSearch_search_doctors_adp;
    String my_String;
    int check_position=0;
    SearchDoctor_Bean searchDoctor_bean;

TextView txt_title;
RelativeLayout rr_cllstrt_checked_unchecked;
    //------------------------------------ Initialise boolean-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    boolean isGPSEnabled = false;
    //------------------------------------ Initialise double-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    double currentlat,currentlong;

String doc_id,dcr_live_table_insert_id,dcr_table_insert_id,doc_name,speciality_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.search_doctor_after, container, false);

        // ---------------------------- For Get Current Location -------------------------------------------------------------------------------//
        getLocation();
        // ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//
        getSaveddata();
        // ---------------------------- For  Bundle Data -------------------------------------------------------------------------------//

        getBundleData();
        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();

        return v;
    }
    // ---------------------------- For  Bundle Data -------------------------------------------------------------------------------//
    private void getBundleData() {

        Bundle b1=this.getArguments();
         my_String = b1.getString("my_String");

         }



    public static Search_Doctors_Submit_Fragment newInstance() {
        Search_Doctors_Submit_Fragment fragment = new Search_Doctors_Submit_Fragment();
        return fragment;
    }



    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
    private void initUi() {
        doc_id="null";
        // ---------------------------- Casting Element -------------------------------------------------------------------------------//
        //Casting RecycleView for Search Doctor
        InboxDetailRV = (RecyclerView) v.findViewById(R.id.InboxDetailRV);
        //Casting EditText for Search Doctor
        txt_search_doctor=(EditText)v.findViewById(R.id.txt_search_doctor);

        //Casting RecycleView for Search Doctor Click Search icon
        InboxDetailRVv = (RecyclerView) v.findViewById(R.id.InboxDetailRVv);

        //Casting RelativeLayout for Search Doctor Click Search icon
        rr_search=(RelativeLayout)v.findViewById(R.id.rr_search);
      //Casting RelativeLayout for Search Doctor Click Start and Doctor Reminder
        rr_cllstrt_docreminder=(RelativeLayout)v.findViewById(R.id.rr_cllstrt_docreminder);

        //Casting RelativeLayout for Call Start
        rr_cllstrt_checked_unchecked=(RelativeLayout)v.findViewById(R.id.rr_cllstrt_checked_unchecked);
        //Casting RelativeLayout for Search Doctor Click Search icon
        rr_InboxDetailRVv=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRVv);
        //Casting RelativeLayout for Search Doctor
        rr_InboxDetailRV=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRV);
        //Casting RelativeLayout for Doctor Reminder
        rr_docreminder_checked_unchecked=(RelativeLayout)v.findViewById(R.id.rr_docreminder_checked_unchecked);

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
        //   InboxDetailRV.smoothScrollToPosition(0);

        //Casting TextView for Title
        txt_title=(TextView)getActivity().findViewById(R.id.txt_title);

        //Set Text Title
        txt_title.setText("Search Doctor");

        // ---------------------------- ArrayList set -------------------------------------------------------------------------------//
        l1=new ArrayList<>();
        l1.add("abc");
        l1.add("Review & End Field Work");
        l1.add("View DCR");

        callWebservicefor_getAllInteriorAcctoCity(my_String);



// ---------------------------- setOnTextChange Listner-------------------------------------------------------------------------------//

 //For Search Doctor

        txt_search_doctor.addTextChangedListener(new TextWatcher() {
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
                    // TODO Auto-generated method stub

//callWebservicefor_getAllInteriorAcctoCity(s.toString());

                  //  System.out.println("Search Doctor Size###"+searchDoctor_bean.getResult().size());
                    if(searchDoctor_bean!=null) {

                        if (searchDoctor_bean.getResult().size() > 0) {
                            System.out.println("Search Doctor Size Above###"+searchDoctor_bean.getResult().size());
                            rr_cllstrt_docreminder.setVisibility(View.GONE);
                            rr_InboxDetailRV.setVisibility(View.GONE);
                            InboxDetailRV.setVisibility(View.GONE);

                            //Set Visiblity of RelativeLayout and RecyclerView
                            rr_InboxDetailRVv.setVisibility(View.VISIBLE);
                            InboxDetailRVv.setVisibility(View.VISIBLE);

                            //Set Adaptetr
                      //  InboxDetailRVv.setAdapter(customSearch_search_doctors_adp);
                        //Filter Data
                            customSearch_search_doctors_adp.filter(s.toString());
                        }

                        if(searchDoctor_bean.getResult().size() == 0)
                        {
                            System.out.println("Search Doctor Size Equal###"+searchDoctor_bean.getResult().size());

                            //    callWebservicefor_getAllInteriorAcctoCity("");
                            //Filter Data
                            customSearch_search_doctors_adp.filter(s.toString());



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
                    rr_InboxDetailRVv.setVisibility(View.GONE);
                    InboxDetailRVv.setVisibility(View.GONE);
                    //edt_selct_city.setText("");
                }

                //   edt_selct_territory.setText("");


            }
        });






        // ---------------------------- RecycleView onTouchListner -------------------------------------------------------------------------------//

        //RecycleView onTouchListner

       /* InboxDetailRV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // disallow scrollview to intercept touch events
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        // allow scrollview to intercept touch events
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }

                v.onTouchEvent(event);
                return true;
            }
        });
*/

        // ---------------------------- Set OnClickListner -------------------------------------------------------------------------------//
        // onClickListner of RelativeLayout for Search Doctor Click when click Search icon
        rr_search.setOnClickListener(this);

        //onClickListner of RelativeLayout for Call Start
        rr_cllstrt_checked_unchecked.setOnClickListener(this);
//onClickListner of RelativeLayout for Doctor Reminder
        rr_docreminder_checked_unchecked.setOnClickListener(this);

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
                role_id = jsonObject.getString("role_id");

                mr_id = sp.getData(getActivity(),"mr_id","");


                Log.e("Id is@@@",user_id4);

                dcr_live_table_insert_id=sp.getData(getActivity(),"dcr_live_table_insert_id","null");
                dcr_table_insert_id=sp.getData(getActivity(),"dcr_table_insert_id","null");

                System.out.println("dcr live id***" + dcr_live_table_insert_id + dcr_table_insert_id);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
    // ---------------------------- For WebService Call for get Search Doctor Suggestion -------------------------------------------------------------------------------//
    private void getData_ForSearchSuggestion(final String cityid4) {

        String url = "http://dailyreporting.in/"+company_name+"/api/search_doctor_suggetion_submit";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_SEARCH_DOCTOR_SUGGETION_SUBMIT,
                new com.android.volley.Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("All City List AccState", "All City List AccStatet response12324343: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    String call_status = jObj.getString("callstatus");
                    String docid = jObj.getString("doctor");

                    String visit_type = jObj.getString("visittype");
                    String visit_remind_docter = jObj.getString("doctorremid");

                    System.out.println("List Size interior:"+call_status);

                    sp.saveData(getActivity(),"check_out_time",call_status);
                    sp.saveData(getActivity(),"doccc_idd",docid);

                    sp.saveData(getActivity(),"visit_type_reminder",visit_type);
                    sp.saveData(getActivity(),"doccc_remind_idd",visit_remind_docter);


                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);

                        Gson gson = new Gson();

                        searchDoctorSubmit_bean=gson.fromJson(response,SearchDoctorSubmit_Bean.class);
                        System.out.println("List Size interior:"+searchDoctorSubmit_bean.getResult().size());
                        /*System.out.println("Base URl Old^^^"+str_base_url);
                        String str_new_base_url=str_base_url.replace("candid-15-pc","192.168.1.2");
                        System.out.println("Base URl New^^^"+str_new_base_url);*/

                        if(searchDoctorSubmit_bean.getResult().size()>0)
                        {
                            //   btn_add_products.setVisibility(View.VISIBLE);
                            // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//
                            customSearch_doctors_adp = new CustomSearch_Doctors_Adp(getActivity(),searchDoctorSubmit_bean.getResult());
                            InboxDetailRV.setAdapter(customSearch_doctors_adp);
                            rr_InboxDetailRV.setVisibility(View.VISIBLE);
                            InboxDetailRV.setVisibility(View.VISIBLE);

                            if (!mr_id.equalsIgnoreCase("0") && mr_id!=null)
                            {
                                getData_ForSearchSuggestionn("");
                            }
                            else
                            {
                                getData_for_docter_without_mr();
                            }


                        }
                        else
                        {
                            rr_InboxDetailRV.setVisibility(View.GONE);
                            InboxDetailRV.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Currently No doctor allocated to you.", Toast.LENGTH_SHORT).show();

                        }

                        String errorMsg = jObj.getString("message");
                        if(getActivity()!=null) {
                         /*     txt_avail_pro.setVisibility(View.VISIBLE);
                                txt_avail_pro.setText("No products added yet.");*/
                            //  Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                        }
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

                    if(error.getMessage()!=null) {
                      /*  JSONObject jsonObject22 = new JSONObject(error.getMessage());

                        String errorMsg = jsonObject22.getString("message");*/
                        //  JSONObject jsonObject33=new JSONObject(message);
                        //   String password=jsonObject33.getString("password");

                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
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
                System.out.println("Search String###"+role_id);
                System.out.println("Search String###"+dcr_table_insert_id);

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id4);
                params.put("role_id",role_id);
                params.put("dcr_id",dcr_table_insert_id);
                params.put("search_string",cityid4);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }

    @Override
    public void onClick(View v) {
        if(v==rr_search)
        {


            if(txt_search_doctor.getText().length()>0)

            {

                Search_Doctors_Submit_Fragment selectedFragment = Search_Doctors_Submit_Fragment.newInstance();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                Bundle b = new Bundle();
                b.putString("my_String", txt_search_doctor.getText().toString());
                selectedFragment.setArguments(b);
                transaction.replace(R.id.contentFrame, selectedFragment);
                transaction.addToBackStack("8");
                transaction.commit();

                txt_search_doctor.setText("");
            /*    rr_InboxDetailRV.setVisibility(View.GONE);
                rr_InboxDetailRVv.setVisibility(View.VISIBLE);
                InboxDetailRV.setVisibility(View.GONE);
                InboxDetailRVvv.setVisibility(View.VISIBLE);*/
            }

            else
            {
                Toast.makeText(getActivity(),"Please Enter Some Input",Toast.LENGTH_SHORT);
            }
        }

        if(v==rr_cllstrt_checked_unchecked) {

          String  dcr_doctor_checkout_time=sp.getData(getActivity(),"check_out_time","null");
          String  dcr_callstrt_id=sp.getData(getActivity(),"doccc_idd","null");

          String  dcr_doctor_visit_type=sp.getData(getActivity(),"visit_type_reminder","null");
          String  dcr_callstrt_idd=sp.getData(getActivity(),"doccc_remind_idd","null");

            System.out.println("Doc Id###" + dcr_callstrt_id);
            System.out.println("dcr_doctor_checkout_time###" + dcr_doctor_checkout_time);

           if (dcr_doctor_checkout_time.equalsIgnoreCase("1") && dcr_doctor_visit_type.equalsIgnoreCase("0"))
           {
               showDialog(getActivity(),dcr_callstrt_id);
           }
           else  if (dcr_doctor_checkout_time.equalsIgnoreCase("1") && dcr_doctor_visit_type.equalsIgnoreCase("1"))
           {
               showDialog2(getActivity(),dcr_callstrt_idd);
           }

           else if (dcr_doctor_checkout_time.equalsIgnoreCase("null") ||dcr_doctor_checkout_time.equalsIgnoreCase("0") )
           {
               if (doc_id != null) {
                   if (!doc_id.equalsIgnoreCase("null"))

                   {
                       // ---------------------------- Call Webservice for Start Call-------------------------------------------------------------------------------//
                       callWebservicefor_get();
                       // Toast.makeText(getActivity(),dcr_doctor_live_table_insert_id,Toast.LENGTH_SHORT).show();
                   }

                   else
                   {
                       Toast.makeText(getActivity(),"Please Select Doctor for Call Start",Toast.LENGTH_SHORT).show();
                   }
               }
               else
               {
                   Toast.makeText(getActivity(),"Please Select Doctor for Call Start",Toast.LENGTH_SHORT).show();
               }
           }
           else
           {
               if (doc_id != null) {
                if (!doc_id.equalsIgnoreCase("null"))

                {
                    // ---------------------------- Call Webservice for Start Call-------------------------------------------------------------------------------//
                   callWebservicefor_get();
                   // Toast.makeText(getActivity(),dcr_doctor_live_table_insert_id,Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Toast.makeText(getActivity(),"Please Select Doctor for Call Start",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getActivity(),"Please Select Doctor for Call Start",Toast.LENGTH_SHORT).show();
            }
           }


        }

        if(v==rr_docreminder_checked_unchecked)
        {
            String  dcr_doctor_visit_type=sp.getData(getActivity(),"visit_type_reminder","null");
            String  dcr_callstrt_id=sp.getData(getActivity(),"doccc_remind_idd","null");

            String  dcr_doctor_checkout_time=sp.getData(getActivity(),"check_out_time","null");
            String  dcr_callstrt_idd=sp.getData(getActivity(),"doccc_idd","null");

            System.out.println("Doc Id###" + dcr_callstrt_id);
            System.out.println("dcr_doctor_visit_type###" + dcr_doctor_visit_type);
            System.out.println("Doc Id###" + doc_id);

            if (dcr_doctor_checkout_time.equalsIgnoreCase("1") && dcr_doctor_visit_type.equalsIgnoreCase("1"))
            {
                showDialog2(getActivity(),dcr_callstrt_id);
            }
            else  if (dcr_doctor_checkout_time.equalsIgnoreCase("1") && dcr_doctor_visit_type.equalsIgnoreCase("0"))
            {
                showDialog(getActivity(),dcr_callstrt_idd);
            }

            else if (dcr_doctor_visit_type.equalsIgnoreCase("null") ||dcr_doctor_visit_type.equalsIgnoreCase("0") )
            {
                if (doc_id != null) {
                    if (!doc_id.equalsIgnoreCase("null")) {
                        // ---------------------------- Call Webservice for Doctor Reminder-------------------------------------------------------------------------------//
                        callWebservicefor_doctorReminder();

                    } else {
                        Toast.makeText(getActivity(), "Please Select Doctor for Doctor Reminder", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Select Doctor for Doctor Reminder", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                if (doc_id != null) {
                    if (!doc_id.equalsIgnoreCase("null"))

                    {
                        // ---------------------------- Call Webservice for Start Call-------------------------------------------------------------------------------//
                           callWebservicefor_doctorReminder();
                        // Toast.makeText(getActivity(),dcr_doctor_live_table_insert_id,Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        Toast.makeText(getActivity(), "Please Select Doctor for Doctor Reminder", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Please Select Doctor for Doctor Reminder", Toast.LENGTH_SHORT).show();
                }
            }
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
    // ---------------------------- Class for RecycleView for Search Doctor Adapter-------------------------------------------------------------------------------//
    private class CustomSearch_Doctors_Adp extends RecyclerView.Adapter<CustomSearch_Doctors_Adp.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<SearchDoctorSubmit_LxDetails_Bean> arr_all_search_doctors3;
        List<SearchDoctorSubmit_LxDetails_Bean> arrayList = new ArrayList<>();
        List<SearchDoctorSubmit_LxDetails_Bean> arSearchlist;
        int selectedPosition = -1;
        RelativeLayout rr_left_checkuncheck;
        int numberOfCheckboxesChecked = 0;

        public CustomSearch_Doctors_Adp(Activity activity, List<SearchDoctorSubmit_LxDetails_Bean> arr_all_search_doctors2) {

            this.activity = activity;

            this.arr_all_search_doctors3 = arr_all_search_doctors2;

            System.out.println("List Size###"+arr_all_search_doctors3.size());

            this.arrayList = arr_all_search_doctors2;
            this.arSearchlist = new ArrayList<SearchDoctorSubmit_LxDetails_Bean>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size###"+arr_all_search_doctors3.size());
        }


        @Override
        public CustomSearch_Doctors_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_doctor_adp2, parent, false);

            return new CustomSearch_Doctors_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomSearch_Doctors_Adp.MyViewHolder holder, final int position) {
            String str_doc_name=arr_all_search_doctors3.get(position).getDoctorName();
            String txt_degree=arr_all_search_doctors3.get(position).getSpeciality();
            String txt_doc_city=arr_all_search_doctors3.get(position).getCityName();

            String upperString_str_doc_name = str_doc_name.substring(0,1).toUpperCase() + str_doc_name.substring(1);
            holder.txt_doctor_namee.setText(upperString_str_doc_name);

            String upperString_str_doc_speciality = txt_degree.substring(0,1).toUpperCase() + txt_degree.substring(1);
            holder.txt_profession.setText(upperString_str_doc_speciality);

            if (!txt_doc_city.equalsIgnoreCase(""))
            {
                String upperString_str_doc_city = txt_doc_city.substring(0,1).toUpperCase() + txt_doc_city.substring(1);
                holder.txt_doc_city.setText(upperString_str_doc_city);
            }
            else
            {
                holder.txt_doc_city.setText("Not Provided");
            }


            holder.chk_left_checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;

                    String  dcr_doctor_visit_type=sp.getData(getActivity(),"visit_type_reminder","null");
                    String  dcr_callstrt_id=sp.getData(getActivity(),"doccc_remind_idd","null");

                    String  dcr_doctor_checkout_time=sp.getData(getActivity(),"check_out_time","null");
                    String  dcr_callstrt_idd=sp.getData(getActivity(),"doccc_idd","null");

                    System.out.println("Doc Id###" + dcr_callstrt_id);
                    System.out.println("dcr_doctor_visit_type###" + dcr_doctor_visit_type);
                    System.out.println("Doc Id###" + doc_id);

                    if (dcr_doctor_visit_type.equalsIgnoreCase("1") && dcr_doctor_checkout_time.equalsIgnoreCase("1"))
                    {
                        showDialog2(getActivity(),dcr_callstrt_id);
                    }
                    else  if (dcr_doctor_checkout_time.equalsIgnoreCase("1") && dcr_doctor_visit_type.equalsIgnoreCase("0"))
                    {
                        showDialog(getActivity(),dcr_callstrt_idd);
                    }
                    else {


                        Integer pos = (Integer) holder.chk_left_checked.getTag();
                        // Toast.makeText(ctx, imageModelArrayList.get(pos).getAnimal() + " clicked!", Toast.LENGTH_SHORT).show();
                        if (cb.isChecked() && numberOfCheckboxesChecked >= 1) {
                            cb.setChecked(false);
                            // Toast.makeText(getActivity(), "Max allowed three checkbox only", Toast.LENGTH_LONG).show();
                            // notifyDataSetChanged();

                        } else if (cb.isChecked()) {
                            //    MySharedPref.bean_list.get(pos).setSelected(true);

                            numberOfCheckboxesChecked++;
                            System.out.println("Int Selected###" + numberOfCheckboxesChecked);


                            doc_id = arrayList.get(position).getDoctorId();
                            doc_name = arrayList.get(position).getDoctorName();
                            speciality_id = arrayList.get(position).getSpecialityId();


                            //   notifyDataSetChanged();

                        } else {
                            //     MySharedPref.bean_list.get(pos).setSelected(false);


                            numberOfCheckboxesChecked--;
                            System.out.println("Int UnSelected###" + numberOfCheckboxesChecked);


                            // qty_lst.remove(l1.get(position));

                            //  customGetProducts_adp.notifyDataSetChanged();
                            //
                        }

                    }


                }
            });

          /* // holder.txt_doctor_namee.setText(arr_all_search_doctors3.get(position).getDoctorName());
            rr_left_checkuncheck.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        System.out.println("Check Position###"+check_position);
       int arr_list_pos= Integer.parseInt(arrayList.get(position).getIndex());
        System.out.println("Get Position###"+arr_list_pos);

        System.out.println("Current Position###"+position);


        if(position==arr_list_pos) {

            System.out.println("Check Position###"+check_position);
         holder.img_left_checked.setImageResource(R.drawable.checkbox_icon);

         doc_id=arrayList.get(position).getDoctorId();
            doc_name=arrayList.get(position).getDoctorName();
            speciality_id=arrayList.get(position).getSpecialityId();
         notifyDataSetChanged();

*//* if (check_position == 0) {
          check_position=1;
                holder.img_left_checked.setImageResource(R.drawable.checkbox_icon);
         // check_position=1;

          notifyDataSetChanged();
          check_position=1;
            }

            if (check_position == 1) {

                holder.img_left_checked.setImageResource(R.drawable.unchecked_box_icon);
                holder.img_left_checked.setImageResource(R.drawable.unchecked_box_icon);
                check_position=0;
            }*//*
        }

        else
        {
            holder.img_left_checked.setImageResource(R.drawable.unchecked_box_icon);
            notifyDataSetChanged();
            }

            }

});*/

        }





        @Override
        public int getItemCount() {
            return arr_all_search_doctors3.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_doctor_namee, txt_profession ,txt_doc_city;
            ImageView img_left_checked;
            CheckBox chk_left_checked;
            public MyViewHolder(View itemView) {
                super(itemView);
                txt_doctor_namee = (TextView) itemView.findViewById(R.id.txt_doctor_namee);
                txt_profession = (TextView) itemView.findViewById(R.id.txt_degree);
                txt_doc_city = (TextView) itemView.findViewById(R.id.txt_doc_city_namee);

                img_left_checked=(ImageView)itemView.findViewById(R.id.img_left_checked);
                rr_left_checkuncheck=(RelativeLayout)itemView.findViewById(R.id.rr_left_checkuncheck);

                chk_left_checked=(CheckBox) itemView.findViewById(R.id.chk_left_checked);
            }
        }
            public void filter(String charText) {
                charText = charText.toString().toLowerCase();
                System.out.println("Character in Filter is###"+charText);
                arrayList.clear();
                if (charText.length() == 0) {
                    arrayList.addAll(arSearchlist);
                } else {
                    for (SearchDoctorSubmit_LxDetails_Bean wp : arSearchlist) {
                        if (wp.getDoctorName().toLowerCase().startsWith(charText))
                        {
                            arrayList.add(wp);
                        }
                    }
                }
                notifyDataSetChanged();

        }
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

    // ---------------------------- WebService Call for Get Call Start-------------------------------------------------------------------------------//
    private void callWebservicefor_get() {


        if(Utils.isConnected(getActivity())) {

            get_Data_forGetCallStart();
        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }
    // ---------------------------- WebService Call for Get Doctor Reminder-------------------------------------------------------------------------------//
    private void callWebservicefor_doctorReminder() {


        if(Utils.isConnected(getActivity())) {
            get_Data_forDoctorReminder();
        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }

     // ---------------------------- For  WebService Call Method for Call Start-------------------------------------------------------------------------------//
    private void get_Data_forGetCallStart() {

        String url = "http://dailyreporting.in/"+company_name+"/api/dcr_call_start";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_DCR_CALL_WORK, new Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DCRCallWork", "DCR Call Work: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    String message=jObj.getString("message");
                    Log.d("DCRCallWork", "DCR Call Work: " + message);

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);

                        String result=jObj.getString("result");
                        System.out.println("Result DCR Call Work***"+result);

                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                        JSONObject jsonObject44=new JSONObject(result);
                        String dcr_doctor_live_table_insert_id=jsonObject44.getString("dcr_doctor_live_table_insert_id");
                        String dcr_doctor_table_insert_id=jsonObject44.getString("dcr_doctor_table_insert_id");
                        String docid=jsonObject44.getString("doctor_id");

                        MySharedPref sp=new MySharedPref();
                        sp.saveData(getActivity(),"dcr_doctor_live_table_insert_id",dcr_doctor_live_table_insert_id);
                        sp.saveData(getActivity(),"dcr_doctor_table_insert_id",dcr_doctor_table_insert_id);
                        sp.saveData(getActivity(),"doc_id",doc_id);
                        sp.saveData(getActivity(),"doccc_idd",docid);

                        CallStart_Fragment selectedFragment = CallStart_Fragment.newInstance();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        Bundle b=new Bundle();
                        b.putString("doc_id",doc_id);
                        selectedFragment.setArguments(b);
                        transaction.replace(R.id.contentFrame, selectedFragment);
                        transaction.addToBackStack("10");
                        transaction.commit();

                    }
                    else  if (error.equals("error") ) {
                        loader.setVisibility(View.GONE);
                        String message2=jObj.getString("message");
                        Toast.makeText(getActivity(), message2, Toast.LENGTH_LONG).show();
                    }
                    else {
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
                   /* JSONObject jsonObject22=new JSONObject(error.getMessage());

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
                params.put("doctor_id",doc_id);
                params.put("dr_name",doc_name);
                params.put("speciality",speciality_id);
                params.put("visit_type","0");
                params.put("dcr_live_table_insert_id",dcr_live_table_insert_id);

                System.out.println("live id #123456"+ dcr_live_table_insert_id);
                System.out.println("tb i id #123456"+ dcr_table_insert_id);

                params.put("dcr_table_insert_id",dcr_table_insert_id);
                params.put("checkin_lat", String.valueOf(currentlat));
                params.put("checkin_long", String.valueOf(currentlong));
                params.put("user_id",user_id4);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }


    // ---------------------------- For WebService Call for get Search Doctor Suggestion -------------------------------------------------------------------------------//
    private void getData_ForSearchSuggestionn(final String cityid4) {

        final String city_id_local = sp.getData(getActivity(),"selected_city_id_local","");
        final String city_id = sp.getData(getActivity(),"selected_city_id","");


        String url = "http://dailyreporting.in/"+company_name+"/api/search_doctor_suggetion";
        System.out.println("sout url "+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_SEARCH_DOCTOR_SUGGETION,
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

                        searchDoctor_bean=gson.fromJson(response,SearchDoctor_Bean.class);
                        System.out.println("List Size interior:"+searchDoctor_bean.getResult().size());
                        /*System.out.println("Base URl Old^^^"+str_base_url);
                        String str_new_base_url=str_base_url.replace("candid-15-pc","192.168.1.2");
                        System.out.println("Base URl New^^^"+str_new_base_url);*/

                        if(searchDoctor_bean.getResult().size()>0)
                        {
                            //   btn_add_products.setVisibility(View.VISIBLE);
                            // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//
                            customSearch_search_doctors_adp = new CustomSearch_Search_Doctors_Adp(getActivity(),searchDoctor_bean.getResult());
                            InboxDetailRVv.setAdapter(customSearch_search_doctors_adp);
                           /* rr_InboxDetailRV.setVisibility(View.VISIBLE);
                            InboxDetailRV.setVisibility(View.VISIBLE);*/
                        }

                        else
                        {
                            rr_InboxDetailRVv.setVisibility(View.GONE);
                            InboxDetailRVv.setVisibility(View.GONE);
                        }
                        String errorMsg = jObj.getString("message");
                        if(getActivity()!=null) {
                         /*   txt_avail_pro.setVisibility(View.VISIBLE);
                            txt_avail_pro.setText("No products added yet.");*/
                            //  Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                        }




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

                    if(error.getMessage()!=null) {
                     /*   JSONObject jsonObject22 = new JSONObject(error.getMessage());

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
                params.put("role_id",role_id);
                params.put("search_string","");

                if (!role_id.equalsIgnoreCase("5"))
                {
                    params.put("mr_id",mr_id);
                }

                if (city_id_local.equalsIgnoreCase(""))
                {
                    System.out.println("city_id###"+city_id);
                    params.put("city_id",city_id);
                }
                else
                {
                    System.out.println("city_id_local ###"+city_id_local);
                    params.put("city_id",city_id_local);
                }

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }




    // ---------------------------- For WebService Call for get Search Doctor Suggestion -------------------------------------------------------------------------------//
    private void getData_for_docter_without_mr() {

        final String city_id_local = sp.getData(getActivity(),"selected_city_id_local","");
        final String city_id = sp.getData(getActivity(),"selected_city_id","");


        String url = "http://dailyreporting.in/"+company_name+"/api/doctor_list_with_city";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_SEARCH_DOCTOR_SUGGETION,
                new com.android.volley.Response.Listener<String>() {*/
        //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("All docter list", "All doctor List: 12344 " + response.toString());


                        try {
                            JSONObject jObj = new JSONObject(response);
                            String error = jObj.getString("status");

                            if (error.equals("success")) {
                                loader.setVisibility(View.GONE);

                                Gson gson = new Gson();

                                searchDoctor_bean=gson.fromJson(response,SearchDoctor_Bean.class);
                                System.out.println("List Size interior:"+searchDoctor_bean.getResult().size());
                        /*System.out.println("Base URl Old^^^"+str_base_url);
                        String str_new_base_url=str_base_url.replace("candid-15-pc","192.168.1.2");
                        System.out.println("Base URl New^^^"+str_new_base_url);*/

                                if(searchDoctor_bean.getResult().size()>0)
                                {
                                    //   btn_add_products.setVisibility(View.VISIBLE);
                                    // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//

                                    customSearch_search_doctors_adp = new CustomSearch_Search_Doctors_Adp(getActivity(),searchDoctor_bean.getResult());
                                    InboxDetailRVv.setAdapter(customSearch_search_doctors_adp);
                           /* rr_InboxDetailRV.setVisibility(View.VISIBLE);
                            InboxDetailRV.setVisibility(View.VISIBLE);*/
                                }

                                else
                                {
                                    rr_InboxDetailRVv.setVisibility(View.GONE);
                                    InboxDetailRVv.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "Currently No doctor allocated to you.", Toast.LENGTH_LONG).show();
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

                    if(error.getMessage()!=null) {
                       /* JSONObject jsonObject22 = new JSONObject(error.getMessage());

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
                System.out.println("Search id 2###"+user_id4);
                System.out.println("Search String 2###"+"");
                System.out.println("Search id 2###"+role_id);
                System.out.println("Search String 2###"+city_id);

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id4);
                params.put("role_id",role_id);
                params.put("search_string","");

                if (city_id_local.equalsIgnoreCase(""))
                {
                    System.out.println("city_id###"+city_id);
                    params.put("city_id",city_id);
                }
                else
                {
                    System.out.println("city_id_local ###"+city_id_local);
                    params.put("city_id",city_id_local);
                }

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }



    //-----------------------------------------ytaha tak chalegi


    // ---------------------------- For  WebService Call Method for Doctor Reminder-------------------------------------------------------------------------------//
    private void get_Data_forDoctorReminder() {

        String url = "http://dailyreporting.in/"+company_name+"/api/dcr_call_start";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        /*StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_DCR_CALL_WORK, new Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
               url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DCRCallWork", "DCR Call Work: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    String message=jObj.getString("message");

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);

                        String result=jObj.getString("result");
                        System.out.println("Result DCR Call Work***"+result);

                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                        JSONObject jsonObject44=new JSONObject(result);
                        String dcr_doctor_live_table_insert_id=jsonObject44.getString("dcr_doctor_live_table_insert_id");
                        String dcr_doctor_table_insert_id=jsonObject44.getString("dcr_doctor_table_insert_id");

                        MySharedPref sp=new MySharedPref();
                        sp.saveData(getActivity(),"dcr_doctor_live_table_insert_id",dcr_doctor_live_table_insert_id);
                        sp.saveData(getActivity(),"dcr_doctor_table_insert_id",dcr_doctor_table_insert_id);
                        sp.saveData(getActivity(),"doc_id",doc_id);
                        sp.saveData(getActivity(),"doccc_idd",doc_id);

                        Doctors_Reminder_Fragment selectedFragment = Doctors_Reminder_Fragment.newInstance();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                        Bundle b=new Bundle();
                        b.putString("doc_id",doc_id);
                        selectedFragment.setArguments(b);
                        transaction.replace(R.id.contentFrame, selectedFragment);
                        transaction.addToBackStack("11");
                        transaction.commit();

                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            Log.e("errorMsg", message);

                        }


                    }
                    else  if (error.equals("error")) {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }

                    else {
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
                   /* JSONObject jsonObject22=new JSONObject(error.getMessage());

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
                params.put("doctor_id",doc_id);
                params.put("dr_name",doc_name);
                params.put("speciality",speciality_id);
                params.put("visit_type","1");
                params.put("dcr_live_table_insert_id",dcr_live_table_insert_id);
                params.put("dcr_table_insert_id",dcr_table_insert_id);
                params.put("checkin_lat", String.valueOf(currentlat));
                params.put("checkin_long", String.valueOf(currentlong));
                params.put("user_id",user_id4);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }
    // ---------------------------- Class for RecycleView for Search Doctor Adapter-------------------------------------------------------------------------------//
    private class CustomSearch_Search_Doctors_Adp extends RecyclerView.Adapter<CustomSearch_Search_Doctors_Adp.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<SearchDoctor_LxDetails_Bean> arr_all_search_doctors3;
        List<SearchDoctor_LxDetails_Bean> arrayList = new ArrayList<>();
        List<SearchDoctor_LxDetails_Bean> arSearchlist;

        public CustomSearch_Search_Doctors_Adp(Activity activity, List<SearchDoctor_LxDetails_Bean> arr_all_search_doctors2) {

            this.activity = activity;

            this.arr_all_search_doctors3 = arr_all_search_doctors2;

            this.arrayList = arr_all_search_doctors2;
            this.arSearchlist = new ArrayList<SearchDoctor_LxDetails_Bean>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size###"+arr_all_search_doctors3.size());
        }


        @Override
        public CustomSearch_Search_Doctors_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_doctor_adp, parent, false);

            return new CustomSearch_Search_Doctors_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomSearch_Search_Doctors_Adp.MyViewHolder holder, final int position) {


            String str_interior_name= arr_all_search_doctors3.get(position).getDoctorName();

            String upperString_str_interior_name = str_interior_name.substring(0,1).toUpperCase() + str_interior_name.substring(1);
            holder.txt_current_loc_descp.setText(upperString_str_interior_name);


            String str_specilast= arr_all_search_doctors3.get(position).getSpeciality();

            String upperString_str_specilast = str_specilast.substring(0,1).toUpperCase() + str_specilast.substring(1);
            holder.txt_profession.setText(upperString_str_specilast);

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
            return arr_all_search_doctors3.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_current_loc_descp,txt_profession;
            RelativeLayout rr_current_loc_descp;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt_current_loc_descp = (TextView) itemView.findViewById(R.id.txt_current_loc_descp);
                txt_profession= (TextView) itemView.findViewById(R.id.txt_profession);
                rr_current_loc_descp= (RelativeLayout) itemView.findViewById(R.id.rr_current_loc_descp);
            }


        }

        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###"+charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (SearchDoctor_LxDetails_Bean wp : arSearchlist) {
                    if (wp.getDoctorName().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        arrayList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public void showDialog(Activity activity, final String doc_id){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dcr_alert_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CallStart_Fragment selectedFragment = CallStart_Fragment.newInstance();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle b=new Bundle();
                b.putString("doc_id",doc_id);
                selectedFragment.setArguments(b);
                transaction.replace(R.id.contentFrame, selectedFragment);
                transaction.addToBackStack("10");
                transaction.commit();
               // Toast.makeText(getActivity(),"You already started a call"+ doc_id,Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();

    }

    public void showDialog2(Activity activity, final String doc_id){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dcr_alert_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Doctors_Reminder_Fragment selectedFragment = Doctors_Reminder_Fragment.newInstance();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                Bundle b=new Bundle();
                b.putString("doc_id",doc_id);
                selectedFragment.setArguments(b);
                transaction.replace(R.id.contentFrame, selectedFragment);
                transaction.addToBackStack("11");
                transaction.commit();
            }
        });

        dialog.show();

    }
}
