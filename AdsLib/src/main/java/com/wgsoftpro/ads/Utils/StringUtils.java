package com.wgsoftpro.ads.Utils;

import lombok.SneakyThrows;

import java.io.UnsupportedEncodingException;

public class StringUtils {
  public static String left(String s, int count){
    return s.substring(s.length()-count);
  }

  public static String charMix(String s1, String s2){
    int l1 = s1.length();
    int l2 = s2.length();
    int k = 0;
    StringBuilder sb = new StringBuilder();
    for(int i=0;i<l1;i++){
      sb.append(s1.charAt(i));
      if (k<l2) {
        sb.append(s2.charAt(k));
      }
      k++;
      if (k==l2) {
        k = 0;
      }
    }
    return sb.toString();
  }

  public static String replicate(String s, long count){
    StringBuilder sb = new StringBuilder(s);
    for (int i=1;i<count;i++) {
      sb.append(s);
    }
    return sb.toString();
  }

  @SneakyThrows(UnsupportedEncodingException.class)
  public static String spCrypt(String cString, String cKeyString, String cParoll)  {
    StringBuilder sb = new StringBuilder();
    if (cParoll == null) {
      cParoll = "SoftPro";
    }
    cKeyString = cKeyString.trim();
    if (cKeyString.length() < cString.length()) {
      cKeyString = StringUtils.replicate(cKeyString, Math.round((float) cString.length() / (float) cKeyString.length() + 0.5));
    }
    String cKeyShift = StringUtils.replicate(cParoll, Math.round(cString.length() / 7.0 + 0.5));
    byte[] keyBytes = cKeyString.getBytes("cp1251");
    byte[] shiftBytes = cKeyShift.getBytes("cp1251");
    byte[] strBytes = cString.getBytes("cp1251");
    byte[] retval = new byte[cString.length()];
    for (int i = 0; i < cString.length(); ++i) {
      retval[i] = (byte) (keyBytes[i] + (i < shiftBytes.length ? shiftBytes[i] : 0) - strBytes[i]); //sb.append(new String(cKeyString.getBytes("cp1251")[i]+cKeyShift.getBytes("cp1251")[i]-cString.getBytes("cp1251")[i],"cp1251"));
    }
    return new String(retval, "cp1251");
  }

}
