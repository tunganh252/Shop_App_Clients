package com.example.tunganh.duan1_app.DetailsList;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.tunganh.duan1_app.Cart.Cart;
import com.example.tunganh.duan1_app.Database.Database;
import com.example.tunganh.duan1_app.Home;
import com.example.tunganh.duan1_app.Model.Details;
import com.example.tunganh.duan1_app.Model.Order;
import com.example.tunganh.duan1_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Details_detail extends AppCompatActivity {

    TextView details_name, details_price, details_description;
    ImageView details_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton bt_cart;
    ElegantNumberButton bt_number;

    String detailsId = "";

    FirebaseDatabase database;
    DatabaseReference details;

    Details currentDetails;

    Button bt_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_detail);

        //Firebase
        database = FirebaseDatabase.getInstance();
        details = database.getReference("Details");


        bt_number = findViewById(R.id.number_button);
        bt_cart = findViewById(R.id.bt_cart);
        bt_buy=findViewById(R.id.bt_buy);



        bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(

                        detailsId,
                        currentDetails.getName(),
                        bt_number.getNumber(),
                        currentDetails.getPrice(),
                        currentDetails.getDiscount()

                ));

                Toast.makeText(Details_detail.this, "Add to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        bt_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Details_detail.this,Cart.class);
                startActivity(i);

            }
        });

        ///// Xem sản phẩm from table "Details" trong Activity "details_detail"

        details_description = findViewById(R.id.details_description);
        details_image = findViewById(R.id.details_image);
        details_name = findViewById(R.id.details_name);
        details_price = findViewById(R.id.details_price);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        /// Get DetailsId from Itent truyền từ DetailsList

        if (getIntent() != null) {
            detailsId = getIntent().getStringExtra("DetailsId");
            if (!detailsId.isEmpty()) {
                getDetails_detail(detailsId);

            }
        }


    }

    ////// Đổ img, name, price vào layout details_detail.xml
    ////// Khai báo giá trị currentDetails lấy từ model (Details) --> get(name,img,price) đổ vào ánh xạ đã khai báo


    private void getDetails_detail(String detailsId) {

        details.child(detailsId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentDetails =dataSnapshot.getValue(Details.class);

                /// Set Image
                Picasso.with(getBaseContext()).load(currentDetails.getImage())
                        .into(details_image);

                collapsingToolbarLayout.setTitle(currentDetails.getName()
                );

                details_price.setText(currentDetails.getPrice());

                details_name.setText(currentDetails.getName());

                details_description.setText(currentDetails.getDescription());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
