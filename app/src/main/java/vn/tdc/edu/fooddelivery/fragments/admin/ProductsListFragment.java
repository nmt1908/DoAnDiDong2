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
import vn.tdc.edu.fooddelivery.adapters.ProductManagementRecyclerViewAdapter;
import vn.tdc.edu.fooddelivery.api.ProductAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.components.ConfirmDialog;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.models.BaseModel;
import vn.tdc.edu.fooddelivery.models.ProductModel;

public class ProductsListFragment extends AbstractFragment implements View.OnClickListener {
    ProductManagementRecyclerViewAdapter adapter;
    List<ProductModel> productsList;
    private Button btnAdd;
    private RecyclerView recyclerViewProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_product_list, container, false);

        btnAdd = view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(this);

        recyclerViewProduct = view.findViewById(R.id.recyclerViewProduct);

        if (productsList == null) {
            productsList = new ArrayList<>();
        }

        adapter = new ProductManagementRecyclerViewAdapter((AppCompatActivity) getActivity(), R.layout.recycler_product, productsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewProduct.setLayoutManager(layoutManager);
        recyclerViewProduct.setAdapter(adapter);

        adapter.setRecylerViewItemClickListener(new ProductManagementRecyclerViewAdapter.OnRecylerViewItemClickListener() {
            @Override
            public void onButtonEditClickListener(int position) {
                ((AbstractActivity) getActivity()).setFragment(ProductFormFragment.class, R.id.frameLayout, true)
                        .setProductModel(productsList.get(position));
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
                        deleteProduct(productsList.get(position));
                        confirmDialog.dismiss();
                    }
                });

                confirmDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Call<List<ProductModel>> call = RetrofitBuilder.getClient().create(ProductAPI.class).findAll();

        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.body() != null) {
                    productsList.clear();
                    productsList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d("api-call", "Fetch product data successfully");
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Log.d("api-call", "Fetch product data fail");
            }
        });
    }

    private void deleteProduct(ProductModel productModel) {
        Call<ProductModel> call = RetrofitBuilder.getClient().create(ProductAPI.class).delete(productModel.getId());
        call.enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                Log.d("Response API", "Status code " + response.code());
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    ((AbstractActivity) getActivity()).showMessageDialog("Xoá sản phẩm thành công");
                    productsList.remove(productModel);
                    adapter.notifyDataSetChanged();
                } else {
                    ((AbstractActivity) getActivity()).showMessageDialog("Xoá sản phẩm thất bại");
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                ((AbstractActivity) getActivity()).showMessageDialog("Xoá sản phẩm thất bại");
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAdd) {
            ((AbstractActivity) getActivity()).setFragment(ProductFormFragment.class, R.id.frameLayout, true);
        }
    }
}