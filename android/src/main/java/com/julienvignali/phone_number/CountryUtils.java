
package com.julienvignali.phone_number;

import android.content.Context;
import android.telephony.TelephonyManager;
import java.util.Locale;

/**
 * Created by taehyunpark on 3/10/16.
 */
public class CountryUtils {
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
