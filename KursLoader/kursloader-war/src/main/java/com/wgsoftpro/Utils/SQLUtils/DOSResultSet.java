package com.wgsoftpro.Utils.SQLUtils;

import io.vavr.control.Try;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by vkorshun on 04.06.2016.
 */
public class DOSResultSet {
  private ResultSet resultSet;

  public DOSResultSet(){
    this.resultSet = null;
  }

  public DOSResultSet(ResultSet resultSet){
    this.resultSet = resultSet;
  }

  public ResultSet getResultSet() {
    return resultSet;
  }

  public void setResultSet(ResultSet resultSet) {
    this.resultSet = resultSet;
  }

  public String getAnsiString(String fieldName) throws SQLException {
    return resultSet.getString(fieldName);
    //return  Try.of(() ->new String(resultSet.getString(fieldName).getBytes("ISO_8859_1"),"windows-1251")).get();
  }

  public String getDosString(String fieldName) throws SQLException, UnsupportedEncodingException {
    return  Try.of( () -> new String(resultSet.getString(fieldName).getBytes("ISO_8859_1"),"cp866")).get();
  }

  public String getRawString(String fieldName) throws SQLException, UnsupportedEncodingException {
    return  Try.of(()->new String(resultSet.getString(fieldName).getBytes("ISO_8859_1"),"ISO_8859_1")).get();
  }

}
