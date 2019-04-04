package com.example.candid_20.dcrapp.fragments.home_menu.for_map_my_work;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.bean.for_latlong.MyLatLong;
import com.example.candid_20.dcrapp.storage.MySharedPref;
import com.example.candid_20.dcrapp.volleyconnector.AppSingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Map_My_Work extends Fragment {

    View v;
    TextView txt_title;
    String ldata, dcr_table_insert_id, user_id4, role_id, token, dcr_chemist_stockist_live_table_id, company_name;
    MySharedPref sp;

    //map view
    GoogleMap googleMap;
    MapView mMapView;
    ArrayList<MyLatLong> arrayList;
    ArrayList<LatLng> points;

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int PATTERN_GAP_LENGTH_PX = 10;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    // Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP,DASH);

    //
    double currentlat,currentlong;


    public static Map_My_Work newInstance() {
        Map_My_Work fragment = new Map_My_Work();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.change_area_view, container, false);

        // ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//
        getSaveddata();


        // ---------------------------- For Check Permission -------------------------------------------------------------------------------//
        // checkForPermission();

        // ---------------------------- For  Bundle Data -------------------------------------------------------------------------------//
        // getBundleData();


        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        return v;
    }

    private void getSaveddata() {

        sp = new MySharedPref();
        points = new ArrayList<>();

        company_name = sp.getData(getActivity(), "company_name", "");
        System.out.println("company name  : " + company_name);

        ldata = sp.getData(getActivity(), "ldata", "null");
        Log.e("LdataHome", ldata);

        if (ldata != null)
        {
            try {
                JSONObject jsonObject = new JSONObject(ldata);
                user_id4 = jsonObject.getString("user_id");

                role_id = jsonObject.getString("role_id");
                System.out.println("user role id ---- " + role_id);

                token = jsonObject.getString("token");
                dcr_table_insert_id = sp.getData(getActivity(), "dcr_table_insert_id", "null");
                dcr_chemist_stockist_live_table_id = sp.getData(getActivity(), "dcr_chemist_stockist_live_table_id", "null");

                System.out.println("Id is***" + user_id4);

               currentlat = Double.parseDouble(sp.getData(getActivity(), MySharedPref.KEY_Lat,"null"));
               currentlong = Double.parseDouble(sp.getData(getActivity(), MySharedPref.KEY_Long, "null"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
//-----------------------------------------------------------------------------------------------------------------
    private void initUi(Bundle savedInstanceState) {

        //Casting TextView for Title
        txt_title = (TextView) getActivity().findViewById(R.id.txt_title);
        //Set Text
        txt_title.setText("Map My Work");


        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                String city_name = getAddressFromLatlong(currentlat, currentlong);
                System.out.println("City Name&&&" + city_name);

                LatLng myLocation1 = new LatLng(currentlat,currentlong);
                googleMap.addMarker(new MarkerOptions().position(myLocation1).title("Currently active location").snippet(city_name));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation1).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                for_Add_Markers();
            }
        });


    }

    // ---------------------------- For  WebService Call Method for Add Expenses--------------------------------------------------------------------------------//
    private void for_Add_Markers() {

        arrayList = new ArrayList<>();

        String url = "http://dailyreporting.in/"+company_name+"/api/get_currentlocation";
        System.out.println("sout url"+ url);


        // Tag used to cancel the request
        String cancel_req_tag = "area";
       /* StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_DAY_EDCRRVRECORD, new Response.Listener<String>() {*/
        //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Expenses Record:", "ExpensesRecord Visit: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    String message=jObj.getString("message");

                    if (error.equals("success")) {
                    try {
                        JSONArray jsonArray =jObj.getJSONArray("result");
                        System.out.println("Result Expenses Record:***"+jsonArray);

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                         for (int i = 0; i < jsonArray.length();i++)
                         {
                             JSONObject jsonObject = jsonArray.getJSONObject(i);
                             String id = jsonObject.getString("id");
                             String lat = jsonObject.getString("checkin_lat");
                             String lng = jsonObject.getString("checkin_long");
                             String stime = jsonObject.getString("stime");
                             String etime = jsonObject.getString("etime");
                             String drname = jsonObject.getString("dr_name");


                             arrayList.add(new MyLatLong(id,lat,lng,stime,etime,drname));
                             System.out.println("Exception is###"+lat);

                         }

                         setupMarker();


                        }
                        catch (Exception e)
                        {
                            System.out.println("Exception is###"+e);
                        }

                    }
                    else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Expenses Record:", "ExpensesRecord:: " + error.getMessage());
                try {
                    Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
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

                System.out.println("Dcr Doctor Live Table Insert id###"+user_id4);

                params.put("user_id",user_id4);
                params.put("date","2019-04-03");

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }
//================================================================
    private void setupMarker() {

        arrayList.add(new MyLatLong("12","22.7195","75.8767","19:22:47","19:22:47", "fg"));
        arrayList.add(new MyLatLong("22","22.7204","75.8702","19:22:47","19:22:47", ""));
        arrayList.add(new MyLatLong("32","22.7244","75.8839","19:22:47","19:22:47", "dgfg"));


        // For dropping a marker at a point on the Map
        for (int i=0;i<arrayList.size();i++)
        {
            Double currentlat = Double.valueOf(arrayList.get(i).getLat());
            Double currentlong = Double.valueOf(arrayList.get(i).getLng());

            if (currentlat>0 && currentlong>0)
            {
             LatLng myLocation = new LatLng(currentlat,currentlong);

             String city_name = getAddressFromLatlong(currentlat, currentlong);
             System.out.println("City Name&&&" + city_name);

             MarkerOptions options = new MarkerOptions();

             if (arrayList.get(i).getDrname().equalsIgnoreCase(user_id4))
             {
                 googleMap.addMarker(options.position(myLocation).title("\nStart Time :"+arrayList.get(i).getStime()
                         +"\nEnd Time :"+arrayList.get(i).getEtime()).snippet(city_name));
             }
             else
             {
                 googleMap.addMarker(options.position(myLocation).title(arrayList.get(i).getDrname()+"\n\nStart Time :"+arrayList.get(i).getStime()
                         +"\nEnd Time :"+arrayList.get(i).getEtime()).snippet(city_name));
             }

             System.out.println("mylocation***" + myLocation);

             points.add(myLocation);

             googleMap.addPolyline(new PolylineOptions().addAll(points).geodesic(true).color(getResources().getColor(R.color.red)).width(POLYLINE_STROKE_WIDTH_PX).pattern(PATTERN_POLYLINE_DOTTED));
            }
        }

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    // Defines the contents of the InfoWindow
                    @Override
                    public View getInfoContents(Marker arg0) {

                        // Getting view from the layout file infowindowlayout.xml
                        View v = getLayoutInflater().inflate(R.layout.info_window_adapter, null);

                        //LatLng latLng = arg0.getPosition();

                        ImageView im = (ImageView) v.findViewById(R.id.imageView1);
                        TextView tv = (TextView) v.findViewById(R.id.tittle);
                        TextView tv1 = (TextView) v.findViewById(R.id.textView1);
                        TextView tv2 = (TextView) v.findViewById(R.id.textView2);

                        String title= arg0.getTitle();
                        String informations=arg0.getSnippet();

                        tv.setText(title);
                        tv1.setText(informations);
                        //tv2.setText(arrayList.get(i).getDrname());

                        im.setImageResource(R.drawable.doctor_reminder);
                        return v;

                    }
                });

        Double currentlat = Double.valueOf(arrayList.get(0).getLat());
        Double currentlong = Double.valueOf(arrayList.get(0).getLng());

        LatLng latLng = new LatLng(currentlat,currentlong);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
//-------------------------------------------------------------------------------------------------------------------
public String getAddressFromLatlong(double currentlat2, double currentlong2)
{
    Geocoder geocoder;
    String city = null;
    List<Address> addresses;

    try {
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        addresses = geocoder.getFromLocation(currentlat2, currentlong2, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getLocality(); // If any additional address line present than only,
        // check with max available address lines by getMaxAddressLineIndex()
        city = addresses.get(0).getAddressLine(0);
        System.out.println("Address$$$"+addresses);
        System.out.println("Address***"+address);

        //Set Address in TextView
        // txt_current_loc_descp.setText(address);
        // img_curent_location.clearAnimation();


    }
    catch (Exception e)
    {
        e.printStackTrace();
    }

    return city;
}

}
