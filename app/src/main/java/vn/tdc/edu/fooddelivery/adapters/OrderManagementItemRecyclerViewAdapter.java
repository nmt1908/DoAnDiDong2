package vn.tdc.edu.fooddelivery.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.enums.OrderStatus;
import vn.tdc.edu.fooddelivery.enums.Role;
import vn.tdc.edu.fooddelivery.models.OrderModel;
import vn.tdc.edu.fooddelivery.utils.Authentication;
import vn.tdc.edu.fooddelivery.utils.CommonUtils;
import vn.tdc.edu.fooddelivery.utils.FormatCurentcy;

public class OrderManagementItemRecyclerViewAdapter extends RecyclerView.Adapter<OrderManagementItemRecyclerViewAdapter.OrderManagementItemHolder> {
    private Activity activity;
    private int layout;
    private List<OrderModel> listOrders;
    private OnRecylerViewItemClickListener onRecylerViewItemClickListener;


    public void setOnRecylerViewItemClickListener(OnRecylerViewItemClickListener onRecylerViewItemClickListener) {
        this.onRecylerViewItemClickListener = onRecylerViewItemClickListener;
    }

    public interface OnRecylerViewItemClickListener {
        public void onButtonOrderDetailClickListener(int position);
        public void onButtonPhoneClickListener(int position);
        public void onButtonAcceptClickListener(int position);

        public void onButtonDeleteClickListener(int position);
    }


    public OrderManagementItemRecyclerViewAdapter(@NonNull Activity activity, int layout, @NonNull List<OrderModel> listOrders) {
        this.activity = activity;
        this.layout = layout;
        this.listOrders = listOrders;
    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }

    @NonNull
    @Override
    public OrderManagementItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        CardView view = (CardView) inflater.inflate(layout, parent, false);
        return new OrderManagementItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderManagementItemHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderModel orderModel = listOrders.get(position);
        holder.tvOrderId.setText(String.valueOf(orderModel.getId()));
        holder.tvCustomerFullName.setText(orderModel.getCustomer().getFullName());
        holder.tvAddress.setText(orderModel.getAddress());
        holder.tvPhone.setText(orderModel.getPhone());
        holder.tvTotal.setText(FormatCurentcy.formatVietnamCurrency(orderModel.getTotal()) + " (đồng)");
        holder.tvCreatedAt.setText(CommonUtils.convertDateToString(orderModel.getCreatedAt()));

        if (orderModel.getStatus() != OrderStatus.CHUA_XU_LY.getStatus()) {
            if (Authentication.getUserLogin().getRolesString().contains(Role.SHIPPER.getName())
            && orderModel.getStatus() == OrderStatus.DANG_GIAO_HANG.getStatus()) {
                holder.btnAccept.setEnabled(true);
                holder.btnDelete.setEnabled(true);
            } else {
                holder.btnAccept.setEnabled(false);
                if (orderModel.getStatus() == OrderStatus.DA_HUY.getStatus() && Authentication.getUserLogin().getRolesString().contains(Role.ADMIN.getName())) {
                    holder.btnDelete.setEnabled(true);
                } else {
                    holder.btnDelete.setEnabled(false);
                }
            }
        }

        if (Authentication.getUserLogin().getRolesString().contains(Role.SHIPPER.getName())) {
            holder.btnAccept.setText("Giao");
            holder.btnDelete.setText("Huỷ");
        }

        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecylerViewItemClickListener != null) {
                    if (view.getId() == R.id.btnOrderDetail) {
                        onRecylerViewItemClickListener.onButtonOrderDetailClickListener(position);
                    } else if (view.getId() == R.id.btnPhone) {
                        onRecylerViewItemClickListener.onButtonPhoneClickListener(position);
                    } else if (view.getId() == R.id.btnAccept) {
                        onRecylerViewItemClickListener.onButtonAcceptClickListener(position);
                    } else if (view.getId() == R.id.btnDelete) {
                        onRecylerViewItemClickListener.onButtonDeleteClickListener(position);
                    }
                }
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return layout;
    }

    public static class OrderManagementItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvOrderId;
        private TextView tvCustomerFullName;
        private TextView tvAddress;
        private TextView tvPhone;
        private TextView tvTotal;
        private TextView tvCreatedAt;
        private Button btnOrderDetail;
        private Button btnPhone;
        private Button btnAccept;
        private Button btnDelete;

        private View.OnClickListener onClickListener;

        public OrderManagementItemHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvCustomerFullName = itemView.findViewById(R.id.tvCustomerFullName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            btnOrderDetail = itemView.findViewById(R.id.btnOrderDetail);
            btnPhone = itemView.findViewById(R.id.btnPhone);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnOrderDetail.setOnClickListener(this);
            btnPhone.setOnClickListener(this);
            btnAccept.setOnClickListener(this);
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
