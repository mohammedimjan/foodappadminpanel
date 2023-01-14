package com.example.admin_app.screen;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.admin_app.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class SingleProduct extends AppCompatActivity {
                TextView viewFoodName;
                TextView viewFoodDescription;
                TextView viewFoodPrice;
                ImageView viewFoodImg;
                String id, oldImageUrl;

                DatabaseReference databaseReference;
;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        viewFoodName = findViewById(R.id.foodname);
        viewFoodDescription = findViewById(R.id.fooddescription);
        viewFoodPrice = findViewById(R.id.foodprice);
        viewFoodImg = findViewById(R.id.foodimg);



        Bundle bundle = getIntent().getExtras();

        if (bundle != null){

                viewFoodName.setText(bundle.getString("FoodName"));
                viewFoodDescription.setText(bundle.getString("FoodDescription"));
                viewFoodPrice.setText(bundle.getString("FoodPrice")+" $");

            Picasso.get()
                    .load(bundle.getString("FoodImage"))
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .fit()
                    .centerCrop()
                    .into(viewFoodImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            System.out.println("Done");
                        }

                        @Override
                        public void onError(Exception e) {

                            System.out.println("Not Done");
                            Picasso.get()
                                    .load(bundle.getString("FoodImage"))
                                    .fit()
                                    .centerCrop()
                                    .into(viewFoodImg);

                        }
                    });


                id = bundle.getString("FoodId");
                oldImageUrl = bundle.getString("FoodImage");



        }

        databaseReference = getInstance().getReference("Foods").child(id);
        databaseReference.keepSynced(true);


    }



}