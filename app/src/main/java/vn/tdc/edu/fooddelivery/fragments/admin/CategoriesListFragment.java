package vn.tdc.edu.fooddelivery.fragments.admin;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.components.ConfirmDialog;
import vn.tdc.edu.fooddelivery.adapters.CategoryManagementRecyclerViewAdapter;
import vn.tdc.edu.fooddelivery.api.CategoryAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.models.CategoryModel;

public class CategoriesListFragment extends AbstractFragment implements View.OnClickListener {
    private CategoryManagementRecyclerViewAdapter adapter;
    private List<CategoryModel> categoriesList;
    private Button btnAdd;
    private RecyclerView recyclerViewCategory;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_categories_list, container, false);

        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        categoriesList = new ArrayList<>();
        adapter = new CategoryManagementRecyclerViewAdapter((AbstractActivity) getActivity(), R.layout.recycler_category, categoriesList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setAdapter(adapter);

        dropCategoriesToRecyclerView();

        adapter.setRecylerViewItemClickListener(new CategoryManagementRecyclerViewAdapter.OnRecylerViewItemClickListener() {
            @Override
            public void onButtonEditClickListener(int position) {
                ((AbstractActivity) getActivity()).setFragment(CategoryFormFragment.class, R.id.frameLayout, true)
                        .setCategoryModel(categoriesList.get(position));
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
                        deleteCategory(categoriesList.get(position));
                        confirmDialog.dismiss();
                    }
                });

                confirmDialog.show();
            }
        });
        return view;
    }

    private void deleteCategory(CategoryModel categoryModel) {
        Call<CategoryModel> call = RetrofitBuilder.getClient().create(CategoryAPI.class).delete(categoryModel.getId());
        call.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    ((AbstractActivity) getActivity()).showMessageDialog("Xoá danh mục thành công");
                    categoriesList.remove(categoryModel);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                ((AbstractActivity) getActivity()).showMessageDialog("Xoá danh mục thất bại");
            }
        });
    }

    private void dropCategoriesToRecyclerView() {
        Call<List<CategoryModel>> call = RetrofitBuilder.getClient().create(CategoryAPI.class).findAll(null);

        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.body() != null) {
                    categoriesList.clear();
                    categoriesList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAdd) {
            ((AbstractActivity) getActivity()).setFragment(CategoryFormFragment.class, R.id.frameLayout, true);
        }
    }
}