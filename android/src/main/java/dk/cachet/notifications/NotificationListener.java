package dk.cachet.notifications;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;

//import android.util.Log;
/**
 * Notification listening service. Intercepts notifications if permission is
 * given to do so.
 */
@SuppressLint("OverrideAbstract")
@RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {

  public static String NOTIFICATION_INTENT = "notification_event";
  public static String NOTIFICATION_MESSAGE_LIST = "notification_message_list";

  private static final String TAG = "Notification";

  private int oldMessageCount = 0;

  // @RequiresApi(api = VERSION_CODES.KITKAT)
  @TargetApi(VERSION_CODES.KITKAT)
  @Override
  public void onNotificationPosted(StatusBarNotification sbn) {
    super.onNotificationPosted(sbn);
    Bundle extras = sbn.getNotification().extras;
    Intent intent = new Intent(NOTIFICATION_INTENT);
    if (oldMessageCount == 0) {
      onListenerConnected();
    }

    Log.i(TAG, "onNotificationPosted ");

    ArrayList<HashMap<String, Object>> _messageList = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> m = mapGroup(sbn);
    if (!m.isEmpty()) {
      _messageList.add(m);
      Gson gson = new Gson();
      intent.putExtra(NOTIFICATION_MESSAGE_LIST, gson.toJson(_messageList));
      sendBroadcast(intent);
    }
  }

  @Override
  public void onListenerConnected() {
    // super.onListenerConnected();
    oldMessageCount = getActiveNotifications().length;
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent intent = new Intent(NOTIFICATION_INTENT);
        ArrayList<HashMap<String, Object>> _messageList = new ArrayList<HashMap<String, Object>>();
        if (oldMessageCount > 0) {
          Bundle extrasGroup;
          CharSequence titleGroup;
          CharSequence textGroup;
          String packageNameGroup;
          String postTimeGroup;

          for (StatusBarNotification statusBarNotification : getActiveNotifications()) {

            HashMap<String, Object> m = mapGroup(statusBarNotification);
            if (!m.isEmpty()) {
              _messageList.add(m);
            }
          }
          Gson gson = new Gson();
          intent.putExtra(NOTIFICATION_MESSAGE_LIST, gson.toJson(_messageList));
          sendBroadcast(intent);
        }

        Log.i(TAG, "onListenerConnected:" + oldMessageCount);

      }
    }, 1000);
    Log.i(TAG, "onListenerConnected");
  }

  @Override
  public void onCreate() {
    Log.i(TAG, "onCreate");
  }

  @Override
  public void onNotificationRemoved(StatusBarNotification sbn) {
    Log.i(TAG, "onNotificationRemoved");
  }

  public HashMap<String, Object> mapGroup(StatusBarNotification statusBarNotification) {
    HashMap<String, Object> mapGroup = new HashMap<String, Object>();
    Bundle extrasGroup;
    CharSequence titleGroup;
    CharSequence textGroup;
    String packageNameGroup;
    String postTimeGroup;
    extrasGroup = statusBarNotification.getNotification().extras;
    if (extrasGroup != null) {
      try {
        titleGroup = extrasGroup.getCharSequence(Notification.EXTRA_TITLE);
        titleGroup = titleGroup==null?"":titleGroup;
        textGroup = extrasGroup.getCharSequence(Notification.EXTRA_TEXT);
        textGroup = textGroup==null?"":textGroup;
        packageNameGroup = statusBarNotification.getPackageName();
        postTimeGroup = String.valueOf(statusBarNotification.getPostTime());

        mapGroup.put("title", titleGroup.toString());
        mapGroup.put("text", textGroup.toString());
        mapGroup.put("packageName", packageNameGroup);
        mapGroup.put("postTime", postTimeGroup);

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return mapGroup;
  }
}
