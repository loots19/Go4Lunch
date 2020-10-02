package com.e.go4lunch.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.e.go4lunch.R;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.restaurant.DetailsRestaurantActivity;
import com.google.gson.Gson;

import java.util.Objects;

public class MyNotificationWorker extends Worker {

    private RestaurantRepository mRestaurantRepository;
    private WorkmatesRepository mWorkmatesRepository;
    private Workmates currentWorkmate;
    private Restaurant mRestaurant;
    private String nameRestaurant;


    public MyNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mRestaurantRepository = new RestaurantRepository();
        this.mWorkmatesRepository = new WorkmatesRepository();

    }


    private void getCurrentWorkmate() {
        this.mWorkmatesRepository.getWorkmate().addOnSuccessListener(documentSnapshot -> {
            currentWorkmate = documentSnapshot.toObject(Workmates.class);
            if (Objects.requireNonNull(currentWorkmate).getRestaurantChosen() != null) {
                getRestaurant(currentWorkmate.getRestaurantChosen().getPlaceId());
                nameRestaurant = currentWorkmate.getRestaurantChosen().getName();

            }
        });
    }

    private void getRestaurant(String placeId) {
        this.mRestaurantRepository.getRestaurant1(placeId).addOnSuccessListener(documentSnapshot -> {
            mRestaurant = documentSnapshot.toObject(Restaurant.class);
            displayNotification();


        });
    }

    @NonNull
    @Override
    public Result doWork() {
        getCurrentWorkmate();
        return Result.success();
    }


    private void displayNotification() {
        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(getApplicationContext(), DetailsRestaurantActivity.class);
        Gson gson = new Gson();
        String jsonSelectedRestaurant = gson.toJson(mRestaurant);
        intent.putExtra(DetailsRestaurantActivity.EXTRA_RESTAURANT, jsonSelectedRestaurant);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Add the Notification to the Notification Manager and show it.
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a channel (Android 8)
        String channelId = "task_channel";
        String channelName = "GO4Lunch";

        // Support Version >= Android 8
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            Objects.requireNonNull(manager).createNotificationChannel(channel);

        }
        // Build a Notification object
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(getApplicationContext().getResources().getString(R.string.notification_title))
                .setContentText(getApplicationContext().getResources().getString(R.string.notification_message) + " " + nameRestaurant)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Show notification
        Objects.requireNonNull(manager).notify(1, builder.build());


    }
}
