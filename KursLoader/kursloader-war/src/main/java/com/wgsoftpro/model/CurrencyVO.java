package com.wgsoftpro.model;

/**
 * Created by vkorshun on 22.11.2015.
 */
public class CurrencyVO {
  int kodv;
  String name;
  String s_name;
  int main;
  String namedop;
  int mashtab;

  public CurrencyVO(){}

  public int getKodv() {
    return kodv;
  }

  public void setKodv(int kodv) {
    this.kodv = kodv;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getS_name() {
    if("руб.".equals(s_name)) {
      return "RUB";
    }else   if("EUER".equals(s_name)) {
      return "EUR";
    }else {
      return s_name;
    }
  }

  public void setS_name(String s_name) {
    this.s_name = s_name;
  }

  public int getMain() {
    return main;
  }

  public void setMain(int main) {
    this.main = main;
  }

  public String getNamedop() {
    return namedop;
  }

  public void setNamedop(String namedop) {
    this.namedop = namedop;
  }

  public int getMashtab() {
    return mashtab;
  }

  public void setMashtab(int mashtab) {
    this.mashtab = mashtab;
  }
}
