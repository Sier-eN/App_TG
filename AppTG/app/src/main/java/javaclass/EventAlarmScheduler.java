package javaclass;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import item.EventItem;

public class EventAlarmScheduler {

    public static void scheduleEventAlarm(Context context, EventItem event) {
        String dateIso = event.getDateIso(); // yyyy-MM-dd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateIso);
            if (date == null) return;

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 6);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            // nếu đã qua 6h hôm nay thì không đặt
            if (cal.getTimeInMillis() < System.currentTimeMillis()) return;

            Intent intent = new Intent(context, EventAlarmReceiver.class);
            intent.putExtra("event_title", event.getTitle());
            intent.putExtra("event_id", event.getId());

            PendingIntent pi = PendingIntent.getBroadcast(context, event.getId(),
                    intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void cancelEventAlarm(Context context, EventItem event) {
        Intent intent = new Intent(context, EventAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, event.getId(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }
}
