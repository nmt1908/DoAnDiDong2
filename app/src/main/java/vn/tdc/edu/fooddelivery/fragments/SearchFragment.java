package vn.tdc.edu.fooddelivery.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.user.MainActivity;
import vn.tdc.edu.fooddelivery.adapters.SearchRecyclerViewAdapter;
import vn.tdc.edu.fooddelivery.api.ProductAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.components.SendDataAndGotoAnotherFragment;
import vn.tdc.edu.fooddelivery.models.ProductModel;

public class SearchFragment extends AbstractFragment implements SearchRecyclerViewAdapter.UserClicListenter {
    private int typeView = 1;
    public static RecyclerView recyclerView;
    private SearchRecyclerViewAdapter myRecycleViewAdapter;
    private List<ProductModel> productsList;
    private ImageButton btn_select_type_view_search_screen;
    private SearchView searchView;
    private boolean flag = false;
    View fragmentLayout = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.seartch_layout, container, false);
        if (productsList == null) {
            productsList = new ArrayList<>();
        }
        anhXa();
        chooseTypeView();
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        btn_select_type_view_search_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventClickTypeView();
            }
        });
        searchActivity();
        return fragmentLayout;
    }


    @Override
    public void onResume() {
        super.onResume();
        Call<List<ProductModel>> callProduct = RetrofitBuilder.getClient().create(ProductAPI.class).findAll();
        callProduct.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.body() != null) {
                    productsList.clear();
                    productsList.addAll(response.body());
                    myRecycleViewAdapter.notifyDataSetChanged();
                    Log.d("api-call", "Fetch product data successfully");
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Log.d("api-call", "Fetch product data fail");
            }
        });
    }


    public void EventClickTypeView() {
        if (typeView % 2 == 0) {
            typeView = 1;
            btn_select_type_view_search_screen.setBackgroundResource((R.drawable.ic_baseline_grid_on_24));
        } else {
            typeView = 2;
            btn_select_type_view_search_screen.setBackgroundResource((R.drawable.ic_baseline_view_list_24));
        }
        chooseTypeView();
    }

    public void chooseTypeView() {
        switch (typeView) {
            case 1:
                //Setup
                StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager1);
                if (myRecycleViewAdapter.cartArrayListOnChange != null) {
                    myRecycleViewAdapter = new SearchRecyclerViewAdapter((Activity) fragmentLayout.getContext(), R.layout.search_layout_item3, myRecycleViewAdapter.cartArrayListOnChange, this::selectedUser);
                } else {
                    myRecycleViewAdapter = new SearchRecyclerViewAdapter((Activity) fragmentLayout.getContext(), R.layout.search_layout_item3, productsList, this::selectedUser);
                }
                recyclerView.setAdapter(myRecycleViewAdapter);
                break;
            case 2:
                //Setup
                GridLayoutManager layoutManager2 = new GridLayoutManager((Activity) fragmentLayout.getContext(), 2);
                recyclerView.setLayoutManager(layoutManager2);
                if (myRecycleViewAdapter.cartArrayListOnChange != null) {
                    myRecycleViewAdapter = new SearchRecyclerViewAdapter((Activity) fragmentLayout.getContext(), R.layout.search_layout_item1, myRecycleViewAdapter.cartArrayListOnChange, this::selectedUser);
                } else {
                    myRecycleViewAdapter = new SearchRecyclerViewAdapter((Activity) fragmentLayout.getContext(), R.layout.search_layout_item1, productsList, this::selectedUser);
                }
                recyclerView.setAdapter(myRecycleViewAdapter);
                break;
        }
        flag = true;
    }


    public void anhXa() {
        btn_select_type_view_search_screen = fragmentLayout.findViewById(R.id.btn_select_type_view_search_screen);
        recyclerView = fragmentLayout.findViewById(R.id.recyclerView_search_screen);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void searchActivity() {
        MainActivity.searchView.setQuery("", false);
        MainActivity.searchView.clearFocus();
        binData();
        MainActivity.searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                myRecycleViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (flag == true) {
                    binData();
                    flag = false;
                }
                myRecycleViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void binData() {
        if (typeView == 1) {
            myRecycleViewAdapter = new SearchRecyclerViewAdapter((Activity) fragmentLayout.getContext(), R.layout.search_layout_item3, productsList, this::selectedUser);
        } else {
            myRecycleViewAdapter = new SearchRecyclerViewAdapter((Activity) fragmentLayout.getContext(), R.layout.search_layout_item1, productsList, this::selectedUser);
        }
        recyclerView.setAdapter(myRecycleViewAdapter);
    }


    @Override
    public void selectedUser(ProductModel cart) {
        if (fragmentLayout.getContext() instanceof Activity) {
            Activity activity = (Activity) fragmentLayout.getContext();
            SendDataAndGotoAnotherFragment.sendToProduceDetail(activity, cart);
        }
    }
}