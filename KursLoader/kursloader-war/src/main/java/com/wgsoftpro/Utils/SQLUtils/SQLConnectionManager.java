package com.wgsoftpro.Utils.SQLUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by vkorshun on 22.11.2015.
 */
public abstract class SQLConnectionManager {
  final static Logger logger = LoggerFactory.getLogger(SQLConnectionManager.class);

  static String hostName = null;
  static String path = null;
  static String userName = null;
  static String password = null;
  static boolean initialized = false;


  public static  void setParamConnections(String aHostName, String aPath, String aUserName, String aPassword){
    hostName = aHostName;
    path = aPath;
    userName = aUserName;
    password = aPassword;
    initialized = true;
  }

  public SQLConnectionManager(String hostName, String path, String login, String password) {
    setParamConnections(hostName, path, login, password);
  }

  public static Connection getConnection(String hostName, String path, String login, String password){
    return null;
  }

  public static  Connection getConnection() throws NamingException, SQLException {
    return null;
  }

  public static void close(Statement stm) {
    try {
      if (stm != null)  stm.close();
    } catch (Exception ex) {
      logger.error(ex.getMessage());
    }
  }

  public static void close(ResultSet rs) {
    try {
      if (rs != null)  rs.close();
    } catch (Exception ex) {
      logger.error(ex.getMessage());
    }
  }

  public static void close(SQLQuery qr) {
    try {
      if (qr != null) qr.close();
    } catch (Exception ex) {
      logger.error(ex.getMessage());
    }
  }

  public static void close(Connection con) {
    try {
      if (con != null) con.close();
    } catch (Exception ex) {
      logger.error(ex.getMessage());
    }
  }

}
