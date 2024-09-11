package com.btlon.fruitshop.AdapterAdmin;//package com.btlon.bantraicay.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.btlon.fruitshop.R;
import com.btlon.fruitshop.models.SanPham;
import com.google.common.util.concurrent.ForwardingListeningExecutorService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SanPhamListAdapter extends ArrayAdapter<SanPham> {
    private Context context;
    private int resource;
    private List<SanPham> sanPhamList; //khai báo biến sanPhamList

    public SanPhamListAdapter(Context context, int resource, List<SanPham> sanPhamList) {
        super(context, resource, sanPhamList);
        this.context = context;
        this.resource = resource;
        this.sanPhamList = sanPhamList; // Gán danh sách sản phẩm
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder();
//            ImageView imgHinhAnh = convertView.findViewById(R.id.imgHinhAnh);
            holder.imgHinhAnh = convertView.findViewById(R.id.imgHinhAnh);
            holder.tvTenSP = convertView.findViewById(R.id.tvTenSP);
            holder.tvMoTa = convertView.findViewById(R.id.tvMoTa);
            holder.tvGia = convertView.findViewById(R.id.tvGia);
            holder.imgMenu = convertView.findViewById(R.id.imgMenu);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SanPham sanPham = getItem(position);
        if (sanPham != null) {
            holder.tvTenSP.setText("Tên sản phẩm: " + sanPham.getTenSP());
            holder.tvMoTa.setText("Mô tả: " + sanPham.getMoTa());
            holder.tvGia.setText("Giá: " + sanPham.getGia() + " VND");

            // Load hình ảnh sử dụng Glide hoặc thư viện khác
            Picasso.get()
                    .load(sanPham.getHinhAnh())
                    .into(holder.imgHinhAnh);

        }

         //Thiết lập sự kiện cho nút ba chấm
        holder.imgMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, holder.imgMenu);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_san_pham, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> handleMenuItemClick (item, position));
            popup.show();
        });

        return convertView;
    }

    private boolean handleMenuItemClick(MenuItem item, int position) {
        // Xử lý sự kiện cho các mục trong menu
        int itemId = item.getItemId();

        if (itemId == R.id.action_edit) {
            editSanPham(position);
            return true;
        } else if (itemId == R.id.action_delete) {
            deleteSanPham(position);
            return true;
        } else {
            return false;
        }
    }

    private void editSanPham(int position) {
        // Lấy sản phẩm cần sửa
        SanPham sanPham = getItem(position);
        if (sanPham == null) {
            return;
        }

        // Hiển thị hộp thoại sửa sản phẩm
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_sua_san_pham, null);
        builder.setView(dialogView);

        TextView tvMaSanPham = dialogView.findViewById(R.id.etMaSanPham);
        EditText etTenSP = dialogView.findViewById(R.id.etTenSP);
        EditText etMoTa = dialogView.findViewById(R.id.etMoTa);
        EditText etGia = dialogView.findViewById(R.id.etGia);

        // Đặt giá trị ban đầu cho các trường
        tvMaSanPham.setText("Mã sản phẩm: " + sanPham.getMaSanPham());
        etTenSP.setText(sanPham.getTenSP());
        etMoTa.setText(sanPham.getMoTa());
        etGia.setText(String.valueOf(sanPham.getGia()));

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Cập nhật thông tin sản phẩm
            sanPham.setTenSP(etTenSP.getText().toString());
            sanPham.setMoTa(etMoTa.getText().toString());
            sanPham.setGia(Double.parseDouble(etGia.getText().toString()));

            // Cập nhật thông tin trong Firebase
            updateSanPhamInDatabase (sanPham);

            // Thông báo adapter cập nhật
            notifyDataSetChanged();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteSanPham(int position) {
        // Lấy sản phẩm cần xóa
        SanPham sanPham = getItem(position);
        if (sanPham == null || sanPham.getMaSanPham() == null) {
            Toast.makeText(context, "Sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Xác nhận việc xóa sản phẩm
        new AlertDialog.Builder(context)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Xóa sản phẩm khỏi danh sách
                    sanPhamList.remove(position);

                    // Xóa sản phẩm khỏi Firebase Realtime Database
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                    dbRef.child("SanPham").child(sanPham.getMaSanPham()).removeValue()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Thông báo thành công
                                    Toast.makeText(context, "Sản phẩm đã được xóa", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Xử lý lỗi
                                    Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                                }
                            });

                    // Thông báo adapter cập nhật
                    notifyDataSetChanged();
                })
                .setNegativeButton("Không", null)
                .show();
    }


    private void updateSanPhamInDatabase(SanPham sanPham) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("SanPham");

        // Update the specific product identified by its ID
        dbRef.child(sanPham.getMaSanPham()).setValue(sanPham)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // thông ba thành công
                        Toast.makeText(context, "Sản phẩm đã được cập nhật", Toast.LENGTH_SHORT).show();
                    } else {
                        // báo lỗi nếu có
                        Toast.makeText(context, "Lỗi khi cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static class ViewHolder { //lớp khai báo toàn bộ nằm ở đây
        ImageView imgHinhAnh;
        TextView tvTenSP;
        TextView tvMoTa;
        TextView tvGia;

        TextView tvMaSanPham;

        ImageView imgMenu;
    }
}

