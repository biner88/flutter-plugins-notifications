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
  // public static String NOTIFICATION_PACKAGE_NAME = "notification_package_name";
  // public static String NOTIFICATION_TEXT = "notification_text";
  // public static String NOTIFICATION_TITLE = "notification_title";
  // public static String NOTIFICATION_POST_TIME = "notification_post_time";
  public static String NOTIFICATION_MESSAGE_LIST = "notification_message_list";

  private static final String TAG = "bh";

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
   

    Log.i("onListenerConnected", String.valueOf(oldMessageCount));

    if (extras != null) {
      try {
        String getPostTime = String.valueOf(sbn.getPostTime());
        String messageList = "[]";
        String packageName = sbn.getPackageName();
        String title = extras.getString(Notification.EXTRA_TITLE, "");
        String text = extras.getString(Notification.EXTRA_TEXT, "");
        ArrayList<Map<String, Object>> _messageList = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> mapGroup = new HashMap<String, Object>();
        mapGroup.put("title", title);
        mapGroup.put("text", text);
        mapGroup.put("packageName", packageName);
        mapGroup.put("postTime", getPostTime);
        _messageList.add(mapGroup);

        Gson gson = new Gson();
        messageList = "["+gson.toJson(mapGroup)+"]";
        intent.putExtra(NOTIFICATION_MESSAGE_LIST, messageList);
        sendBroadcast(intent);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

  @Override
  public void onListenerConnected() {
    // super.onListenerConnected();
    oldMessageCount=getActiveNotifications().length;
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent intent = new Intent(NOTIFICATION_INTENT);
        String messageList = "[]";
        
        ArrayList<Map<String, Object>> _messageLst = new ArrayList<Map<String,
        Object>>();
        if (oldMessageCount > 0) {
          Bundle extrasGroup;
          String titleGroup;
          String textGroup;
          String packageNameGroup;
          String postTimeGroup;

          for (StatusBarNotification statusBarNotification : getActiveNotifications())
          {
            extrasGroup = statusBarNotification.getNotification().extras;
            if (extrasGroup != null) {
              titleGroup = extrasGroup.getString(Notification.EXTRA_TITLE, "");
              textGroup = extrasGroup.getString(Notification.EXTRA_TEXT, "");
              packageNameGroup = statusBarNotification.getPackageName();
              postTimeGroup = String.valueOf(statusBarNotification.getPostTime());

              HashMap<String, Object> mapGroup = new HashMap<String, Object>();

              mapGroup.put("title", titleGroup);
              mapGroup.put("text", textGroup);
              mapGroup.put("packageName", packageNameGroup);
              mapGroup.put("postTime", postTimeGroup);

              _messageLst.add(mapGroup);

            }
          }
          Gson gson = new Gson();
          messageList = gson.toJson(_messageLst);
        }
        intent.putExtra(NOTIFICATION_MESSAGE_LIST, messageList);
        sendBroadcast(intent);
        // Log.v(TAG, "messageList:" + messageList);

      }
    },1000);
  }

  @Override
  public void onCreate() {
    Log.i("onCreate", "Notification onCreate ");
    // if (oldMessageCount==0) {
    // onListenerConnected();
    // }
  }

  @Override
  public void onNotificationRemoved(StatusBarNotification sbn) {
    // Bundle extras = sbn.getNotification().extras;
    // // 获取接收消息APP的包名
    // String notificationPkg = sbn.getPackageName();
    // // 获取接收消息的抬头
    // String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
    // // 获取接收消息的内容
    // String notificationText = extras.getString(Notification.EXTRA_TEXT);
    // Log.i("XSL_Test", "Notification removed " + notificationTitle + " & " +
    // notificationText);
  }
}
