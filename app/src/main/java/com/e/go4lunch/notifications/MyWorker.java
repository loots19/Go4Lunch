package com.e.go4lunch.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.e.go4lunch.R;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.restaurant.DetailsRestaurantActivity;
import com.e.go4lunch.ui.MainActivity;

import java.util.Objects;

public class MyWorker extends Worker {

    private RestaurantRepository mRestaurantRepository;


    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mRestaurantRepository = new RestaurantRepository();
    }

    @NonNull
    @Override
    public Result doWork() {
        displayNotification("this a test", "test is finish");
        return Result.success();
    }

    private void displayNotification(String task, String desc) {

        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(getApplicationContext(), DetailsRestaurantActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Add the Notification to the Notification Manager and show it.
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a channel (Android 8)
        String channelId = "task_channel";
        String channelName = "task8name";

        // Support Version >= Android 8
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            Objects.requireNonNull(manager).createNotificationChannel(channel);

        }
        // Build a Notification object
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(getApplicationContext().getResources().getString(R.string.notification_title))
                .setContentText(getApplicationContext().getResources().getString(R.string.notification_message))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Show notification
        Objects.requireNonNull(manager).notify(1, builder.build());


    }
}

