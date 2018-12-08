package com.example.tunganh.duan1_app.DetailsList;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.tunganh.duan1_app.Cart.Cart;
import com.example.tunganh.duan1_app.Database.Database;
import com.example.tunganh.duan1_app.General.General;
import com.example.tunganh.duan1_app.Home;
import com.example.tunganh.duan1_app.Model.Details;
import com.example.tunganh.duan1_app.Model.Order;
import com.example.tunganh.duan1_app.Model.Rating;
import com.example.tunganh.duan1_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class Details_detail extends AppCompatActivity implements RatingDialogListener {

    TextView details_name, details_price, details_description, tv_nameRating;
    ImageView details_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton bt_cart;

    ElegantNumberButton bt_number;

    String detailsId = "";

    FirebaseDatabase database;
    DatabaseReference details;
    DatabaseReference ratingTbl;

    Details currentDetails;

    Button bt_buy;
    LinearLayout bt_rating;
    RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_detail);


        //Firebase
        database = FirebaseDatabase.getInstance();
        details = database.getReference("Details");
        ratingTbl = database.getReference("Rating");


        bt_number = findViewById(R.id.number_button);
        bt_cart = findViewById(R.id.bt_cart);
        bt_buy = findViewById(R.id.bt_buy);
        ratingBar = findViewById(R.id.ratingBar);
        bt_rating = findViewById(R.id.bt_rating);

        bt_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRating();
            }
        });


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

                Toast.makeText(Details_detail.this, "Added to Cart !!!", Toast.LENGTH_LONG).show();
            }
        });

        bt_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Details_detail.this, Cart.class);
                startActivity(i);

            }
        });

        ///// Xem sản phẩm from table "Details" trong Activity "details_detail"

        details_description = findViewById(R.id.details_description);
        details_image = findViewById(R.id.details_image);
        details_name = findViewById(R.id.details_name);
        details_price = findViewById(R.id.details_price);

        tv_nameRating = findViewById(R.id.tv_nameRating);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        /// Get DetailsId from Itent truyền từ DetailsList

        if (getIntent() != null) {
            detailsId = getIntent().getStringExtra("DetailsId");
            if (!detailsId.isEmpty()) {
                if (General.isConnectedtoInternet(getBaseContext())) {

                    getDetails_detail(detailsId);
                    getRating_detail(detailsId);

                } else {
                    Toast.makeText(this, "Check your connection !!!", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    private void getRating_detail(String detailsId) {

        Query detailRating = ratingTbl.orderByChild("detailsId").equalTo(detailsId);

        detailRating.addValueEventListener(new ValueEventListener() {
            int count = 0, sum = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Rating item = postSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }
                if (count != 0) {
                    float average = sum / count;
                    ratingBar.setRating(average);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDialogRating() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite Ok", "Very Good", "Excelent"))
                .setTitle("Rate this product")
                .setDefaultRating(1)
                .setDescription("Give your feeback and rating star !")
                .setTitleTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here...")
                .setHintTextColor(R.color.colorTransparentWhite)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setCommentTextColor(R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimary)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(Details_detail.this)
                .show();
    }

    ////// Đổ img, name, price vào layout details_detail.xml
    ////// Khai báo giá trị currentDetails lấy từ model (Details) --> get(name,img,price) đổ vào ánh xạ đã khai báo


    private void getDetails_detail(String detailsId) {

        details.child(detailsId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentDetails = dataSnapshot.getValue(Details.class);

                /// Set Image
                Picasso.with(getBaseContext()).load(currentDetails.getImage())
                        .into(details_image);

                collapsingToolbarLayout.setTitle(currentDetails.getName()
                );

                details_price.setText(currentDetails.getPrice());

                details_name.setText(currentDetails.getName());

                details_description.setText(currentDetails.getDescription());

                tv_nameRating.setText(General.currentUser.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, String comments) {

        //// Get Rating and upload firebase
        final Rating rating = new Rating(
                General.currentUser.getName(),
                detailsId,
                String.valueOf(value),
                comments,
                currentDetails.getName()
        );
        ratingTbl.child(General.currentUser.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(General.currentUser.getName()).exists()) {

                    ////// Remove old value
                    ratingTbl.child(General.currentUser.getName()).removeValue();

                    ///// Update new value
                    ratingTbl.child(General.currentUser.getName()).setValue(rating);
                    ratingTbl.child(currentDetails.getName());

                } else {

                    ////// Update new value
                    ratingTbl.child(General.currentUser.getName()).setValue(rating);
                }

                Toast.makeText(Details_detail.this, "Thanks for your feedback !!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
