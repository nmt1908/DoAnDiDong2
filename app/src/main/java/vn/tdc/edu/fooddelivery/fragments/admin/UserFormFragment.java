package vn.tdc.edu.fooddelivery.fragments.admin;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.api.RoleAPI;
import vn.tdc.edu.fooddelivery.api.UserAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.components.MultiSelectDialog;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.models.RoleModel;
import vn.tdc.edu.fooddelivery.models.UserModel;
import vn.tdc.edu.fooddelivery.utils.ImageUploadUtils;

public class UserFormFragment extends AbstractFragment implements View.OnClickListener {
    private final String PASSWORD_DEFAULT = "123456";
    private TextView tvRoles;
    private EditText edEmail;
    private EditText edFullName;
    private ShapeableImageView imgUser;
    private EditText edId;
    private EditText edImage;
    private Button btnAdd;
    private Button btnResetPassword;
    private Button btnUpdateInfo;
    private LinearLayout editOptions;
    private FloatingActionButton btnUploadImage;
    private List<RoleModel> listRoles;

    private UserModel userModel;
    MultiSelectDialog<RoleModel> multiSelectDialog;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_form, container, false);
        tvRoles = view.findViewById(R.id.edRoles);
        imgUser = view.findViewById(R.id.imgUser);
        edFullName = view.findViewById(R.id.edFullName);
        edEmail = view.findViewById(R.id.edEmail);
        edId = view.findViewById(R.id.edId);
        edImage = view.findViewById(R.id.edImage);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnResetPassword = view.findViewById(R.id.btnResetPassword);
        btnUpdateInfo = view.findViewById(R.id.btnUpdateInfo);
        btnUploadImage = view.findViewById(R.id.btnUploadImage);
        editOptions = view.findViewById(R.id.editOptions);

        tvRoles.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnResetPassword.setOnClickListener(this);
        btnUpdateInfo.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);

        if (this.userModel == null) {
            this.userModel = new UserModel();
        }

        ImageUploadUtils.getInstance().registerForUploadImageActivityResult(this, imgUser);

        createMuiltiSelectedUserRoleDialog();
        dropUserDataToEditForm();

        return view;
    }

    private void createMuiltiSelectedUserRoleDialog() {
        Call<List<RoleModel>> call = RetrofitBuilder.getClient().create(RoleAPI.class).findAll();

        call.enqueue(new Callback<List<RoleModel>>() {
            @Override
            public void onResponse(Call<List<RoleModel>> call, Response<List<RoleModel>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    listRoles = response.body();

                    if (getActivity() != null && multiSelectDialog == null) {
                        multiSelectDialog = new MultiSelectDialog(getActivity(), listRoles, userModel.getRoleIds());

                        multiSelectDialog.setTitle("Chọn vai trò");
                        multiSelectDialog.setOnActionClickListener(new MultiSelectDialog.Action() {
                            @Override
                            public void cancel() {
                            }

                            @Override
                            public void ok(List<Integer> listObjectSelected) {
                                userModel.setRoleIds(listObjectSelected);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RoleModel>> call, Throwable t) {

            }
        });
    }

    private boolean validateData() {
        if (edFullName.getText().toString().isEmpty()) {
            edFullName.setError("Tên người dùng không được để trống");
            return false;
        }

        if (userModel.getRoleIds() == null || userModel.getRoleIds().isEmpty()) {
            tvRoles.setError("Bạn chưa chọn vai trò");
            return false;
        } else {
            tvRoles.setError(null);
        }

        if (edEmail.getText().toString().isEmpty()) {
            edEmail.setError("Email không được để trống");
            return false;
        }

        return true;
    }

    private void getUserFromUserInput() {
        if (edId.getText() != null && !edId.getText().toString().isEmpty()) {
            userModel.setId(Integer.valueOf(String.valueOf(edId.getText())));
        }

        if (edImage.getText() != null && !edImage.getText().toString().isEmpty()) {
            userModel.setImageName(String.valueOf(edImage.getText()));
        }

        userModel.setFullName(edFullName.getText().toString());
        userModel.setEmail(edEmail.getText().toString());
    }

    private void dropUserDataToEditForm() {
        if (userModel != null && userModel.getId() != null) {
            Glide.with(getActivity()).load(userModel.getImageUrl())
                    .into(imgUser);
            edEmail.setText(userModel.getEmail());
            edFullName.setText(userModel.getFullName());
            edId.setText(userModel.getId() == null ? "" : userModel.getId().toString());
            edImage.setText(userModel.getImageName() == null ? "" : userModel.getImageName());

            btnAdd.setVisibility(View.GONE);
            editOptions.setVisibility(View.VISIBLE);
            edEmail.setEnabled(false);
        } else {
            btnAdd.setVisibility(View.VISIBLE);
            editOptions.setVisibility(View.GONE);
            edEmail.setEnabled(true);
        }
    }

    private void saveUser() {
        ImageUploadUtils.getInstance().handleUploadFileToServer(new ImageUploadUtils.Action() {
            @Override
            public void onSucess(String fileName) {
                userModel.setImageName(fileName);

                if (fileName.isEmpty()) {
                    userModel.setImageName(ImageUploadUtils.USER_IMAGE_UPLOAD_DEFAULT);
                }

                userModel.setPassword(PASSWORD_DEFAULT);

                Call<UserModel> call = RetrofitBuilder.getClient().create(UserAPI.class).save(userModel);

                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                            ((AbstractActivity) getActivity()).showMessageDialog("Thêm người dùng thành công");
                            getActivity().onBackPressed();
                        } else {
                            ((AbstractActivity) getActivity()).showMessageDialog("Thêm người dùng thất bại");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        ((AbstractActivity) getActivity()).showMessageDialog("Thêm người dùng thất bại");
                    }
                });
            }

            @Override
            public void onFailed() {
            }
        });
    }

    private void updateUser(UserModel userModel) {
        ImageUploadUtils.getInstance().handleUploadFileToServer(new ImageUploadUtils.Action() {
            @Override
            public void onSucess(String fileName) {
                if (!fileName.isEmpty()) {
                    userModel.setImageName(fileName);
                }

                Call<UserModel> call = RetrofitBuilder.getClient().create(UserAPI.class).update(userModel);
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                            ((AbstractActivity) getActivity()).showMessageDialog("Cập nhật thông tin người dùng thành công");
                            getActivity().onBackPressed();
                        } else {
                            ((AbstractActivity) getActivity()).showMessageDialog("Cập nhật thông tin người dùng thất bại");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        ((AbstractActivity) getActivity()).showMessageDialog("Hệ thống đang bảo trì");
                    }
                });
            }

            @Override
            public void onFailed() {
                ((AbstractActivity) getActivity()).showMessageDialog("Hệ thống đang bảo trì");
            }
        });
    }

    private void resetPassword() {
        Call<UserModel> call = RetrofitBuilder.getClient().create(UserAPI.class).changPassword(userModel);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                    ((AbstractActivity) getActivity()).showMessageDialog("Reset password thành công");
                } else {
                    ((AbstractActivity) getActivity()).showMessageDialog("Reset password thất bại");
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                ((AbstractActivity) getActivity()).showMessageDialog("Hệ thống đang bảo trì");
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edRoles) {
            if (multiSelectDialog != null) {
                multiSelectDialog.show();
            }
        } else if (view.getId() == R.id.btnAdd) {
            if (!validateData()) {
                return;
            }

            getUserFromUserInput();

            if (userModel != null) {
                saveUser();
            }
        } else if (view.getId() == R.id.btnUploadImage) {
            ImageUploadUtils.getInstance().showChoosingImageOptionsDialog((AbstractActivity) getActivity(), imgUser);
        } else if (view.getId() == R.id.btnResetPassword) {
            userModel.setId(Integer.valueOf(String.valueOf(edId.getText())));
            userModel.setPassword(PASSWORD_DEFAULT);
            resetPassword();
        } else if (view.getId() == R.id.btnUpdateInfo) {
            if (!validateData()) {
                return;
            }

            getUserFromUserInput();

            UserModel userRequest = new UserModel();
            userRequest.setId(userModel.getId());
            userRequest.setFullName(userModel.getFullName());
            userRequest.setRoleIds(userModel.getRoleIds());

            updateUser(userRequest);
        }
    }
}