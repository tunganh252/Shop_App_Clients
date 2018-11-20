package com.example.tunganh.duan1_app;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Slide_Hello extends AppCompatActivity {
    private ViewPager slideViewPager;
    private LinearLayout mDotsLayout;

    private SliderAdapter sliderAdapter;

    private TextView[] mDots;

    private Button bt_prev;
    private Button bt_next;
    private Button bt_finish;

    private int mCurrentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide__hello);

        slideViewPager = findViewById(R.id.slideViewPager);
        mDotsLayout = findViewById(R.id.dots);
        bt_prev=findViewById(R.id.bt_prev);
        bt_next=findViewById(R.id.bt_next);
        bt_finish=findViewById(R.id.bt_finish);

        sliderAdapter = new SliderAdapter(this);

        slideViewPager.setAdapter(sliderAdapter);

        addDots(0);

        slideViewPager.addOnPageChangeListener(viewListenr);


        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(mCurrentPage+1);
            }
        });

        bt_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    slideViewPager.setCurrentItem(mCurrentPage-1);


            }
        });

        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Slide_Hello.this,MainActivity.class);
                startActivity(i);
                finish();


            }
        });
    }

    public void addDots(int position) {

        mDots = new TextView[3];
        mDotsLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotsLayout.addView(mDots[i]);

        }
        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));

        }

    }

    ViewPager.OnPageChangeListener viewListenr = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDots(i);

            mCurrentPage=i;

            if (i==0) {

                bt_next.setEnabled(true);
                bt_prev.setEnabled(false);
                bt_prev.setVisibility(View.INVISIBLE);
                bt_finish.setVisibility(View.INVISIBLE);
                bt_next.setVisibility(View.VISIBLE);

                bt_next.setText("Next");
                bt_prev.setText("");

            }
            else if(i==mDots.length - 1) {

                bt_next.setEnabled(true);
                bt_prev.setEnabled(true);
                bt_prev.setVisibility(View.VISIBLE);
                bt_finish.setVisibility(View.VISIBLE);

                /////
                bt_next.setVisibility(View.INVISIBLE);
                /////

                bt_next.setText("Finish");
                bt_prev.setText("Back");

            }else {

                bt_next.setEnabled(true);
                bt_prev.setEnabled(true);
                bt_prev.setVisibility(View.VISIBLE);
                bt_finish.setVisibility(View.INVISIBLE);
                bt_next.setVisibility(View.VISIBLE);

                bt_next.setText("Next");
                bt_prev.setText("Back");
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
