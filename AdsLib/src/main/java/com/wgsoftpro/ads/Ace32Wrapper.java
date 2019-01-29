package com.wgsoftpro.ads;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


//@Slf4j
public class Ace32Wrapper {
  private static final String defaultAnsiCharSet = "cp1251";
  private static String f_simpleDateTimeFormat = "dd.MM.yyyy hh:mm:ss";
  private static String f_simpleDateFormat = "dd.MM.yyyy";

  public static SimpleDateFormat getSimpleDateTimeFormat() {
    return new SimpleDateFormat(f_simpleDateTimeFormat);
  }

  public static SimpleDateFormat getSimpleDateFormat() {
    return new SimpleDateFormat(f_simpleDateFormat);
  }
  /*public static void setSimpleDateTimeFormat(SimpleDateFormat format) {
    simpleDateTimeFormat = format;
  }*/


  /*public static void setSimpleDateFormat(SimpleDateFormat format) {
    simpleDateFormat = format;
  }*/

  public AdsError AdsGetLastError() {
    AdsError retval = new AdsError();
    byte[] acValue = new byte[Ace32.ADS_MAX_ERROR_LEN];
    ShortByReference usLen = new ShortByReference(Ace32.ADS_MAX_ERROR_LEN);
    IntByReference errorCode = new IntByReference(0);
    Ace32Native.AdsGetLastError(errorCode, acValue, usLen);
    retval.setCode(errorCode.getValue());
    retval.setMessage(com.sun.jna.Native.toString(acValue, defaultAnsiCharSet).substring(0, usLen.getValue()));
    return retval;
  }

  public String AdsGetVersion() {
    byte[] acVersion = new byte[Ace32.MAX_DATA_LEN];
    ShortByReference usLen = new ShortByReference(Ace32.MAX_DATA_LEN);
    IntByReference ulMajor = new IntByReference();
    IntByReference ulMinor = new IntByReference();
    ByteByReference ucLetter = new ByteByReference();
    checkError(Ace32Native.AdsGetVersion(ulMajor, ulMinor, ucLetter, acVersion, usLen));
    return Native.toString(acVersion, defaultAnsiCharSet).substring(0, usLen.getValue());
  }

  public void checkError(int errCode) {
    if (errCode != AdsError.AE_SUCCESS) {
      AdsError adsError = AdsGetLastError();
      throw new Ace32Exception(adsError.getMessage(), adsError.getCode());
    }
  }

  public NativeLong AdsConnect60(String serverPath, short serverTypes, String userName, String password, int options) {
    NativeLongByReference handle = new NativeLongByReference(new NativeLong(0));
    checkError(Ace32Native.AdsConnect60(getAnsiBytes(serverPath), serverTypes,
        getAnsiBytes(userName),
        getAnsiBytes(password),
        options, handle));
    return handle.getValue();
  }

  public String getConnectString(String serverPath, String serverTypes, String userName, String password, String  options) {
    StringBuilder sb = new StringBuilder("");

    return sb.toString();
  }

  public NativeLong AdsConnect101(String connectionString) {
    NativeLongByReference handle = new NativeLongByReference(new NativeLong(0));
    NativeLongByReference _nil = new NativeLongByReference(new NativeLong(0));
    _nil.setPointer(Pointer.NULL);
    checkError(Ace32Native.AdsConnect101(getAnsiBytes(connectionString),
            Pointer.NULL,
            handle));
    return handle.getValue();
  }


  public void AdsSetSQLTimeout(NativeLong hObj, NativeLong sqlTimeout) {
    checkError(Ace32Native.AdsSetSQLTimeout(hObj, sqlTimeout));
  }

  public byte[] getAnsiBytes(String s) {
    return Native.toByteArray(s, defaultAnsiCharSet);
  }

  public void AdsDisconnect(NativeLong hObj) {
    checkError(Ace32Native.AdsDisconnect(hObj));
  }

  public String AdsGetConnectionPath(NativeLong hObj) {
    byte[] path = new byte[Ace32.ADS_MAX_PATH];
    short us = Ace32.ADS_MAX_PATH - 1;
    ShortByReference usLen = new ShortByReference(us);
    checkError(Ace32Native.AdsGetConnectionPath(hObj, path, usLen));
    return Native.toString(path, defaultAnsiCharSet).substring(0, usLen.getValue());
  }

  public short AdsGetConnectionType(NativeLong hObj) {
    ShortByReference conType = new ShortByReference();
    checkError(Ace32Native.AdsGetConnectionType(hObj, conType));
    return conType.getValue();
  }

  public void AdsCacheOpenTables(short count) {
    checkError(Ace32Native.AdsCacheOpenTables(count));
  }

  public void AdsCacheOpenCursors(short count) {
    checkError(Ace32Native.AdsCacheOpenCursors(count));
  }

  public void AdsCloseCachedTables(NativeLong hConnection) {
    checkError(Ace32Native.AdsCloseCachedTables(hConnection));
  }

  public NativeLong AdsCreateSQLStatement(NativeLong hConnect) {
    NativeLongByReference hStm = new NativeLongByReference();
    checkError(Ace32Native.AdsCreateSQLStatement(hConnect, hStm));
    return hStm.getValue();
  }

  public void AdsStmtSetTableLockType(NativeLong hStatement, short usLockType) {
    checkError(Ace32Native.AdsStmtSetTableLockType(hStatement, usLockType));
  }

  public void AdsStmtSetTableCharType(NativeLong hStatement, short usCharType) {
    checkError(Ace32Native.AdsStmtSetTableCharType(hStatement, usCharType));
  }

  public void AdsStmtSetTableType(NativeLong hStatement, short usTableType) {
    checkError(Ace32Native.AdsStmtSetTableType(hStatement, usTableType));
  }

  public void AdsStmtSetTablePassword(NativeLong hStatement, String tableName, String password) {
    byte[] pucTableName = getAnsiBytes(tableName);
    byte[] pucPassword = getAnsiBytes(password);
    checkError(Ace32Native.AdsStmtSetTablePassword(hStatement, pucTableName, pucPassword));
  }

  public void AdsPrepareSQL(NativeLong hStatement, String cSQL) {
    byte[] pucSQL = getAnsiBytes(cSQL);
    checkError(Ace32Native.AdsPrepareSQL(hStatement, pucSQL));
  }


  public void AdsPrepareSQLW(NativeLong hStatement, String cSQL) {
    checkError(Ace32Native.AdsPrepareSQLW(hStatement, new WString(cSQL)));
  }

  public NativeLong AdsCachePrepareSQL(NativeLong hConnect, String cSQL, NativeLong hStatement) {
    byte[] pucSQL = getAnsiBytes(cSQL);
    NativeLongByReference phStatement = new NativeLongByReference(hStatement);
    checkError(Ace32Native.AdsCachePrepareSQL(hConnect, pucSQL, phStatement));
    return phStatement.getValue();
  }

  public NativeLong AdsCachePrepareSQLW(NativeLong hConnect, String cSQL, NativeLong hStatement) {
    NativeLongByReference phStatement = new NativeLongByReference(hStatement);
    checkError(Ace32Native.AdsCachePrepareSQLW(hConnect, new WString(cSQL), phStatement));
    return phStatement.getValue();
  }

  public NativeLong AdsExecuteSQL(NativeLong hStatement) {
    NativeLongByReference phCursor = new NativeLongByReference();
    checkError(Ace32Native.AdsExecuteSQL(hStatement, phCursor));
    return phCursor.getValue();
  }

  public NativeLong AdsExecuteSQLDirect(NativeLong hStatement, String cSQL) {
    NativeLongByReference phCursor = new NativeLongByReference();
    byte[] pucSQL = getAnsiBytes(cSQL);
    checkError(Ace32Native.AdsExecuteSQLDirect(hStatement, pucSQL, phCursor));
    return phCursor.getValue();
  }

  public NativeLong AdsExecuteSQLDirectW(NativeLong hStatement, String cSQL) {
    NativeLongByReference phCursor = new NativeLongByReference();
    checkError(Ace32Native.AdsExecuteSQLDirectW(hStatement, new WString(cSQL), phCursor));
    return phCursor.getValue();
  }

  public void AdsCloseSQLStatement(NativeLong hStatement) {
    checkError(Ace32Native.AdsCloseSQLStatement(hStatement));
  }

  public void AdsStmtSetTableRights(NativeLong hStatement, short usCheckRights) {
    checkError(Ace32Native.AdsStmtSetTableRights(hStatement, usCheckRights));
  }

  public void AdsStmtSetTableReadOnly(NativeLong hStatement, short usReadOnly) {
    checkError(Ace32Native.AdsStmtSetTableReadOnly(hStatement, usReadOnly));
  }

  public void AdsCloseTable(NativeLong hTable) {
    checkError(Ace32Native.AdsCloseTable(hTable));
  }

  public void AdsSetString(NativeLong hObj, String fldName, String value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    byte[] pucBuf = getAnsiBytes(value);
    NativeLong uLen = new NativeLong(pucBuf.length);
    checkError(Ace32Native.AdsSetString(hObj, pucFldName, pucBuf, uLen));
  }

  public void AdsSetTime(NativeLong hObj, String fldName, String value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    byte[] pucBuf = getAnsiBytes(value);
    NativeLong uLen = new NativeLong(pucBuf.length);
    checkError(Ace32Native.AdsSetTime(hObj, pucFldName, pucBuf, uLen));
  }

  public void AdsSetDate(NativeLong hObj, String fldName, String value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    byte[] pucBuf = getAnsiBytes(value);
    short uLen = (short) pucBuf.length;
    checkError(Ace32Native.AdsSetDate(hObj, pucFldName, pucBuf, uLen));
  }

  public void AdsSetDateFormat60(NativeLong hConnect, String format) {
    byte[] pucFormat = getAnsiBytes(format);
    checkError(Ace32Native.AdsSetDateFormat60(hConnect, pucFormat));
  }

  public void AdsSetDecimal(short usDecimals) {
    checkError(Ace32Native.AdsSetDecimals(usDecimals));
  }

  public void AdsSetDefault(String defValue) {
    byte[] pucDefault = getAnsiBytes(defValue);
    checkError(Ace32Native.AdsSetDefault(pucDefault));
  }

  public void AdsShowDeleted(short bShowDeleted) {
    checkError(Ace32Native.AdsShowDeleted(bShowDeleted));
  }

  public void AdsSetDouble(NativeLong hObj, String fldName, double value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsSetDouble(hObj, pucFldName, value));
  }

  public void AdsSetEmpty(NativeLong hObj, String fldName) {
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsSetEmpty(hObj, pucFldName));
  }

  public void AdsSetField(NativeLong hObj, String fldName, Pointer p, NativeLong uLen) {
    byte[] pucFldName = getAnsiBytes(fldName);
    //byte[] pucBuf = getAnsiBytes(value);
    //int uLen = pucBuf.length;
    // StringByReference _val = new IntByReference(0);
    checkError(Ace32Native.AdsSetField(hObj, pucFldName, p, uLen));
  }

  public void AdsSetHandleLong(NativeLong hObj, long uVal) {
    checkError(Ace32Native.AdsSetHandleLong(hObj, new NativeLong(uVal)));
  }

  public void AdsSetLogical(NativeLong hObj, String fldName, boolean value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    short uVal = (short) (value ? Ace32.ADS_TRUE : Ace32.ADS_FALSE);
    checkError(Ace32Native.AdsSetLogical(hObj, pucFldName, uVal));
  }

  public void AdsSetLong(NativeLong hObj, String fldName, long value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsSetLong(hObj, pucFldName, new NativeLong(value)));
  }

  public void AdsSetLongLong(NativeLong hObj, String fldName, Long value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsSetLongLong(hObj, pucFldName, value));
  }

  //to-do it's need check mashtab
  public void AdsSetMilliseconds(NativeLong hObj, String fldName, Time value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsSetMilliseconds(hObj, pucFldName, (int) value.getTime()));
  }

  public void AdsSetMoney(NativeLong hObj, String fldName, Long value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsSetMoney(hObj, pucFldName, value));
  }

  public void AdsSetTimeStampRaw(NativeLong hObj, String fldName, Timestamp value) {
    byte[] pucFldName = getAnsiBytes(fldName);
    byte[] pucBuf = getSimpleDateTimeFormat().format(value).getBytes();
    checkError(Ace32Native.AdsSetTimeStampRaw(hObj, pucFldName, pucBuf, new NativeLong(pucBuf.length)));
  }

  public void AdsSetBinary(NativeLong hTable, String fldName, short usBinaryType, NativeLong ulTotalLength
      , NativeLong ulOffset, byte[] pucBuf) {
    checkError(Ace32Native.AdsSetBinary(hTable, getAnsiBytes(fldName), usBinaryType, ulTotalLength, ulOffset, pucBuf, new NativeLong(pucBuf.length)));
  }

  public String AdsGetString(NativeLong hTable, String fldName, long size) {
    byte[] pucFldName = getAnsiBytes(fldName);
    byte[] pucBuf = new byte[(int) size + 1];
    NativeLongByReference realSize = new NativeLongByReference(new NativeLong(size + 1));
    checkError(Ace32Native.AdsGetString(hTable, pucFldName, pucBuf, realSize, Ace32.ADS_TRIM));
    return Native.toString(pucBuf, defaultAnsiCharSet).substring(0, realSize.getValue().intValue());
  }

  public String AdsGetStringW(NativeLong hObj, String fldName, long size) {
    byte[] pucFldName = getAnsiBytes(fldName);
    NativeLongByReference realSize = new NativeLongByReference();
    WString retval = new WString(new String(new byte[(short) size + 1]));
    checkError(Ace32Native.AdsGetStringW(hObj, pucFldName, retval, realSize, Ace32.ADS_TRIM));
    return retval.toString().substring(0, (short) size - 1);
  }

  public Boolean AdsGetLogical(NativeLong hTable, String fldName) {
    ShortByReference retval = new ShortByReference(Ace32.ADS_FALSE);
    checkError(Ace32Native.AdsGetLogical(hTable, getAnsiBytes(fldName), retval));
    return new Boolean(retval.getValue() == Ace32.ADS_TRUE);
  }

  public long AdsGetLong(NativeLong hTable, String fldName) {
    NativeLongByReference retval = new NativeLongByReference();
    checkError(Ace32Native.AdsGetLong(hTable, getAnsiBytes(fldName), retval));
    return new Long(retval.getValue().longValue());
  }

  public long AdsGetLongLong(NativeLong hTable, String fldName) {
    LongByReference retval = new LongByReference();
    checkError(Ace32Native.AdsGetLongLong(hTable, getAnsiBytes(fldName), retval));
    return new Long(retval.getValue());
  }

  public Short AdsGetMemoBlockSize(NativeLong hTable) {
    ShortByReference retval = new ShortByReference();
    checkError(Ace32Native.AdsGetMemoBlockSize(hTable, retval));
    return new Short(retval.getValue());
  }

  public Long AdsGetMemoLength(NativeLong hTable, String fldName) {
    NativeLongByReference retval = new NativeLongByReference();
    checkError(Ace32Native.AdsGetMemoLength(hTable, getAnsiBytes(fldName), retval));
    return new Long(retval.getValue().longValue());
  }

  public Short AdsGetMemoDataType(NativeLong hTable, String fldName) {
    ShortByReference retval = new ShortByReference(Ace32.ADS_FALSE);
    checkError(Ace32Native.AdsGetMemoDataType(hTable, getAnsiBytes(fldName), retval));
    return new Short(retval.getValue());
  }

  public Integer AdsGetMilliseconds(NativeLong hTable, String fldName) {
    IntByReference retval = new IntByReference();
    checkError(Ace32Native.AdsGetMilliseconds(hTable, getAnsiBytes(fldName), retval));
    return new Integer(retval.getValue());
  }

  public Long AdsGetMoney(NativeLong hTable, String fldName) {
    LongByReference retval = new LongByReference();
    checkError(Ace32Native.AdsGetMoney(hTable, getAnsiBytes(fldName), retval));
    return new Long(retval.getValue());
  }

  public Double AdsGetDouble(NativeLong hTable, String fldName) {
    DoubleByReference retval = new DoubleByReference();
    checkError(Ace32Native.AdsGetDouble(hTable, getAnsiBytes(fldName), retval));
    return new Double(retval.getValue());
  }

  public Long AdsGetField(NativeLong hTable, String fldName, Pointer p) {
    NativeLongByReference retval = new NativeLongByReference();
    checkError(Ace32Native.AdsGetField(hTable, getAnsiBytes(fldName), p, retval, Ace32.ADS_NONE));
    return new Long(retval.getValue().longValue());
  }


  public byte[] AdsGetBinary(NativeLong hTable, String fldName, int size) {
    NativeLongByReference retSize = new NativeLongByReference();
    byte[] retval = new byte[size];
    checkError(Ace32Native.AdsGetBinary(hTable, getAnsiBytes(fldName), new NativeLong(size), retval, retSize));
    return retval;
  }

  public Long AdsGetBinaryLength(NativeLong hTable, String fldName) {
    NativeLongByReference retval = new NativeLongByReference();
    checkError(Ace32Native.AdsGetBinaryLength(hTable, getAnsiBytes(fldName), retval));
    return new Long(retval.getValue().longValue());
  }

  public Short AdsGetFieldDecimals(NativeLong hTable, String fldName) {
    ShortByReference retval = new ShortByReference();
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsGetFieldDecimals(hTable, pucFldName, retval));
    return new Short(retval.getValue());
  }

  public Long AdsGetFieldLength(NativeLong hTable, String fldName) {
    NativeLongByReference retval = new NativeLongByReference();
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsGetFieldLength(hTable, pucFldName, retval));
    return new Long(retval.getValue().longValue());
  }

  public String AdsGetFieldName(NativeLong hTable, short fldNumber) {
    ShortByReference retval = new ShortByReference((short) 255);
    byte[] pucFldName = new byte[255];
    checkError(Ace32Native.AdsGetFieldName(hTable, fldNumber, pucFldName, retval));
    return Native.toString(pucFldName, defaultAnsiCharSet).substring(0, retval.getValue());
  }


  public Short AdsGetFieldNum(NativeLong hTable, String fldName) {
    ShortByReference retval = new ShortByReference();
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsGetFieldNum(hTable, pucFldName, retval));
    return new Short(retval.getValue());
  }

  public Long AdsGetFieldOffset(NativeLong hTable, String fldName) {
    NativeLongByReference retval = new NativeLongByReference();
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsGetFieldOffset(hTable, pucFldName, retval));
    return new Long(retval.getValue().longValue());
  }

  public Short AdsGetFieldType(NativeLong hTable, String fldName) {
    ShortByReference retval = new ShortByReference();
    byte[] pucFldName = getAnsiBytes(fldName);
    checkError(Ace32Native.AdsGetFieldType(hTable, pucFldName, retval));
    return new Short(retval.getValue());
  }

  public Short AdsGetNumFields(NativeLong hTable) {
    ShortByReference retval = new ShortByReference();
    checkError(Ace32Native.AdsGetNumFields(hTable, retval));
    return new Short(retval.getValue());
  }

  public Timestamp AdsGetDate(NativeLong hTable, String fldName) {
    byte[] pucFldName = getAnsiBytes(fldName);
    byte[] pucBuf = new byte[11];
    ShortByReference retsize = new ShortByReference((short) 11);
    checkError(Ace32Native.AdsGetDate(hTable, pucFldName, pucBuf, retsize));
    String _d = Native.toString(pucBuf, defaultAnsiCharSet).substring(0, retsize.getValue());
    try {
      return new Timestamp(getSimpleDateFormat().parse(_d).getTime());
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public void AdsGotoBottom(NativeLong hObj) {
    checkError(Ace32Native.AdsGotoBottom(hObj));
  }

  public void AdsGotoRecord(NativeLong hTable, long nRec) {
    checkError(Ace32Native.AdsGotoRecord(hTable, new NativeLong(nRec)));
  }

  public void AdsGotoTop(NativeLong hObj) {
    checkError(Ace32Native.AdsGotoTop(hObj));
  }

  public void AdsSkip(NativeLong hObj, int nRec) {
    checkError(Ace32Native.AdsSkip(hObj, nRec));
  }

  public void AdsSkipUnique(NativeLong hIndex, int nRec) {
    checkError(Ace32Native.AdsSkipUnique(hIndex, nRec));
  }

  public boolean AdsAtBOF( NativeLong hTable){
    ShortByReference pbBof = new ShortByReference();
    checkError(Ace32Native.AdsAtBOF(hTable, pbBof));
    return pbBof.getValue() == Ace32.ADS_TRUE;
  }
  public boolean AdsAtEOF( NativeLong hTable){
    ShortByReference pbEof = new ShortByReference();
    checkError(Ace32Native.AdsAtEOF(hTable, pbEof));
    return pbEof.getValue() == Ace32.ADS_TRUE;
  }


}
