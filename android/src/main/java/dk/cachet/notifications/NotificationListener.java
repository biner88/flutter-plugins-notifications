package dk.cachet.notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Notification listening service. Intercepts notifications if permission is given to do so.
 */
@SuppressLint("OverrideAbstract")
@RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {

  public static String NOTIFICATION_INTENT = "notification_event";
  public static String NOTIFICATION_PACKAGE_NAME = "notification_package_name";
  public static String NOTIFICATION_TEXT = "notification_text";
  public static String NOTIFICATION_TITLE = "notification_title";
  public static String NOTIFICATION_POST_TIME = "notification_post_time";

  private static final String TAG = "bh";

  @RequiresApi(api = VERSION_CODES.KITKAT)
  @Override
  public void onNotificationPosted(StatusBarNotification sbn) {
    // super.onNotificationPosted(sbn);
    int xc = getActiveNotifications().length;
    Log.v(TAG, "log:" + xc);

    // String packageName = sbn.getPackageName();
    Bundle extras = sbn.getNotification().extras;

    Intent intent = new Intent(NOTIFICATION_INTENT);


    if (extras != null) {
      try {
        String getPostTime = String.valueOf(sbn.getPostTime());
        boolean isAppGroup = sbn.isAppGroup();
        boolean isGroup = sbn.isGroup();
        boolean isOngoing = sbn.isOngoing();
        Log.v(TAG, "getPostTime:" + getPostTime);
        Log.v(TAG, "isAppGroup:" + isAppGroup);
        Log.v(TAG, "isGroup:" + isGroup);
        //包名
        String packageName = sbn.getPackageName();
        // 获取通知标题
        String title = extras.getString(Notification.EXTRA_TITLE, "");
        // 获取通知内容
        String text = extras.getString(Notification.EXTRA_TEXT, "");
        
        // CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
        intent.putExtra(NOTIFICATION_PACKAGE_NAME, packageName);
        intent.putExtra(NOTIFICATION_TITLE, title);
        intent.putExtra(NOTIFICATION_TEXT, text);
        intent.putExtra(NOTIFICATION_POST_TIME, getPostTime);
        Log.v(TAG, "log:" + extras.toString());

        // CharSequence te = extras.getCharSequence("android.textLines");
     

        // ArrayList<StatusBarNotification> groupedNotifications = new ArrayList<>();
        // Bundle extras2222;
        // CharSequence title222;
        // CharSequence msg222;
        // CharSequence text;
        
        // for(StatusBarNotification statusBarNotification : getActiveNotifications()) {
        //    extras2222 = statusBarNotification.getNotification().extras;
        //    if (extras2222!=null) {
        //       title222 = extras2222.getCharSequence("android.title");
        //       msg222 = extras2222.getCharSequence("android.text");
        //       if (title222!=null) {
        //         Log.v(TAG, title222.toString());
        //       }
        //       if (msg222!=null) {
        //         Log.v(TAG, msg222.toString());
        //       }
            
        //       if (extras2222.getCharSequenceArray("android.textLines") != null)
        //       System.out.println(title222);
        //    }
        // }
        sendBroadcast(intent);
      } catch (Exception e) {
        e.printStackTrace() ; 
      }
    }
   
  }
  @Override
  public void onNotificationRemoved(StatusBarNotification sbn) {
      Bundle extras = sbn.getNotification().extras;
      // 获取接收消息APP的包名
      String notificationPkg = sbn.getPackageName();
      // 获取接收消息的抬头
      String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
      // 获取接收消息的内容
      String notificationText = extras.getString(Notification.EXTRA_TEXT);
      Log.i("XSL_Test", "Notification removed " + notificationTitle + " & " + notificationText);
  }
}
