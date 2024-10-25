package vn.tdc.edu.fooddelivery.components;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import vn.tdc.edu.fooddelivery.R;

public class Notification {

    // Notifications var
    private static final String CHANNEL_ID = "my_channel";
    private static final int NOTIFICATION_ID = 1;
    
    @SuppressLint("MissingPermission")
    public void showNotificationBasic(Context context, String title, String content) {
//         Tạo kênh thông báo (chỉ cần thực hiện trên Android 8.0 trở lên)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        // Tạo biểu tượng lớn từ tệp ảnh
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

        // Tạo thông báo
        @SuppressLint("ResourceAsColor") NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setColor(R.color.green_light)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @SuppressLint("MissingPermission")
    public static void showNotificationUpgrade(Context context, String title, String notificationContent, String longNotificationContent) {
        @SuppressLint("ResourceAsColor") NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setColor(ContextCompat.getColor(context, R.color.green_light))
                .setColorized(true)
                .setContentText(notificationContent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(longNotificationContent));
        // Xây dựng thông báo
        android.app.Notification notification = builder.build();
        // Hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}

