package com.example.candid_20.dcrapp.constant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.candid_20.dcrapp.storage.MySharedPref;
import com.example.candid_20.dcrapp.volleyconnector.AppSingleton;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ReceiverCall extends BroadcastReceiver {

    MySharedPref sp;
    Context mcontext;
    String company_name, user_id4;

    @Override
    public void onReceive(Context context, Intent intent) {
        String value = intent.getStringExtra(LocationMonitorService.ACTION_LOCATION_BROADCAST);
        Log.i("Service Stops", "Ohhhhhhh" + value);

        if (value.equalsIgnoreCase("0")) {
            mcontext = context;
            sp = new MySharedPref();

            company_name = sp.getData(context, "company_name", "");
            String ldata = sp.getData(context, "ldata", "null");
            Log.e("LdataHome", ldata);

            if (!ldata.equalsIgnoreCase("null")) {
                try {
                    JSONObject jsonObject = new JSONObject(ldata);
                    user_id4 = jsonObject.getString("user_id");
                    Log.i("Service Stops", "Ohhhhhhh" + user_id4);

                    String latitude = intent.getStringExtra(LocationMonitorService.EXTRA_LATITUDE);
                    String longitude = intent.getStringExtra(LocationMonitorService.EXTRA_LONGITUDE);

                    if (latitude != null && longitude != null) {
                        System.out.println("updated lat long ## broadcast" + latitude + longitude);
                        Update_Data_for_LatLong(latitude, longitude);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        else {
            context.startService(new Intent(context, LocationMonitorService.class));
        }


       /* if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"));
        {
            // Retrieve a PendingIntent that will perform a broadcast
           AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

            // Set the alarm to start at 11:59:59 PM
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR, 11);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.AM_PM, Calendar.PM);

            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            alarmIntent.putExtra("MAKE_SERVER_CALL","1");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            manager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pendingIntent);
        }*/



    }

    // ---------------------------- For  WebService Call Method of Login-------------------------------------------------------------------------------//
    private void Update_Data_for_LatLong(final String lat, final String longi) {
        // Tag used to cancel the request
        String url = "http://dailyreporting.in/"+company_name+"/api/update_current_location";
        System.out.println("sout url"+ url);

        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_CHECK_USER_TIME_DURATION, new Response.Listener<String>() {*/
        //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CheckUserFieldWork", "CheckUserFieldWork: 35345445 " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    if (error.equals("success")) {
                        String result=jObj.getString("result");
                        System.out.println("Result CheckUserFieldWork***"+result);
                    }
                    else
                    {
                        Log.e("CheckUserFieldWork", "CheckUserFieldWork: " + error);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("CheckUserFieldWork", "CheckUserFieldWork: " + error.getMessage());

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
                params.put("current_lat",lat);
                params.put("current_long",longi);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(mcontext).addToRequestQueue(strReq, cancel_req_tag);
    }

}
