package com.example.tunganh.duan1_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    //    MaterialEditText et_user, et_fullname,et_pass, et_phone,et_email;
    EditText et_user, et_fullname, et_pass, et_phone, et_email;
    Button bt_dangki;


    String validEmail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
            + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"
            + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
    String validPass = "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}";
    String validName = "^[\\p{L} .'-]+$";
    String validPhone = "(09|01[2|6|8|9])+([0-9]{8})\\b";
    String validUser = "[a-zA-Z0-9][a-zA-Z0-9\\-]{3,50}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_user = findViewById(R.id.et_user);
        et_pass = findViewById(R.id.et_pass);
        et_fullname = findViewById(R.id.et_fullname);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        bt_dangki = findViewById(R.id.bt_dangki);
        /// Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        bt_dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (General.isConnectedtoInternet(getBaseContext())) {

//                    final ProgressDialog mDialog = new ProgressDialog(Register.this);
//                    mDialog.setMessage("Please waiting...");
//                    mDialog.show();

                    final ProgressDialog mDialog = new ProgressDialog(Register.this);
                    mDialog.setTitle("Connecting");
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();

                    Runnable progressRunnable = new Runnable() {

                        @Override
                        public void run() {
                            mDialog.cancel();
                        }
                    };

                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 3000);

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//// Validate email
                            String email = et_email.getText().toString();
                            Matcher matcherEmail = Pattern.compile(validEmail).matcher(email);
//// Validate pass
                            String pass = et_pass.getText().toString();
                            Matcher matcherPass = Pattern.compile(validPass).matcher(pass);
//// Validate Name
                            String name = et_fullname.getText().toString();
                            Matcher matcherName = Pattern.compile(validName).matcher(name);
//// Validate Phone
                            String phone = et_phone.getText().toString();
                            Matcher matcherPhone = Pattern.compile(validPhone).matcher(phone);
//// Validate User
                            String user1 = et_user.getText().toString();
                            Matcher matcherUser = Pattern.compile(validUser).matcher(user1);

                            if (matcherUser.matches()) {
                                if (matcherPass.matches()) {
                                    if (matcherName.matches()) {
                                        if (matcherPhone.matches()) {
                                            if (matcherEmail.matches()) {
///Check add new account
                                                if (dataSnapshot.child(et_user.getText().toString()).exists()) {
                                                    mDialog.dismiss();
                                                    Toast.makeText(Register.this, "Username already register !!!", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    mDialog.dismiss();
                                                    User user = new User(
                                                            et_user.getText().toString(),
                                                            et_pass.getText().toString(),
                                                            et_fullname.getText().toString(),
                                                            et_phone.getText().toString(),
                                                            et_email.getText().toString());
                                                    table_user.child(et_user.getText().toString()).setValue(user);
                                                    Toast.makeText(Register.this, "Register successfully !!!", Toast.LENGTH_SHORT).show();
                                                    finish();

                                                    Intent i = new Intent(Register.this, SigIn.class);
                                                    startActivity(i);
                                                }

                                            } else {
                                                Toast.makeText(Register.this, "Invalid Email !!!", Toast.LENGTH_SHORT).show();
                                            }

                                        } else
                                            Toast.makeText(Register.this, "Invalid Phone number !!!.", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(Register.this, "Invalid Your Name !!!", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(Register.this, "Invalid Password !!!", Toast.LENGTH_SHORT).show();

                            } else
                                Toast.makeText(Register.this, "Invalid Username !!!", Toast.LENGTH_SHORT).show();


                            // Check add them username vao user
//                            if (dataSnapshot.child(et_user.getText().toString()).exists()) {
//                                mDialog.dismiss();
//                                Toast.makeText(Register.this, "Username already register !!!", Toast.LENGTH_SHORT).show();
//
//                            } else {
//                                mDialog.dismiss();
//                                User user = new User(et_pass.getText().toString(),
//                                        et_fullname.getText().toString(),
//                                        et_phone.getText().toString(),
//                                        et_email.getText().toString());
//                                table_user.child(et_user.getText().toString()).setValue(user);
//                                Toast.makeText(Register.this, "Register successfully !!!", Toast.LENGTH_SHORT).show();
//                                finish();
//
//                                Intent i = new Intent(Register.this, SigIn.class);
//                                startActivity(i);
//                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(Register.this, "Check your connection !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });


    }

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
//                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=.*[a-zA-Z0-9])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");
}





