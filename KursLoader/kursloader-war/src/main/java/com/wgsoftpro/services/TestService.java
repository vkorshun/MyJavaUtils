package com.wgsoftpro.services;

import com.wgsoftpro.beans.KursLoad.KursLoaderMB;
import com.wgsoftpro.beans.KursLoad.KursLoaderUKR;
import com.wgsoftpro.beans.KursLoad.WsdlKursLoaderCbRu;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class TestService {

  public String test() {
    Timestamp curDate = new Timestamp(System.currentTimeMillis());
    KursLoaderUKR kl = new KursLoaderUKR(curDate, curDate);
    //WsdlKursLoaderCbRu kl = new WsdlKursLoaderCbRu(curDate, curDate, false);
    //kl.checkKurs();
    KursLoaderMB k2 = new KursLoaderMB(curDate, curDate, true);
    k2.checkKurs();
    return "{\"Ok\":\"OK\"}";
  }

  public String loadNBU(String d1, String d2)  {
    try {

      SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
      Timestamp ts1 = new Timestamp(df.parse(d1).getTime());
      Timestamp ts2 = new Timestamp(df.parse(d2).getTime());
      KursLoaderUKR kl = new KursLoaderUKR(ts1, ts2);
      kl.checkKurs();
      return "{\"Ok\":\"OK\"}";
    } catch (Exception ex) {
      return String.format("{\"ERROR\":\"%s\"}", ex.getMessage());

    }
  }
}
