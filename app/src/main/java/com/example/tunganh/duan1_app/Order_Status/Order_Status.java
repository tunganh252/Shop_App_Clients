package com.example.tunganh.duan1_app.Order_Status;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tunganh.duan1_app.AdapterView.Order_Adapter_View;
import com.example.tunganh.duan1_app.General.General;
import com.example.tunganh.duan1_app.Model.Order_Details;
import com.example.tunganh.duan1_app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Order_Status extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    FirebaseRecyclerAdapter<Order_Details, Order_Adapter_View> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order__status);

        ///// Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Order_Details");

        recyclerView = findViewById(R.id.list_order);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(General.currentUser.getName());
    }

    private void loadOrders(String name) {
        adapter = new FirebaseRecyclerAdapter<Order_Details, Order_Adapter_View>(
                Order_Details.class,
                R.layout.order_item,
                Order_Adapter_View.class,
                requests.orderByChild("name")
                        .equalTo(name)

        ) {
            @Override
            protected void populateViewHolder(Order_Adapter_View viewHolder, Order_Details model, int position) {

                viewHolder.tv_order_id.setText(adapter.getRef(position).getKey());
                viewHolder.tv_order_status.setText(convertCodeToStatus(model.getStatus()));
                viewHolder.tv_order_name.setText(model.getName());
                viewHolder.tv_order_phone.setText(model.getPhone());
                viewHolder.tv_order_address.setText(model.getAddress());

            }
        };

        recyclerView.setAdapter(adapter);
    }

    private String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "On my way";
        else
        return "Shipped";
    }
}
