package vn.tdc.edu.fooddelivery.components;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import vn.tdc.edu.fooddelivery.R;

public class ToastCustome {
    //Success
    public void customeToasl(View activityToasl, LayoutInflater layoutInflater) {
        Toast toast = new Toast(activityToasl.getContext());
        View view = layoutInflater.inflate(R.layout.customize_toasl, activityToasl.findViewById(R.id.id_customize_toasl));
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM, 0, 250);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
