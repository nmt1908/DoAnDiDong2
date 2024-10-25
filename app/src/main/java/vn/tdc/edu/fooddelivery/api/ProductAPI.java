package vn.tdc.edu.fooddelivery.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.tdc.edu.fooddelivery.models.ProductModel;

public interface ProductAPI {
    @GET("api/products")
    Call<List<ProductModel>> findAll();

    @POST("api/products")
    Call<ProductModel> save(@Body ProductModel productModel);

    @PUT("api/products")
    Call<ProductModel> update(@Body ProductModel productModel);
    @DELETE("api/products/{id}")
    Call<ProductModel> delete(@Path("id") Integer id);
}
