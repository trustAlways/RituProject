package com.example.candid_20.dcrapp.fragments.home_menu.for_add_expenses;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.activity.LoginActivity;
import com.example.candid_20.dcrapp.activity.MainActivity;
import com.example.candid_20.dcrapp.bean.DCR_REVIEW_DATA.dcr_review_chemist_visited;
import com.example.candid_20.dcrapp.bean.DCR_REVIEW_DATA.dcr_review_docter;
import com.example.candid_20.dcrapp.bean.DCR_REVIEW_DATA.dcr_review_docter_reminder;
import com.example.candid_20.dcrapp.constant.FilePath;
import com.example.candid_20.dcrapp.constant.Utils;
import com.example.candid_20.dcrapp.custom_gallery.Action;
import com.example.candid_20.dcrapp.fragments.HomeFragments;
import com.example.candid_20.dcrapp.fragments.home_menu.TimeLineFragment;
import com.example.candid_20.dcrapp.fragments.home_menu.search_doctor_after.CallStart_Fragment;
import com.example.candid_20.dcrapp.fragments.home_menu.view_DCR.DCR_Fullview_Frag;
import com.example.candid_20.dcrapp.other.URLs;
import com.example.candid_20.dcrapp.storage.MySharedPref;
import com.example.candid_20.dcrapp.volleyconnector.AppSingleton;
import com.example.candid_20.dcrapp.volleyconnector.VolleyMultipartRequest;
import com.google.gson.JsonArray;
import com.nononsenseapps.filepicker.FilePickerActivity;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


import static android.app.Activity.RESULT_OK;
import static android.content.Intent.EXTRA_ALLOW_MULTIPLE;

public class Expenses_Frag extends Fragment implements View.OnClickListener {
    View v;
    MySharedPref sp;
    String ldata,dcr_table_insert_id,user_id4,role_id;
    ProgressBar loader;
    TextView txt_title;
    RecyclerView txtlistview_image_doc_name;
    UserAdapter adapter;
    String str_edt_reminder_expenses,str_edt_telephone,str_edt_fwa,str_sundary,str_printingandstationary,
            str_remark,str_car_taxi,petrol,work_via_value,str_petrol,region_id;
    EditText edt_reminder_expenses,edt_cartaxi_expenses,edt_telephone,edt_fwa,edt_sundary,edt_printingstationary,edt_remark,edt_petrol;
    RelativeLayout rr_addfiles_checked_unchecked,rr_dcrsubmit_checked_unchecked;
    TextView text_petrol,txt_car_expense;
    String path;
    //Pdf request code
    private int PICK_PDF_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Uri to store the image uri
    private Uri filePath;
    Uri imgUri;

    String company_name;
    Bitmap bitmap;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> real_path_list = new ArrayList<>();

    public static Expenses_Frag newInstance() {
        Expenses_Frag fragment = new Expenses_Frag();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.expenses, container, false);

        // ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//
        getSaveddata();

        //Requesting storage permission
        requestStoragePermission();
        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();


        return v;
    }
    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
    private void initUi() {
        // ---------------------------- For Casting Elements -------------------------------------------------------------------------------//
        //Casting ProgressBar for loading
        loader=(ProgressBar)v.findViewById(R.id.expenseloader);
        //Casting TextView for Title
        txt_title=(TextView)getActivity().findViewById(R.id.txt_title);
        //Set Text Title
        txt_title.setText("Add Expenses");

        //casting textview for showing images name
        txtlistview_image_doc_name = (RecyclerView) v.findViewById(R.id.list_item_image_doc_name);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        txtlistview_image_doc_name.setLayoutManager(linearLayoutManager);


        //Casting Edittext for Reminder Expenses
        edt_reminder_expenses=(EditText) v.findViewById(R.id.edt_reminder_expenses);
        //Casting Edittext for Telephone
        edt_telephone=(EditText) v.findViewById(R.id.edt_telephone);

        //Casting Edittext for FWA
        edt_fwa=(EditText) v.findViewById(R.id.edt_fwa);

        //Casting Edittext for Sundary
        edt_sundary=(EditText) v.findViewById(R.id.edt_sundary);

        //Casting Edittext for Printing and Stationary
        edt_printingstationary=(EditText) v.findViewById(R.id.edt_printingstationary);

        //Casting Edittext for Petrol
        edt_petrol=(EditText) v.findViewById(R.id.edt_petrol);

        //Casting Edittext for Remark
        edt_remark=(EditText)v.findViewById(R.id.edt_remark);

        //Casting RelativeLayout for Add Files
        rr_addfiles_checked_unchecked=(RelativeLayout)v.findViewById(R.id.rr_addfiles_checked_unchecked);

        //Casting RelativeLayout for DCR Submit
        rr_dcrsubmit_checked_unchecked=(RelativeLayout)v.findViewById(R.id.rr_dcrsubmit_checked_unchecked);
        text_petrol = (TextView)v.findViewById(R.id.txt_petrol);

        edt_cartaxi_expenses = (EditText)v.findViewById(R.id.edt_car_expenses);
        txt_car_expense = (TextView)v.findViewById(R.id.txt_car_expense);

        //Check Petrol Conditiom
        System.out.println("work via"+ work_via_value + petrol);

        if (work_via_value.equalsIgnoreCase("Car/Taxi"))
        {
            txt_car_expense.setVisibility(View.VISIBLE);
            edt_cartaxi_expenses.setVisibility(View.VISIBLE);
            edt_cartaxi_expenses.setEnabled(true);
        }
        else if(work_via_value.equalsIgnoreCase("Bike"))
        {
            if(!petrol.equalsIgnoreCase("null") && !petrol.equalsIgnoreCase(""))
            {
                //Set Petrol Visibility
                text_petrol.setVisibility(View.VISIBLE);
                edt_petrol.setVisibility(View.VISIBLE);
                edt_petrol.setText(petrol);
                edt_petrol.setEnabled(false);
            }
            else
            {
                text_petrol.setVisibility(View.VISIBLE);
                edt_petrol.setVisibility(View.VISIBLE);
                edt_petrol.setText("0");
                edt_petrol.setEnabled(false);
            }
        }
        else
        {
            if(!petrol.equalsIgnoreCase("null") && !petrol.equalsIgnoreCase(""))
            {
                //Set Petrol Visibility
                text_petrol.setVisibility(View.VISIBLE);
                edt_petrol.setVisibility(View.VISIBLE);
                edt_petrol.setText(petrol);
                edt_petrol.setEnabled(false);
            }
        }






        if (str_edt_fwa!=null)
        {
            if(!str_edt_fwa.equalsIgnoreCase("null") && !str_edt_fwa.equalsIgnoreCase("")) {
                //Set Petrol Visibility
                edt_fwa.setText(str_edt_fwa);
                edt_fwa.setEnabled(false);
            }
            else
            {
                edt_fwa.setText("0");
                edt_fwa.setEnabled(false);
            }
        }


        // ---------------------------- set OnClickListner-------------------------------------------------------------------------------//
//Add Files
        rr_addfiles_checked_unchecked.setOnClickListener(this);
        //DCR Submit
        rr_dcrsubmit_checked_unchecked.setOnClickListener(this);


        //getSubmitedExpenses
         //get_Submit_expenses();
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
                role_id = jsonObject.getString("role_id");

                dcr_table_insert_id=sp.getData(getActivity(),"dcr_table_insert_id","null");
                petrol=sp.getData(getActivity(),"petrol","null");
                str_edt_fwa=sp.getData(getActivity(),"field_work_allowence","null");
                work_via_value=sp.getData(getActivity(),"work_via","null");
                region_id = sp.getData(getActivity(),"region_id","null");

                Log.e("Id is@@@",user_id4);
                System.out.println("Id is***" + user_id4);
                System.out.println("petrol is***" + petrol + str_edt_fwa + work_via_value + region_id);

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onClick(View v) {
    if(v==rr_dcrsubmit_checked_unchecked)
    {
        //For Validation
        Validate();
    }

    if(v==rr_addfiles_checked_unchecked)
    {
        //openGallery Condition
        //openGallery();

        openPdf();
    }
}

    //------------------------------------manage condition for open pdf files ----------------------------------------------------------
    private void openPdf() {

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
       // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select File/Pdf"), PICK_PDF_REQUEST);
    }

// ---------------------------- For  Open Gallery -------------------------------------------------------------------------------//
  /*  private void openGallery() {

        Intent i = new Intent(getActivity(), FilePickerActivity.class);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        if (i.resolveActivity(getActivity().getPackageManager())!=null) {

            File photoFile = null;
            try {

                photoFile = createImageFile();
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if(photoFile!=null)
            {
                Uri photoUri = null;
                photoUri = FileProvider.getUriForFile(getActivity(), "com.example.candid_20.dcrapp.fileprovider", photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION );
                startActivityForResult(i, 42);
            }
        }


        *//* File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SOMEFOLDER");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent1 = Intent.createChooser(intent, "Open With");
        try {
            startActivity(intent1);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }*//*
        }
*/
    // This always works

    // ---------------------------- onActivityResult -------------------------------------------------------------------------------//


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK ) {
            if (data.getBooleanExtra(EXTRA_ALLOW_MULTIPLE, true)) {
                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                      ClipData clip = data.getClipData();
                        if (clip != null) {
                            for (int i = 0; i < clip.getItemCount(); i++) {
                              Uri uri = clip.getItemAt(i).getUri();
                              filePath = uri;
                              System.out.println("filepath " + filePath);

                              if (filePath!=null)
                              {
                                  arrayList.add(String.valueOf(filePath));
                                  real_path_list.add(FilePath.getRealPath(getActivity(),filePath));
                              }

                              if (arrayList != null) {
                                for (String path : arrayList) {
                                    System.out.println("arralist data " + path);
                                }
                                  adapter = new UserAdapter(getActivity(),real_path_list);
                                  txtlistview_image_doc_name.setAdapter(adapter);
                            }
                        }
                    }

                else
                    {
                        imgUri = data.getData();
                        if (imgUri!=null)
                        {
                            real_path_list.add(FilePath.getRealPath(getActivity(),imgUri));
                            adapter = new UserAdapter(getActivity(),real_path_list);
                            txtlistview_image_doc_name.setAdapter(adapter);
                        }
                        System.out.println("clip data123567890   " + imgUri);


                    }
                }
                    // For Ice Cream Sandwich
            }
        else {
                 filePath = data.getData();
                 if (filePath!=null)
                 {
                    real_path_list.add(FilePath.getRealPath(getActivity(),filePath));
                    adapter = new UserAdapter(getActivity(),real_path_list);
                    txtlistview_image_doc_name.setAdapter(adapter);
                    System.out.println("clip data12356789   " + filePath);
                 }
                 else
                 {
                     Toast.makeText(getActivity(), "File Path Not Available.", Toast.LENGTH_SHORT).show();
                 }


                    /*ArrayList<String> paths = data.getStringArrayListExtra
                            (FilePickerActivity.EXTRA_PATHS);

                    if (paths != null) {
                        for (String path : paths) {
                            Uri uri = Uri.parse(path);
                            Toast.makeText(getActivity(), ""+uri, Toast.LENGTH_SHORT).show();
                        }
                    }*/
                }

            }
         /*   else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                         filePath = data.getData();
                         System.out.println("pathhyhyy "+ filePath);
                      }*/


            else {
                    if (data != null)
                    {
                        Uri uri = data.getData();
                        // Do something with the URI
                        Toast.makeText(getActivity(), "123"+uri, Toast.LENGTH_SHORT).show();
                    }
                }

        }



  /*  private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
       // File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)));
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        selectedImagePath = image.getAbsolutePath();

        return image;
    }*/

// ---------------------------- For  Validation -------------------------------------------------------------------------------//
    private void Validate() {

         str_edt_reminder_expenses=edt_reminder_expenses.getText().toString();
         str_edt_telephone=edt_telephone.getText().toString();
         str_edt_fwa=edt_fwa.getText().toString();
         str_sundary=edt_sundary.getText().toString();
         str_printingandstationary=edt_printingstationary.getText().toString();
         str_petrol=edt_petrol.getText().toString();
         str_remark=edt_remark.getText().toString();
         str_car_taxi=edt_cartaxi_expenses.getText().toString();


        boolean iserror=false;
        callWebService_forDCRSubmit();
       /* if(dcr_table_insert_id==null)
        {
            iserror=true;
            Toast.makeText(getActivity(),"Please Start field Work",Toast.LENGTH_SHORT).show();
        }

        if(str_edt_reminder_expenses.equalsIgnoreCase(""))
        {
            iserror=true;
            txt_reminder_expenses_error.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_reminder_expenses_error.setVisibility(View.GONE);
        }
        if(str_edt_telephone.equalsIgnoreCase(""))
        {
            iserror=true;
            txt_telephone_error.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_telephone_error.setVisibility(View.GONE);
        }
        if(str_edt_fwa.equalsIgnoreCase(""))
        {
            iserror=true;
            txt_fwa_error.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_fwa_error.setVisibility(View.GONE);
        }
        if(str_sundary.equalsIgnoreCase(""))
        {
            iserror=true;
            txt_sundary_error.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_sundary_error.setVisibility(View.GONE);
        }
        if(str_printingandstationary.equalsIgnoreCase(""))
        {
            iserror=true;
            txt_printingstationary_error.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_printingstationary_error.setVisibility(View.GONE);
        }
        if(petrol!=null)
        {
            if(!petrol.equalsIgnoreCase("")) {

                if (str_petrol.equalsIgnoreCase("")) {
                    iserror = true;
                    txt_petrol_error.setVisibility(View.VISIBLE);
                } else {
                    txt_petrol_error.setVisibility(View.GONE);
                }
            }
            }
        if(str_remark.equalsIgnoreCase(""))
        {
            iserror=true;
            txt_remark_error.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_remark_error.setVisibility(View.GONE);
        }
*/
        if(!iserror)
        {
           /* txt_reminder_expenses_error.setVisibility(View.GONE);
            txt_telephone_error.setVisibility(View.GONE);
            txt_fwa_error.setVisibility(View.GONE);
            txt_sundary_error.setVisibility(View.GONE);
            txt_printingstationary_error.setVisibility(View.GONE);
            txt_remark_error.setVisibility(View.GONE);*/


        }
    }

    // ---------------------------- For  WebService Call Method with Internet Connectivity-------------------------------------------------------------------------------//
    private void callWebService_forDCRSubmit() {
        // Check Internet Connectivity
        if (Utils.isConnected(getActivity())) {
            //Call WebService
             sendImages();
        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }
    // ---------------------------- For  WebService Call Method -------------------------------------------------------------------------------//
   /* private void dcr_Submit_ws() {
        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_EXPENSES_SUBMIT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Expenses Submit", "Expenses Submit response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);


                        String result=jObj.getString("result");
                        System.out.println("Result Expenses Submit***"+result);
                        //JSONObject jsonObject22=new JSONObject(result);
                        String message=jObj.getString("message");

                        Toast.makeText(getActivity(), message,Toast.LENGTH_SHORT).show();

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFrame, new HomeFragments())
                                .addToBackStack("27")
                                .commit();

                    }
                    else {
                        loader.setVisibility(View.GONE);

                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getActivity(), "Nothing to submit.",Toast.LENGTH_SHORT).show();

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

                Log.e("ExpensesSubmitResponse", "Expenses Submit  Error: " + error.getMessage());
                try {
                   *//* JSONObject jsonObject22=new JSONObject(error.getMessage());

                    String errorMsg=jsonObject22.getString("message");*//*
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
                params.put("dcr_table_insert_id",dcr_table_insert_id);
                params.put("user_id",user_id4);
                System.out.println("reminder +"+ str_edt_reminder_expenses);
                params.put("reminder_exp",str_edt_reminder_expenses);
                params.put("telephone",str_edt_telephone);
                params.put("photocopy",str_printingandstationary);
                params.put("fwa",str_edt_fwa);
                params.put("sundry",str_sundary);
                System.out.println("petroll "+ str_petrol);
               // params.put("petrol",str_petrol);
                if(petrol!=null)
                {
                    if(!petrol.equalsIgnoreCase("")) {
                        params.put("petrol",petrol);
                    }

                    else
                    {
                        params.put("petrol","");

                    }

                    }
                    else
                {

                }
                params.put("remark",str_remark);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);


    }
*/




    public void uploadMultipart(String path) {

        String url = "http://dailyreporting.in/"+company_name+"/api/expenses_submit";
        System.out.println("sout url"+ url);

        System.out.println("pathh final"+ path);
        //getting the actual path of the image
       /* if (filePath!=null)
        {
            for (String path : arrayList) {
                path = FilePath.getPath(getActivity(), filePath);
            }
            System.out.println("pathh"+ path);
        }*/
        if (path == null) {
            Toast.makeText(getActivity(), "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        }
        else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(getActivity(), uploadId, url)
                        .addFileToUpload(path, "files[]") //Adding file
                         //Adding text parameter to the request
                        .addParameter("dcr_table_insert_id",dcr_table_insert_id)
                        .addHeader("X-API-KEY","TEST@123")
                        .addParameter("user_id",user_id4)
                        .addParameter("reminder_exp",str_edt_reminder_expenses)
                        .addParameter("telephone",str_edt_telephone)
                        //.setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

                System.out.println("path@@@"+path);
                System.out.println("dcr_table_insert_id@@@"+dcr_table_insert_id);
                System.out.println(",user_id4##"+ user_id4);


            }
            catch (Exception exc) {
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void sendImages() {

        String url = "http://dailyreporting.in/"+company_name+"/api/expenses_submit";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest;
       /* volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,URLs.URL_EXPENSES_SUBMIT,
                new Response.Listener<NetworkResponse>() {
*/
        volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,url,
                new Response.Listener<NetworkResponse>() {

                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("Expenses Submit", "Expenses Submit response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(new String(response.data));
                            String error = jObj.getString("status");

                            if (error.equals("success")) {
                                loader.setVisibility(View.GONE);

                                System.out.println("data size  "+ arrayList.size());

                                if (arrayList!=null && arrayList.size()!=0)
                                {
                                    for (String path : arrayList) {
                                        System.out.println("after submit :"+ path);

                                        if (Build.VERSION.SDK_INT < 11)
                                        {
                                            String path2 = FilePath.getRealPathFromURI_BelowAPI11(getActivity(), Uri.parse(path));
                                            uploadMultipart(path2);
                                         }
                                        // SDK >= 11 && SDK < 19
                                        else if (Build.VERSION.SDK_INT < 19)
                                        {
                                            String path2 = FilePath.getRealPathFromURI_API11to18(getActivity(), Uri.parse(path));
                                            uploadMultipart(path2);

                                        }
                                        // SDK > 19 (Android 4.4)
                                        else
                                        {
                                            System.out.println("sdk version :"+ Build.VERSION.SDK_INT);

                                            String path2 = FilePath.getRealPathFromURI_API19(getActivity(), Uri.parse(path));
                                            uploadMultipart(path2);
                                        }

                                       // String path2 = FilePath.getRealPath(getActivity(), Uri.parse(path));
                                      //  System.out.println("ubmit path :"+ path2);

                                       // uploadMultipart(path2);
                                    }
                                }
                                else
                                {
                                 if (imgUri!=null)
                                 {
                                    if (Build.VERSION.SDK_INT < 19)
                                    {
                                        path = FilePath.getRealPathFromURI_API11to18(getActivity(), imgUri);
                                        System.out.println("SINGLE PATH :"+ path);
                                        if (path!=null)
                                        {
                                            uploadMultipart(path);
                                        }
                                    }
                                    else if (Build.VERSION.SDK_INT < 11)
                                    {
                                        path = FilePath.getRealPathFromURI_BelowAPI11(getActivity(), imgUri);
                                        System.out.println("SINGLE PATH :"+ path);
                                        if (path!=null)
                                        {
                                            uploadMultipart(path);
                                        }
                                    }
                                    else
                                    {
                                        path = FilePath.getRealPathFromURI_API19(getActivity(), imgUri);
                                        System.out.println("SINGLE PATH :"+ path);
                                        if (path!=null)
                                        {
                                            uploadMultipart(path);
                                        }
                                    }

                                 }

                                  /* else
                                    {
                                        System.out.println("SINGLE PATH :"+ "33");

                                        path = getRealPathFromURI(imgUri);

                                        System.out.println("SINGLE PATH 33 :"+ path);

                                        uploadMultipart(path);

                                    }*/
                                }



                                String result=jObj.getString("result");
                                System.out.println("Result Expenses Submit***"+result);
                                String message=jObj.getString("message");
                                System.out.println("message" + message);
                                Toast.makeText(getActivity(), "The expenses for today are added",Toast.LENGTH_SHORT).show();

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.contentFrame, new TimeLineFragment())
                                        .addToBackStack("7")
                                        .commit();

                            }
                            else {
                                loader.setVisibility(View.GONE);

                                String errorMsg = jObj.getString("message");
                                Toast.makeText(getActivity(), "Nothing to submit.",Toast.LENGTH_SHORT).show();

                                Log.e("errorMsg", errorMsg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loader.setVisibility(View.GONE);
                        Log.e("ExpensesSubmitResponse", "Expenses Submit  Error: " + error.getMessage());
                        try {
                            Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {



            /*  * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags*/
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-API-KEY","TEST@123");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dcr_table_insert_id",dcr_table_insert_id);
                params.put("user_id",user_id4);
                System.out.println("reminder +"+ str_edt_reminder_expenses);
                params.put("reminder_exp",str_edt_reminder_expenses);
                params.put("telephone",str_edt_telephone);
                params.put("photocopy",str_printingandstationary);
                params.put("fwa",str_edt_fwa);
                params.put("sundry",str_sundary);
                System.out.println("petroll "+ str_petrol);
                // params.put("petrol",str_petrol);
                if(petrol!=null)
                {
                    if(!petrol.equalsIgnoreCase("")) {
                        params.put("petrol",petrol);
                    }
                    else
                    {
                        params.put("petrol","");
                    }
                }
                else
                {

                }
                if(str_car_taxi!=null)
                {
                    if(!str_car_taxi.equalsIgnoreCase("")) {
                        System.out.println("car_taxi_expenses,"+str_car_taxi);
                        params.put("car_taxi_expenses",str_car_taxi);
                    }
                    else
                    {
                        params.put("car_taxi_expenses","");
                    }
                }


                params.put("remark",str_remark);
                return params;
            }
           // Here we are passing image by renaming it with a unique name
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

               /* if (arrayList != null) {
                    for (String path : arrayList) {
                         bitmap = BitmapFactory.decodeFile(path);
                         System.out.println("when server call time " + bitmap);
                         long imagename = System.currentTimeMillis();
                        if (bitmap!=null)
                        {
                           params.put("files[]", new VolleyMultipartRequest.DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                        }
                    }
                }*/

                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);

    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }



//---------------------------------------------------------get pathh


//------------------------------------------------------get Expenses-------------------------------------
  /*  private void dcr_Get_Expenses() {
        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URLs.URL_GET_EXPENSES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Expenses get", "Expenses get response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    if (error.equals("success")) {
                        loader.setVisibility(View.GONE);

                        JSONObject result=jObj.getJSONObject("result");
                        System.out.println("Result Expenses Submit***"+result);
                        //JSONObject jsonObject22=new JSONObject(result);
                        String message=jObj.getString("message");

                        String va = result.getString("petrol_expenses");
                        petrol = result.getString("petrol_values");
                        String fwa_expense = result.getString("fwa_expenses");
                        str_edt_fwa = result.getString("fwa_values");

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

                Log.e("ExpensesSubmitResponse", "Expenses Submit  Error: " + error.getMessage());
                try {
                   *//* JSONObject jsonObject22=new JSONObject(error.getMessage());

                    String errorMsg=jsonObject22.getString("message");*//*
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


    }*/

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


     public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

         Context context;
         ArrayList<String> arrayList;

         public UserAdapter(FragmentActivity activity, ArrayList<String> arrayList)
         {
             this.context = activity;
             this.arrayList = arrayList;
         }

         @NonNull
        @Override
        public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View v = LayoutInflater.from(context).inflate(R.layout.search_doctor_adp,null,false);
             return new ViewHolder(v);
        }

         @Override
         public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

             holder.deletetxt.setText("Remove");
             holder.deletetxt.setTextColor(getResources().getColor(R.color.red));
             holder.txt_current_loc_descp1.setText(arrayList.get(position).toString());


             holder.deletetxt.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     arrayList.remove(position);
                     adapter.notifyDataSetChanged();
                 }
             });
         }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_current_loc_descp1,deletetxt;


            public ViewHolder(View itemView) {
                super(itemView);

                txt_current_loc_descp1 = (TextView)itemView.findViewById(R.id.txt_current_loc_descp);
                deletetxt = (TextView)itemView.findViewById(R.id.txt_profession);

            }
        }
     }


    // ---------------------------- For WebService Call for get submit expenses -------------------------------------------------------------------------------//
    private void get_Submit_expenses() {

        String url = "http://dailyreporting.in/"+company_name+"/api/user_all_expenses";
        System.out.println("sout url"+ url);

        loader.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String cancel_req_tag = "area";
        /*StringRequest strReq = new StringRequest(Request.Method.POST, URLs.URL_FULL_DCRVIEW+"/"+user_id4,
                new com.android.volley.Response.Listener<String>() {*/
        //  "http://candid13/webservices/api/salesmen", new Response.Listener<String>() {

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DCR submit expenses", "expenses : " + response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String error = jObj.getString("status");
                            if (error.equals("success")) {

                                loader.setVisibility(View.GONE);

                                JSONObject jsonObject1 = jObj.getJSONObject("result");
                                try {
                                    // String expense_reminder_name = jsonObject1.getString("expense_reminder");
                                    String value_reminder = jsonObject1.getString("value_reminder");
                                    // String expense_telephone_name = jsonObject1.getString("expense_telephone");
                                    String value_telephone = jsonObject1.getString("value_telephone");
                                    // String expense_expense_photocopy_name = jsonObject1.getString("expense_photocopy");
                                    String value_expense_photocopy = jsonObject1.getString("value_photocopy");
                                    /// String expense_value_fwa = jsonObject1.getString("expense_sundry");
                                    String value_sundry = jsonObject1.getString("value_sundry");
                                    //String expense_petrol = jsonObject1.getString("expense_petrol");
                                    String value_petrol = jsonObject1.getString("value_petrol");

                                    String car_taxi_expenses = jsonObject1.getString("value_Car/Taxi");
                                    String value_fwa = jsonObject1.getString("value_fwa");
                                    String remark = jsonObject1.getString("value_remark");

                                    //all the expenses
                                    setdata(value_reminder, value_telephone, value_expense_photocopy, value_sundry, value_petrol, value_fwa,
                                            remark,car_taxi_expenses);



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }


                            else {
                                loader.setVisibility(View.GONE);

                                String errorMsg = jObj.getString("message");
                                if(getActivity()!=null)
                                {

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
                    Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();

                    /*if(error.getMessage()!=null) {
                        JSONObject jsonObject22 = new JSONObject(error.getMessage());

                        String errorMsg = jsonObject22.getString("message");
                        //  JSONObject jsonObject33=new JSONObject(message);
                        //   String password=jsonObject33.getString("password");

                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                    }*/

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
                System.out.println("Search id###"+user_id4);
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id4);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }

//---------------------------------all the submitted expenses------------------------------------------------------------------------------
    private void setdata(String value_reminder, String value_telephone, String value_expense_photocopy, String sundry,
                         String valuePetrol, String value_fwa, String remark,String car_taxi_expense) {

       // edt_fwa.setText(value_fwa);

        if (!value_reminder.equalsIgnoreCase("null") && !value_reminder.equalsIgnoreCase(""))
        {
            edt_reminder_expenses.setText(value_reminder);
        }
        else
        {
            edt_reminder_expenses.setText("");
        }

        if (!value_telephone.equalsIgnoreCase("null") && !value_telephone.equalsIgnoreCase(""))
        {
            edt_telephone.setText(value_telephone);
        }
        else
        {
            edt_telephone.setText("0");
        }

        if (!value_expense_photocopy.equalsIgnoreCase("null") && !value_expense_photocopy.equalsIgnoreCase(""))
        {
            edt_printingstationary.setText(value_expense_photocopy);
        }
        else
        {
            edt_printingstationary.setText("");
        }



        if (!sundry.equalsIgnoreCase("null") && !sundry.equalsIgnoreCase(""))
        {
            edt_sundary.setText(sundry);
        }
        else
        {
            edt_sundary.setText("0");
        }


        if (!remark.equalsIgnoreCase("0"))
        {
            edt_remark.setText(remark);
        }

    }


}
