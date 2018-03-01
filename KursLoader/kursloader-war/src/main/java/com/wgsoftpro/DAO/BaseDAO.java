package com.wgsoftpro.DAO;

import com.wgsoftpro.Utils.SQLUtils.DOSResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by vkorshun on 04.06.2016.
 */
public class BaseDAO {
  final static Logger LOGGER = LoggerFactory.getLogger(BaseDAO.class);

  public BaseDAO(){
  }

  protected static PlatformTransactionManager transactionManager = new org.springframework.jdbc.datasource.DataSourceTransactionManager();


  /*public static DataSource getDataSource() {
    DataSource datasource = null;
    try {
      Context initialContext = new InitialContext();
      datasource = (DataSource) initialContext.lookup("java:jboss/datasources/MikkosrvAdsDS");
      return datasource;
    } catch (Exception ex) {
      LOGGER.error(ex.getMessage());
      return getTestDataSource();
    }
  }*/


  public static DataSource getDataSource()  {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    try {
      Class.forName("com.extendedsystems.jdbc.advantage.ADSDriver");
      dataSource.setDriverClassName("com.extendedsystems.jdbc.advantage.ADSDriver");
      dataSource.setUrl(System.getProperty("connection.url"));
      dataSource.setUsername(System.getProperty("connection.user"));
      dataSource.setPassword(System.getProperty("connection.password"));
    } catch (Exception ex) {
      LOGGER.error(ex.getMessage());
    }
    return dataSource;
  }

  public static BigDecimal getNewId(String alias) throws Exception{
    BigDecimal result = getNewNum(alias);
    if (result == null) {
      throw new Exception("Error get sequence");
    }
    return result;
  }

  public static BigDecimal getNewNum(String alias){
    DataSource ds = getDataSource();
   ((org.springframework.jdbc.datasource.DataSourceTransactionManager) transactionManager).setDataSource(ds);
    TransactionDefinition def = new DefaultTransactionDefinition();
    TransactionStatus status = transactionManager.getTransaction(def);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
    try {
      jdbcTemplate.update("UPDATE tools\\newnum SET sequence=sequence+1 WHERE name=?", new Object[]{alias});
      List<BigDecimal> sequnce = jdbcTemplate.query("SELECT sequence FROM tools\\newnum WHERE name=?", new Object[]{alias},
          (resultSet,i)-> {  return resultSet.getBigDecimal("sequence"); });
/*           new RowMapper<BigDecimal>() {
             @Override
             public BigDecimal mapRow(ResultSet resultSet, int i) {
               try {
                 return resultSet.getBigDecimal("sequence");
               } catch (SQLException e) {
                 LOGGER.error(e.getMessage());
                 return null;
               }
             }
           }
            );*/
      transactionManager.commit(status);

      return sequnce.size()>0 ? sequnce.get(0) : null;
    } catch (Exception ex) {
      transactionManager.rollback(status);
      LOGGER.error(ex.getMessage());
      return null;
    }
  }

  public static String getTest() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
    List<String> retList = jdbcTemplate.query("SELECT name FROM client WHERE kodkli< 100",(rs,i)-> {
         return new DOSResultSet(rs).getAnsiString("name");
    });
    return "{\"test\":\""+retList.get(0)+"\"}";

  }
}
