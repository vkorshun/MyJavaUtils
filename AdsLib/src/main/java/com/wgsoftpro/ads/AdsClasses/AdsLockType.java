package com.wgsoftpro.ads.AdsClasses;

import com.wgsoftpro.ads.Ace32;

public enum AdsLockType {
  ADS_COMPATIBLE_LOCKING(Ace32.ADS_COMPATIBLE_LOCKING),
  ADS_PROPRIETARY_LOCKING(Ace32.ADS_PROPRIETARY_LOCKING);


  private short code;

  AdsLockType(short code) {
    this.code = code;
  }

  public short getCode() {
    return code;
  }
}
