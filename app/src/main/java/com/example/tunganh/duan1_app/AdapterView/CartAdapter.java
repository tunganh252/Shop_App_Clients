package com.example.tunganh.duan1_app.AdapterView;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.tunganh.duan1_app.Cart.Cart;
import com.example.tunganh.duan1_app.Database.Database;
import com.example.tunganh.duan1_app.Display.ItemClickListener;
import com.example.tunganh.duan1_app.General.General;
import com.example.tunganh.duan1_app.Model.Order;
import com.example.tunganh.duan1_app.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class Cart_Adapter_View extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener {

    public TextView tv_cart_name, tv_cart_price;
    public ElegantNumberButton bt_quantity;

    private ItemClickListener itemClickListener;

    public void setTv_cart_name(TextView tv_cart_name) {
        this.tv_cart_name = tv_cart_name;
    }

    public Cart_Adapter_View(View itemView) {
        super(itemView);
        tv_cart_name = itemView.findViewById(R.id.cart_item_name);
        tv_cart_price = itemView.findViewById(R.id.cart_item_price);
        bt_quantity = itemView.findViewById(R.id.bt_quantity);


        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle(" Select Action ");
        contextMenu.add(0, 0, getAdapterPosition(), General.DELETE);
    }
}


public class CartAdapter extends RecyclerView.Adapter<Cart_Adapter_View> {

    private List<Order> listData = new ArrayList<>();
    private Cart cart;

    public CartAdapter(List<Order> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @Override
    public Cart_Adapter_View onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_item, viewGroup, false);
        return new Cart_Adapter_View(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Cart_Adapter_View holder, final int position) {
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound("" + listData.get(position).getQuantity(), Color.RED);
//        holder.img_cart_count.setImageDrawable(drawable);

        holder.bt_quantity.setNumber(listData.get(position).getQuantity());
        holder.bt_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCart(order);

                //// update total khi thay đổi số lượng number button

                        /// Tính tổng tiển
                int total = 0;
                List<Order> orders=new Database(cart).getCarts();
                for (Order item : orders)
                    total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(item.getQuantity()));
                Locale locale = new Locale("en", "US");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                cart.tv_total.setText(fmt.format(total));
            }
        });

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice())) * (Integer.parseInt(listData.get(position).getQuantity()));
        holder.tv_cart_price.setText(fmt.format(price));

        holder.tv_cart_name.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
