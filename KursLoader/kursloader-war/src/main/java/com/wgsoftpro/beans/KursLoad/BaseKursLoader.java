package com.wgsoftpro.beans.KursLoad;

import com.wgsoftpro.DAO.KursDAO;
import com.wgsoftpro.Utils.Timestamputils;
import com.wgsoftpro.model.CurrencyList;
import com.wgsoftpro.model.CurrencyVO;
import com.wgsoftpro.model.KursVO;
import org.apache.sling.commons.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vkorshun on 26.11.2015.
 */
public abstract class BaseKursLoader {
  final static Logger logger = LoggerFactory.getLogger(BaseKursLoader.class);

  protected List<CurrencyVO> currencyVOList = null;
  protected Timestamp dataBegin;
  protected Timestamp dataEnd;
  protected String siteUrl;
  protected CurrencyList currencyList = null;
  protected KursDAO kursDAO = null;
  protected List<String> errorList = new ArrayList();

  public BaseKursLoader(Timestamp databegin, Timestamp dataend) {
    dataBegin = new Timestamp(databegin.getTime());
    dataEnd = new Timestamp(dataend.getTime());
    currencyList = new CurrencyList();
    kursDAO = new KursDAO();
  }

  public abstract boolean LoadKurs(Timestamp data) throws SQLException;

  public static String getHTML(String urlToRead) throws Exception {
    return getHTML(urlToRead, "UTF-8");
  }

  public static String getHTML(String urlToRead, String charSet) throws Exception {
    StringBuilder result = new StringBuilder();
    URL url = new URL(urlToRead);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName(charSet)));
    String line;
    while ((line = rd.readLine()) != null) {
      result.append(line);
    }
    rd.close();
    return result.toString();
  }

  public void checkKurs() {
    Timestamp t1 = new Timestamp(dataBegin.getTime());
    try {
      while (t1.before(dataEnd) || t1.equals(dataEnd)) {
        LoadKurs(t1);
        t1.setTime(t1.getTime() + Timestamputils.dayToMiliseconds(1));
      }
      logger.debug("kurs loaded ");
    } catch (Exception ex) {
      errorList.add(ex.getMessage());
      logger.error(ex.getMessage());
    }
  }

  public static CurrencyVO findInList(List<CurrencyVO> list, Integer kodv) {
    CurrencyVO result = null;
    for (CurrencyVO cur : list) {
      if (kodv.equals(cur.getKodv())) {
        result = cur;
        break;
      }
    }
    return result;
  }

  //protected abstract KursVO getKursFromText(String text, CurrencyVO cur, Timestamp d) throws JSONException;
  protected abstract KursVO getKursFromText(String text, CurrencyVO cur, Timestamp d) throws JSONException;

  /*protected MessageElement findName(MessageElement element, String name){
    MessageElement result = null;
    List<MessageElement> child = null;
    child = (List<MessageElement>) element.getChildren();
    while (child != null && result==null) {
      for (MessageElement el: child){
        if (el.getName().equals(name)){
          result = el;
          break;
        } else {
          result = findName(el,name);
          if (result != null) break;
        }
      }
    }
    return result;
  }*/

  protected abstract void WsdlRequest(Timestamp data);

  protected abstract BigDecimal findKurs(String curCode);


  public boolean WsdlLoadKurs(Timestamp data) throws SQLException {
    boolean retval = true;
    WsdlRequest(data);
    for (CurrencyVO cur : currencyVOList) {
      KursVO kurs = new KursVO(cur.getKodv(), data, null);
      BigDecimal curs = findKurs(cur.getS_name().substring(0, 3));
      if (curs != null) {
        curs = curs.multiply(new BigDecimal(cur.getMashtab()));
      } else {
        retval = false;
      }
      kurs.setSumma(curs);
      if (!kursDAO.setKusVO(kurs)) retval = false;
    }
    return retval;
  }

  public List<String> getErrorList() {
    return errorList;
  }

}
