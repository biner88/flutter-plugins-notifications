package dk.cachet.notifications;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Notification listening service. Intercepts notifications if permission is
 * given to do so.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class NotificationListener extends NotificationListenerService {

  public static String NOTIFICATION_INTENT = "notification_event";
  public static String NOTIFICATION_MESSAGE_LIST = "notification_message_list";

  private static final String TAG = "Notification";

  private int oldMessageCount = 0;

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  @Override
  public void onNotificationPosted(StatusBarNotification sbn) {
    super.onNotificationPosted(sbn);
    Bundle extras = sbn.getNotification().extras;
    Intent intent = new Intent(NOTIFICATION_INTENT);
    if (oldMessageCount == 0) {
      getMessageList();
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

  public void getMessageList() {
   int _oldMessageCount = getActiveNotifications().length;
    Intent intent = new Intent(NOTIFICATION_INTENT);
    ArrayList<HashMap<String, Object>> _messageList = new ArrayList<HashMap<String, Object>>();
    if (_oldMessageCount > 0) {
      for (StatusBarNotification statusBarNotification : getActiveNotifications()) {
        HashMap<String, Object> m = mapGroup(statusBarNotification);
        if (!m.isEmpty()) {
          _messageList.add(m);
        }
      }
      Gson gson = new Gson();
      intent.putExtra(NOTIFICATION_MESSAGE_LIST, gson.toJson(_messageList));
      sendBroadcast(intent);
      oldMessageCount = _oldMessageCount;
      Log.i(TAG, "onListenerConnected:" + oldMessageCount);
    }

    
  }
  @Override
  public void onListenerConnected() {
    super.onListenerConnected();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        getMessageList();
      }
    }, 2000);
    Log.i(TAG, "onListenerConnected");
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.i(TAG, "onCreate");
  }

  @Override
  public void onNotificationRemoved(StatusBarNotification sbn) {
    Log.i(TAG, "onNotificationRemoved");
  }

  private synchronized String getAppName(String packgename) {
    PackageManager pm = getPackageManager();
    try {
      ApplicationInfo appInfo = pm.getApplicationInfo(packgename, 0);
      //获取应用名
      String appName =appInfo.loadLabel(pm).toString();
      return appName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return "";
  }
  public HashMap<String, Object> mapGroup(StatusBarNotification statusBarNotification) {
    HashMap<String, Object> mapGroup = new HashMap<String, Object>();
    Bundle extrasGroup;
    String titleGroup;
    String textGroup;
    String packageNameGroup;
    String postTimeGroup;
    String idGroup;
    String appName;

    extrasGroup = statusBarNotification.getNotification().extras;
    if (extrasGroup != null) {
      try {
        CharSequence _titleGroup = extrasGroup.getCharSequence(Notification.EXTRA_TITLE);
        titleGroup = _titleGroup == null ? "" : _titleGroup.toString();
        CharSequence _textGroup = extrasGroup.getCharSequence(Notification.EXTRA_TEXT);
        textGroup = _textGroup == null ? "" : _textGroup.toString();
        packageNameGroup = statusBarNotification.getPackageName();
        postTimeGroup = String.valueOf(statusBarNotification.getPostTime());
        idGroup = String.valueOf(statusBarNotification.getId());
        appName = getAppName(packageNameGroup);
        mapGroup.put("title", titleGroup);
        mapGroup.put("text", textGroup);
        mapGroup.put("packageName", packageNameGroup);
        mapGroup.put("postTime", postTimeGroup);
        mapGroup.put("id", idGroup);
        mapGroup.put("appName", appName);

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return mapGroup;
  }
}
