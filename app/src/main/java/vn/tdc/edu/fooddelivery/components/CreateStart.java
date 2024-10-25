package vn.tdc.edu.fooddelivery.components;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import vn.tdc.edu.fooddelivery.R;
import vn.tdc.edu.fooddelivery.models.ProductModel;

public class CreateStart {
    public static void renderStart(LinearLayout linearLayout, ProductModel cart, Activity activity) {
        for (int i = 0; i < cart.getRating(); i++) {
            ImageView imageView = new ImageView(activity);
            imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_baseline_star_24, activity.getTheme()));
            linearLayout.addView(imageView);
        }
    }
}
