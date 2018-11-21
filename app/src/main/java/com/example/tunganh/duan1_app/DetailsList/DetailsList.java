package com.example.tunganh.duan1_app.DetailsList;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tunganh.duan1_app.AdapterView.Details_Adapter_View;
import com.example.tunganh.duan1_app.Display.ItemClickListener;
import com.example.tunganh.duan1_app.Model.Details;
import com.example.tunganh.duan1_app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailsList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference details_List;

    String categoryId = "";

    FirebaseRecyclerAdapter<Details, Details_Adapter_View> adapter;

    //// function search_bar
    FirebaseRecyclerAdapter<Details, Details_Adapter_View> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        details_List = database.getReference("Details");

        recyclerView = findViewById(R.id.recycler_details);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //// Get intent i chứa CategoryId

        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null) {
            loadListDetails(categoryId);
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

                if (!enabled){
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
                details_List.orderByChild("Name").equalTo(text.toString())


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

                    };
                });
            }
        };
        recyclerView.setAdapter(searchAdapter); /////  adpter cho recycler view ---> search result

    }




    private void loadSuggest() {
        details_List.orderByChild("MenuId").equalTo(categoryId)
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
                details_List.orderByChild("MenuId").equalTo(categoryId) /// Get MenuID trong table Details
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
