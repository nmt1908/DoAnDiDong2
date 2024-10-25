package vn.tdc.edu.fooddelivery.fragments.admin;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.activities.AbstractActivity;
import vn.tdc.edu.fooddelivery.api.CategoryAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.fragments.AbstractFragment;
import vn.tdc.edu.fooddelivery.models.CategoryModel;
import vn.tdc.edu.fooddelivery.utils.ImageUploadUtils;

public class CategoryFormFragment extends AbstractFragment implements View.OnClickListener {
    private String imageUpload;
    private FloatingActionButton btnUploadImage;
    private EditText edId;
    private EditText edName;
    private EditText edImage;
    private ShapeableImageView imgCategory;
    private Button btnAddOrUpdate;

    private CategoryModel categoryModel;

    public CategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_category_form, container, false);
        imgCategory = view.findViewById(R.id.imgCategory);
        edId = view.findViewById(R.id.edId);
        btnUploadImage = view.findViewById(R.id.btnUploadImage);
        edImage = view.findViewById(R.id.edImage);
        edName = view.findViewById(R.id.edName);
        btnAddOrUpdate = view.findViewById(R.id.btnAddUser);

        imgCategory.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);
        btnAddOrUpdate.setOnClickListener(this);

        ImageUploadUtils.getInstance().registerForUploadImageActivityResult(this, imgCategory);
        dropCategoryModelToEditForm();
        return view;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnUploadImage) {
            ImageUploadUtils.getInstance().showChoosingImageOptionsDialog((AbstractActivity) getActivity(), imgCategory);
        } else if (view.getId() == R.id.btnAddUser) {
            if (!validateData()) {
                return;
            }

            if (categoryModel != null && categoryModel.getId() != null) {
                updateCategory();
            } else {
                saveCategory();
            }
        }
    }

    private boolean validateData() {
        if (edName.getText().toString().isEmpty()) {
            edName.setError("Tên danh mục không được để trống");
            return false;
        }

        return true;
    }

    private void getCategoryFromUserInputs() {
        categoryModel = new CategoryModel();
        if (edId.getText() != null && !edId.getText().toString().isEmpty()) {
            categoryModel.setId(Integer.valueOf(edId.getText().toString()));
        }
        if (edImage.getText() != null && !edImage.getText().toString().isEmpty()) {
            categoryModel.setImageName(edImage.getText().toString());
        }
        categoryModel.setName(edName.getText().toString());
        categoryModel.setNumberOfProduct(0);
    }

    private void dropCategoryModelToEditForm() {
        if (categoryModel != null && categoryModel.getId() != null) {
            btnAddOrUpdate.setText(R.string.btn_update_category);
            edId.setText(categoryModel.getId().toString());
            edImage.setText(categoryModel.getImageName() == null ? "" : categoryModel.getImageName());
            edName.setText(categoryModel.getName());
            Glide.with(getActivity()).load(categoryModel.getImageUrl())
                    .into(imgCategory);
        }
    }

    private void saveCategory() {
        ImageUploadUtils.getInstance().handleUploadFileToServer(new ImageUploadUtils.Action() {
            @Override
            public void onSucess(String fileName) {
                getCategoryFromUserInputs();
                categoryModel.setImageName(fileName);

                if (fileName.isEmpty()) {
                    categoryModel.setImageName(ImageUploadUtils.IMAGE_UPLOAD_DEFAULT);
                }

                Call<CategoryModel> call = RetrofitBuilder.getClient().create(CategoryAPI.class).save(categoryModel);

                call.enqueue(new Callback<CategoryModel>() {
                    @Override
                    public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                            ((AbstractActivity) getActivity()).showMessageDialog("Thêm danh mục thành công");
                            getActivity().onBackPressed();
                        } else {
                            ((AbstractActivity) getActivity()).showMessageDialog("Thêm danh mục thất bại");
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryModel> call, Throwable t) {
                        ((AbstractActivity) getActivity()).showMessageDialog("Thêm danh mục thất bại");
                    }
                });
            }

            @Override
            public void onFailed() {
            }
        });
    }

    private void updateCategory() {
        ImageUploadUtils.getInstance().handleUploadFileToServer(new ImageUploadUtils.Action() {
            @Override
            public void onSucess(String fileName) {
                getCategoryFromUserInputs();

                if (!fileName.isEmpty()) {
                    categoryModel.setImageName(fileName);
                }

                Call<CategoryModel> call = RetrofitBuilder.getClient().create(CategoryAPI.class).update(categoryModel);

                call.enqueue(new Callback<CategoryModel>() {
                    @Override
                    public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                            ((AbstractActivity) getActivity()).showMessageDialog("Cập nhật danh mục thành công");
                            getActivity().onBackPressed();
                        } else {
                            ((AbstractActivity) getActivity()).showMessageDialog("Cập nhật danh mục thất bại");
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryModel> call, Throwable t) {
                        ((AbstractActivity) getActivity()).showMessageDialog("Cập nhật danh mục thất bại");
                    }
                });
            }

            @Override
            public void onFailed() {
            }
        });
    }
}