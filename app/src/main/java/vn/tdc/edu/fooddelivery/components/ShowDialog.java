package vn.tdc.edu.fooddelivery.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import vn.tdc.edu.fooddelivery.R;

public class ShowDialog extends Dialog {

    private TextView tvTitle, tvMessage;
    private String title;
    private String message;
    private ShowDialog.DialogComfirmAction dialogComfirmAction;

    public ShowDialog setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    public ShowDialog setMessage(String message) {
        tvMessage.setText(message);
        return this;
    }

    public ShowDialog setOnDialogComfirmAction(ShowDialog.DialogComfirmAction dialogComfirmAction) {
        this.dialogComfirmAction = dialogComfirmAction;
        return this;
    }

    public ShowDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public ShowDialog(@NonNull Context context, String title, String message) {
        super(context);
        this.title = title;
        this.message = message;
        init();
    }

    public ShowDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected ShowDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private ShowDialog init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_show_dialog_popup);

        Window window = getWindow();
        if (window != null) {

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.TOP;
            window.setAttributes(windowAttributes);

            setCancelable(false);

            tvTitle = findViewById(R.id.tvTitle);
            tvMessage = findViewById(R.id.tvMessage);

            Button btnOk = findViewById(R.id.btnOk);

            if (title != null && !title.isEmpty()) {
                tvTitle.setText(title);
            }

            if (message != null && !message.isEmpty()) {
                tvMessage.setText(message);
            }

            btnOk.setOnClickListener(view -> {
                if (dialogComfirmAction != null) {
                    dialogComfirmAction.ok();
                }
            });

        }
        return this;
    }

    public interface DialogComfirmAction {
        void ok();
    }
}
