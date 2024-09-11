package com.btlon.fruitshop.AdapterAdmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.btlon.fruitshop.R;
import com.btlon.fruitshop.models.KhachHang;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;


public class KhachHangAdapter extends ArrayAdapter<KhachHang> {
    private Context context; // Thêm biến này
    private List<KhachHang> objects;
    public KhachHangAdapter(@NonNull Context context, @NonNull List<KhachHang> objects) {
        super(context, 0, objects);
        this.context = context; // Lưu trữ biến context
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Kiểm tra và tạo view nếu chưa có
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_khach_hang, parent, false);
        }

        // Lấy đối tượng KhachHang tại vị trí hiện tại
        KhachHang khachHang = getItem(position);

        // Ánh xạ các thành phần trong layout
        ImageView imgAvatar = convertView.findViewById(R.id.imgAvatar);
        TextView tvTen = convertView.findViewById(R.id.tvTen);
        TextView tvEmail = convertView.findViewById(R.id.tvEmail);
        ImageButton btnOptions = convertView.findViewById(R.id.btnOptions);

        // Thiết lập dữ liệu vào các thành phần
        if (khachHang != null) {
            // Set dữ liệu cho TextView
            tvTen.setText(khachHang.getTen());
            tvEmail.setText(khachHang.getEmail());

            // Sử dụng Picasso để tải hình ảnh từ URL vào ImageView
            Picasso.get()
                    .load(khachHang.getAvatarUrl())
                    .placeholder(R.drawable.ic_avatar_placeholder) // Placeholder trong khi tải
                    .error(R.drawable.ic_avatar_placeholder) // Hình ảnh lỗi nếu không tải được
                    .into(imgAvatar);

            // Thêm xử lý cho nút tùy chọn
            btnOptions.setOnClickListener(v -> showPopupMenu(v, khachHang));
        }

        return convertView;
    }


    //hàm sua khách hàng
    private void showEditDialog(KhachHang khachHang) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_sua_khach_hang, null);

        TextView tvMaKhachHang = view.findViewById(R.id.tvMaKhachHang);
        TextView tvTenDangNhap = view.findViewById(R.id.tvTenDangNhap);
        TextInputEditText etTen = view.findViewById(R.id.etTen);
        TextInputEditText etEmail = view.findViewById(R.id.etEmail);
        TextInputEditText etDiaChi = view.findViewById(R.id.etDiaChi);
        TextInputEditText etSoDienThoai = view.findViewById(R.id.etSoDienThoai);

        tvMaKhachHang.setText("Mã KH: " + khachHang.getMaKhachHang());
        tvTenDangNhap.setText("Tên đăng nhập: " + khachHang.getTenDangNhap());
        etTen.setText(khachHang.getTen());
        etEmail.setText(khachHang.getEmail());
        etDiaChi.setText(khachHang.getDiaChi());
        etSoDienThoai.setText(khachHang.getSoDienThoai());

        builder.setView(view)
                .setTitle("Sửa thông tin khách hàng")
                .setPositiveButton("Lưu", null)
                .setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            // Kiểm tra và cập nhật thông tin
            if (validateInput(etTen, etEmail, etDiaChi, etSoDienThoai)) {
                khachHang.setTen(etTen.getText().toString().trim());
                khachHang.setEmail(etEmail.getText().toString().trim());
                khachHang.setDiaChi(etDiaChi.getText().toString().trim());
                khachHang.setSoDienThoai(etSoDienThoai.getText().toString().trim());

                updateKhachHangToFirebase(khachHang);
                dialog.dismiss();
            }
        });
    }

    private boolean validateInput(TextInputEditText... editTexts) {
        boolean isValid = true;
        for (TextInputEditText editText : editTexts) {
            if (editText.getText().toString().trim().isEmpty()) {
                editText.setError("Trường này không được để trống");
                isValid = false;
            } else {
                editText.setError(null);
            }
        }
        return isValid;
    }

    private void updateKhachHangToFirebase(KhachHang khachHang) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("KhachHang");
        databaseRef.child(khachHang.getMaKhachHang()).setValue(khachHang)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    //ham xoa khach hang ne
    private void deleteKhachHang(KhachHang khachHang) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa Khách Hàng")
                .setMessage("Bạn có chắc chắn muốn xóa khách hàng này?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("KhachHang");
                    databaseRef.child(khachHang.getMaKhachHang()).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Khách hàng đã được xóa thành công!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Lỗi khi xóa khách hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }


    private void showPopupMenu(View view, KhachHang khachHang) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenuInflater().inflate(R.menu.menu_san_pham, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_edit) {
                showEditDialog(khachHang);
                return true;
            } else if (itemId == R.id.action_delete) {
                deleteKhachHang(khachHang);
                return true;
            }
            return false;
        });

        popup.show();
    }

}

