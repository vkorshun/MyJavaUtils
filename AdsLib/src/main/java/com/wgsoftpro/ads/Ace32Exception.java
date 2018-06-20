package com.wgsoftpro.ads;

public class Ace32Exception extends RuntimeException {
  public static final int CKR_OK = 0;

  private int errorCode;

  public static final String errorToString(int var0) {
    return String.format(" error code %d", var0);
  }

  public Ace32Exception(int var1) {
    super(String.format("0x%08x: %s", new Object[]{Integer.valueOf(var1), errorToString(var1)}));
    this.errorCode = var1;
  }

  public Ace32Exception(String var1, int var2) {
    super(String.format("0x%08x: %s : %s", new Object[]{Integer.valueOf(var2), errorToString(var2), var1}));
    this.errorCode = var2;
  }

  public int getErrorCode() {
    return this.errorCode;
  }

}
