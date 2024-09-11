package com.btlon.fruitshop.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btlon.fruitshop.Adapter.ListFruitsAdapter;
import com.btlon.fruitshop.Adapter.ListFruitsCTAdapter;
import com.btlon.fruitshop.R;
import com.btlon.fruitshop.databinding.ActivityListFruitBinding;
import com.btlon.fruitshop.models.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFruitActivity extends AppCompatActivity {

    ActivityListFruitBinding binding;
    private RecyclerView.Adapter adapterListFruit;
    private String maloai;
    private String searchText;
    private boolean isSearch;
    private   FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityListFruitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        IntnList();
        getIntentExtra();
    }

    private void getIntentExtra() {
        maloai=getIntent().getStringExtra("maLoai");
        searchText= getIntent().getStringExtra("text");
        isSearch= getIntent().getBooleanExtra("isSearch",false);
        binding.backbnt.setOnClickListener(v -> finish());

    }

    private void IntnList() {
        DatabaseReference myref = database.getReference("SanPham");
        binding.progressBar2.setVisibility(View.VISIBLE);
        ArrayList<SanPham> list = new ArrayList<>();
        Query query;
        if(isSearch){
            query= myref.orderByChild("tenSP").startAt(searchText).endAt(searchText+'\uf8ff');
        }
        else {
            query=myref.orderByChild("maLoai").equalTo(maloai);
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(SanPham.class));
                    }
//                    if(list.size()>0){
                        binding.listViewFruit.setLayoutManager(new GridLayoutManager(ListFruitActivity.this,2));
                        adapterListFruit = new ListFruitsCTAdapter(list);
                        binding.listViewFruit.setAdapter(adapterListFruit);
//                    }
                        binding.progressBar2.setVisibility(View.GONE);  // Ẩn pro

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}