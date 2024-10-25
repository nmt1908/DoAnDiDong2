package vn.tdc.edu.fooddelivery.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.tdc.edu.fooddelivery.models.CategoryModel;

public interface CategoryAPI {
    @GET("api/categories")
    Call<List<CategoryModel>> findAll(@Query("sort") String sortBy);

    @POST("api/categories")
    Call<CategoryModel> save(@Body CategoryModel categoryModel);

    @PUT("api/categories")
    Call<CategoryModel> update(@Body CategoryModel categoryModel);

    @DELETE("api/categories/{id}")
    Call<CategoryModel> delete(@Path("id") Integer id);
}
