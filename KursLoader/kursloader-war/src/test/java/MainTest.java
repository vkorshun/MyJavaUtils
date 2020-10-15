import com.wgsoftpro.beans.KursLoad.KursLoaderRU;
import com.wgsoftpro.beans.KursLoad.KursLoaderUKR;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MainTest {

   @Before
   public void setProperties() throws IOException {
      InputStream is = getClass().getResourceAsStream("test.conf");
      System.getProperties().load(is);
   }

   @Test
   public void rusKursLoadTest() throws ParseException {
      String d1 = "20082020";
      String d2 = "27082020";
      SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
      Timestamp ts1 = new Timestamp(df.parse(d1).getTime());
      Timestamp ts2 = new Timestamp(df.parse(d2).getTime());

//      Timestamp curDate = new Timestamp(System.currentTimeMillis());
      KursLoaderRU kurs = new KursLoaderRU(ts1, ts2);
      kurs.checkKurs();

   }
}
