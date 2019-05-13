import 'dart:async';

import 'package:flutter/services.dart';

class PhoneNumber {
  static const MethodChannel _channel = const MethodChannel('phone_number');

//  static Future<dynamic> parse(String string, {String region}) async {
//    final args = {"string": string, "region": region};
//    final result = await _channel.invokeMethod("parse", args);
//    return result;
//  }

  static Future<dynamic> formatE164(String phone) async {
    final result = await _channel.invokeMethod("formatE164", {"phone": phone});
    return result;
  }
}
