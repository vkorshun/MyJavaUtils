package com.wgsoftpro.Utils.SQLUtils;

//import com.extendedsystems.jdbc.advantage.ADSStatement;

import java.io.UnsupportedEncodingException;
import java.sql.*;

/**
 * Created by vkorshun on 22.11.2015.
 */
public class SQLQuery {
  Statement statment = null;
  ResultSet resultSet = null;
  Connection connection;

  public SQLQuery(Connection connection) throws SQLException {
    this.connection = connection;
    statment =  connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
          ResultSet.CONCUR_UPDATABLE);
  }

  public void close() throws SQLException {
    if (resultSet != null ) {
        resultSet.close();
    }
    if (statment != null) {
      statment.close();;
    }
  }

  public PreparedStatement prepareQuery(String AQuery) throws SQLException {
    if (resultSet != null && !resultSet.isClosed()) {
      resultSet.close();
    }
    return connection.prepareStatement(AQuery);
  }

  public Statement getStatment() {
    return statment;
  }

  public void setStatment(Statement statment) {
    this.statment = statment;
  }

  public ResultSet getResultSet() {
    return resultSet;
  }

  public void setResultSet(ResultSet resultSet) {
    this.resultSet = resultSet;
  }

  public String getAnsiString(String fieldName) throws SQLException, UnsupportedEncodingException {
    //return resultSet.getString(fieldName);
    return  new String(resultSet.getString(fieldName).getBytes("ISO_8859_1"),"windows-1251");
  }

  public String getDosString(String fieldName) throws SQLException, UnsupportedEncodingException {
    //return resultSet.getString(fieldName);
    return  new String(resultSet.getString(fieldName).getBytes("ISO_8859_1"),"cp866");
  }

  public String getRawString(String fieldName) throws SQLException, UnsupportedEncodingException {
    //return resultSet.getString(fieldName);
    return  new String(resultSet.getString(fieldName).getBytes("ISO_8859_1"),"ISO_8859_1");
  }


}
