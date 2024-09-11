package com.btlon.fruitshop.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.btlon.fruitshop.ActivityLichSuMuaHang;
import com.btlon.fruitshop.R;
import com.btlon.fruitshop.models.KhachHang;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class inforActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, addressEditText, phoneEditText;
    private ImageView avatarImageView;
    private Button updateProfileButton, viewOrderHistoryButton;
    private DatabaseReference userRef;
    private TextView back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);

        // Initialize views
        avatarImageView = findViewById(R.id.avatarImageView);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        updateProfileButton = findViewById(R.id.updateProfileButton);
        viewOrderHistoryButton = findViewById(R.id.viewOrderHistoryButton); // New Button for Order History

        // Get the currently authenticated user
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("KhachHang").child(userId);

        // Load the user profile data
        loadUserProfile();
        // Set up the update button click listener
        updateProfileButton.setOnClickListener(v -> updateUserProfile());

        // Set up the view order history button click listener
        viewOrderHistoryButton.setOnClickListener(v -> {
            // Navigate to OrderHistoryActivity (create this activity separately)
            Intent intent = new Intent(inforActivity.this, ActivityLichSuMuaHang.class);
            startActivity(intent);
        });
    }

    private void loadUserProfile() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KhachHang khachHang = dataSnapshot.getValue(KhachHang.class);
                if (khachHang != null) {
                    // Populate the fields with user data
                    nameEditText.setText(khachHang.getTen());
                    emailEditText.setText(khachHang.getEmail());
                    addressEditText.setText(khachHang.getDiaChi());
                    phoneEditText.setText(khachHang.getSoDienThoai());

                    // Load avatar with Glide
                    Glide.with(inforActivity.this)
                            .load(khachHang.getAvatarUrl())
                            .into(avatarImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(inforActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile() {
        // Get updated information from input fields
        String updatedName = nameEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();
        String updatedAddress = addressEditText.getText().toString();
        String updatedPhone = phoneEditText.getText().toString();

        // Update the user info in Firebase
        KhachHang updatedKhachHang = new KhachHang();
        updatedKhachHang.setTen(updatedName);
        updatedKhachHang.setEmail(updatedEmail);
        updatedKhachHang.setDiaChi(updatedAddress);
        updatedKhachHang.setSoDienThoai(updatedPhone);

        userRef.updateChildren(updatedKhachHang.toMap()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(inforActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(inforActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
