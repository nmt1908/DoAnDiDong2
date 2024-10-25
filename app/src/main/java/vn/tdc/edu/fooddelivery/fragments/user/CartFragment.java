package vn.tdc.edu.fooddelivery.fragments.user;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.adapters.CartRecycleViewAdapter;
import vn.tdc.edu.fooddelivery.api.CartsAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.models.ItemCartModel;
import vn.tdc.edu.fooddelivery.models.CartModel;
import vn.tdc.edu.fooddelivery.models.UserModel;
import vn.tdc.edu.fooddelivery.utils.Authentication;
import vn.tdc.edu.fooddelivery.utils.FormatCurentcy;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;

public class CartFragment extends AbstractFragment {

    public static CartRecycleViewAdapter myRecycleViewAdapter;
    private RecyclerView recyclerView;
    private static TextView cartScreeb;
    private Button order_btn_cart_screen;
    private static View fragmentLayout = null;
    private Activity activityCart;
    private static LinearLayout linearLayoutWrapper;
    private static Button btnBuy;

    private static List<CartModel> listCarts;
    UserModel userModel = Authentication.getUserLogin();
    private final int userID = userModel.getId();

    public Activity getActivityCart() {
        return activityCart;
    }


    public void setActivityCart(Activity activityCart) {
        this.activityCart = activityCart;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.fragment_cart, container, false);
        if (listCarts == null) {
            listCarts = new ArrayList<>();
        }
        AnhXa();
        clickBtnBuyEvent();
        return fragmentLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderListFromAPI();
    }

    private void getOrderListFromAPI() {
        Log.d("TAG", "getOrderListFromAPI: " + userID);
        Call<List<CartModel>> call = RetrofitBuilder.getClient().create(CartsAPI.class).findCartsOfUser(userID);
        call.enqueue(new Callback<List<CartModel>>() {
            @Override
            public void onResponse(Call<List<CartModel>> call, Response<List<CartModel>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    listCarts.clear();
                    listCarts.addAll(response.body());
                    setUpCart(listCarts);
                    CalculateAndAssign(listCarts);
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<CartModel>> call, Throwable t) {
                CalculateAndAssign(listCarts);
            }
        });
    }


    public void updateCart(ItemCartModel carstModel, CartModel cart) {
        Call<List<CartModel>> call = RetrofitBuilder.getClient().create(CartsAPI.class).updateAndCreate(carstModel);
        call.enqueue(new Callback<List<CartModel>>() {
            @Override
            public void onResponse(Call<List<CartModel>> call, Response<List<CartModel>> response) {

            }

            @Override
            public void onFailure(Call<List<CartModel>> call, Throwable t) {
                if (listCarts != null && cart != null) {
                    int index = listCarts.indexOf(cart);
                    listCarts.get(index).setQuantity(listCarts.get(index).getQuantity() + carstModel.getQuantity());
                    myRecycleViewAdapter.notifyDataSetChanged();
                    CalculateAndAssign(listCarts);
                }
            }
        });
    }


    public void deleteCarst(ItemCartModel carstModel, CartModel p) {

        Call<CartModel> call = RetrofitBuilder.getClient().create(CartsAPI.class).delete(carstModel.getId());
        call.enqueue(new Callback<CartModel>() {
            @Override
            public void onResponse(Call<CartModel> call, Response<CartModel> response) {
            }

            @Override
            public void onFailure(Call<CartModel> call, Throwable t) {
                if (listCarts != null) {
                    listCarts.remove(p);
                    myRecycleViewAdapter.notifyDataSetChanged();
                    CalculateAndAssign(listCarts);
                }
            }
        });
    }


    public void clickBtnBuyEvent() {
        if (btnBuy != null) {
            order_btn_cart_screen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((AbstractActivity) fragmentLayout.getContext()).setFragment(PaymentFragment.class, R.id.frameLayout, true);
                }
            });
        }
    }


    public void CalculateAndAssign(List<CartModel> orderItemModels) {
        int sum = 0;
        for (int i = 0; i < orderItemModels.size(); i++) {
            sum += orderItemModels.get(i).getProduct().getPrice() * orderItemModels.get(i).getQuantity();
        }
        String value = FormatCurentcy.format(sum + "");
        if (cartScreeb == null) {
            cartScreeb = activityCart.findViewById(R.id.total_cart_screen);
        }

        if (sum == 0) {
            btnBuy.setVisibility(View.INVISIBLE);
            cartScreeb.setText("0 đồng ");
            //--------------------Create animation gif!----------------------------//
            createAnimation();
        } else {
            //-----------------------------------
            linearLayoutWrapper.removeAllViews();
            //-----------------------------------
            btnBuy.setVisibility(View.VISIBLE);
            cartScreeb.setText(value + " đồng ");
        }
    }

    //------------------------------Create animation---------------------//
    public void createAnimation() {
        LottieAnimationView anim = new LottieAnimationView(fragmentLayout.getContext());
        anim.setAnimation(R.raw.nothing_gif2);
        TranslateAnimation ta = new TranslateAnimation(0, 0, Animation.RELATIVE_TO_SELF, 100);
        ta.setDuration(1000);
        ta.setFillAfter(true);
        anim.startAnimation(ta);
        anim.playAnimation();
        anim.loop(true);
        linearLayoutWrapper.addView(anim);
    }


    public void AnhXa() {
        btnBuy = fragmentLayout.findViewById(R.id.btn_order_btn_cart_screen);
        linearLayoutWrapper = fragmentLayout.findViewById(R.id.linearLayout_wrapper_cart_Screen);
        cartScreeb = fragmentLayout.findViewById(R.id.total_cart_screen);
        order_btn_cart_screen = fragmentLayout.findViewById(R.id.btn_order_btn_cart_screen);
        recyclerView = fragmentLayout.findViewById(R.id.recyclerView_card_screen);
    }

    public void setUpCart(List<CartModel> orderModel) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(fragmentLayout.getContext());
        layoutManager.setOrientation(layoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        myRecycleViewAdapter = new CartRecycleViewAdapter((Activity) fragmentLayout.getContext(), R.layout.cart_layout_item, orderModel);
        recyclerView.setAdapter(myRecycleViewAdapter);
        myRecycleViewAdapter.notifyDataSetChanged();
    }

    public void showMessageDialog(String message) {
        androidx.appcompat.app.AlertDialog alert = new androidx.appcompat.app.AlertDialog.Builder(fragmentLayout.getContext())
                .setTitle("Message")
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }
}