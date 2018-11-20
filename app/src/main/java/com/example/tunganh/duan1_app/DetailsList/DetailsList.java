package com.example.tunganh.duan1_app.DetailsList;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tunganh.duan1_app.AdapterView.Details_Adapter_View;
import com.example.tunganh.duan1_app.Display.ItemClickListener;
import com.example.tunganh.duan1_app.Model.Details;
import com.example.tunganh.duan1_app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class DetailsList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference details_List;

    String categoryId = "";

    FirebaseRecyclerAdapter<Details, Details_Adapter_View> adapter;

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

                        Intent i = new Intent(DetailsList.this,Details_detail.class);
                        i.putExtra("DetailsId",adapter.getRef(position).getKey()); // Send Details id sang activity mới
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
