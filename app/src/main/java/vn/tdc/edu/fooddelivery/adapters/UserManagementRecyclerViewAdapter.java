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
import vn.tdc.edu.fooddelivery.models.UserModel;
import vn.tdc.edu.fooddelivery.utils.CommonUtils;

public class UserManagementRecyclerViewAdapter extends RecyclerView.Adapter<UserManagementRecyclerViewAdapter.UserAccountItemHolder> {
    private AppCompatActivity activity;
    private int layout;
    private List<UserModel> listUsers;
    private OnRecylerViewItemClickListener recylerViewItemClickListener;

    public void setRecylerViewItemClickListener(OnRecylerViewItemClickListener recylerViewItemClickListener) {
        this.recylerViewItemClickListener = recylerViewItemClickListener;
    }

    public UserManagementRecyclerViewAdapter(@NonNull AppCompatActivity activity, int layout, @NonNull List<UserModel> listUsers) {
        this.activity = activity;
        this.layout = layout;
        this.listUsers = listUsers;
    }

    @NonNull
    @Override
    public UserAccountItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        CardView cardView = (CardView) inflater.inflate(layout,parent, false);
        return new UserAccountItemHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAccountItemHolder holder, @SuppressLint("RecyclerView") int position) {
        UserModel userModel = listUsers.get(position);
        Glide.with(activity).load(userModel.getImageUrl())
                .into(holder.imgUser);
        holder.tvFullName.setText(userModel.getFullName());
        holder.tvEmail.setText(userModel.getEmail());
        holder.tvCreatedAt.setText(CommonUtils.convertDateToString(userModel.getCreatedAt()));

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
        return listUsers.size();
    }

    @Override
    public int getItemViewType(int position) {
        return layout;
    }

    public interface OnRecylerViewItemClickListener {
        public void onButtonEditClickListener(int position);

        public void onButtonDeleteClickListener(int position);
    }

    public static class UserAccountItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgUser;
        private TextView tvFullName;
        private TextView tvEmail;
        private TextView tvCreatedAt;
        private ImageButton btnEdit;
        private ImageButton btnDelete;

        private View.OnClickListener onClickListener;

        public UserAccountItemHolder(@NonNull View itemView) {
            super(itemView);
            this.imgUser = itemView.findViewById(R.id.imgUser);
            this.tvFullName = itemView.findViewById(R.id.tvFullName);
            this.tvEmail = itemView.findViewById(R.id.tvEmail);
            this.tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
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
