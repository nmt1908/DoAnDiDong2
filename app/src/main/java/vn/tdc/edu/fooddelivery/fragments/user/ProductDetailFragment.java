package vn.tdc.edu.fooddelivery.fragments.user;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.user.MainActivity;
import vn.tdc.edu.fooddelivery.adapters.ProductDetailRecyclerViewAdapter;
import vn.tdc.edu.fooddelivery.components.CreateStart;
import vn.tdc.edu.fooddelivery.components.ToastCustome;
import vn.tdc.edu.fooddelivery.models.ItemCartModel;
import vn.tdc.edu.fooddelivery.models.ProductModel;
import vn.tdc.edu.fooddelivery.models.UserModel;
import vn.tdc.edu.fooddelivery.utils.Authentication;
import vn.tdc.edu.fooddelivery.utils.FormatCurentcy;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;

public class ProductDetailFragment extends AbstractFragment {
    private ToastCustome _customeToasl;
    private RecyclerView recyclerView;
    private ProductDetailRecyclerViewAdapter myRecycleViewAdapter;
    private List<ProductModel> arrayList = new ArrayList<>();
    private ProductModel DetailProduct;
    private ImageButton buttonHeart;
    private ImageButton buttonCart;
    private View fragmentLayout = null;
    private RatingBar ratingBar;
    private ImageButton ratingImgBtn;
    // ------------------Start Main product area------------------//
    private TextView txt_NameProductMain;
    private TextView txt_PriceProductMain;
    private TextView txt_DescriptionProductMain;
    private ImageView img_MainProduct;
    private TextView txt_start;
    private LinearLayout startLayoutWrapper;
    // ------------------End Main product area------------------//
    private CartFragment cartFragment = new CartFragment();
    // -----------------Change number notify in bottomBar----//
    private MainActivity mainActivity;

    UserModel userModel = Authentication.getUserLogin();
    int userId = userModel.getId();

    public List<ProductModel> getArrayList() {
        return arrayList;
    }

    public ProductDetailFragment setArrayList(List<ProductModel> arrayList) {
        this.arrayList = arrayList;
        return this;
    }

    public ProductModel getDetailProduct() {
        return DetailProduct;
    }

    public ProductDetailFragment setDetailProduct(ProductModel detailProduct) {
        DetailProduct = detailProduct;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.fragment_product, container, false);
        // --------------------------Start-------------------------//
        Log.d("TAG", "onCreateView: chay ");
        anhXa();
        RatingEvent();
        createDataForMainProduct();
        ClickEvent();
        buttonCart();
        // -----------------------------End---------------------------//
        return fragmentLayout;
    }

    public void buttonCart() {
        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartFragment cartFragment1 = new CartFragment();
                ItemCartModel carstModel = new ItemCartModel();
                carstModel.setProduct_id(DetailProduct.getId());
                carstModel.setUser_id(userId);
                carstModel.setQuantity(1);
                cartFragment1.updateCart(carstModel,null);
                showMessageDialog("Đặt hàng thành công");
            }
        });
    }


    public void buttonBuyEventClick(ProductModel cart) {

    }

    public void buttonHeart() {
        buttonHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void RatingEvent() {
        ratingImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRating();
            }
        });
    }

    public void dialogRating() {
        Dialog dialog = new Dialog(fragmentLayout.getContext());
        dialog.setContentView(R.layout.rating_layout);
        dialog.show();

        // Anh xa
        Button button = (Button) dialog.findViewById(R.id.btn_Ratting_layout_item);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar_item_layout);
                Toast.makeText(fragmentLayout.getContext(), "" + ratingBar.getRating(), Toast.LENGTH_SHORT).show();
                dialog.hide();
            }
        });
    }

    public void anhXa() {
        // --------------Main object-------------------------
        img_MainProduct = fragmentLayout.findViewById(R.id.img_detail_screen);
        txt_NameProductMain = fragmentLayout.findViewById(R.id.txt_name_detail_screen);
        txt_PriceProductMain = fragmentLayout.findViewById(R.id.txt_price_detail_screen);
        txt_DescriptionProductMain = fragmentLayout.findViewById(R.id.txt_description_detail_screen);
        txt_start = fragmentLayout.findViewById(R.id.txt_start_detail_screen);
        startLayoutWrapper = fragmentLayout.findViewById(R.id.linearLayout_start_layout_wrapper_detail_screen);
        // ---------------Rating---------------------------
        ratingImgBtn = fragmentLayout.findViewById(R.id.imgBtn_rating_detail_screen);
        // --------------End------------------------------
        buttonCart = fragmentLayout.findViewById(R.id.btn_cart_detail_screen);
        buttonHeart = fragmentLayout.findViewById(R.id.btn_heart_detail_screen);
        recyclerView = fragmentLayout.findViewById(R.id.recyclerView_detail_screen);
        // Setup
        setUp();
    }

    public void setUp() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(fragmentLayout.getContext());
        layoutManager.setOrientation(layoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        myRecycleViewAdapter = new ProductDetailRecyclerViewAdapter((Activity) fragmentLayout.getContext(),
                R.layout.detail_layout_item, arrayList);
        recyclerView.setAdapter(myRecycleViewAdapter);
    }

    public void createDataForMainProduct() {
        Log.d("TAG", "createDataForMainProduct: processing produce detail!");
        Glide.with(fragmentLayout).load(DetailProduct.getImageUrl())
                .into(img_MainProduct);
        if (DetailProduct.getDescription().trim().isEmpty() == true) {
            txt_DescriptionProductMain.setText("" +
                    "Xin lỗi, sản phẩm này chưa được cập nhât chi tiết chúng" +
                    " tôi sẽ cập nhật trong thời gian sớm nhất có thể!" +
                    "");
        } else {
            txt_DescriptionProductMain.setText(DetailProduct.getDescription());
        }
        txt_NameProductMain.setText(DetailProduct.getName());
        txt_PriceProductMain.setText((FormatCurentcy.format(String.valueOf(DetailProduct.getPrice()))) + " đồng");
        txt_start.setText(String.valueOf(DetailProduct.getRating()));
        CreateStart.renderStart(startLayoutWrapper, DetailProduct, (Activity) fragmentLayout.getContext());
        // ----------------------End Start printf------------------//
    }

    // ----------------------------Catch event click-----------------//
    public void ClickEvent() {
        myRecycleViewAdapter
                .set_OnRecyclerViewOnClickListener(new ProductDetailRecyclerViewAdapter.onRecyclerViewOnClickListener() {
                    @Override
                    public void onItemRecyclerViewOnClickListener(int id) {
                        Toast.makeText(mainActivity, "" + id, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void showMessageDialog(String message) {
        androidx.appcompat.app.AlertDialog alert = new androidx.appcompat.app.AlertDialog.Builder(fragmentLayout.getContext())
                .setTitle("Message")
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }
}
