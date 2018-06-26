package vkorshun;

import com.wgsoftpro.ads.Ace32;
import com.wgsoftpro.ads.Ace32Native;
import com.wgsoftpro.ads.Ace32Wrapper;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

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
    ace32.prepareLib();
    Ace32Native.init(System.getProperty("java.io.tmpdir")+"ace64");
    try {
      //AceWrapper wrapper = new AceWrapper();
      System.out.println(Ace32Wrapper.AdsGetVersion());
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

    while((read = in.read(buffer)) != -1) {
      fos.write(buffer, 0, read);
    }
    fos.close();
    in.close();

    System.load(temp.getAbsolutePath());
  }
}
