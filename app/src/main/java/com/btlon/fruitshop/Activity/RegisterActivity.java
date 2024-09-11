//package com.btlon.fruitshop.Activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//
//import com.btlon.fruitshop.R;
//import com.btlon.fruitshop.models.KhachHang;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthUserCollisionException;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class RegisterActivity extends AppCompatActivity {
//
//    private static final String TAG = "RegisterActivity";
//
//    private EditText tenEditText, usernameEditText, emailEditText, passwordEditText, phoneEditText, diaChiEditText, avatarUrlEditText;
//    private Button registerButton;
//    private FirebaseAuth mAuth;
//    private DatabaseReference usersRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        // Khởi tạo Firebase Auth và Realtime Database
//        mAuth = FirebaseAuth.getInstance();
//        usersRef = FirebaseDatabase.getInstance().getReference("KhachHang");
//
//        tenEditText = findViewById(R.id.tenEditText);
//        usernameEditText = findViewById(R.id.usernameEditText);
//        emailEditText = findViewById(R.id.emailEditText);
//        passwordEditText = findViewById(R.id.passwordEditText);
//        phoneEditText = findViewById(R.id.phoneEditText);
//        diaChiEditText = findViewById(R.id.diaChiEditText);
//        avatarUrlEditText = findViewById(R.id.avatarUrlEditText);
//        registerButton = findViewById(R.id.registerButton);
//
//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String ten = tenEditText.getText().toString().trim();
//                String username = usernameEditText.getText().toString().trim();
//                String email = emailEditText.getText().toString().trim();
//                String password = passwordEditText.getText().toString().trim();
//                String phone = phoneEditText.getText().toString().trim();
//                String diaChi = diaChiEditText.getText().toString().trim();
//                String avatarUrl = avatarUrlEditText.getText().toString().trim();
//
//                if (ten.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || diaChi.isEmpty()) {
//                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//                } else {
//                    register(ten, username, email, password, phone, diaChi, avatarUrl);
//                }
//            }
//        });
//    }
//
//    private void register(String ten, String username, String email, String password, String phone, String diaChi, String avatarUrl) {
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        // Đăng ký thành công
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        if (user != null) {
//                            String uid = user.getUid();
//                            saveUserToDatabase(uid, ten, username, email, phone, diaChi, avatarUrl);
//                        }
//                    } else {
//                        // Nếu đăng ký thất bại, lấy thông tin lỗi và thông báo lỗi
//                        Exception exception = task.getException();
//                        if (exception != null) {
//                            if (exception instanceof FirebaseAuthUserCollisionException) {
//                                // Email đã tồn tại
//                                Toast.makeText(RegisterActivity.this, "Email đã tồn tại.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Log.e(TAG, "Đăng ký thất bại", exception);
//                                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    private void saveUserToDatabase(String uid, String ten, String username, String email, String phone, String diaChi, String avatarUrl) {
//        KhachHang KhachHang = new KhachHang(uid, ten, email, diaChi, phone, username, "", avatarUrl);
//        usersRef.child(uid).setValue(KhachHang).addOnCompleteListener(this, task -> {
//            if (task.isSuccessful()) {
//                // Lưu thông tin người dùng thành công
//                Toast.makeText(RegisterActivity.this, "Đăng ký thành công.", Toast.LENGTH_SHORT).show();
//                // Chuyển đến màn hình chính hoặc thực hiện hành động khác
//                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                finish();
//            } else {
//                // Nếu lưu thông tin thất bại, thông báo lỗi
//                Exception exception = task.getException();
//                if (exception != null) {
//                    Log.e(TAG, "Lưu thông tin thất bại", exception);
//                    Toast.makeText(RegisterActivity.this, "Lưu thông tin thất bại: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(RegisterActivity.this, "Lưu thông tin thất bại.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//}
package com.btlon.fruitshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.btlon.fruitshop.R;
import com.btlon.fruitshop.models.KhachHang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText tenEditText, usernameEditText, emailEditText, passwordEditText, phoneEditText, diaChiEditText, avatarUrlEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo Firebase Auth và Realtime Database
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("KhachHang");

        tenEditText = findViewById(R.id.tenEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        diaChiEditText = findViewById(R.id.diaChiEditText);
        avatarUrlEditText = findViewById(R.id.avatarUrlEditText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = tenEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String diaChi = diaChiEditText.getText().toString().trim();
                String avatarUrl = avatarUrlEditText.getText().toString().trim();

                if (ten.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || diaChi.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    register(ten, username, email, password, phone, diaChi, avatarUrl);
                }
            }
        });
    }

    private void register(String ten, String username, String email, String password, String phone, String diaChi, String avatarUrl) {
        Log.d(TAG, "register: Register method called");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "register: Registration successful");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            saveUserToDatabase(uid, ten, username, email, phone, diaChi, avatarUrl);
                        }
                    } else {
                        Log.d(TAG, "register: Registration failed");
                        Exception exception = task.getException();
                        if (exception != null) {
                            if (exception instanceof FirebaseAuthUserCollisionException) {
                                // Email đã tồn tại
                                Toast.makeText(RegisterActivity.this, "Email đã tồn tại.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "Đăng ký thất bại", exception);
                                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserToDatabase(String uid, String ten, String username, String email, String phone, String diaChi, String avatarUrl) {
        Log.d(TAG, "saveUserToDatabase: Saving user to database");
        KhachHang khachHang = new KhachHang(uid, ten, email, diaChi, phone, username, "", avatarUrl, "1");

        usersRef.child(uid).setValue(khachHang).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "saveUserToDatabase: User saved successfully");
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            } else {
                Log.d(TAG, "saveUserToDatabase: Failed to save user");
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e(TAG, "Lưu thông tin thất bại", exception);
                    Toast.makeText(RegisterActivity.this, "Lưu thông tin thất bại: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Lưu thông tin thất bại.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

