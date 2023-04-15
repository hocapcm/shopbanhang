package com.example.shopbanhang.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopbanhang.R;
import com.example.shopbanhang.models.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.StringValue;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<CartModel> cartModelList;
    int total_price_of_all = 0;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public CartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(cartModelList.get(position).getProductImg()).into(holder.cartimg);
        holder.name.setText(cartModelList.get(position).getProductName());
        holder.price.setText(cartModelList.get(position).getProductPrice());
        holder.date.setText(cartModelList.get(position).getCurrentDate());
        holder.time.setText(cartModelList.get(position).getCurrentTime());
        holder.quantity.setText(cartModelList.get(position).getTotal_quantity());
        holder.totalprice.setText(String.valueOf(cartModelList.get(position).getTotal_price())+"$");

        int iiiii = position;

        holder.remove_item_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("AddToCart")
                        .document(cartModelList.get(iiiii).getDocumentID())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    cartModelList.remove(cartModelList.get(iiiii));
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(context, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        //Truyền dữ liệu total_price




    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView cartimg,remove_item_cart;
        TextView name,price,date,time,quantity,totalprice;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cartimg = itemView.findViewById(R.id.cart_img);
            remove_item_cart = itemView.findViewById(R.id.remove_item_cart);
            name = itemView.findViewById(R.id.cart_name);
            price = itemView.findViewById(R.id.cart_price);
            date = itemView.findViewById(R.id.cart_current_date);
            time = itemView.findViewById(R.id.cart_current_time);
            quantity = itemView.findViewById(R.id.cart_quantity);
            totalprice = itemView.findViewById(R.id.cart_total_price);

        }
    }
}
