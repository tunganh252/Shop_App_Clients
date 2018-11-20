package com.example.tunganh.duan1_app;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;


public class MainActivity extends AppCompatActivity {
Button bt_dangnhap,bt_dangki;
TextView tv_slogan;

CircularProgressButton aaa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_dangnhap=findViewById(R.id.bt_dangnhap);
        bt_dangki=findViewById(R.id.bt_dangki);

        //// add font
//        tv_slogan=findViewById(R.id.text_slogan);
//        Typeface face=Typeface.createFromAsset(getAssets(),"font/NABILA.TTF");
//        tv_slogan.setTypeface(face);
        /////////////



        bt_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SigIn.class);
                startActivity(i);
            }
        });
        bt_dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Register.class);
                startActivity(i);
            }
        });
    }

    /////// Hàm back 2 lần để thoát activity
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2500);
    }
}
