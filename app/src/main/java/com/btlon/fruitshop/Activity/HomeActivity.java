//package com.btlon.fruitshop.Activity;
//
//import android.os.Bundle;
//import android.view.View;
//
//import com.btlon.fruitshop.Adapter.ListFruitsAdapter;
//import com.btlon.fruitshop.databinding.ActivityHomeBinding;
//import com.btlon.fruitshop.models.SanPham;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class HomeActivity extends AppCompatActivity {
//
//    private ActivityHomeBinding  binding;
//    FirebaseDatabase database;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding= ActivityHomeBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        inittraicay();
//
//    }
//    protected void inittraicay(){
//
//        DatabaseReference myref= database.getReference("SanPham");
//        binding.progressBarbanchay.setVisibility(View.VISIBLE);
//        ArrayList<SanPham> list =new ArrayList<>();
//
//        myref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists())
//                {
//                    for(DataSnapshot issue: snapshot.getChildren())
//                    {
//                        list.add(issue.getValue(SanPham.class));
//                    }
//                    binding.banchay.setLayoutManager((new LinearLayoutManager(HomeActivity.this,LinearLayoutManager.HORIZONTAL,false)));
//                    RecyclerView.Adapter<ListFruitsAdapter.viewholder> adapter=new ListFruitsAdapter(list);
//                    binding.banchay.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
//}
package com.btlon.fruitshop.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.btlon.fruitshop.Adapter.ListFruitsAdapter;
import com.btlon.fruitshop.admin.TrangChuActivity;
import com.btlon.fruitshop.databinding.ActivityHomeBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btlon.fruitshop.models.DanhGia;
import com.btlon.fruitshop.models.SanPham;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private FirebaseDatabase database;  // Sửa lại biến database
    private FirebaseAuth mAuth;
    private TextView tenhttxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();  // Khởi tạo FirebaseDatabase
        FirebaseUser currentUser = mAuth.getCurrentUser();



        if (currentUser == null) {
            // Chưa đăng nhập
            binding.loginbnt.setVisibility(View.VISIBLE);
            binding.logoutbnt.setVisibility(View.GONE);
            binding.tenhttxt.setVisibility(View.GONE);

        } else {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("KhachHang").child(userId).child("ten");
            userRef.get().addOnCompleteListener(task -> {

                String ten = task.getResult().getValue(String.class);

                binding.tenhttxt.setText(ten);


            });
            // Đã đăng nhập
            binding.loginbnt.setVisibility(View.GONE);
            binding.logoutbnt.setVisibility(View.VISIBLE);
            binding.tenhttxt.setVisibility(View.VISIBLE);
        }
        if (currentUser != null) {
            String maR = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("KhachHang").child(maR).child("maRole");

            // Lấy giá trị maRole từ Firebase
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String maRole = task.getResult().getValue(String.class);

                    // Kiểm tra nếu maRole không null
                    if (maRole != null) {
                        binding.tenhttxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Kiểm tra giá trị của maRole và điều hướng đến trang tương ứng
                                if (maRole.equals("2")) {
                                    // Điều hướng đến trang khác nếu maRole = "2"
                                    Intent intent = new Intent(HomeActivity.this, TrangChuActivity.class);
                                    startActivity(intent);
                                } else {
                                    // Điều hướng đến trang inforActivity nếu maRole khác "2"
                                    Intent intent = new Intent(HomeActivity.this, inforActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        // Xử lý khi maRole là null, có thể hiển thị thông báo lỗi hoặc điều hướng
                        Toast.makeText(HomeActivity.this, "Không lấy được thông tin maRole.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý khi task không thành công, có thể hiển thị thông báo lỗi
                    Toast.makeText(HomeActivity.this, "Lỗi khi truy xuất dữ liệu từ Firebase.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Nếu người dùng chưa đăng nhập, điều hướng về màn hình đăng nhập
            Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        inittraicaybanchay();
        inittraicay();
        search();
        servi();
    }

    private void servi() {
        binding.logoutbnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this,HomeActivity.class));

                // Chuyển hướng người dùng về màn hình đăng nhập

            }
        });
        binding.loginbnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();

            }
        });
        binding.giohangbnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLoginAndProceed();
            }
        });

        binding.xemthemtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HomeActivity.this, ListFruitActivity.class);
                startActivity(intent);

            }
        });

    }
    private void checkLoginAndProceed() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Nếu chưa đăng nhập, chuyển hướng đến màn hình đăng nhập
            Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else {
            // Nếu đã đăng nhập, chuyển hướng đến màn hình giỏ hàng
            Intent cartIntent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(cartIntent);
        }
    }
    private void search(){
        binding.searchbnt.setOnClickListener(v -> {
            String text= binding.searchtxt.getText().toString();
            if(!text.isEmpty()){
                Intent intent= new Intent(HomeActivity.this, ListFruitActivity.class);
                intent.putExtra("text",text);
                intent.putExtra("isSearch",true);
                startActivity(intent);
            }
        });
    }
    protected void inittraicaybanchay() {
        if (database != null) {
            DatabaseReference sanPhamRef = database.getReference("SanPham");
            DatabaseReference danhGiaRef = database.getReference("DanhGia");
            binding.progressBarbanchay.setVisibility(View.VISIBLE);

            ArrayList<SanPham> listSanPham = new ArrayList<>();
            Map<String, Double> soSaoTrungBinh = new HashMap<>();

            // Lấy danh sách sản phẩm
            sanPhamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshotSanPham) {
                    if (snapshotSanPham.exists()) {
                        for (DataSnapshot spSnapshot : snapshotSanPham.getChildren()) {
                            SanPham sanPham = spSnapshot.getValue(SanPham.class);
                            if (sanPham != null) {
                                listSanPham.add(sanPham);
                            }
                        }

                        // Lấy đánh giá và tính số sao trung bình cho từng sản phẩm
                        danhGiaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotDanhGia) {
                                // Tính số sao trung bình cho mỗi sản phẩm
                                for (SanPham sanPham : listSanPham) {
                                    int tongSoSao = 0;
                                    int soDanhGia = 0;

                                    for (DataSnapshot danhGiaSnapshot : snapshotDanhGia.getChildren()) {
                                        DanhGia danhGia = danhGiaSnapshot.getValue(DanhGia.class);
                                        if (danhGia != null && danhGia.getMaSanPham().equals(sanPham.getMaSanPham())) {
                                            tongSoSao += danhGia.getSoSao();
                                            soDanhGia++;
                                        }
                                    }

                                    double trungBinhSao = (soDanhGia == 0) ? 0 : (double) tongSoSao / soDanhGia;
                                    soSaoTrungBinh.put(sanPham.getMaSanPham(), trungBinhSao);
                                }

                                // Sắp xếp danh sách sản phẩm theo số sao trung bình
                                listSanPham.sort((sp1, sp2) -> {
                                    double sao1 = soSaoTrungBinh.getOrDefault(sp1.getMaSanPham(), 0.0);
                                    double sao2 = soSaoTrungBinh.getOrDefault(sp2.getMaSanPham(), 0.0);
                                    return Double.compare(sao2, sao1); // Sắp xếp giảm dần
                                });

                                // Lấy 5 sản phẩm có số sao trung bình cao nhất
                                List<SanPham> top5SanPham = listSanPham.subList(0, Math.min(5, listSanPham.size()));

                                // Hiển thị sản phẩm trên RecyclerView
                                binding.banchay.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
                                RecyclerView.Adapter<ListFruitsAdapter.viewholder> adapter = new ListFruitsAdapter(new ArrayList<>(top5SanPham));
                                binding.banchay.setAdapter(adapter);

                                // Ẩn progress bar sau khi hoàn tất
                                binding.progressBarbanchay.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("HomeActivity", "DatabaseError: " + error.getMessage());
                                binding.progressBarbanchay.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Log.d("HomeActivity", "Không có sản phẩm nào.");
                        binding.progressBarbanchay.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("HomeActivity", "DatabaseError: " + error.getMessage());
                    binding.progressBarbanchay.setVisibility(View.GONE);
                }
            });
        } else {
            Log.e("HomeActivity", "FirebaseDatabase instance is null");
        }
    }

    protected void inittraicay() {
        if (database != null) {
            DatabaseReference myref = database.getReference("SanPham");
            binding.progressBar.setVisibility(View.VISIBLE);
            ArrayList<SanPham> list = new ArrayList<>();

            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            list.add(issue.getValue(SanPham.class));
                        }
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter<ListFruitsAdapter.viewholder> adapter = new ListFruitsAdapter(list);
                        binding.recyclerView.setAdapter(adapter);
                    }
                    binding.progressBar.setVisibility(View.GONE);  // Ẩn progress bar sau khi tải xong
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("HomeActivity", "DatabaseError: " + error.getMessage());
                    binding.progressBar.setVisibility(View.GONE);  // Ẩn progress bar khi xảy ra lỗi
                }
            });
        } else {
            Log.e("HomeActivity", "FirebaseDatabase instance is null");
        }
    }



}
