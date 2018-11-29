package com.example.tunganh.duan1_app.DetailsList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tunganh.duan1_app.AdapterView.Details_Adapter_View;
import com.example.tunganh.duan1_app.Cart.Cart;
import com.example.tunganh.duan1_app.Database.Database;
import com.example.tunganh.duan1_app.Display.ItemClickListener;
import com.example.tunganh.duan1_app.General.General;
import com.example.tunganh.duan1_app.Home;
import com.example.tunganh.duan1_app.Model.Details;
import com.example.tunganh.duan1_app.Order_Status.Order_Status;
import com.example.tunganh.duan1_app.Profile_User;
import com.example.tunganh.duan1_app.R;
import com.example.tunganh.duan1_app.SigIn;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class DetailsList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressWarnings("StatementWithEmptyBody2")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu2) {
            Intent i = new Intent(DetailsList.this, Home.class);
            startActivity(i);
        } else if (id == R.id.nav_cart2) {
            Intent i = new Intent(DetailsList.this, Cart.class);
            startActivity(i);
        } else if (id == R.id.nav_order2) {
            Intent i = new Intent(DetailsList.this, Order_Status.class);
            startActivity(i);
        }else if (id == R.id.nav_user2) {
            Intent i = new Intent(DetailsList.this, Profile_User.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    ////////////////////////// Share Facebook
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    /// Target from library Picasso
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto photo=new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if (ShareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent content=new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    ///////////////////////////////////////


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference details_List;

    String categoryId = "";

    ///// Favorite
    Database localDB;


    FirebaseRecyclerAdapter<Details, Details_Adapter_View> adapter;

    //// function search_bar
    FirebaseRecyclerAdapter<Details, Details_Adapter_View> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.details_list);

        //////// Navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);


        //// Init Facebook
        callbackManager = CallbackManager.Factory.create();
        shareDialog=new ShareDialog(this);



        //Firebase
        database = FirebaseDatabase.getInstance();
        details_List = database.getReference("Details");

        // Local DB
        localDB = new Database(this);

        recyclerView = findViewById(R.id.recycler_details);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //// Get intent i chứa CategoryId

        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null) {
            if (General.isConnectedtoInternet(getBaseContext()))
                loadListDetails(categoryId);
            else {
                Toast.makeText(this, "Check your connection !!!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        ///////////////////////////////////////////////////
        ////////////////// SEARCH BAR /////////////////////
        ///////////////////////////////////////////////////

        materialSearchBar = findViewById(R.id.search_bar);
        materialSearchBar.setHint("Enter for search");

        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ///// Thay đổi suggest list khi user nhập text sản phẩm

                List<String> suggest = new ArrayList<String>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);

                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

                if (!enabled) {
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Details, Details_Adapter_View>(

                Details.class,
                R.layout.details_item,
                Details_Adapter_View.class,
                details_List.orderByChild("name").equalTo(text.toString())


        ) {
            @Override
            protected void populateViewHolder(Details_Adapter_View viewHolder, Details model, int position) {
                viewHolder.details_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.details_image);

                final Details local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //// chuyển activity buy

                        Intent i = new Intent(DetailsList.this, Details_detail.class);
                        i.putExtra("DetailsId", searchAdapter.getRef(position).getKey()); // Send Details id sang activity mới
                        startActivity(i);

                    }

                    ;
                });
            }
        };
        recyclerView.setAdapter(searchAdapter); /////  adpter cho recycler view ---> search result

    }


    private void loadSuggest() {
        details_List.orderByChild("menuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Details item = postSnapshot.getValue(Details.class);
                            suggestList.add(item.getName());  //// Add name of Details(chi tiết sản phẩm) to suggest list

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadListDetails(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Details, Details_Adapter_View>(Details.class,
                R.layout.details_item,
                Details_Adapter_View.class,
                details_List.orderByChild("menuId").equalTo(categoryId) /// Get MenuID trong table Details
        ) {
            @Override
            protected void populateViewHolder(final Details_Adapter_View viewHolder, final Details model, final int position) {
                viewHolder.details_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.details_image);

                //// Add favorite
                if (localDB.isFavorite(adapter.getRef(position).getKey()))
                    viewHolder.fav_image.setImageResource(R.drawable.heart);

                //// Click Share
                viewHolder.bt_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Picasso.with(getApplicationContext())
                                .load(model.getImage())
                                .into(target);
                    }
                });

                //// Click to change favorite (Thả tim.... :)))
                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!localDB.isFavorite(adapter.getRef(position).getKey())) {
                            localDB.addToFavorites(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.heart);
//                            Toast.makeText(DetailsList.this, "" + model.getName() + "Add Favorite !", Toast.LENGTH_SHORT).show();
                        } else {
                            localDB.removeFromFavorites(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_border_heart);
//                            Toast.makeText(DetailsList.this, "" + model.getName() + "Remove Favorite !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                final Details local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //// chuyển activity buy

                        Intent i = new Intent(DetailsList.this, Details_detail.class);
                        i.putExtra("DetailsId", adapter.getRef(position).getKey()); // Send Details id sang activity mới
                        startActivity(i);

                    }
                });
            }
        };
        /// set Adapter
        Log.d("TAG", "" + adapter.getItemCount());
        recyclerView.setAdapter(adapter);
    }




}
