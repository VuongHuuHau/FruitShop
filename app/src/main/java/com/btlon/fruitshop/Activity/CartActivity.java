//package com.btlon.fruitshop.Activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.btlon.fruitshop.Adapter.CartAdapter;
//import com.btlon.fruitshop.R;
//import com.btlon.fruitshop.Vnpay.ThanhToanActivity;
//import com.btlon.fruitshop.Vnpay.WebViewActivity;
//import com.btlon.fruitshop.databinding.ActivityCartBinding;
//import com.btlon.fruitshop.models.CartItem;
//import com.btlon.fruitshop.models.ChiTietDonHang;
//import com.btlon.fruitshop.models.DonHang;
//import com.btlon.fruitshop.models.SanPham;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.io.UnsupportedEncodingException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//public class CartActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private CartAdapter adapter;
//    private FirebaseDatabase database;
//    private FirebaseUser user;
//    private ActivityCartBinding binding;
//    private FirebaseAuth mAuth;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mAuth = FirebaseAuth.getInstance();
//        // Sử dụng View Binding để thiết lập layout
//        binding = ActivityCartBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Khởi tạo Firebase
//        database = FirebaseDatabase.getInstance();
//        user = FirebaseAuth.getInstance().getCurrentUser();
//
//        // Thiết lập RecyclerView
//        recyclerView = binding.cartview;
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Khởi tạo adapter với danh sách trống
//        adapter = new CartAdapter(new ArrayList<>());
//        recyclerView.setAdapter(adapter);
//
//
//        // Gọi phương thức để tải dữ liệu giỏ hàng
//        loadCartItems();
//        ver();
//    }
//
////    private void ver() {
////
////        binding.backbnt.setOnClickListener(v -> {
////            finish();;
////        });
////        binding.thanhtoanbnt.setOnClickListener(v -> {
////            List<CartItem> cartItems = adapter.getItems(); // Lấy danh sách các mục từ adapter
////            if (cartItems.isEmpty()) {
////                Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
////                return;
////            }
////
////            String orderId = UUID.randomUUID().toString(); // Tạo mã đơn hàng ngẫu nhiên
////            int totalAmount = 0;
////
////            for (CartItem cartItem : cartItems) {
////                totalAmount += (int) cartItem.getTotalPrice();
////            }
////
////            handlePayment(orderId, totalAmount); // Xử lý thanh toán
////
////            // Tạo chi tiết đơn hàng và đơn hàng
////            for (CartItem cartItem : cartItems) {
////                ChiTietDonHang chiTietDonHang = new ChiTietDonHang(
////                        UUID.randomUUID().toString(), // Tạo mã chi tiết đơn hàng
////                        cartItem.getQuantity(),
////                        (int) cartItem.getTotalPrice(),
////                        orderId,
////                        cartItem.getProductId() // Mã sản phẩm
////                );
////
////                DonHang donHang = new DonHang(
////                        orderId,
////                        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()), // Ngày tạo đơn hàng
////                        totalAmount,
////                        "Hoàn Thành", // Trạng thái đơn hàng
////                        user != null ? user.getUid() : "Vui Long dang nhap"
////                );
////
////                // Lưu vào Firebase Realtime Database
////                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
////                DatabaseReference chiTietDonHangRef = database.child("ChiTietDonHang");
////                DatabaseReference donHangRef = database.child("DonHang");
////
////                // Lưu chi tiết đơn hàng
////                chiTietDonHangRef.child(chiTietDonHang.getId()).setValue(chiTietDonHang);
////
////                // Lưu đơn hàng
////                donHangRef.child(orderId).setValue(donHang);
////
////                // Xóa mục giỏ hàng sau khi thanh toán thành công
////                DatabaseReference cartRef = database.child("Cart").child(user != null ? user.getUid() : "unknown_user").child(cartItem.getProductId());
////                cartRef.removeValue().addOnCompleteListener(task -> {
////                    if (task.isSuccessful()) {
////                        // Xóa thành công, bạn có thể cập nhật UI nếu cần thiết
////                        Toast.makeText(this, "Đã thanh toán thành công đơn hàng", Toast.LENGTH_SHORT).show();
////                    } else {
////                        // Xử lý nếu việc xóa không thành công
////                        Toast.makeText(this, "Lỗi khi xóa mục khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
////                    }
////                });
////            }
////
////            // Cập nhật lại danh sách giỏ hàng
////            loadCartItems();
////        });
////    }
//private void ver() {
//    binding.backbnt.setOnClickListener(v -> finish());
//    binding.thanhtoanbnt.setOnClickListener(v -> {
//        List<CartItem> cartItems = adapter.getItems(); // Lấy danh sách các mục từ adapter
//        if (cartItems.isEmpty()) {
//            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String orderId = UUID.randomUUID().toString(); // Tạo mã đơn hàng ngẫu nhiên
//        int totalAmount = 0;
//
//        for (CartItem cartItem : cartItems) {
//            totalAmount += (int) cartItem.getTotalPrice();
//        }
//
//        // Xử lý thanh toán
//        handlePayment(orderId, totalAmount);
//
//        // Tạo chi tiết đơn hàng và đơn hàng
//        DatabaseReference chiTietDonHangRef = database.getReference("ChiTietDonHang");
//        DatabaseReference donHangRef = database.getReference("DonHang");
//        DatabaseReference cartRef = database.getReference("Carts").child(user != null ? user.getUid() : "unknown_user");
//
//        // Lưu chi tiết đơn hàng và đơn hàng
//        for (CartItem cartItem : cartItems) {
//            ChiTietDonHang chiTietDonHang = new ChiTietDonHang(
//                    UUID.randomUUID().toString(), // Tạo mã chi tiết đơn hàng
//                    cartItem.getQuantity(),
//                    (int) cartItem.getTotalPrice(),
//                    orderId,
//                    cartItem.getProductId() // Mã sản phẩm
//            );
//
//            DonHang donHang = new DonHang(
//                    orderId,
//                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()), // Ngày tạo đơn hàng
//                    totalAmount,
//                    "Hoàn Thành", // Trạng thái đơn hàng
//                    user != null ? user.getUid() : "Vui Long dang nhap"
//            );
//
//            // Lưu chi tiết đơn hàng
//            chiTietDonHangRef.child(chiTietDonHang.getId()).setValue(chiTietDonHang);
//
//            // Lưu đơn hàng
//            donHangRef.child(orderId).setValue(donHang);
//        }
//
//        // Xóa từng sản phẩm trong giỏ hàng sau khi thanh toán thành công
//        for (CartItem cartItem : cartItems) {
//            DatabaseReference cartItemRef = cartRef.child(cartItem.getProductId());
//            cartItemRef.removeValue().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    // Xóa thành công, bạn có thể cập nhật UI nếu cần thiết
//                    Toast.makeText(this, "Sản phẩm đã được xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Xử lý nếu việc xóa không thành công
//                    Toast.makeText(this, "Lỗi khi xóa sản phẩm khỏi giỏ hàng: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//        // Cập nhật lại danh sách giỏ hàng
//        loadCartItems();
//    });
//}
//
//    private void loadCartItems() {
//        if (user == null) {
//            Log.e("CartActivity", "Vui long dang nhap");
//            return;
//        }
//
//        DatabaseReference cartRef = database.getReference("Carts").child(user.getUid());
//
//        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ArrayList<CartItem> list = new ArrayList<>();
//
//                if (snapshot.exists()) {
//                        double tongTien = 0; // Khởi tạo tổng tiền bên ngoài vòng lặp
//
//                        for (DataSnapshot issue : snapshot.getChildren()) {
//                            CartItem cartItem = issue.getValue(CartItem.class);
//                            if (cartItem != null) {
//                                list.add(cartItem);
//                                tongTien += cartItem.getTotalPrice(); // Cộng dồn tổng tiền cho mỗi CartItem
//                            }
//                        }
//
//                        // Sau khi tính xong tổng tiền, cập nhật giá trị vào TextView
//                        binding.tongtientxt.setText(tongTien + " VND");
//
//
//                    // Cập nhật adapter với danh sách mới
//                    adapter = new CartAdapter(list);
//                    recyclerView.setAdapter(adapter);
//                } else {
//                    Log.e("CartActivity", "No items found in cart");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("CartActivity", "DatabaseError: " + error.getMessage());
//            }
//        });
//    }
//    public void handlePayment(String orderId, int amount) {
//        ThanhToanActivity thanhToanActivity = new ThanhToanActivity();
//        String paymentUrl;
//        try {
//            paymentUrl = thanhToanActivity.createPaymentUrl(orderId, amount, "192.168.1.2");
//            startVnpayPayment(paymentUrl);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void startVnpayPayment(String paymentUrl) {
//        Intent intent = new Intent(this, WebViewActivity.class);
//        intent.putExtra("paymentUrl", paymentUrl);
//        startActivity(intent);
//    }
//}
package com.btlon.fruitshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btlon.fruitshop.Adapter.CartAdapter;
import com.btlon.fruitshop.R;
import com.btlon.fruitshop.Vnpay.ThanhToanActivity;
import com.btlon.fruitshop.Vnpay.WebViewActivity;
import com.btlon.fruitshop.databinding.ActivityCartBinding;
import com.btlon.fruitshop.models.CartItem;
import com.btlon.fruitshop.models.ChiTietDonHang;
import com.btlon.fruitshop.models.DonHang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private ActivityCartBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Firebase initialization
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();

        // Setup RecyclerView
        recyclerView = binding.cartview;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with an empty list
        adapter = new CartAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Load cart items
        loadCartItems();

        // Handle button events
        handleButtonEvents();
    }

    private void handleButtonEvents() {
        binding.backbnt.setOnClickListener(v -> finish());

        binding.thanhtoanbnt.setOnClickListener(v -> {
            List<CartItem> cartItems = adapter.getItems();
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                return;
            }

            String orderId = UUID.randomUUID().toString(); // Generate a random order ID
            int totalAmount = 0;

            for (CartItem cartItem : cartItems) {
                totalAmount += (int) cartItem.getTotalPrice();
            }

            // Process the payment
            handlePayment(orderId, totalAmount);

            // Save order and details, and clear the cart
            saveOrderAndDetails(orderId, cartItems, totalAmount);
        });
    }

    private void saveOrderAndDetails(String orderId, List<CartItem> cartItems, int totalAmount) {
        DatabaseReference chiTietDonHangRef = database.getReference("ChiTietDonHang");
        DatabaseReference donHangRef = database.getReference("DonHang");
        DatabaseReference cartRef = database.getReference("Carts").child(user != null ? user.getUid() : "unknown_user");

        // Save order details and the order itself
        for (CartItem cartItem : cartItems) {
            ChiTietDonHang chiTietDonHang = new ChiTietDonHang(
                    UUID.randomUUID().toString(), // Generate a unique ID for order details
                    cartItem.getQuantity(),
                    (int) cartItem.getTotalPrice(),
                    orderId,
                    cartItem.getProductId() // Product ID
            );

            DonHang donHang = new DonHang(
                    orderId,
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()), // Order creation date
                    totalAmount,
                    "Hoàn Thành", // Order status
                    user != null ? user.getUid() : "Vui Long dang nhap"
            );

            // Save order details
            chiTietDonHangRef.child(chiTietDonHang.getId()).setValue(chiTietDonHang);

            // Save the order
            donHangRef.child(orderId).setValue(donHang);
        }

        // Remove each product from the cart after successful payment
        for (CartItem cartItem : cartItems) {
            DatabaseReference cartItemRef = cartRef.child(cartItem.getProductId());
            cartItemRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Sản phẩm đã được xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi xóa sản phẩm khỏi giỏ hàng: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Refresh cart items after payment
        loadCartItems();
    }

    private void loadCartItems() {
        if (user == null) {
            Log.e("CartActivity", "Vui long dang nhap");
            return;
        }

        DatabaseReference cartRef = database.getReference("Carts").child(user.getUid());

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CartItem> list = new ArrayList<>();
                double totalPrice = 0;

                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        CartItem cartItem = issue.getValue(CartItem.class);
                        if (cartItem != null) {
                            list.add(cartItem);
                            totalPrice += cartItem.getTotalPrice();
                        }
                    }
                    // Update total price in the UI
                    binding.tongtientxt.setText(totalPrice + " VND");

                    // Update adapter with the new list
                    adapter = new CartAdapter(list);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("CartActivity", "No items found in cart");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CartActivity", "DatabaseError: " + error.getMessage());
            }
        });
    }

    public void handlePayment(String orderId, int amount) {
        ThanhToanActivity thanhToanActivity = new ThanhToanActivity();
        String paymentUrl;
        try {
            paymentUrl = thanhToanActivity.createPaymentUrl(orderId, amount, "192.168.1.2");
            startVnpayPayment(paymentUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void startVnpayPayment(String paymentUrl) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("paymentUrl", paymentUrl);
        startActivity(intent);
    }
}

