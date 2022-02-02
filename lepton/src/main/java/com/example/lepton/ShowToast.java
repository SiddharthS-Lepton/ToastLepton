package com.example.lepton;

import android.content.Context;
import android.widget.Toast;

public class ShowToast {

    public static void showToast(Context c, String message){

        Toast.makeText(c,message, Toast.LENGTH_SHORT).show();

    }

}
