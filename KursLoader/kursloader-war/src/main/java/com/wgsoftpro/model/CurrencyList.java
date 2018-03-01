package com.wgsoftpro.model;

//import com.gmail.vkorshun.vklib.SQLUtils.DOSResultSet;
import com.wgsoftpro.Utils.SQLUtils.DOSResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.wgsoftpro.DAO.BaseDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by vkorshun on 22.11.2015.
 */
public class CurrencyList {
  final static Logger LOGGER = LoggerFactory.getLogger(CurrencyList.class);

  List<CurrencyVO> items = new ArrayList<CurrencyVO>();
  private List<Integer> nbuCurrencyCodeList = new ArrayList();
  private List<Integer> excludedCurrencyCodeList = new ArrayList();
  private List<Integer> russianCurrencyCodeList = new ArrayList();
  private List<Integer> mbCurrencyCodeList = new ArrayList();
  private List<Integer> mbSaleCurrencyCodeList = new ArrayList();
  private List<Integer> bofmCurrencyCodeList = new ArrayList();
  private List<Integer> kzCurrencyCodeList = new ArrayList();


  private List<CurrencyVO> ukrCurrencyList = new ArrayList<CurrencyVO>();
  private List<CurrencyVO> rusCurrencyList = new ArrayList<CurrencyVO>();
  private List<CurrencyVO> mbCurrencyList = new ArrayList<CurrencyVO>();
  private List<CurrencyVO> mbSaleCurrencyList = new ArrayList<CurrencyVO>();
  private List<CurrencyVO> bofmCurrencyList = new ArrayList<CurrencyVO>();
  private List<CurrencyVO> kzCurrencyList = new ArrayList<CurrencyVO>();

  public CurrencyList() {
    //excludedCurrencyCodeList.add(10); //GRN
    //excludedCurrencyCodeList.add(160);//Mark
    //excludedCurrencyCodeList.add(163);//Brb
    nbuCurrencyCodeList = getList(System.getProperty("currency.nbu.list"));
    russianCurrencyCodeList = getList(System.getProperty("currency.cbru.list"));
    mbCurrencyCodeList = getList(System.getProperty("currency.mb.list"));
    mbSaleCurrencyCodeList = getList(System.getProperty("currency.mb.sale.list"));
    bofmCurrencyCodeList = getList(System.getProperty("currency.mb.bofm.list"));
    kzCurrencyCodeList = getList(System.getProperty("currency.kz.list"));

    /*russianCurrencyCodeList.add(51567);// Eur (rus)
    russianCurrencyCodeList.add(51568);// USD (rus)

    mbCurrencyCodeList.add(154100);//USD
    mbCurrencyCodeList.add(156194); //EUR

    mbSaleCurrencyCodeList.add(172889);//USD
    mbSaleCurrencyCodeList.add(172889);//USD

    bofmCurrencyCodeList.add(179727);//EUR

    kzCurrencyCodeList.add(272940);// KZ - EUR
    */

    loadList();
    loadAllCurrencyList();
  }

  public List<CurrencyVO> getItems() {
    return items;
  }

  public void loadList() {
    items.clear();
    JdbcTemplate jdbcTemplate = new JdbcTemplate(BaseDAO.getDataSource());
    items = jdbcTemplate.query("SELECT * FROM valuta", new MikkoCurrencyVORowMapper()
      );
  }

  private void loadAllCurrencyList() {
    for (CurrencyVO cur : items) {
      //if ((excludedCurrencyCodeList.indexOf(cur.getKodv()) == -1) && (russianCurrencyCodeList.indexOf(cur.getKodv()) == -1)
      //    && (mbCurrencyCodeList.indexOf(cur.getKodv()) == -1) && (mbSaleCurrencyCodeList.indexOf(cur.getKodv()) == -1)
      //    && (bofmCurrencyCodeList.indexOf(cur.getKodv()) == -1) && (kzCurrencyCodeList.indexOf(cur.getKodv()) == -1)) {
      if (nbuCurrencyCodeList.indexOf(cur.getKodv()) > -1) {
        ukrCurrencyList.add(cur);
      } else if (russianCurrencyCodeList.indexOf(cur.getKodv()) > -1) {
        rusCurrencyList.add(cur);
      } else if (mbCurrencyCodeList.indexOf(cur.getKodv()) > -1) {
        mbCurrencyList.add(cur);
      } else if (mbSaleCurrencyCodeList.indexOf(cur.getKodv()) > -1) {
        mbSaleCurrencyList.add(cur);
      } else if (bofmCurrencyCodeList.indexOf(cur.getKodv()) > -1) {
        bofmCurrencyList.add(cur);
      } else if (kzCurrencyCodeList.indexOf(cur.getKodv()) > -1) {
        kzCurrencyList.add(cur);
      }
    }
  }


  private static class MikkoCurrencyVORowMapper implements RowMapper<CurrencyVO> {
    @Override
    public CurrencyVO mapRow(ResultSet resultSet, int i) throws SQLException {
      try {
        DOSResultSet query = new DOSResultSet(resultSet);
        CurrencyVO mc = new CurrencyVO();
        mc.setKodv(query.getResultSet().getInt("KODV"));
        mc.setName(query.getAnsiString("NAME"));
        mc.setS_name(query.getAnsiString("S_NAME"));
        mc.setNamedop(query.getAnsiString("NAMEDOP"));
        mc.setMain(query.getResultSet().getInt("MAIN"));
        mc.setMashtab(query.getResultSet().getInt("MASHTAB"));
        return mc;
      } catch (Exception ex) {
        LOGGER.error(ex.getMessage());
        return null;
      }
    }
  }

  public static List<Integer> getList(String s){
    if (Optional.of(s).isPresent()) {
      List<String> listStrings = Arrays.asList(s.split(","));
      List<Integer> listKodv = new ArrayList();
      listStrings.forEach((item) -> listKodv.add(Integer.valueOf(item.trim())));
      return listKodv;
    } else {
      return null;
    }
  }

  public List<CurrencyVO> getUkrCurrencyList() {
    return ukrCurrencyList;
  }

  public List<CurrencyVO> getRusCurrencyList() {
    return rusCurrencyList;
  }

  public List<CurrencyVO> getMbCurrencyList() {
    return mbCurrencyList;
  }

  public List<CurrencyVO> getMbSaleCurrencyList() {
    return mbSaleCurrencyList;
  }

  public List<CurrencyVO> getBofmCurrencyList() {
    return bofmCurrencyList;
  }

  public List<CurrencyVO> getKzCurrencyList() {
    return kzCurrencyList;
  }
}
