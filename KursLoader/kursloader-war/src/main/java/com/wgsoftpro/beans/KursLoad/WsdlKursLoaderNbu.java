package com.wgsoftpro.beans.KursLoad;

import com.wgsoftpro.model.CurrencyVO;
import com.wgsoftpro.model.KursVO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vkorshun on 13.12.2015.
 */
public class WsdlKursLoaderNbu extends BaseKursLoader {
  List<NbuKursInfo> kursList = null;

  public WsdlKursLoaderNbu(Timestamp databegin, Timestamp dataend) {
    super(databegin, dataend);
    currencyVOList = currencyList.getUkrCurrencyList();
    kursList = new ArrayList();
  }

  @Override
  protected void WsdlRequest(Timestamp data){
    /*ExchangeRateNBULocator nbu = new ExchangeRateNBULocator();
    try {
      Calendar d = Calendar.getInstance();
      ExchangeRateNBUSoap_PortType pt = nbu.getExchangeRateNBUSoap();
      d.setTime(data);
      GetRatesResponseGetRatesResult resp = pt.getRates(d);
      MessageElement[] elements = resp.get_any();
      if (elements.length>1) {
        MessageElement ame = findName(elements[1],"DocumentElement");
        List<MessageElement> list =  ame.getChildren();
        for (MessageElement el: list) {
          List<MessageElement> values = el.getChildren();
          NbuKursInfo info = new NbuKursInfo();
          for (MessageElement val : values) {
            List<Text> text = val.getChildren();
            if (val.getName().equals("CodeDig"))
              info.setCodeDig(text.get(0).getValue());
            if (val.getName().equals("CodeLit"))
              info.setCodeLit(text.get(0).getValue());
            if (val.getName().equals("Amount"))
              info.setAmount(text.get(0).getValue());
            if (val.getName().equals("Exch"))
              info.setExch(text.get(0).getValue());
            if (val.getName().equals("Date"))
              info.setDate(text.get(0).getValue());
            if (val.getName().equals("Name"))
              info.setName(text.get(0).getValue());
          }
          kursList.add(info);
        }
      }
    } catch (ServiceException e) {
      logger.error(e.getMessage());
      errorList.add(e.getMessage());
    } catch (RemoteException e) {
      logger.error(e.getMessage());
      errorList.add(e.getMessage());
    }*/
  }

  @Override
  protected BigDecimal findKurs(String curCode){
    BigDecimal result = null;
    for (NbuKursInfo info: kursList){
      if (info.getCodeLit().equals(curCode)){
        result = new BigDecimal(info.getExch());
        result = result.divide(new BigDecimal(info.getAmount()));
        break;
      }
    }
    return result;
  }

  @Override
  public boolean LoadKurs(Timestamp data) throws SQLException {
    return WsdlLoadKurs(data);
  }


  @Override
  protected KursVO getKursFromText(String text, CurrencyVO cur, Timestamp d) {
    return null;
  }

  public class NbuKursInfo {
    private String CodeDig;
    private String CodeLit;
    private String Amount;
    private String Exch;
    private String Date;
    private String Name;

    public NbuKursInfo(){}

    public String getCodeDig() {
      return CodeDig;
    }

    public void setCodeDig(String codeDig) {
      CodeDig = codeDig;
    }

    public String getCodeLit() {
      return CodeLit;
    }

    public void setCodeLit(String codeLit) {
      CodeLit = codeLit;
    }

    public String getAmount() {
      return Amount;
    }

    public void setAmount(String amount) {
      Amount = amount;
    }

    public String getExch() {
      return Exch;
    }

    public void setExch(String exch) {
      Exch = exch;
    }

    public String getDate() {
      return Date;
    }

    public void setDate(String date) {
      Date = date;
    }

    public String getName() {
      return Name;
    }

    public void setName(String name) {
      Name = name;
    }
  }
}
