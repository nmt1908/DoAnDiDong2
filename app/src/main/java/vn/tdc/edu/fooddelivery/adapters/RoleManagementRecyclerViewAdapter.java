package vn.tdc.edu.fooddelivery.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.models.RoleModel;

public class RoleManagementRecyclerViewAdapter extends RecyclerView.Adapter<RoleManagementRecyclerViewAdapter.RoleItemHolder> {

    private AppCompatActivity activity;
    private int layout;
    private List<RoleModel> listRoles;
    private RoleManagementRecyclerViewAdapter.OnRecylerViewItemClickListener recylerViewItemClickListener;

    public void setRecylerViewItemClickListener(RoleManagementRecyclerViewAdapter.OnRecylerViewItemClickListener recylerViewItemClickListener) {
        this.recylerViewItemClickListener = recylerViewItemClickListener;
    }

    public RoleManagementRecyclerViewAdapter(@NonNull AppCompatActivity activity, int layout, @NonNull List<RoleModel> listRoles) {
        this.activity = activity;
        this.layout = layout;
        this.listRoles = listRoles;
    }

    @NonNull
    @Override
    public RoleManagementRecyclerViewAdapter.RoleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        CardView view = (CardView) inflater.inflate(layout, parent, false);
        return new RoleManagementRecyclerViewAdapter.RoleItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoleManagementRecyclerViewAdapter.RoleItemHolder holder, @SuppressLint("RecyclerView") int position) {
        RoleModel roleModel = listRoles.get(position);

        holder.tvCode.setText(roleModel.getCode());
        holder.tvName.setText(roleModel.getName());

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
        return listRoles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return layout;
    }

    public interface OnRecylerViewItemClickListener {
        public void onButtonEditClickListener(int position);

        public void onButtonDeleteClickListener(int position);
    }

    public static class RoleItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvCode;
        private TextView tvName;
        private ImageButton btnEdit;
        private ImageButton btnDelete;

        private View.OnClickListener onClickListener;

        public RoleItemHolder(@NonNull View itemView) {
            super(itemView);
            this.tvCode = itemView.findViewById(R.id.tvCode);
            this.tvName = itemView.findViewById(R.id.tvNameRole);
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
