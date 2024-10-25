package vn.tdc.edu.fooddelivery.fragments.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.adapters.RoleManagementRecyclerViewAdapter;
import vn.tdc.edu.fooddelivery.api.RoleAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.components.ConfirmDialog;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.models.RoleModel;

public class RoleListFragment extends AbstractFragment implements View.OnClickListener {

    RoleManagementRecyclerViewAdapter adapter;
    List<RoleModel> rolesList;
    private Button btnAdd;
    private RecyclerView recyclerViewRole;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_role_list, container, false);

        btnAdd = view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(this);

        recyclerViewRole = view.findViewById(R.id.recyclerViewRole);

        if (rolesList == null) {
            rolesList = new ArrayList<>();
        }

        adapter = new RoleManagementRecyclerViewAdapter((AppCompatActivity) getActivity(), R.layout.recycler_role, rolesList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewRole.setLayoutManager(layoutManager);
        recyclerViewRole.setAdapter(adapter);

        adapter.setRecylerViewItemClickListener(new RoleManagementRecyclerViewAdapter.OnRecylerViewItemClickListener(){
            @Override
            public void onButtonEditClickListener(int position) {
                ((AbstractActivity) getActivity()).setFragment(RoleFormFragment.class, R.id.frameLayout, true)
                        .setRoleModel(rolesList.get(position));
            }

            @Override
            public void onButtonDeleteClickListener(int position) {
                ConfirmDialog confirmDialog = new ConfirmDialog(getActivity());
                confirmDialog.setTitle("Xác nhận");
                confirmDialog.setMessage("Dữ liệu đã xoá không thể hoàn tác.\nBạn có muốn tiếp tục không?");
                confirmDialog.setOnDialogComfirmAction(new ConfirmDialog.DialogComfirmAction() {
                    @Override
                    public void cancel() {
                        confirmDialog.dismiss();
                    }

                    @Override
                    public void ok() {
                        deleteRole(rolesList.get(position));
                        confirmDialog.dismiss();
                    }
                });

                confirmDialog.show();
            }
        });

        return view;
    }

    public void onResume() {
        super.onResume();
        Call<List<RoleModel>> call = RetrofitBuilder.getClient().create(RoleAPI.class).findAll();

        call.enqueue(new Callback<List<RoleModel>>() {
            @Override
            public void onResponse(Call<List<RoleModel>> call, Response<List<RoleModel>> response) {
                if (response.body() != null) {
                    rolesList.clear();
                    rolesList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d("api-call", "Fetch role data successfully");
                }
            }

            @Override
            public void onFailure(Call<List<RoleModel>> call, Throwable t) {
                Log.d("api-call", "Fetch role data fail");
            }
        });
    }

    private void deleteRole(RoleModel roleModel) {
        Call<RoleModel> call = RetrofitBuilder.getClient().create(RoleAPI.class).delete(roleModel.getId());
        call.enqueue(new Callback<RoleModel>() {
            @Override
            public void onResponse(Call<RoleModel> call, Response<RoleModel> response) {
                Log.d("Response API", "Status code " + response.code());
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    ((AbstractActivity) getActivity()).showMessageDialog("Xoá vai trò thành công");
                    rolesList.remove(roleModel);
                    adapter.notifyDataSetChanged();
                } else {
                    ((AbstractActivity) getActivity()).showMessageDialog("Xoá vai trò thất bại");
                }
            }

            @Override
            public void onFailure(Call<RoleModel> call, Throwable t) {
                ((AbstractActivity) getActivity()).showMessageDialog("Xoá sản phẩm thất bại");
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAdd) {
            ((AbstractActivity) getActivity()).setFragment(RoleFormFragment.class, R.id.frameLayout, true);
        }
    }
}
