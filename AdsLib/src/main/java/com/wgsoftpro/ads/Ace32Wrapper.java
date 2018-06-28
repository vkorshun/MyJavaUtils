package com.wgsoftpro.ads;

import com.sun.jna.Native;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.ShortByReference;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;


//@Slf4j
public class Ace32Wrapper {
  private static String defaultAnsiCharSet = "cp1251";

  public static AdsError AdsGetLastError() {
    AdsError retval = new AdsError();
    byte[] acValue = new byte[Ace32.ADS_MAX_ERROR_LEN];
    ShortByReference usLen = new ShortByReference(Ace32.ADS_MAX_ERROR_LEN);
    IntByReference errorCode = new IntByReference(0);
    Ace32Native.AdsGetLastError(errorCode, acValue, usLen);
    retval.setCode(errorCode.getValue());
    retval.setMessage(com.sun.jna.Native.toString(acValue).substring(0, usLen.getValue() - 1));
    return retval;
  }

  public static String AdsGetVersion() {
    byte[] acVersion = new byte[Ace32.MAX_DATA_LEN];
    ShortByReference usLen = new ShortByReference(Ace32.MAX_DATA_LEN);
    IntByReference ulMajor = new IntByReference();
    IntByReference ulMinor = new IntByReference();
    ByteByReference ucLetter = new ByteByReference();
    checkError(Ace32Native.AdsGetVersion(ulMajor, ulMinor, ucLetter, acVersion, usLen));
    return Native.toString(acVersion).substring(0, usLen.getValue());
  }

  public static void checkError(int errCode) {
    if (errCode != AdsError.AE_SUCCESS) {
      AdsError adsError = AdsGetLastError();
      throw new Ace32Exception(adsError.getMessage(), adsError.getCode());
    }
  }

  public static int AdsConnect60(String serverPath, short serverTypes, String userName, String password, int options) {
    IntByReference handle = new IntByReference(0);
    checkError(Ace32Native.AdsConnect60(getAnsiBytes(serverPath), serverTypes,
        getAnsiBytes(userName),
        getAnsiBytes(password),
        options, handle));
    return handle.getValue();
  }

  public static void AdsSetSQLTimeout(int hObj, int sqlTimeout) {
    checkError(Ace32Native.AdsSetSQLTimeout(hObj, sqlTimeout));
  }

  public static byte[] getAnsiBytes(String s) {
    try {
      return s.getBytes(defaultAnsiCharSet);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException();
    }
  }

  public static void AdsDisconnect(int hObj) {
    checkError(Ace32Native.AdsDisconnect(hObj));
  }

  public static String AdsGetConnectionPath(int hObj) {
    byte[] path = new byte[Ace32.ADS_MAX_PATH];
    short us = Ace32.ADS_MAX_PATH - 1;
    ShortByReference usLen = new ShortByReference(us);
    checkError(Ace32Native.AdsGetConnectionPath(hObj, path, usLen));
    return Native.toString(path).substring(0, usLen.getValue() - 1);
  }

  public static short AdsGetConnectionType(int hObj) {
    ShortByReference conType = new ShortByReference();
    checkError(Ace32Native.AdsGetConnectionType(hObj, conType));
    return conType.getValue();
  }

  public static void AdsCacheOpenTables(short count) {
    checkError(Ace32Native.AdsCacheOpenTables(count));
  }

  public static void AdsCacheOpenCursors(short count) {
    checkError(Ace32Native.AdsCacheOpenCursors(count));
  }

  public static void AdsCloseCachedTables( int hConnection ) {
    checkError(Ace32Native.AdsCloseCachedTables(hConnection));
  }

  public static int  AdsCreateSQLStatement(int  hConnect) {
    IntByReference hStm = new IntByReference();
    checkError(Ace32Native.AdsCreateSQLStatement(hConnect, hStm));
    return hStm.getValue();
  }

  public static void AdsStmtSetTableLockType(int hStatement, short usLockType) {
    checkError(Ace32Native.AdsStmtSetTableLockType(hStatement, usLockType));
  }

  public static void AdsStmtSetTableCharType(int hStatement, short usCharType) {
    checkError(Ace32Native.AdsStmtSetTableCharType(hStatement, usCharType));
  }

  public static void AdsStmtSetTableType(int hStatement, short usTableType) {
    checkError(Ace32Native.AdsStmtSetTableType(hStatement, usTableType));
  }

  public static void AdsStmtSetTablePassword(int hStatement, String tableName, String password) {
    byte[] pucTableName = getAnsiBytes(tableName);
    byte[] pucPassword = getAnsiBytes(password);
    checkError(Ace32Native.AdsStmtSetTablePassword(hStatement, pucTableName, pucPassword));
  }

}
