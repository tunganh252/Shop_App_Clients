package com.example.tunganh.duan1_app.AdapterView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tunganh.duan1_app.Display.ItemClickListener;
import com.example.tunganh.duan1_app.R;

public class Menu_Adapter_View extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvMenuName;
    public ImageView imageView1;

    private ItemClickListener itemClickListener;

    public Menu_Adapter_View(@NonNull View itemView) {
        super(itemView);

        tvMenuName=itemView.findViewById(R.id.menu_name);
        imageView1=itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}