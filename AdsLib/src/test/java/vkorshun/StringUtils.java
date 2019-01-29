package vkorshun;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class StringUtils {
    public static String left(String s, int count){
        return s.substring(s.length()-count);
    }

    public static String charMix(String s1, String s2){
        int l1 = s1.length();
        int l2 = s2.length();
        int k = 0;
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<l1;i++){
            sb.append(s1.charAt(i));
            if (k<l2) {
                sb.append(s2.charAt(k));
            }
            k++;
            if (k==l2) {
                k = 0;
            }
        }
        return sb.toString();
    }

    public static String replicate(String s, long count){
        StringBuilder sb = new StringBuilder(s);
        for (int i=1;i<count;i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static String dtos(Timestamp d) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(d);
    }
}
