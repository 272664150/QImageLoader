package com.example.qimageloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.vinci.Vinci;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Vinci.getInstance()
                .load(findViewById(R.id.iv_show),
                        R.mipmap.ic_launcher,
                        null);
    }
}
