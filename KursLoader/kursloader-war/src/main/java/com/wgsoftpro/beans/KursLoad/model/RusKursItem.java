package com.wgsoftpro.beans.KursLoad.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class RusKursItem {
   private String id;
   private String numcode;
   private String charcode;
   private Integer nominal;
   private String name;
   private BigDecimal value;

   public RusKursItem() {
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getNumcode() {
      return numcode;
   }

   public void setNumcode(String numcode) {
      this.numcode = numcode;
   }

   public String getCharcode() {
      return charcode;
   }

   public void setCharcode(String charcode) {
      this.charcode = charcode;
   }

   public Integer getNominal() {
      return nominal;
   }

   public void setNominal(Integer nominal) {
      this.nominal = nominal;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public BigDecimal getValue() {
      return value;
   }

   public void setValue(BigDecimal value) {
      this.value = value;
   }
}
