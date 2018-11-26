package com.example.tunganh.duan1_app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;

public class SigIn extends AppCompatActivity {
//MaterialEditText et_user,et_pass;
//Button bt_dangki,bt_dangnhap;

    ImageView bt_dangnhap;
    EditText et_user, et_pass;
    TextView tv_forgot, tv_dangki;
    CheckBox checkRemember;

    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sig_in);
        et_user = findViewById(R.id.et_user);
        et_pass = findViewById(R.id.et_pass);
        bt_dangnhap = findViewById(R.id.bt_dangnhap);
        checkRemember = findViewById(R.id.checkRemember);
        tv_forgot = findViewById(R.id.tv_forgot);
        tv_dangki = findViewById(R.id.tv_dangki);


        ///// Init paper
        Paper.init(this);


        /// Firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        bt_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (General.isConnectedtoInternet(getBaseContext())) {

                    ////// Save user + password ---> Remember me

                    if (checkRemember.isChecked()) {
                        Paper.book().write(General.USER_KEY, et_user.getText().toString());
                        Paper.book().write(General.PASS_KEY, et_pass.getText().toString());
                    }


                    final ProgressDialog mDialog = new ProgressDialog(SigIn.this);
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            // Check User, if not user ---> exit database
                            if (dataSnapshot.child(et_user.getText().toString()).exists()) {
                                // Lay thong tin User

                                mDialog.dismiss();
                                User user = dataSnapshot.child(et_user.getText().toString()).getValue(User.class);
                                if (user.getPass().equals(et_pass.getText().toString())) {
                                    Toast.makeText(SigIn.this, "Log in successfull !!!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SigIn.this, Home_2.class);
                                    General.currentUser = user;
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(SigIn.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SigIn.this, "User is not register !!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(SigIn.this, "Check your connection !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotDialog();
            }
        });

        tv_dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SigIn.this, Register.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void showForgotDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        builder.setMessage("Enter your phone & email");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgot_view = inflater.inflate(R.layout.forgot_password, null);

        builder.setView(forgot_view);
        builder.setIcon(R.drawable.ic_security);

        final EditText et_User_forgot = (EditText) forgot_view.findViewById(R.id.et_User_forgot);
        final EditText et_Email_forgot = (EditText) forgot_view.findViewById(R.id.et_Email_forgot);
        final EditText et_Phone_forgot = (EditText) forgot_view.findViewById(R.id.et_Phone_forgot);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(et_User_forgot.getText().toString()).exists()) {
                            // Lay thong tin User
                            User user = dataSnapshot.child(et_User_forgot.getText().toString()).getValue(User.class);

                            if (user.getPhone().equals(et_Phone_forgot.getText().toString())) {

                                if (user.getEmail().equals(et_Email_forgot.getText().toString())) {
                                    Toast.makeText(SigIn.this, "Your Password: " + user.getPass(), Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(SigIn.this, "Wrong Email !!!", Toast.LENGTH_SHORT).show();
                            }else
                                Toast.makeText(SigIn.this, "Wrong Phone number !!!", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(SigIn.this, "Wrong Username !!!", Toast.LENGTH_SHORT).show();


//                        User user = dataSnapshot.child(et_Phone_forgot.getText().toString())
//                                .getValue(User.class);
//
//                        Toast.makeText(SigIn.this, "con cặc", Toast.LENGTH_SHORT).show();
//                        if (user.getEmail().equals(et_Email_forgot.getText().toString()))
//                            Toast.makeText(SigIn.this, "Your password: " + user.getPass(), Toast.LENGTH_LONG).show();
//                        else
//                            Toast.makeText(SigIn.this, "Invalid information !!!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        builder.show();


    }
}
