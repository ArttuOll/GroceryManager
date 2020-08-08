package com.bsuuv.grocerymanager.notifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.util.DateHelper;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;

import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class GroceryDayNotifier {

    public static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    public static final int NOTIFICATION_ID = 0;

    private Context mContext;
    private DateHelper mDateHelper;
    private SharedPreferencesHelper mSharedPrefsHelper;

    public GroceryDayNotifier(Context context, DateHelper dateHelper,
                              SharedPreferencesHelper sharedPreferencesHelper) {
        this.mContext = context;
        this.mDateHelper = dateHelper;
        this.mSharedPrefsHelper = sharedPreferencesHelper;

        createNotificationChannel();
    }

    public void scheduleGroceryDayNotification() {
        Intent notificationIntent = new Intent(mContext,
                GroceryDayReceiver.class);

        final PendingIntent notificationPendingIntent =
                PendingIntent.getBroadcast(mContext, NOTIFICATION_ID,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final AlarmManager alarmManager =
                (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

        long notifyAfterThisManyDays =
                AlarmManager.INTERVAL_DAY * mDateHelper.timeUntilNextGroceryDay();

        SharedPreferences.OnSharedPreferenceChangeListener groceryDaysListener =
                createOnSharedPreferenceChangeListener(alarmManager,
                        notificationPendingIntent, notifyAfterThisManyDays);

        SharedPreferences sharedPreferences =
                mSharedPrefsHelper.getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(groceryDaysListener);
    }

    private void createNotificationChannel() {
        NotificationManager mNotifManager =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel primaryChannel =
                    new NotificationChannel(PRIMARY_CHANNEL_ID,
                            mContext.getString(R.string.notifchan_primary_name),
                            NotificationManager.IMPORTANCE_DEFAULT);

            primaryChannel.enableLights(true);
            primaryChannel.setLightColor(Color.GREEN);
            primaryChannel.enableVibration(true);
            primaryChannel.setDescription(mContext.getString(R.string.notifchan_primary_description));
            Objects.requireNonNull(mNotifManager).createNotificationChannel(primaryChannel);
        }
    }

    private SharedPreferences.OnSharedPreferenceChangeListener
    createOnSharedPreferenceChangeListener(AlarmManager alarmManager,
                                           PendingIntent notificationPendingIntent,
                                           long daysToNotif) {

        return (sharedPreferences, preferenceKey) -> {

            if (preferenceKey.equals(SharedPreferencesHelper.GROCERY_DAYS_KEY)) {

                long triggerTime =
                        SystemClock.elapsedRealtime() + daysToNotif;

                if (alarmManager != null) {
                    alarmManager.setInexactRepeating(
                            AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            triggerTime, daysToNotif,
                            notificationPendingIntent);
                }
            }
        };

    }
}
