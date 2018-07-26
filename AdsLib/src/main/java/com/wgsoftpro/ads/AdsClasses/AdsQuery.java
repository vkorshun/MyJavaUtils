package com.wgsoftpro.ads.AdsClasses;

import com.wgsoftpro.ads.Ace32Wrapper;

import java.util.*;

public class AdsQuery extends AdsStatement {
  private String query;
  private HashSet<String> params;
  private HashMap hmParams;

  public AdsQuery(AdsConnection adsConnection) {
    super(adsConnection);
    params = new HashSet();
    hmParams = new HashMap();
  }

  public void execute() {
    if (query == null || query.isEmpty()) {
      throw new RuntimeException("Query isn't defined");
    }
    createStatement();
    if (params.isEmpty()) {
      hCursor = ace32Wrapper.AdsExecuteSQLDirect(hStatement, query);
    } else {
      ace32Wrapper.AdsPrepareSQL(hStatement, query);
      setParams();
      hCursor = ace32Wrapper.AdsExecuteSQL(hStatement);
    }
    if (hCursor != null) {
      adsFieldsInfo = new AdsFieldsInfo(ace32Wrapper, hCursor);
    }
  }

  public void setQuery(String query){
    parse(query, params);
    this.query = query;
  }

  public static final String parse(String query, Set params) {
    // I was originally using regular expressions, but they didn't work well for ignoring
    // parameter-like strings inside quotes.

    int length = query.length();
    StringBuffer parsedQuery = new StringBuffer(length);
    boolean inSingleQuote = false;
    boolean inDoubleQuote = false;
    boolean inComment = false;
    boolean inCommentLine = false;
    int index = 1;

    for (int i = 0; i < length; i++) {
      char c = query.charAt(i);
      if (inSingleQuote) {
        if (c == '\'') {
          inSingleQuote = false;
        }
      } else if (inDoubleQuote) {
        if (c == '"') {
          inDoubleQuote = false;
        }
      } else if (inComment) {
        if (c == '*' && query.charAt(i + 1) == '/') {
          inComment = false;
        }
      } else if (inCommentLine) {
        if (c == '\n') {
          inCommentLine = false;
        }
      } else {
        if (c == '\'') {
          inSingleQuote = true;
        } else if (c == '"') {
          inDoubleQuote = true;
        } else if (c == '/' && i < length - 1 && query.charAt(i + 1) == '*') {
          inComment = true;
        } else if ((c == '/' && i < length - 1 && query.charAt(i + 1) == '/') || (c == '-' && query.charAt(i + 1) == '-')) {
          inCommentLine = true;
        } else if (c == ':' && i + 1 < length &&
            Character.isJavaIdentifierStart(query.charAt(i + 1)) && (i > 0 ? query.charAt(i - 1) != ':' : true)) {
          int j = i + 2;
          while (j < length && Character.isJavaIdentifierPart(query.charAt(j))) {
            j++;
          }
          String name = query.substring(i + 1, j);
          /*c = '?'; // replace the parameter with a question mark
          i += name.length(); // skip past the end if the parameter

          List indexList = (List) paramMap.get(name);
          if (indexList == null) {
            indexList = new LinkedList();
            paramMap.put(name, indexList);
          }
          indexList.add(new Integer(index));

          index++;*/
          params.add(name);
        }
      }
      parsedQuery.append(c);
    }

    // replace the lists of Integer objects with arrays of ints
    /*for (Iterator itr = paramMap.entrySet().iterator(); itr.hasNext(); ) {
      Map.Entry entry = (Map.Entry) itr.next();
      List list = (List) entry.getValue();
      int[] indexes = new int[list.size()];
      int i = 0;
      for (Iterator itr2 = list.iterator(); itr2.hasNext(); ) {
        Integer x = (Integer) itr2.next();
        indexes[i++] = x.intValue();
      }
      entry.setValue(indexes);
    }*/

    return parsedQuery.toString();
  }

  public void setParam(String name, Object value) {
    hmParams.put(name, value);
  }

  private void setParams() {
    for (String parName: params) {
      if (hmParams.containsKey(parName)) {
        setFieldValue(parName, hmParams.get(parName));
      } else {
        setFieldValue(parName, null);
      }
    }
  }
}
