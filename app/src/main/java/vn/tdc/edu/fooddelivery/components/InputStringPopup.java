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

public class InputStringPopup extends Dialog implements View.OnClickListener {
    private TextView tvTitle;
    private EditText edString;
    private Button btnCancel;
    private Button btnOk;
    private DialogAssignDialogAction dialogComfirmAction;

    public InputStringPopup setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    public InputStringPopup setPlaceHolder(String placeHolder) {
        edString.setHint(placeHolder);
        return this;
    }

    public InputStringPopup setText(String placeHolder) {
        edString.setText(placeHolder);
        return this;
    }

    public InputStringPopup setOnInputStringDialogAction(DialogAssignDialogAction dialogComfirmAction) {
        this.dialogComfirmAction = dialogComfirmAction;
        return this;
    }

    public InputStringPopup(@NonNull Context context) {
        super(context);
        init();
    }

    public InputStringPopup(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected InputStringPopup(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private InputStringPopup init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_input_string_popup);

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
            edString = findViewById(R.id.edString);
            btnOk.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
        }
        return this;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnOk) {
            String inputText = edString.getText().toString();
            dialogComfirmAction.ok(inputText);
        } else if (view.getId() == R.id.btnCancel) {
            dialogComfirmAction.cancel();
        }
    }

    public interface DialogAssignDialogAction {
        void cancel();
        void ok(String inputText);
    }
}
