package dk.cachet.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;

import java.util.HashMap;

import androidx.annotation.RequiresApi;
import io.flutter.plugin.common.EventChannel.EventSink;

/**
 * Receives events from @NotificationListener
 */

public class NotificationReceiver extends BroadcastReceiver {

  private EventSink eventSink;

  public NotificationReceiver(EventSink eventSink) {
    this.eventSink = eventSink;
  }

  @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
  @Override
  public void onReceive(Context context, Intent intent) {
    /// Unpack intent contents
//    String packageName = intent.getStringExtra(NotificationListener.NOTIFICATION_PACKAGE_NAME);
//    String title = intent.getStringExtra(NotificationListener.NOTIFICATION_TITLE);
//    String content = intent.getStringExtra(NotificationListener.NOTIFICATION_TEXT);
//    String post_time = intent.getStringExtra(NotificationListener.NOTIFICATION_POST_TIME);
    String message_list = intent.getStringExtra(NotificationListener.NOTIFICATION_MESSAGE_LIST);

    /// Send data back via the Event Sink
    HashMap<String, Object> data = new HashMap<>();
//    data.put("packageName", packageName);
//    data.put("title", title);
//    data.put("text", content);
//    data.put("post_time", post_time);
    data.put("message_list", message_list);
    eventSink.success(data);
  }
}
