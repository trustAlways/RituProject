package com.example.candid_20.dcrapp.fragments.home_menu.for_add_chemist;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
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
import com.example.candid_20.dcrapp.bean.for_chemist.For_ChemistList_Bean;
import com.example.candid_20.dcrapp.bean.for_chemist.For_ChemistList_Lxdetails_Bean;
import com.example.candid_20.dcrapp.bean.for_gift_list.Gift_List_Bean;
import com.example.candid_20.dcrapp.bean.for_gift_list.Gift_List_Lxdetails_Bean;
import com.example.candid_20.dcrapp.bean.for_stockiest.For_StockiestList_Bean;
import com.example.candid_20.dcrapp.bean.for_stockiest.For_StockiestList_Lxdetails_Bean;
import com.example.candid_20.dcrapp.constant.Utils;
import com.example.candid_20.dcrapp.fragments.home_menu.TimeLineFragment;
import com.example.candid_20.dcrapp.fragments.home_menu.search_doctor_after.Add_Products_Fragment;
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
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

public class Add_Chemist_Fragment extends Fragment implements View.OnClickListener {
    View v;
    TextView txt_title,txt_chname_error,txt_gift_error,txt_stckiest1_error,txt_pob_error;
    MySharedPref sp;
    String ldata,dcr_table_insert_id,user_id4,token,dcr_chemist_stockist_live_table_id;
ProgressBar loader;
EditText edt_chemist_name,spn_product_pobamount;
    RelativeLayout rr_InboxDetailRVvv,rr_InboxDetailRVvv_gift,
            rr_InboxDetailRVvv_stockiest,
            rr_InboxDetailRVvv_stockiest2,rr_InboxDetailRVvv_stockiest3
            ,rr_callcmplte_checked_unchecked;

    RecyclerView InboxDetailRVvv,InboxDetailRVvv_gift,
            InboxDetailRVvv_stockiest,InboxDetailRVvv_stockiest2,InboxDetailRVvv_stockiest3;
    EditText spn_product,edt_product_qty,
            spn_stockiest1,edt_stockiest1_qty,
            spn_stockiest2,edt_stockiest2_qty,
            spn_stockiest3,edt_stockiest3_qty;
    Gift_List_Bean dcr_products_bean;
    CustomSearch_Doctors_Gift_Adp customGetProducts_adp;
    int str_proquant,str_stockiest1quant;
    String str_product_id,str_chemist_proquant,str_stockiest_proquant,str_stockiest2_proquant,str_stockiest3_proquant;
    For_ChemistList_Bean for_chemistList_bean;
    CustomSearch_Chemist_Adp customSearch_chemist_adp;

    CustomSearch_Stockiest_Adp customSearch_stockiest_adp;
CustomSearch_Stockiest2_Adp customSearch_stockiest2_adp;
CustomSearch_Stockiest3_Adp customSearch_stockiest3_adp;

For_StockiestList_Bean for_stockiestList_bean;
RelativeLayout rr_selected_date;
TextView txt_selected_datee;
String company_name;
    //------------------------------------ Initialise boolean-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    boolean isGPSEnabled = false;
    //------------------------------------ Initialise double-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    double currentlat,currentlong;


public static Add_Chemist_Fragment newInstance() {
        Add_Chemist_Fragment fragment = new Add_Chemist_Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.add_chemist_new, container, false);


        // ---------------------------- For Get Current Location -------------------------------------------------------------------------------//
        getLocation();


        // ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//
        getSaveddata();

        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();


        return v;
    }

    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
    private void initUi() {

       /* String  Ho = sp.getData(MySharedPref.KEY_HOUR);
        String  m = sp.getData(MySharedPref.KEY_MINUTE);
        String  ss = sp.getData(MySharedPref.KEY_SEC);

        System.out.println("dbfvcn3645745874365 +"+Ho+ m +ss);*/

        // ---------------------------- Casting Element -------------------------------------------------------------------------------//
        //Casting TextView for Title
        txt_title=(TextView)getActivity().findViewById(R.id.txt_title);
        //Set Text Title
        txt_title.setText("Add Chemist Visit");
        //Casting EditText for Chemist Name
        edt_chemist_name=(EditText)v.findViewById(R.id.edt_chemist_name);
        //Casting EditText for Gift Quantity
        edt_product_qty=(EditText)v.findViewById(R.id.edt_product_qty);

        //Casting EditText for POB amount Name
        spn_product_pobamount=(EditText)v.findViewById(R.id.spn_product_pobamount);

        //Casting EditText for  Gift  Name Error
        txt_gift_error=(TextView)v.findViewById(R.id.txt_gift_error);
        //Casting EditText for  POB  Error
        txt_pob_error=(TextView)v.findViewById(R.id.txt_pob_error);

        //Casting EditText for  Chemist  Name Error
        txt_chname_error=(TextView)v.findViewById(R.id.txt_chname_error);
        //Casting EditText for Gift Name
        spn_product=(EditText)v.findViewById(R.id.spn_product);
        //Casting EditText for Stockiest1 Name
        spn_stockiest1=(EditText)v.findViewById(R.id.spn_stockiest1);
        //Casting EditText for Stockiest2 Name
        spn_stockiest2=(EditText)v.findViewById(R.id.spn_stockiest2);
        //Casting EditText for Stockiest3 Name
        spn_stockiest3=(EditText)v.findViewById(R.id.spn_stockiest3);


        //Casting EditText for Stockiest1 Quantity error
        edt_stockiest1_qty=(EditText)v.findViewById(R.id.edt_stockiest1_qty);
        //Casting EditText for Stockiest2 Quantity error
        edt_stockiest2_qty=(EditText)v.findViewById(R.id.edt_stockiest2_qty);
        //Casting EditText for Stockiest3 Quantity error
        edt_stockiest3_qty=(EditText)v.findViewById(R.id.edt_stockiest3_qty);


        //Casting EditText for Stockiest1 Name error
        txt_stckiest1_error=(TextView)v.findViewById(R.id.txt_stckiest1_error);


        //Casting ProgressBar for Loader
        loader=(ProgressBar)v.findViewById(R.id.loader);

        //Casting RelativeLayout for Chemist
        rr_InboxDetailRVvv=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRVvv);
        //Casting RecyclerView for Chemist
        InboxDetailRVvv=(RecyclerView)v.findViewById(R.id.InboxDetailRVvv);


        //Casting RelativeLayout for Call Complete
        rr_callcmplte_checked_unchecked=(RelativeLayout)v.findViewById(R.id.rr_callcmplte_checked_unchecked);

        // /Casting RelativeLayout for Chemist
        rr_InboxDetailRVvv_gift=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRVvv_gift);
        //Casting RecyclerView for Chemist
        InboxDetailRVvv_gift=(RecyclerView)v.findViewById(R.id.InboxDetailRVvv_gift);

        // /Casting RelativeLayout for Stockiest
        rr_InboxDetailRVvv_stockiest=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRVvv_stockiest);
        //Casting RecyclerView for Stockiest
        InboxDetailRVvv_stockiest=(RecyclerView)v.findViewById(R.id.InboxDetailRVvv_stockiest);

        // /Casting RelativeLayout for Stockiest2
        rr_InboxDetailRVvv_stockiest2=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRVvv_stockiest2);
        //Casting RecyclerView for Stockiest2
        InboxDetailRVvv_stockiest2=(RecyclerView)v.findViewById(R.id.InboxDetailRVvv_stockiest2);

        // /Casting RelativeLayout for Stockiest3
        rr_InboxDetailRVvv_stockiest3=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRVvv_stockiest3);
        //Casting RecyclerView for Stockiest3
        InboxDetailRVvv_stockiest3=(RecyclerView)v.findViewById(R.id.InboxDetailRVvv_stockiest3);


        //For  Set Chemist Create by these
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        InboxDetailRVvv.setLayoutManager(mLayoutManager2);
        InboxDetailRVvv.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRVvv.setItemAnimator(new DefaultItemAnimator());

        //For  Set Gift Create by these
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity());
        InboxDetailRVvv_gift.setLayoutManager(mLayoutManager3);
        InboxDetailRVvv_gift.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRVvv_gift.setItemAnimator(new DefaultItemAnimator());



        //For  Set Stockiest  Create by these
        RecyclerView.LayoutManager mLayoutManager4 = new LinearLayoutManager(getActivity());
        InboxDetailRVvv_stockiest.setLayoutManager(mLayoutManager4);
        InboxDetailRVvv_stockiest.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRVvv_stockiest.setItemAnimator(new DefaultItemAnimator());


        //For  Set Stockiest2  Create by these
        RecyclerView.LayoutManager mLayoutManager5 = new LinearLayoutManager(getActivity());
        InboxDetailRVvv_stockiest2.setLayoutManager(mLayoutManager5);
        InboxDetailRVvv_stockiest2.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRVvv_stockiest2.setItemAnimator(new DefaultItemAnimator());

        //For  Set Stockiest3  Create by these
        RecyclerView.LayoutManager mLayoutManager6 = new LinearLayoutManager(getActivity());
        InboxDetailRVvv_stockiest3.setLayoutManager(mLayoutManager6);
        InboxDetailRVvv_stockiest3.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRVvv_stockiest3.setItemAnimator(new DefaultItemAnimator());

        callWebservicefor_getAllInteriorAcctoCity1(user_id4);
// ---------------------------- setOnTextChange Listner-------------------------------------------------------------------------------//
        //For Search Product
        edt_chemist_name.addTextChangedListener(new TextWatcher() {
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

                    txt_chname_error.setVisibility(View.GONE);
                    // TODO Auto-generated method stub

//callWebservicefor_getAllInteriorAcctoCity(s.toString());

                   // System.out.println("Search Product Size###"+for_chemistList_bean.getResult().size());
                    if(for_chemistList_bean!=null) {

                        if (for_chemistList_bean.getResult().size() > 0) {
                           // System.out.println("Search Doctor Size Above###"+for_chemistList_bean.getResult().size());
                            edt_product_qty.setEnabled(true);
                            //Set Visiblity of RelativeLayout and RecyclerView
                            rr_InboxDetailRVvv.setVisibility(View.VISIBLE);
                            InboxDetailRVvv.setVisibility(View.VISIBLE);

                            //Set Adaptetr
                            // InboxDetailRV.setAdapter(customSearch_doctors_adp);
                            //Filter Data
                            customSearch_chemist_adp.filter(s.toString());

                        }

                       else if(for_chemistList_bean.getResult().size() == 0)
                        {
                            //System.out.println("Search Doctor Size Equal###"+dcr_products_bean.getResult().size());

                            //    callWebservicefor_getAllInteriorAcctoCity("");
                            //Filter Data


                            customSearch_chemist_adp.filter(s.toString());



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
                    rr_InboxDetailRVvv.setVisibility(View.GONE);

                    InboxDetailRVvv.setVisibility(View.GONE);
                    //edt_selct_city.setText("");
                }

                //   edt_selct_territory.setText("");


            }
        });
        //For Search Product
        spn_product.addTextChangedListener(new TextWatcher() {
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

                    txt_gift_error.setVisibility(View.GONE);
                    // TODO Auto-generated method stub

//callWebservicefor_getAllInteriorAcctoCity(s.toString());

//                    System.out.println("Search Product Size###"+dcr_products_bean.getResult().size());
                    if(dcr_products_bean!=null) {

                        if (dcr_products_bean.getResult().size() > 0) {
                            System.out.println("Search Doctor Size Above###"+dcr_products_bean.getResult().size());

                            //Set Visiblity of RelativeLayout and RecyclerView
                            rr_InboxDetailRVvv_gift.setVisibility(View.VISIBLE);
                            InboxDetailRVvv_gift.setVisibility(View.VISIBLE);


                            //Set Adaptetr
                            // InboxDetailRV.setAdapter(customSearch_doctors_adp);
                            //Filter Data
                            customGetProducts_adp.filter(s.toString());
                            edt_product_qty.setEnabled(true);


                        }

                        if(dcr_products_bean.getResult().size() == 0)
                        {
                            System.out.println("Search Doctor Size Equal###"+dcr_products_bean.getResult().size());

                            //    callWebservicefor_getAllInteriorAcctoCity("");
                            //Filter Data
                            customGetProducts_adp.filter(s.toString());
                            str_product_id = null;


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
                    rr_InboxDetailRVvv_gift.setVisibility(View.GONE);

                    InboxDetailRVvv_gift.setVisibility(View.GONE);
                    //edt_selct_city.setText("");
                }

                //   edt_selct_territory.setText("");


            }
        });


        //For Search Product
        spn_stockiest1.addTextChangedListener(new TextWatcher() {
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

                    txt_stckiest1_error.setVisibility(View.GONE);
                    // TODO Auto-generated method stub

//callWebservicefor_getAllInteriorAcctoCity(s.toString());

                    //System.out.println("Search Product Size###"+for_stockiestList_bean.getResult().size());
                    if(for_stockiestList_bean!=null) {

                        if (for_stockiestList_bean.getResult().size() > 0) {
                            System.out.println("Search Doctor Size Above###"+for_stockiestList_bean.getResult().size());

                            //Set Visiblity of RelativeLayout and RecyclerView
                            rr_InboxDetailRVvv_stockiest.setVisibility(View.VISIBLE);
                            InboxDetailRVvv_stockiest.setVisibility(View.VISIBLE);
                            edt_stockiest1_qty.setEnabled(true);

                            //Set Adaptetr
                            // InboxDetailRV.setAdapter(customSearch_doctors_adp);
                            //Filter Data
                            customSearch_stockiest_adp.filter(s.toString());

                        }

                        if(for_stockiestList_bean.getResult().size() == 0)
                        {
                            System.out.println("Search Doctor Size Equal###"+for_stockiestList_bean.getResult().size());

                            //    callWebservicefor_getAllInteriorAcctoCity("");
                            //Filter Data
                            customSearch_stockiest_adp.filter(s.toString());
                            str_stockiest_proquant = null;

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
                    rr_InboxDetailRVvv_stockiest.setVisibility(View.GONE);

                    InboxDetailRVvv_stockiest.setVisibility(View.GONE);
                    //edt_selct_city.setText("");
                }

                //   edt_selct_territory.setText("");


            }
        });

        //For Search Product
        spn_stockiest2.addTextChangedListener(new TextWatcher() {
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

                    //System.out.println("Search Product Size###"+for_stockiestList_bean.getResult().size());
                    if(for_stockiestList_bean!=null) {

                        if (for_stockiestList_bean.getResult().size() > 0) {
                            System.out.println("Search Doctor Size Above###"+for_stockiestList_bean.getResult().size());
                            edt_stockiest2_qty.setEnabled(true);

                            //Set Visiblity of RelativeLayout and RecyclerView
                            rr_InboxDetailRVvv_stockiest2.setVisibility(View.VISIBLE);
                            InboxDetailRVvv_stockiest2.setVisibility(View.VISIBLE);

                            //Set Adaptetr
                            // InboxDetailRV.setAdapter(customSearch_doctors_adp);
                            //Filter Data
                            customSearch_stockiest2_adp.filter(s.toString());

                        }

                        if(for_stockiestList_bean.getResult().size() == 0)
                        {
                            System.out.println("Search Doctor Size Equal###"+for_stockiestList_bean.getResult().size());

                            //    callWebservicefor_getAllInteriorAcctoCity("");
                            //Filter Data
                            customSearch_stockiest2_adp.filter(s.toString());
                            str_stockiest2_proquant = null;


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
                    rr_InboxDetailRVvv_stockiest2.setVisibility(View.GONE);

                    InboxDetailRVvv_stockiest2.setVisibility(View.GONE);
                    //edt_selct_city.setText("");
                }

                //   edt_selct_territory.setText("");


            }
        });
        //For Search Product
        spn_stockiest3.addTextChangedListener(new TextWatcher() {
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

                  //  System.out.println("Search Product Size###"+for_stockiestList_bean.getResult().size());
                    if(for_stockiestList_bean!=null) {

                        if (for_stockiestList_bean.getResult().size() > 0) {
                            System.out.println("Search Doctor Size Above###"+for_stockiestList_bean.getResult().size());
                            edt_stockiest3_qty.setEnabled(true);

                            //Set Visiblity of RelativeLayout and RecyclerView
                            rr_InboxDetailRVvv_stockiest3.setVisibility(View.VISIBLE);
                            InboxDetailRVvv_stockiest3.setVisibility(View.VISIBLE);

                            //Set Adaptetr
                            // InboxDetailRV.setAdapter(customSearch_doctors_adp);
                            //Filter Data
                            customSearch_stockiest3_adp.filter(s.toString());

                        }

                        if(for_stockiestList_bean.getResult().size() == 0)
                        {
                            System.out.println("Search Doctor Size Equal###"+for_stockiestList_bean.getResult().size());

                            //    callWebservicefor_getAllInteriorAcctoCity("");
                            //Filter Data
                            customSearch_stockiest3_adp.filter(s.toString());
                            str_stockiest3_proquant=null;


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
                    rr_InboxDetailRVvv_stockiest3.setVisibility(View.GONE);

                    InboxDetailRVvv_stockiest3.setVisibility(View.GONE);
                    //edt_selct_city.setText("");
                }

                //   edt_selct_territory.setText("");


            }
        });
        rr_callcmplte_checked_unchecked.setOnClickListener(this);


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
    @Override
    public void onClick(View v) {
        if(v==rr_callcmplte_checked_unchecked)
        {

            Validate();
           // get_Data_forGetCallComplete();
        }
    }
    // ---------------------------- For Validation-------------------------------------------------------------------------------//
    private void Validate() {
        String str_chemist_name=edt_chemist_name.getText().toString();
        String str_giftt_name=spn_product.getText().toString();
        String str_pro_qty=edt_product_qty.getText().toString();
        String str_product_pobamount=spn_product_pobamount.getText().toString();
        String str_spn_stockiest1=spn_stockiest1.getText().toString();
        String str_stockiest1_qty=edt_stockiest1_qty.getText().toString();

        String str_spn_stockiest2=spn_stockiest2.getText().toString();
        String str_stockiest2_qty=edt_stockiest2_qty.getText().toString();
        String str_spn_stockiest3=spn_stockiest3.getText().toString();
        String str_stockiest3_qty=edt_stockiest3_qty.getText().toString();
        int in_pro_qty=0,in_stockiest1_qty=0;

        System.out.println("str_chemist_name###"+str_chemist_name);
        System.out.println("str_giftt_name###"+str_giftt_name);
        System.out.println("str_pro_qty###"+str_pro_qty);

        System.out.println("str_product_pobamount###"+str_product_pobamount);
        System.out.println("str_spn_stockiest1###"+str_spn_stockiest1);
        System.out.println("str_stockiest1_qty###"+str_stockiest1_qty);

        System.out.println("str_spn_stockiest2###"+str_spn_stockiest2);
        System.out.println("str_stockiest2_qty###"+str_stockiest2_qty);
        System.out.println("str_spn_stockiest3###"+str_spn_stockiest3+str_stockiest3_qty);
        try {
            in_pro_qty= Integer.parseInt(edt_product_qty.getText().toString());
            in_stockiest1_qty= Integer.parseInt(edt_stockiest1_qty.getText().toString());

        }
        catch (NumberFormatException e)
        {

        }
        System.out.println("Pro Quant###"+str_proquant);
        boolean iserror=false;

        if(str_chemist_name.equalsIgnoreCase(""))
        {
            iserror=true;
            txt_chname_error.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_chname_error.setVisibility(View.GONE);

        }
     /*   if(str_giftt_name.equalsIgnoreCase(""))
        {
            iserror=true;
            txt_gift_error.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_gift_error.setVisibility(View.GONE);

        }*/

             System.out.println("Pro Id###"+str_product_id);

             if(!str_giftt_name.equalsIgnoreCase(""))
         {

            if(str_product_id==null)
             {
               iserror=true;
                 txt_gift_error.setVisibility(View.VISIBLE);
                 txt_gift_error.setText("Please Select Only allocated gifts.");
             }
           else if(str_product_id.equalsIgnoreCase("null"))
                         {
                               iserror=true;
            txt_gift_error.setVisibility(View.VISIBLE);
            txt_gift_error.setText("Please Select Only allocated gifts.");

             }
         else if (dcr_products_bean.getResult().size()==0)
            {
                txt_gift_error.setVisibility(View.VISIBLE);
                txt_gift_error.setText("Please Select Only allocated gifts.");
            }
     if(str_product_id!=null)
     {
         if(str_pro_qty.equalsIgnoreCase(""))
         {
             iserror=true;
             edt_product_qty.setError("Please enter Gift QTY.");
         }

             if(str_pro_qty.equalsIgnoreCase("0"))
         {
             iserror=true;
             edt_product_qty.setError("You can't enter 0 quantity");
         }
             if(str_proquant<in_pro_qty)
         {
             iserror=true;
             edt_product_qty.setError("You can't enter more quantity");
         }
        /* if (str_proquant==0)
         {
             Toast.makeText(getActivity(), "Quantity is "+str_proquant, Toast.LENGTH_SHORT).show();
         }*/
         }
         }



         /*if(str_product_pobamount.equalsIgnoreCase(""))
         {
             iserror=true;
             txt_pob_error.setVisibility(View.VISIBLE);
         }

         else if(str_product_pobamount.equalsIgnoreCase("0"))
         {
             iserror=true;
             txt_pob_error.setVisibility(View.VISIBLE);

         }

         else
         {
             txt_pob_error.setVisibility(View.GONE);

         }*/

         if(!str_product_pobamount.equalsIgnoreCase("")) {
                         System.out.println("Stockiest one Quant"+in_stockiest1_qty);
                           System.out.println("StockiestIDDDD"+str_stockiest_proquant);
                            System.out.println("StockiestIDDDD--22"+str_stockiest2_proquant);
                             System.out.println("StockiestIDDDD--33"+str_stockiest3_proquant);

             if (str_spn_stockiest1.equalsIgnoreCase("")) {
                 iserror = true;
                 txt_stckiest1_error.setVisibility(View.VISIBLE);
             }
             else  if (str_stockiest_proquant==null) {
                 iserror = true;
                 spn_stockiest1.setError("Please select only allocate stockiest.");
             }
             else {
                 txt_stckiest1_error.setVisibility(View.GONE);
             }
             if (str_stockiest1_qty.equalsIgnoreCase("")) {
                 iserror = true;
                 edt_stockiest1_qty.setError("Please enter the amount.");
             }
             else if (str_stockiest1_qty.equalsIgnoreCase("0")) {
                 iserror = true;
                 edt_stockiest1_qty.setError("You can't enter 0 quantity");
             }

            /* if (str_stockiest1_qty.equalsIgnoreCase("")) {
                 iserror = true;
                 edt_stockiest1_qty.setError("This field is Required");
             }
             else if (str_stockiest1_qty.equalsIgnoreCase("0")) {
                 iserror = true;
                 edt_stockiest1_qty.setError("You can't enter 0 quantity");
             }

             if(str_spn_stockiest2.equalsIgnoreCase("")) {
                 iserror = true;
                 spn_stockiest2.setError("This field is required.");
             }
             else
             {
                 if(str_stockiest2_proquant==null)
                     {
                              iserror = true;
                             spn_stockiest2.setError("allocate stockiest");
                     }

                 if (str_stockiest2_qty.equalsIgnoreCase("")) {
                     iserror = true;
                     edt_stockiest2_qty.setError("This field is Required");
                 }

                 else if (str_stockiest2_qty.equalsIgnoreCase("0"))
                 {
                     iserror = true;
                     edt_stockiest1_qty.setError("You can't enter 0 quantity");
                 }
             }
            *//* else
             {
                 spn_stockiest2.setError("This field is required");
             }*//*

             if(str_spn_stockiest3.equalsIgnoreCase(""))
             {
                 iserror = true;
                 spn_stockiest3.setError("This field is required.");
             }
             else {
                          if(str_stockiest3_proquant==null)
                     {
                              iserror = true;
                              spn_stockiest3.setError("Please select only allocate stockiest.");
                        }

                 if (str_stockiest3_qty.equalsIgnoreCase("")) {
                     iserror = true;
                     edt_stockiest3_qty.setError("This field is Required");
                 } else if (str_stockiest3_qty.equalsIgnoreCase("0")) {
                     iserror = true;
                     edt_stockiest3_qty.setError("You can't enter 0 quantity");
                 }
             }*/

         }
         else
         {
             if (!str_spn_stockiest1.equalsIgnoreCase("")) {
                 iserror = true;
                 spn_product_pobamount.setError("Enter the amoount.");
             }
             else if (!str_spn_stockiest2.equalsIgnoreCase(""))
             {
                 iserror = true;
                 spn_product_pobamount.setError("Enter the amoount.");
             }
             else if (!str_spn_stockiest3.equalsIgnoreCase(""))
             {
                 iserror = true;
                 spn_product_pobamount.setError("Enter the amoount.");
             }

         }

        if(!str_spn_stockiest2.equalsIgnoreCase("")) {


             if(str_stockiest2_proquant==null)
            {
                iserror = true;
                spn_stockiest2.setError("Please select only allocate stockiest.");
            }

            if (str_stockiest2_qty.equalsIgnoreCase("")) {
                iserror = true;
                edt_stockiest2_qty.setError("Enter the amoount.");
            }

            else if (str_stockiest2_qty.equalsIgnoreCase("0"))
            {
                iserror = true;
                edt_stockiest1_qty.setError("You can't enter 0 quantity");
            }
        }

        if(!str_spn_stockiest3.equalsIgnoreCase(""))
        {
            if(str_stockiest3_proquant==null)
            {
                iserror = true;
                spn_stockiest3.setError("Please select only allocate stockiest.");
            }

            if (str_stockiest3_qty.equalsIgnoreCase("")) {
                iserror = true;
                edt_stockiest3_qty.setError("Enter the amoount.");
            } else if (str_stockiest3_qty.equalsIgnoreCase("0")) {
                iserror = true;
                edt_stockiest3_qty.setError("You can't enter 0 quantity");
            }
        }


       /* else if(str_stockiest1quant<in_stockiest1_qty)
        {
            iserror=true;
            edt_stockiest1_qty.setError("You can't enter more quantity");
        }*/



         if(!iserror)
         {            txt_chname_error.setVisibility(View.GONE);
             txt_gift_error.setVisibility(View.GONE);
             txt_pob_error.setVisibility(View.GONE);
             txt_stckiest1_error.setVisibility(View.GONE);

             callWebservicefor_get();
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

        String url = "http://dailyreporting.in/"+company_name+"/api/chemist_submit";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_DCR_CHEMIST_SUBMIT, new Response.Listener<String>() {*/
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

                        String result=jObj.getString("result");
                        System.out.println("Result DCR Call Complete***"+result);
                        String message=jObj.getString("message");

                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), "Chemist Visit added successfully.", Toast.LENGTH_SHORT).show();
                        }


                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFrame, new TimeLineFragment())
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

                Log.e("DCRCallComplete", "DCR Call Complete: " + error.getMessage());
                try {
                   /* JSONObject jsonObject22=new JSONObject(error.getMessage());

                    String errorMsg=jsonObject22.getString("message");
                    //  JSONObject jsonObject33=new JSONObject(message);*/
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

                                        System.out.println("Table Insert Id Proquant***"+dcr_table_insert_id);
                                        System.out.println("Table Chemist Stockiest LiveInsert Id Proquant***"+dcr_chemist_stockist_live_table_id);

               System.out.println("Table Latitude Proquant***"+String.valueOf(currentlat));
                  System.out.println("Table Longitude Proquant***"+String.valueOf(currentlong));

                  params.put("dcr_table_insert_id", dcr_table_insert_id);
                params.put("dcr_chemist_stockist_live_table_id",dcr_chemist_stockist_live_table_id);
                params.put("user_id",user_id4);
                                        System.out.println("Stockiest Id Proquant***"+str_stockiest_proquant);
                                        System.out.println("Chemist Id Proquant***"+str_chemist_proquant);
 if(str_chemist_proquant==null)

    {
                              params.put("chemist_id",edt_chemist_name.getText().toString());

}
else if(str_chemist_proquant.equalsIgnoreCase("null"))
    {
                          params.put("chemist_id",edt_chemist_name.getText().toString());

}
/*

                       if(!str_chemist_proquant.equalsIgnoreCase("null")) {
                    params.put("chemist_id", str_chemist_proquant);
                }


                if(!str_chemist_proquant.equalsIgnoreCase("")) {
                    params.put("chemist_id", str_chemist_proquant);
                }
*/


                else
                {
                    params.put("chemist_id",str_chemist_proquant);

                }

                if(spn_product_pobamount.getText().toString()==null)
                    {
                                        params.put("pob_amount_chemist","");

}
else
    {
                                                params.put("pob_amount_chemist",spn_product_pobamount.getText().toString());

}

 if(str_product_id==null)

    {

 params.put("chemist_gift","");
                              params.put("chemist_gift_value","");



}
else
    if(str_product_id.equalsIgnoreCase("null"))
    {


 params.put("chemist_gift","");
                              params.put("chemist_gift_value","");

    }
/*if(!str_product_id.equalsIgnoreCase(""))
{
    params.put("chemist_gift",str_product_id);

}*/



else
{
           params.put("chemist_gift",str_product_id);
                params.put("chemist_gift_value",edt_product_qty.getText().toString());




}


                 if(str_stockiest_proquant==null)

    {

         params.put("stockist1","");
                params.put("stockist1_value","");



}
           else if(str_stockiest_proquant.equalsIgnoreCase("null"))
                {


                 params.put("stockist1","");
                params.put("stockist1_value","");
                }


                else
                    {
                           params.put("stockist1",str_stockiest_proquant);
                params.put("stockist1_value",edt_stockiest1_qty.getText().toString());
}
                     if(str_stockiest2_proquant==null){
            params.put("stockist2","");
                              params.put("stockist2_value","");

}

else if(str_stockiest2_proquant.equalsIgnoreCase("null"))
    {
               params.put("stockist2","");
                              params.put("stockist2_value","");
                }




                else
                    {
  params.put("stockist2",str_stockiest2_proquant);
                params.put("stockist2_value",edt_stockiest2_qty.getText().toString());


                              }


           if(str_stockiest3_proquant==null)

    {
                                params.put("stockist3","");
                params.put("stockist3_value","");


}
else if(str_stockiest3_proquant.equalsIgnoreCase("null"))
    {

         params.put("stockist3","");
                params.put("stockist3_value","");
                }

                else
                    {

                                    params.put("stockist3",str_stockiest3_proquant);
                params.put("stockist3_value",edt_stockiest3_qty.getText().toString());

}
                params.put("checkout_lat", String.valueOf(currentlat));
                params.put("checkout_long", String.valueOf(currentlong));

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

        String url = "http://dailyreporting.in/"+company_name+"/api/chemists_record";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_CHEMIST_RECORD,
                new com.android.volley.Response.Listener<String>() {*/
            //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

                    StringRequest strReq = new StringRequest(Request.Method.POST, url,
                            new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Chemist Record", "Chemist Record response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {

                        loader.setVisibility(View.GONE);

                        Gson gson = new Gson();

                        for_chemistList_bean=gson.fromJson(response,For_ChemistList_Bean.class);
                        System.out.println("List Size interior:"+for_chemistList_bean.getResult().size());


                        if(for_chemistList_bean.getResult().size()>0)
                        {
                            // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//
                            customSearch_chemist_adp = new CustomSearch_Chemist_Adp(getActivity(),for_chemistList_bean.getResult());
                            InboxDetailRVvv.setAdapter(customSearch_chemist_adp);

                        }
                        else
                        {
                            rr_InboxDetailRVvv.setVisibility(View.GONE);
                            InboxDetailRVvv.setVisibility(View.GONE);
                        }

                        getData_ForSearchSuggestion_Gift(user_id4);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id4);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }


    // ---------------------------- For WebService Call for get Search Doctor Suggestion -------------------------------------------------------------------------------//
    private void getData_ForSearchSuggestion_Gift(final String cityid4) {

        String url = "http://dailyreporting.in/"+company_name+"/api/user_allocate_gift";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        /*StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_USER_ALLOCATE_GIFTS,
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

                        dcr_products_bean=gson.fromJson(response,Gift_List_Bean.class);
                        System.out.println("List Size interior:"+dcr_products_bean.getResult().size());


                        if(dcr_products_bean.getResult().size()>0)
                        {
                            // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//
                            customGetProducts_adp = new CustomSearch_Doctors_Gift_Adp(getActivity(),dcr_products_bean.getResult());

                            InboxDetailRVvv_gift.setAdapter(customGetProducts_adp);

                        }
                        else
                        {
                            rr_InboxDetailRVvv_gift.setVisibility(View.GONE);
                            InboxDetailRVvv_gift.setVisibility(View.GONE);
                        }

                        getData_ForSearchSuggestion_Stockiest(user_id4);
                        String errorMsg = jObj.getString("message");
                        if(getActivity()!=null) {
                         /*   txt_avail_pro.setVisibility(View.VISIBLE);
                            txt_avail_pro.setText("No products added yet.");*/
                            //  Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                        }




                    } else {
                        loader.setVisibility(View.GONE);
                        getData_ForSearchSuggestion_Stockiest(user_id4);

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id4);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }
    // ---------------------------- For WebService Call for get Search Doctor Suggestion -------------------------------------------------------------------------------//
    private void getData_ForSearchSuggestion_Stockiest(final String cityid4) {

       String url = "http://dailyreporting.in/"+company_name+"/api/stockist_record";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_USER_STOCKIEST_RECORD,
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

                        for_stockiestList_bean=gson.fromJson(response,For_StockiestList_Bean.class);
                        System.out.println("List Size interior:"+for_stockiestList_bean.getResult().size());


                        if(for_stockiestList_bean.getResult().size()>0)
                        {
                            // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//
                            customSearch_stockiest_adp = new CustomSearch_Stockiest_Adp(getActivity(),for_stockiestList_bean.getResult());
                            InboxDetailRVvv_stockiest.setAdapter(customSearch_stockiest_adp);


                            // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//
                            customSearch_stockiest2_adp = new CustomSearch_Stockiest2_Adp(getActivity(),for_stockiestList_bean.getResult());
                            InboxDetailRVvv_stockiest2.setAdapter(customSearch_stockiest2_adp);


                            // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//
                            customSearch_stockiest3_adp = new CustomSearch_Stockiest3_Adp(getActivity(),for_stockiestList_bean.getResult());
                            InboxDetailRVvv_stockiest3.setAdapter(customSearch_stockiest3_adp);



                        }
                        else
                        {
                            rr_InboxDetailRVvv_stockiest.setVisibility(View.GONE);
                            InboxDetailRVvv_stockiest.setVisibility(View.GONE);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id4);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
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
                dcr_table_insert_id=sp.getData(getActivity(),"dcr_table_insert_id","null");
                dcr_chemist_stockist_live_table_id =sp.getData(getActivity(),"dcr_chemist_stockist_live_table_id","null");

                Log.e("Id is@@@",user_id4);
                System.out.println("Id is***" + user_id4);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
    // ---------------------------- Class for RecycleView for Search Gift Adapter-------------------------------------------------------------------------------//
    private class CustomSearch_Doctors_Gift_Adp extends RecyclerView.Adapter<CustomSearch_Doctors_Gift_Adp.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<Gift_List_Lxdetails_Bean> arr_all_search_doctors3;
        List<Gift_List_Lxdetails_Bean> arrayList = new ArrayList<>();
        List<Gift_List_Lxdetails_Bean> arSearchlist;

        public CustomSearch_Doctors_Gift_Adp(Activity activity, List<Gift_List_Lxdetails_Bean> arr_all_search_doctors2) {

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
        public CustomSearch_Doctors_Gift_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_doctor_adp, parent, false);

            return new CustomSearch_Doctors_Gift_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomSearch_Doctors_Gift_Adp.MyViewHolder holder, final int position) {



            holder.img_next.setVisibility(View.GONE);
            holder.txt_profession.setVisibility(View.GONE);
            String str_interior_name= arr_all_search_doctors3.get(position).getGname();

            String upperString_str_interior_name = str_interior_name.substring(0,1).toUpperCase() + str_interior_name.substring(1);
            holder.txt_current_loc_descp.setText(upperString_str_interior_name);



            holder.rr_current_loc_descp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        spn_product.setText(arr_all_search_doctors3.get(position).getGname());

                        int it_totlquant= Integer.parseInt(arr_all_search_doctors3.get(position).getTotalQty());
                        if(it_totlquant>0) {
                            edt_product_qty.setEnabled(true);
                        }
                        else
                        {
                            edt_product_qty.setEnabled(false);
                            Toast.makeText(activity, "All are distributed.",Toast.LENGTH_LONG ).show();
                        }


                        str_product_id=arr_all_search_doctors3.get(position).getGiftTableId();
                        rr_InboxDetailRVvv_gift.setVisibility(View.GONE);
                        InboxDetailRVvv_gift.setVisibility(View.GONE);
                        str_proquant= Integer.parseInt(arr_all_search_doctors3.get(position).getTotalQty());
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

        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###"+charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (Gift_List_Lxdetails_Bean wp : arSearchlist) {
                    if (wp.getGname().toLowerCase().startsWith(charText))
                    {
                        arrayList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
    // ---------------------------- Class for RecycleView for Search Doctor Adapter-------------------------------------------------------------------------------//
    private class CustomSearch_Chemist_Adp extends RecyclerView.Adapter<CustomSearch_Chemist_Adp.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<For_ChemistList_Lxdetails_Bean> arr_all_search_doctors3;
        List<For_ChemistList_Lxdetails_Bean> arrayList = new ArrayList<>();
        List<For_ChemistList_Lxdetails_Bean> arSearchlist;

        public CustomSearch_Chemist_Adp(Activity activity, List<For_ChemistList_Lxdetails_Bean> arr_all_search_doctors2) {

            this.activity = activity;

            this.arr_all_search_doctors3 = arr_all_search_doctors2;

            this.arrayList = arr_all_search_doctors2;
            this.arSearchlist = new ArrayList<For_ChemistList_Lxdetails_Bean>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size###"+arr_all_search_doctors3.size());
        }


        @Override
        public CustomSearch_Chemist_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_doctor_adp, parent, false);

            return new CustomSearch_Chemist_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomSearch_Chemist_Adp.MyViewHolder holder, final int position) {



            holder.img_next.setVisibility(View.GONE);
            holder.txt_profession.setVisibility(View.GONE);
            String str_interior_name= arr_all_search_doctors3.get(position).getChemistName();

            String upperString_str_interior_name = str_interior_name.substring(0,1).toUpperCase() + str_interior_name.substring(1);
            holder.txt_current_loc_descp.setText(upperString_str_interior_name);



            holder.rr_current_loc_descp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        edt_chemist_name.setText(arr_all_search_doctors3.get(position).getChemistName());


                        str_chemist_proquant=arr_all_search_doctors3.get(position).getChemistId();
                        rr_InboxDetailRVvv.setVisibility(View.GONE);
                        InboxDetailRVvv.setVisibility(View.GONE);

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

        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###"+charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (For_ChemistList_Lxdetails_Bean wp : arSearchlist) {
                    if (wp.getChemistName().toLowerCase().startsWith(charText))
                    {
                        arrayList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }


    // ---------------------------- Class for RecycleView for Search Doctor Adapter-------------------------------------------------------------------------------//
    private class CustomSearch_Stockiest_Adp extends RecyclerView.Adapter<CustomSearch_Stockiest_Adp.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<For_StockiestList_Lxdetails_Bean> arr_all_search_doctors3;
        List<For_StockiestList_Lxdetails_Bean> arrayList = new ArrayList<>();
        List<For_StockiestList_Lxdetails_Bean> arSearchlist;

        public CustomSearch_Stockiest_Adp(Activity activity, List<For_StockiestList_Lxdetails_Bean> arr_all_search_doctors2) {

            this.activity = activity;

            this.arr_all_search_doctors3 = arr_all_search_doctors2;

            this.arrayList = arr_all_search_doctors2;
            this.arSearchlist = new ArrayList<For_StockiestList_Lxdetails_Bean>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size###"+arr_all_search_doctors3.size());
        }


        @Override
        public CustomSearch_Stockiest_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_doctor_adp, parent, false);

            return new CustomSearch_Stockiest_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomSearch_Stockiest_Adp.MyViewHolder holder, final int position) {



            holder.img_next.setVisibility(View.GONE);
            holder.txt_profession.setVisibility(View.GONE);
            String str_interior_name= arr_all_search_doctors3.get(position).getStockistName();

            String upperString_str_interior_name = str_interior_name.substring(0,1).toUpperCase() + str_interior_name.substring(1);
            holder.txt_current_loc_descp.setText(upperString_str_interior_name);



            holder.rr_current_loc_descp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        spn_stockiest1.setText(arr_all_search_doctors3.get(position).getStockistName());

                        str_stockiest_proquant=arr_all_search_doctors3.get(position).getStockistId();
                        rr_InboxDetailRVvv_stockiest.setVisibility(View.GONE);
                        InboxDetailRVvv_stockiest.setVisibility(View.GONE);

                        edt_stockiest1_qty.setEnabled(true);
              //  str_stockiest1quant=arr_all_search_doctors3.get(position).ge

                        // str_proquant= Integer.parseInt(arr_all_search_doctors3.get(position).getTotalQty());
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

        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###"+charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (For_StockiestList_Lxdetails_Bean wp : arSearchlist) {
                    if(!wp.getStockistName().equalsIgnoreCase(spn_stockiest2.getText().toString()) && !wp.getStockistName().equalsIgnoreCase(spn_stockiest3.getText().toString())) {

                        if (wp.getStockistName().toLowerCase().startsWith(charText)) {
                            arrayList.add(wp);
                        }
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    // ---------------------------- Class for RecycleView for Search Stockiest2 Adapter-------------------------------------------------------------------------------//
    private class CustomSearch_Stockiest2_Adp extends RecyclerView.Adapter<CustomSearch_Stockiest2_Adp.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<For_StockiestList_Lxdetails_Bean> arr_all_search_doctors3;
        List<For_StockiestList_Lxdetails_Bean> arrayList = new ArrayList<>();
        List<For_StockiestList_Lxdetails_Bean> arSearchlist;

        public CustomSearch_Stockiest2_Adp(Activity activity, List<For_StockiestList_Lxdetails_Bean> arr_all_search_doctors2) {

            this.activity = activity;

            this.arr_all_search_doctors3 = arr_all_search_doctors2;

            this.arrayList = arr_all_search_doctors2;
            this.arSearchlist = new ArrayList<For_StockiestList_Lxdetails_Bean>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size###"+arr_all_search_doctors3.size());
        }


        @Override
        public CustomSearch_Stockiest2_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_doctor_adp, parent, false);

            return new CustomSearch_Stockiest2_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomSearch_Stockiest2_Adp.MyViewHolder holder, final int position) {



            holder.img_next.setVisibility(View.GONE);
            holder.txt_profession.setVisibility(View.GONE);
            String str_interior_name= arr_all_search_doctors3.get(position).getStockistName();

            String upperString_str_interior_name = str_interior_name.substring(0,1).toUpperCase() + str_interior_name.substring(1);
            holder.txt_current_loc_descp.setText(upperString_str_interior_name);



            holder.rr_current_loc_descp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        spn_stockiest2.setText(arr_all_search_doctors3.get(position).getStockistName());


                        str_stockiest2_proquant=arr_all_search_doctors3.get(position).getStockistId();
                        rr_InboxDetailRVvv_stockiest2.setVisibility(View.GONE);
                        InboxDetailRVvv_stockiest2.setVisibility(View.GONE);
                        edt_stockiest2_qty.setEnabled(true);

                        //  str_stockiest1quant=arr_all_search_doctors3.get(position).ge

                        // str_proquant= Integer.parseInt(arr_all_search_doctors3.get(position).getTotalQty());
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

        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###"+charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (For_StockiestList_Lxdetails_Bean wp : arSearchlist) {

                    if(!wp.getStockistName().equalsIgnoreCase(spn_stockiest1.getText().toString()) && !wp.getStockistName().equalsIgnoreCase(spn_stockiest3.getText().toString())) {
                        if (wp.getStockistName().toLowerCase().startsWith(charText)) {
                            arrayList.add(wp);
                        }
                    }
                }
            }
            notifyDataSetChanged();
        }
    }



    // ---------------------------- Class for RecycleView for Search Stockiest3 Adapter-------------------------------------------------------------------------------//
    private class CustomSearch_Stockiest3_Adp extends RecyclerView.Adapter<CustomSearch_Stockiest3_Adp.MyViewHolder> {

        Activity activity;

        private Context mContext;
        List<For_StockiestList_Lxdetails_Bean> arr_all_search_doctors3;
        List<For_StockiestList_Lxdetails_Bean> arrayList = new ArrayList<>();
        List<For_StockiestList_Lxdetails_Bean> arSearchlist;

        public CustomSearch_Stockiest3_Adp(Activity activity, List<For_StockiestList_Lxdetails_Bean> arr_all_search_doctors2) {

            this.activity = activity;

            this.arr_all_search_doctors3 = arr_all_search_doctors2;

            this.arrayList = arr_all_search_doctors2;
            this.arSearchlist = new ArrayList<For_StockiestList_Lxdetails_Bean>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }

            System.out.println("List Size###"+arr_all_search_doctors3.size());
        }


        @Override
        public CustomSearch_Stockiest3_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_doctor_adp, parent, false);

            return new CustomSearch_Stockiest3_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomSearch_Stockiest3_Adp.MyViewHolder holder, final int position) {



            holder.img_next.setVisibility(View.GONE);
            holder.txt_profession.setVisibility(View.GONE);
            String str_interior_name= arr_all_search_doctors3.get(position).getStockistName();

            String upperString_str_interior_name = str_interior_name.substring(0,1).toUpperCase() + str_interior_name.substring(1);
            holder.txt_current_loc_descp.setText(upperString_str_interior_name);



            holder.rr_current_loc_descp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        spn_stockiest3.setText(arr_all_search_doctors3.get(position).getStockistName());
                        edt_stockiest3_qty.setEnabled(true);


                        str_stockiest3_proquant=arr_all_search_doctors3.get(position).getStockistId();
                        rr_InboxDetailRVvv_stockiest3.setVisibility(View.GONE);
                        InboxDetailRVvv_stockiest3.setVisibility(View.GONE);
                        //  str_stockiest1quant=arr_all_search_doctors3.get(position).ge

                        // str_proquant= Integer.parseInt(arr_all_search_doctors3.get(position).getTotalQty());
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

        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###"+charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (For_StockiestList_Lxdetails_Bean wp : arSearchlist) {
                    if(!wp.getStockistName().equalsIgnoreCase(spn_stockiest1.getText().toString()) && !wp.getStockistName().equalsIgnoreCase(spn_stockiest2.getText().toString())) {

                        if (wp.getStockistName().toLowerCase().startsWith(charText)) {
                            arrayList.add(wp);
                        }
                    }
                }
            }
            notifyDataSetChanged();
        }
    }


}
