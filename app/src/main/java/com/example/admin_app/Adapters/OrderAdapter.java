package com.example.admin_app.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_app.Models.OrderModel;
import com.example.admin_app.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    Context context;
    ArrayList<OrderModel> orderList;


    public OrderAdapter(Context context, ArrayList<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_orders,parent,false);
        return new MyViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderModel orderModel = orderList.get(position);

        holder.foodName.setText(orderModel.getFoodName());
        holder.foodPrice.setText(orderModel.getFoodPrice()+" $");
        holder.orderId.setText(orderModel.getOrderId());
        holder.customerAddress.setText(orderModel.getCustomerAddress());
        holder.customerName.setText(orderModel.getCustomerName());


        Picasso.get()
                .load(orderModel.getFoodImage())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .centerCrop()
                .into(holder.foodImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(orderModel.getFoodImage())
                                .fit()
                                .centerCrop()
                                .into(holder.foodImage);
                    }
                });




        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.foodName.getContext());
                builder.setTitle("Are you sure you want to delete?");
                builder.setMessage("Deleted data can't be undone.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference("Order").child(orderModel.getOrderId())
                                .removeValue();
                        Toast.makeText(context, "Order Deleted Successfully", Toast.LENGTH_SHORT).show();
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

        holder.completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.foodName.getContext());
                builder.setTitle("Are you sure you want to Complete the order?");
                builder.setMessage("Click \"Complete\" Complete the order.");
                builder.setPositiveButton("Complete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference("Order").child(orderModel.getOrderId())
                                .removeValue();
                        Toast.makeText(context, "Order Completed Successfully", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView foodImage;
        TextView customerName,customerAddress, foodName, foodPrice, orderId;
        Button completeBtn;
        ImageButton deleteBtn;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.orderfoodimg);
            customerName = itemView.findViewById(R.id.ordercustomername);
            customerAddress = itemView.findViewById(R.id.ordercustomerAddress);
            foodName = itemView.findViewById(R.id.orderfoodname);
            foodPrice = itemView.findViewById(R.id.orderfoodprice);
            orderId = itemView.findViewById(R.id.orderid);
            completeBtn = itemView.findViewById(R.id.completeBtn);
            deleteBtn = itemView.findViewById(R.id.orderdeleteBtn);
        }
    }
}
