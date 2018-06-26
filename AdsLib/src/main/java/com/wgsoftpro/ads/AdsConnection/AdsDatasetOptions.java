package com.wgsoftpro.ads.AdsConnection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdsDatasetOptions {
  private short musAdsLockType; //        : UNSIGNED16; {  Specific table lock type }
  private short musAdsCharType; //        : UNSIGNED16; {  Specific char type }
  private short musAdsRightsCheck;//      : UNSIGNED16; {  rights checking is enabled }
  private short musAdsTableType;//        : UNSIGNED16; { Table type }
  String mstrCollation;//          : String;     { Collation Language }
}
