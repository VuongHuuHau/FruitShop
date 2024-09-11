package com.btlon.fruitshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

public class ListFruitsCTAdapter extends RecyclerView.Adapter<ListFruitsCTAdapter.viewholder> {

     ArrayList<SanPham> items;
     Context context;

    public ListFruitsCTAdapter(ArrayList<SanPham> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ListFruitsCTAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_list_food,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ListFruitsCTAdapter.viewholder holder, int position) {
        SanPham sanPham = items.get(position);
        holder.tensptxt.setText(sanPham.getTenSP());
        holder.pricetxt.setText(String.valueOf(sanPham.getGia()));

        Glide.with(context)
                .load(sanPham.getHinhAnh())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent= new Intent(context, DetailActivity.class);
            intent.putExtra("tc", (Serializable) items.get(position));
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView tensptxt, pricetxt,themsptxt;
        ImageView pic;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            tensptxt = itemView.findViewById(R.id.tensptxt);
            pricetxt = itemView.findViewById(R.id.pricetxt);
            pic = itemView.findViewById(R.id.pic);
            themsptxt= itemView.findViewById(R.id.themsptxt);

        }
    }
}