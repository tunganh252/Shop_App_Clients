package com.example.tunganh.duan1_app.AdapterView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.tunganh.duan1_app.Display.ItemClickListener;
import com.example.tunganh.duan1_app.R;

import org.w3c.dom.Text;

public class Order_Adapter_View extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tv_order_id, tv_order_status,tv_order_name, tv_order_phone, tv_order_address;

    private ItemClickListener itemClickListener;

    public Order_Adapter_View(View itemView) {
        super(itemView);

        tv_order_id = itemView.findViewById(R.id.order_id);
        tv_order_status = itemView.findViewById(R.id.order_status);
        tv_order_name = itemView.findViewById(R.id.order_name);
        tv_order_phone = itemView.findViewById(R.id.order_phone);
        tv_order_address = itemView.findViewById(R.id.order_address);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
