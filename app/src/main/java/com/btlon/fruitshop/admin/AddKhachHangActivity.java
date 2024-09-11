package com.btlon.fruitshop.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.btlon.fruitshop.R;
import com.btlon.fruitshop.models.KhachHang;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddKhachHangActivity extends AppCompatActivity {
    private EditText edtMaKhachHang, edtTen, edtEmail, edtDiaChi, edtSoDienThoai, edtTenDangNhap, edtMatKhau, edtAvatarUrl;
    private Button btnThemKhachHang;
    private DatabaseReference databaseKhachHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_khach_hang);

        // Khởi tạo Firebase Database
        databaseKhachHang = FirebaseDatabase.getInstance().getReference("KhachHang");

        // Ánh xạ các view
        edtMaKhachHang = findViewById(R.id.edtMaKhachHang);
        edtTen = findViewById(R.id.edtTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtAvatarUrl = findViewById(R.id.edtAvatarUrl);
        btnThemKhachHang = findViewById(R.id.btnThemKhachHang);

        // Xử lý sự kiện khi bấm nút "Thêm Khách Hàng"
        btnThemKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themKhachHang();
            }
        });
    }

    private void themKhachHang() {
        String maKhachHang = edtMaKhachHang.getText().toString().trim();
        String ten = edtTen.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();
        String soDienThoai = edtSoDienThoai.getText().toString().trim();
        String tenDangNhap = edtTenDangNhap.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();
        String avatarUrl = edtAvatarUrl.getText().toString().trim();

        if (!TextUtils.isEmpty(maKhachHang)) {
            String id = databaseKhachHang.push().getKey();
            KhachHang khachHang = new KhachHang(id, ten, email, diaChi, soDienThoai, tenDangNhap, matKhau, avatarUrl);
            databaseKhachHang.child(id).setValue(khachHang);
            Toast.makeText(this, "Khách hàng đã được thêm", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        }
    }
}
