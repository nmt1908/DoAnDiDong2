package vn.tdc.edu.fooddelivery.fragments.user;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.adapters.OrderDetailRecyclerViewAdapter;
import vn.tdc.edu.fooddelivery.api.OrderAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.components.ConfirmDialog;
import vn.tdc.edu.fooddelivery.enums.OrderStatus;
import vn.tdc.edu.fooddelivery.enums.Role;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.models.OrderRequestModel;
import vn.tdc.edu.fooddelivery.models.OrderModel;
import vn.tdc.edu.fooddelivery.utils.Authentication;
import vn.tdc.edu.fooddelivery.utils.CommonUtils;
import vn.tdc.edu.fooddelivery.utils.FormatCurentcy;

public class OrderDetailsFragment extends AbstractFragment implements View.OnClickListener {
    private TextView tvCustomerName;
    private TextView tvAddress;
    private TextView tvCreateDate;
    private TextView tvTotal;
    private TextView tvShipperName;
    private TextView tvOrderStatus;
    private LinearLayout layoutBtnAction;
    private LinearLayout layoutShipperInfo;
    private RecyclerView recyclerView;
    private Button btnSuccess;
    private Button btnCancel;
    private OrderDetailRecyclerViewAdapter adapter;
    private OrderModel orderModel;

    private ConfirmDialog confirmDialog;

    public OrderModel getOrderModel() {
        return orderModel;
    }

    public void setOrderModel(OrderModel orderModel) {
        this.orderModel = orderModel;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        tvCustomerName = view.findViewById(R.id.tvCustomerName);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvCreateDate = view.findViewById(R.id.tvCreateDate);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvShipperName = view.findViewById(R.id.tvShipperName);
        tvOrderStatus = view.findViewById(R.id.tvOrderStatus);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutBtnAction = view.findViewById(R.id.layoutBtnAction);
        layoutShipperInfo = view.findViewById(R.id.layoutShipperInfo);
        btnSuccess = view.findViewById(R.id.btnSuccess);
        btnCancel = view.findViewById(R.id.btnCancel);

        btnSuccess.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if (orderModel != null && orderModel.getItems() != null) {
            tvCustomerName.setText(orderModel.getCustomer().getFullName());
            tvCreateDate.setText(CommonUtils.convertDateToString(orderModel.getCreatedAt()));
            tvAddress.setText(orderModel.getAddress());
            tvTotal.setText(FormatCurentcy.formatVietnamCurrency(orderModel.getTotal()));

            if (orderModel.getStatus() != OrderStatus.DANG_GIAO_HANG.getStatus()
                    || Authentication.getUserLogin().getRolesString().contains(Role.ADMIN.getName())) {
                layoutBtnAction.setVisibility(View.GONE);
            }

            if (orderModel.getStatus() == OrderStatus.CHUA_XU_LY.getStatus()) {
                layoutShipperInfo.setVisibility(View.GONE);
            } else {
                if (orderModel.getShipper() != null) {
                    tvShipperName.setText(orderModel.getShipper().getFullName());
                } else {
                    tvShipperName.setText("");
                }
            }

            if (orderModel.getStatus() == OrderStatus.CHUA_XU_LY.getStatus()) {
                tvOrderStatus.setText(OrderStatus.CHUA_XU_LY.getName());
                tvOrderStatus.setTextColor(Color.parseColor("#f1c602"));
            } else if (orderModel.getStatus() == OrderStatus.DANG_GIAO_HANG.getStatus()) {
                tvOrderStatus.setText(OrderStatus.DANG_GIAO_HANG.getName());
                tvOrderStatus.setTextColor(Color.parseColor("#FF00B0FF"));
            } else if (orderModel.getStatus() == OrderStatus.DA_GIAO.getStatus()) {
                tvOrderStatus.setText(OrderStatus.DA_GIAO.getName());
                tvOrderStatus.setTextColor(Color.parseColor("#008132"));
            } else if (orderModel.getStatus() == OrderStatus.DA_HUY.getStatus()) {
                tvOrderStatus.setText(OrderStatus.DA_HUY.getName());
                tvOrderStatus.setTextColor(Color.parseColor("#EB4A3E"));
            }

            adapter = new OrderDetailRecyclerViewAdapter((AppCompatActivity) getActivity(), R.layout.recycler_order_management, orderModel.getItems());

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSuccess || view.getId() == R.id.btnCancel) {
            OrderRequestModel orderRequest = new OrderRequestModel();
            orderRequest.setId(orderModel.getId());
            if (view.getId() == R.id.btnSuccess) {
                confirmDialog = new ConfirmDialog(getActivity());
                confirmDialog.setTitle("Xác nhận giao hàng");
                confirmDialog.setMessage("Bạn có muốn tiếp tục không?");
                confirmDialog.setOnDialogComfirmAction(new ConfirmDialog.DialogComfirmAction() {
                    @Override
                    public void cancel() {
                        confirmDialog.dismiss();
                    }

                    @Override
                    public void ok() {
                        orderRequest.setStatus(OrderStatus.DA_GIAO.getStatus());
                        updateOrderStatus(orderRequest);
                    }
                });

                confirmDialog.show();
            } else {
                confirmDialog = new ConfirmDialog(getActivity());
                confirmDialog.setTitle("Xác nhận huỷ đơn hàng");
                confirmDialog.setMessage("Bạn có muốn tiếp tục không?");
                confirmDialog.setOnDialogComfirmAction(new ConfirmDialog.DialogComfirmAction() {
                    @Override
                    public void cancel() {
                        confirmDialog.dismiss();
                    }

                    @Override
                    public void ok() {
                        orderRequest.setStatus(OrderStatus.DA_HUY.getStatus());
                        updateOrderStatus(orderRequest);
                    }
                });

                confirmDialog.show();
            }
        }
    }

    private void updateOrderStatus(OrderRequestModel orderRequest) {
        Call<OrderModel> call = RetrofitBuilder.getClient().create(OrderAPI.class).update(orderRequest);
        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                    if (response.body().getStatus() == OrderStatus.DA_GIAO.getStatus()) {
                        ((AbstractActivity) getActivity()).showMessageDialog("Đơn hàng đã giao thành công");
                    } else if (response.body().getStatus() == OrderStatus.DA_HUY.getStatus()) {
                        ((AbstractActivity) getActivity()).showMessageDialog("Huỷ đơn hàng thành công");
                    }
                    confirmDialog.dismiss();
                    getActivity().onBackPressed();
                } else {
                    ((AbstractActivity) getActivity()).showMessageDialog("Hệ thống đang bảo trì");
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                ((AbstractActivity) getActivity()).showMessageDialog("Hệ thống đang bảo trì");
            }
        });
    }
}