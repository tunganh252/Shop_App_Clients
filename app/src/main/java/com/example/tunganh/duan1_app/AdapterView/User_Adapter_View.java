package com.example.tunganh.duan1_app.AdapterView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tunganh.duan1_app.Display.ItemClickListener;
import com.example.tunganh.duan1_app.General.General;
import com.example.tunganh.duan1_app.R;

public class User_Adapter_View extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener

{

    public ImageView iv_imageUser;

    private ItemClickListener itemClickListener;

    public User_Adapter_View(@NonNull View itemView) {
        super(itemView);

        iv_imageUser=itemView.findViewById(R.id.imageView2);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select Action");

        contextMenu.add(0,0,getAdapterPosition(),General.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(),General.DELETE);
    }
}
