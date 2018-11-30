package com.example.candid_20.dcrapp.fragments.home_menu.for_map_my_work;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.fragments.home_menu.for_view_timeline.TimeLine_FullView_Frag;
import com.example.candid_20.dcrapp.storage.MySharedPref;

import org.json.JSONException;
import org.json.JSONObject;

public class Map_My_Work extends Fragment {

    View v;
    TextView txt_title;
    String ldata,dcr_table_insert_id,user_id4,token,dcr_chemist_stockist_live_table_id,company_name;
    MySharedPref sp;

    public static Map_My_Work newInstance()
    {
        Map_My_Work fragment = new Map_My_Work();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.full_viewtimeline, container, false);

        // ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//
        getSaveddata();

        // ---------------------------- For Check Permission -------------------------------------------------------------------------------//
       // checkForPermission();

        // ---------------------------- For  Bundle Data -------------------------------------------------------------------------------//
       // getBundleData();

        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
       initUi();

        return v;
    }

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

    private void initUi() {

        //Casting TextView for Title
        txt_title=(TextView)getActivity().findViewById(R.id.txt_title);
        //Set Text
        txt_title.setText("Map My Work");
    }


}
