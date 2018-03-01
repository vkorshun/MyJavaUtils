package com.wgsoftpro.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by vkorshun on 26.11.2015.
 */
public class KursVO {
  private Integer kodv;
  private Timestamp data;
  private BigDecimal summa;

  public KursVO(){}
  public KursVO(Integer kodv, Timestamp data, BigDecimal summa){
    this.kodv = kodv;
    this.data = data;
    this.summa = summa;
  }

  public Integer getKodv() {
    return kodv;
  }

  public void setKodv(Integer kodv) {
    this.kodv = kodv;
  }

  public Timestamp getData() {
    return data;
  }

  public void setData(Timestamp data) {
    this.data = data;
  }

  public BigDecimal getSumma() {
    return summa;
  }

  public void setSumma(BigDecimal summa) {
    this.summa = summa;
  }
}
