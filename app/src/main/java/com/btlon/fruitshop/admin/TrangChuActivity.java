package com.btlon.fruitshop.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.btlon.fruitshop.Activity.HomeActivity;
import com.btlon.fruitshop.Activity.LoginActivity;
import com.btlon.fruitshop.Activity.RegisterActivity;
import com.btlon.fruitshop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TrangChuActivity extends AppCompatActivity {
//    private TextView trangchu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set up BottomNavigationView with NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

//        trangchu = findViewById(R.id.trangchutxt);
//        trangchu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Chuyển sang SignInActivity
//                Intent intent = new Intent(TrangChuActivity.this, HomeActivity.class);
//                startActivity(intent);
//            }
//        });

    }


}