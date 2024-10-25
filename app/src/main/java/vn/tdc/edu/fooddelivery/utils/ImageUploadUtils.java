package vn.tdc.edu.fooddelivery.utils;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tdc.edu.fooddelivery.api.UploadAPI;
import vn.tdc.edu.fooddelivery.api.builder.RetrofitBuilder;
import vn.tdc.edu.fooddelivery.models.FileModel;

public class ImageUploadUtils {
    private String imageSelected;
    public static final int REQ_CAMERA = 99;
    public static final int REQ_READ_EXTERNAL_STORAGE = 100;
    public static final String GALLERY = "Thư viện";
    public static final String CAMERA = "Máy ảnh";
    public static final String CANCEL = "Huỷ";
    public static final String IMAGE_UPLOAD_DEFAULT = "image_upload_default.png";
    public static final String USER_IMAGE_UPLOAD_DEFAULT = "user_image_default.png";
    public static final CharSequence[] OPTIONS = {CAMERA, GALLERY, CANCEL};
    private ActivityResultLauncher<Intent> startActivityForResult;
    private Drawable oldImagaViewDrawable;

    private static OnResultUpload onResultUpload;

    public static void setOnResultUpload(OnResultUpload onResultUpload) {
        ImageUploadUtils.onResultUpload = onResultUpload;
    }

    public interface OnResultUpload {
        void onUploadImageResultAction();
    }

    public static ImageUploadUtils getInstance() {
        return Helper.INSTANCE;
    }

    public String getImageSelected() {
        return imageSelected;
    }

    public void setImageSelected(String imageSelected) {
        this.imageSelected = imageSelected;
    }

    public void pickImageAction(AppCompatActivity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.getIntent().putExtra("req", GALLERY);
        startActivityForResult.launch(intent);
    }

    public void takePhotoAction(AppCompatActivity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.getIntent().putExtra("req", CAMERA);
        startActivityForResult.launch(intent);
    }


    private Uri getImageUri(Context context, Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "myImage", "");
        return Uri.parse(path);
    }

    public ActivityResultLauncher<Intent> registerForUploadImageActivityResult(Fragment fragment, ImageView imageView) {
        startActivityForResult = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        oldImagaViewDrawable = imageView.getDrawable();
                        Intent data = result.getData();
                        if (result.getResultCode() == AppCompatActivity.RESULT_OK && data != null) {
                            switch (fragment.getActivity().getIntent().getStringExtra("req")) {
                                case CAMERA:
                                    Bitmap image = (Bitmap) data.getExtras().get("data");
                                    imageSelected = FileUtils.getPath(fragment.getActivity(), getImageUri(fragment.getActivity(), image));
                                    imageView.setImageBitmap(image);
                                    break;
                                case GALLERY:
                                    Uri imageUri = data.getData();
                                    imageSelected = FileUtils.getPath(fragment.getActivity(), imageUri);
                                    Picasso.get().load(imageUri).into(imageView);
                                    break;
                                default:
                                    break;
                            }
                            if (onResultUpload != null) {
                                onResultUpload.onUploadImageResultAction();
                            }
                        }
                    }
                }
        );
        return startActivityForResult;
    }

    private boolean checkPermissions(AppCompatActivity activity, String permission) {
        int check = activity.checkSelfPermission(permission);
        return check == PackageManager.PERMISSION_GRANTED;
    }

    public void showChoosingImageOptionsDialog(AppCompatActivity activity, ImageView imageView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Chọn hình ảnh");

        builder.setItems(OPTIONS, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (OPTIONS[which].equals(CAMERA)) {
                    if (checkPermissions(activity, Manifest.permission.CAMERA)) {
                        ImageUploadUtils.getInstance().takePhotoAction(activity);
                    } else {
                        activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQ_CAMERA);
                    }
                } else if (OPTIONS[which].equals(GALLERY)) {
                    if (checkPermissions(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        pickImageAction(activity);
                    } else {
                        activity.requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_READ_EXTERNAL_STORAGE);
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    public void handleUploadFileToServer(Action action) {
        if (imageSelected == null) {
            action.onSucess("");
            return;
        }

        File fileUpload = new File(Uri.parse((imageSelected)).getPath());

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileUpload);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", fileUpload.getName(), requestBody);

        UploadAPI uploadAPI = RetrofitBuilder.getClient().create(UploadAPI.class);

        Call<FileModel> call = uploadAPI.upload(filePart);

        call.enqueue(new Callback<FileModel>() {
            @Override
            public void onResponse(Call<FileModel> call, Response<FileModel> response) {
                FileModel fileModel = response.body();

                if (action != null) {
                    imageSelected = null;
                    String fileName = fileModel == null ? IMAGE_UPLOAD_DEFAULT : fileModel.getFileName();
                    action.onSucess(fileName);
                }
            }

            @Override
            public void onFailure(Call<FileModel> call, Throwable t) {
                action.onFailed();
            }
        });
    }

    private Action action;

    public interface Action {
        void onSucess(String fileName);
        void onFailed();
    }

    private static class Helper {
        private static final ImageUploadUtils INSTANCE = new ImageUploadUtils();
    }
}
