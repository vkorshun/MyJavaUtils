package com.wgsoftpro.ads.AdsClasses;

import lombok.Data;

@Data
public class AdsConnectOptions {
    private boolean isRemoteConnect;
    private boolean isLocalConnect;
    private boolean isInternetConnect;
    private TADSTableType adsTableType;
    private TADSCharSet adsCharSet;
    private TADSCompressionType adsCompressionType;
    private String dateFormat;
    private TADSCommunicationType adsCommunicationType;



    public enum TADSTableType {
        ADS_ADT,
        ADS_DBF
    }

    public enum TADSCharSet {
        ADS_ANSI,
        ADS_OEM
    }

    public enum TADSCompressionType {
        ADS_COMPRESSION_NEVE,
        ADS_COMPRESSION_ALWAYS,
        ADS_COMPRESSION_INTERNET
    }

    public enum TADSCommunicationType {
        ADS_UDP_IP,
        ADS_IPX,
        ADS_TCP_IP,
        ADS_TLS
    }
}
