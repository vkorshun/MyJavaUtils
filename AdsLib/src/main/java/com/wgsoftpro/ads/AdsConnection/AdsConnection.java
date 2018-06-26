package com.wgsoftpro.ads.AdsConnection;

import com.wgsoftpro.ads.Ace32;
import com.wgsoftpro.ads.Ace32Wrapper;
import lombok.Getter;

import static com.wgsoftpro.ads.Ace32.*;

@Getter
public class AdsConnection {
  private String path;
  private String userName;
  private String password;
  private boolean isConnected;
  private short serverTypes;
  private int options;
  private int sqlTimeout = ADS_DEFAULT_SQL_TIMEOUT;

  private int handle;

  public AdsConnection(String path, String userName, String password, short serverTypes, int options) {
    this.path = path;
    this.userName = userName;
    this.password = password;
    this.serverTypes = serverTypes;
    this.options = options;
  }

  public AdsConnection(String path, String userName, String password) {
    this.path = path;
    this.userName = userName;
    this.password = password;
    this.serverTypes = getDefaultServerTypes();
    this.options = getDefaultOptions();
  }


  public void connect() {
    if (isConnected) {
      throw new RuntimeException("New connections is imposible without close previous");
    }
    handle = Ace32Wrapper.AdsConnect60(path, serverTypes, userName, password, options);
    isConnected = true;
    if (sqlTimeout != ADS_DEFAULT_SQL_TIMEOUT) {
      Ace32Wrapper.AdsSetSQLTimeout(handle, sqlTimeout);
    }
  }

  public void disconnect() {

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

}
