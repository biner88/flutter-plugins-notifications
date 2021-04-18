import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';
import 'dart:io' show Platform;

/// Custom Exception for the plugin,
/// thrown whenever the plugin is used on platforms other than Android
class NotificationException implements Exception {
  String _cause;

  NotificationException(this._cause);

  @override
  String toString() {
    return _cause;
  }
}

class NotificationEvent {
  final dynamic messageList;

  NotificationEvent({required this.messageList});

  factory NotificationEvent.fromData(dynamic data) {
    return NotificationEvent(messageList: jsonDecode(data));
  }
}

NotificationEvent _notificationEvent(dynamic data) {
  return NotificationEvent.fromData(data);
}

class Notifications {
  static const EventChannel _notificationEventChannel =
      EventChannel('notifications');

  late Stream<NotificationEvent> _notificationStream;

  Stream<NotificationEvent> get notificationStream {
    if (Platform.isAndroid) {
      _notificationStream = _notificationEventChannel
          .receiveBroadcastStream()
          .map((event) => _notificationEvent(event));
      return _notificationStream;
    }
    throw NotificationException(
        'Notification API exclusively available on Android!');
  }
}
