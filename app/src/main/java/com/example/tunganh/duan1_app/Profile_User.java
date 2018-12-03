package com.example.tunganh.duan1_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
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

import com.example.tunganh.duan1_app.AdapterView.User_Adapter_View;
import com.example.tunganh.duan1_app.Cart.Cart;
import com.example.tunganh.duan1_app.Database.Database;
import com.example.tunganh.duan1_app.DetailsList.DetailsList;
import com.example.tunganh.duan1_app.Display.ItemClickListener;
import com.example.tunganh.duan1_app.General.General;
import com.example.tunganh.duan1_app.Model.Details;
import com.example.tunganh.duan1_app.Model.Order;
import com.example.tunganh.duan1_app.Model.Order_Details;
import com.example.tunganh.duan1_app.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class Profile_User extends AppCompatActivity implements  View.OnCreateContextMenuListener{

    android.support.v7.widget.Toolbar toolbar;
    private boolean isChecked = false;
    ImageButton like, back;
    ImageView imageView2,bt_select;
    Button bt_changePass, bt_upImage;
    Button bt_upload, bt_changeImage;
    //    ImageView facebook, instagram, follow;
    TextView tv_name, tv_phone, tv_email, tv_nameUSER;


    FirebaseDatabase database;
    DatabaseReference profile;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseRecyclerAdapter<User, User_Adapter_View> adapter;


    List<User> image_user = new ArrayList<>();

    ////View
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;


    ///// Upload + Edit Image User
    User newUser;
    User item;
    DrawerLayout drawer;
    Uri saveUri;

    String key="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            setTheme(R.style.darktheam);
//        } else
//            setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__user);


        ///// Init Firebase
        database = FirebaseDatabase.getInstance();
        profile = database.getReference("User");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


//        /// Init View
//        recycler_menu = findViewById(R.id.recycler_user);
//        recycler_menu.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recycler_menu.setLayoutManager(layoutManager);

//        loadMenu();


//loadImageUser(imageId);


        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_email = findViewById(R.id.tv_email);
        tv_nameUSER = findViewById(R.id.tv_nameUSER);
        imageView2 = findViewById(R.id.imageView2);


        tv_name.setText(General.currentUser.getName());
        tv_phone.setText(General.currentUser.getPhone());
        tv_email.setText(General.currentUser.getEmail());
        tv_nameUSER.setText(General.currentUser.getName());
//        Picasso.with(getBaseContext()).load(General.currentUser.getImage())
//                .into(imageView2);

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

                showAddImageUserDialog();

            }
        });


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

    }

    private void showAddImageUserDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Profile_User.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu = inflater.inflate(R.layout.add_image_user, null);

        bt_select = add_menu.findViewById(R.id.bt_select);
        bt_upload = add_menu.findViewById(R.id.bt_upload);
        bt_changeImage = add_menu.findViewById(R.id.bt_changeImage);


        //// Event Button
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); /// Lấy image từ Gallary và save Uri img đó
            }
        });

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
//                changeImage();
            }
        });

        alertDialog.setView(add_menu);

        /// Set button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (newUser != null) {
                    profile.push().setValue(newUser);
                    Snackbar.make(drawer, "New category   *" + newUser.getImage().toUpperCase() + "*    was added", Snackbar.LENGTH_LONG)
                            .show();
                }
                profile.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(newUser);
            }
        });
        alertDialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();

            }
        });

        alertDialog.show();


        bt_changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showUpdateDialog();
            }
        });
//
  }



    private void uploadImage() {

        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading....");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("image/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            mDialog.dismiss();
                            Toast.makeText(Profile_User.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // set value for newCategory if Image upload  and we can get download link
//                                    newUser = new (et_name.getText().toString(), uri.toString());

                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(Profile_User.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded " + progress + "%");
                        }
                    });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == General.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            saveUri = data.getData();
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), General.PICK_IMAGE_REQUEST);
    }



        private void changeImage() {
        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading....");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("image/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            mDialog.dismiss();
                            Toast.makeText(Profile_User.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // set value for newCategory if Image upload  and we can get download link
                                    item.setImage(uri.toString());
                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(Profile_User.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded " + progress + "%");
                        }
                    });

        }

    }




    /////////////////////////////// UPDATE IMAGE USER /////////////////////////////////////////

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        if (item.getTitle().equals(General.UPDATE)) {
//            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
//
//        } else if (item.getTitle().equals(General.DELETE)) {
//
//            deleteCategory(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
//
//        }
//
//
//        return super.onContextItemSelected(item);
//
//    }


    private void showUpdateDialog() {
        ///// Copy code showAddDialog and modify

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Profile_User.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu = inflater.inflate(R.layout.add_image_user, null);

        bt_select = add_menu.findViewById(R.id.bt_select);
        bt_upload = add_menu.findViewById(R.id.bt_upload);

        /// Default name
//        et_name.setText(item.getName());


        //// Event Button
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); /// Lấy image từ Gallary và save Uri img đó
            }
        });

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });

        alertDialog.setView(add_menu);


        /// Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                ////// Update information
//                item.setName(et_name.getText().toString());
                profile.child(key).setValue(item);

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();


            }
        });
        alertDialog.show();

    }

    private void deleteCategory(String key, User item) {

        ////////// Call all products in Category
        DatabaseReference proudcts=database.getReference("User");
        Query productInCategory=proudcts.orderByChild("menuId").equalTo(key);
        productInCategory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    postSnapShot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile.child(key).removeValue();
        Toast.makeText(this, "Item Deleted !!!", Toast.LENGTH_SHORT).show();

    }



    ////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////


    private void showChangePasswordDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Profile_User.this);

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

                if (ed_password.getText().toString().equals(General.currentUser.getPass())) {
                    if (ed_newPassword.getText().toString().equals(ed_repeatPassword.getText().toString())) {
                        Map<String, Object> passwordUpdate = new HashMap<>();
                        passwordUpdate.put("pass", ed_newPassword.getText().toString());

                        DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                        user.child(General.currentUser.getUser())
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
                    } else {
                        waitingDialog.dismiss();
                        Toast.makeText(Profile_User.this, "New Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                } else {
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

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<User, User_Adapter_View>(

                User.class,
                R.layout.menu_item,
                User_Adapter_View.class,
                profile

        ) {
            @Override
            protected void populateViewHolder(User_Adapter_View viewHolder, User model, int position) {
                Picasso.with(Profile_User.this).load(model.getImage())
                        .into(viewHolder.iv_imageUser);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        ///***///   Send Category Id and start new Activity
                        Intent i = new Intent(Profile_User.this, Home.class);
                        i.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(i);

                    }
                });
            }
        };

        adapter.notifyDataSetChanged(); /// Refresh data if have data changed
        recycler_menu.setAdapter(adapter);

    }


}



