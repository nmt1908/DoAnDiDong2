package vn.tdc.edu.fooddelivery.fragments.user;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.activities.LoginActivity;
import vn.tdc.edu.fooddelivery.activities.user.MainActivity;
import vn.tdc.edu.fooddelivery.api.UserAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.components.ChangePasswordPopup;
import vn.tdc.edu.fooddelivery.components.ConfirmDialog;
import vn.tdc.edu.fooddelivery.components.InputStringPopup;
import vn.tdc.edu.fooddelivery.constant.SystemConstant;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.models.ErrorModel;
import vn.tdc.edu.fooddelivery.models.UserModel;
import vn.tdc.edu.fooddelivery.utils.Authentication;
import vn.tdc.edu.fooddelivery.utils.ImageUploadUtils;

public class ProfileFragment extends AbstractFragment implements View.OnClickListener, ImageUploadUtils.OnResultUpload {
    private TextView tvUserName, tvUserEmail;
    private ImageButton btnUpdateUserName, btnUpdatePassword;
    private Button btnSave, btnLogout;
    private ShapeableImageView userImage, imgUploadImage;

    private Object prevUserImagaTag;

    private ChangePasswordPopup changePasswordPopup;

    private UserModel userModel = new UserModel();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        btnUpdateUserName = view.findViewById(R.id.btnUpdateUserName);
        btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword);
        btnSave = view.findViewById(R.id.btnSave);
        btnLogout = view.findViewById(R.id.btnLogout);
        userImage = view.findViewById(R.id.userImage);
        imgUploadImage = view.findViewById(R.id.imgUploadImage);

        tvUserName.setText(Authentication.getUserLogin().getFullName());
        tvUserEmail.setText(Authentication.getUserLogin().getEmail());
        Glide.with(getActivity()).load(Authentication.getUserLogin().getImageUrl())
                .into(userImage);

        btnSave.setEnabled(false);

        btnUpdateUserName.setOnClickListener(this);
        btnUpdatePassword.setOnClickListener(this);
        imgUploadImage.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        imgUploadImage.setOnClickListener(this);

        ImageUploadUtils.setOnResultUpload(this);
        ImageUploadUtils.getInstance().registerForUploadImageActivityResult(this, userImage);

        userModel.setId(Authentication.getUserLogin().getId());
        userModel.setFullName(Authentication.getUserLogin().getFullName());
        return view;
    }


    @Override
    public void onUploadImageResultAction() {
        btnSave.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgUploadImage) {
            ImageUploadUtils.getInstance().showChoosingImageOptionsDialog((AbstractActivity) getActivity(), userImage);
        } else if (view.getId() == R.id.btnLogout) {
            ConfirmDialog confirmDialog = new ConfirmDialog(getActivity());
            confirmDialog.setTitle("Đăng xuất");
            confirmDialog.setMessage("Đăng xuất khỏi tài khoản của bạn ?");
            confirmDialog.setOnDialogComfirmAction(new ConfirmDialog.DialogComfirmAction() {
                @Override
                public void cancel() {
                    confirmDialog.dismiss();
                }

                @Override
                public void ok() {
                    confirmDialog.dismiss();
                    if (Authentication.logout()) {
                        ((AbstractActivity) getActivity()).switchActivity(LoginActivity.class, "Logout");
                        ((AbstractActivity) getActivity()).finish();
                    }
                    ;
                }
            });

            confirmDialog.show();
        } else if (view.getId() == R.id.btnUpdateUserName) {
            InputStringPopup inputStringPopup = new InputStringPopup(getActivity());
            inputStringPopup.setTitle("Cập nhật họ tên")
                    .setPlaceHolder("Nhập tên của bạn")
                    .setText(tvUserName.getText().toString());
            inputStringPopup.setOnInputStringDialogAction(new InputStringPopup.DialogAssignDialogAction() {
                @Override
                public void cancel() {
                    inputStringPopup.dismiss();
                }

                @Override
                public void ok(String inputText) {
                    btnSave.setEnabled(true);
                    tvUserName.setText(inputText);
                    userModel.setFullName(inputText);
                    inputStringPopup.dismiss();
                }
            });
            inputStringPopup.show();
        } else if (view.getId() == R.id.btnSave) {
            updateUser(userModel);
        } else if (view.getId() == R.id.btnUpdatePassword) {
            changePasswordPopup = new ChangePasswordPopup(getActivity());
            changePasswordPopup.setOnInputDialogAction(new ChangePasswordPopup.DialogPopupAction() {
                @Override
                public void cancel() {
                    changePasswordPopup.dismiss();
                }

                @Override
                public void ok(String oldPassword, String newPassword) {
                    if (oldPassword.isEmpty()) {
                        changePasswordPopup.setEditOldPasswordError("Bạn chưa nhập mật khẩu cũ");
                        return;
                    }

                    if (newPassword.length() < 6) {
                        changePasswordPopup.setEditNewPasswordError("Độ dài mật khẩu tối thiểu 6 ký tự trở lên");
                        return;
                    }

                    changePassword(oldPassword, newPassword);
                }
            });
            changePasswordPopup.show();
        }
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
                            Authentication.updateUser(userModel);
                            ((AbstractActivity) getActivity()).showMessageDialog("Lưu thông tin thành công");
                            Authentication.isUpdated = true;
                        } else {
                            ((AbstractActivity) getActivity()).showMessageDialog("Thao tác không thành công");
                        }

                        btnSave.setEnabled(false);
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

    private void changePassword(String oldPassword, String newPassword) {
        userModel.setId(Authentication.getUserLogin().getId());
        userModel.setEmail(Authentication.getUserLogin().getEmail());
        userModel.setPassword(oldPassword);
        Call<UserModel> checkCorrectPasswordRequest = RetrofitBuilder.getClient().create(UserAPI.class).login(userModel);

        checkCorrectPasswordRequest.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    userModel.setEmail(null);
                    userModel.setPassword(newPassword);
                    Call<UserModel> changePasswordRequest = RetrofitBuilder.getClient().create(UserAPI.class).changPassword(userModel);
                    changePasswordRequest.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                                ((AbstractActivity) getActivity()).showMessageDialog("Cập nhật mật khẩu thành công");
                                changePasswordPopup.dismiss();
                            } else {
                                ((AbstractActivity) getActivity()).showMessageDialog("Cập nhật mật khẩu thất bại");
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            ((AbstractActivity) getActivity()).showMessageDialog("Hệ thống đang bảo trì");
                        }
                    });
                } else {
                    changePasswordPopup.setEditOldPasswordError("Mật khẩu cũ không đúng");
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                ((AbstractActivity) getActivity()).showMessageDialog("Hệ thống đang bảo trì");
            }
        });
    }
}