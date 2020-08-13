package com.bsuuv.grocerymanager.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.ui.MainActivity;

public class GroceryDayReceiver extends BroadcastReceiver {

    public static final String PRIMARY_CHANNEL_ID =
        "primary_notification_channel";
    public static final int NOTIFICATION_ID = 0;

    private NotificationManager mNotifManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mNotifManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        sendNotification(context);
    }

    public void sendNotification(Context context) {
        NotificationCompat.Builder notifBuilder =
            getNotificationBuilder(context);
        mNotifManager.notify(NOTIFICATION_ID, notifBuilder.build());
    }

    private NotificationCompat.Builder getNotificationBuilder(Context context) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent notifPendingIntent = PendingIntent.getActivity(context,
            NOTIFICATION_ID, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(context,
            PRIMARY_CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_content_title))
            .setContentText(context.getString(R.string.notification_content_text))
            .setSmallIcon(R.drawable.ic_notification_primary)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(notifPendingIntent)
            .setAutoCancel(true);
    }
}
