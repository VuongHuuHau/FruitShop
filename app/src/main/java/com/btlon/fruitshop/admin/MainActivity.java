package com.btlon.fruitshop.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import com.btlon.fruitshop.AdapterAdmin.SanPhamListAdapter;
import com.btlon.fruitshop.R;
import com.btlon.fruitshop.models.SanPham;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    // Mã này sẽ mở thư viện ảnh
//    private static final int PICK_IMAGE_REQUEST = 1;
//    private Uri imageUri; // URI của ảnh đã chọn
    private ListView listView;
    private List<SanPham> sanPhamList;
    private SanPhamListAdapter adapter;
    private DatabaseReference databaseReference;

    //khai báo hàm chuyển tab
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.nav_host_fragment);
//        setupBottomNavigation(navHostFragment);

        Toolbar toolbar = findViewById(R.id.toolbar); // Tìm Toolbar trong layout
        setSupportActionBar(toolbar); // Thiết lập Toolbar làm ActionBar

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Load the default fragment (TrangChuFragment)
        if (savedInstanceState == null) {
            loadFragment(new TrangChuFragment());
            navigationView.setCheckedItem(R.id.nav_trang_chu);
        }

        listView = findViewById(R.id.fruitTypesListView);
        Button btnThem = findViewById(R.id.addNewType);
        sanPhamList = new ArrayList<>();

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("SanPham");

        // Create adapter and set it to the ListView
        adapter = new SanPhamListAdapter(this, R.layout.activity_hien_thi_san_pham, sanPhamList);
        listView.setAdapter(adapter);

        // Load data from Firebase
        loadSanPhamFromFirebase();

        // Initialize views

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSanPhamDialog();
            }
        });

//        Button btnThemHinhAnh = findViewById(R.id.btnChonHinhAnh);
//        btnThemHinhAnh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openImageChooser();
//            }
//        });

    }


    //hàm load dữ liệu
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
                Log.e("MainActivity", "Failed to read data from Firebase", databaseError.toException());
            }
        });
    }

    //hàm thêm khi bấm nút thêm sp
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
//        Spinner spnLoaiTraiCay = dialogView.findViewById(R.id.spnLoaiTraiCay);

        // Create AlertDialog
        new AlertDialog.Builder(this)
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
                        Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Create SanPham object
                    SanPham sanPham = new SanPham(maSanPham, tenSP, Double.parseDouble(dinhLuong), Double.parseDouble(gia), hinhAnh, moTa, maLoaiTraiCay);

                    // Save to Firebase
                    databaseReference.child(maSanPham).setValue(sanPham)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Sản phẩm đã được thêm", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }


    private void setupBottomNavigation(NavHostFragment navHostFragment) {
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            navController.navigate(item.getItemId());
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;

        int id = menuItem.getItemId();
        if (id == R.id.nav_trang_chu) {
            selectedFragment = new TrangChuFragment();
        } else if (id == R.id.nav_san_pham) {
            selectedFragment = new MainFragment();
        } else if (id == R.id.nav_khach_hang) {
            selectedFragment = new HienThiKhachHangFragment();
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
            drawerLayout.closeDrawers();
        }
        return true;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment); // Đảm bảo ID này khớp với layout của bạn
        transaction.commit();
    }



//    private void openImageChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }

}
