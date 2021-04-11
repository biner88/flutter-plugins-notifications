import 'package:flutter/material.dart';
import 'dart:async';
import 'package:notifications/notifications.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Notifications _notifications;
  StreamSubscription<NotificationEvent> _subscription;
  List<NotificationEvent> _log = [];
  bool started = false;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    startListening();
  }

  void onData(NotificationEvent event) {
    setState(() {
      _log.add(event);
      print("title:::" + event.title.toString());
      print("text:::" + event.text.toString());
      print("postTime:::" + event.postTime.toString());
      print("messageList:::" + event.messageList.toString());
    });
    print(event.toString());
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
                itemCount: _log.length,
                reverse: true,
                itemBuilder: (BuildContext context, int idx) {
                  final entry = _log[idx];

                  return ListTile(
                      leading: Text(entry.postTime),
                      title: Text(entry.title),
                      subtitle: Text(entry.text),
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
