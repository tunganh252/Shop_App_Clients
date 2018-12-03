package com.example.tunganh.duan1_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tunganh.duan1_app.AdapterView.Menu_Adapter_View;
import com.example.tunganh.duan1_app.Cart.Cart;
import com.example.tunganh.duan1_app.DetailsList.DetailsList;
import com.example.tunganh.duan1_app.Display.ItemClickListener;
import com.example.tunganh.duan1_app.General.General;
import com.example.tunganh.duan1_app.Model.Category;
import com.example.tunganh.duan1_app.Order_Status.Order_Status;
import com.example.tunganh.duan1_app.Service.ListenOrder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;

    TextView tv_Username;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category, Menu_Adapter_View> adapter;


    SwipeRefreshLayout swipeRefreshLayout;

    //////////////////
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent i = new Intent(Home.this, Home_2.class);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    Intent intent = new Intent(Home.this, Profile_User.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        /////////////Bottom Nav//////////////
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ////////////////////////////////////

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Products");
        setSupportActionBar(toolbar);


        ///// Swipe
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (General.isConnectedtoInternet(getBaseContext()))
                    loadMenu();
                else {
                    Toast.makeText(Home.this, "Please check your connection !", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                if (General.isConnectedtoInternet(getBaseContext()))
                    loadMenu();
                else {
                    Toast.makeText(Home.this, "Please check your connection !", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });


        ////// Paper
        Paper.init(this);


        //// Firebase:

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Cart.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /////// Hiển thị tên của User lên (Navigation Drawer)

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        tv_Username = headerView.findViewById(R.id.tv_Username);
        tv_Username.setText(General.currentUser.getName());

        ////// Load Menu
        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setLayoutManager(new GridLayoutManager(this, 2));

        Animation controller = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_animation_fall_down);
        recycler_menu.setAnimation(controller);

//        layoutManager = new LinearLayoutManager(this);
//        recycler_menu.setLayoutManager(layoutManager);


        if (General.isConnectedtoInternet(this))
            loadMenu();
        else {
            Toast.makeText(this, "Check your connection !!!", Toast.LENGTH_SHORT).show();
            return;
        }
////// Register Service
        Intent service = new Intent(Home.this, ListenOrder.class);
        startService(service);

    }


    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, Menu_Adapter_View>(Category.class,
                R.layout.menu_item,
                Menu_Adapter_View.class, category) {
            @Override
            protected void populateViewHolder(Menu_Adapter_View viewHolder, Category model, int position) {
                viewHolder.tvMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView1);
                final Category clickItem = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        /// lấy Category ID ---> truyền sang DetailsList
                        Intent detailsList = new Intent(Home.this, DetailsList.class);
                        /// Trong firebase Category là key ---> get key
                        detailsList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(detailsList);
                    }
                });

            }
        };
        recycler_menu.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

        //// animation
        recycler_menu.getAdapter().notifyDataSetChanged();
        recycler_menu.scheduleLayoutAnimation();
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_homepage) {
            loadMenu();
            return true;
        }
        if (id == R.id.action_cart) {
            Intent i = new Intent(Home.this, Cart.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_logout) {
            /////// Delete Remember user+password
            Paper.book().destroy();

            ///// Logout
            Intent signIn = new Intent(Home.this, SigIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {
            Intent i = new Intent(Home.this, Cart.class);
            startActivity(i);
        } else if (id == R.id.nav_order) {
            Intent i = new Intent(Home.this, Order_Status.class);
            startActivity(i);
        } else if (id == R.id.nav_user) {
            Intent i = new Intent(Home.this, Profile_User.class);
            startActivity(i);
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {

            /////// Delete Remember user+password
            Paper.book().destroy();

            ///// Logout
            Intent signIn = new Intent(Home.this, SigIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
