package com.example.admin_app.Adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_app.Models.FoodModel;
import com.example.admin_app.R;
import com.example.admin_app.screen.SingleProduct;
import com.example.admin_app.screen.ViewFood;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterFood extends RecyclerView.Adapter<AdapterFood.HolderFood> {


    private Context context;
    public ArrayList<FoodModel> foodList;
    Uri uri;
    String imageUrl;
    String oldImageUrl;

    public AdapterFood(Context context, ArrayList<FoodModel> foodList) {
        this.context = context;
        this.foodList = foodList;
    }


    @NonNull
    @Override
    public HolderFood onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.all_products,parent,false);
        return new HolderFood(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HolderFood holder, @SuppressLint("RecyclerView") int position) {
        FoodModel foodModel = foodList.get(position);

        
        holder.foodId.setText(foodModel.getFoodId());
        holder.foodName.setText(foodModel.getFoodName());
        holder.foodPrice.setText("$ "+foodModel.getFoodPrice());
        Picasso.get()
                .load(foodModel.getFoodImage())
                .fit()
                .centerCrop()
                .into(holder.foodImage);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.foodName.getContext());
                builder.setTitle("Are you sure you want to delete?");
                builder.setMessage("Deleted data can't be undone.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference("Foods").child(foodModel.getFoodId())
                                .removeValue();
                        Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(context,UpdateFood.class)
//                        .putExtra("FoodName",foodModel.getFoodName())
//                        .putExtra("FoodDescription",foodModel.getFoodDescription())
//                        .putExtra("FoodPrice",foodModel.getFoodPrice())
//                        .putExtra("FoodImage",foodModel.getFoodImage())
//                        .putExtra("FoodId",foodModel.getFoodId());
//
//                context.startActivity(intent);

                DialogPlus dialogPlus = DialogPlus.newDialog(holder.foodImage.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1800)
                        .create();

                dialogPlus.show();
                View view = dialogPlus.getHolderView();

                EditText name = view.findViewById(R.id.foodname);
                EditText description = view.findViewById(R.id.fooddescription);
                EditText price = view.findViewById(R.id.foodprice);
                ImageView image = view.findViewById(R.id.foodimg);

                Button btnUpdate = view.findViewById(R.id.updateBtn);

                name.setText(foodModel.getFoodName());
                description.setText(foodModel.getFoodDescription());
                price.setText(foodModel.getFoodPrice());

                Picasso.get().load(foodModel.getFoodImage()).networkPolicy(NetworkPolicy.OFFLINE).into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(foodModel.getFoodImage()).into(image);
                    }
                });
                oldImageUrl = foodModel.getFoodImage();

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        context.startActivity(intent);
                    }
                });

                btnUpdate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String fName = name.getText().toString();
                        String fDesc = description.getText().toString();
                        String fPrice = price.getText().toString();

                        if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(fDesc) || TextUtils.isEmpty(fPrice) ){
                            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                        }

                        else if (fName != null && fDesc != null && fPrice != null ){

                            Map<String, Object> food = new HashMap<>();
                            food.put("FoodId", foodModel.getFoodId());
                            food.put("FoodName", name.getText().toString());
                            food.put("FoodDescription", description.getText().toString());
                            food.put("FoodPrice", price.getText().toString());
                            food.put("FoodImage", foodModel.getFoodImage());

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Foods");
                            databaseReference.keepSynced(true);

                            databaseReference
                                    .child(foodModel.getFoodId()).updateChildren(food)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(context, "Food Updated", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, ViewFood.class);
                                            context.startActivity(intent);
                                            ((Activity)context).finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleProduct.class)
                        .putExtra("FoodName",foodModel.getFoodName())
                        .putExtra("FoodDescription",foodModel.getFoodDescription())
                        .putExtra("FoodPrice",foodModel.getFoodPrice())
                        .putExtra("FoodImage",foodModel.getFoodImage())
                        .putExtra("FoodId",foodModel.getFoodId());

                context.startActivity(intent);
            }
        });

    }




    @Override
    public int getItemCount() {
        return foodList.size();
    }

    class HolderFood extends RecyclerView.ViewHolder{

        ImageView foodImage;
        TextView foodName, foodPrice;
        TextView foodId;
        Button editBtn;
        ImageButton deleteBtn;

        public HolderFood(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.productimg);
            foodName = itemView.findViewById(R.id.productname);
            foodPrice = itemView.findViewById(R.id.productprice);
            foodId = itemView.findViewById(R.id.foodid);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            editBtn = itemView.findViewById(R.id.editBtn);


        }

    }



}
