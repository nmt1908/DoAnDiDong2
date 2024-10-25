package vn.tdc.edu.fooddelivery.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.user.MainActivity;
import vn.tdc.edu.fooddelivery.api.UserAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.components.ShowDialog;
import vn.tdc.edu.fooddelivery.constant.SystemConstant;
import vn.tdc.edu.fooddelivery.enums.Role;
import vn.tdc.edu.fooddelivery.models.ErrorModel;
import vn.tdc.edu.fooddelivery.models.UserModel;
import vn.tdc.edu.fooddelivery.utils.Authentication;

public class RegisterActivity extends AbstractActivity implements View.OnClickListener {
    private TextView tvLoginLink;
    private EditText edNameRegister;
    private EditText edEmailRegister;
    private EditText edPasswordRegister;
    private EditText edRePasswordRegister;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        dialogEmailRules();

        edNameRegister = findViewById(R.id.edNameRegister);
        edEmailRegister = findViewById(R.id.edEmailRegister);
        edPasswordRegister = findViewById(R.id.edPasswordRegister);
        edRePasswordRegister = findViewById(R.id.edRePasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        btnRegister.setOnClickListener(this);
        tvLoginLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvLoginLink) {
            switchActivity(LoginActivity.class, "Login");
        } else if (view.getId() == R.id.btnRegister) {
            if (checkNoEmpty()) {
                if(checkRegex()){
                    UserModel userRegisterRequest = new UserModel();
                    userRegisterRequest.setEmail(edEmailRegister.getText().toString());
                    userRegisterRequest.setFullName(edNameRegister.getText().toString());
                    userRegisterRequest.setImageName("user_image_default.png");

                    List<Integer> roleIds = new ArrayList<>();
                    roleIds.add(Role.CUSTOMER.getId());
                    userRegisterRequest.setRoleIds(roleIds);

                    if (!edPasswordRegister.getText().toString().equals(edRePasswordRegister.getText().toString())) {
                        edRePasswordRegister.setError("Mật khẩu nhập lại không khớp");
                    } else {
                        userRegisterRequest.setPassword(edPasswordRegister.getText().toString());
                        register(userRegisterRequest);
                    }
                }
            }
        }
    }

    private void register(UserModel userModel) {
        Call<UserModel> call = RetrofitBuilder.getClient().create(UserAPI.class).save(userModel);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel userResponse = response.body();
                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    Authentication.login(userResponse);
                    ShowDialog showDialog = new ShowDialog(RegisterActivity.this);
                    showDialog.setTitle("Đăng ký");
                    showDialog.setMessage("Đăng ký thành công");
                    showDialog.setOnDialogComfirmAction(new ShowDialog.DialogComfirmAction() {
                        @Override
                        public void ok() {
                            showDialog.dismiss();
                            switchActivity(MainActivity.class,"Login success");
                        }
                    });
                    showDialog.show();
                } else {
                    ResponseBody error = response.errorBody();
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        ErrorModel errorModel = objectMapper.readValue(error.string(), ErrorModel.class);

                        if (errorModel.getMsg().equals(SystemConstant.MSG_EMAIL_IS_EXIST)) {
                            edEmailRegister.setError("Email đã tồn tại, chọn email khác!");
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }

    private boolean checkNoEmpty() {
        boolean check = true;
        if (edNameRegister.getText().toString().equals("")) {
            edNameRegister.setError("Không được để trống tên");
            check = false;
        } else if (edEmailRegister.getText().toString().equals("")) {
            edEmailRegister.setError("Không được để trống email");
            check = false;
        } else if (edPasswordRegister.getText().toString().equals("")) {
            edPasswordRegister.setError("Không được để trống mật khẩu");
            check = false;
        } else if (edRePasswordRegister.getText().toString().equals("")) {
            edRePasswordRegister.setError("Vui lòng nhập lại mật khẩu");
            check = false;
        }
        return check;
    }

    private boolean checkRegex(){
        boolean check = true;
        if(!edEmailRegister.getText().toString().matches("\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+")){
            edEmailRegister.setError("Email không đúng định dạng");
            check = false;
        }
        if(!edPasswordRegister.getText().toString().matches("(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}")){
            edPasswordRegister.setError("Mật khẩu tối thiểu 6 ký tự , ít nhất một chữ số và một ký tự");
            check = false;
        }
        return check;
    }

    private void dialogEmailRules(){
        ShowDialog showDialog = new ShowDialog(RegisterActivity.this);
        showDialog.setTitle("Quy tắc email");
        showDialog.setMessage("Tên email : \n" +
                "1. Phải có ký tự '@'. \n" +
                "2. Không có ký tự đặc biệt nào sau và trước '@'. \n" +
                "3. Tên miền cấp cao nhất phải hợp lệ. \n" +
                "4. Email không được bắt đầu bằng '.'. \n" +
                "5. Email chỉ cho phép ký tự, chữ số, dấu gạch dưới và dấu gạch ngang. \n" +
                "6. Không được phép chấm đôi."
        );
        showDialog.setOnDialogComfirmAction(new ShowDialog.DialogComfirmAction() {
            @Override
            public void ok() {
                showDialog.dismiss();
            }
        });
        showDialog.show();
    }

}