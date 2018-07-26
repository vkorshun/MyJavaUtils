package com.wgsoftpro.ads;

import com.wgsoftpro.ads.AdsClasses.AdsConnection;
import com.wgsoftpro.ads.AdsClasses.AdsQuery;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.sql.Timestamp;

public class Test {
  public static void main(String[] args) throws IOException, URISyntaxException {
/*    System.out.println(System.getProperty("java.io.tmpdir"));
    System.out.println(System.getProperty("os.name"));
    testMix();

    Ace32 ace32 = new Ace32();
    //ace32.prepareLib();
    //Ace32Native.init();
    try {
      //AceWrapper wrapper = new AceWrapper();
      String cPassword = "";
      System.out.println(Ace32Wrapper.AdsGetVersion());
      try (AdsConnection adsConn = new AdsConnection("D:/VKFIN/COMMON/INSTALL","adssys","air") ) {
        adsConn.connect();
        if (adsConn.isConnected()) {
          System.out.println("connect" + adsConn.AdsGetConnectionPath());
          AdsQuery _query = new AdsQuery(adsConn);
          _query.setQuery("SELECT * FROM \"install.db7\"");
          //_query.setParam("kodkli",0);
          _query.execute();
          while (!_query.isEof()) {
            String sn =_query.getFieldValue("serialnumber").asString();
            Timestamp _d = _query.getFieldValue("date").asTimestamp();
            cPassword = StringUtils.left(sn,3)+StringUtils.charMix(sn, DateUtils.dtos(_d));
            System.out.println(cPassword);
            cPassword = StringUtils.spCrypt(cPassword,"62133645487231331357",null);
            System.out.println(cPassword);
            _query.next();
          }
//          System.out.println(_query.getFieldValue("name").asString()+_query.getFieldValue("kodkli").asLong().toString());
          _query.close();
          _query.setPassword("softpro.db7",cPassword);
          _query.setQuery("SELECT * FROM \"softpro.db7\"");
          _query.execute();
          _query.goBottom();
          System.out.println(_query.getFieldValue("weblicense").asString());
          _query.close();
          adsConn.disconnect();
          System.out.println("disconnect");
        } else {
          System.out.println("no connection");
        }
      }
    } finally {
      Ace32Native.destroy();
    }*/
  }

  public static void testMix() throws UnsupportedEncodingException {
    //System.out.println(Crypto.spCrypt("123456","123","Вася"));
    //System.out.println(StringUtils.charMix("123","4567"));
  }


}
