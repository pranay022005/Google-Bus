package com.example.googlebus.Comman;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//this java used to check internet connect or not
public class NetworkDetails {
//class and method anywhere use

    public static boolean isConnectedToInternet(Context context) {
//connect network check service
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

            if (networkInfos != null) ;
            {
                //multiple time condition exicute
                for (int i = 0; i<networkInfos.length; i++)
                {
                    if (networkInfos[i].getState() ==  NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}