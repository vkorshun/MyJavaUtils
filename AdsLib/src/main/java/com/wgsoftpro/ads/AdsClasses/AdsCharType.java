package com.wgsoftpro.ads.AdsClasses;

import com.wgsoftpro.ads.Ace32;

public enum AdsCharType {
  ADS_ANSI(Ace32.ADS_ANSI),
  ADS_OEM(Ace32.ADS_OEM);

  private short code;

  AdsCharType(short code) {
    this.code = code;
  }

  public short getCode() {
    return code;
  }
}
