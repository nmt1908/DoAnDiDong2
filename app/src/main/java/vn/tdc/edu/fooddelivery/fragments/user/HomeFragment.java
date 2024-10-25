package vn.tdc.edu.fooddelivery.fragments.user;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.user.MainActivity;
import vn.tdc.edu.fooddelivery.adapters.HomeMenuRecyclerViewAdapter;
import vn.tdc.edu.fooddelivery.adapters.HomeCategoryRecyclerViewAdapter;
import vn.tdc.edu.fooddelivery.api.CategoryAPI;
import vn.tdc.edu.fooddelivery.api.ProductAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.components.ToastCustome;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.models.CategoryModel;
import vn.tdc.edu.fooddelivery.models.ProductModel;
import vn.tdc.edu.fooddelivery.utils.FileUtils;

public class HomeFragment extends AbstractFragment {
    private int i = 0;

    private static LayoutInflater layoutInflater = null;
    private static View fragmentLayout = null;

    private ViewFlipper viewFlipper;
    private RecyclerView recyclerView_category;
    private RecyclerView recyclerView_menu;
    private HomeMenuRecyclerViewAdapter myAdapterMenu;
    private HomeCategoryRecyclerViewAdapter myAdapterCategories;
    private SearchView searchView;
    private TextView txtMenu;
    private NestedScrollView nestedScrollView;
    private FloatingActionButton fab;
    private CategoryModel categoryHasChoose;
    private int selectedRow = -1;
    private TextView previousCartViewItemClicked;
    List<ProductModel> productsList;
    List<CategoryModel> categoriesList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.fragment_home, container, false);
        layoutInflater = getLayoutInflater();
        if (productsList == null) {
            productsList = new ArrayList<>();
        }
        if (categoriesList == null) {
            categoriesList = new ArrayList<>();
        }
        anhXa();
        setInvisibleFab();
        ActionViewFlipper();
        ClickEventMenu();
        ClickEventCategory();
        //Event scroll up
        ClickEventFab();
        catchEventScrollNestedScrollView();
        return fragmentLayout;
    }


    @Override
    public void onResume() {
        super.onResume();
        Call<List<ProductModel>> callProduct = RetrofitBuilder.getClient().create(ProductAPI.class).findAll();
        Call<List<CategoryModel>> callCategories = RetrofitBuilder.getClient().create(CategoryAPI.class).findAll(null);
        callProduct.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.body() != null) {
                    productsList.clear();
                    productsList.addAll(response.body());
                    myAdapterMenu.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Log.d("api-call", "Fetch product data fail");
            }
        });
        callCategories.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.body() != null) {
                    categoriesList.clear();
                    categoriesList.addAll(response.body());
                    myAdapterCategories.notifyDataSetChanged();
                    Log.d("api-call", "Fetch product data successfully");
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.d("api-call", "Fetch product data fail");
            }
        });
    }

    public void catchEventScrollNestedScrollView() {
        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY >= 5) {
                    setVisibleFab();
                } else {
                    setInvisibleFab();
                }
            }
        });
    }

    public void setInvisibleFab() {
        fab.setVisibility(View.INVISIBLE);
    }

    public void setVisibleFab() {
        fab.setVisibility(View.VISIBLE);
    }

    private void ClickEventFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollView.smoothScrollTo(0, 0); // Cuộn lên trên
                    }
                });
            }
        });
    }


    public void setUpSystem() {
        recyclerView_menu.setFocusable(false);
        recyclerView_menu.setNestedScrollingEnabled(false);
    }


    public void anhXa() {
        nestedScrollView = fragmentLayout.findViewById(R.id.nestedScrollView);
        fab = fragmentLayout.findViewById(R.id.fab_btn);
        txtMenu = fragmentLayout.findViewById(R.id.txt_menu_home_screen);
        recyclerView_menu = fragmentLayout.findViewById(R.id.recyclerView_menu_home_screen);
        recyclerView_category = fragmentLayout.findViewById(R.id.recyclerView_category_home_screen);
        viewFlipper = fragmentLayout.findViewById(R.id.viewFlipper_home_screen);
        //---------------------Setup category recyclerView------------------------//
        binDataCategory();
        //-------------------------Setup menu recyclerView-------------------------//
        binDataMenu();
    }

    public void binDataCategory() {
        myAdapterCategories = new HomeCategoryRecyclerViewAdapter((Activity) fragmentLayout.getContext(), R.layout.home_layout_category_item, (ArrayList<CategoryModel>) categoriesList);
        recyclerView_category.setAdapter(myAdapterCategories);
    }


    public void binDataMenu() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(fragmentLayout.getContext());
        layoutManager.setOrientation(layoutManager.HORIZONTAL);
        recyclerView_category.setLayoutManager(layoutManager);
        //Setup menu recyclerView
        grifViewList(productsList);
    }

    public void grifViewList(List<ProductModel> arrayList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(fragmentLayout.getContext(), 2);
        recyclerView_menu.setLayoutManager(gridLayoutManager);
        myAdapterMenu = new HomeMenuRecyclerViewAdapter((Activity) fragmentLayout.getContext(), R.layout.home_layout_menu_item, arrayList);
        recyclerView_menu.setAdapter(myAdapterMenu);
    }

    //----------------------------------Banner------------------------//
    private void ActionViewFlipper() {
        ArrayList<Integer> mangQuangCao = new ArrayList<Integer>();
        mangQuangCao.add(R.drawable.banner_1);
        mangQuangCao.add(R.drawable.banner_2);
        mangQuangCao.add(R.drawable.banner_3);
        mangQuangCao.add(R.drawable.banner_4);
        mangQuangCao.add(R.drawable.banner_5);
        mangQuangCao.add(R.drawable.banner_6);
        mangQuangCao.add(R.drawable.banner_7);

        for (int i = 0; i < mangQuangCao.size(); i++) {
            ImageView imageView = new ImageView(fragmentLayout.getContext().getApplicationContext());
            Glide.with(fragmentLayout.getContext().getApplicationContext()).load(mangQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3500);
        viewFlipper.setAutoStart(true);
        Animation animation_in = AnimationUtils.loadAnimation(fragmentLayout.getContext().getApplicationContext(), R.anim.slide_in);
        Animation animation_out = AnimationUtils.loadAnimation(fragmentLayout.getContext().getApplicationContext(), R.anim.slide_out);
        viewFlipper.setInAnimation(animation_in);
        viewFlipper.setOutAnimation(animation_out);
    }

    //----------------------Catch event click----------------------------//
    public void ClickEventMenu() {
        myAdapterMenu.set_OnRecyclerViewOnClickListener(new HomeMenuRecyclerViewAdapter.onRecyclerViewOnClickListener() {
            @Override
            public void onItemRecyclerViewOnClickListener(int p, View CardView) {

            }
        });
    }

    public <T> T setFragment(Class<T> tClass, int layout, boolean addToBackStack) {
        T fragment = null;
        try {
            fragment = tClass.newInstance();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction()
                    .replace(layout, (Fragment) fragment)
                    .setReorderingAllowed(true);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }

            transaction.commit();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (java.lang.InstantiationException e) {
            throw new RuntimeException(e);
        }

        return fragment;
    }

    //--------------------------Catch event click category---------------------------//
    public void ClickEventCategory() {
        myAdapterCategories.setonRecyclerViewOnClickListener(new HomeCategoryRecyclerViewAdapter.onRecyclerViewOnClickListener() {
            @Override
            public void onItemRecyclerViewOnClickListener(int p, View CardView) {
                final List<ProductModel> arrayFilter = new ArrayList<>();
                categoryHasChoose = categoriesList.get(p);
                for (int i = 0; i < productsList.size(); i++) {
                    if (productsList.get(i).getCategoryIds().contains(categoryHasChoose.getId())) {
                        arrayFilter.add(productsList.get(i));
                    }
                }
                //Add into menu
                grifViewList(arrayFilter);
                //Bin color for category clicked
                fillColorForCategoryClicked(p, CardView);
                txtMenu.setText(categoriesList.get(p).getName());
            }
        });
    }

    public void fillColorForCategoryClicked(int p, View CardView) {
        if (selectedRow == -1 || p == selectedRow) {
            selectedRow = p;
            TextView cardViewBg = CardView.findViewById(R.id.txt_name_search_item_type);
            previousCartViewItemClicked = cardViewBg;
            cardViewBg.setTextColor(getResources().getColor(R.color.green, fragmentLayout.getContext().getTheme()));
        } else {
            selectedRow = p;
            previousCartViewItemClicked.setTextColor(getResources().getColor(R.color.black, fragmentLayout.getContext().getTheme()));
            TextView cardViewBg = CardView.findViewById(R.id.txt_name_search_item_type);
            cardViewBg.setTextColor(getResources().getColor(R.color.green, fragmentLayout.getContext().getTheme()));
            previousCartViewItemClicked = cardViewBg;
        }
    }
}