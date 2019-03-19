import com.wgsoftpro.beans.KursLoad.KursLoaderRU;
import com.wgsoftpro.beans.KursLoad.KursLoaderUKR;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

public class MainTest {

   @Before
   public void setProperties() throws IOException {
      InputStream is = getClass().getResourceAsStream("test.conf");
      System.getProperties().load(is);
   }

   @Test
   public void rusKursLoadTest() {
      Timestamp curDate = new Timestamp(System.currentTimeMillis());
      KursLoaderRU kurs = new KursLoaderRU(curDate, curDate);
      kurs.checkKurs();

   }
}
