package com.wgsoftpro.ads.Utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateUtils {
  public static String dtos(Timestamp d) {
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    return format.format(d);
  }
}
