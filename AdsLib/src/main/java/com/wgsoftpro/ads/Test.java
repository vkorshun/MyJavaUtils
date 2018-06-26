package com.wgsoftpro.ads;

import com.wgsoftpro.ads.AdsConnection.AdsConnection;

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
      AdsConnection adsConn = new AdsConnection("D:/VKFIN/COMMON/INSALL","","");
      adsConn.connect();
      if (adsConn.isConnected()) {
        System.out.println("connect");
        adsConn.disconnect();
        System.out.println("disconnect");
      } else {
        System.out.println("no connection");
      }
    } finally {
      Ace32Native.destroy();
    }
  }
}
