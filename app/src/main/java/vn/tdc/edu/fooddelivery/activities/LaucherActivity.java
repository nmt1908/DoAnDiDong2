package vn.tdc.edu.fooddelivery.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.user.MainActivity;
import vn.tdc.edu.fooddelivery.utils.Authentication;

public class LaucherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_laucher);
        Authentication.setContext(this);

        Intent intent = null;

        if (Authentication.getUserLogin() != null) {
            intent = new Intent(LaucherActivity.this, MainActivity.class);
        } else {
            intent = new Intent(LaucherActivity.this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}