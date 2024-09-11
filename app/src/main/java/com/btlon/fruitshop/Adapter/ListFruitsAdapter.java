package com.btlon.fruitshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.btlon.fruitshop.Activity.DetailActivity;
import com.btlon.fruitshop.R;
import com.btlon.fruitshop.models.DanhGia;
import com.btlon.fruitshop.models.SanPham;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class ListFruitsAdapter extends RecyclerView.Adapter<ListFruitsAdapter.viewholder> {
     ArrayList<SanPham> items;
     Context context;

    public ListFruitsAdapter(ArrayList<SanPham> items) {
        this.items = items;
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.traicaychitiet, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        SanPham sanPham = items.get(position);
        holder.tensptxt.setText(sanPham.getTenSP());
        holder.pricetxt.setText(String.valueOf(sanPham.getGia()));

        Glide.with(context)
                .load(sanPham.getHinhAnh())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);
        calculateAverageRating(sanPham.getMaSanPham(), holder.soSao);
        holder.itemView.setOnClickListener(v -> {
            Intent intent= new Intent(context, DetailActivity.class);
            intent.putExtra("tc", (Serializable) items.get(position));
            context.startActivity(intent);
        });
    }
    private void calculateAverageRating(String productId, TextView ratingTextView) {
        DatabaseReference danhGiaRef = FirebaseDatabase.getInstance().getReference("DanhGia");
        danhGiaRef.orderByChild("maSanPham").equalTo(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                double sum = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DanhGia danhGia = snapshot.getValue(DanhGia.class);
                    if (danhGia != null) {
                        sum += danhGia.getSoSao();
                        count++;
                    }
                }

                double averageRating = (count > 0) ? sum / count : 0;
                ratingTextView.setText(String.format("Số sao: %.1f", averageRating));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("CartAdapter", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size(); // Cập nhật để trả về số lượng phần tử thực tế
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tensptxt, pricetxt,soSao;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tensptxt = itemView.findViewById(R.id.tensptxt);
            pricetxt = itemView.findViewById(R.id.pricetxt);
            pic = itemView.findViewById(R.id.pic);
            soSao = itemView.findViewById(R.id.sosao);
        }
    }
}
