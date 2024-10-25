package vn.tdc.edu.fooddelivery.fragments.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.api.NotificationAPI;
import vn.tdc.edu.fooddelivery.api.ProductAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.models.NotificationModel;
import vn.tdc.edu.fooddelivery.models.ProductModel;
import vn.tdc.edu.fooddelivery.utils.CommonUtils;
import vn.tdc.edu.fooddelivery.utils.ImageUploadUtils;

public class NotifyCationDetailFragment extends AbstractFragment {

    private static CommonUtils commonUtils;

    private View fragmentLayout;

    private NotificationModel notificationModel;

    private TextView title;

    private TextView timing;

    private TextView content;

    public NotificationModel getNotificationModel() {
        return notificationModel;
    }

    public NotifyCationDetailFragment setNotificationModel(NotificationModel notificationModel) {
        this.notificationModel = notificationModel;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.fragment_notification_detail, container, false);
        //-------------------------Processing-------------------//
        anhXa();
        binData();
        //------------------------------------------------------//
        return fragmentLayout;
    }

    public void anhXa() {
        title = fragmentLayout.findViewById(R.id.txt_title_notify_detail_screen);
        content = fragmentLayout.findViewById(R.id.txt_content_notify_detail_screen);
        timing = fragmentLayout.findViewById(R.id.txt_timing_notify_detail_screen);
    }

    public void binData() {
        if (notificationModel != null) {
            title.setText(String.valueOf(notificationModel.getTitle()));
            content.setText(String.valueOf(notificationModel.getContent()));
            timing.setText(String.valueOf(commonUtils.convertDateToString(notificationModel.getCreatedAt())));
            updateNotifications();

        }
    }

    private void updateNotifications() {
        NotificationModel notificationModel1 = new NotificationModel();
        notificationModel1.setUser_id(notificationModel.getUser_id());
        notificationModel1.setId(notificationModel.getId());
        notificationModel1.setStatus(1);
        Call<NotificationModel> call = RetrofitBuilder.getClient().create(NotificationAPI.class).update(notificationModel1);

        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                    Log.d("TAG", "onResponse: cap nhat thong bao thanh cong!");
                } else {
                    Log.d("TAG", "onResponse: cap nhat thong bao khong thanh cong!");
                }
            }
            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
            }
        });

    }

}