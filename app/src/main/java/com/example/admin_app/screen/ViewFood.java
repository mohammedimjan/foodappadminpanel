package com.example.admin_app.screen;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_app.Adapters.AdapterFood;
import com.example.admin_app.Models.FoodModel;
import com.example.admin_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewFood extends AppCompatActivity {


    private RecyclerView foodRv;
    private AdapterFood adapterFood;
    private DatabaseReference reference;
    private ArrayList<FoodModel> foodList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        foodRv = (RecyclerView) findViewById(R.id.foodrv);
        foodRv.setHasFixedSize(true);
        foodRv.setLayoutManager(new LinearLayoutManager(this));

        foodList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Foods");
        reference.keepSynced(true);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    FoodModel foodModel = postSnapshot.getValue(FoodModel.class);
                    foodList.add(foodModel);
                }
                adapterFood = new AdapterFood(ViewFood.this,foodList);
                foodRv.setAdapter(adapterFood);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFood.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

}