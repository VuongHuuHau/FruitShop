package com.btlon.fruitshop.Activity;//package com.btlon.fruitshop.Activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.btlon.fruitshop.Activity.CartActivity;
import com.btlon.fruitshop.R;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.Firebase;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private EditText emailEditText;
//    private EditText passwordEditText;
//    private Button loginButton;
//    private FirebaseAuth mAuth;
//    private FirebaseDatabase db;
//    private TextView signintxt;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        // Khởi tạo Firebase Auth và Firestore
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseDatabase.getInstance();
//
//        emailEditText = findViewById(R.id.emailEditText);
//        passwordEditText = findViewById(R.id.passwordEditText);
//        loginButton = findViewById(R.id.loginbnt);
//
//        loginButton.setOnClickListener(v -> {
//            String email = emailEditText.getText().toString().trim();
//            String matKhau = passwordEditText.getText().toString().trim();
//
//            if (email.isEmpty() || matKhau.isEmpty()) {
//                Toast.makeText(LoginActivity.this, "vui long nhap day du thong tin", Toast.LENGTH_SHORT).show();
//            } else {
//                login(email, matKhau);
//            }
//        });
//        signintxt = findViewById(R.id.signintxt);
//        signintxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Chuyển sang SignInActivity
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//
//    private void login(String email, String password) {
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                        } else {
//                            // Nếu đăng nhập thất bại, thông báo lỗi
//                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "người dùng không tồn tại";
//                            Toast.makeText(LoginActivity.this, "Loi xac thuc nguoi dung: " + errorMessage, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//
//    private void getUserData(String uid, String inputPassword) {
//
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("KhanhHang").child(uid);
//
//        // Sử dụng addListenerForSingleValueEvent để đọc dữ liệu một lần
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Lấy mật khẩu đã lưu trong database
//
//                    String storedPassword = dataSnapshot.child("matKhau").getValue(String.class);
//
//                    // Kiểm tra mật khẩu nhập vào có khớp với mật khẩu trong database không
//                    if (inputPassword.equals(storedPassword)) {
//                        // Nếu mật khẩu đúng, chuyển đến màn hình giỏ hàng
//                        startActivity(new Intent(LoginActivity.this, CartActivity.class));
//                        finish();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Mat khau sai ", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(LoginActivity.this, "khong tim thay nguoi dung", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                String errorMessage = databaseError.getMessage();
//                Toast.makeText(LoginActivity.this, "lay du lieu that bai: " + errorMessage, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.btlon.fruitshop.Activity.HomeActivity;
import com.btlon.fruitshop.AdminActivity;
import com.btlon.fruitshop.admin.TrangChuActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView signintxt;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("KhachHang");
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);


        // Ví dụ: Khi người dùng nhấn nút đăng nhập
        findViewById(R.id.loginbnt).setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String matKhau = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || matKhau.isEmpty()) {
                Toast.makeText(LoginActivity.this, "vui long nhap day du thong tin", Toast.LENGTH_SHORT).show();
            } else {
                login(email, matKhau);
            }
        });
        signintxt = findViewById(R.id.signintxt);
        signintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang SignInActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    checkUserTypeAndRedirect(currentUser.getUid());
                }
            } else {
                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserTypeAndRedirect(String uid) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("KhachHang").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userType = dataSnapshot.child("maRole").getValue(String.class);
                    if (userType != null) {
                        if (userType.equals("2")) {
                            // Chuyển đến màn hình admin
                            startActivity(new Intent(LoginActivity.this, TrangChuActivity.class));
                        } else if (userType.equals("1")) {
                            // Chuyển đến màn hình người dùng
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Loại người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Không thể xác định loại người dùng", Toast.LENGTH_SHORT).show();
                    }
                    finish(); // Kết thúc màn hình đăng nhập sau khi chuyển trang
                } else {
                    Toast.makeText(LoginActivity.this, "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}

