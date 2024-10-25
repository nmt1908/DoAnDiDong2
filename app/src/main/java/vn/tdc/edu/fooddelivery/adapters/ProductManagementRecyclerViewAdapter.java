package vn.tdc.edu.fooddelivery.adapters;

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
import vn.tdc.edu.fooddelivery.models.ProductModel;

public class ProductManagementRecyclerViewAdapter extends RecyclerView.Adapter<ProductManagementRecyclerViewAdapter.ProductItemHolder> {
    private AppCompatActivity activity;
    private int layout;
    private List<ProductModel> listProducts;
    private OnRecylerViewItemClickListener recylerViewItemClickListener;

    public void setRecylerViewItemClickListener(OnRecylerViewItemClickListener recylerViewItemClickListener) {
        this.recylerViewItemClickListener = recylerViewItemClickListener;
    }

    public ProductManagementRecyclerViewAdapter(@NonNull AppCompatActivity activity, int layout, @NonNull List<ProductModel> listProducts) {
        this.activity = activity;
        this.layout = layout;
        this.listProducts = listProducts;
    }

    @NonNull
    @Override
    public ProductItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        CardView view = (CardView) inflater.inflate(layout, parent, false);
        return new ProductItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemHolder holder, int position) {
        ProductModel productModel = listProducts.get(position);
        Glide.with(activity).load(productModel.getImageUrl())
                .into(holder.imgProduct);
        holder.tvName.setText(productModel.getName());
        holder.tvPrice.setText(String.format("%,d VND", productModel.getPrice()));
        holder.tvQuantity.setText(String.format("%d (%s)", productModel.getQuantity(), productModel.getUnit()));

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
        return listProducts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return layout;
    }

    public interface OnRecylerViewItemClickListener {
        public void onButtonEditClickListener(int position);

        public void onButtonDeleteClickListener(int position);
    }

    public static class ProductItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgProduct;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvQuantity;
        private ImageButton btnEdit;
        private ImageButton btnDelete;

        private View.OnClickListener onClickListener;

        public ProductItemHolder(@NonNull View itemView) {
            super(itemView);
            this.imgProduct = itemView.findViewById(R.id.imgProduct);
            this.tvName = itemView.findViewById(R.id.tvName);
            this.tvPrice = itemView.findViewById(R.id.tvPrice);
            this.tvQuantity = itemView.findViewById(R.id.tvQuantity);
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
