package com.example.tunganh.duan1_app.AdapterView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tunganh.duan1_app.Display.ItemClickListener;
import com.example.tunganh.duan1_app.R;

public class Details_Adapter_View extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView details_name;
    public ImageView details_image,fav_image,bt_share;

    private ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public Details_Adapter_View(@NonNull View itemView) {
        super(itemView);

        details_name=itemView.findViewById(R.id.details_name);
        details_image=itemView.findViewById(R.id.details_image);
        fav_image=itemView.findViewById(R.id.favo);
        bt_share=itemView.findViewById(R.id.bt_share);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
