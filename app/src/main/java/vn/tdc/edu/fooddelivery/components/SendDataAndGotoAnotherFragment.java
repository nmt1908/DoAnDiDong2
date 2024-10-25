package vn.tdc.edu.fooddelivery.components;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.activities.user.MainActivity;
import vn.tdc.edu.fooddelivery.api.ProductAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.fragments.user.NotifyCationDetailFragment;
import vn.tdc.edu.fooddelivery.fragments.user.ProductDetailFragment;
import vn.tdc.edu.fooddelivery.models.NotificationModel;
import vn.tdc.edu.fooddelivery.models.ProductModel;

public class SendDataAndGotoAnotherFragment {
    private static MainActivity mainActivity;

    //Send data and change screen to detail screen!
    public static void sendToProduceDetail(Activity activity, ProductModel cart) {
        mainActivity.clearSearchView();
        List<ProductModel> productList = new ArrayList<>();
        List<ProductModel> productListSameCategory = new ArrayList<>();
        getProduct().thenAcceptAsync(productsList -> {
            productList.addAll(productsList);
            productListSameCategory.addAll(getProductSameCategory(productList, cart));
            binData(activity, cart, productListSameCategory);
        }).exceptionally(throwable -> {
            // Xử lý ngoại lệ (nếu có)
            return null;
        });
    }


    private static List<ProductModel> getProductSameCategory(List<ProductModel> productModelList, ProductModel cart) {
        List<ProductModel> productModels = new ArrayList<>();
        for (ProductModel p : productModelList) {
            for (int id : p.getCategoryIds()) {
                if (cart.getCategoryIds().contains(id)) {
                    productModels.add(p);
                }
            }
        }
        return productModels;
    }

    private static void binData(Activity activity, ProductModel cart, List<ProductModel> productsList) {
        ((AbstractActivity) activity).setFragment(ProductDetailFragment.class, R.id.frameLayout, true)
                .setDetailProduct(cart).
                setArrayList(null)
                .setArrayList(productsList);
    }

    public static void sendToDetailNotifyCation(Activity activity, NotificationModel cart) {
        ((AbstractActivity) activity).setFragment(NotifyCationDetailFragment.class, R.id.frameLayout, true)
                .setNotificationModel(cart);
    }
    private static CompletableFuture<List<ProductModel>> getProduct() {
        CompletableFuture<List<ProductModel>> future = new CompletableFuture<>();
        Call<List<ProductModel>> callProduct = RetrofitBuilder.getClient().create(ProductAPI.class).findAll();
        callProduct.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.body() != null) {
                    List<ProductModel> productsList = response.body();
                    Log.d("api-call", "Fetch product data successfully");
                    future.complete(productsList);
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Log.d("api-call", "Fetch product data fail");
                future.completeExceptionally(t);
            }
        });

        return future;
    }


}
