import 'dart:async';

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
  final String text;
  final String packageName;
  final String title;
  final String postTime;

  NotificationEvent({this.packageName, this.title, this.text, this.postTime});

  factory NotificationEvent.fromMap(Map<dynamic, dynamic> map) {
    String name = map['packageName'] ?? '';
    String text = map['text'] ?? '';
    String title = map['title'] ?? '';
    String postTime = map['post_time'] ?? "0";
    return NotificationEvent(
        packageName: name, title: title, text: text, postTime: postTime);
  }

  @override
  String toString() {
    return "Notification Event \n"
        "Package Name: $packageName \n "
        "Title: $title \n"
        "Text: $text \n"
        "PostTime: $postTime \n";
  }
}

NotificationEvent _notificationEvent(dynamic data) {
  return NotificationEvent.fromMap(data);
}

class Notifications {
  static const EventChannel _notificationEventChannel =
      EventChannel('notifications');

  Stream<NotificationEvent> _notificationStream;

  Stream<NotificationEvent> get notificationStream {
    if (Platform.isAndroid) {
      if (_notificationStream == null) {
        _notificationStream = _notificationEventChannel
            .receiveBroadcastStream()
            .map((event) => _notificationEvent(event));
      }
      return _notificationStream;
    }
    throw NotificationException(
        'Notification API exclusively available on Android!');
  }
}
