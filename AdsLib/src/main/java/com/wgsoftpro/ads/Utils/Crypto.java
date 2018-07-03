package com.wgsoftpro.ads.Utils;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import java.io.UnsupportedEncodingException;

public class Crypto {

  /*FUNCTION SpCrypt(cString,cKeyString,cParoll)
  LOCAL  cEncrypt:='', i, cKeySft
  IF cParoll=NIL
  cParoll:='SoftPro'
  ENDIF
  IF LEN(cKeyString:=ALLTRIM(cKeyString)) < LEN(cString)
  cKeyString := REPLICATE(cKeyString,ROUND(LEN(cString)/LEN(cKeyString)+0.5,0))
  ENDIF
  cKeySft := REPLICATE(cParoll,ROUND(LEN(cString)/7+0.5,0))
  FOR i:=1 TO LEN(cString)
  cEncrypt := cEncrypt + CHR(ASC(SUBSTR(cKeyString,i,1))+ASC(SUBSTR(cKeySft,i,1))-ASC(SUBSTR(cString,i,1)))
  NEXT
  RETURN cEnCrypt*/

  public static String spCrypt(String cString, String cKeyString, String cParoll) throws UnsupportedEncodingException {
    StringBuilder sb = new StringBuilder();
    if (cParoll == null) {
      cParoll = "SoftPro";
    }
    cKeyString = cKeyString.trim();
    if (cKeyString.length() < cString.length()) {
      cKeyString = StringUtils.replicate(cKeyString, Math.round((float) cString.length() / (float) cKeyString.length() + 0.5));
    }
    String cKeyShift = StringUtils.replicate(cParoll, Math.round(cString.length() / 7.0 + 0.5));
    byte[] keyBytes = cKeyString.getBytes("cp1251");
    byte[] shiftBytes = cKeyShift.getBytes("cp1251");
    byte[] strBytes = cString.getBytes("cp1251");
    byte[] retval = new byte[cString.length()];
    for (int i = 0; i < cString.length(); ++i) {
      retval[i] = (byte) (keyBytes[i] + (i < shiftBytes.length ? shiftBytes[i] : 0) - strBytes[i]); //sb.append(new String(cKeyString.getBytes("cp1251")[i]+cKeyShift.getBytes("cp1251")[i]-cString.getBytes("cp1251")[i],"cp1251"));
    }
    return new String(retval, "cp1251");
  }
}
