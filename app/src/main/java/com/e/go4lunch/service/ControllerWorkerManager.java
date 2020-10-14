package com.e.go4lunch.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public abstract class ControllerWorkerManager {
    private static final String WORK_TAG = "WORK_REQUEST_TAG_Go4Lunch";
    private static final String WORK_TAG_DELETE = "WORK_REQUEST_TAG_DELETE_Go4Lunch";

    // --------------------------------------------------
    // ----- display notification everyday at 12.00 -----
    // --------------------------------------------------
    public static void scheduleWork(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        long nowMillis = calendar.getTimeInMillis();

        if (calendar.get(Calendar.HOUR_OF_DAY) > hour ||
                (calendar.get(Calendar.HOUR_OF_DAY) == hour && calendar.get(Calendar.MINUTE) + 1 >= minute)) {
            calendar.add(Calendar.DAY_OF_MONTH, 0);
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long diff = calendar.getTimeInMillis() - nowMillis;

        WorkManager mWorkManager = WorkManager.getInstance();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        mWorkManager.cancelAllWorkByTag(WORK_TAG);
        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(MyNotificationWorker.class)
                .setConstraints(constraints)
                .setInitialDelay(diff, TimeUnit.MILLISECONDS)
                .addTag(WORK_TAG)
                .build();
        mWorkManager.enqueue(mRequest);

    }

    // --------------------------------------------------------
    // ----- delete selected restaurant everyday at 00.00 -----
    // --------------------------------------------------------
    public static void deleteWork(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        long nowMillis = calendar.getTimeInMillis();

        if (calendar.get(Calendar.HOUR_OF_DAY) > hour ||
                (calendar.get(Calendar.HOUR_OF_DAY) == hour && calendar.get(Calendar.MINUTE) + 1 >= minute)) {
            calendar.add(Calendar.DAY_OF_MONTH, 0);
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long diff = calendar.getTimeInMillis() - nowMillis;

        WorkManager workManager = WorkManager.getInstance();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        workManager.cancelAllWorkByTag(WORK_TAG_DELETE);
        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(DeleteSelectedRestaurant.class)
                .setConstraints(constraints)
                .setInitialDelay(diff, TimeUnit.MILLISECONDS)
                .addTag(WORK_TAG_DELETE)
                .build();
        workManager.enqueue(mRequest);
    }

    // -------------------------------------------
    // ----- open settings for notifications -----
    // -------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void goToNotificationSettings(String channel, Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            if (channel != null) {
                intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel);
            } else {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            }
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (channel != null) {
                intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel);
            } else {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            }
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
        }
        context.startActivity(intent);
    }


}
