package com.julienvignali.phone_number;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import java.util.Locale;

public class PhoneNumberPlugin implements MethodCallHandler {

  private final Context context;

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "phone_number");
    channel.setMethodCallHandler(new PhoneNumberPlugin(registrar.context()));
  }

  public PhoneNumberPlugin(Context context) {
    this.context = context;
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("formatE164")) {
      formatE164(call, result);
    } else {
      result.notImplemented();
    }
  }

  private void formatE164(MethodCall call, Result result) {
    String phone = call.argument("phone");
    String countryCode = country(context).toUpperCase();
    try {
      String formatted = PhoneNumberUtil.getInstance()
          .format(PhoneNumberUtil.getInstance().parse(phone, countryCode),
              PhoneNumberUtil.PhoneNumberFormat.E164);
      result.success(formatted);
    } catch (Exception ignored) {
      result.error("InvalidNumber", "Number is invalid", ignored);
    }
  }

  public static String country(Context context) {
    try {
      TelephonyManager manager =
          (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      String iso = manager.getSimCountryIso();
      if (iso != null && iso.length() == 2) {
        return iso;
      }
      iso = manager.getNetworkCountryIso();
      if (iso != null && iso.length() == 2) {
        return iso;
      }
    } catch (Exception ignored) {

    }
    String country = Locale.getDefault().getCountry();
    if (country != null && country.length() == 2) {
      return country;
    }
    return "ZZ";
  }
}
