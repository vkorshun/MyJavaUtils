package com.wgsoftpro.beans.KursLoad;

import com.wgsoftpro.Utils.Timestamputils;
import com.wgsoftpro.Utils.VkStringUtils;
import com.wgsoftpro.model.CurrencyVO;
import com.wgsoftpro.model.KursVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by vkorshun on 04.12.2015.
 */
public class KursLoaderMB extends BaseKursLoader {
  final static Logger LOGGER = LoggerFactory.getLogger(KursLoaderMB.class);

  private boolean bSale = false;

  public KursLoaderMB(Timestamp databegin, Timestamp dataend, boolean isSale) {
    super(databegin, dataend);
    bSale = isSale;
    if (isSale) {
      currencyVOList = currencyList.getMbSaleCurrencyList();
    } else {
      currencyVOList = currencyList.getMbCurrencyList();
    }
  }

  private boolean loadKursOnValuta(Timestamp data, CurrencyVO cur) {
    SimpleDateFormat formatD = new SimpleDateFormat("dd-MM-yyyy");
    Timestamp calc_data1 = getKursData(data, 2);
    Timestamp calc_data2 = getKursData(data, 1);
    String url = "https://minfin.com.ua/currency/mb/archive/" + cur.getS_name() + "/" + formatD.format(calc_data2) + "/" + formatD.format(calc_data2);
    try {
      String response = getHTML(url);
      String emptyKurs = "В выбранный вами период торги на Межбанке не проводились";

      int k = response.indexOf(emptyKurs);
      while (k > -1) {
        calc_data1 = getKursData(calc_data1, 1);
        calc_data2 = getKursData(calc_data2, 1);
        url = "https://minfin.com.ua/currency/mb/archive/" + cur.getS_name() + "/" + formatD.format(calc_data2) + "/" + formatD.format(calc_data2);
        response = getHTML(url);
        k = response.indexOf(emptyKurs);
      }
      KursVO kurs = getKursFromText(response, cur, null);
      kurs.setData(data);
      kursDAO.setKusVO(kurs);
      return kurs.getSumma().compareTo(BigDecimal.ZERO) != 0;
    } catch (Exception e) {
      logger.error(e.getMessage());
      errorList.add(e.getMessage());
    }
    return false;
  }


  @Override
  public boolean LoadKurs(Timestamp data) {
    for (CurrencyVO cur : currencyVOList) {
      loadKursOnValuta(data, cur);
    }
    return true;
  }

  private String getEmptyKurs(Timestamp d) {
    return "'Котировки межбанковского валютного рынка Украины на " + VkStringUtils.dateToStr(d) + " отсутствуют.<br>Ближайшие курсы есть на ".toUpperCase();
  }

  @Override
  protected KursVO getKursFromText(String text, CurrencyVO cur, Timestamp d) {
    final String sBegin = "<th>Курс<br>продажи<small>грн</small></th>";
    final String sEnd = "</td></tr></tbody>";
    final String sTag = "</i>";
    int p1 = text.indexOf(sBegin);
    int p2 = text.indexOf(sEnd);
    KursVO result = new KursVO();
    result.setKodv(cur.getKodv());
    result.setData(d);
    if (p2 > p1) {
      p2 = text.indexOf(sTag, p2 - 20);
      if (p2 > 0) {
        String s = text.substring(p2 + sTag.length(), p2 + sTag.length() + 10);
        int p3 = s.indexOf("<");
        s = s.substring(0, p3).trim();
        result.setSumma(new BigDecimal(s));

        if (!bSale) {
          s = text.substring(p2 - 100, p2);
          p3 = s.indexOf(sTag);
          if (p3 > 0) {
            s = s.substring(p3 + 4);
            p3 = s.indexOf("<");
            s = s.substring(0, p3).trim();
            BigDecimal newSumma = result.getSumma().add(new BigDecimal(s));
            result.setSumma(newSumma.divide(BigDecimal.valueOf(2)));
          }
        }
      }
    }
    return result;
  }

  @Override
  protected void WsdlRequest(Timestamp data) {

  }

  @Override
  protected BigDecimal findKurs(String curCode) {
    return null;
  }

  public boolean isbSale() {
    return bSale;
  }

  public void setbSale(boolean bSale) {
    this.bSale = bSale;
    if (bSale) {
      currencyVOList = currencyList.getMbSaleCurrencyList();
    } else {
      currencyVOList = currencyList.getMbCurrencyList();
    }
  }

  protected Timestamp getKursData(Timestamp data, int previous) {
    Timestamp result;
    if (previous != 0) {
      result = new Timestamp(data.getTime() - Timestamputils.dayToMiliseconds(previous));
    } else {
      result = data;
    }
    return result;
  }
}
