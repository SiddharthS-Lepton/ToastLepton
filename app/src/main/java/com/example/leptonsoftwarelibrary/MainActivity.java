package com.example.leptonsoftwarelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lepton.LeptonAppUtil;
import com.example.lepton.LeptonMapsActivity;
import com.example.lepton.LocationSearchActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        startActivity(new Intent(this, LeptonMapsActivity.class));

    }
}