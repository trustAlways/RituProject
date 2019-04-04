package com.example.candid_20.dcrapp.fragments.home_menu;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.bean.for_city_list.CityList_LxDetails_Bean;
import com.example.candid_20.dcrapp.bean.for_interior.InteriorList_Bean;
import com.example.candid_20.dcrapp.bean.for_search_doctor.SearchDoctor_Bean;
import com.example.candid_20.dcrapp.bean.for_search_doctor.SearchDoctor_LxDetails_Bean;
import com.example.candid_20.dcrapp.constant.Utils;
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

public class Search_Doctors_Fragment extends Fragment implements View.OnClickListener {
    View v;
    RelativeLayout rr_search,rr_InboxDetailRVv,rr_InboxDetailRV;
    RecyclerView InboxDetailRV,InboxDetailRVvv;
    ArrayList<String> l1;
    EditText txt_search_doctor;
    ProgressBar loader;
    // Sharedpreferences Class
    MySharedPref sp;
    //String Sharedpreferences Data
    String ldata,user_id4,token,company_name,role_id;

    SearchDoctor_Bean searchDoctor_bean;
    CustomSearch_Doctors_Adp customSearch_doctors_adp;
    TextView txt_title;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.search_doctors, container, false);
        // ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//

        getSaveddata();
        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();


        return v;
    }

    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
    private void initUi() {
        // ---------------------------- Casting Element -------------------------------------------------------------------------------//
        //Casting RecycleView for Search Doctor
        InboxDetailRV = (RecyclerView) v.findViewById(R.id.InboxDetailRV);
        //Casting EditText for Search Doctor
        txt_search_doctor=(EditText)v.findViewById(R.id.txt_search_doctor);

        //Casting RecycleView for Search Doctor Click Search icon
        InboxDetailRVvv = (RecyclerView) v.findViewById(R.id.InboxDetailRVv);

        //Casting RelativeLayout for Search Doctor Click Search icon
        rr_search=(RelativeLayout)v.findViewById(R.id.rr_search);
        //Casting RelativeLayout for Search Doctor Click Search icon
        rr_InboxDetailRVv=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRVv);
        //Casting RelativeLayout for Search Doctor
        rr_InboxDetailRV=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRV);
        //Casting ProgressBar for loading

        loader=(ProgressBar)v.findViewById(R.id.loader);

        //Casting TextView for Title
        txt_title=(TextView)getActivity().findViewById(R.id.txt_title);

        //Set Text
        txt_title.setText("Search Doctor");

//For  Search DoctorRecycle Create by these

        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        InboxDetailRV.setLayoutManager(mLayoutManager2);
        InboxDetailRV.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
   //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRV.setItemAnimator(new DefaultItemAnimator());

        //DoctorRecycle Create by these for Search Doctor Click Search icon

        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity());
        InboxDetailRVvv.setLayoutManager(mLayoutManager3);
        InboxDetailRVvv.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRVvv.setItemAnimator(new DefaultItemAnimator());
     //   InboxDetailRV.smoothScrollToPosition(0);

        // ---------------------------- ArrayList set -------------------------------------------------------------------------------//
        l1=new ArrayList<>();
        l1.add("abc");
        l1.add("Review & End Field Work");
        l1.add("View DCR");

        callWebservicefor_getAllInteriorAcctoCity("");


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

                   // System.out.println("Search Doctor Size###"+searchDoctor_bean.getResult().size());
                    if(searchDoctor_bean!=null) {

                        if (searchDoctor_bean.getResult().size() > 0) {
                            System.out.println("Search Doctor Size Above###"+searchDoctor_bean.getResult().size());

                            //Set Visiblity of RelativeLayout and RecyclerView
                            rr_InboxDetailRV.setVisibility(View.VISIBLE);
                            InboxDetailRV.setVisibility(View.VISIBLE);

                           //Set Adaptetr
                           // InboxDetailRV.setAdapter(customSearch_doctors_adp);
                        //Filter Data
                            customSearch_doctors_adp.filter(s.toString());
                            }

                        if(searchDoctor_bean.getResult().size() == 0)
                        {
                            System.out.println("Search Doctor Size Equal###"+searchDoctor_bean.getResult().size());

                        //    callWebservicefor_getAllInteriorAcctoCity("");
                            //Filter Data
                            customSearch_doctors_adp.filter(s.toString());


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


    }
    // ---------------------------- WebService Call for Getall City-------------------------------------------------------------------------------//
    private void callWebservicefor_getAllInteriorAcctoCity(String city_id2) {


        if(Utils.isConnected(getActivity())) {

            final String mr_id = sp.getData(getActivity(),"mr_id","");

            if (!mr_id.equalsIgnoreCase("0") && mr_id!=null)
            {
                getData_ForSearchSuggestion("");
                System.out.println("mr id12465 "+ mr_id);
            }
            else
            {
                System.out.println("mr id12465 else "+ mr_id);
                getData_for_docter_without_mr(city_id2);
            }

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

        ldata = sp.getData(getActivity(), "ldata", "null");
        Log.e("LdataHome", ldata);

        if (ldata != null)

        {
            try {
                JSONObject jsonObject = new JSONObject(ldata);
                user_id4 = jsonObject.getString("user_id");
                token = jsonObject.getString("token");
                role_id = jsonObject.getString("role_id");


                Log.e("Id is@@@",user_id4);
                System.out.println("Id is***" + user_id4);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
    // ---------------------------- For WebService Call for get Search Doctor Suggestion -------------------------------------------------------------------------------//
    private void getData_ForSearchSuggestion(final String cityid4) {

        final String city_id_local = sp.getData(getActivity(),"selected_city_id_local","");
        final String city_id = sp.getData(getActivity(),"selected_city_id","");
        final String mr_id = sp.getData(getActivity(),"mr_id","");


        String url = "http://dailyreporting.in/"+company_name+"/api/search_doctor_suggetion";
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
                              customSearch_doctors_adp = new CustomSearch_Doctors_Adp(getActivity(),searchDoctor_bean.getResult());
                              InboxDetailRV.setAdapter(customSearch_doctors_adp);
                           /* rr_InboxDetailRV.setVisibility(View.VISIBLE);
                            InboxDetailRV.setVisibility(View.VISIBLE);*/
                        }

                        else
                        {
                            rr_InboxDetailRV.setVisibility(View.GONE);
                            InboxDetailRV.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Currently No doctor allocated to you.", Toast.LENGTH_LONG).show();
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
                System.out.println("Search id###"+user_id4);
                System.out.println("Search String###"+cityid4);
                System.out.println("Search id###"+role_id);
                System.out.println("Search String###"+city_id + "local" + city_id_local);
                System.out.println("Search String###"+mr_id);

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id4);
                params.put("role_id",role_id);
                params.put("search_string",cityid4);

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

    //---------------------------------------------------Api call for without mr id search docters----------------------------------------

    // ---------------------------- For WebService Call for get Search Doctor Suggestion -------------------------------------------------------------------------------//
    private void getData_for_docter_without_mr(final String cityid4) {

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
                                    customSearch_doctors_adp = new CustomSearch_Doctors_Adp(getActivity(),searchDoctor_bean.getResult());
                                    InboxDetailRV.setAdapter(customSearch_doctors_adp);
                                   /* rr_InboxDetailRV.setVisibility(View.VISIBLE);
                                    InboxDetailRV.setVisibility(View.VISIBLE);*/
                                }

                                else
                                {
                                    rr_InboxDetailRV.setVisibility(View.GONE);
                                    InboxDetailRV.setVisibility(View.GONE);
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
                System.out.println("Search String 2###"+cityid4);
                System.out.println("Search id 2###"+role_id);
                System.out.println("Search String 2###"+city_id);

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id4);
                params.put("role_id",role_id);
                params.put("search_string",cityid4);

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
                transaction.addToBackStack("9");
                transaction.commit();

                txt_search_doctor.setText("");
            }
            else {

                Toast.makeText(getActivity(), "Please Enter Some Input", Toast.LENGTH_SHORT);
            }
       /*     getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, new Search_Doctors_Fragment())
                    .addToBackStack("20")
                    .commit();*/
          /*  rr_InboxDetailRV.setVisibility(View.GONE);
            rr_InboxDetailRVv.setVisibility(View.VISIBLE);
            InboxDetailRV.setVisibility(View.GONE);
            InboxDetailRVvv.setVisibility(View.VISIBLE);*/




        }

    }



    // ---------------------------- Class for RecycleView GridSpacing-------------------------------------------------------------------------------//

    class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

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
        List<SearchDoctor_LxDetails_Bean> arr_all_search_doctors3;
        List<SearchDoctor_LxDetails_Bean> arrayList = new ArrayList<>();
        List<SearchDoctor_LxDetails_Bean> arSearchlist;

        public CustomSearch_Doctors_Adp(Activity activity, List<SearchDoctor_LxDetails_Bean> arr_all_search_doctors2) {

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
        public CustomSearch_Doctors_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_doctor_adp, parent, false);

            return new CustomSearch_Doctors_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomSearch_Doctors_Adp.MyViewHolder holder, final int position) {


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
            transaction.addToBackStack("9");
            transaction.commit();
            txt_search_doctor.setText("");

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






}
