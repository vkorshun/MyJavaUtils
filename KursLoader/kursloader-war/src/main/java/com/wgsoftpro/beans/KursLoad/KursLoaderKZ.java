package com.wgsoftpro.beans.KursLoad;

import com.wgsoftpro.Utils.VkStringUtils;
import com.wgsoftpro.model.CurrencyVO;
import com.wgsoftpro.model.KursVO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by vkorshun on 26.05.2016.
 */
public class KursLoaderKZ extends  BaseKursLoader{

  public KursLoaderKZ(Timestamp databegin, Timestamp dataend) {
    super(databegin, dataend);
    currencyVOList = currencyList.getKzCurrencyList();
  }

  @Override
  public boolean LoadKurs(Timestamp data) throws SQLException {
    String url = "http://www.nationalbank.kz/rss/get_rates.cfm?fdate="+
        VkStringUtils.dateToStr(data);
    try {
      String response = getHTML(url).toUpperCase();
      currencyVOList = currencyList.getKzCurrencyList();
      for (CurrencyVO cur: currencyVOList) {
        KursVO kurs = getKursFromText(response,cur, null);
        kurs.setData(data);
        kursDAO.setKusVO(kurs);
        return kurs.getSumma().compareTo(BigDecimal.ZERO) !=0;
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      errorList.add(e.getMessage());
    }
    return false;
  }

  @Override
  protected KursVO getKursFromText(String text, CurrencyVO cur, Timestamp d) {
    KursVO result = new KursVO(cur.getKodv(),null,null);
    String keyValue = cur.getS_name()+"</TITLE><DESCRIPTION>";
    String keyValue2 = "</DESCRIPTION>";
    try {
      int m_p = text.indexOf(keyValue);
      int m_p2 = m_p+keyValue.length();
      if (m_p>=0) {
        String sammount = text.substring(m_p2,m_p2+100);
        int m_p3 = sammount.indexOf(keyValue2);
        sammount = sammount.substring(0,m_p3);
        BigDecimal kurs = new BigDecimal(sammount);
        result.setSumma(kurs);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
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
}
