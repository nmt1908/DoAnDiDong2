package vn.tdc.edu.fooddelivery.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.user.MainActivity;
import vn.tdc.edu.fooddelivery.api.UserAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.constant.SystemConstant;
import vn.tdc.edu.fooddelivery.models.ErrorModel;
import vn.tdc.edu.fooddelivery.models.UserModel;
import vn.tdc.edu.fooddelivery.utils.Authentication;

public class LoginActivity extends AbstractActivity implements View.OnClickListener {
    private EditText edEmail, edPassword;
    private TextView tvRegisterLink;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            UserModel userLoginRequest = new UserModel();
            userLoginRequest.setEmail(edEmail.getText().toString());
            userLoginRequest.setPassword(edPassword.getText().toString());
            login(userLoginRequest);
        } else if (view.getId() == R.id.tvRegisterLink) {
            switchActivity(RegisterActivity.class, "Register");
        }
    }

    private void login(UserModel userModel) {
        Call<UserModel> call = RetrofitBuilder.getClient().create(UserAPI.class).login(userModel);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel userResponse = response.body();
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    Authentication.login(userResponse);
                    switchActivity(MainActivity.class,"Login success");
                    finish();
                } else {
                    ResponseBody error = response.errorBody();
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        ErrorModel errorModel = objectMapper.readValue(error.string(), ErrorModel.class);

                        if (errorModel.getMsg().equals(SystemConstant.MSG_USER_NOT_EXISTS)) {
                            edEmail.setError("Tài khoản không tồn tại");
                        } else if (errorModel.getMsg().equals(SystemConstant.MSG_WRONG_PASSWORD)){
                            edPassword.setError("Sai mật khẩu");
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
}