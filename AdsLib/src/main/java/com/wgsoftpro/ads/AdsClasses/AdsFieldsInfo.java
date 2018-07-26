package com.wgsoftpro.ads.AdsClasses;

import com.sun.jna.NativeLong;
import com.wgsoftpro.ads.Ace32Native;
import com.wgsoftpro.ads.Ace32Wrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AdsFieldsInfo {
  private NativeLong handle;
  private List<AdsFieldDescription> fieldList;
  private Ace32Wrapper ace32Wrapper;

  public AdsFieldsInfo(Ace32Wrapper ace32Wrapper, NativeLong handle ) {
    this.ace32Wrapper = ace32Wrapper;
    this.handle = handle;
    fieldList = new ArrayList<>();
    int nFieldsCount = ace32Wrapper.AdsGetNumFields(handle);
    for (short i=1;i<=nFieldsCount;i++) {
      AdsFieldDescription adsFieldDescription = new AdsFieldDescription();
      adsFieldDescription.setName(ace32Wrapper.AdsGetFieldName(handle, i));
      adsFieldDescription.setLength(ace32Wrapper.AdsGetFieldLength(handle,adsFieldDescription.getName()));
      adsFieldDescription.setType(ace32Wrapper.AdsGetFieldType(handle, adsFieldDescription.getName()));
      adsFieldDescription.setDecimals(ace32Wrapper.AdsGetFieldDecimals(handle, adsFieldDescription.getName()));
      adsFieldDescription.setNumber(i);
      fieldList.add(adsFieldDescription);
    }
  }

  public AdsFieldDescription getAdsFieldDescription(String name){
    List<AdsFieldDescription> list = fieldList.stream().filter(item -> item.getName().toUpperCase().equals(name.toUpperCase())).collect(Collectors.toList());
    if (list.size()==1) {
      return list.get(0);
    } else {
      throw new RuntimeException("Field not found "+name);
    }
  }
}
