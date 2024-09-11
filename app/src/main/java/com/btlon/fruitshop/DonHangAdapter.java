package com.btlon.fruitshop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btlon.fruitshop.models.ChiTietDonHang;
import com.btlon.fruitshop.models.DonHang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.ViewHolder> {
    private FirebaseAuth mAuth;

    private List<DonHang> donHangList;

    public DonHangAdapter(List<DonHang> donHangList) {
        this.donHangList = donHangList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_lich_su_mua_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);
        holder.tvOrderName.setText("Mã ĐH: " + donHang.getMaDonHang());
        holder.tvOrderCustomerName.setText(donHang.getMaKhachHang()); // Hiển thị tên khách hàng (hoặc sử dụng thông tin từ model khác)
        String orderDateString = donHang.getNgayTao();
        String formattedDate = null;
        try {
            formattedDate = convertDate(orderDateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.tvOrderDate.setText(formattedDate);
        holder.tvOrderTotal.setText("Tổng Tiền:  " + String.format(Locale.getDefault(), "%.0f", donHang.getTongTien()) + " VND");
        holder.tvOrderStatus.setText(donHang.getTrangThai());
        holder.itemView.setOnClickListener(v -> {
            try {
                showOrderDetailsDialog(holder.itemView.getContext(), donHang.getMaDonHang(),
                        donHang.getMaKhachHang(), convertDate(orderDateString), donHang.getTongTien());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderCustomerName, tvOrderDate, tvOrderName, tvOrderTotal, tvOrderStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderCustomerName = itemView.findViewById(R.id.tvOrderCustomerName);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderName = itemView.findViewById(R.id.tvOrderName);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }

    private String convertDate(String date) throws ParseException {
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date orderDate = iso8601Format.parse(date);
        return displayFormat.format(orderDate);
    }

    private void showOrderDetailsDialog(Context context, String maDonHang, String tenKhachHang, String orderDate, double totalPay) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_lich_su_don_hang, null);
        builder.setView(dialogView);

        TextView tvCusName = dialogView.findViewById(R.id.tvOrderCustomerName);
        TextView tvOrderDate = dialogView.findViewById(R.id.tvOrderDate);
        TextView tvTotalPay = dialogView.findViewById(R.id.tvOrderTotal);
        ListView listViewSanPham = dialogView.findViewById(R.id.listViewSanPham);

        if (mAuth.getCurrentUser() != null) {
            // Người dùng đã đăng nhập
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("KhachHang").child(userId).child("ten");
            userRef.get().addOnCompleteListener(task -> {

                String ten = task.getResult().getValue(String.class);

                tvCusName.setText("Tên KH: " + ten);


            });

        }

        tvOrderDate.setText("Ngày Tạo: " + orderDate);
        tvTotalPay.setText("Tổng Tiền: " + String.format(Locale.getDefault(), "%.0f VND", totalPay));

        DatabaseReference chiTietDonHangRef = FirebaseDatabase.getInstance().getReference("ChiTietDonHang");

        // Tạo danh sách chứa chi tiết đơn hàng
        List<ChiTietDonHang> chiTietDonHangList = new ArrayList<>();

        // Truy vấn chi tiết đơn hàng từ Firebase
        chiTietDonHangRef.orderByChild("maDonHang").equalTo(maDonHang).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChiTietDonHang chiTietDonHang = snapshot.getValue(ChiTietDonHang.class);
                    if (chiTietDonHang != null) {
                        // Thêm chi tiết đơn hàng vào danh sách
                        chiTietDonHangList.add(chiTietDonHang);
                    }
                }

                // Tạo adapter và gán cho ListView
                SanPhamAdapter adapter = new SanPhamAdapter(context, R.layout.items_san_pham, chiTietDonHangList);
                listViewSanPham.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

}
