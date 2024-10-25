package vn.tdc.edu.fooddelivery.components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import vn.tdc.edu.fooddelivery.R;

public class ChangePasswordPopup extends Dialog implements View.OnClickListener {
    private TextView tvTitle, oldPasswordLabel, newPasswordLabel;
    private EditText edOldPassword, edNewPassword;
    private Button btnCancel;
    private Button btnOk;
    private DialogPopupAction dialogComfirmAction;

    public ChangePasswordPopup setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    public ChangePasswordPopup setOldPasswordLabel(String label) {
        oldPasswordLabel.setText(label);
        return this;
    }

    public ChangePasswordPopup setNewPasswordLabel(String label) {
        newPasswordLabel.setText(label);
        return this;
    }

    public ChangePasswordPopup setEditOldPasswordHint(String hint) {
        edOldPassword.setHint(hint);
        return this;
    }

    public ChangePasswordPopup setEditNewPasswordHint(String hint) {
        edNewPassword.setHint(hint);
        return this;
    }

    public ChangePasswordPopup setEditOldPasswordError(String error) {
        edOldPassword.setError(error);
        return this;
    }

    public ChangePasswordPopup setEditNewPasswordError(String error) {
        edNewPassword.setError(error);
        return this;
    }

    public ChangePasswordPopup setOnInputDialogAction(DialogPopupAction dialogComfirmAction) {
        this.dialogComfirmAction = dialogComfirmAction;
        return this;
    }

    public ChangePasswordPopup(@NonNull Context context) {
        super(context);
        init();
    }

    public ChangePasswordPopup(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected ChangePasswordPopup(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private ChangePasswordPopup init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_change_password_popup);

        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);

            setCancelable(false);

            tvTitle = findViewById(R.id.tvTitle);
            btnCancel = findViewById(R.id.btnCancel);
            btnOk = findViewById(R.id.btnOk);
            edOldPassword = findViewById(R.id.edOldPassword);
            edNewPassword = findViewById(R.id.edNewPassword);
            oldPasswordLabel = findViewById(R.id.oldPasswordLabel);
            newPasswordLabel = findViewById(R.id.newPasswordLabel);
            btnOk.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
        }
        return this;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnOk) {
            String oldPassword = edOldPassword.getText().toString();
            String newPassword = edNewPassword.getText().toString();
            dialogComfirmAction.ok(oldPassword, newPassword);
        } else if (view.getId() == R.id.btnCancel) {
            dialogComfirmAction.cancel();
        }
    }

    public interface DialogPopupAction {
        void cancel();
        void ok(String oldPassword, String newPassword);
    }
}
