package vn.tdc.edu.fooddelivery.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.models.CategoryModel;

public class CategoryManagementRecyclerViewAdapter extends RecyclerView.Adapter<CategoryManagementRecyclerViewAdapter.CategoryItemHolder> {
    private AppCompatActivity activity;
    private int layout;
    private List<CategoryModel> listCategories;

    private  OnRecylerViewItemClickListener recylerViewItemClickListener;

    public void setRecylerViewItemClickListener(OnRecylerViewItemClickListener recylerViewItemClickListener) {
        this.recylerViewItemClickListener = recylerViewItemClickListener;
    }

    public CategoryManagementRecyclerViewAdapter(@NonNull AppCompatActivity activity, int layout, @NonNull List<CategoryModel> listCategories) {
        this.activity = activity;
        this.layout = layout;
        this.listCategories = listCategories;
    }

    @NonNull
    @Override
    public CategoryItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        CardView view = (CardView) inflater.inflate(layout, parent, false);
        return new CategoryItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemHolder holder, @SuppressLint("RecyclerView") int position) {
        CategoryModel categoryModel = listCategories.get(position);
        Glide.with(activity).load(categoryModel.getImageUrl())
                .into(holder.imgCategory);
        holder.tvName.setText(categoryModel.getName());
        holder.tvNumberOfProduct.setText(categoryModel.getNumberOfProduct() + " (sản phẩm)");

        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recylerViewItemClickListener != null) {
                    if (view.getId() == R.id.btnEdit) {
                        recylerViewItemClickListener.onButtonEditClickListener(position);
                    } else if (view.getId() == R.id.btnDelete) {
                        recylerViewItemClickListener.onButtonDeleteClickListener(position);
                    }
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return listCategories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return layout;
    }

    public interface OnRecylerViewItemClickListener {
        public void onButtonEditClickListener(int position);

        public void onButtonDeleteClickListener(int position);
    }

    public static class CategoryItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgCategory;
        private TextView tvName;
        private TextView tvNumberOfProduct;
        private View.OnClickListener onClickListener;

        private ImageButton btnEdit;
        private ImageButton btnDelete;

        public CategoryItemHolder(@NonNull View itemView) {
            super(itemView);
            this.imgCategory = itemView.findViewById(R.id.imgCategory);
            this.tvName = itemView.findViewById(R.id.tvName);
            this.tvNumberOfProduct = itemView.findViewById(R.id.tvNumberOfProduct);
            this.btnEdit = itemView.findViewById(R.id.btnEdit);
            this.btnDelete = itemView.findViewById(R.id.btnDelete);

            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }
}
