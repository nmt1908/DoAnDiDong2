package vn.tdc.edu.fooddelivery.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import vn.tdc.edu.fooddelivery.enums.Role;
import vn.tdc.edu.fooddelivery.fragments.admin.UsersListFragment;
import vn.tdc.edu.fooddelivery.models.RoleModel;
import vn.tdc.edu.fooddelivery.utils.Authentication;

public class UserFragmentStateAdapter extends FragmentStateAdapter {
    private UsersListFragment fragment;
    private List<RoleModel> listRoles;
    public void setListRoles(List<RoleModel> listRoles) {
        this.listRoles = listRoles;
    }

    public UserFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public UserFragmentStateAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public UserFragmentStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        fragment = new UsersListFragment();

        if (Authentication.getUserLogin().getRolesString().contains(Role.SHIPPER.getName())) {
            fragment.setRoleModel(listRoles.get(position));
        } else {
            if (position > 0) {
                fragment.setRoleModel(listRoles.get(position - 1));
            }
        }

        fragment.fragmentStateAdapter = this;

        return fragment;
    }

    @Override
    public int getItemCount() {
        return listRoles.size() + 1;
    }
}
