package com.wgsoftpro.beans.KursLoad.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class RusKursList {
   private Timestamp data;
   private List<RusKursItem> list;

   public RusKursList() {
      list = new ArrayList<>();
   }

   public RusKursItem getItem(String key) {
      for (RusKursItem item : list) {
         if (key.equals(item.getCharcode())) {
            return item;
         }
      }
      return null;
   }
}
