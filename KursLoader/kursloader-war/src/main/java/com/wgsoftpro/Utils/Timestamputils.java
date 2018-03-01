package com.wgsoftpro.Utils;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by vkorshun on 05.12.2015.
 */
public class Timestamputils {

  public static Long dayToMiliseconds(int days){
    Long result = Long.valueOf(days * 24 * 60 * 60 * 1000);
    return result;
  }

  public static Timestamp firstDayOfMonth(Timestamp data){
    if (data == null) {
      return null;
    } else {
      Calendar d = Calendar.getInstance();
      d.setTime(data);
      //int year = d.get(Calendar.YEAR);
      //int month = d.get(Calendar.MONTH);
      //int day = 1;
      d.set(Calendar.DAY_OF_MONTH,1);
      return new Timestamp(d.getTime().getTime());
    }
  }
}
