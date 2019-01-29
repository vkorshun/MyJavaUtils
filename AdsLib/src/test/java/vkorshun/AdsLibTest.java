package vkorshun;

import com.wgsoftpro.ads.Ace32;
import com.wgsoftpro.ads.Ace32Native;
import com.wgsoftpro.ads.Ace32Wrapper;
import com.wgsoftpro.ads.AdsClasses.AdsConnection;
import com.wgsoftpro.ads.AdsClasses.AdsQuery;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.Timestamp;

public class AdsLibTest {

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println(System.getProperty("java.io.tmpdir"));
        System.out.println(System.getProperty("os.name"));
        testPath("/Libraries/Win/64/");
        System.out.println("Ok");
    }

    @SneakyThrows
    public static void testPath(String resourcePath) throws URISyntaxException {
        Ace32 ace32 = new Ace32();
        //ace32.prepareLib();
        //Ace32Native.init(System.getProperty("java.io.tmpdir")+"ace64");
        try {
            //AceWrapper wrapper = new AceWrapper();
            System.out.println(new Ace32Wrapper().AdsGetVersion());
        } finally {
            Ace32Native.destroy();
        }
        //ace32.prepareLib(resourcePath);
    }

    public static void loadJarDll(String name) throws IOException {
        InputStream in = AdsLibTest.class.getResourceAsStream(name);
        byte[] buffer = new byte[1024];
        int read = -1;
        File temp = File.createTempFile(name, "");
        FileOutputStream fos = new FileOutputStream(temp);

        while ((read = in.read(buffer)) != -1) {
            fos.write(buffer, 0, read);
        }
        fos.close();
        in.close();

        System.load(temp.getAbsolutePath());
    }

    @Test
    public void adsLibTest() throws IOException {
        Ace32 ace32 = new Ace32();
        int n = getWebLicenseCount("//192.168.0.50:6262/VKFIN/COMMON/INSTALL", "adssys", "air");
        System.out.println(String.format(" COUNT = %d", n));

        try (AdsConnection adsConn = new AdsConnection("D:/VKFIN/universal-7.add", "adssys", "air")) {
            adsConn.connect();
            if (adsConn.isConnected()) {
                //System.out.println("connect" + adsConn.AdsGetConnectionPath());
                try (AdsQuery _query = new AdsQuery(adsConn)) {
                    _query.setQuery("SELECT COUNT(*) AS kol FROM paroll WHERE webaccess=true AND deleted=false");
                    //_query.setParam("kodkli",0);
                    _query.execute();
                    if (!_query.isEmpty()) {
                        int usedLic = _query.getFieldValue("kol").asInteger();
                        System.out.println(" LIC COUNT " + usedLic + " " + usedLic);
                    }
                } finally{
                }

            }

        }
    }




    private int getWebLicenseCount(String licensePath, String userName, String password) throws IOException {
        String cPassword = "";
        int retval = 0;
        try (AdsConnection adsConn = new AdsConnection(licensePath, userName, password)) {
            adsConn.connect();
            if (adsConn.isConnected()) {
                try (AdsQuery _query = new AdsQuery(adsConn)) {
                    _query.setQuery("SELECT * FROM \"install.db7\"");
                    //_query.setParam("kodkli",0);
                    _query.execute();
                    if (!_query.isEmpty()) {
                        String sn = _query.getFieldValue("serialnumber").asString();
                        Timestamp _d = _query.getFieldValue("date").asTimestamp();
                        cPassword = StringUtils.left(sn, 3) + StringUtils.charMix(sn, StringUtils.dtos(_d));
                        //SpCrypt spCrypt = new SpCrypt();
                        cPassword = spCrypt(cPassword, getCheckString(), null);
                    }
//          System.out.println(_query.getFieldValue("name").asString()+_query.getFieldValue("kodkli").asLong().toString());
                    _query.close();
                    _query.setPassword("softpro.db7", cPassword);
                    _query.setQuery("SELECT * FROM \"softpro.db7\"");
                    _query.execute();
                    _query.goBottom();
                    retval = _query.getFieldValue("weblicense").asInteger();
                    // _query.close();
                }
                //adsConn.disconnect();
            }
        }
        return retval;
    }

    private String spCrypt(String cString, String cKeyString, String cParoll) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (cParoll == null) {
            cParoll = getDefault();
        }
        cKeyString = cKeyString.trim();
        if (cKeyString.length() < cString.length()) {
            cKeyString = StringUtils.replicate(cKeyString, Math.round((float) cString.length() / (float) cKeyString.length() + 0.5));
        }
        String cKeyShift = StringUtils.replicate(cParoll, Math.round(cString.length() / 7.0 + 0.5));
        byte[] keyBytes = cKeyString.getBytes("cp1251");
        byte[] shiftBytes = cKeyShift.getBytes("cp1251");
        byte[] strBytes = cString.getBytes("cp1251");
        byte[] retval = new byte[cString.length()];
        for (int i = 0; i < cString.length(); ++i) {
            retval[i] = (byte) (keyBytes[i] + (i < shiftBytes.length ? shiftBytes[i] : 0) - strBytes[i]); //sb.append(new String(cKeyString.getBytes("cp1251")[i]+cKeyShift.getBytes("cp1251")[i]-cString.getBytes("cp1251")[i],"cp1251"));
        }
        return new String(retval, "cp1251");
    }

    private byte getCheckByte(int i) {
        byte[] _all = {40, 50};
        int retval = 0;
        switch (i) {
            case 0:
            case 5:
                retval = _all[1] + 4;
                break;
            case 1:
            case 11:
                retval = _all[1];
                break;
            case 2:
            case 13:
                retval = _all[1] - 1;
                break;
            case 3:
            case 4:
            case 12:
            case 14:
            case 15:
            case 17:
                retval = _all[1] + 1;
                break;
            case 6:
            case 8:
                retval = _all[1] + 2;
                break;
            case 7:
            case 18:
                retval = _all[1] + 3;
                break;
            case 9:
                retval = _all[1] + 6;
                break;
            case 10:
            case 19:
                retval = _all[1] + 5;
                break;
            case 16:
                retval = _all[0] + 9;
                break;

        }
        return (byte) retval;
    }

    private String getCheckString() {
        byte[] _b2 = new byte[20];
        for (int i = 0; i < _b2.length; ++i) {
            _b2[i] = getCheckByte(i);
        }
        return new String(_b2);
    }

    private byte getDefaultByte(int i) {
        byte[] _all = {70, 80, 100};
        byte[] _next = {10, 3, 2, 11, 14, 16};
        int j = i;
        switch (i) {
            case 0:
                j = 1;
                break;
            case 1:
            case 6:
                j = 3;
                break;
            case 3:
                j = 5;
                break;
            case 4:
                j = 0;
                break;
            case 5:
                j = 4;
                break;
        }

        int k = j;
        if (k >= 2) {
            k = 2;
        }
        return (byte) (_next[j] + _all[k]);
    }

    private String getDefault() {
        byte[] _default = new byte[7];
        for (int i = 0; i < _default.length; ++i) {
            _default[i] = getDefaultByte(i);
        }
        return new String(_default);
    }

}
