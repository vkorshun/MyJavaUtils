package com.wgsoftpro.beans.KursLoad;

/**
 * Created by vkorshun on 25.11.2015.
 */

import com.wgsoftpro.Utils.MailUtils;
import com.wgsoftpro.Utils.Timestamputils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component("kursLoadBean")
public class KursLoadBean  {
  final static Logger logger = LoggerFactory.getLogger(KursLoadBean.class);
  List<String> errorList = new ArrayList();
  String mailHost;
  String mailPort;
  String mailUser;
  String mailPassword;
  String mailFrom;
  String mailTo;

  public void KursLoaderBean() {
    System.out.println("I am called by Spring scheduler");
  }

  public void connectAds() {
    String hostName;
    String path;
    String userName;
    String password;


    /*if (!MikkoAdsConnection.isInitialized()) {
      hostName = System.getProperty("ADS.hostname");
      path = System.getProperty("ADS.path");
      userName = System.getProperty("ADS.username");
      password = System.getProperty("ADS.password");
      MikkoAdsConnection.setParamConnections(hostName, path, userName, password);
    }*/
//    Timestamp now = new Timestamp(System.currentTimeMillis());
//    WsdlKursLoaderCbRu kursLoader = new WsdlKursLoaderCbRu(now, now);
//    kursLoader.checkKurs();
  }


  public void loadNbu(){
    Timestamp now = new Timestamp(System.currentTimeMillis());
    try {
      logger.info("nbu start");
      connectAds();
      logger.info("nbu connect");
      WsdlKursLoaderNbu kloader = new WsdlKursLoaderNbu(now, now);
      logger.info("nbu brgin check");
      kloader.checkKurs();
      logger.info("nbu loaded");
      Timestamp mbData = new Timestamp(now.getTime() - Timestamputils.dayToMiliseconds(1));
      loadMb(mbData, false);
      loadMb(mbData, true);
    } catch (Exception e) {
      logger.error(e.getMessage());
      errorList.add(e.getMessage());
    }
    if (errorList.size()>0) {
      sendMessage(errorList.toString());
    } else {
      sendMessage(" NBU loaded");
    }
  }

  public void loadCbRu(){
    try {
      Timestamp now = new Timestamp(System.currentTimeMillis());
      connectAds();
      WsdlKursLoaderCbRu kloader = new WsdlKursLoaderCbRu(now, now, false);
      kloader.checkKurs();
      logger.info("cbru loaded");

      kloader = new WsdlKursLoaderCbRu(now, now, true);
      kloader.checkKurs();
      logger.info("bofm loaded");
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      errorList.add(ex.getMessage());
    }
    if (errorList.size()>0) {
      sendMessage(errorList.toString());
    } else {
      sendMessage(" CBRU loaded");
    }
  }

  private void loadMb(Timestamp data, boolean isSale){
    try {
      KursLoaderMB kloader = new KursLoaderMB(data, data, isSale);
      kloader.checkKurs();
      if (isSale) {
        logger.debug("mb_sale loaded");
      } else {
        logger.debug("mb loaded");
      }
    }catch (Exception ex) {
      logger.error(ex.getMessage());
      errorList.add(ex.getMessage());
    }
    if (errorList.size()>0) {
      sendMessage(errorList.toString());
    } else {
      sendMessage(" MB loaded");
    }
  }

  public void loadKz(){
    try {
      Timestamp now = new Timestamp(System.currentTimeMillis());
      connectAds();
      KursLoaderKZ kloader = new KursLoaderKZ(now, now);
      kloader.checkKurs();
      logger.debug("KZ loaded");
    }catch (Exception ex) {
      logger.error(ex.getMessage());
      errorList.add(ex.getMessage());
    }
    if (errorList.size()>0) {
      sendMessage(errorList.toString());
    } else {
      sendMessage(" KZ loaded");
    }
  }

  private void sendMessage(String text){
    if ("true".equals(System.getProperty("mail.enabled"))) {
      mailHost = System.getProperty("mail.host");
      mailPort = System.getProperty("mail.port");
      mailUser = System.getProperty("mail.user");
      mailPassword = System.getProperty("mail.password");
      mailFrom = System.getProperty("mail.from");
      mailTo = System.getProperty("mail.to");

      MailUtils.sendGmail(mailHost, mailPort, mailFrom, mailTo, mailUser, mailPassword, "load kurs", text);
    }
  }

}


