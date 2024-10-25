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
import vn.tdc.edu.fooddelivery.models.UserModel;

public interface UserAPI {
    @GET("api/users")
    Call<List<UserModel>> findAll(@Query("roleCode") String roleCode);

    @POST("api/users")
    Call<UserModel> save(@Body UserModel userModel);

    @POST("api/users/login")
    Call<UserModel> login(@Body UserModel userModel);

    @PUT("api/users")
    Call<UserModel> update(@Body UserModel userModel);

    @PUT("api/users/changePassword")
    Call<UserModel> changPassword(@Body UserModel userModel);

    @DELETE("api/users/{id}")
    Call<UserModel> delete(@Path("id") Integer id);
}
