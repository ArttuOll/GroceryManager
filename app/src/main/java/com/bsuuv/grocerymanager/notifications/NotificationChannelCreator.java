package com.bsuuv.grocerymanager.notifications;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import com.bsuuv.grocerymanager.R;
import java.util.Objects;

/**
 * A logic class responsible for creating the notification channel for the grocery day
 * notification.
 */
class NotificationChannelCreator {

  private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

  private Context mContext;

  NotificationChannelCreator(Context context) {
    this.mContext = context;
  }

  /**
   * Creates the primary notification channel of this app. This method can be
   * safely called multiple times, since trying to create a notification channel
   * that already exists causes no action (see <a href="https://developer.android.com/training/notify-user/channels#importance">Android documentation on notifications</a>).
   * <p>
   * Since notification channels are required only on SDKs higher than 26, on
   * lower SDKs this method does nothing.
   */
  public void createNotificationChannel() {
    NotificationManager mNotifManager = createNotificationManager();

    if (sdkOreoOrHigher()) {
      NotificationChannel primaryChannel = Objects.requireNonNull(buildPrimaryChannel());
      mNotifManager.createNotificationChannel(primaryChannel);
    }
  }

  private NotificationManager createNotificationManager() {
    Object notificationService =
        Objects.requireNonNull(mContext.getSystemService(NOTIFICATION_SERVICE));
    return (NotificationManager) notificationService;
  }

  private NotificationChannel buildPrimaryChannel() {
    if (sdkOreoOrHigher()) {
      String channelName = mContext.getString(R.string.notifchan_primary_name);
      NotificationChannel primaryChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
          channelName, NotificationManager.IMPORTANCE_DEFAULT);
      setChannelAttributes(primaryChannel);
      return primaryChannel;
    }
    return null;
  }

  private void setChannelAttributes(NotificationChannel primaryChannel) {
    if (sdkOreoOrHigher()) {
      primaryChannel.enableLights(true);
      primaryChannel.setLightColor(Color.GREEN);
      primaryChannel.enableVibration(true);
      primaryChannel.setDescription(mContext.getString(R.string.notifchan_primary_description));
    }
  }

  private boolean sdkOreoOrHigher() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
  }
}
