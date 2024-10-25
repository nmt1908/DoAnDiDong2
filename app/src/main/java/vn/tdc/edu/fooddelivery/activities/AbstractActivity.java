package vn.tdc.edu.fooddelivery.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.utils.ImageUploadUtils;

public abstract class AbstractActivity extends AppCompatActivity {
    private AbstractFragment currentFragment;
    private AbstractFragment prevFragment;
    private Toolbar toolbar;

    public AbstractFragment getCurrentFragment() {
        return currentFragment;
    }

    public AbstractFragment getPrevFragment() {
        return prevFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    protected void createActionBar() {
        setTitle(getIntent().getAction());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void showMessageDialog(String message) {
        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("Message")
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }

    public void switchActivity(Class<?> targetActivity, String action) {
        Intent intent = new Intent(this, targetActivity);
        intent.setAction(action);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public <T> T setFragment(Class<T> tClass, int layout, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AbstractFragment fragment = (AbstractFragment) fragmentManager.findFragmentByTag(tClass.getSimpleName());
        try {
            if (fragment == null) {
                fragment = (AbstractFragment) tClass.newInstance();
            }

            transaction.replace(layout, fragment, tClass.getSimpleName());

            if (fragmentManager.findFragmentByTag(tClass.getSimpleName()) == null && addToBackStack) {
                transaction.addToBackStack(null);
            }

            transaction.commit();

            prevFragment = currentFragment;
            currentFragment = fragment;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        return (T) fragment;
    }

    public void openFeedbackDialog(int gravity, boolean cancelable) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_confirm_dialog_popup);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(cancelable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length == grantResults.length) {
            for (int grant : grantResults) {
                if (grant == PackageManager.PERMISSION_DENIED) {
                    return;
                }
            }

            if (requestCode == ImageUploadUtils.REQ_CAMERA) {
                ImageUploadUtils.getInstance().takePhotoAction(this);
            } else if (requestCode == ImageUploadUtils.REQ_READ_EXTERNAL_STORAGE) {
                ImageUploadUtils.getInstance().pickImageAction(this);
            }
        }
    }
}
