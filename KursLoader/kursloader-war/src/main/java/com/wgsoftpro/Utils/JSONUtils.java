package com.wgsoftpro.Utils;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class JSONUtils {

  public static HashMap<String,Object> JSON2Hm(JSONObject obj) {
    HashMap<String, Object> hm = new HashMap<>();
    for (Iterator<String> it = obj.keys(); it.hasNext(); ) {
       String key = it.next();
      try {
        hm.put(key, obj.get(key));
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
    }
    return hm;
  }

  public static JSONObject Hm2JSON(HashMap<String,Object> hm) throws JSONException {
    JSONObject retval = new JSONObject("{}");
    for (String key: hm.keySet() ) {
      retval.put(key, hm.get(key));
    }
    return retval;
  }

}
