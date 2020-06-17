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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.e.go4lunch.R;
import com.e.go4lunch.injection.Injection;
import com.e.go4lunch.injection.ViewModelFactory;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.RestaurantRepository;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.restaurant.DetailsRestaurantActivity;
import com.e.go4lunch.restaurant.RestaurantViewModel;
import com.e.go4lunch.ui.MainActivity;
import com.e.go4lunch.workmates.WorkmateViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Objects;

public class MyWorker extends Worker {

    private RestaurantRepository mRestaurantRepository;
    private WorkmatesRepository mWorkmatesRepository;
    private RestaurantViewModel mRestaurantViewModel;
    private WorkmateViewModel mWorkmateViewModel;
    private Workmates currentWorkmate;
    private Restaurant mRestaurant;
    private String placeId;
    private Context context;


    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mRestaurantRepository = new RestaurantRepository();
        this.mWorkmatesRepository = new WorkmatesRepository();

    }



    private void getCurrentWorkmate() {
        String workmateUid = FirebaseAuth.getInstance().getUid();
        mWorkmateViewModel.getWorkmate(workmateUid).observe((LifecycleOwner) getApplicationContext(), workmates -> {
            currentWorkmate = workmates;
            if (currentWorkmate.getRestaurantChoosen() != null) {
                placeId = currentWorkmate.getRestaurantChoosen().getPlaceId();
                getRestaurant();

            }

        });

    }

    private void getRestaurant() {
        mRestaurantViewModel.getRestaurant(placeId).observe((LifecycleOwner) getApplicationContext(), restaurant -> {
            mRestaurant = restaurant;


        });
    }

    @NonNull
    @Override
    public Result doWork() {
        displayNotification("this a test", "test is finish");
        //getCurrentWorkmate();
        return Result.success();
    }

    private void displayNotification(String task, String desc) {
       
        getCurrentWorkmate();
        if (currentWorkmate.getRestaurantChoosen() != null) {

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
}
