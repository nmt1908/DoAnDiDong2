package vn.tdc.edu.fooddelivery.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.fragments.user.HomeFragment;
import vn.tdc.edu.fooddelivery.models.CategoryModel;

public class HomeCategoryRecyclerViewAdapter extends RecyclerView.Adapter<HomeCategoryRecyclerViewAdapter.MyViewHolder> {
    private onRecyclerViewOnClickListener onRecyclerViewOnClickListener;
    private Activity activity;
    private int layout_id;

    public List<CategoryModel> arrayList;

    private HomeFragment homeFragment = new HomeFragment();

    public void setArrayList(ArrayList<CategoryModel> arrayList) {
        this.arrayList = arrayList;
    }

    public HomeCategoryRecyclerViewAdapter(Activity activity, int layout_id, List<CategoryModel> arrayList) {
        this.activity = activity;
        this.layout_id = layout_id;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        CardView cardView = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(cardView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CategoryModel cart = arrayList.get(position);
        holder.txt_name.setText(cart.getName());
        //holder.img.setImageDrawable(activity.getResources().getDrawable(cart.getImage(), activity.getTheme()));
        Glide.with(activity).load(cart.getImageUrl())
                .into(holder.img);
        //B3: Event click
        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecyclerViewOnClickListener != null) {
                    onRecyclerViewOnClickListener.onItemRecyclerViewOnClickListener(position, holder.itemView);
                }
            }
        };
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return layout_id;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img;
        private TextView txt_name;
        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View v) {
            super(v);
            img = v.findViewById(R.id.img_search_item_type);
            txt_name = v.findViewById(R.id.txt_name_search_item_type);
            //Catch event
            img.setOnClickListener(this);
            txt_name.setOnClickListener(this);
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

    public void setonRecyclerViewOnClickListener(onRecyclerViewOnClickListener
                                                         onRecyclerViewOnClickListener) {
        this.onRecyclerViewOnClickListener = onRecyclerViewOnClickListener;
    }
}