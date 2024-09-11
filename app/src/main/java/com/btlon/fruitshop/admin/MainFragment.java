package com.btlon.fruitshop.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import com.btlon.fruitshop.AdapterAdmin.SanPhamListAdapter;
import com.btlon.fruitshop.R;
import com.btlon.fruitshop.models.SanPham;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private ListView listView;
    private List<SanPham> sanPhamList;
    private SanPhamListAdapter adapter;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        listView = view.findViewById(R.id.fruitTypesListView);
        Button btnThem = view.findViewById(R.id.addNewType);

        // Tạo danh sách sản phẩm mẫu
        sanPhamList = new ArrayList<>();

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("SanPham");

        // Create adapter and set it to the ListView
        adapter = new SanPhamListAdapter(getContext(), R.layout.item_san_pham, sanPhamList);
        listView.setAdapter(adapter);


        // Load data from Firebase
        loadSanPhamFromFirebase();

        // Set up the add button click listener
        btnThem.setOnClickListener(v -> showAddSanPhamDialog());

        return view;
    }

    // Load data from Firebase
    private void loadSanPhamFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sanPhamList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SanPham sanPham = postSnapshot.getValue(SanPham.class);
                    sanPhamList.add(sanPham);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MainFragment", "Failed to read data from Firebase", databaseError.toException());
            }
        });
    }

    // Show add product dialog
    private void showAddSanPhamDialog() {
        // Inflate custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.activity_add_san_pham, null);

        // Get references to EditTexts and Spinner
        EditText etMaSanPham = dialogView.findViewById(R.id.etMaSanPham);
        EditText etTenSP = dialogView.findViewById(R.id.etTenSP);
        EditText etDinhLuong = dialogView.findViewById(R.id.etDinhLuong);
        EditText etGia = dialogView.findViewById(R.id.etGia);
        EditText etHinhAnh = dialogView.findViewById(R.id.etHinhAnh);
        EditText etMoTa = dialogView.findViewById(R.id.etMoTa);
        EditText etMaLoaiTraiCay = dialogView.findViewById(R.id.etMaLoaiTraiCay);

        // Create AlertDialog
        new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                .setTitle("Thêm Sản Phẩm Mới")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    // Get user input
                    String maSanPham = etMaSanPham.getText().toString().trim();
                    String tenSP = etTenSP.getText().toString().trim();
                    String dinhLuong = etDinhLuong.getText().toString().trim();
                    String gia = etGia.getText().toString().trim();
                    String hinhAnh = etHinhAnh.getText().toString().trim();
                    String moTa = etMoTa.getText().toString().trim();
                    String maLoaiTraiCay = etMaLoaiTraiCay.getText().toString().trim();

                    if (maSanPham.isEmpty() || tenSP.isEmpty() || dinhLuong.isEmpty() || gia.isEmpty() || hinhAnh.isEmpty() || moTa.isEmpty()) {
                        Toast.makeText(getActivity(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Create SanPham object
                    SanPham sanPham = new SanPham(maSanPham, tenSP, Double.parseDouble(dinhLuong), Double.parseDouble(gia), hinhAnh, moTa, maLoaiTraiCay);

                    // Save to Firebase
                    databaseReference.child(maSanPham).setValue(sanPham)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Sản phẩm đã được thêm", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

}
