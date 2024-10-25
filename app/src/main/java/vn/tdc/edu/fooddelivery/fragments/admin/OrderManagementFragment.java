package vn.tdc.edu.fooddelivery.fragments.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.adapters.OrderFragmentStateAdapter;
import vn.tdc.edu.fooddelivery.enums.Role;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.utils.Authentication;

public class OrderManagementFragment extends AbstractFragment {
    private TabLayout tabLayout;
    private CharSequence[] listStatus;
    private int LIST_STATUS_LENGTH = 4;
    private ViewPager2 viewPager2;
    private OrderFragmentStateAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_status_tab, container, false);

        if (listStatus == null) {
            listStatus = new CharSequence[LIST_STATUS_LENGTH];
            listStatus[0] = "Đơn hàng mới";
            listStatus[1] = "Đang giao hàng";
            listStatus[2] = "Giao thành công";
            listStatus[3] = "Đơn hàng đã huỷ";

            if (Authentication.getUserLogin().getRolesString().contains(Role.SHIPPER.getName())) {
                listStatus = Arrays.copyOfRange(listStatus, 1, listStatus.length, CharSequence[].class);
            }
        }

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.page);

        createTabLayoutForOrderStatus();

        return view;
    }

    private void createTabLayoutForOrderStatus() {
        adapter = new OrderFragmentStateAdapter(OrderManagementFragment.this);
        adapter.setListStatus(listStatus);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                adapter.setListStatus(listStatus);
                tab.setText(listStatus[position]);
            }
        }).attach();
    }
}