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
    private boolean isDictionaryConn = false;
    private AdsConnectOptions adsConnectOptions;

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
        isDictionaryConn = path.trim().endsWith(".add");
        createDefaultAdsConnectionOptions();
    }

    public AdsConnection(String path, String userName, String password, short serverTypes, int options, AdsConnectOptions adsConnectOptions) {
        ace32Wrapper = new Ace32Wrapper();
        handle = null;
        this.path = path;
        this.userName = userName;
        this.password = password;
        this.serverTypes = serverTypes;
        this.options = options;
        isDictionaryConn = path.trim().endsWith(".add");
        this.adsConnectOptions = adsConnectOptions;
    }


    private void createDefaultAdsConnectionOptions() {
        adsConnectOptions = new AdsConnectOptions();
        adsConnectOptions.setAdsCharSet(AdsConnectOptions.TADSCharSet.ADS_ANSI);
        adsConnectOptions.setAdsTableType(AdsConnectOptions.TADSTableType.ADS_ADT);
        adsConnectOptions.setRemoteConnect(true);
        adsConnectOptions.setLocalConnect(false);
        adsConnectOptions.setInternetConnect(false);
        adsConnectOptions.setDateFormat("dd.mm.yyyy");
        adsConnectOptions.setAdsCompressionType(AdsConnectOptions.TADSCompressionType.ADS_COMPRESSION_INTERNET);
        adsConnectOptions.setAdsCommunicationType(AdsConnectOptions.TADSCommunicationType.ADS_TCP_IP);
    }

    public AdsConnection(String path, String userName, String password) {
        ace32Wrapper = new Ace32Wrapper();
        handle = null;
        this.path = path;
        this.userName = userName;
        this.password = password;
        this.serverTypes = getDefaultServerTypes();
        this.options = getDefaultOptions();
        isDictionaryConn = path.trim().endsWith(".add");
        createDefaultAdsConnectionOptions();
    }


    public void connect() {
        if (handle != null) {
            throw new RuntimeException("New connections is imposible without close previous");
        }
        handle = ace32Wrapper.AdsConnect101(getConnectionString());

        //handle = ace32Wrapper.AdsConnect60(path, serverTypes, userName, password, options);
        if (sqlTimeout != ADS_DEFAULT_SQL_TIMEOUT) {
            ace32Wrapper.AdsSetSQLTimeout(handle, new NativeLong(sqlTimeout));
        }
        ace32Wrapper.AdsSetDateFormat60(handle, "dd.mm.yyyy");
    }

    public void disconnect() {
        if (handle != null) {
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
        if (handle != null) {
            return ace32Wrapper.AdsGetConnectionPath(handle);
        } else {
            return "";
        }
    }

    public short AdsGetConnectionType() {
        if (handle != null) {
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
        ace32Wrapper.AdsCacheOpenTables((short) 0);
        ace32Wrapper.AdsCacheOpenCursors((short) 25);
    }

    @Override
    public void close() throws IOException {
        disconnect();
    }

    public boolean isConnected() {
        return handle != null;
    }

    public String getConnectionString() {
        String strCommType = "";
        String strCipherSuite = "";
        String strCompression = "";
        String strEncryptionType = "";
        String strServerType = "";
        String strAliasTableType = "";
        StringBuilder sb = new StringBuilder("");

        //{ Start with the connection path - use only first 255 chars to maintain consistency w/ AdsConnect60 behavior}
        sb.append(String.format("Data Source=%.255s;", path));

        //{ Add the Username and Password -- but only if connect path is to a DD }
        if (isDictionaryConn && !userName.isEmpty()) {
            sb.append(String.format("User ID=%s;", userName));
        }
        if (!password.isEmpty()) {
            sb.append(String.format("Password=\"%s\";", password));
        }

    /*{ Always include the FIPS mode just because it's easy. }
      {$IFDEF ADSDELPHI6_OR_NEWER}
      Result := Result + Format( 'FIPS=%s;', [ BoolToStr( FEncryptionOptions.FIPSMode, True ) ] );
      {$ELSE}
      if FEncryptionOptions.FIPSMode then
      Result := Result + 'FIPS=TRUE;'
   else
      Result := Result + 'FIPS=FALSE;';
      {$ENDIF}

      { The remaining properties can be set in no particular order }
      if FEncryptionOptions.DataDictionaryPassword <> '' then
      Result := Result + Format( 'DDPassword=%s;', [ FEncryptionOptions.DataDictionaryPassword ] );

      if FEncryptionOptions.DataEncryptionType <> etAdsDefault then
            begin
      if FEncryptionOptions.DataEncryptionType = etAdsAES128 then
      strEncryptionType := 'AES128'
      else if FEncryptionOptions.DataEncryptionType = etAdsAES256 then
      strEncryptionType := 'AES256'
      else if FEncryptionOptions.DataEncryptionType = etAdsRC4 then
      strEncryptionType := 'RC4'
      else
      // This should not be possible, and is a real error
      strEncryptionType := 'NO_ENCRYPT_TYPE';

      Result := Result + Format( 'EncryptionType=%s;', [ strEncryptionType ] );
      end;
*/
  /*    if FEncryptionOptions.TLSCertificate <> '' then
      Result := Result + Format( 'TLSCertificate=%s;', [ FEncryptionOptions.TLSCertificate ] );

      if FEncryptionOptions.TLSCommonName <> '' then
      Result := Result + Format( 'TLSCommonName=%s;', [ FEncryptionOptions.TLSCommonName ] );

      { The Cipher Suite is a comma/colon-delimited list of values }
      if FEncryptionOptions.TLSCipherSuite <> [] then
      begin
      strCipherSuite := '';
      if tlsAES128SHA in FEncryptionOptions.TLSCipherSuite then
      strCipherSuite := strCipherSuite + ADS_CIPHER_SUITE_STRING_AES128 + ':';
      if tlsAES256SHA in FEncryptionOptions.TLSCipherSuite then
      strCipherSuite := strCipherSuite + ADS_CIPHER_SUITE_STRING_AES256 + ':';
      if tlsRC4MD5 in FEncryptionOptions.TLSCipherSuite then
      strCipherSuite := strCipherSuite + ADS_CIPHER_SUITE_STRING_RC4 + ':';

      // trim the final colon off of strCipherSuite
      Delete( strCipherSuite, Length( strCipherSuite ), 1 );

      Result := Result + Format( 'TLSCiphers=%s;', [ strCipherSuite ] );
      end;
*/
  /*    { Only one communication type will be specified }
      if FAdsCommunicationType <> ctAdsDefault then
            begin
      if FAdsCommunicationType = ctAdsUDPIP then
      strCommType := 'UDP_IP'
      else if FAdsCommunicationType = ctAdsIPX then
      strCommType := 'IPX'
      else if FAdsCommunicationType = ctAdsTCPIP then
      strCommType := 'TCP_IP'
      else if FAdsCommunicationType = ctAdsTLS then
      strCommType := 'TLS'
      else
      // This should not be possible, and is a real error
      strCommType := 'NO_COMM_TYPE';
*/
        String strComType = "TCP_IP";
        if (adsConnectOptions.getAdsCommunicationType().equals(AdsConnectOptions.TADSCommunicationType.ADS_UDP_IP)) {
            strComType = "UDP_IP";
        } else if (adsConnectOptions.getAdsCommunicationType().equals(AdsConnectOptions.TADSCommunicationType.ADS_IPX)) {
            strComType = "IPX";
        } else {
            strComType = "TCP_IP";
        }


        sb.append(String.format("CommType=%s;", strComType));
//      end;

      /*{ The remainder of the options are existing (non-encryption) options we need to use}
      { Compression: }
      if FAdsCompression <> ccAdsCompressionNotSet then
            begin
      if FAdsCompression = ccAdsCompressInternet then
      strCompression := 'INTERNET'
      else if FAdsCompression = ccADsCompressAlways then
      strCompression := 'ALWAYS'
      else if FAdsCompression = ccAdsCompressNever then
      strCompression := 'NEVER'
      else
      // This should not be possible, and is a real error.
      strCompression := 'COMPRESSION_NOT_SET';

      Result := Result + Format( 'Compression=%s;', [ strCompression ] );
      end;
      */
      //String strCompression = "";
      if (adsConnectOptions.getAdsCompressionType().equals(AdsConnectOptions.TADSCompressionType.ADS_COMPRESSION_INTERNET)) {
          strCompression = "INTERNET";
      } else if (adsConnectOptions.getAdsCompressionType().equals(AdsConnectOptions.TADSCompressionType.ADS_COMPRESSION_ALWAYS)) {
          strCompression = "ALWAYS";
      } else {
          strCompression = "NEVER";
      }
      sb.append(String.format("Compression=%s;", strCompression));

      /*{ SQLTimeout: }
      if FSQLTimeout <> ACE.ADS_DEFAULT_SQL_TIMEOUT then
      Result := Result + Format( 'SQLTimeout=%d;', [ FSQLTimeout ] );

      { MiddleTierConnection: }
      if FMiddleTierConn then
      Result := Result + 'IncrementUserCount=TRUE;';

      { StoredProcedureConnection: }
      if FStoredProcConn then
      Result := Result + 'StoredProcedureConnection=TRUE;';
      */
        //{ ServerType }
        StringBuilder sbServerType = new StringBuilder("");
        if (adsConnectOptions.isLocalConnect()) sbServerType.append("LOCAL|");
        if (adsConnectOptions.isRemoteConnect()) sbServerType.append("REMOTE|");
        if (adsConnectOptions.isInternetConnect()) sbServerType.append("INTERNET|");
        String serverType = sbServerType.toString();
        sb.append(String.format("ServerType=%s;", serverType.substring(0,serverType.length()-1)));
        //{ DateFormat }
        //if FDateFormat <> '' then
        //Result := Result + Format( 'DateFormat=%s;', [ DateFormat ] );
        sb.append(String.format("DateFormat=%s;", adsConnectOptions.getDateFormat()));

      /*{ Table Type (from alias) }
      if meAliasTableType <> ttAdsConnectUnspecified then
            begin
      if meAliasTableType = ttAdsConnectCDX then
      strAliasTableType := 'CDX'
      else if meAliasTableType <> ttAdsConnectNTX then
      strAliasTableType := 'NTX'
      else if meAliasTableType <> ttAdsConnectADT then
      strAliasTableType := 'ADT'
      else if meAliasTableType = ttAdsConnectVFP then
      strAliasTableType := 'VFP'
      else
      // This should not be possible, and is a real error.
      strAliasTableType := 'ALIAS_TABLE_TYPE_NOT_SET';

      Result := Result + Format( 'TableType=%s;', [ strAliasTableType ] );
      end;*/
        if (adsConnectOptions.getAdsTableType()==AdsConnectOptions.TADSTableType.ADS_DBF) {
            sb.append(String.format("TableType=%s;", "VFP"));
        } else {
            sb.append(String.format("TableType=%s;", "ADT"));
        }

      /*{* Add on the ExtraConnectString options *}
      if FExtraConnectString <> '' then
      Result := Result + FExtraConnectString;
      */
        return sb.toString();
    }
}
