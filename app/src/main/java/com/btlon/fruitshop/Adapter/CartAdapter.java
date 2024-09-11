
package com.btlon.fruitshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.btlon.fruitshop.R;
import com.btlon.fruitshop.models.CartItem;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<CartItem> cartItems;
    private Context context;

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
    public List<CartItem> getItems() {
        return cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.tenSP.setText(cartItem.getProductName());
        holder.giaSP.setText("VND " + cartItem.getProductPrice());
        holder.soLuongSP.setText(cartItem.getQuantity()+"");
        holder.totaltxt.setText(cartItem.getQuantity()*cartItem.getProductPrice()+" VND");
        // Sử dụng Glide để tải hình ảnh
        Glide.with(context)
                .load(cartItem.getImg())
                .into(holder.hinhAnhSP);

        // Xử lý sự kiện khi nhấn nút xóa
        holder.spTtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tăng số lượng sản phẩm hiện tại
                double currentQuantity = cartItem.getQuantity();
                double newQuantity = Math.round((currentQuantity + 0.1) * 10.0) / 10.0;
                double newTotalPrice = newQuantity * cartItem.getProductPrice();

                // Cập nhật lại số lượng và tổng tiền trong Firebase
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Carts").child(userId).child(cartItem.getProductId());

                Map<String, Object> updates = new HashMap<>();
                updates.put("quantity", newQuantity);
                updates.put("totalPrice", newTotalPrice);

                cartRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Cập nhật lại dữ liệu trong danh sách và giao diện
                            cartItem.setQuantity(newQuantity);
                            holder.soLuongSP.setText(newQuantity + "");
                            holder.totaltxt.setText("VND " + newTotalPrice);

                            // Thông báo cho RecyclerView để cập nhật hiển thị
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        holder.spGtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tăng số lượng sản phẩm hiện tại
                double currentQuantity = cartItem.getQuantity();
                double newQuantity = Math.round((currentQuantity - 0.1) * 10.0) / 10.0;
                double newTotalPrice = newQuantity * cartItem.getProductPrice();

                // Cập nhật lại số lượng và tổng tiền trong Firebase
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Carts").child(userId).child(cartItem.getProductId());

                Map<String, Object> updates = new HashMap<>();
                updates.put("quantity", newQuantity);
                updates.put("totalPrice", newTotalPrice);

                cartRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Cập nhật lại dữ liệu trong danh sách và giao diện
                            cartItem.setQuantity(newQuantity);
                            holder.soLuongSP.setText(newQuantity + "");
                            holder.totaltxt.setText("VND " + newTotalPrice);

                            // Thông báo cho RecyclerView để cập nhật hiển thị
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        // Cài đặt thông tin sản phẩm vào ViewHolder

        holder.itemView.setOnClickListener(v -> {
            showDeleteConfirmDialog(cartItem);
        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenSP, giaSP, soLuongSP,spTtxt,spGtxt,totaltxt;
        ImageView hinhAnhSP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenSP = itemView.findViewById(R.id.tensptxt);
            giaSP = itemView.findViewById(R.id.pricetxt);
            soLuongSP = itemView.findViewById(R.id.numtxt);
            hinhAnhSP = itemView.findViewById(R.id.pic);
            spTtxt = itemView.findViewById(R.id.spTtxt);
            spGtxt = itemView.findViewById(R.id.spGtxt);
            totaltxt=itemView.findViewById(R.id.totaltxt);


        }
         
    }
    private void showDeleteConfirmDialog(CartItem item) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    deleteCartItem(item);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteCartItem(CartItem item) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Carts").child(userId);

            cartRef.child(item.getProductId()).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Sản phẩm đã được xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                    cartItems.remove(item);
                    notifyDataSetChanged(); // Cập nhật giao diện
                } else {
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Xóa thất bại";
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }



}


