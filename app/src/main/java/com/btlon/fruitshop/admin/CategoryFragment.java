package com.btlon.fruitshop.admin;

import android.app.AlertDialog;
import android.os.Bundle;
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


import com.btlon.fruitshop.AdapterAdmin.FruitTypeAdapter;
import com.btlon.fruitshop.R;
import com.btlon.fruitshop.models.LoaiTraiCay;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private ListView fruitTypesListView;
    private FruitTypeAdapter adapter;
    private List<LoaiTraiCay> fruitTypes = new ArrayList<>();
    private DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_category, container, false);

        fruitTypesListView = view.findViewById(R.id.fruitTypesListView);
        adapter = new FruitTypeAdapter(getContext(), fruitTypes);
        fruitTypesListView.setAdapter(adapter);

        // Khởi tạo Firebase Realtime Database
        dbRef = FirebaseDatabase.getInstance().getReference("LoaiTraiCay");

        Button btAddNew = view.findViewById(R.id.addNewType);
        btAddNew.setOnClickListener(v -> addNewTypeFruit());

        loadFruitTypes();

        return view;
    }

    private void loadFruitTypes() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fruitTypes.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LoaiTraiCay fruitType = snapshot.getValue(LoaiTraiCay.class);
                    if (fruitType != null) {
                        fruitTypes.add(fruitType);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }

    ///Hàm Tạo LoaiTraiCay mới
    private void addNewTypeFruit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_add_loai_traicay, null);
        builder.setView(dialogView);

        EditText etMaLoai = dialogView.findViewById(R.id.etMaLoai);
        EditText etTenLoai = dialogView.findViewById(R.id.etTenLoai);
        Button btnAddLoaiTraiCay = dialogView.findViewById(R.id.btnAddLoaiTraiCay);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnAddLoaiTraiCay.setOnClickListener(v -> {
            String maLoai = etMaLoai.getText().toString().trim();
            String tenLoai = etTenLoai.getText().toString().trim();

            if (maLoai.isEmpty() || tenLoai.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng LoaiTraiCay mới
            LoaiTraiCay newLoaiTraiCay = new LoaiTraiCay(maLoai, tenLoai);

            // Lưu vào Firebase Realtime Database
            DatabaseReference loaiTraiCayRef = FirebaseDatabase.getInstance().getReference("LoaiTraiCay");
            loaiTraiCayRef.child(maLoai)
                    .setValue(newLoaiTraiCay)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Thêm loại trái cây thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
