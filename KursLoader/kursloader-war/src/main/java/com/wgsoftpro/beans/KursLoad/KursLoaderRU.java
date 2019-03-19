package com.wgsoftpro.beans.KursLoad;

import com.wgsoftpro.Utils.Timestamputils;
import com.wgsoftpro.Utils.VkStringUtils;
import com.wgsoftpro.beans.KursLoad.bean.RusKursParser;
import com.wgsoftpro.beans.KursLoad.model.RusKursItem;
import com.wgsoftpro.beans.KursLoad.model.RusKursList;
import com.wgsoftpro.model.CurrencyVO;
import com.wgsoftpro.model.KursVO;
import org.apache.sling.commons.json.JSONException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;

public class KursLoaderRU extends BaseKursLoader {
   public KursLoaderRU(Timestamp databegin, Timestamp dataend) {
      super(databegin, dataend);
      currencyVOList = currencyList.getRusCurrencyList();
   }

   @Override
   public boolean LoadKurs(Timestamp data) throws SQLException {
      String url = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=%s";//+StrZero(d,2)+'/'+StrZero(m,2)+'/'+IntToStr(y);
      try {
         currencyVOList = currencyList.getRusCurrencyList();
         String response = getHTML(String.format(url, VkStringUtils.dateToStrSpalsh(data)),"cp1251");
         RusKursList list = new RusKursParser().parseXml(response);
         for (CurrencyVO cur : currencyVOList) {
            KursVO kurs = new KursVO();
            RusKursItem rusKursItem = list.getItem(cur.getS_name().substring(0,3));
            kurs.setKodv(cur.getKodv());
            kurs.setData(list.getData());
            kurs.setSumma(rusKursItem.getValue().divide(new BigDecimal(rusKursItem.getNominal())));
            kursDAO.setKusVO(kurs);
            //return kurs.getSumma().compareTo(BigDecimal.ZERO) !=0;
         }
      } catch (Exception e) {
         logger.error(e.getMessage());
         errorList.add(e.getMessage());
      }
      return false;
   }

   @Override
   public void checkKurs() {
      super.checkKurs();
   }


   @Override
   protected KursVO getKursFromText(String text, CurrencyVO cur, Timestamp d) throws JSONException {
      return null;
   }

   @Override
   protected void WsdlRequest(Timestamp data) {

   }

   @Override
   protected BigDecimal findKurs(String curCode) {
      return null;
   }
}
