package dk.cachet.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;

import androidx.annotation.RequiresApi;
import io.flutter.plugin.common.EventChannel.EventSink;

/**
 * Receives events from @NotificationListener
 */

public class NotificationReceiver extends BroadcastReceiver  {

  private EventSink eventSink;

  public NotificationReceiver(EventSink eventSink) {
    this.eventSink = eventSink;
  }

  @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
  @Override
  public void onReceive(Context context, Intent intent) {
    String _messageList = intent.getStringExtra(NotificationListener.NOTIFICATION_MESSAGE_LIST);
    eventSink.success(_messageList);
  }
}
