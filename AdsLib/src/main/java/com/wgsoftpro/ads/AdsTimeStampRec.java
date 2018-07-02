package com.wgsoftpro.ads;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class AdsTimeStampRec  extends Structure {
  public static class ByReference extends AdsTimeStampRec implements Structure.ByReference {}
  public long lDate;
  public long lTime;

  public AdsTimeStampRec() {
  }

  public AdsTimeStampRec(Pointer p) {
    super(p);
    read();
  }

  @Override
  protected List<String> getFieldOrder() {
    return Arrays.asList("lDate", "lTime");
  }

  public long getlDate() {
    return lDate;
  }

  public void setlDate(long lDate) {
    this.lDate = lDate;
  }

  public long getlTime() {
    return lTime;
  }

  public void setlTime(long lTime) {
    this.lTime = lTime;
  }
}
