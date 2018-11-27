package com.example.tunganh.duan1_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Home_2 extends AppCompatActivity {

    private TextView mTextMessage;
    private ViewFlipper viewFlipper;

    private FloatingActionButton bt_webView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    Intent i = new Intent(Home_2.this, Home.class);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };
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
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_2);

        ////// ViewFlipper /////
        viewFlipper = findViewById(R.id.viewFlipper);
        ActionViewFlipper();
        //////////////////////

        bt_webView=findViewById(R.id.bt_webView);
        bt_webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Home_2.this, com.example.tunganh.duan1_app.WebView.class);
                startActivity(i);
            }
        });

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation1);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




    }

    ///// Load quảng cáo
    private void ActionViewFlipper() {

        ArrayList<String> ArrayQuangCao = new ArrayList<>();
        ArrayQuangCao.add("https://images.foody.vn/biz_banner/foody-675x355-gdn2-636707069138735788.jpg");
        ArrayQuangCao.add("https://media3.scdn.vn/img2/2018/11_12/KtSJMu.png");
        ArrayQuangCao.add("https://cdn.tgdd.vn/qcao/22_11_2018_09_21_16_Nokia51-Ver4-800-300.png");
        ArrayQuangCao.add("https://media3.scdn.vn/img2/2018/11_9/MlPvKI.jpg");
        ArrayQuangCao.add("https://cdn.tgdd.vn/qcao/14_11_2018_16_05_26_mi8-800-300.png");
        ArrayQuangCao.add("https://cdn.tgdd.vn/qcao/04_11_2018_13_12_27_A7-P2-800-300.png");
        ArrayQuangCao.add("https://cdn.tgdd.vn/qcao/12_11_2018_17_01_49_iphone-new-800-300.png");
        for (int i = 0; i < ArrayQuangCao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(ArrayQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);

        }
        viewFlipper.setFlipInterval(3500);
        viewFlipper.setAutoStart(true);
        Animation animation_slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_quangcao);
        Animation animation_slide2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_quangcao_2);
        viewFlipper.setInAnimation(animation_slide);
        viewFlipper.setOutAnimation(animation_slide2);

    }
}
