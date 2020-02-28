package com.wgsoftpro.ads.AdsClasses;

import com.sun.jna.NativeLong;
import com.wgsoftpro.ads.Ace32;
import com.wgsoftpro.ads.Ace32Wrapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

//@Slf4j
@Getter()
@Setter
public class AdsStatement implements Closeable {
  @Setter(AccessLevel.NONE)
  protected AdsConnection adsConnection;
  protected AdsCharType adsCharType;
  protected AdsTableType adsTableType;
  protected AdsLockType adsLockType;
  @Setter(AccessLevel.NONE)
  protected NativeLong hStatement;
  @Setter(AccessLevel.NONE)
  protected NativeLong hCursor;
  protected AdsFieldsInfo adsFieldsInfo;
  @Setter(AccessLevel.NONE)
  protected Map<String,String> passwords;
  protected Ace32Wrapper ace32Wrapper;


  public AdsStatement(AdsConnection adsConnection) {
    this.adsConnection = adsConnection;
    ace32Wrapper = adsConnection.getAce32Wrapper();
    if (adsConnection.getAdsConnectOptions().getAdsCharSet().equals(AdsConnectOptions.TADSCharSet.ADS_OEM)) {
      adsCharType = AdsCharType.ADS_OEM;
    } else {
      adsCharType = AdsCharType.ADS_ANSI;
    }
    if (adsConnection.getAdsConnectOptions().getAdsTableType().equals(AdsConnectOptions.TADSTableType.ADS_DBF)) {
      adsTableType = AdsTableType.ADS_VFP;
    } else {
      adsTableType = AdsTableType.ADS_ADT;
    }
    adsLockType = AdsLockType.ADS_PROPRIETARY_LOCKING;
    //createStatement();
    hStatement = null;
    hCursor = null;
    adsFieldsInfo = null;
    passwords = new HashMap<>();
  }

  protected void createStatement() {
    hStatement = ace32Wrapper.AdsCreateSQLStatement(adsConnection.getHandle());
    ace32Wrapper.AdsStmtSetTableType(hStatement, adsTableType.getCode());
    ace32Wrapper.AdsStmtSetTableCharType(hStatement, adsCharType.getCode());
    ace32Wrapper.AdsStmtSetTableLockType(hStatement, adsLockType.getCode());
    for (String key: passwords.keySet()) {
      ace32Wrapper.AdsStmtSetTablePassword(hStatement, key, passwords.get(key));
    }

  }

//  public void setPassword(String tableName, String password) {
//    if (hStatement != null) {
//    }
//  }

  protected void closeStatement() {
    try {
      if (hStatement != null) {
        ace32Wrapper.AdsCloseSQLStatement(hStatement);
      }
    } catch (Exception ex) {
//        log.error(ex.getMessage());
    }
  }

  protected void closeCursor() {
    try {
      if (hCursor != null) {
        ace32Wrapper.AdsCloseTable(hCursor);
      }
    } catch (Exception ex) {
//      log.error(ex.getMessage());
    }
  }

  @Override
  public void close() throws IOException {
    closeCursor();
    closeStatement();
  }

/*  protected void setParams() {

  }*/

  protected void setFieldValue(String name, Object value) {
    if (value == null) {
      ace32Wrapper.AdsSetEmpty(hStatement, name);
    } else if (value instanceof Time) {
      ace32Wrapper.AdsSetTime(hStatement, name, value.toString());

    } else if (value instanceof Date) {
      ace32Wrapper.AdsSetDate(hStatement, name, value.toString());

    } else if (value instanceof Boolean) {
      ace32Wrapper.AdsSetLogical(hStatement, name, (Boolean) value);

    } else if (value instanceof byte[]) {
      byte[] _arr = (byte[]) value;
      ace32Wrapper.AdsSetBinary(hStatement, name, Ace32.ADS_BINARY, new NativeLong(_arr.length), new NativeLong(0), _arr);
    } else {
      ace32Wrapper.AdsSetString(hStatement, name, value.toString());
    }
  }

  public AdsFieldValue getFieldValue(String fieldName) {
    Object retval = null;
    if (!isEmpty()) {
      AdsFieldDescription adsFieldDescription = adsFieldsInfo.getAdsFieldDescription(fieldName);
      if (adsFieldDescription.getType() == Ace32.ADS_NUMERIC) {
        if (adsFieldDescription.getLength() > 9 && adsFieldDescription.getDecimals() > 0) {
          retval = ace32Wrapper.AdsGetDouble(hCursor, fieldName);
        } else {
          retval = ace32Wrapper.AdsGetLongLong(hCursor, fieldName);
        }
      } else {
        switch (adsFieldDescription.getType()) {
          case Ace32.ADS_DOUBLE:
          case Ace32.ADS_CURDOUBLE: {
            retval = ace32Wrapper.AdsGetDouble(hCursor, fieldName);
            break;
          }
          case Ace32.ADS_INTEGER:
          case Ace32.ADS_SHORTINT:
          case Ace32.ADS_ROWVERSION:
          case Ace32.ADS_LONGLONG:
          case Ace32.ADS_AUTOINC: {
            retval = ace32Wrapper.AdsGetLongLong(hCursor, fieldName);
            break;
          }
          case Ace32.ADS_MONEY: {
            retval = ace32Wrapper.AdsGetMoney(hCursor, fieldName);
            break;
          }
          case Ace32.ADS_MEMO: {
            adsFieldDescription.setLength(ace32Wrapper.AdsGetMemoLength(hCursor, fieldName));
            retval = ace32Wrapper.AdsGetString(hCursor, fieldName, adsFieldDescription.getLength());
            break;
          }
          case Ace32.ADS_NMEMO: {
            adsFieldDescription.setLength(ace32Wrapper.AdsGetMemoLength(hCursor, fieldName));
            retval = ace32Wrapper.AdsGetStringW(hCursor, fieldName, adsFieldDescription.getLength());
            break;
          }
          case Ace32.ADS_NCHAR:
          case Ace32.ADS_NVARCHAR: {
            retval = ace32Wrapper.AdsGetStringW(hCursor, fieldName, adsFieldDescription.getLength());
            break;
          }
          case Ace32.ADS_STRING:
          case Ace32.ADS_VARCHAR_FOX:
          case Ace32.ADS_CISTRING: {
            retval = ace32Wrapper.AdsGetString(hCursor, fieldName, adsFieldDescription.getLength());
            break;
          }
          case Ace32.ADS_DATE: {
            retval = ace32Wrapper.AdsGetDate(hCursor, fieldName);
            break;
          }
          case Ace32.ADS_LOGICAL: {
            retval = ace32Wrapper.AdsGetLogical(hCursor, fieldName);
            break;
          }
          case Ace32.ADS_TIME:
          case Ace32.ADS_MODTIME:
          case Ace32.ADS_TIMESTAMP: {
            retval = new Timestamp(ace32Wrapper.AdsGetMilliseconds(hCursor, fieldName));
            break;
          }
          case Ace32.ADS_BINARY:
          case Ace32.ADS_VARBINARY_FOX:
          case Ace32.ADS_IMAGE: {
            retval = adsFieldDescription.getLength() > 0 ? "BLOB" : "blob";
          }
        }
      }
    }
    return new AdsFieldValue(retval);
  }

  public boolean isEof(){
    return ace32Wrapper.AdsAtEOF(hCursor);
  }

  public boolean isBof(){
    return ace32Wrapper.AdsAtBOF(hCursor);
  }

  public boolean isEmpty(){
    return isBof() && isEof();
  }

  public void goTop(){
    ace32Wrapper.AdsGotoTop(hCursor);
  }
  public void goBottom(){
    ace32Wrapper.AdsGotoBottom(hCursor);
  }
  public void next(){
    ace32Wrapper.AdsSkip(hCursor, 1);
  }
  public void previous(){
    ace32Wrapper.AdsSkip(hCursor, -1);
  }

  public void setPassword(String tablename, String password){
    passwords.put(tablename, password);
  }
}
