package com.wgsoftpro.services;

import com.wgsoftpro.beans.KursLoad.KursLoaderMB;
import com.wgsoftpro.beans.KursLoad.KursLoaderUKR;
import com.wgsoftpro.beans.KursLoad.WsdlKursLoaderCbRu;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

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
}
