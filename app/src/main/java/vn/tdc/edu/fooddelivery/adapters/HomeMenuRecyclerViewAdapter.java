package vn.tdc.edu.fooddelivery.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.user.MainActivity;
import vn.tdc.edu.fooddelivery.components.CreateStart;
import vn.tdc.edu.fooddelivery.components.SendDataAndGotoAnotherFragment;
import vn.tdc.edu.fooddelivery.fragments.user.CartFragment;
import vn.tdc.edu.fooddelivery.fragments.user.HomeFragment;
import vn.tdc.edu.fooddelivery.models.ItemCartModel;
import vn.tdc.edu.fooddelivery.models.ProductModel;
import vn.tdc.edu.fooddelivery.models.UserModel;
import vn.tdc.edu.fooddelivery.utils.Authentication;

public class HomeMenuRecyclerViewAdapter extends RecyclerView.Adapter<HomeMenuRecyclerViewAdapter.MyViewHolder> {

    private SendDataAndGotoAnotherFragment sendDataAndGotoDetailScreen = new SendDataAndGotoAnotherFragment();
    private Activity activity;
    private HomeFragment homeFragment;
    private int layout_ID;
    private List<ProductModel> arrayList;
    private onRecyclerViewOnClickListener _onRecyclerViewOnClickListener;
    private int flag = 1;

    private MainActivity mainActivity = null;


    public HomeMenuRecyclerViewAdapter(Activity activity, int layout_ID, List<ProductModel> arrayList) {
        this.activity = activity;
        this.layout_ID = layout_ID;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        CardView cardView = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductModel cart = arrayList.get(position);
        holder.txt_name.setText(cart.getName());
        holder.txt_price.setText(String.valueOf(cart.getPrice()) + " đ");
        holder.txt_qtyStart.setText("(" + String.valueOf(cart.getRating()) + ")");
        holder.linearLayout.removeAllViews();
        CreateStart.renderStart(holder.linearLayout, cart, activity);
        Glide.with(activity).load(cart.getImageUrl())
                .into(holder.imageView);
        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_onRecyclerViewOnClickListener != null) {
                    flag = 2;
                    //MARK
                    SendDataAndGotoAnotherFragment.sendToProduceDetail(activity, cart);
                }
            }
        };
        if (flag == 1) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendDataAndGotoAnotherFragment.sendToProduceDetail(activity, cart);
                }
            });
        }
        buttonBuyEventClick(holder, cart);
    }

    public void buttonBuyEventClick(MyViewHolder holder, ProductModel cart) {
        holder.btn_buy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UserModel userModel = Authentication.getUserLogin();
                int userId = userModel.getId();
                CartFragment cartFragment1 = new CartFragment();
                ItemCartModel carstModel = new ItemCartModel();
                if (cart.getId() != null) {
                    carstModel.setProduct_id(cart.getId());
                    carstModel.setUser_id(userId);
                    carstModel.setQuantity(1);
                    cartFragment1.updateCart(carstModel,null);
                    showMessageDialog("Đặt hàng thành công");
                }
            }
        });
    }

    public void showMessageDialog(String message) {
        androidx.appcompat.app.AlertDialog alert = new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setTitle("Message")
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return layout_ID;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        public LinearLayout linearLayout;
        private TextView txt_name;
        private TextView txt_price;
        private TextView txt_qtyStart;
        private Button btn_buy;
        private View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View v) {
            super(v);
            linearLayout = v.findViewById(R.id.linearlayout_rating_wrapper);
            imageView = v.findViewById(R.id.img_menu_home_screen);
            txt_name = v.findViewById(R.id.txt_name_menu_home_screen);
            txt_price = v.findViewById(R.id.txt_price_menu_home_screen);
            txt_qtyStart = v.findViewById(R.id.txt_qty_start_menu_home_screen);
            btn_buy = v.findViewById(R.id.btn_buy_menu_home_screen);

            imageView.setOnClickListener(this);
            txt_name.setOnClickListener(this);
            txt_price.setOnClickListener(this);
            txt_qtyStart.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
        }
    }

    public interface onRecyclerViewOnClickListener {
        public void onItemRecyclerViewOnClickListener(int p, View CardView);
    }

    public void set_OnRecyclerViewOnClickListener(onRecyclerViewOnClickListener _OnRecyclerViewOnClickListener) {
        this._onRecyclerViewOnClickListener = _OnRecyclerViewOnClickListener;
    }
}
