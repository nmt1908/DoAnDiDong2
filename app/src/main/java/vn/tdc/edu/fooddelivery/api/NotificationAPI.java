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
import vn.tdc.edu.fooddelivery.models.NotificationModel;

public interface NotificationAPI {
    @GET("api/notifycations")
    Call<List<NotificationModel>> findAll();
    @GET("api/notifycations")
    Call<List<NotificationModel>> findNotifyCationsOfUser(@Query("userId") Integer userId);

    @POST("api/notifycations")
    Call<NotificationModel> save(@Body NotificationModel notificationModel);

    @PUT("api/notifycations/changeStatus")
    Call<NotificationModel> update(@Body NotificationModel notificationModel);

    @DELETE("api/notifycations/{id}")
    Call<NotificationModel> delete(@Path("id") Integer id);
}
