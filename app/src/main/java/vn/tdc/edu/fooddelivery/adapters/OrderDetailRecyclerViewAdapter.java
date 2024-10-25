package vn.tdc.edu.fooddelivery.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.models.OrderItemModel;
import vn.tdc.edu.fooddelivery.utils.FormatCurentcy;

public class OrderDetailRecyclerViewAdapter extends RecyclerView.Adapter<OrderDetailRecyclerViewAdapter.OrderDetailItemHolder>{
    private AppCompatActivity activity;
    private int layout;
    private List<OrderItemModel> listOrderItems;

    public OrderDetailRecyclerViewAdapter(@NonNull AppCompatActivity activity, int layout, @NonNull List<OrderItemModel> listOrderItems) {
        this.activity = activity;
        this.layout = layout;
        this.listOrderItems = listOrderItems;
    }

    @NonNull
    @Override
    public OrderDetailItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView view = (CardView) activity.getLayoutInflater().inflate(R.layout.recycler_order_detail, parent, false);
        return new OrderDetailItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailItemHolder holder, int position) {
        OrderItemModel orderItemModel = listOrderItems.get(position);
        Glide.with(activity).load(orderItemModel.getProductModel().getImageUrl())
                .into(holder.imgProduct);
        String detail = FormatCurentcy.formatVietnamCurrency(orderItemModel.getPrice()) + " x "
                + orderItemModel.getQuantity()
                + " = " + FormatCurentcy.formatVietnamCurrency( orderItemModel.getSubTotal());
        holder.tvDetail.setText(detail);
    }

    @Override
    public int getItemCount() {
        return listOrderItems.size();
    }


    class OrderDetailItemHolder  extends RecyclerView.ViewHolder  {
        private ImageView imgProduct;
        private TextView tvDetail;
        private TextView tvName;

        public OrderDetailItemHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
