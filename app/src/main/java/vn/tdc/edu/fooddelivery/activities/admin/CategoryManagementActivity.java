package vn.tdc.edu.fooddelivery.activities.admin;

import android.os.Bundle;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.fragments.admin.CategoriesListFragment;

public class CategoryManagementActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_category_management);
        createActionBar();

        setFragment(CategoriesListFragment.class, R.id.frameLayout, false);
    }
}