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


  public AdsStatement(AdsConnection adsConnection) {
    this.adsConnection = adsConnection;
    adsCharType = AdsCharType.ADS_ANSI;
    adsTableType = AdsTableType.ADS_ADT;
    adsLockType = AdsLockType.ADS_PROPRIETARY_LOCKING;
    //createStatement();
    hStatement = null;
    hCursor = null;
    adsFieldsInfo = null;
  }

  protected void createStatement() {
    hStatement = Ace32Wrapper.AdsCreateSQLStatement(adsConnection.getHandle());
    Ace32Wrapper.AdsStmtSetTableType(hStatement, adsTableType.getCode());
    Ace32Wrapper.AdsStmtSetTableCharType(hStatement, adsCharType.getCode());
    Ace32Wrapper.AdsStmtSetTableLockType(hStatement, adsLockType.getCode());
  }

  public void setPassword(String tableName, String password) {
    if (hStatement != null) {
      Ace32Wrapper.AdsStmtSetTablePassword(hStatement, tableName, password);
    }
  }

  protected void closeStatement() {
    try {
      if (hStatement != null) {
        Ace32Wrapper.AdsCloseSQLStatement(hStatement);
      }
    } catch (Exception ex) {
//        log.error(ex.getMessage());
    }
  }

  protected void closeCursor() {
    try {
      if (hCursor != null) {
        Ace32Wrapper.AdsCloseTable(hCursor);
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
      Ace32Wrapper.AdsSetEmpty(hStatement, name);
    } else if (value instanceof Time) {
      Ace32Wrapper.AdsSetTime(hStatement, name, value.toString());

    } else if (value instanceof Date) {
      Ace32Wrapper.AdsSetDate(hStatement, name, value.toString());

    } else if (value instanceof Boolean) {
      Ace32Wrapper.AdsSetLogical(hStatement, name, (Boolean) value);

    } else if (value instanceof byte[]) {
      byte[] _arr = (byte[]) value;
      Ace32Wrapper.AdsSetBinary(hStatement, name, Ace32.ADS_BINARY, new NativeLong(_arr.length), new NativeLong(0), _arr);
    } else {
      Ace32Wrapper.AdsSetString(hStatement, name, value.toString());
    }
  }

  public AdsFieldValue getFieldValue(String fieldName) {
    Object retval = null;
    if (isEmpty()) {
      return null;
    }
    AdsFieldDescription adsFieldDescription = adsFieldsInfo.getAdsFieldDescription(fieldName);
    if (adsFieldDescription.getType() == Ace32.ADS_NUMERIC) {
      if (adsFieldDescription.getLength() > 9 && adsFieldDescription.getDecimals() > 0) {
        retval = Ace32Wrapper.AdsGetDouble(hCursor, fieldName);
      } else {
        retval = Ace32Wrapper.AdsGetLongLong(hCursor, fieldName);
      }
    } else {
      switch (adsFieldDescription.getType()) {
        case Ace32.ADS_DOUBLE:
        case Ace32.ADS_CURDOUBLE: {
          retval = Ace32Wrapper.AdsGetDouble(hCursor, fieldName);
          break;
        }
        case Ace32.ADS_INTEGER:
        case Ace32.ADS_SHORTINT:
        case Ace32.ADS_ROWVERSION:
        case Ace32.ADS_LONGLONG:
        case Ace32.ADS_AUTOINC: {
          retval = Ace32Wrapper.AdsGetLongLong(hCursor, fieldName);
          break;
        }
        case Ace32.ADS_MONEY: {
          retval = Ace32Wrapper.AdsGetMoney(hCursor, fieldName);
          break;
        }
        case Ace32.ADS_MEMO: {
          adsFieldDescription.setLength(Ace32Wrapper.AdsGetMemoLength(hCursor, fieldName));
          retval = Ace32Wrapper.AdsGetString(hCursor, fieldName, adsFieldDescription.getLength());
          break;
        }
        case Ace32.ADS_NMEMO: {
          adsFieldDescription.setLength(Ace32Wrapper.AdsGetMemoLength(hCursor, fieldName));
          retval = Ace32Wrapper.AdsGetStringW(hCursor, fieldName, adsFieldDescription.getLength());
          break;
        }
        case Ace32.ADS_NCHAR:
        case Ace32.ADS_NVARCHAR: {
          retval = Ace32Wrapper.AdsGetStringW(hCursor, fieldName, adsFieldDescription.getLength());
          break;
        }
        case Ace32.ADS_STRING:
        case Ace32.ADS_VARCHAR_FOX:
        case Ace32.ADS_CISTRING: {
          retval = Ace32Wrapper.AdsGetString(hCursor, fieldName, adsFieldDescription.getLength());
          break;
        }
        case Ace32.ADS_DATE: {
          retval = Ace32Wrapper.AdsGetDate(hCursor, fieldName);
        }
        case Ace32.ADS_LOGICAL: {
          retval = Ace32Wrapper.AdsGetLogical(hCursor, fieldName);
          break;
        }
        case Ace32.ADS_TIME:
        case Ace32.ADS_MODTIME:
        case Ace32.ADS_TIMESTAMP: {
          retval = new Timestamp(Ace32Wrapper.AdsGetMilliseconds(hCursor, fieldName));
          break;
        }
        case Ace32.ADS_BINARY:
        case Ace32.ADS_VARBINARY_FOX:
        case Ace32.ADS_IMAGE: {
          retval = adsFieldDescription.getLength() > 0 ? "BLOB" : "blob";
        }
      }
    }
    return new AdsFieldValue(retval);
  }

  public boolean isEof(){
    return Ace32Wrapper.AdsAtEOF(hCursor);
  }

  public boolean isBof(){
    return Ace32Wrapper.AdsAtBOF(hCursor);
  }

  public boolean isEmpty(){
    return isBof() && isEof();
  }

  public void goTop(){
    Ace32Wrapper.AdsGotoTop(hCursor);
  }
  public void goBottom(){
    Ace32Wrapper.AdsGotoBottom(hCursor);
  }
  public void next(){
    Ace32Wrapper.AdsSkip(hCursor, 1);
  }
  public void previous(){
    Ace32Wrapper.AdsSkip(hCursor, -1);
  }
}
