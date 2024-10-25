package vn.tdc.edu.fooddelivery.fragments.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.adapters.OrderManagementItemRecyclerViewAdapter;
import vn.tdc.edu.fooddelivery.api.NotificationAPI;
import vn.tdc.edu.fooddelivery.api.OrderAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.components.AssigntOrderPopupToStaff;
import vn.tdc.edu.fooddelivery.components.ConfirmDialog;
import vn.tdc.edu.fooddelivery.enums.OrderStatus;
import vn.tdc.edu.fooddelivery.enums.Role;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.fragments.user.OrderDetailsFragment;
import vn.tdc.edu.fooddelivery.models.NotificationModel;
import vn.tdc.edu.fooddelivery.models.OrderRequestModel;
import vn.tdc.edu.fooddelivery.models.OrderModel;
import vn.tdc.edu.fooddelivery.models.UserModel;
import vn.tdc.edu.fooddelivery.utils.Authentication;

public class OrdersListFragment extends AbstractFragment implements OrderManagementItemRecyclerViewAdapter.OnRecylerViewItemClickListener {
    public final static String ORDER_MODEL = "ordermodel";
    private OrderManagementItemRecyclerViewAdapter adapter;
    private List<OrderModel> listOrders;
    private AssigntOrderPopupToStaff assign;
    private RecyclerView recyclerViewOrder;
    private ConfirmDialog confirmDialog;

    public List<OrderModel> getListOrders() {
        return listOrders;
    }

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        recyclerViewOrder = view.findViewById(R.id.recyclerViewOrder);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderListFromAPI(status);
        Log.d("fragment-life-cycler", "on resume order list");
    }

    @Override
    public void onButtonOrderDetailClickListener(int position) {
            ((AbstractActivity) getActivity()).setFragment(OrderDetailsFragment.class, R.id.frameLayout, true)
                    .setOrderModel(listOrders.get(position));
    }

    @Override
    public void onButtonPhoneClickListener(int position) {
        String phone = "tel:" + listOrders.get(position).getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phone));
        getActivity().startActivity(intent);
    }

    @Override
    public void onButtonAcceptClickListener(int position) {
        OrderRequestModel orderRequest = new OrderRequestModel();
        orderRequest.setId(listOrders.get(position).getId());
        if (Authentication.getUserLogin().getRolesString().contains(Role.ADMIN.getName())) {
            if (getActivity() != null) {
                assign = new AssigntOrderPopupToStaff(getActivity());
                assign.setOnAssignmentDialogAction(new AssigntOrderPopupToStaff.DialogAssignDialogAction() {
                    @Override
                    public void cancel() {
                        assign.dismiss();
                    }

                    @Override
                    public void ok(UserModel shipper) {
                        orderRequest.setStatus(OrderStatus.DANG_GIAO_HANG.getStatus());
                        orderRequest.setShipperId(shipper.getId());
                        assignmentOrderToShipper(orderRequest, position);
                    }
                });

                assign.show();
            }
        } else if (Authentication.getUserLogin().getRolesString().contains(Role.SHIPPER.getName())) {
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
                    updateOrder(orderRequest, position);
                }
            });

            confirmDialog.show();
        }
    }

    @Override
    public void onButtonDeleteClickListener(int position) {
        OrderRequestModel orderRequest = new OrderRequestModel();
        orderRequest.setId(listOrders.get(position).getId());
        if (Authentication.getUserLogin().getRolesString().contains(Role.ADMIN.getName())) {
            confirmDialog = new ConfirmDialog(getActivity());
            confirmDialog.setTitle("Xác nhận");
            confirmDialog.setMessage("Dữ liệu đã xoá không thể hoàn tác.\nBạn có muốn tiếp tục không?");
            confirmDialog.setOnDialogComfirmAction(new ConfirmDialog.DialogComfirmAction() {
                @Override
                public void cancel() {
                    confirmDialog.dismiss();
                }

                @Override
                public void ok() {
                    deleteOrder(position);
                    confirmDialog.dismiss();
                }
            });

            confirmDialog.show();
        } else if (Authentication.getUserLogin().getRolesString().contains(Role.SHIPPER.getName())) {
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
                    updateOrder(orderRequest, position);
                }
            });

            confirmDialog.show();
        }
    }

    private void assignmentOrderToShipper(OrderRequestModel orderRequest, int position) {
        Call<OrderModel> call = RetrofitBuilder.getClient().create(OrderAPI.class).update(orderRequest);

        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                    ((AbstractActivity) getActivity()).showMessageDialog("Đơn hàng đã được bàn giao cho nhân viên");

                    listOrders.remove(listOrders.get(position));
                    adapter.notifyItemRemoved(position);
                    assign.dismiss();

                    NotificationModel notificationModel = new NotificationModel();
                    notificationModel.setUser_id(response.body().getShipper().getId());
                    notificationModel.setTitle("Nhiệm vụ mới");
                    notificationModel.setContent("Quản trị viên vừa giao đơn hàng mới cho bạn. Mã đơn hàng là: #" + response.body().getId());
                    addNotification(notificationModel);
                } else {
                    ((AbstractActivity) getActivity()).showMessageDialog("Thao tác không thành công");
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                ((AbstractActivity) getActivity()).showMessageDialog("Thao tác không thành công");
            }
        });
    }

    private void updateOrder(OrderRequestModel orderRequest, int position) {
        Call<OrderModel> call = RetrofitBuilder.getClient().create(OrderAPI.class).update(orderRequest);
        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                    listOrders.remove(listOrders.get(position));
                    adapter.notifyItemRemoved(position);
                    confirmDialog.dismiss();
                    if (response.body().getStatus() == OrderStatus.DA_GIAO.getStatus()) {
                        ((AbstractActivity) getActivity()).showMessageDialog("Đơn hàng đã giao thành công");
                        NotificationModel notificationModel = new NotificationModel();
                        notificationModel.setUser_id(response.body().getCustomer().getId());
                        notificationModel.setTitle("Đơn hàng đã được giao thành công");
                        notificationModel.setContent("Đơn hàng đã được giao thành công. Cảm ơn quý khách đã đặt món từ nhà hàng chúng tôi. Chúc quý khách ngon miệng. Trân trọng !");
                        addNotification(notificationModel);
                    } else if (response.body().getStatus() == OrderStatus.DA_HUY.getStatus()) {
                        ((AbstractActivity) getActivity()).showMessageDialog("Đã huỷ đơn hàng thành công");
                        NotificationModel notificationModel = new NotificationModel();
                        notificationModel.setUser_id(response.body().getCustomer().getId());
                        notificationModel.setTitle("Đơn hàng đã huỷ thành công");
                        notificationModel.setContent("Đơn hàng số #" + response.body().getId()
                        + " đã huỷ thành công. Cảm ơn quý khách đã đặt món từ nhà hàng chúng tôi. Trân trọng !");
                        addNotification(notificationModel);
                    }
                } else {
                    ((AbstractActivity) getActivity()).showMessageDialog("Thao tác không thành công");
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                ((AbstractActivity) getActivity()).showMessageDialog("Thao tác không thành công");
            }
        });
    }

    private void deleteOrder(int position) {
        OrderModel orderModel = listOrders.get(position);
        Call<OrderModel> call = RetrofitBuilder.getClient().create(OrderAPI.class).delete(orderModel.getId());
        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    ((AbstractActivity) getActivity()).showMessageDialog("Xoá đơn hàng thành công");
                    listOrders.remove(orderModel);
                    adapter.notifyItemRemoved(position);
                } else {
                    ((AbstractActivity) getActivity()).showMessageDialog("Xoá đơn hàng thất bại");
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                ((AbstractActivity) getActivity()).showMessageDialog("Xoá đơn hàng thất bại");
            }
        });
    }

    private void getOrderListFromAPI(Integer status) {
        Call<List<OrderModel>> call = null;
        if (Authentication.getUserLogin().getRolesString().contains(Role.SHIPPER.getName())) {
            call = RetrofitBuilder.getClient().create(OrderAPI.class).findAll(status, Authentication.getUserLogin().getId());
        } else {
            call = RetrofitBuilder.getClient().create(OrderAPI.class).findAll(status);
        }

        call.enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (listOrders == null) {
                        listOrders = new ArrayList<>();
                    }

                    listOrders.clear();
                    listOrders.addAll(response.body());

                    adapter = new OrderManagementItemRecyclerViewAdapter(getActivity(), R.layout.recycler_order_management, listOrders);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewOrder.setLayoutManager(layoutManager);
                    recyclerViewOrder.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                    adapter.setOnRecylerViewItemClickListener(OrdersListFragment.this);
                }
            }

            @Override
            public void onFailure(Call<List<OrderModel>> call, Throwable t) {
                Log.d("api-error", t.getCause().getMessage());
            }
        });
    }

    private void addNotification(NotificationModel notificationModel) {
        Call<NotificationModel> call = RetrofitBuilder.getClient().create(NotificationAPI.class).save(notificationModel);
        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                    Log.d("notification", "added notification");
                } else {
                    Log.d("notification", "error adding notification");
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                Log.d("notification", "error adding notification");
            }
        });
    }
}