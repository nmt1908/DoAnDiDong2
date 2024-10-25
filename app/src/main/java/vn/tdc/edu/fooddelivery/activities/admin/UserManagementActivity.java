package vn.tdc.edu.fooddelivery.activities.admin;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.fragments.admin.UserFormFragment;
import vn.tdc.edu.fooddelivery.fragments.admin.UserManagementFragment;

public class UserManagementActivity extends AbstractActivity {
    private MenuItem menuItemSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order_management);
        createActionBar();

        setFragment(UserManagementFragment.class, R.id.frameLayout, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_order_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            setFragment(UserFormFragment.class,R.id.frameLayout,true);
            menuItemSelected = item;
            disableMenuOptions();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        enableMenuOptions();
        super.onBackPressed();
    }

    public void disableMenuOptions() {
        menuItemSelected.setVisible(false);
    }

    private void enableMenuOptions() {
        if (menuItemSelected != null && !menuItemSelected.isVisible()) {
            menuItemSelected.setVisible(true);
        }
    }
}