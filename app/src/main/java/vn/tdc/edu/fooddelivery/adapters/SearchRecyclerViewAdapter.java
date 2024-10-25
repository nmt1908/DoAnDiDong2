package vn.tdc.edu.fooddelivery.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.models.ProductModel;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.MyViewHolder> implements Filterable {
    private Activity activity;
    private int layout_id;
    public List<ProductModel> arrayList;
    private List<ProductModel> arrayListOld;
    public static List<ProductModel> cartArrayListOnChange;
    public UserClicListenter userClicListenter;

    public void setArrayList(List<ProductModel> arrayList) {
        this.arrayList = arrayList;
    }

    public SearchRecyclerViewAdapter(Activity activity, int layout_id, List<ProductModel> arrayList, UserClicListenter userClicListenter) {
        this.activity = activity;
        this.layout_id = layout_id;
        this.arrayList = arrayList;
        arrayListOld = arrayList;
        this.userClicListenter = userClicListenter;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        CardView cardView = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductModel cart = arrayList.get(position);
        holder.txt_name.setText(cart.getName());
        Glide.with(activity).load(cart.getImageUrl())
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userClicListenter.selectedUser(cart);
            }
        });
    }


    //Definitions getType
    @Override
    public int getItemViewType(int position) {
        return layout_id;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView txt_name;

        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View v) {
            super(v);
            img = v.findViewById(R.id.img_search_item_type);
            txt_name = v.findViewById(R.id.txt_name_search_item_type);
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.values = arrayListOld;
                    filterResults.count = arrayListOld.size();
                } else {
                    String searchStr = constraint.toString().toLowerCase();
                    List<ProductModel> userModels = new ArrayList<>();
                    for (ProductModel userModel : arrayListOld) {
                        if (containsVietnameseWithAccents(userModel.getName(), searchStr)) {
                            userModels.add(userModel);
                        }
                    }
                    filterResults.values = userModels;
                    filterResults.count = userModels.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults Results) {
                arrayList = (List<ProductModel>) Results.values;
                cartArrayListOnChange = arrayList;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    private boolean containsVietnameseWithAccents(String input, String searchStr) {
        String normalizedInput = normalizeString(input);
        String normalizedSearchStr = normalizeString(searchStr);
        return normalizedInput.toLowerCase().contains(normalizedSearchStr.toLowerCase());
    }

    private String normalizeString(String input) {
        String normalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalizedString = normalizedString.replaceAll("\\p{M}", ""); // Loại bỏ dấu
        return normalizedString;
    }


    public interface UserClicListenter {
        void selectedUser(ProductModel userModel);
    }

}
