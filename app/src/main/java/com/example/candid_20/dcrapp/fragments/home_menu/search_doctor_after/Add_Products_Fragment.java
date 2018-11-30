package com.example.candid_20.dcrapp.fragments.home_menu.search_doctor_after;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.candid_20.dcrapp.R;
import com.example.candid_20.dcrapp.activity.MainActivity;
import com.example.candid_20.dcrapp.bean.ItemModel;
import com.example.candid_20.dcrapp.bean.ItemModel_Selected;
import com.example.candid_20.dcrapp.bean.for_dcr_products.Dcr_Products_Bean;
import com.example.candid_20.dcrapp.bean.for_dcr_products.Dcr_Products_LxDetails_Bean;
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
import java.util.Map;

public class Add_Products_Fragment extends Fragment implements View.OnClickListener {

    View v;
    TextView txt_title,txt_doctor_namee,txt_degree,txt_doc_address;
    MySharedPref sp;
    String ldata,user_id4,token,company_name;
    RelativeLayout rr_InboxDetailRV;
    RecyclerView InboxDetailRV;
    ProgressBar loader;
    Dcr_Products_Bean dcr_products_bean;
    CustomGetProducts_Adp customGetProducts_adp;
    ArrayList<Integer> arr_product;
    ArrayList<Integer> bool_lst;
    ArrayList<String> qty_lst;
    CheckBox chk_left_checked;
    Button btn_ok;
    int bool_size,qut_size;

    public static Add_Products_Fragment newInstance() {
        Add_Products_Fragment fragment = new Add_Products_Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MySharedPref.bean_list=new ArrayList<ItemModel>();
        v = inflater.inflate(R.layout.add_products, container, false);


        // ---------------------------- For  Get Saved Data -------------------------------------------------------------------------------//
        getSaveddata();

        // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
        initUi();

        return v;
    }
    // ---------------------------- For Initilize UI -------------------------------------------------------------------------------//
    private void initUi() {

        bool_lst=new ArrayList<>();
        qty_lst=new ArrayList<>();
        // ---------------------------- Casting Element -------------------------------------------------------------------------------//
        //Casting TextView for Title
        txt_title=(TextView)getActivity().findViewById(R.id.txt_title);
        //Set Text Title
        txt_title.setText("Add products");

        //Casting RelativeLayout for Product Get
        rr_InboxDetailRV=(RelativeLayout)v.findViewById(R.id.rr_InboxDetailRV);
        //Casting RecyclerView for Product Get
        InboxDetailRV=(RecyclerView)v.findViewById(R.id.InboxDetailRV) ;
        //Casting Button for Ok
        btn_ok=(Button)v.findViewById(R.id.btn_ok);
        //Casting ProgressBar for Loader
        loader=(ProgressBar)v.findViewById(R.id.loader);

        //Casting TextView for Doctor Name
        txt_doctor_namee=(TextView)v.findViewById(R.id.txt_doctor_namee);
        //Casting TextView for Doctor Degree
        txt_degree=(TextView)v.findViewById(R.id.txt_degree);
        //Casting TextView for Doctor Address
        txt_doc_address=(TextView)v.findViewById(R.id.txt_doc_address);

        //For  Set Products Create by these

        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        InboxDetailRV.setLayoutManager(mLayoutManager2);
        InboxDetailRV.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        //     InboxDetailRV.setNestedScrollingEnabled(false);
        InboxDetailRV.setItemAnimator(new DefaultItemAnimator());

        callWebservicefor_getAllInteriorAcctoCity(user_id4);
        setData();

        //Button onClickListner
        btn_ok.setOnClickListener(this);


    }

    private void setData() {
        String name =  sp.getData(getActivity(),"docter_name","null");
        String spcl = sp.getData(getActivity(),"speciality","null");
        String address = sp.getData(getActivity(),"clinic adress","null");

        //For Doctor Name
        if (!name.equalsIgnoreCase(""))
        {
            String upperString_str_doc_name = name.substring(0,1).toUpperCase() + name.substring(1);
            txt_doctor_namee.setText(upperString_str_doc_name);
        }

        //For Doctor Speciality
        if (!spcl.equalsIgnoreCase(""))
        {
            String upper_speciality = spcl.substring(0,1).toUpperCase() + spcl.substring(1);
            txt_degree.setText(upper_speciality);
        }

        //For Doctor Address
        if (!address.equalsIgnoreCase(""))
        {
            String upperString_clinic_address = address.substring(0,1).toUpperCase() + address.substring(1);
            txt_doc_address.setText(upperString_clinic_address);
        }
        else
        {
            txt_doc_address.setText("Not Provided");

        }
       // String upperString_clinic_address = address.substring(0,1).toUpperCase() + address.substring(1);
       // txt_doc_address.setText(upperString_clinic_address);
    }

    // ---------------------------- WebService Call for Getall Allocated Products-------------------------------------------------------------------------------//
    private void callWebservicefor_getAllInteriorAcctoCity(String city_id2) {


        if(Utils.isConnected(getActivity())) {

            getData_ForSearchSuggestion(city_id2);
        }
        else
        {
            Toast.makeText(getActivity(), "Please Check network conection..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v==btn_ok)
        {
          String  str_edt_quant = null;
            String  str_totl_quant = null;
int in_edt_quant = 0,in_totl_quant=0;
            boolean iseeor=false;

            if(MySharedPref.bean_list!=null) {
                System.out.println("Exception List is###"+MySharedPref.bean_list.size());
MySharedPref.bean_list22=new ArrayList<>();
                for (int k = 0; k < MySharedPref.bean_list.size(); k++) {

                    if (MySharedPref.bean_list.get(k).isSelected()) {
                        System.out.println("Exception Selected List is###"+MySharedPref.bean_list.get(k).isSelected());

                        System.out.println("Exception is###"+MySharedPref.bean_list.get(k).getEdt_product_quantity());

                         str_edt_quant=MySharedPref.bean_list.get(k).getEdt_product_quantity();
                         System.out.println("Total Amount Loop###"+MySharedPref.bean_list.get(k).getProduct_quantity());

                        str_totl_quant=MySharedPref.bean_list.get(k).getProduct_quantity();

                        System.out.println("Exception is###"+str_edt_quant);
                        System.out.println("Exception is###"+str_totl_quant);

               try {
                   in_edt_quant= Integer.parseInt(str_edt_quant);
                   in_totl_quant= Integer.parseInt(str_totl_quant);

               }
               catch (NumberFormatException e)
               {

               }

               ItemModel consultant_list_bean = new ItemModel(str_totl_quant,str_edt_quant);
                        MySharedPref.bean_list22.add(consultant_list_bean);

                        }

                        }

                if(MySharedPref.bean_list22.size()>0) {
                    System.out.println("Edit Amount###"+str_edt_quant);
                    System.out.println("Total Amount###"+in_totl_quant);

                    if (str_edt_quant == null) {
                        iseeor = true;
                        Toast.makeText(getActivity(), "Please Select Quantity", Toast.LENGTH_SHORT).show();
                    }
                  else if(str_edt_quant.equalsIgnoreCase("0"))
                    {
                        iseeor = true;
                        Toast.makeText(getActivity(), "Please Select More than 0 quantity", Toast.LENGTH_SHORT).show();
                    }

                    else if(in_edt_quant>in_totl_quant)
                    {
                        iseeor = true;
                        Toast.makeText(getActivity(), "Please Select Less than total product quantity", Toast.LENGTH_SHORT).show();
                    }
                    else if (str_edt_quant.equalsIgnoreCase("null")) {
                        iseeor = true;
                        Toast.makeText(getActivity(), "Please Select Quantity", Toast.LENGTH_SHORT).show();
                    }
                    if (!iseeor) {

                        MySharedPref sp=new MySharedPref();
                        sp.saveData(getActivity(),"checked_userid",user_id4);
                        if (getActivity() != null) {
                            int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                            if (count == 0) {
                                Intent ii = new Intent(getActivity(), MainActivity.class);
                                startActivity(ii);
                                //additional code
                            } else {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        }
                    }
                }

                else
                {
                    if (getActivity() != null) {
                        int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                        if (count == 0) {
                            Intent ii = new Intent(getActivity(), MainActivity.class);
                            startActivity(ii);
                            //additional code
                        } else {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }
                }
            }

            else
            {
                if (getActivity() != null) {
                    int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                    if (count == 0) {
                        Intent ii = new Intent(getActivity(), MainActivity.class);
                        startActivity(ii);
                        //additional code
                    } else {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }
            }

            }


    }

    private void Validate1() {

      //  String str_pro_qunt=edt_product_qty.getText().toString();
        boolean iseeor=false;
        System.out.println("Bool List Size###"+bool_lst.size());

if(bool_lst==null)
{
    iseeor=true;
    Toast.makeText(getActivity(),"Please Select This One product",Toast.LENGTH_SHORT).show();
}

else if(bool_lst.size()<=0) {

    iseeor=true;
    Toast.makeText(getActivity(),"Please Select This One product",Toast.LENGTH_SHORT).show();
}

if(bool_size==0)
{
    iseeor=true;
    Toast.makeText(getActivity(),"Please Select This One product",Toast.LENGTH_SHORT).show();
}

if(bool_lst.size()>0) {

    qut_size=qty_lst.size();

for(int i=0;i<bool_lst.size();i++)
{
    System.out.println("Bool List Size###"+bool_lst.get(i));
    for(int j=0;j<qty_lst.size();j++)
    {

     //   System.out.println("Quantity List Size###"+qty_lst.get(i));


    }
}
    //qut_size=  qty_lst.size()+1;
    if (qty_lst == null) {
            iseeor = true;
            //   edt_product_qty.setError("This field is Required");

            Toast.makeText(getActivity(), "This field is Required", Toast.LENGTH_SHORT).show();
        }

       else if (qty_lst.size() <= 0) {

            iseeor = true;
            //  edt_product_qty.setError("This field is Required");

            Toast.makeText(getActivity(), "This field is Required", Toast.LENGTH_SHORT).show();

    }

    else if(qut_size==0)
    {

        iseeor = true;
        //  edt_product_qty.setError("This field is Required");

        Toast.makeText(getActivity(), "This field is Required", Toast.LENGTH_SHORT).show();
    }

  else if(qty_lst.size()<bool_lst.size())
    {

        iseeor = true;
        //  edt_product_qty.setError("This field is Required");

        Toast.makeText(getActivity(), "This field is Required", Toast.LENGTH_SHORT).show();
    }
}


        if(!iseeor)
        {


            //edt_product_qty.setError("");
            if (getActivity() != null) {
                int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                if (count == 0) {
                    Intent ii = new Intent(getActivity(), MainActivity.class);
                    startActivity(ii);
                    //additional code
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
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


                Log.e("Id is@@@",user_id4);
                System.out.println("Id is***" + user_id4);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    // ---------------------------- Class for RecycleView for Call Start Adapter-------------------------------------------------------------------------------//
    private class CustomGetProducts_Adp extends RecyclerView.Adapter<CustomGetProducts_Adp.MyViewHolder> {

        Activity activity;
        int numberOfCheckboxesChecked = 0;
        ArrayList<Integer> l1;

        private Context mContext;
        List<Dcr_Products_LxDetails_Bean> arr_all_search_doctors3;
        List<Dcr_Products_LxDetails_Bean> arrayList = new ArrayList<>();
        List<Dcr_Products_LxDetails_Bean> arSearchlist;
        int in_total_qty;
        ArrayList<ItemModel> itemModels3;
        ArrayList<ItemModel_Selected> itemModels4 = new ArrayList<>();
        ItemModel model3;
        int in_id;
        String doc_idd, doc_idd_selected;

        public CustomGetProducts_Adp(Activity activity, List<Dcr_Products_LxDetails_Bean> arr_all_search_doctors2, ArrayList<ItemModel> itemlist2) {

            this.activity = activity;

            this.arr_all_search_doctors3 = arr_all_search_doctors2;

            this.arrayList = arr_all_search_doctors2;
            this.arSearchlist = new ArrayList<Dcr_Products_LxDetails_Bean>();

            if (arrayList != null) {
                arSearchlist.addAll(arrayList);
            }
            arr_product = new ArrayList<>();

            System.out.println("List Size###" + arr_all_search_doctors3.size());

            this.itemModels3 = itemlist2;

            bool_lst = new ArrayList<>();
            qty_lst = new ArrayList<>();
            l1 = new ArrayList<>();

            MySharedPref sp = new MySharedPref();
            doc_idd = sp.getData(getActivity(), "doc_idd", "null");
            doc_idd_selected = sp.getData(getActivity(), "doc_idd_selected", "null");

            System.out.println("Doctor idd^^^" + doc_idd);

            if (doc_idd != null) {
                if (doc_idd_selected != null) {
                    if (doc_idd.equalsIgnoreCase(doc_idd_selected)) {
                        if (MySharedPref.bean_list2 != null) {

                            if (MySharedPref.bean_list2.size() > 0) {
                                System.out.println("Size Checkbox Constructor###" + MySharedPref.bean_list2.size());
                                l1 = new ArrayList<>();
                                bool_lst = new ArrayList<>();
                                qty_lst = new ArrayList<>();
                                for (int j = 0; j < MySharedPref.bean_list2.size(); j++) {

                                    l1.add(Integer.valueOf(MySharedPref.bean_list2.get(j).getIndex()));
                                    if (MySharedPref.bean_list2.get(j).isSelected()) {
                                        //   in_id = l1.get(j);

                                        System.out.println("List Size###" + l1.size());

                                        bool_lst.add(l1.get(j));

                                        qty_lst.add(MySharedPref.bean_list2.get(j).getEdt_product_quantity());
                                        System.out.println("List Size Bool Cons###" + qty_lst.size());


                                        qut_size = qty_lst.size();
                                        bool_size = bool_lst.size();
                                        System.out.println("List Size Bool Cons###" + bool_lst.size());
                                    }
                                    //   in_id = l1.get(j);
                                    //            System.out.println("Int id###" + in_id);

                                }
                            }
                        }
                    }
                }

            }


        }


        @Override
        public CustomGetProducts_Adp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_products_adp, parent, false);

            return new CustomGetProducts_Adp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CustomGetProducts_Adp.MyViewHolder holder, final int position) {


            String str_doc_name = arr_all_search_doctors3.get(position).getPname();
            String upperString_str_doc_name = str_doc_name.substring(0, 1).toUpperCase() + str_doc_name.substring(1);
            holder.txt_product.setText(upperString_str_doc_name);


//holder.edt_product_qty.setText(arr_all_search_doctors3.get(position).getTotalQty());

            //     System.out.println("Int id Bind###" + in_id);
            //System.out.println("Size Checkbox_Position###" + position);
            MySharedPref sp = new MySharedPref();
            doc_idd = sp.getData(getActivity(), "doc_idd", "null");
            doc_idd_selected = sp.getData(getActivity(), "doc_idd_selected", "null");
            if (doc_idd != null) {
                if (doc_idd_selected != null) {
                    if (doc_idd.equalsIgnoreCase(doc_idd_selected)) {

                        if (MySharedPref.bean_list2 != null) {

                            if (MySharedPref.bean_list2.size() > 0) {

                                for (int j = 0; j < MySharedPref.bean_list2.size(); j++) {
                                    in_id = l1.get(j);
                                    if (position == in_id) {
                                        chk_left_checked.setChecked(MySharedPref.bean_list2.get(j).isSelected());

                                        numberOfCheckboxesChecked = MySharedPref.bean_list2.size();

                                        MySharedPref.bean_list.get(position).setSelected(true);


                                        if (MySharedPref.bean_list.get(position).isSelected()) {
                                            holder.edt_product_qty.setEnabled(true);
                                            holder.edt_product_qty.setText(MySharedPref.bean_list2.get(j).getEdt_product_quantity());

                                            MySharedPref.bean_list.get(position).setEdt_product_quantity(MySharedPref.bean_list2.get(j).getEdt_product_quantity());

                                        }

                                    }
                                    System.out.println("List Size###" + l1.size());
              /*   bool_lst.add(l1.get(j));

                 qty_lst.add(MySharedPref.bean_list2.get(j).getEdt_product_quantity());*/

                                    System.out.println("Int id###" + numberOfCheckboxesChecked);
                                    System.out.println("Size Checkbox_Position###" + position);


                                }
                            }

                        }
                    }
                }
            }
            in_total_qty = Integer.parseInt(arr_all_search_doctors3.get(position).getTotalQty());
            //EditText OnTextChangeListner
            chk_left_checked.setTag(new Integer(position));


            holder.edt_product_qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    try {
                        int in_edt_pro_qty = Integer.parseInt(holder.edt_product_qty.getText().toString());
                        System.out.println("Edit Quantity is###" + in_edt_pro_qty);

                        in_total_qty = Integer.parseInt(arr_all_search_doctors3.get(position).getTotalQty());
                        System.out.println("Total Quantity is###" + in_total_qty);
                        //  holder.edt_product_qty.setEnabled(true);

                        if (in_edt_pro_qty == 0) {
                            holder.edt_product_qty.setError("Please Enter more than zero");
                            btn_ok.setClickable(false);
                        } else if (in_edt_pro_qty > in_total_qty) {
                            holder.edt_product_qty.setError("Please Enter less than total Quantity");
                            MySharedPref.bean_list.get(position).setEdt_product_quantity("null");
                            btn_ok.setClickable(false);
                        }
                        else {
                            btn_ok.setClickable(true);
                            qty_lst.add(holder.edt_product_qty.getText().toString());
                            System.out.println("List Size Quant###" + qty_lst);

                            MySharedPref.bean_list.get(position).setEdt_product_quantity(holder.edt_product_qty.getText().toString());

                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Exception is###" + e);
                        MySharedPref.bean_list.get(position).setEdt_product_quantity("null");

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });

            System.out.println("Array Size Bind###" + arr_product.size());
            //CheckBox OnClickListner
            chk_left_checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Integer pos = (Integer) chk_left_checked.getTag();
                    // Toast.makeText(ctx, imageModelArrayList.get(pos).getAnimal() + " clicked!", Toast.LENGTH_SHORT).show();
                    if (cb.isChecked() && numberOfCheckboxesChecked >= 3) {
                        cb.setChecked(false);
                        Toast.makeText(getActivity(), "Max allowed three checkbox only", Toast.LENGTH_LONG).show();
                        // notifyDataSetChanged();

                    } else if (cb.isChecked()) {
                        //    MySharedPref.bean_list.get(pos).setSelected(true);
                        holder.edt_product_qty.setEnabled(true);
                        numberOfCheckboxesChecked++;
                        System.out.println("Int Selected###" + numberOfCheckboxesChecked);
                        MySharedPref.bean_list.get(position).setSelected(true);
                        MySharedPref sp = new MySharedPref();
                        sp.saveData(getActivity(), "doc_idd_selected", doc_idd);
                        bool_lst.add(position);

                        bool_size = bool_lst.size() + 1;
                        int in_total_qty1 = Integer.parseInt(arr_all_search_doctors3.get(position).getTotalQty());
                        System.out.println("Totla quat is###" + in_total_qty1);
                        if (in_total_qty1 > 0) {
                            holder.edt_product_qty.setEnabled(true);

                        } else {
                            holder.edt_product_qty.setEnabled(false);

                        }


                        //   notifyDataSetChanged();

                    } else {
                        //     MySharedPref.bean_list.get(pos).setSelected(false);
                        holder.edt_product_qty.setEnabled(false);
                        holder.edt_product_qty.setText("");

                        numberOfCheckboxesChecked--;
                        System.out.println("Int UnSelected###" + numberOfCheckboxesChecked);
                        bool_size = bool_lst.size();
                        qut_size = qty_lst.size();
                        bool_size = bool_lst.size() - 1;
                        qut_size = qty_lst.size() - 1;
                        System.out.println("List Size Bool###" + bool_lst.size());

                        // qty_lst.remove(l1.get(position));
                        MySharedPref.bean_list.get(position).setSelected(false);

                        //  customGetProducts_adp.notifyDataSetChanged();
                        //
                    }


                  /*  CheckBox cb = (CheckBox)v;
                    int clickedPos = ((Integer)cb.getTag()).intValue();
                    if (cb.isChecked() && numberOfCheckboxesChecked >= 3){
                        cb.setChecked(false);
                        Toast.makeText(getActivity(), "Max allowed three checkbox only", Toast.LENGTH_LONG).show();

                    }else {
                        if (cb.isChecked()) {
                            numberOfCheckboxesChecked++;
                            System.out.println("Total Quantity is###"+in_total_qty);
                            if(in_total_qty>0)
                            {
                                holder.edt_product_qty.setEnabled(true);

                            }

                            else
                            {
                                holder.edt_product_qty.setEnabled(false);

                            }


                        } else {
                            numberOfCheckboxesChecked--;
                            holder.edt_product_qty.setEnabled(false);
                        }



                        itemModels3.get(clickedPos).setSelected(cb.isChecked());
                        notifyDataSetChanged();

                    }*/

                }
            });

        }

        public List getSelectedItem() {
            List itemModelList = new ArrayList<>();
            for (int i = 0; i < itemModels3.size(); i++) {
                ItemModel itemModel = (ItemModel) itemModels3.get(i);
                if (itemModel.isSelected()) {
                    System.out.println("L1 is&&&" + itemModel);
                    itemModelList.add(itemModel);
                }
            }
            return itemModelList;
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
            return MySharedPref.bean_list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_product;
            EditText edt_product_qty;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt_product = (TextView) itemView.findViewById(R.id.txt_product);
                edt_product_qty = (EditText) itemView.findViewById(R.id.edt_product_qty);
                chk_left_checked = (CheckBox) itemView.findViewById(R.id.chk_left_checked);

            }


        }

        public void filter(String charText) {
            charText = charText.toString().toLowerCase();
            System.out.println("Character in Filter is###" + charText);
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arSearchlist);
            } else {
                for (Dcr_Products_LxDetails_Bean wp : arSearchlist) {
                    if (wp.getPname().toLowerCase().startsWith(charText)) {
                        arrayList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
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

                        MySharedPref.bean_list= new ArrayList<ItemModel>();
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




    MySharedPref.bean_list = new ArrayList<ItemModel>();
    for (int i = 0; i < dcr_products_bean.getResult().size(); i++) {


        String id = dcr_products_bean.getResult().get(i).getProductId();
        String product_name1 = dcr_products_bean.getResult().get(i).getPname();
        String pro_quantity1 = dcr_products_bean.getResult().get(i).getTotalQty();
        //  boolean   is_selected=MySharedPref.bean_list.get(i).isSelected();
        String index1 = dcr_products_bean.getResult().get(i).getIndex();


        ItemModel consultant_list_bean = new ItemModel(id,index1, product_name1, pro_quantity1);
        MySharedPref.bean_list.add(consultant_list_bean);
    }

                            // ---------------------------- Set Custom RecycleView and Adapter -------------------------------------------------------------------------------//

                            customGetProducts_adp = new CustomGetProducts_Adp(getActivity(),dcr_products_bean.getResult(),MySharedPref.bean_list);
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

}
