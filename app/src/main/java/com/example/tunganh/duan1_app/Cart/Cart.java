package com.example.tunganh.duan1_app.Cart;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tunganh.duan1_app.AdapterView.CartAdapter;
import com.example.tunganh.duan1_app.Database.Database;
import com.example.tunganh.duan1_app.General.General;
import com.example.tunganh.duan1_app.Model.Order;
import com.example.tunganh.duan1_app.Model.Order_Details;
import com.example.tunganh.duan1_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView tv_total;
    Button bt_place;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        // Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Order_Details");

///Truyền list danh sách mua hàng
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        tv_total = findViewById(R.id.tv_total);
        bt_place = findViewById(R.id.bt_PlaceOrder);

        bt_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.size() > 0)
                    showAlertDialog();
                else
                    Toast.makeText(Cart.this, "Your cart is empty !!!", Toast.LENGTH_SHORT).show();
            }
        });

        loadListDetail();

    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step !!!");
        alertDialog.setMessage("Enter your address: ");

        final EditText et_address = new EditText(Cart.this);
//        final EditText et_phone = new EditText(Cart.this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        et_address.setLayoutParams(lp);
        alertDialog.setView(et_address);
        alertDialog.setIcon(R.drawable.ic_mall);

//        et_phone.setLayoutParams(lp);
//        alertDialog.setView(et_phone);
//        alertDialog.setIcon(R.drawable.ic_phone);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Order_Details request = new Order_Details(
                        General.currentUser.getName(),
                        General.currentUser.getPhone(),
                        General.currentUser.getEmail(),
                        et_address.getText().toString(),
                        tv_total.getText().toString(),
                        cart
                );
                ///// Thêm chi tiết mua hàng vào Firebase ///// Dùng System.CurrentMilli to key

                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                ///// Xóa Cart
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thanks for your order! \n      Have a nice day.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();

            }
        });

        alertDialog.show();
    }

    private void loadListDetail() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


        /// Tính tổng tiển
        int total = 0;
        for (Order order : cart)
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        tv_total.setText(fmt.format(total));


    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(General.DELETE))
            deleteCart(item.getOrder());
        return true;
    }


    private void deleteCart(int position) {

        cart.remove(position);
        new Database(this).cleanCart();
        for (Order item : cart)
            new Database(this).addToCart(item);
        loadListDetail();

    }
}
