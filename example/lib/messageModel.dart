class MessageModel {
  late String id;
  late String title;
  late String text;
  late String packageName;
  late String postTime;

  MessageModel({
    required this.id,
    required this.title,
    required this.text,
    required this.packageName,
    required this.postTime,
  });

  MessageModel.fromJson(Map<String, dynamic> json) {
    id = json['id'] ?? '';
    title = json['title'] ?? '';
    text = json['text'] ?? '';
    packageName = json['packageName'] ?? '';
    postTime = json['postTime'] ?? '';
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['title'] = this.title;
    data['text'] = this.text;
    data['packageName'] = this.packageName;
    data['postTime'] = this.postTime;
    return data;
  }
}
