package com.example.admin_app.screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.admin_app.Models.OrderModel;
import com.example.admin_app.R;


public class AdminPanel extends AppCompatActivity {

    Button addFoodBtn,viewFoodBtn, viewOrdersBtn, logOutBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        OrderModel orderModel;

        addFoodBtn = findViewById(R.id.addFood);
        viewFoodBtn = findViewById(R.id.viewFood);
        viewOrdersBtn = findViewById(R.id.viewOrders);
        logOutBtn = findViewById(R.id.logOut);

        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPanel.this, AddFood.class);
                startActivity(intent);
            }
        });

        viewFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPanel.this, ViewFood.class);
                startActivity(intent);
            }
        });

        viewOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPanel.this, ViewOrders.class);
                startActivity(intent);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPanel.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
                Toast.makeText(AdminPanel.this,"Logout Successful",Toast.LENGTH_SHORT).show();

            }
        });
    }
}