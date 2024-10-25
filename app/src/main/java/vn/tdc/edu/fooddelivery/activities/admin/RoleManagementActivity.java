package vn.tdc.edu.fooddelivery.activities.admin;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.fragments.admin.RoleListFragment;
import vn.tdc.edu.fooddelivery.models.RoleModel;

public class RoleManagementActivity extends AbstractActivity {
    private List<RoleModel> listRoles;

    private RecyclerView recyclerViewRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_role_management);
        createActionBar();
        this.setFragment(RoleListFragment.class, R.id.frameLayout, false);
    }
}
