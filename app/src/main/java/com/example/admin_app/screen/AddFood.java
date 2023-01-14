package com.example.admin_app.screen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.admin_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AddFood extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText foodName,foodDescription,foodPrice;
    Button addFood, selectImgBtn;
    ImageView foodImage;
    ProgressDialog progressDialog;

    DatabaseReference databaseReference;


    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //databaseReference= FirebaseDatabase.getInstance().getReference("Foods").push();
        databaseReference= FirebaseDatabase.getInstance().getReference("Foods");



        addFood = findViewById(R.id.addproduct);
        foodImage = findViewById(R.id.foodimg);
        foodName = findViewById(R.id.productName);
        foodDescription = findViewById(R.id.productDesc);
        foodPrice = findViewById(R.id.productPrice);
        selectImgBtn = findViewById(R.id.selectimg);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);


        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFoodData();

            }
        });



        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChoose();
            }
        });
        foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChoose();
            }
        });

    }

    private String fName, fDescription, fPrice;
    private void addFoodData() {

        fName = foodName.getText().toString().trim();
        fDescription = foodDescription.getText().toString().trim();
        fPrice = foodPrice.getText().toString().trim();



        if (TextUtils.isEmpty(fName)) {
            foodName.setError("Name cannot be empty");
        }
        if (TextUtils.isEmpty(fDescription)) {
            foodDescription.setError("Description cannot be empty");
        }
        if (TextUtils.isEmpty(fPrice)) {
            foodPrice.setError("Price cannot be empty");
        } else if (imageUri == null) {
            Toast.makeText(AddFood.this, "Image Cannot be empty!", Toast.LENGTH_SHORT).show();
        }
        else{
            addFoodDb();
        }


    }

    private void addFoodDb() {
        progressDialog.setMessage("Adding Food.....");
        progressDialog.show();

        String timeStamp = ""+System.currentTimeMillis();
        if (imageUri != null){

            String filePathName = imageUri.getLastPathSegment();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Foods").child(filePathName);
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();

                            if(uriTask.isSuccessful()){
                                HashMap<String,Object> food = new HashMap<>();
                                food.put("FoodId",""+timeStamp);
                                food.put("FoodName",""+fName);
                                food.put("FoodDescription",""+fDescription);
                                food.put("FoodPrice",""+fPrice);
                                food.put("FoodImage",""+downloadImageUri);

                                //databaseReference.setValue(food);
                                databaseReference.child(timeStamp).setValue(food)

                                //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Foods").child(timeStamp).setValue(food);
                                //databaseReference.child(firebaseAuth.getUid()).child("Foods").child(timeStamp).setValue(food)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(AddFood.this,"Food Added Successfully",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(AddFood.this, AdminPanel.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(AddFood.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddFood.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }


    private void openImageChoose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(foodImage);

        }
    }



}