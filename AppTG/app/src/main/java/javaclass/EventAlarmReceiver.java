package javaclass;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.apptg.MainActivity;
import com.example.apptg.R;

public class EventAlarmReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "event_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("event_title");
        int id = intent.getIntExtra("event_id", 0);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Sự kiện",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Thông báo sự kiện hàng ngày");
            nm.createNotificationChannel(channel);
        }

        Intent openApp = new Intent(context, MainActivity.class);
        openApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pi = PendingIntent.getActivity(
                context,
                id,
                openApp,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.event) // thay icon phù hợp
                .setContentTitle("Sự kiện")
                .setContentText("Đến ngày " + title + " rồi!")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        nm.notify(id, builder.build());
    }
}
