package vn.tdc.edu.fooddelivery.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.tdc.edu.fooddelivery.models.ItemCartModel;
import vn.tdc.edu.fooddelivery.models.CartModel;

public interface CartsAPI {
    @GET("api/carts")
    Call<List<CartModel>> findCartsOfUser(@Query("userId") Integer userId);

    @POST("api/carts")
    Call<List<CartModel>> updateAndCreate(@Body ItemCartModel carstModel);

    @DELETE("api/carts/{id}")
    Call<CartModel> delete(@Path("id") Integer id);
}
