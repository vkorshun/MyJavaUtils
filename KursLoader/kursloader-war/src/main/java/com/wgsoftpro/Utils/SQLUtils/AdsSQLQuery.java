package com.wgsoftpro.Utils.SQLUtils;


import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by vkorshun on 30.01.2016.
 */
public class AdsSQLQuery extends SQLQuery {

  public AdsSQLQuery(Connection connection) throws SQLException {
    super(connection);
  }

  public String getString(String fieldName) throws UnsupportedEncodingException, SQLException {
    return getAnsiString(fieldName);
  }
}
