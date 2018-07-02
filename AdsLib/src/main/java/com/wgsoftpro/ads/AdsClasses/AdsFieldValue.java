package com.wgsoftpro.ads.AdsClasses;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

public class AdsFieldValue {
  private Object value;

  public AdsFieldValue(Object value) {
    this.value = value;
  }

  public boolean isBlank() {
    return value == null;
  }
  public String asString(){
    return Optional.ofNullable(value).map(item->item.toString()).orElse("");
  }

  public Integer asInteger() {
    return Optional.ofNullable(value).map(item-> (item instanceof Integer ? (Integer) item : new Integer(item.toString()))).orElse(0);
  }

  public Long asLong() {
    if (isBlank()) {
      return 0L;
    } else {
      return value instanceof Long ? (Long) value : new Long(value.toString()) ;
    }
  }

  public Double asDouble() {
    if (isBlank()) {
      return new Double(0);
    } else {
      return value instanceof Double ? (Double) value : new Double(value.toString()) ;
    }
  }

  public Date asDate() {
    if (isBlank()) {
      return new Date(0);
    } else {
      return value instanceof Date ? (Date) value : new Date(value.toString()) ;
    }
  }

  public Timestamp asTimestamp() {
    if (isBlank()) {
      return new Timestamp(0);
    } else {
      return value instanceof Timestamp ? (Timestamp) value : new Timestamp((Long)value) ;
    }
  }
}
