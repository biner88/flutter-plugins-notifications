import 'dart:convert';

import 'package:flutter/material.dart';
import 'dart:async';
import 'package:notifications/notifications.dart';

import 'messageModel.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  late Notifications _notifications;
  late StreamSubscription<NotificationEvent> _subscription;
  // List<NotificationEvent> _log = [];
  bool started = false;

  List<MessageModel> messageList = [];

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  @override
  void dispose() {
    _subscription.cancel();
    super.dispose();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    startListening();
  }

  void onData(NotificationEvent event) {
    List<MessageModel> _ls = [];

    var data = event.messageList;
    for (var item in data) {
      MessageModel val = MessageModel.fromJson(item);
      if (val.title != '') {
        _ls.add(val);
      }
    }
    if (_ls.length > 0) {
      if (messageList.length == 0) {
        messageList = _ls;
      } else {
        if (_ls.length == 1) {
          messageList.insertAll(0, _ls);
        } else if (_ls.length > 1) {
          messageList.addAll(_ls);
        }
      }
      setState(() {});
    }
  }

  void startListening() {
    _notifications = new Notifications();
    try {
      _subscription = _notifications.notificationStream.listen(onData);
      setState(() => started = true);
    } on NotificationException catch (exception) {
      print(exception);
    }
  }

  void stopListening() {
    _subscription.cancel();
    setState(() => started = false);
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: const Text('Notifications Example app'),
        ),
        body: new Center(
            child: new ListView.builder(
                itemCount: messageList.length,
                reverse: true,
                itemBuilder: (BuildContext context, int index) {
                  final entry = messageList[index];

                  return ListTile(
                      // leading: Text(entry.postTime),
                      title: Text(entry.title),
                      subtitle: Text(
                          entry.postTime + ':' + entry.text + ':' + entry.id),
                      trailing:
                          Text(entry.packageName.toString().split('.').last));
                })),
        floatingActionButton: new FloatingActionButton(
          onPressed: started ? stopListening : startListening,
          tooltip: 'Start/Stop sensing',
          child: started ? Icon(Icons.stop) : Icon(Icons.play_arrow),
        ),
      ),
    );
  }
}
