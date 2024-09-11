package com.btlon.fruitshop.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.btlon.fruitshop.AdapterAdmin.OrderViewPagerAdapter;
import com.btlon.fruitshop.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrderFragment extends Fragment {
    private TabLayout tab;
    private ViewPager2 viewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tab = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager2);

        OrderViewPagerAdapter orderViewPagerAdapter = new OrderViewPagerAdapter(requireActivity());
        viewPager.setAdapter(orderViewPagerAdapter);

        // Liên kết ViewPager2 với TabLayout
        new TabLayoutMediator(tab, viewPager,
                (tab, position) -> tab.setText(orderViewPagerAdapter.getPageTitle(position))
        ).attach();
    }

    public void showErrorMessage() {
        TextView errorTextView = getView().findViewById(R.id.error);
        if (errorTextView != null) {
            errorTextView.setVisibility(View.VISIBLE);
        }
    }

    public void hideErrorMessage() {
        TextView errorTextView = getView().findViewById(R.id.error);
        if (errorTextView != null) {
            errorTextView.setVisibility(View.GONE);
        }
    }
}
