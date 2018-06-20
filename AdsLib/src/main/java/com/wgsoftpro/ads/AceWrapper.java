package com.wgsoftpro.ads;

import com.sun.jna.Native;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.ShortByReference;

public class AceWrapper {

  public static AdsError AdsGetLastError() {
    AdsError retval = new AdsError();
    byte[] acValue = new byte[Ace32.ADS_MAX_ERROR_LEN];
    ShortByReference usLen = new ShortByReference(Ace32.ADS_MAX_ERROR_LEN);
    IntByReference errorCode = new IntByReference(0);
    Ace32Native.AdsGetLastError(errorCode, acValue, usLen);
    retval.setCode(errorCode.getValue());
    retval.setMessage(com.sun.jna.Native.toString(acValue).substring(0,usLen.getValue()));
    return retval;
  }

  public static String AdsGetVersion() {
    byte[] acVersion = new byte[Ace32.MAX_DATA_LEN];
    ShortByReference usLen = new ShortByReference(Ace32.MAX_DATA_LEN);
    IntByReference ulMajor = new IntByReference();
    IntByReference ulMinor = new IntByReference();
    ByteByReference ucLetter = new ByteByReference();
    checkError(Ace32Native.AdsGetVersion( ulMajor, ulMinor, ucLetter, acVersion, usLen )) ;
    return Native.toString(acVersion).substring(0,usLen.getValue());
  }

  public static void checkError(int errCode) {
    if (errCode != AdsError.AE_SUCCESS) {
      AdsError adsError = AdsGetLastError();
      throw new Ace32Exception(adsError.getMessage(), adsError.getCode());
    }
  }
}
