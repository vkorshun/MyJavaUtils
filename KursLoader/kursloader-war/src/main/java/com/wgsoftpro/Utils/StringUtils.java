package com.wgsoftpro.Utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by vkorshun on 28.11.2015.
 */
public class StringUtils {
  public static String dateToStr(Timestamp t){
    SimpleDateFormat dateFormat = new SimpleDateFormat( "dd.mm.yyyy" );
    return dateFormat.format(t);
  }
}
