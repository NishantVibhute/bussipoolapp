package com.bussinesspoolapp.bussipool.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Nishant on 3/13/2018.
 */

public class CommonUtil {
   public  static String url = "http://192.168.1.7:8084";

    public static void setSessionVariable(Context con,String name,String val,String type)
    {
        SharedPreferences settings = con.getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        switch (type)
        {
            case "int":
                editor.putInt(name, Integer.parseInt(val));
                break;
            case "String":
                editor.putString(name, val);
                break;
        }
        editor.commit();


    }

    public static Integer getSessionInt(Context con,String name)
    {
        SharedPreferences settings = con.getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        return settings.getInt(name,0);
    }


    public static String getSessionString(Context con,String name)
    {
        SharedPreferences settings = con.getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        return settings.getString(name,"");
    }

    public static Long getSessionLong(Context con,String name)
    {
        SharedPreferences settings = con.getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        return settings.getLong(name,0);
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static boolean isInternetAvailable() {
        try {
            final InetAddress address = InetAddress.getByName(CommonUtil.url);
            return !address.equals("");
        } catch (Exception e) {
            return false;
        }

    }
}
