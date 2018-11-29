package com.example.tunganh.duan1_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.tunganh.duan1_app.General.General;
import com.example.tunganh.duan1_app.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {
    Button bt_dangnhap, bt_dangki;
    TextView tv_slogan;

    CircularProgressButton aaa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.tunganh.duan1_app",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        setContentView(R.layout.activity_main);



            bt_dangnhap = findViewById(R.id.bt_dangnhap);
            bt_dangki = findViewById(R.id.bt_dangki);




            /// Init paper
            Paper.init(this);


            bt_dangnhap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, SigIn.class);
                    startActivity(i);

                }
            });
            bt_dangki.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, Register.class);
                    startActivity(i);
                }
            });


            ///////////// Check Remember
            String userRe = Paper.book().read(General.USER_KEY);
            String passRe = Paper.book().read(General.PASS_KEY);
            if (userRe != null && passRe != null) {
                {
                    if (!userRe.isEmpty() && !passRe.isEmpty())
                        login(userRe, passRe);
                }
            }


        }


        private void login ( final String userRe, final String passRe){
            if (General.isConnectedtoInternet(getBaseContext())) {

                /// Firebase
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference table_user = database.getReference("User");


                final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // Check User, if not user ---> exit database
                        if (dataSnapshot.child(userRe).exists()) {
                            // Lay thong tin User

                            mDialog.dismiss();
                            User user = dataSnapshot.child(userRe).getValue(User.class);
                            if (user.getPass().equals(passRe)) {
                                Toast.makeText(MainActivity.this, "Log in successfull !!!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this, Home_2.class);
                                General.currentUser = user;
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "User is not register !!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {
                Toast.makeText(MainActivity.this, "Check your connection !!!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        /////// Hàm back 2 lần để thoát activity
        boolean doubleBackToExitPressedOnce = false;

        @Override
        public void onBackPressed () {
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
    }
