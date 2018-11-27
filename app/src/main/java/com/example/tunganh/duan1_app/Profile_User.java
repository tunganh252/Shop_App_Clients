package com.example.tunganh.duan1_app;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

public class Profile_User extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    private boolean isChecked = false;
    ImageButton like, back;
    Button poke;
    ImageView facebook, instagram, follow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheam);
        } else
            setTheme(R.style.AppTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__user);

        like = (ImageButton) findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Profile_User.this, "You Like me", Toast.LENGTH_SHORT).show();
            }
        });

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile_User.this, Home.class);
                startActivity(i);
                finish();
            }
        });

        poke = (Button) findViewById(R.id.button);
        poke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        facebook = (ImageView) findViewById(R.id.imageView4);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        instagram = (ImageView) findViewById(R.id.imageView7);
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        follow = (ImageView) findViewById(R.id.imageView6);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

    }

}

























//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem checkable = menu.findItem(R.id.theam);
//        checkable.setChecked(isChecked);
//        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
//            checkable.setChecked(true);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.profile_user, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id){
//            case R.id.theam:
//                isChecked = !item.isChecked();
//                item.setChecked(isChecked);
//                if(isChecked){
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    Toast.makeText(Profile_User.this,"Dark Mode On",Toast.LENGTH_SHORT).show();
//
//                }else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    Toast.makeText(Profile_User.this,"Dark Mode Off",Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            default:
//                return true;
//        }
//    }
//
//    private void restartApp() {
//        Intent i = new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(i);
//        finish();
//    }
