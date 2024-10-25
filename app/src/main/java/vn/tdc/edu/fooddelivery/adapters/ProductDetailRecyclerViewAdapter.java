package vn.tdc.edu.fooddelivery.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.fragments.user.ProductDetailFragment;
import vn.tdc.edu.fooddelivery.models.ProductModel;

public class ProductDetailRecyclerViewAdapter extends RecyclerView.Adapter<ProductDetailRecyclerViewAdapter.MyViewHolder> {
    private Activity activity;
    private int layout_ID;
    private List<ProductModel> arrayList;
    private onRecyclerViewOnClickListener _onRecyclerViewOnClickListener;

    public ProductDetailRecyclerViewAdapter(Activity activity, int layout_ID, List<ProductModel> arrayList) {
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
        Glide.with(activity).load(cart.getImageUrl())
                .into(holder.imageViewListItem);

        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_onRecyclerViewOnClickListener != null) {
                    if (activity != null) {
                        sendDataForDetailScreen(cart, arrayList);
                    }
                }
            }
        };
    }

    public void sendDataForDetailScreen(ProductModel cart, List<ProductModel> arrayList) {
        ((AbstractActivity) activity).setFragment(ProductDetailFragment.class, R.id.frameLayout, true)
                .setDetailProduct(cart)
                .setArrayList(arrayList);
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
        private ImageView imageViewListItem;
        private View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewListItem = itemView.findViewById(R.id.img_suggestive_detail_screen);
            imageViewListItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
        }
    }

    public interface onRecyclerViewOnClickListener {
        public void onItemRecyclerViewOnClickListener(int productModel);
    }

    public void set_OnRecyclerViewOnClickListener(onRecyclerViewOnClickListener _OnRecyclerViewOnClickListener) {
        this._onRecyclerViewOnClickListener = _OnRecyclerViewOnClickListener;
    }
}
