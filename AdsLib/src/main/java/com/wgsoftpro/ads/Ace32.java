package com.wgsoftpro.ads;

import lombok.SneakyThrows;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class Ace32 {
  public static final short ADS_MAX_ERROR_LEN = 600;
  public static final short MAX_DATA_LEN = 255;

  private Ace32Native ace32;
  private AceWrapper wrapper;

  public Ace32() {
    prepareLib();
    System.out.println(getAce32LibName());
    Ace32Native.init(getAce32LibName());

  }

  public String test(){
    return "Welcome";
  }

  @SneakyThrows
  public void prepareLib() {
   // URL baseUrl = getClass().getResource(".");
   // System.out.println(baseUrl);
    String resourcePath = isWindowsOS() ? "/Libraries/Win/64/" : "/Libraries/Nix/64/";
    URI uri = getClass().getResource(resourcePath).toURI();
    System.out.println(uri);

    Path myPath;
    if (uri.getScheme().equals("jar")) {
      FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
      myPath = fileSystem.getPath(resourcePath);
    } else {
      myPath = Paths.get(uri);
    }

  //  Path path = Paths.get(uri);
    List<Path> flist = getEntries(myPath);
    flist.forEach(item -> {unPackResource(item);
    System.out.println(item.getFileName());});

  }

  private void getFile(Path path) {

  }

  public List<Path> getEntries(final Path dir) throws IOException {
    final List<Path> entries = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
      for (final Iterator<Path> it = stream.iterator(); it.hasNext();) {
        entries.add(it.next());
      }
    }
    return entries;
  }

  @SneakyThrows
  public void unPackResource(Path path) {
    InputStream in = Files.newInputStream(path);
    byte[] buffer = new byte[1024];
    int read = -1;
    //File temp = new File(SystemProperties.getProperty("io.temp"));
    Path newFile = Paths.get(System.getProperty("java.io.tmpdir")+"/"+path.getFileName());
    BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(newFile));
    while((read = in.read(buffer)) != -1) {
      bos.write(buffer, 0, read);
    }
    bos.close();

    //System.load(temp.getAbsolutePath());
  }

  public static boolean isWindowsOS(){
    String os = System.getProperty("os.name").toUpperCase();
    return os.contains("WINDOWS");
  }


  private String getAce32LibName() {
    if (isWindowsOS()) {
      return System.getProperty("java.io.tmpdir")+"/"+"ace64";
    } else {
      return System.getProperty("java.io.tmpdir")+"/"+"libace.so.11.10.0.24";
    }
  }

}
