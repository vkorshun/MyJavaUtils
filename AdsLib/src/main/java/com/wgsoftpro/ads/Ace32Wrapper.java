package com.wgsoftpro.ads;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.*;
import jdk.nashorn.internal.objects.NativeString;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


//@Slf4j
public class Ace32Wrapper {
  private static String defaultAnsiCharSet = "cp1251";
  private static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
  private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

  public static SimpleDateFormat getSimpleDateTimeFormat() {
    return simpleDateTimeFormat;
  }

  public static void setSimpleDateTimeFormat(SimpleDateFormat format) {
    simpleDateTimeFormat = format;
  }

  public static SimpleDateFormat getSimpleDateFormat() {
    return simpleDateFormat;
  }

  public static void setSimpleDateFormat(SimpleDateFormat format) {
    simpleDateFormat = format;
  }

  public static AdsError AdsGetLastError() {
    AdsError retval = new AdsError();
    byte[] acValue = new byte[Ace32.ADS_MAX_ERROR_LEN];
    ShortByReference usLen = new ShortByReference(Ace32.ADS_MAX_ERROR_LEN);
    IntByReference errorCode = new IntByReference(0);
    Ace32Native.AdsGetLastError(errorCode, acValue, usLen);
    retval.setCode(errorCode.getValue());
    retval.setMessage(com.sun.jna.Native.toString(acValue, defaultAnsiCharSet).substring(0, usLen.getValue()));
    return retval;
  }

  public static String AdsGetVersion() {
    byte[] acVersion = new byte[Ace32.MAX_DATA_LEN];
    ShortByReference usLen = new ShortByReference(Ace32.MAX_DATA_LEN);
    IntByReference ulMajor = new IntByReference();
    IntByReference ulMinor = new IntByReference();
    ByteByReference ucLetter = new ByteByReference();
    checkError(Ace32Native.AdsGetVersion(ulMajor, ulMinor, ucLetter, acVersion, usLen));
    return Native.toString(acVersion, defaultAnsiCharSet).substring(0, usLen.getValue());
  }

  public static void checkError(int errCode) {
    if (errCode != AdsError.AE_SUCCESS) {
      AdsError adsError = AdsGetLastError();
      throw new Ace32Exception(adsError.getMessage(), adsError.getCode());
    }
  }

  public static NativeLong AdsConnect60(String serverPath, short serverTypes, String userName, String password, int options) {
    NativeLongByReference handle = new NativeLongByReference(new NativeLong(0));
    checkError(Ace32Native.AdsConnect60(getAnsiBytes(serverPath), serverTypes,
        getAnsiBytes(userName),
        getAnsiBytes(password),
        options, handle));
    return handle.getValue();
  }

  public static void AdsSetSQLTimeout(NativeLong hObj, NativeLong sqlTimeout) {
    checkError(Ace32Native.AdsSetSQLTimeout(hObj, sqlTimeout));
  }

  public static byte[] getAnsiBytes(String s) {
    return Native.toByteArray(s, defaultAnsiCharSet);
  }

  public static void AdsDisconnect(NativeLong hObj) {
    checkError(Ace32Native.AdsDisconnect(hObj));
  }

  public static String AdsGetConnectionPath(NativeLong hObj) {
    byte[] path = new byte[Ace32.ADS_MAX_PATH];
    short us = Ace32.ADS_MAX_PATH - 1;
    ShortByReference usLen = new ShortByReference(us);
    checkError(Ace32Native.AdsGetConnectionPath(hObj, path, usLen));
    return Native.toString(path, defaultAnsiCharSet).substring(0, usLen.getValue());
  }

  public static short AdsGetConnectionType(NativeLong hObj) {
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

  public static void AdsCloseCachedTables(NativeLong hConnection) {
    checkError(Ace32Native.AdsCloseCachedTables(hConnection));
  }

  public static NativeLong AdsCreateSQLStatement(NativeLong hConnect) {
    NativeLongByReference hStm = new NativeLongByReference();
    checkError(Ace32Native.AdsCreateSQLStatement(hConnect, hStm));
    return hStm.getValue();
  }

  public static void AdsStmtSetTableLockType(NativeLong hStatement, short usLockType) {
    checkError(Ace32Native.AdsStmtSetTableLockType(hStatement, usLockType));
  }

  public static void AdsStmtSetTableCharType(NativeLong hStatement, short usCharType) {
    checkError(Ace32Native.AdsStmtSetTableCharType(hStatement, usCharType));
  }

  public static void AdsStmtSetTableType(NativeLong hStatement, short usTableType) {
    checkError(Ace32Native.AdsStmtSetTableType(hStatement, usTableType));
  }

  public static void AdsStmtSetTablePassword(NativeLong hStatement, String tableName, String password) {
    byte[] pucTableName = getAnsiBytes(tableName);
    byte[] pucPassword = getAnsiBytes(password);
    checkError(Ace32Native.AdsStmtSetTablePassword(hStatement, pucTableName, pucPassword));
  }

  public static void AdsPrepareSQL(NativeLong hStatement, String cSQL) {
    byte[] pucSQL = getAnsiBytes(cSQL);
    checkError(Ace32Native.AdsPrepareSQL(hStatement, pucSQL));
  }


  public static void AdsPrepareSQLW(NativeLong hStatement, String cSQL) {
    checkError(Ace32Native.AdsPrepareSQLW(hStatement, new WString(cSQL)));
  }

  public static NativeLong AdsCachePrepareSQL(NativeLong hConnect, String cSQL, NativeLong hStatement) {
    byte[] pucSQL = getAnsiBytes(cSQL);
    NativeLongByReference phStatement = new NativeLongByReference(hStatement);
    checkError(Ace32Native.AdsCachePrepareSQL(hConnect, pucSQL, phStatement));
    return phStatement.getValue();
  }

  public static NativeLong AdsCachePrepareSQLW(NativeLong hConnect, String cSQL, NativeLong hStatement) {
    NativeLongByReference phStatement = new NativeLongByReference(hStatement);
    checkError(Ace32Native.AdsCachePrepareSQLW(hConnect, new WString(cSQL), phStatement));
    return phStatement.getValue();
  }

  public static NativeLong AdsExecuteSQL(NativeLong hStatement) {
    NativeLongByReference phCursor = new NativeLongByReference();
    checkError(Ace32Native.AdsExecuteSQL(hStatement, phCursor));
    return phCursor.getValue();
  }

  public static NativeLong AdsExecuteSQLDirect(NativeLong hStatement, String cSQL) {
    NativeLongByReference phCursor = new NativeLongByReference();
    byte[] pucSQL = getAnsiBytes(cSQL);
    checkError(Ace32Native.AdsExecuteSQLDirect(hStatement, pucSQL, phCursor));
    return phCursor.getValue();
  }

  public static NativeLong AdsExecuteSQLDirectW(NativeLong hStatement, String cSQL) {
    NativeLongByReference phCursor = new NativeLongByReference();
    checkError(Ace32Native.AdsExecuteSQLDirectW(hStatement, new WString(cSQL), phCursor));
    return phCursor.getValue();
  }

  public static void AdsCloseSQLStatement(NativeLong hStatement) {
    checkError(Ace32Native.AdsCloseSQLStatement(hStatement));
  }

  public static void AdsStmtSetTableRights(NativeLong hStatement, short usCheckRights) {
    checkError(Ace32Native.AdsStmtSetTableRights(hStatement, usCheckRights));
  }

  public static void AdsStmtSetTableReadOnly(NativeLong hStatement, short usReadOnly) {
    checkError(Ace32Native.AdsStmtSetTableReadOnly(hStatement, usReadOnly));
  }

  public static void AdsCloseTable(NativeLong hTable) {
    checkError(Ace32Native.AdsCloseTable(hTable));
  }

  public static void AdsSetString(NativeLong hObj, String fldName, String value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    byte[] pucBuf = getAnsiBytes(value);
    NativeLong uLen = new NativeLong(pucBuf.length);
    checkError(Ace32Native.AdsSetString(hObj, pucFldName, pucBuf, uLen));
  }

  public static void AdsSetTime(NativeLong hObj, String fldName, String value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    byte[] pucBuf = getAnsiBytes(value);
    NativeLong uLen = new NativeLong(pucBuf.length);
    checkError(Ace32Native.AdsSetTime(hObj, pucFldName, pucBuf, uLen));
  }

  public static void AdsSetDate(NativeLong hObj, String fldName, String value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    byte[] pucBuf = getAnsiBytes(value);
    short uLen = (short) pucBuf.length;
    checkError(Ace32Native.AdsSetDate(hObj, pucFldName, pucBuf, uLen));
  }

  public static void AdsSetDateFormat60(NativeLong hConnect, String format) {
    byte[] pucFormat = getAnsiBytes(format);
    checkError(Ace32Native.AdsSetDateFormat60(hConnect, pucFormat));
  }

  public static void AdsSetDecimal(short usDecimals) {
    checkError(Ace32Native.AdsSetDecimals(usDecimals));
  }

  public static void AdsSetDefault(String defValue) {
    byte[] pucDefault = getAnsiBytes(defValue);
    checkError(Ace32Native.AdsSetDefault(pucDefault));
  }

  public static void AdsShowDeleted(short bShowDeleted) {
    checkError(Ace32Native.AdsShowDeleted(bShowDeleted));
  }

  public static void AdsSetDouble(NativeLong hObj, String fldName, double value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsSetDouble(hObj, pucFldName, value));
  }

  public static void AdsSetEmpty(NativeLong hObj, String fldName) {
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsSetEmpty(hObj, pucFldName));
  }

  public static void AdsSetField(NativeLong hObj, String fldName, Pointer p, NativeLong uLen) {
    byte[] pucFldName = getAnsiBytes(fldName);
    //byte[] pucBuf = getAnsiBytes(value);
    //int uLen = pucBuf.length;
    // StringByReference _val = new IntByReference(0);
    checkError(Ace32Native.AdsSetField(hObj, pucFldName, p, uLen));
  }

  public static void AdsSetHandleLong(NativeLong hObj, long uVal) {
    checkError(Ace32Native.AdsSetHandleLong(hObj, new NativeLong(uVal)));
  }

  public static void AdsSetLogical(NativeLong hObj, String fldName, boolean value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    short uVal = (short) (value ? Ace32.ADS_TRUE : Ace32.ADS_FALSE);
    checkError(Ace32Native.AdsSetLogical(hObj, pucFldName, uVal));
  }

  public static void AdsSetLong(NativeLong hObj, String fldName, long value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsSetLong(hObj, pucFldName, new NativeLong(value)));
  }

  public static void AdsSetLongLong(NativeLong hObj, String fldName, Long value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsSetLongLong(hObj, pucFldName, value));
  }

  //to-do it's need check mashtab
  public static void AdsSetMilliseconds(NativeLong hObj, String fldName, Time value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsSetMilliseconds(hObj, pucFldName, (int) value.getTime()));
  }

  public static void AdsSetMoney(NativeLong hObj, String fldName, Long value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsSetMoney(hObj, pucFldName, value));
  }

  public static void AdsSetTimeStampRaw(NativeLong hObj, String fldName, Timestamp value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    byte[] pucBuf = simpleDateTimeFormat.format(value).getBytes();
    checkError(Ace32Native.AdsSetTimeStampRaw(hObj, pucFldName, pucBuf, new NativeLong(pucBuf.length)));
  }

  public static void AdsSetBinary(NativeLong hTable, String fldName, short usBinaryType, NativeLong ulTotalLength
      , NativeLong ulOffset, byte[] pucBuf) {
    checkError(Ace32Native.AdsSetBinary(hTable, getAnsiBytes(fldName), usBinaryType, ulTotalLength, ulOffset, pucBuf, new NativeLong(pucBuf.length)));
  }

  public static String AdsGetString(NativeLong hTable, String fldName, long size) {
    byte[] pucFldName = getAnsiBytes(fldName);
    byte[] pucBuf = new byte[(int) size + 1];
    NativeLongByReference realSize = new NativeLongByReference(new NativeLong(size + 1));
    checkError(Ace32Native.AdsGetString(hTable, pucFldName, pucBuf, realSize, Ace32.ADS_TRIM));
    return Native.toString(pucBuf, defaultAnsiCharSet).substring(0, realSize.getValue().intValue());
  }

  public static String AdsGetStringW(NativeLong hObj, String fldName, long size) {
    byte[] pucFldName = getAnsiBytes(fldName);
    NativeLongByReference realSize = new NativeLongByReference();
    WString retval = new WString(new String(new byte[(short) size + 1]));
    checkError(Ace32Native.AdsGetStringW(hObj, pucFldName, retval, realSize, Ace32.ADS_TRIM));
    return retval.toString().substring(0, (short) size - 1);
  }

  public static Boolean AdsGetLogical(NativeLong hTable, String fldName) {
    ShortByReference retval = new ShortByReference(Ace32.ADS_FALSE);
    checkError(Ace32Native.AdsGetLogical(hTable, getAnsiBytes(fldName), retval));
    return new Boolean(retval.getValue() == Ace32.ADS_TRUE);
  }

  public static long AdsGetLong(NativeLong hTable, String fldName) {
    NativeLongByReference retval = new NativeLongByReference();
    checkError(Ace32Native.AdsGetLong(hTable, getAnsiBytes(fldName), retval));
    return new Long(retval.getValue().longValue());
  }

  public static long AdsGetLongLong(NativeLong hTable, String fldName) {
    LongByReference retval = new LongByReference();
    checkError(Ace32Native.AdsGetLongLong(hTable, getAnsiBytes(fldName), retval));
    return new Long(retval.getValue());
  }

  public static Short AdsGetMemoBlockSize(NativeLong hTable) {
    ShortByReference retval = new ShortByReference();
    checkError(Ace32Native.AdsGetMemoBlockSize(hTable, retval));
    return new Short(retval.getValue());
  }

  public static Long AdsGetMemoLength(NativeLong hTable, String fldName) {
    NativeLongByReference retval = new NativeLongByReference();
    checkError(Ace32Native.AdsGetMemoLength(hTable, getAnsiBytes(fldName), retval));
    return new Long(retval.getValue().longValue());
  }

  public static Short AdsGetMemoDataType(NativeLong hTable, String fldName) {
    ShortByReference retval = new ShortByReference(Ace32.ADS_FALSE);
    checkError(Ace32Native.AdsGetMemoDataType(hTable, getAnsiBytes(fldName), retval));
    return new Short(retval.getValue());
  }

  public static Integer AdsGetMilliseconds(NativeLong hTable, String fldName) {
    IntByReference retval = new IntByReference();
    checkError(Ace32Native.AdsGetMilliseconds(hTable, getAnsiBytes(fldName), retval));
    return new Integer(retval.getValue());
  }

  public static Long AdsGetMoney(NativeLong hTable, String fldName) {
    LongByReference retval = new LongByReference();
    checkError(Ace32Native.AdsGetMoney(hTable, getAnsiBytes(fldName), retval));
    return new Long(retval.getValue());
  }

  public static Double AdsGetDouble(NativeLong hTable, String fldName) {
    DoubleByReference retval = new DoubleByReference();
    checkError(Ace32Native.AdsGetDouble(hTable, getAnsiBytes(fldName), retval));
    return new Double(retval.getValue());
  }

  public static Long AdsGetField(NativeLong hTable, String fldName, Pointer p) {
    NativeLongByReference retval = new NativeLongByReference();
    checkError(Ace32Native.AdsGetField(hTable, getAnsiBytes(fldName), p, retval, Ace32.ADS_NONE));
    return new Long(retval.getValue().longValue());
  }


  public static byte[] AdsGetBinary(NativeLong hTable, String fldName, int size) {
    NativeLongByReference retSize = new NativeLongByReference();
    byte[] retval = new byte[size];
    checkError(Ace32Native.AdsGetBinary(hTable, getAnsiBytes(fldName), new NativeLong(size), retval, retSize));
    return retval;
  }

  public static Long AdsGetBinaryLength(NativeLong hTable, String fldName) {
    NativeLongByReference retval = new NativeLongByReference();
    checkError(Ace32Native.AdsGetBinaryLength(hTable, getAnsiBytes(fldName), retval));
    return new Long(retval.getValue().longValue());
  }

  public static Short AdsGetFieldDecimals(NativeLong hTable, String fldName) {
    ShortByReference retval = new ShortByReference();
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsGetFieldDecimals(hTable, pucFldName, retval));
    return new Short(retval.getValue());
  }

  public static Long AdsGetFieldLength(NativeLong hTable, String fldName) {
    NativeLongByReference retval = new NativeLongByReference();
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsGetFieldLength(hTable, pucFldName, retval));
    return new Long(retval.getValue().longValue());
  }

  public static String AdsGetFieldName(NativeLong hTable, short fldNumber) {
    ShortByReference retval = new ShortByReference((short) 255);
    byte[] pucFldName = new byte[255];
    checkError(Ace32Native.AdsGetFieldName(hTable, fldNumber, pucFldName, retval));
    return Native.toString(pucFldName, defaultAnsiCharSet).substring(0, retval.getValue());
  }


  public static Short AdsGetFieldNum(NativeLong hTable, String fldName) {
    ShortByReference retval = new ShortByReference();
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsGetFieldNum(hTable, pucFldName, retval));
    return new Short(retval.getValue());
  }

  public static Long AdsGetFieldOffset(NativeLong hTable, String fldName) {
    NativeLongByReference retval = new NativeLongByReference();
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsGetFieldOffset(hTable, pucFldName, retval));
    return new Long(retval.getValue().longValue());
  }

  public static Short AdsGetFieldType(NativeLong hTable, String fldName) {
    ShortByReference retval = new ShortByReference();
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsGetFieldType(hTable, pucFldName, retval));
    return new Short(retval.getValue());
  }

  public static Short AdsGetNumFields(NativeLong hTable) {
    ShortByReference retval = new ShortByReference();
    checkError(Ace32Native.AdsGetNumFields(hTable, retval));
    return new Short(retval.getValue());
  }

  public static String AdsGetDate(NativeLong hTable, String fldName) {
    byte[] pucFldName = getAnsiBytes(fldName);
    byte[] pucBuf = new byte[11];
    ShortByReference retsize = new ShortByReference((short) 11);
    checkError(Ace32Native.AdsGetDate(hTable, pucFldName, pucBuf, retsize));
    return Native.toString(pucBuf, defaultAnsiCharSet).substring(0, retsize.getValue());
  }

  public static void AdsGotoBottom(NativeLong hObj) {
    checkError(Ace32Native.AdsGotoBottom(hObj));
  }

  public static void AdsGotoRecord(NativeLong hTable, long nRec) {
    checkError(Ace32Native.AdsGotoRecord(hTable, new NativeLong(nRec)));
  }

  public static void AdsGotoTop(NativeLong hObj) {
    checkError(Ace32Native.AdsGotoTop(hObj));
  }

  public static void AdsSkip(NativeLong hObj, int nRec) {
    checkError(Ace32Native.AdsSkip(hObj, nRec));
  }

  public static void AdsSkipUnique(NativeLong hIndex, int nRec) {
    checkError(Ace32Native.AdsSkipUnique(hIndex, nRec));
  }

  public static boolean AdsAtBOF( NativeLong hTable){
    ShortByReference pbBof = new ShortByReference();
    checkError(Ace32Native.AdsAtBOF(hTable, pbBof));
    return pbBof.getValue() == Ace32.ADS_TRUE;
  }
  public static boolean AdsAtEOF( NativeLong hTable){
    ShortByReference pbEof = new ShortByReference();
    checkError(Ace32Native.AdsAtEOF(hTable, pbEof));
    return pbEof.getValue() == Ace32.ADS_TRUE;
  }


}
