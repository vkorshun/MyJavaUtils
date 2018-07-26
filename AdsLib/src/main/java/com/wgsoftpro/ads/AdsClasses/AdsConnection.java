package com.wgsoftpro.ads.AdsClasses;

import com.sun.jna.NativeLong;
import com.wgsoftpro.ads.Ace32;
import com.wgsoftpro.ads.Ace32Wrapper;
import lombok.Getter;

import java.io.Closeable;
import java.io.IOException;

import static com.wgsoftpro.ads.Ace32.*;

@Getter
public class AdsConnection implements Closeable {
  private String path;
  private String userName;
  private String password;
  private short serverTypes;
  private int options;
  private int sqlTimeout = ADS_DEFAULT_SQL_TIMEOUT;

  private NativeLong handle;
  private Ace32Wrapper ace32Wrapper;

  public AdsConnection(String path, String userName, String password, short serverTypes, int options) {
    ace32Wrapper = new Ace32Wrapper();
    handle = null;
    this.path = path;
    this.userName = userName;
    this.password = password;
    this.serverTypes = serverTypes;
    this.options = options;
  }

  public AdsConnection(String path, String userName, String password) {
    ace32Wrapper = new Ace32Wrapper();
    handle = null;
    this.path = path;
    this.userName = userName;
    this.password = password;
    this.serverTypes = getDefaultServerTypes();
    this.options = getDefaultOptions();
  }


  public void connect() {
    if (handle != null) {
      throw new RuntimeException("New connections is imposible without close previous");
    }
    handle = ace32Wrapper.AdsConnect60(path, serverTypes, userName, password, options);
    if (sqlTimeout != ADS_DEFAULT_SQL_TIMEOUT) {
      ace32Wrapper.AdsSetSQLTimeout(handle, new NativeLong(sqlTimeout));
    }
    ace32Wrapper.AdsSetDateFormat60(handle,"dd.mm.yyyy");
  }

  public void disconnect() {
    if (handle  != null) {
      ace32Wrapper.AdsDisconnect(handle);
    }
    handle = null;
  }

  public static short getDefaultServerTypes() {
    return Ace32.ADS_REMOTE_SERVER + Ace32.ADS_LOCAL_SERVER;
  }

  public static short getRemoteServerType() {
    return Ace32.ADS_REMOTE_SERVER;
  }

  public static int getDefaultOptions() {
    int retval = ADS_DEFAULT + ADS_COMPRESS_INTERNET;
    return retval;
  }

  public static int getAllOptions() {
    int retval = ADS_DEFAULT
        + ADS_INC_USERCOUNT
        + ADS_STORED_PROC_CONN
        + ADS_COMPRESS_INTERNET
//        + ADS_COMPRESS_ALWAYS
//        + ADS_COMPRESS_NEVER
        + ADS_UDP_IP_CONNECTION
        + ADS_IPX_CONNECTION
        + ADS_TCP_IP_CONNECTION;
//        + ADS_TLS_CONNECTION;
    return retval;
  }

  public String AdsGetConnectionPath() {
    if (handle != null ) {
      return ace32Wrapper.AdsGetConnectionPath(handle);
    } else {
      return "";
    }
  }

  public short AdsGetConnectionType(){
    if (handle != null ) {
      return ace32Wrapper.AdsGetConnectionType(handle);
    } else {
      return -1;
    }
  }

  public void closeCachedTables() {
    if (handle != null) {
      ace32Wrapper.AdsCloseCachedTables(handle);
    }
  }

  public void setDefaultSettings() {
    ace32Wrapper.AdsCacheOpenTables((short)0);
    ace32Wrapper.AdsCacheOpenCursors((short)25);
  }

  @Override
  public void close() throws IOException {
    disconnect();
  }

  public boolean isConnected() {
    return handle != null;
  }
}
