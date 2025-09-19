package com.example.luyenthiblxmay.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.luyenthiblxmay.R;

import java.util.concurrent.TimeUnit;

// ReminderWorker.java
public class ReminderWorker extends Worker {
    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        showNotification("Nhắc nhở ôn tập", "Bạn đã luyện tập hôm nay chưa?");

        // Sau khi xong → enqueue lại sau 30s
        WorkManager.getInstance(getApplicationContext()).enqueue(
                new OneTimeWorkRequest.Builder(ReminderWorker.class)
                        .setInitialDelay(30, TimeUnit.SECONDS)  // trong thoi gian 30s
                        .build()
        );

        return Result.success();
    }

    private void showNotification(String title, String message) {
        NotificationManager manager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "reminder_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Nhắc nhở học tập",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        manager.notify((int) System.currentTimeMillis(), builder.build()); // id khác nhau mỗi lần
    }
}
