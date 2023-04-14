package com.example.shopbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopbanhang.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {


    ImageView detail_img;
    TextView detail_name,detail_price,detail_description,quantity;
    Button add_to_cart;
    ImageView minus, plus;

    int total_quantity = 1;
    double total_price = 0;

    ViewAllModel viewAllModel = null;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //Bắt dữ liệu truyền qua Serializable
        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof ViewAllModel){
            viewAllModel = (ViewAllModel) object;
        }

        detail_img = findViewById(R.id.detail_img);
        detail_name = findViewById(R.id.detail_name);
        detail_price = findViewById(R.id.detail_price);
        detail_description = findViewById(R.id.detail_description);
        add_to_cart = findViewById(R.id.add_to_cart_btn);
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
        quantity = findViewById(R.id.quantity);


        //Gán thông tin
        if (viewAllModel != null){
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detail_img);
            detail_name.setText(viewAllModel.getName());
            detail_price.setText(viewAllModel.getPrice()+"$/cái");
            detail_description.setText(viewAllModel.getDescription());
            total_price = viewAllModel.getPrice()*total_quantity;
        }

        //Giảm số lượng mua tối thiểu 0 cái
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total_quantity>0){
                    total_quantity--;
                    quantity.setText(String.valueOf(total_quantity));
                    total_price = viewAllModel.getPrice()*total_quantity;
                }
            }
        });


        //Tăng số lượng mua tối đa 100 cái
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total_quantity<100){
                    total_quantity++;
                    quantity.setText(String.valueOf(total_quantity));
                    total_price = viewAllModel.getPrice()*total_quantity;
                }
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });




    }

    private void addToCart() {

        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("productImg",viewAllModel.getImg_url());
        cartMap.put("productName",detail_name.getText().toString());
        cartMap.put("productPrice",detail_price.getText().toString());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("total_quantity",quantity.getText().toString());
        cartMap.put("total_price",total_price);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailActivity.this,"Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                        finish();


                    }
                });
    }
}