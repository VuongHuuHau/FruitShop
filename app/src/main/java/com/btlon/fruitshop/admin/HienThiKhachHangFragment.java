package com.btlon.fruitshop.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import com.btlon.fruitshop.AdapterAdmin.KhachHangAdapter;
import com.btlon.fruitshop.R;
import com.btlon.fruitshop.models.KhachHang;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HienThiKhachHangFragment extends Fragment {
    private ListView listView;
    private KhachHangAdapter adapter;
    private List<KhachHang> khachHangList;
    private DatabaseReference databaseRef;
    private FloatingActionButton fabAddCustomer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_hien_thi_khach_hang, container, false);

        listView = view.findViewById(R.id.listViewKhachHang);
        khachHangList = new ArrayList<>();
        adapter = new KhachHangAdapter(getContext(), khachHangList);
        listView.setAdapter(adapter);

        // Khởi tạo Firebase Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("KhachHang");

        // Lấy dữ liệu từ Firebase
        loadKhachHangData();

        // Xử lý sự kiện khi nhấn vào nút thêm khách hàng
        fabAddCustomer = view.findViewById(R.id.fabAddCustomer);
        fabAddCustomer.setOnClickListener(v -> showAddCustomerDialog());

        return view;
    }

    private void loadKhachHangData() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                khachHangList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    KhachHang khachHang = snapshot.getValue(KhachHang.class);
                    if (khachHang != null) {
                        khachHangList.add(khachHang);
                    }
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("HienThiKhachHang", "Adapter is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddCustomerDialog() {
        // Tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("THÊM KHÁCH HÀNG");

        // Inflate layout cho dialog
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.activity_add_khach_hang, null);
        builder.setView(dialogView);

        // Ánh xạ các thành phần trong layout add_khach_hang
        EditText edtMaKhachHang = dialogView.findViewById(R.id.edtMaKhachHang);
        EditText edtTen = dialogView.findViewById(R.id.edtTen);
        EditText edtEmail = dialogView.findViewById(R.id.edtEmail);
        EditText edtDiaChi = dialogView.findViewById(R.id.edtDiaChi);
        EditText edtSoDienThoai = dialogView.findViewById(R.id.edtSoDienThoai);
        EditText edtTenDangNhap = dialogView.findViewById(R.id.edtTenDangNhap);
        EditText edtMatKhau = dialogView.findViewById(R.id.edtMatKhau);
        EditText edtAvatarUrl = dialogView.findViewById(R.id.edtAvatarUrl);
        Button btnThemKhachHang = dialogView.findViewById(R.id.btnThemKhachHang);
        Button btnHuyKhachHang = dialogView.findViewById(R.id.btnHuyKhachHang);

        // Tạo AlertDialog từ builder
        AlertDialog alertDialog = builder.create();

        // Xử lý khi nhấn nút "Thêm Khách Hàng"
        btnThemKhachHang.setOnClickListener(view -> {
            // Lấy dữ liệu từ các EditText
            String maKhachHang = edtMaKhachHang.getText().toString().trim();
            String ten = edtTen.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String diaChi = edtDiaChi.getText().toString().trim();
            String soDienThoai = edtSoDienThoai.getText().toString().trim();
            String tenDangNhap = edtTenDangNhap.getText().toString().trim();
            String matKhau = edtMatKhau.getText().toString().trim();
            String avatarUrl = edtAvatarUrl.getText().toString().trim();

            // Kiểm tra xem tất cả các trường đã được nhập dữ liệu chưa
            if (maKhachHang.isEmpty() || ten.isEmpty() || email.isEmpty() || diaChi.isEmpty() ||
                    soDienThoai.isEmpty() || tenDangNhap.isEmpty() || matKhau.isEmpty() || avatarUrl.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo một đối tượng KhachHang mới
            KhachHang khachHang = new KhachHang(maKhachHang, ten, email, diaChi, soDienThoai, tenDangNhap, matKhau, avatarUrl);

            // Lưu khách hàng vào Firebase Database
            databaseRef.child(maKhachHang).setValue(khachHang)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss(); // Đóng dialog sau khi thêm thành công
                        } else {
                            Toast.makeText(getContext(), "Thêm khách hàng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Xử lý khi nhấn nút "Hủy"
        btnHuyKhachHang.setOnClickListener(view -> alertDialog.dismiss());

        // Hiển thị dialog
        alertDialog.show();
    }
}
