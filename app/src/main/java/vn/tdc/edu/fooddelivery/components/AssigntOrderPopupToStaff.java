package vn.tdc.edu.fooddelivery.components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.api.UserAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.enums.OrderStatus;
import vn.tdc.edu.fooddelivery.enums.Role;
import vn.tdc.edu.fooddelivery.models.OrderModel;
import vn.tdc.edu.fooddelivery.models.UserModel;

public class AssigntOrderPopupToStaff extends Dialog {
    private String title;
    private String message;
    private Spinner spinerShippers;
    private TextView tvTitle;
    private TextView tvMessage;
    private Button btnCancel;
    private Button btnOk;
    private ArrayAdapter<UserModel> adapter;
    private DialogAssignDialogAction dialogComfirmAction;

    private UserModel shipperSelected;

    private List<UserModel> shipperList = new ArrayList<>();

    public AssigntOrderPopupToStaff setTitle(String title) {
        this.title = title;
        return this;
    }

    public AssigntOrderPopupToStaff setMessage(String message) {
        this.message = message;
        return this;
    }

    public AssigntOrderPopupToStaff setOnAssignmentDialogAction(DialogAssignDialogAction dialogComfirmAction) {
        this.dialogComfirmAction = dialogComfirmAction;
        return this;
    }

    public AssigntOrderPopupToStaff(@NonNull Context context) {
        super(context);
        init();
    }

    public AssigntOrderPopupToStaff(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected AssigntOrderPopupToStaff(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private AssigntOrderPopupToStaff init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_assignment_order_to_staff_dialog_popup);

        Window window = getWindow();
        if (window != null) {

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);

            setCancelable(false);

            tvTitle = findViewById(R.id.tvTitle);
            tvMessage = findViewById(R.id.tvMessage);
            btnCancel = findViewById(R.id.btnCancel);
            btnOk = findViewById(R.id.btnOk);
            spinerShippers = findViewById(R.id.spinerShippers);

            loadShipperList();

        }
        return this;
    }

    private void loadShipperList() {
        Call<List<UserModel>> call = RetrofitBuilder.getClient().create(UserAPI.class).findAll(Role.SHIPPER.getName());
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    shipperList.clear();
                    shipperList.addAll(response.body());
                    adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, shipperList);
                    spinerShippers.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    if (title != null && !title.isEmpty()) {
                        tvTitle.setText(title);
                    }

                    if (message != null && !message.isEmpty()) {
                        tvMessage.setText(message);
                    }

                    btnCancel.setOnClickListener(view -> {
                        if (dialogComfirmAction != null) {
                            dialogComfirmAction.cancel();
                        }
                    });

                    btnOk.setOnClickListener(view -> {
                        if (dialogComfirmAction != null) {
                            dialogComfirmAction.ok(shipperSelected);
                        }
                    });

                    spinerShippers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            shipperSelected = shipperList.get(i);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {

            }
        });
    }

    public interface DialogAssignDialogAction {
        void cancel();

        void ok(UserModel shipper);
    }
}
