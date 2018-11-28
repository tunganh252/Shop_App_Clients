package com.example.tunganh.duan1_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.tunganh.duan1_app.General.General;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Profile_User extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    private boolean isChecked = false;
    ImageButton like, back;
    Button bt_changePass, bt_upImage;
    ImageView facebook, instagram, follow;
    TextView tv_name, tv_phone, tv_email, tv_nameUSER;

    /////////// change password

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheam);
        } else
            setTheme(R.style.AppTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__user);

        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_email = findViewById(R.id.tv_email);
        tv_nameUSER = findViewById(R.id.tv_nameUSER);

        tv_name.setText(General.currentUser.getName());
        tv_phone.setText(General.currentUser.getPhone());
        tv_email.setText(General.currentUser.getEmail());
        tv_nameUSER.setText(General.currentUser.getName());


        like = (ImageButton) findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        bt_changePass = (Button) findViewById(R.id.bt_changePass);
        bt_changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePasswordDialog();

            }
        });

        bt_upImage = (Button) findViewById(R.id.bt_upImage);
        bt_upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Profile_User.this, "Upload Image", Toast.LENGTH_SHORT).show();
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

    private void showChangePasswordDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Profile_User.this);
        alertDialog.setTitle("CHANGE PASSWORD");
        alertDialog.setMessage("Please fill all information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_change = inflater.inflate(R.layout.change_password, null);

        final EditText ed_password = layout_change.findViewById(R.id.ed_password);
        final EditText ed_newPassword = layout_change.findViewById(R.id.ed_newPassword);
        final EditText ed_repeatPassword = layout_change.findViewById(R.id.ed_repeatPassword);

        alertDialog.setView(layout_change);

        /// Button
        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                /////// Dùng AlertDialog from android.app vì AlertDialog V7 đụng với library SpotsDialog
                final android.app.AlertDialog waitingDialog = new SpotsDialog(Profile_User.this);
                waitingDialog.show();

                if (ed_password.getText().toString().equals(General.currentUser.getPass()))
                {
                    if (ed_newPassword.getText().toString().equals(ed_repeatPassword.getText().toString()))
                    {
                        Map<String,Object> passwordUpdate=new HashMap<>();
                        passwordUpdate.put("pass",ed_newPassword.getText().toString());

                        DatabaseReference user=FirebaseDatabase.getInstance().getReference("User");
                        user.child(General.currentUser.getName())
                                .updateChildren(passwordUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        waitingDialog.dismiss();
                                        Toast.makeText(Profile_User.this, "Password was update !!!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Profile_User.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else
                    {
                        waitingDialog.dismiss();
                        Toast.makeText(Profile_User.this, "New Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    waitingDialog.dismiss();
                    Toast.makeText(Profile_User.this, "Wrong old password", Toast.LENGTH_SHORT).show();
                }

            }
        });


        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        alertDialog.show();

    }

}



