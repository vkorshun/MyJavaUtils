package com.wgsoftpro.beans.KursLoad;

import com.wgsoftpro.Utils.VkStringUtils;
import com.wgsoftpro.model.CurrencyVO;
import com.wgsoftpro.model.KursVO;
import io.vavr.control.Try;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by vkorshun on 28.11.2015.
 */
public class KursLoaderUKR extends  BaseKursLoader {
  final static Logger logger = LoggerFactory.getLogger(KursLoaderUKR.class);

  public KursLoaderUKR(Timestamp databegin, Timestamp dataend){
    super(databegin,dataend);
    currencyVOList = currencyList.getUkrCurrencyList();
  }

  @Override
  public boolean LoadKurs(Timestamp data) {
    String url = "http://www.bank.gov.ua/control/uk/curmetal/currency/search?formType=searchFormDate&time_step=daily&date="+
        VkStringUtils.dateToStr(data)+"'&execute=%D0%92%D0%B8%D0%BA%D0%BE%D0%BD%D0%B0%D1%82%D0%B8";
    url = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=%s&date=%s&json";
    try {
      currencyVOList = currencyList.getUkrCurrencyList();
      for (CurrencyVO cur: currencyVOList) {
        String response = getHTML(String.format(url, cur.getS_name().toUpperCase(), VkStringUtils.dateToStrDD(data))).toUpperCase();
        KursVO kurs = getKursFromText(response,cur, data);

        kurs.setData(data);
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

  protected KursVO getKursFromText(String text, CurrencyVO cur, Timestamp d) throws JSONException {
    KursVO result = new KursVO(cur.getKodv(), null, null);
    JSONArray _arr = new JSONArray(text);
    logger.info("UKR {}: {}", cur.getS_name(),_arr.toString());
    if (_arr.length() == 0) {
      new RuntimeException(" Response is empty :"+cur.getS_name());
    }

    try{
      JSONObject _obj = _arr.getJSONObject(0);
      result.setSumma(new BigDecimal(_obj.getString("RATE")).multiply(new BigDecimal(cur.getMashtab())));
      result.setData(d);

    } catch (Exception ex) {
      logger.error(ex.getMessage());
      errorList.add(ex.getMessage());
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
