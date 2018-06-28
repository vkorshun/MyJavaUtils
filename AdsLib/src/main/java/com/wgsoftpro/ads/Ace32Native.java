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

  public static native int AdsGetConnectionPath(int hConnect//: ADSHANDLE;
      , byte[] pucConnectionPath //: PAceChar;
      , ShortByReference pusLen //: PUNSIGNED16 ):UNSIGNED32;
  );

  public static native int AdsGetConnectionType( int hConnect//: ADSHANDLE;
      ,ShortByReference pusConnectType //: PUNSIGNED16 ):UNSIGNED32;
  );

  public static native int AdsCacheOpenTables(short count);
  public static native int AdsCacheOpenCursors(short count);

  public static native int AdsCloseCachedTables( int hConnection );

  public static native int AdsCreateSQLStatement(int  hConnect //: ADSHANDLE;
      ,IntByReference phStatement //: pADSHANDLE //):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsStmtSetTableLockType(int hStatement//: ADSHANDLE;
      ,short usLockType//: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsStmtSetTableCharType(int hStatement //: ADSHANDLE;
      ,short usCharType //: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsStmtSetTableType(int hStatement//: ADSHANDLE;
      ,short usTableType //: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsStmtSetTablePassword(int hStatement//: ADSHANDLE;
      ,byte[] pucTableName//: PAceChar;
      ,byte[] pucPassword //: PAceChar ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );
}
