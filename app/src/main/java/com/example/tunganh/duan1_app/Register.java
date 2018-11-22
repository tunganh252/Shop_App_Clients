package com.example.tunganh.duan1_app;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.tunganh.duan1_app.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Register extends AppCompatActivity {
    MaterialEditText et_user, et_fullname,et_pass, et_phone,et_email;
    Button bt_dangki;

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

                final ProgressDialog mDialog = new ProgressDialog(Register.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Check add them username vao user
                        if (dataSnapshot.child(et_user.getText().toString()).exists()) {
                            mDialog.dismiss();
                            Toast.makeText(Register.this, "Username already register !!!", Toast.LENGTH_SHORT).show();
                        } else {
                            mDialog.dismiss();
                            User user = new User(et_pass.getText().toString(), et_fullname.getText().toString(),et_phone.getText().toString(),et_email.getText().toString());
                            table_user.child(et_user.getText().toString()).setValue(user);
                            Toast.makeText(Register.this, "Register successfully !!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }
}
