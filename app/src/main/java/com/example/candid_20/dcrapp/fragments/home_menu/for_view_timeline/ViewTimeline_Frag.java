package com.example.candid_20.dcrapp.fragments.home_menu.for_view_timeline;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.candid_20.dcrapp.R;

public class ViewTimeline_Frag extends Fragment implements View.OnClickListener {
    View v;
    RelativeLayout rr_search_descp;
TextView txt_title;
EditText txt_search_doctor;
CalendarView calender;
String msg;
    public static ViewTimeline_Frag newInstance()
    {
        ViewTimeline_Frag fragment = new ViewTimeline_Frag();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.view_timeline,container,false);

        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();
        return v;
    }

    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
    private void initUi() {

        // ---------------------------- Casting Element -------------------------------------------------------------------------------//
        //Casting TextView for Title
        txt_title=(TextView)getActivity().findViewById(R.id.txt_title);
        //Set Text Title
        txt_title.setText("Timeline");
//Casting EditText for
        txt_search_doctor=(EditText) v.findViewById(R.id.txt_search_doctor);

        //Casting RelativeLayout for CalenderShow
        rr_search_descp=(RelativeLayout)v.findViewById(R.id.rr_search_descp);

        //Casting CalendarView for CalenderShow
        calender=(CalendarView)v.findViewById(R.id.calender);
       // calender.setMaxDate(System.currentTimeMillis());
        // ---------------------------- setOnTextChange Listner-------------------------------------------------------------------------------//
        //For CalenderShow
        rr_search_descp.setOnClickListener(this);
        //For CalenderShow EditText
        txt_search_doctor.setOnClickListener(this);
        // ---------------------------- setOnCalenderClick Listner-------------------------------------------------------------------------------//

        //For Calender Click
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int month1 = month + 1;

                //Note that months are indexed from 0. So, 0 means january, 1 means February, 2 means march etc.
                 msg  = year + "-" + month1 + "-" + dayOfMonth;
                 System.out.println("Message is###"+msg);


                TimeLine_FullView_Frag selectedFragment = TimeLine_FullView_Frag.newInstance();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                Bundle b = new Bundle();
                b.putString("today_date",msg);
                selectedFragment.setArguments(b);
                transaction.replace(R.id.contentFrame, selectedFragment);
                transaction.addToBackStack("29");
                transaction.commit();

                }
        });

        }

    @Override
    public void onClick(View v) {

        if(v==rr_search_descp)
        {
            calender.setVisibility(View.VISIBLE);
        }

        if(v==txt_search_doctor)
        {
            calender.setVisibility(View.VISIBLE);
        }
        if(v==calender)
        {

        }
    }
}
