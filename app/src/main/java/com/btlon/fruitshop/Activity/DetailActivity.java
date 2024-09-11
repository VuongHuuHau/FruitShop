package com.btlon.fruitshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.btlon.fruitshop.R;

import com.btlon.fruitshop.databinding.ActivityDetailBinding;
import com.btlon.fruitshop.models.SanPham;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private SanPham tc;
    private double num=0.1;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
    getintn();
    serVar();
    }

    private void serVar() {

        binding.backbnt.setOnClickListener(v -> {
            finish();;
        });
        Glide.with(DetailActivity.this)
                .load(tc.getHinhAnh())
                .into(binding.pic);
        binding.pricetxt.setText("VND "+ tc.getGia());
        binding.tensptxt.setText("VND "+ tc.getTenSP());

        binding.spTbnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=num+0.1;
                binding.numtxt.setText(num+" kg");
                binding.tongtxt.setText((num*tc.getGia()+"VND"));
            }
        });
        binding.spGbnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num>0.1)
                {
                    num=num-0.1;
                    binding.numtxt.setText(num+" kg");
                    binding.tongtxt.setText((num*tc.getGia()+"VND"));
                }
            }
        });
        binding.muabnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth != null) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Carts").child(userId);

                        cartRef.child(tc.getMaSanPham()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // Xử lý sản phẩm đã tồn tại trong giỏ hàng
                                    double currentQuantity = snapshot.child("quantity").getValue(Double.class);
                                    double newQuantity = currentQuantity + num; // Tăng số lượng
                                    double newTotalPrice = newQuantity * tc.getGia(); // Cập nhật tổng tiền

                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("quantity", newQuantity);
                                    updates.put("totalPrice", newTotalPrice);

                                    // Cập nhật sản phẩm trong giỏ hàng
                                    cartRef.child(tc.getMaSanPham()).updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(DetailActivity.this, "Số lượng đã được cập nhật", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(DetailActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    // Thêm sản phẩm mới vào giỏ hàng
                                    Map<String, Object> cartItem = new HashMap<>();
                                    cartItem.put("productId", tc.getMaSanPham());
                                    cartItem.put("productName", tc.getTenSP());
                                    cartItem.put("productPrice", tc.getGia());
                                    cartItem.put("quantity", num);
                                    cartItem.put("totalPrice", num * tc.getGia());
                                    cartItem.put("img", tc.getHinhAnh());

                                    cartRef.child(tc.getMaSanPham()).setValue(cartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(DetailActivity.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                            } else {
                                                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Thêm thất bại";
                                                Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(DetailActivity.this, "Lỗi cơ sở dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(DetailActivity.this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "FirebaseAuth chưa được khởi tạo", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void getintn() {
        tc=(SanPham) getIntent().getSerializableExtra("tc");

    }

}