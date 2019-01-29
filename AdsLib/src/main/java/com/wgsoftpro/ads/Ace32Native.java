package com.wgsoftpro.ads;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.WString;
import com.sun.jna.ptr.*;

public class Ace32Native {

  public static void init(String var0) {
    com.sun.jna.Native.register(var0);
  }

  public static void destroy() {
    com.sun.jna.Native.unregister();
  }

  public static native int AdsGetVersion(IntByReference pulMajor,
                                         IntByReference pulMinor,
                                         ByteByReference pucLetter,
                                         byte[] pucDesc,
                                         ShortByReference pusDescLen);

  public static native int AdsGetLastError(IntByReference pulErrCode,
                                           byte[] pucBuf,
                                           ShortByReference pusBufLen);

  public static native int AdsConnect101(byte[] connectOptions, Pointer p1, NativeLongByReference p2);

  public static native int AdsConnect60(
      byte[] pucServerPath,//: PAceChar;
      short usServerTypes, //: UNSIGNED16;
      byte[] pucUserName, //: PAceChar;
      byte[] pucPassword, //: PAceChar;
      int ulOptions, //: UNSIGNED32;
      NativeLongByReference phConnect); //: pADSHANDLE );

  public static native int AdsSetSQLTimeout(NativeLong hObj, //: ADSHANDLE,
                                            NativeLong ulTimeout //: UNSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsDisconnect(NativeLong hConnect//: ADSHANDLE )
  );

  public static native int AdsGetConnectionPath(NativeLong hConnect//: ADSHANDLE;
      , byte[] pucConnectionPath //: PAceChar;
      , ShortByReference pusLen //: PUNSIGNED16 ):UNSIGNED32;
  );

  public static native int AdsGetConnectionType(NativeLong hConnect//: ADSHANDLE;
      , ShortByReference pusConnectType //: PUNSIGNED16 ):UNSIGNED32;
  );

  public static native int AdsCacheOpenTables(short count);

  public static native int AdsCacheOpenCursors(short count);

  public static native int AdsCloseCachedTables(NativeLong hConnection);

  public static native int AdsCreateSQLStatement(NativeLong hConnect //: ADSHANDLE;
      , NativeLongByReference phStatement //: pADSHANDLE //):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsStmtSetTableLockType(NativeLong hStatement//: ADSHANDLE;
      , short usLockType//: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsStmtSetTableCharType(NativeLong hStatement //: ADSHANDLE;
      , short usCharType //: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsStmtSetTableType(NativeLong hStatement//: ADSHANDLE;
      , short usTableType //: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsStmtSetTablePassword(NativeLong hStatement//: ADSHANDLE;
      , byte[] pucTableName//: PAceChar;
      , byte[] pucPassword //: PAceChar ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsPrepareSQL(NativeLong hStatement//: ADSHANDLE;
      , byte[] pucSQL//: PAceChar ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsPrepareSQLW(NativeLong hStatement//: ADSHANDLE;
      , WString pwcSQL//: PWideChar ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsCachePrepareSQL(NativeLong hConnect//: ADSHANDLE;
      , byte[] pucSQL//: PAceChar;
      , NativeLongByReference phStatement//: pADSHANDLE ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsCachePrepareSQLW(NativeLong hConnect//: ADSHANDLE;
      , WString pwcSQL//: PWideChar;
      , NativeLongByReference phStatement//: pADSHANDLE ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsExecuteSQL(NativeLong hStatement//: ADSHANDLE;
      , NativeLongByReference phCursor//: pADSHANDLE ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsExecuteSQLDirect(NativeLong hStatement//: ADSHANDLE;
      , byte[] pucSQL//: PAceChar;
      , NativeLongByReference phCursor//: pADSHANDLE ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsExecuteSQLDirectW(NativeLong hStatement//: ADSHANDLE;
      , WString pwcSQL//: PWideChar;
      , NativeLongByReference phCursor//: pADSHANDLE ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsCloseSQLStatement(NativeLong hStatement);//: ADSHANDLE ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsStmtSetTableRights(NativeLong hStatement//: ADSHANDLE;
      , short usCheckRights//: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsStmtSetTableReadOnly(NativeLong hStatement//: ADSHANDLE;
      , short usReadOnly//: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsCloseTable(NativeLong hTable);

  public static native int AdsSetString( NativeLong hObj//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,byte[] pucBuf//: PAceChar;
      ,NativeLong ulLen//: UNSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsSetTime( NativeLong hObj//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,byte[] pucBuf//: PAceChar;
      ,NativeLong ulLen//: UNSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsSetDate(NativeLong hObj//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,byte[] pucValue//: PAceChar;
      , short usLen//: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsSetDateFormat60(NativeLong hConnect//: ADSHANDLE;
      , byte[] pucFormat//: PAceChar ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  );

  public static native int AdsSetDecimals( short usDecimals); //: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsSetDefault( byte[] pucDefault);//: PAceChar ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsShowDeleted( short bShowDeleted);//: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsSetDouble(NativeLong hObj//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,double dValue);//: DOUBLE ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsSetEmpty(NativeLong hObj//: ADSHANDLE;
      ,byte[] pucFldName);//: PAceChar ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsSetField(NativeLong hObj//: ADSHANDLE;
      , byte[] pucFldName//: PAceChar;
      , Pointer pucBuf//: PAceChar;
      , NativeLong ulLen);//: UNSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsSetHandleLong(NativeLong hObj//: ADSHANDLE;
      ,NativeLong ulVal);//: UNSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsSetLogical(NativeLong hObj//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,short bValue);//: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsSetLong(NativeLong hObj//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,NativeLong lValue);//: SIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsSetLongLong(NativeLong hObj//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,long qValue);//: SIGNED64 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}


  public static native int AdsSetMilliseconds(NativeLong hObj//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,int lTime);//: SIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsSetMoney(NativeLong hObj//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,long qValue);//: SIGNED64 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsSetTimeStampRaw( NativeLong hObj// : ADSHANDLE;
      ,byte[] pucFldName// : PAceChar;
      ,byte[] pucBuf// : PAceChar;
      ,NativeLong ulLen);// : UNSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsSetBinary( NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,short usBinaryType//: UNSIGNED16;
      ,NativeLong ulTotalLength//: UNSIGNED32;
      ,NativeLong ulOffset//: UNSIGNED32;
      ,byte[] pucBuf//: PAceBinary;
      ,NativeLong ulLen );//:UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetString( NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,byte[] pucBuf//: PAceChar;
      ,NativeLongByReference pulLen//: PUNSIGNED32;
      ,short usOption);//: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetStringW(NativeLong hObj//: ADSHANDLE;
      , byte[] pucFldName//: PAceChar;
      , WString pwcBuf//: PWideChar;
      , NativeLongByReference pulLen//: PUNSIGNED32;
      , short usOption);//: UNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetLogical( NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,ShortByReference pbValue);//: PUNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetLong(NativeLong hTable//: ADSHANDLE;
      , byte[] pucFldName//: PAceChar;
      , NativeLongByReference plValue);//: pSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetLongLong( NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,LongByReference pqValue);//: pSIGNED64 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetMemoBlockSize(NativeLong hTable//: ADSHANDLE;
      ,ShortByReference pusBlockSize);//: PUNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetMemoLength( NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,NativeLongByReference pulLength);//: PUNSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetMemoDataType(NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,ShortByReference pusType);//: PUNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetMilliseconds( NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,IntByReference plTime);//: pSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetMoney( NativeLong hTbl//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,LongByReference pqValue);//: pSIGNED64 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetDouble( NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,DoubleByReference pdValue);//: pDOUBLE ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetField( NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,Pointer pucBuf//: PAceChar;
      ,NativeLongByReference pulLen//: PUNSIGNED32;
      ,short usOption );//:UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetBinary(NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,NativeLong ulOffset//: UNSIGNED32;
      ,byte[] pucBuf//: PAceBinary;
      ,NativeLongByReference pulLen);//: PUNSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetBinaryLength( NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,NativeLongByReference pulLength);//: PUNSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}


  public static native int AdsGetFieldDecimals( NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,ShortByReference pusDecimals);//: PUNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetFieldLength(NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,NativeLongByReference pulLength);//: PUNSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetFieldName( NativeLong hTable//: ADSHANDLE;
      ,short usFld//: UNSIGNED16;
      ,byte[] pucName//: PAceChar;
      ,ShortByReference pusBufLen);//: PUNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetFieldNum( NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,ShortByReference pusNum);//: PUNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetFieldOffset(NativeLong  hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,NativeLongByReference pulOffset);//: PUNSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetFieldType(NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,ShortByReference pusType);//: PUNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetNumFields( NativeLong hTable//: ADSHANDLE;
      ,ShortByReference pusCount);//: PUNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGetDate( NativeLong hTable//: ADSHANDLE;
      ,byte[] pucFldName//: PAceChar;
      ,byte[] pucBuf//: PAceChar;
      ,ShortByReference pusLen);//: PUNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGotoBottom( NativeLong hObj );//:UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGotoRecord( NativeLong hTable//: ADSHANDLE;
      ,NativeLong ulRec);//: UNSIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsGotoTop( NativeLong hObj);//: ADSHANDLE ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsSkip( NativeLong hObj//: ADSHANDLE;
      ,int lRecs);//: SIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsSkipUnique(NativeLong hIndex//: ADSHANDLE;
      ,int lRecs);//: SIGNED32 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsAtBOF( NativeLong hTable//: ADSHANDLE;
      ,ShortByReference pbBof);//: PUNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}

  public static native int AdsAtEOF(NativeLong hTable//: ADSHANDLE;
      ,ShortByReference pbEof);//: PUNSIGNED16 ):UNSIGNED32; {$IFDEF WIN32}stdcall;{$ENDIF}{$IFDEF LINUX}cdecl;{$ENDIF}
  
}
