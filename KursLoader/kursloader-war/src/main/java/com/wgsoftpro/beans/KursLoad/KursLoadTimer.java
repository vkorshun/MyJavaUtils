package com.wgsoftpro.beans.KursLoad;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class KursLoadTimer {

  private void loadNBU() {
    Timestamp curDate = new Timestamp(System.currentTimeMillis());
    KursLoaderUKR kursLoaderUKR = new KursLoaderUKR(curDate, curDate);
    kursLoaderUKR.checkKurs();
  }

  @Scheduled(cron="${cron.nbu.first}")
  public void loadNBUFirst(){
    loadNBU();
  }

  @Scheduled(cron="${cron.nbu.second}")
  public void loadNBUSecond(){
    loadNBU();
  }

  @Scheduled(cron="${cron.nbu.third}")
  public void loadNBUThird(){
    loadNBU();
  }

}
