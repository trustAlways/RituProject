package com.example.candid_20.dcrapp.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

import com.example.candid_20.dcrapp.bean.ItemModel;
import com.example.candid_20.dcrapp.bean.ItemModelArea;
import com.example.candid_20.dcrapp.bean.ItemModelArea_Selected;
import com.example.candid_20.dcrapp.bean.ItemModelAreaaa;
import com.example.candid_20.dcrapp.bean.ItemModel_Selected;
import com.example.candid_20.dcrapp.bean.for_gift.ItemModelGift;
import com.example.candid_20.dcrapp.bean.for_gift.ItemModel_Selected_Gift;

import java.util.ArrayList;


/**
 * Created by technorizen on 19/4/17.
 */

public class MySharedPref {
     SharedPreferences sp;

    public void saveData(Context context, String key, String value)
    {
        if (context!=null)
        {
            sp = context.getSharedPreferences("DCRapp",context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key,value);
            editor.commit();
        }

    }
    public String getData(Context context, String key, String value)
    {
        if (context!=null) {
            sp = context.getSharedPreferences("DCRapp", context.MODE_PRIVATE);
        }
        return sp.getString(key, value);
    }

    public void saveData1(Context context, String key, String[] value)
    {
        sp = context.getSharedPreferences("DCRapp",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key +" _size ", value.length);
        for(int i=0 ; i<=value.length ; i++){
        editor.putString(key + "_ " + i, value[i]);
    }
        //editor.putString(key,value);
      //  editor.putString(key,value);
        editor.commit();
    }



    public String getData1(Context context, String key, String value)
    {
        sp = context.getSharedPreferences("DCRapp",context.MODE_PRIVATE);
        return sp.getString(key,value);
    }
    public void DeleteData(Context context)
    {
        sp = context.getSharedPreferences("DCRapp",context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }


    public void NullData(Context context , String key)
    {
        sp = context.getSharedPreferences("DCRapp",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,null);
        editor.commit();
    }
    public  void removeFromSharedPreferences(Context context, String key) {

        sp=context.getSharedPreferences("DCRapp",context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
     /*  if (mContext != null) {
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
            if (mSharedPreferences != null)
                mSharedPreferences.edit().remove(key).commit();
        }  */
    }
    public static ArrayList<ItemModel> bean_list;
    public static ItemModel bean;

    public static ArrayList<ItemModel> bean_list22;
    public static ItemModel bean22;

    public static ArrayList<ItemModel_Selected> bean_list2;
    public static ItemModel_Selected bean2;

    public static ArrayList<ItemModelGift> bean_list3;
    public static ItemModel bean3;

    public static ArrayList<ItemModelGift> bean_list33;
    public static ItemModelGift bean33;

    public static ArrayList<ItemModel_Selected_Gift> bean_list4;
    public static ItemModel_Selected bean4;

   /* public static ArrayList<ItemModelArea> bean_list_area;
    public static ItemModelArea beanArea;

    public static ArrayList<ItemModelAreaaa> bean_list_areaaa;
    public static ItemModelAreaaa beanAreaa;

    public static ArrayList<ItemModelArea_Selected> bean_list_selected_area;
    public static ItemModel_Selected beanareaselected;*/

    public static final String KEY_Interior_id = "interior_id";
    public static final String KEY_Lat = "update_lat";
    public static final String KEY_Long = "update_long";
    public static final String KEY_HOUR = "update_KEY_HOUR";
    public static final String KEY_MINUTE = "update_KEY_MINUTE";
    public static final String KEY_SEC = "update_KEY_Sec";

    public String getData(String key) {
        return sp.getString(key, null);
    }

    public void setData(Context activity,String key, String value) {
        sp = activity.getSharedPreferences("DCRapp",activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
