package vn.tdc.edu.fooddelivery.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.tdc.edu.fooddelivery.models.RoleModel;

public interface RoleAPI {
    @GET("api/roles")
    Call<List<RoleModel>> findAll();

    @POST("api/roles")
    Call<RoleModel> save(@Body RoleModel roleModel);

    @PUT("api/roles")
    Call<RoleModel> update(@Body RoleModel roleModel);

    @DELETE("api/roles/{id}")
    Call<RoleModel> delete(@Path("id") Integer id);
}
