package com.wgsoftpro.Utils.SQLUtils;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by vkorshun on 30.01.2016.
 */
public class MikkoAdsSQLConnectionManager extends SQLConnectionManager {

  public MikkoAdsSQLConnectionManager(String hostName, String path, String login, String password) {
    super(hostName, path, login, password);
  }

  public static Connection getConnection(String hostName, String path, String login, String password) {
    Connection connection = null;
    try {
      connection = getConnection();
    } catch (Exception ex) {
      try {
        Class.forName("com.extendedsystems.jdbc.advantage.ADSDriver");
//      connection = DriverManager.getConnection ( "jdbc:extendedsystems:advantage://localhost:6262;catalog = e:\\mikko_ejb.ads\\common\\mikkoe2.add" , "adssys", "123");
        connection = DriverManager.getConnection("jdbc:extendedsystems:advantage:" + hostName + ";catalog = " + path + ";TableType=cdx;CharType=oem");
        //login, password);
      } catch (Exception ex2) {
        ex2.printStackTrace();
      }
    }
    return connection;
  }

  public static Connection getConnection() throws NamingException, SQLException {
    Connection connection = null;
    Context initialContext = new InitialContext();
    DataSource datasource = (DataSource) initialContext.lookup("java:jboss/datasources/MikkosrvAdsDS");
    connection = datasource.getConnection();
    return connection;
  }

  public static DataSource getDataSource() throws NamingException, SQLException {
    Context initialContext = new InitialContext();
    DataSource datasource = (DataSource) initialContext.lookup("java:jboss/datasources/MikkosrvAdsDS");
    return datasource;
  }

}
