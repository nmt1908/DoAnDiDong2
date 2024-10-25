package vn.tdc.edu.fooddelivery.fragments.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.adapters.UserManagementRecyclerViewAdapter;
import vn.tdc.edu.fooddelivery.api.UserAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.components.ConfirmDialog;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.models.RoleModel;
import vn.tdc.edu.fooddelivery.models.UserModel;

public class UsersListFragment extends AbstractFragment implements UserManagementRecyclerViewAdapter.OnRecylerViewItemClickListener {
    private RecyclerView recyclerViewUser;
    private UserManagementRecyclerViewAdapter adapter;
    private List<UserModel> listUsers;
    private RoleModel roleModel;
    private ConfirmDialog confirmDialog;

    public FragmentStateAdapter fragmentStateAdapter;

    public RoleModel getRoleModel() {
        return roleModel;
    }

    public void setRoleModel(RoleModel roleModel) {
        this.roleModel = roleModel;
    }

    public List<UserModel> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<UserModel> listUsers) {
        this.listUsers = listUsers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);
        recyclerViewUser = view.findViewById(R.id.recyclerViewUser);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserListFromAPI(roleModel == null ? null : roleModel.getCode());
    }

    private void deleteUser(int position) {
        UserModel userModel = listUsers.get(position);
        Call<UserModel> call = RetrofitBuilder.getClient().create(UserAPI.class).delete(userModel.getId());
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    ((AbstractActivity) getActivity()).showMessageDialog("Xoá người dùng thành công");
                    getUserListFromAPI(roleModel == null ? null : roleModel.getCode());
                    adapter.notifyItemRemoved(position);
                    confirmDialog.dismiss();

                } else {
                    ((AbstractActivity) getActivity()).showMessageDialog("Xoá người dùng thất bại");
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                ((AbstractActivity) getActivity()).showMessageDialog("Xoá người dùng thất bại");
            }
        });
    }

    private void getUserListFromAPI(String roleCode) {
        Call<List<UserModel>> call = RetrofitBuilder.getClient().create(UserAPI.class).findAll(roleCode);

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                    if (listUsers == null) {
                        listUsers = new ArrayList<>();
                    }

                    listUsers.clear();
                    listUsers.addAll(response.body());

                    adapter = new UserManagementRecyclerViewAdapter((AppCompatActivity) getActivity(), R.layout.recycler_user_management, listUsers);
                    adapter.setRecylerViewItemClickListener(UsersListFragment.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewUser.setLayoutManager(layoutManager);
                    recyclerViewUser.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onButtonEditClickListener(int position) {
        ((AbstractActivity) getActivity()).setFragment(UserFormFragment.class, R.id.frameLayout, true)
                .setUserModel(listUsers.get(position));
    }

    @Override
    public void onButtonDeleteClickListener(int position) {
        confirmDialog = new ConfirmDialog(getActivity());
        confirmDialog.setTitle("Xác nhận");
        confirmDialog.setMessage("Dữ liệu đã xoá không thể hoàn tác.\nBạn có muốn tiếp tục không?");
        confirmDialog.setOnDialogComfirmAction(new ConfirmDialog.DialogComfirmAction() {
            @Override
            public void cancel() {
                confirmDialog.dismiss();
            }

            @Override
            public void ok() {
                deleteUser(position);
            }
        });

        confirmDialog.show();
    }
}