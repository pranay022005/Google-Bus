package com.example.googlebus.Comman;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.example.googlebus.R;

public class NetworkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!NetworkDetails.isConnectedToInternet(context))
        {
            AlertDialog.Builder ad= new AlertDialog.Builder(context); //dialog build
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_internet_connection_,null);
            ad.setView(layout_dialog);

            AppCompatButton btnTryAgain = layout_dialog.findViewById(R.id.btnCheckInternateTryAgain);

            AlertDialog alertDialog = ad.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);

            btnTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    onReceive(context,intent);
                }
            });
            //layoutInflater = layout call
            //form(context)
        }
        else
        {
            Toast.makeText(context, "Your Internet is Connected", Toast.LENGTH_SHORT).show();
        }
    }
}
//Activity
//service => Long Running Operation background
//Broadcast Receiver => System Communication and App Communicate
//content Provider => Data Store, Data Pass