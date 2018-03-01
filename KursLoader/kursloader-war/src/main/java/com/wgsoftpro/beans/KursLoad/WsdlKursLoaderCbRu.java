package com.wgsoftpro.beans.KursLoad;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import com.wgsoftpro.Utils.Timestamputils;
import com.wgsoftpro.beans.KursLoad.WSDL.DailyInfo.DailyInfo;
import com.wgsoftpro.beans.KursLoad.WSDL.DailyInfo.DailyInfoSoap;
import com.wgsoftpro.beans.KursLoad.WSDL.DailyInfo.GetCursOnDateResponse;
import com.wgsoftpro.model.CurrencyVO;
import com.wgsoftpro.model.KursVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

/**
 * Created by vkorshun on 12.12.2015.
 */
public class WsdlKursLoaderCbRu extends BaseKursLoader {
  final static Logger logger = LoggerFactory.getLogger(WsdlKursLoaderCbRu.class);

  List<CbKursInfo> kursList = null;

  public WsdlKursLoaderCbRu(Timestamp databegin, Timestamp dataend, boolean isBofm) {
    super(databegin, dataend);

    if (isBofm) {
      dataBegin = Timestamputils.firstDayOfMonth(databegin);
      dataEnd = new Timestamp(dataBegin.getTime());
      currencyVOList = currencyList.getBofmCurrencyList();
    } else {
      currencyVOList = currencyList.getRusCurrencyList();
    }
    kursList = new ArrayList();
  }

  @Override
  protected void WsdlRequest(Timestamp data) {
    DailyInfo dailyInfo = new DailyInfo();
    try {
      GregorianCalendar d = (GregorianCalendar) GregorianCalendar.getInstance();
      DailyInfoSoap soap = dailyInfo.getDailyInfoSoap();
      d.setTime(data);
      GetCursOnDateResponse.GetCursOnDateResult resp = soap.getCursOnDate(new XMLGregorianCalendarImpl(d));
      d = null;
      Node element =  ((Node)resp.getAny()).getFirstChild().getFirstChild();

      while (element != null) {
        Node node =  element.getFirstChild();
        //List<Element> list = ame.getChildren();
        //for (MessageElement el : list) {
          //List<MessageElement> values = el.getChildren();
          CbKursInfo info = new CbKursInfo();
          //for (MessageElement val : values) {
          //  List<Text> text = val.getChildren();
        while (node != null) {
            if (node.getLocalName().equals("Vname"))
              info.setVname(node.getFirstChild().getNodeValue());
            if (node.getLocalName().equals("Vnom"))
              info.setVnom(node.getFirstChild().getNodeValue());
            if (node.getLocalName().equals("Vcurs"))
              info.setVcurs(node.getFirstChild().getNodeValue());
            if (node.getLocalName().equals("Vcode"))
              info.setVcode(node.getFirstChild().getNodeValue());
            if (node.getLocalName().equals("VchCode"))
              info.setVchCode(node.getFirstChild().getNodeValue());
            node = node.getNextSibling();

        }
        kursList.add(info);
        element = element.getNextSibling();
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      throw new RuntimeException(ex);
    }
  }

  @Override
  protected BigDecimal findKurs(String curCode) {
    return Optional.ofNullable(kursList.stream().filter((item) -> item.getVchCode().equals(curCode)).findFirst()).map((item) -> new BigDecimal(item.get().getVcurs()) ).
        orElseGet( null);
  }

  @Override
  public boolean LoadKurs(Timestamp data) throws SQLException {
    return WsdlLoadKurs(data);
  }


  @Override
  protected KursVO getKursFromText(String text, CurrencyVO cur, Timestamp d) {
    return null;
  }

  public class CbKursInfo {
    private String Vname;
    private String Vnom;
    private String Vcurs;
    private String Vcode;
    private String VchCode;

    public CbKursInfo() {
    }

    public String getVname() {
      return Vname;
    }

    public void setVname(String vname) {
      Vname = vname;
    }

    public String getVnom() {
      return Vnom;
    }

    public void setVnom(String vnom) {
      Vnom = vnom;
    }

    public String getVcurs() {
      return Vcurs;
    }

    public void setVcurs(String vcurs) {
      Vcurs = vcurs;
    }

    public String getVcode() {
      return Vcode;
    }

    public void setVcode(String vcode) {
      Vcode = vcode;
    }

    public String getVchCode() {
      return VchCode;
    }

    public void setVchCode(String vchCode) {
      VchCode = vchCode;
    }
  }
}
