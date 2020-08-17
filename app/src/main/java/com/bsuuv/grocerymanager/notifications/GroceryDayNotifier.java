package com.bsuuv.grocerymanager.notifications;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import com.bsuuv.grocerymanager.util.DateHelper;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.Objects;

/**
 * Logic class responsible for scheduling a notification for the grocery day(s).
 */
public class GroceryDayNotifier {

  private static final int NOTIFICATION_ID = 0;

  private SharedPreferencesHelper mSharedPrefsHelper;
  private AlarmManager mAlarmManager;
  private Context mContext;
  private long mDaysToNotif;

  public GroceryDayNotifier(Context context, SharedPreferencesHelper sharedPreferencesHelper) {
    this.mContext = context;
    this.mSharedPrefsHelper = sharedPreferencesHelper;
    this.mAlarmManager = createAlarmManager();
    NotificationChannelCreator mChannelCreator = new NotificationChannelCreator(mContext);
    mChannelCreator.createNotificationChannel();
  }

  private AlarmManager createAlarmManager() {
    Object alarmService = mContext.getSystemService(ALARM_SERVICE);
    return Objects.requireNonNull((AlarmManager) alarmService);
  }

  /**
   * Schedules a notification to be shown on the primary notification channel (see {@link
   * NotificationChannelCreator}), if the grocery days have changed since the last time this method
   * was called. <code>AlarmManager</code> and {@link GroceryDayReceiver} are used to trigger and
   * the notification on time.
   *
   * @param daysUntilGroceryDay Integer representing days until grocery day. As long as this value
   *                            is retrieved from {@link DateHelper}, it can never be less than 0.
   * @see NotificationChannelCreator
   * @see DateHelper
   * @see GroceryDayReceiver
   */
  public void scheduleGroceryDayNotification(int daysUntilGroceryDay) {
    this.mDaysToNotif = calculateDaysToNotif(daysUntilGroceryDay);

    SharedPreferences.OnSharedPreferenceChangeListener groceryDaysChangedListener =
        createOnSharedPrefsChangeListener();

    SharedPreferences sharedPreferences = mSharedPrefsHelper.getSharedPreferences();
    sharedPreferences.registerOnSharedPreferenceChangeListener(groceryDaysChangedListener);
  }

  private long calculateDaysToNotif(int daysUntilGroceryDay) {
    return AlarmManager.INTERVAL_DAY * daysUntilGroceryDay;
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
