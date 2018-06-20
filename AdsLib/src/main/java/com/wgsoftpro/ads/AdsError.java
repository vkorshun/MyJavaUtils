package com.wgsoftpro.ads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdsError {
  public static final int AE_SUCCESS = 0;

  private int code;
  private String message;
}
