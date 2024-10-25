package vn.tdc.edu.fooddelivery.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.components.ConfirmDialog;
import vn.tdc.edu.fooddelivery.fragments.user.NotificationFragment;
import vn.tdc.edu.fooddelivery.models.NotificationModel;
import vn.tdc.edu.fooddelivery.utils.CommonUtils;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.MyViewHolder> {
    private Activity activity;

    private NotificationFragment notificationFragment = new NotificationFragment();
    private int layout_ID;
    private List<NotificationModel> arrayList;
    //B2: Definition variable
    private onRecyclerViewOnClickListener onRecyclerViewOnClickListener;

    public NotificationRecyclerViewAdapter(Activity activity, int layout_ID, List<NotificationModel> arrayList) {
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

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NotificationModel notification = arrayList.get(position);
        holder.txt_title.setText(notification.getTitle());
        holder.txt_timing.setText(String.valueOf(CommonUtils.convertDateToString(notification.getCreatedAt())));
        holder.txt_content_notification.setText(notification.getContent());
        if (notification.getStatus() != 0) {
            holder.txt_title.setTextColor(R.color.black);
            holder.img.setImageDrawable(activity.getResources().getDrawable(R.drawable.khong_thong_bao, activity.getTheme()));
            holder.linearLayout.setBackgroundResource(R.color.black);
        } else {
            holder.linearLayout.setBackgroundResource(R.color.green);
            holder.img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_baseline_circle_notifications_24, activity.getTheme()));
        }

        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecyclerViewOnClickListener != null) {
                    onRecyclerViewOnClickListener.onItemRecyclerViewClickListener(position, holder.itemView);
                }
            }
        };

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogToConfirm(notification);
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogToConfirm(notification);
            }
        });
    }

    public void deleteNotifycations(NotificationModel notification) {
        notificationFragment.deleteNotifications(notification);
    }

    public void createDialogToConfirm(NotificationModel NotificationModel) {
        ConfirmDialog confirmDialog = new ConfirmDialog(activity);
        confirmDialog.setTitle("Đăng xuất");
        confirmDialog.setMessage("Đăng xuất khỏi tài khoản của bạn ?");
        confirmDialog.setOnDialogComfirmAction(new ConfirmDialog.DialogComfirmAction() {
            @Override
            public void cancel() {
                confirmDialog.dismiss();
            }

            @Override
            public void ok() {
                deleteNotifycations(NotificationModel);
                confirmDialog.dismiss();
            }
        });

        confirmDialog.show();
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
        private TextView txt_title;
        private TextView txt_timing;
        private TextView txt_content_notification;
        private ImageView img;

        private View currentView;
        private View.OnClickListener onClickListener;

        private LinearLayout linearLayout;

        private ImageButton imageButton;

        public MyViewHolder(@NonNull View v) {
            super(v);
            currentView = v;
            txt_title = v.findViewById(R.id.txt_title_notification_screen);
            txt_timing = v.findViewById(R.id.txt_timing_notification_screen);
            txt_content_notification = v.findViewById(R.id.txt_content_notification_screen);
            img = v.findViewById(R.id.img_notification_screen);
            linearLayout = v.findViewById(R.id.linear_item_notifycation);
            imageButton = v.findViewById(R.id.imgBtn_item_notifycation);

            txt_title.setOnClickListener(this);
            txt_timing.setOnClickListener(this);
            txt_content_notification.setOnClickListener(this);
            img.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
        }
    }

    public interface onRecyclerViewOnClickListener {
        public void onItemRecyclerViewClickListener(int position, View cardView);

    }


    public void setOnRecyclerViewOnClickListener(NotificationRecyclerViewAdapter.onRecyclerViewOnClickListener onRecyclerViewOnClickListener) {
        this.onRecyclerViewOnClickListener = onRecyclerViewOnClickListener;
    }
}
