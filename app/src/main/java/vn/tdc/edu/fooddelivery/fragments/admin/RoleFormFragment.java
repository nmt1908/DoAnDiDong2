package vn.tdc.edu.fooddelivery.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.api.RoleAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.components.MultiSelectDialog;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.models.CategoryModel;
import vn.tdc.edu.fooddelivery.models.RoleModel;

public class RoleFormFragment extends AbstractFragment implements View.OnClickListener {

    private EditText edId;
    private EditText edCode;
    private EditText edNameRole;
    private Button btnAddOrUpdate;
    MultiSelectDialog<CategoryModel> multiSelectDialog;
    private RoleModel roleModel;

    public RoleModel getRoleModel() {
        return roleModel;
    }

    public RoleFormFragment setRoleModel(RoleModel roleModel) {
        this.roleModel = roleModel;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_role_form, container, false);
        edCode = view.findViewById(R.id.edCode);
        edNameRole = view.findViewById(R.id.edNameRole);
        edId = view.findViewById(R.id.edId);
        btnAddOrUpdate = view.findViewById(R.id.btnAddUser);

        btnAddOrUpdate.setOnClickListener(this);

        dropRoleModelToEditForm();
        return view;
    }

    @Override
    public void onClick(View view) {
        if (!validateData()) {
            return;
        }
        if (view.getId() == R.id.btnAddUser) {
            if (roleModel != null && roleModel.getId() != null) {
                updateRole();
            } else {
                saveRole();
            }
        }
    }

    private boolean validateData() {
        if (edCode.getText().toString().isEmpty()) {
            edCode.setError("Tên vai trò không được để trống");
            return false;
        }
        if (edNameRole.getText().toString().isEmpty()) {
            edNameRole.setError("Mã vai trò không được để trống");
            return false;
        }

        return true;
    }

    private void getRoleFromUserInputs() {
        if (roleModel == null) {
            roleModel = new RoleModel();
        }

        if (edId.getText() != null && !edId.getText().toString().isEmpty()) {
            roleModel.setId(Integer.valueOf(edId.getText().toString()));
        }
        roleModel.setCode(edCode.getText().toString());
        roleModel.setName(edNameRole.getText().toString());
    }

    private void dropRoleModelToEditForm() {
        if (roleModel != null) {
            btnAddOrUpdate.setText(R.string.btn_update_role);
            edCode.setText(roleModel.getCode());
            edNameRole.setText(roleModel.getName());
        }
    }

    private void saveRole() {

        getRoleFromUserInputs();
        Call<RoleModel> call = RetrofitBuilder.getClient().create(RoleAPI.class).save(roleModel);

        call.enqueue(new Callback<RoleModel>() {
            @Override
            public void onResponse(Call<RoleModel> call, Response<RoleModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                    ((AbstractActivity) getActivity()).showMessageDialog("Thêm vai trò thành công");
                    getActivity().onBackPressed();
                } else {
                    ((AbstractActivity) getActivity()).showMessageDialog("Thêm vai trò thất bại");
                }
            }

            @Override
            public void onFailure(Call<RoleModel> call, Throwable t) {
                ((AbstractActivity) getActivity()).showMessageDialog("Thêm vai trò thất bại");
            }
        });
    }

    private void updateRole() {

        getRoleFromUserInputs();
        Call<RoleModel> call = RetrofitBuilder.getClient().create(RoleAPI.class).update(roleModel);

        call.enqueue(new Callback<RoleModel>() {
            @Override
            public void onResponse(Call<RoleModel> call, Response<RoleModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                    ((AbstractActivity) getActivity()).showMessageDialog("Cập nhật vai trò thành công");
                    getActivity().onBackPressed();
                } else {
                    ((AbstractActivity) getActivity()).showMessageDialog("Cập nhật vai trò thất bại");
                }
            }

            @Override
            public void onFailure(Call<RoleModel> call, Throwable t) {
                ((AbstractActivity) getActivity()).showMessageDialog("Cập nhật vai trò thất bại");
            }
        });
    }

}
