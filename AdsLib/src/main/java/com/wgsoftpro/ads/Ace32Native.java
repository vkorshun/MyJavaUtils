package com.wgsoftpro.ads;

import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.ShortByReference;

public class Ace32Native {

  public static void init(String var0) {
    com.sun.jna.Native.register(var0);
  }

  public static void destroy(){
    com.sun.jna.Native.unregister();
  }

  public static native int AdsGetVersion(IntByReference pulMajor,
                                         IntByReference pulMinor,
                                         ByteByReference pucLetter,
                                         byte[] pucDesc,
                                         ShortByReference pusDescLen );

  public static native int AdsGetLastError(IntByReference pulErrCode,
                                           byte[] pucBuf,
                                           ShortByReference pusBufLen);

  public static native int AdsConnect60(
      byte[] pucServerPath,//: PAceChar;
      short usServerTypes, //: UNSIGNED16;
      byte[] pucUserName, //: PAceChar;
      byte[] pucPassword, //: PAceChar;
      int ulOptions, //: UNSIGNED32;
      IntByReference phConnect); //: pADSHANDLE );

  public static native int AdsSetSQLTimeout( int hObj, //: ADSHANDLE,
      int ulTimeout //: UNSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsDisconnect(int hConnect//: ADSHANDLE )
  );
}
