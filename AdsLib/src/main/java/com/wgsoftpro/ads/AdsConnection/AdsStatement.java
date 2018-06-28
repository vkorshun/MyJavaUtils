package com.wgsoftpro.ads.AdsConnection;

import com.sun.jna.ptr.IntByReference;
import com.wgsoftpro.ads.Ace32Wrapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter()
@Setter
public class AdsStatement {
  @Setter(AccessLevel.NONE)
  protected AdsConnection adsConnection;
  protected AdsCharType adsCharType;
  protected AdsTableType adsTableType;
  protected AdsLockType adsLockType;
  @Setter(AccessLevel.NONE)
  protected Integer hStatement;

  public AdsStatement(AdsConnection adsConnection) {
    this.adsConnection = adsConnection;
    adsCharType = AdsCharType.ADS_ANSI;
    adsTableType = AdsTableType.ADS_ADT;
    adsLockType = AdsLockType.ADS_PROPRIETARY_LOCKING;
    //createStatement();
    hStatement = null;
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

}
