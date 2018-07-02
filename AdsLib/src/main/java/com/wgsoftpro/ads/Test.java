package com.wgsoftpro.ads;

import com.wgsoftpro.ads.AdsClasses.AdsConnection;
import com.wgsoftpro.ads.AdsClasses.AdsQuery;

import java.io.IOException;
import java.net.URISyntaxException;

public class Test {
  public static void main(String[] args) throws IOException, URISyntaxException {
    System.out.println(System.getProperty("java.io.tmpdir"));
    System.out.println(System.getProperty("os.name"));

    Ace32 ace32 = new Ace32();
    //ace32.prepareLib();
    //Ace32Native.init();
    try {
      //AceWrapper wrapper = new AceWrapper();
      System.out.println(Ace32Wrapper.AdsGetVersion());
      try (AdsConnection adsConn = new AdsConnection("D:/VKFIN/universal-7.add","adssys","air") ) {
        adsConn.connect();
        if (adsConn.isConnected()) {
          System.out.println("connect" + adsConn.AdsGetConnectionPath());
          AdsQuery _query = new AdsQuery(adsConn);
          _query.setQuery("SELECT * FROM client WHERE kodkli>=:kodkli");
          _query.setParam("kodkli",0);
          _query.execute();
          while (!_query.isEof()) {
            System.out.println(_query.getFieldValue("name").asString()+_query.getFieldValue("kodkli").asLong().toString());
            _query.next();
          }
//          System.out.println(_query.getFieldValue("name").asString()+_query.getFieldValue("kodkli").asLong().toString());
          _query.close();
          adsConn.disconnect();
          System.out.println("disconnect");
        } else {
          System.out.println("no connection");
        }
      }
    } finally {
      Ace32Native.destroy();
    }
  }
}
