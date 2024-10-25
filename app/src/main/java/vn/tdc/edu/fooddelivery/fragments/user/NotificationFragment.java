package vn.tdc.edu.fooddelivery.fragments.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.user.MainActivity;
import vn.tdc.edu.fooddelivery.adapters.NotificationRecyclerViewAdapter;
import vn.tdc.edu.fooddelivery.api.NotificationAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.components.SendDataAndGotoAnotherFragment;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.models.NotificationModel;
import vn.tdc.edu.fooddelivery.models.UserModel;
import vn.tdc.edu.fooddelivery.utils.Authentication;
import vn.tdc.edu.fooddelivery.utils.FileUtils;

public class NotificationFragment extends AbstractFragment {

    private int background;
    private RecyclerView recyclerView;
    private static NotificationRecyclerViewAdapter myAdapter;
    private static View fragmentLayout = null;
    private LinearLayout linearLayoutWrapper;
    private MainActivity mainActivity = new MainActivity();

    private static List<NotificationModel> listNotifycations;

    UserModel userModel = Authentication.getUserLogin();
    public int user_id = userModel.getId();;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.fragment_notification, container, false);
        //---------------------Start---------------------------
        if (listNotifycations == null) {
            listNotifycations = new ArrayList<>();
        }
        anhXa();
        createAnimation();
        //---------------------End-----------------------------
        return fragmentLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Call<List<NotificationModel>> callNotifycations = RetrofitBuilder.getClient().create(NotificationAPI.class).findNotifyCationsOfUser(user_id);
        callNotifycations.enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if (response.body() != null) {
                    listNotifycations.clear();
                    listNotifycations.addAll(response.body());
                    setUp(listNotifycations);
                    createAnimation();
                    clickEvent();
                    Log.d("api-call", "Fetch product data successfully !");
                }
            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                Log.d("api-call", "Fetch product data fail");
            }
        });
    }


    public void updateArrayListCart() {
        myAdapter.notifyDataSetChanged();
    }


    public void anhXa() {
        linearLayoutWrapper = fragmentLayout.findViewById(R.id.linearLayout_wrapper_notification);
        recyclerView = fragmentLayout.findViewById(R.id.recyclerView_notification_screen);
        //Setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(fragmentLayout.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setUp(List<NotificationModel> listNotifycations) {
        myAdapter = new NotificationRecyclerViewAdapter((Activity) fragmentLayout.getContext(), R.layout.notification_layout_item, listNotifycations);
        recyclerView.setAdapter(myAdapter);
    }

    private void clickEvent() {
        myAdapter.setOnRecyclerViewOnClickListener(new NotificationRecyclerViewAdapter.onRecyclerViewOnClickListener() {
            @Override
            public void onItemRecyclerViewClickListener(int position, View cardView) {
                if (fragmentLayout.getContext() instanceof Activity) {
                    Activity activity = (Activity) fragmentLayout.getContext();
                    SendDataAndGotoAnotherFragment.sendToDetailNotifyCation(activity, listNotifycations.get(position));
                }
            }
        });
    }

    //-----------------------------Animation when not have notify -----------------------//
    public void createAnimation() {
        if (listNotifycations.size() == 0) {
            LottieAnimationView anim = new LottieAnimationView(fragmentLayout.getContext());
            anim.setAnimation(R.raw.nothing_gift4);
            TranslateAnimation ta = new TranslateAnimation(0, 0, Animation.RELATIVE_TO_SELF, 100);
            ta.setDuration(1000);
            ta.setFillAfter(true);
            anim.startAnimation(ta);
            anim.playAnimation();
            anim.loop(true);
            linearLayoutWrapper.addView(anim);
        } else {
            linearLayoutWrapper.removeAllViews();
        }
    }

    public void deleteNotifications(NotificationModel notificationModel) {
        Call<NotificationModel> call = RetrofitBuilder.getClient().create(NotificationAPI.class).delete(notificationModel.getId());

        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                    Log.d("TAG", "onResponse: xoa thong bao thanh cong!");
                    listNotifycations.remove(notificationModel);
                    myAdapter.notifyDataSetChanged();
                    showMessageDialog("Đã xóa thông báo thành công");
                } else {
                    Log.d("TAG", "onResponse: xoa thong bao khong thanh cong!");
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
            }
        });
    }


    //Show message tam thoi
    public void showMessageDialog(String message) {
        androidx.appcompat.app.AlertDialog alert = new androidx.appcompat.app.AlertDialog.Builder(fragmentLayout.getContext())
                .setTitle("Message")
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }
}