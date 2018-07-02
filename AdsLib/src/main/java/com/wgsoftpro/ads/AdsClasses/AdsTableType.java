package com.wgsoftpro.ads.AdsClasses;

import com.wgsoftpro.ads.Ace32;

public enum AdsTableType {
  ADS_DATABASE_TABLE(Ace32.ADS_DATABASE_TABLE),
  ADS_NTX(Ace32.ADS_NTX),
  ADS_CDX(Ace32.ADS_CDX),
  ADS_ADT(Ace32.ADS_ADT),
  ADS_VFP(Ace32.ADS_VFP);
  private short code;

  AdsTableType(short code) {
    this.code = code;
  }

  public short getCode() {
    return code;
  }
}
