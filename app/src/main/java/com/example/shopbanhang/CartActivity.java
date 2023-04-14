package com.example.shopbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.shopbanhang.adapters.CartAdapter;
import com.example.shopbanhang.adapters.ViewAllAdapter;
import com.example.shopbanhang.models.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    RecyclerView cart_rec;
    CartAdapter cartAdapter;
    List<CartModel> cartModelList;

    TextView total_price_of_all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        total_price_of_all = findViewById(R.id.total_price_of_all);

        cart_rec = findViewById(R.id.cart_rec);


        cart_rec.setLayoutManager(new LinearLayoutManager(this));

        cartModelList = new ArrayList<>();
        cartAdapter = new CartAdapter(this,cartModelList);
        cart_rec.setAdapter(cartAdapter);

        //Đọc thông tin các sản phẩm trong giỏ hàng của user hiện tại
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                                CartModel cartModel = documentSnapshot.toObject(CartModel.class);


                                cartModelList.add(cartModel);
                                cartAdapter.notifyDataSetChanged();

                            }
                            //Tính giá của cả giỏ hàng
                            calTotalPriceOfAll(cartModelList);

                        }


                    }
                });



        //Xủ lý bottom_nav
        bottomNavigationView = findViewById(R.id.nav_bottom);
        bottomNavigationView.setSelectedItemId(R.id.cart);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.cart:

                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.user:
                        startActivity(new Intent(getApplicationContext(),UserActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }


    //Tính tổng giá của cả giỏ hàng
    private void calTotalPriceOfAll(List<CartModel> cartModelList) {

        double totalPriceOfAll = 0.0;
        for(CartModel cartModel : cartModelList){
            totalPriceOfAll = totalPriceOfAll+cartModel.getTotal_price();
        }
        String text1 = String.valueOf(totalPriceOfAll);
        total_price_of_all.setText(text1+"$");
    }
}