package com.bsuuv.grocerymanager.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;

import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;

public class GroceryDayNotifier {

    public static final int NOTIFICATION_ID = 0;

    private final SharedPreferencesHelper mSharedPrefsHelper;
    private final AlarmManager mAlarmManager;
    private Context mContext;
    private long mDaysToNotif;

    public GroceryDayNotifier(Context context, SharedPreferencesHelper sharedPreferencesHelper) {
        this.mContext = context;
        this.mSharedPrefsHelper = sharedPreferencesHelper;
        this.mAlarmManager = createAlarmManager();
        NotificationChannelCreator mChannelCreator = new NotificationChannelCreator();

        // Trying to create a notification channel that already exists causes no action, so
        // calling this many times does no harm.
        mChannelCreator.createNotificationChannel(mContext);
    }

    private AlarmManager createAlarmManager() {
        Object alarmService = mContext.getSystemService(ALARM_SERVICE);
        return Objects.requireNonNull((AlarmManager) alarmService);
    }

    public void scheduleGroceryDayNotification(int timeUntilGroceryDay) {
        this.mDaysToNotif = calculateDaysToNotif(timeUntilGroceryDay);

        SharedPreferences.OnSharedPreferenceChangeListener groceryDaysChangedListener =
                createOnSharedPrefsChangeListener();

        SharedPreferences sharedPreferences = mSharedPrefsHelper.getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(groceryDaysChangedListener);
    }

    private long calculateDaysToNotif(int timeUntilGroceryDay) {
        return AlarmManager.INTERVAL_DAY * timeUntilGroceryDay;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener createOnSharedPrefsChangeListener() {
        return (sharedPreferences, preferenceKey) -> {
            if (groceryDaysChanged(preferenceKey)) {
                final PendingIntent notificationPendingIntent = createPendingIntent();
                long triggerTime = calculateTriggerTime();

                setAlarm(notificationPendingIntent, triggerTime);
            }
        };
    }

    private boolean groceryDaysChanged(String preferenceKey) {
        return preferenceKey.equals(SharedPreferencesHelper.GROCERY_DAYS_KEY);
    }

    private PendingIntent createPendingIntent() {
        Intent notificationIntent = new Intent(mContext, GroceryDayReceiver.class);
        return PendingIntent.getBroadcast(mContext, NOTIFICATION_ID, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private long calculateTriggerTime() {
        return SystemClock.elapsedRealtime() + mDaysToNotif;
    }

    private void setAlarm(PendingIntent notificationPendingIntent, long triggerTime) {
        mAlarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime, mDaysToNotif,
                notificationPendingIntent);
    }
}
