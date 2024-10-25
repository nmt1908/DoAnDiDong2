package vn.tdc.edu.fooddelivery.fragments.admin;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.api.CategoryAPI;
import vn.tdc.edu.fooddelivery.api.ProductAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.components.MultiSelectDialog;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.models.CategoryModel;
import vn.tdc.edu.fooddelivery.models.ProductModel;
import vn.tdc.edu.fooddelivery.utils.ImageUploadUtils;

public class ProductFormFragment extends AbstractFragment implements View.OnClickListener {
    private TextView tvCategories;

    private ShapeableImageView imgProduct;
    private EditText edId;
    private EditText edName;
    private EditText edImage;
    private EditText edPrice;
    private EditText edQuantity;
    private EditText edUnit;
    private EditText edDescription;
    private Button btnAddOrUpdate;
    MultiSelectDialog<CategoryModel> multiSelectDialog;
    private ProductModel productModel;
    private List<CategoryModel> categoriesList;

    private FloatingActionButton btnUploadImage;

    public ProductModel getProductModel() {
        return productModel;
    }

    public ProductFormFragment setProductModel(ProductModel productModel) {
        this.productModel = productModel;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_form, container, false);
        tvCategories = view.findViewById(R.id.edCategories);
        imgProduct = view.findViewById(R.id.imgProduct);
        edId = view.findViewById(R.id.edId);
        edName = view.findViewById(R.id.edName);
        edImage = view.findViewById(R.id.edImage);
        edPrice = view.findViewById(R.id.edPrice);
        edQuantity = view.findViewById(R.id.edQuantity);
        edUnit = view.findViewById(R.id.edUnit);
        edDescription = view.findViewById(R.id.edDescription);
        btnAddOrUpdate = view.findViewById(R.id.btnAddUser);
        btnUploadImage = view.findViewById(R.id.btnUploadImage);

        tvCategories.setOnClickListener(this);
        btnAddOrUpdate.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);

        if (this.productModel == null) {
            this.productModel = new ProductModel();
        }

        ImageUploadUtils.getInstance().registerForUploadImageActivityResult(this, imgProduct);

        createMultiSelectCategoryDialog();
        dropProductToEditForm();

        return view;
    }

    private void createMultiSelectCategoryDialog() {
        Call<List<CategoryModel>> call = RetrofitBuilder.getClient().create(CategoryAPI.class).findAll("name");

        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    categoriesList = response.body();

                    if (getActivity() != null && multiSelectDialog == null) {
                        multiSelectDialog = new MultiSelectDialog(getActivity(), categoriesList, productModel.getCategoryIds());
                        multiSelectDialog.setTitle("Chọn danh mục");
                        multiSelectDialog.setOnActionClickListener(new MultiSelectDialog.Action() {
                            @Override
                            public void cancel() {
                            }
                            @Override
                            public void ok(List<Integer> listObjectSelected) {
                                productModel.setCategoryIds(listObjectSelected);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {

            }
        });
    }

    private boolean validateData() {
        if (edName.getText().toString().isEmpty()) {
            edName.setError("Tên không được để trống");
            return false;
        }

        if (productModel.getCategoryIds() == null || productModel.getCategoryIds().isEmpty()) {
            tvCategories.setError("Bạn chưa chọn danh mục");
            return false;
        } else {
            tvCategories.setError(null);
        }

        if (edPrice.getText().toString().isEmpty()) {
            edPrice.setError("Giá sản phẩm không được để trống");
            return false;
        }

        if (edQuantity.getText().toString().isEmpty()) {
            edQuantity.setError("Số lượng sản phẩm không được để trống");
            return false;
        }

        if (edUnit.getText().toString().isEmpty()) {
            edUnit.setError("Đơn vị sản phẩm không được để trống");
            return false;
        }

        if (edDescription.getText().toString().isEmpty()) {
            edDescription.setError("Chi tiết sản phẩm không được để trống");
            return false;
        }

        return true;
    }

    private void getProductFromUserInput() {
        if (productModel == null) {
            productModel = new ProductModel();
        }

        if (edId.getText() != null && !edId.getText().toString().isEmpty()) {
            productModel.setId(Integer.valueOf(String.valueOf(edId.getText())));
        }

        if (edImage.getText() != null && !edImage.getText().toString().isEmpty()) {
            productModel.setImageName(String.valueOf(edImage.getText()));
        }

        productModel.setName(edName.getText().toString());
        productModel.setPrice(Long.valueOf(edPrice.getText().toString()));
        productModel.setQuantity(Integer.valueOf(edQuantity.getText().toString()));
        productModel.setUnit(edUnit.getText().toString());
        productModel.setDescription(edDescription.getText().toString());
    }

    private void dropProductToEditForm() {
        if (productModel != null && productModel.getId() != null) {
            btnAddOrUpdate.setText(R.string.btn_update_product);
            edId.setText(productModel.getId() == null ? "" : productModel.getId().toString());
            edImage.setText(productModel.getImageName() == null ? "" : productModel.getImageName());
            edName.setText(productModel.getName());
            edPrice.setText(String.valueOf(productModel.getPrice()));
            edQuantity.setText(String.valueOf(productModel.getQuantity()));
            edUnit.setText(productModel.getUnit());
            edDescription.setText(productModel.getDescription());
            Glide.with(getActivity()).load(productModel.getImageUrl())
                    .into(imgProduct);
        }
    }

    private void saveProduct() {
        ImageUploadUtils.getInstance().handleUploadFileToServer(new ImageUploadUtils.Action() {
            @Override
            public void onSucess(String fileName) {
                productModel.setImageName(fileName);

                if (fileName.isEmpty()) {
                    productModel.setImageName(ImageUploadUtils.IMAGE_UPLOAD_DEFAULT);
                }

                Call<ProductModel> call = RetrofitBuilder.getClient().create(ProductAPI.class).save(productModel);

                call.enqueue(new Callback<ProductModel>() {
                    @Override
                    public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                            ((AbstractActivity) getActivity()).showMessageDialog("Thêm sản phẩm thành công");
                            getActivity().onBackPressed();
                        } else {
                            ((AbstractActivity) getActivity()).showMessageDialog("Thêm sản phẩm thất bại");
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductModel> call, Throwable t) {
                        ((AbstractActivity) getActivity()).showMessageDialog("Thêm sản phẩm thất bại");
                    }
                });
            }

            @Override
            public void onFailed() {
            }
        });
    }

    private void updateProduct() {
        ImageUploadUtils.getInstance().handleUploadFileToServer(new ImageUploadUtils.Action() {
            @Override
            public void onSucess(String fileName) {
                if (!fileName.isEmpty()) {
                    productModel.setImageName(fileName);
                }

                Call<ProductModel> call = RetrofitBuilder.getClient().create(ProductAPI.class).update(productModel);

                call.enqueue(new Callback<ProductModel>() {
                    @Override
                    public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                            ((AbstractActivity) getActivity()).showMessageDialog("Cập nhật sản phẩm thành công");
                            getActivity().onBackPressed();
                        } else {
                            ((AbstractActivity) getActivity()).showMessageDialog("Cập nhật sản phẩm thất bại");
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductModel> call, Throwable t) {
                        ((AbstractActivity) getActivity()).showMessageDialog("Cập nhật sản phẩm thất bại");
                    }
                });
            }

            @Override
            public void onFailed() {
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edCategories) {
            if (multiSelectDialog != null) {
                multiSelectDialog.show();
            }
        } else if (view.getId() == R.id.btnAddUser) {
            if (!validateData()) {
                return;
            }

            getProductFromUserInput();

            if (productModel != null && productModel.getId() != null) {
                updateProduct();
            } else {
                saveProduct();
            }
        } else if (view.getId() == R.id.btnUploadImage) {
            ImageUploadUtils.getInstance().showChoosingImageOptionsDialog((AbstractActivity) getActivity(), imgProduct);
        }
    }
}