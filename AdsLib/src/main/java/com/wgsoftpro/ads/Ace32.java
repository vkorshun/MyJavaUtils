package com.wgsoftpro.ads;

import lombok.SneakyThrows;

import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class Ace32 {
  public static final short ADS_MAX_ERROR_LEN = 600;
  public static final short MAX_DATA_LEN = 255;
  public static final short ADS_MAX_PATH = 260;

  public static final short ADS_LOCAL_SERVER = 0x0001;
  public static final short ADS_REMOTE_SERVER = 0x0002;
  public static final short ADS_AIS_SERVER = 0x0004;

  public static final short ADS_CONNECTION = 1;
  public static final short ADS_TABLE = 2;
  public static final short ADS_INDEX_ORDER = 3;
  public static final short ADS_STATEMENT = 4;
  public static final short ADS_CURSOR = 5;
  public static final short ADS_DATABASE_CONNECTION = 6;
  public static final short ADS_SYS_ADMIN_CONNECTION = 7;
  public static final short ADS_FTS_INDEX_ORDER = 8;

  public static final short ADS_DEFAULT_SQL_TIMEOUT = 0x0000;
  public static final short ADS_DEFAULT = 0;

  //* options for connecting to Advantage servers - can be ORed together
  public static final int ADS_INC_USERCOUNT = 0x00000001;
  public static final int ADS_STORED_PROC_CONN = 0x00000002;
  public static final int ADS_COMPRESS_ALWAYS = 0x00000004;
  public static final int ADS_COMPRESS_NEVER = 0x00000008;
  public static final int ADS_COMPRESS_INTERNET = 0x0000000C;
  public static final int ADS_REPLICATION_CONNECTION = 0x00000010;
  public static final int ADS_UDP_IP_CONNECTION = 0x00000020;
  public static final int ADS_IPX_CONNECTION = 0x00000040;
  public static final int ADS_TCP_IP_CONNECTION = 0x00000080;
  public static final int ADS_TCP_IP_V6_CONNECTION = 0x00000100;
  public static final int ADS_NOTIFICATION_CONNECTION = 0x00000200;
  // Reserved                         0x00000400
  // Reserved                         0x00000800
  public static final int ADS_TLS_CONNECTION = 0x00001000;
  public static final int ADS_CHECK_FREE_TABLE_ACCESS = 0x00002000;

  // options for opening/create tables - can be ORed together
  public static final int ADS_EXCLUSIVE = 0x00000001;
  public static final int ADS_READONLY = 0x00000002;
  public static final int ADS_SHARED = 0x00000004;
  public static final int ADS_CLIPPER_MEMOS = 0x00000008;
  public static final int ADS_TABLE_PERM_READ = 0x00000010;
  public static final int ADS_TABLE_PERM_UPDATE = 0x00000020;
  public static final int ADS_TABLE_PERM_INSERT = 0x00000040;
  public static final int ADS_TABLE_PERM_DELETE = 0x00000080;
  public static final int ADS_REINDEX_ON_COLLATION_MISMATCH = 0x00000100;
  public static final int ADS_IGNORE_COLLATION_MISMATCH = 0x00000200;
  public static final int ADS_FREE_TABLE = 0x00001000; // Mutually exclusive with ADS_DICTIONARY_BOUND_TABLE
  public static final int ADS_TEMP_TABLE = 0x00002000; // Mutually exclusive with ADS_DICTIONARY_BOUND_TABLE
  public static final int ADS_DICTIONARY_BOUND_TABLE = 0x00004000; // Mutually exclusive with ADS_FREE_TABLE or ADS_TEMP_TABLE
  public static final int ADS_CACHE_READS = 0x20000000; // Enable caching of reads on the table
  public static final int ADS_CACHE_WRITES = 0x40000000; // Enable caching of reads & writes on the table

  //{* locking compatibility *}
  public static final short ADS_COMPATIBLE_LOCKING = 0;
  public static final short ADS_PROPRIETARY_LOCKING = 1;

  //{* Supported file types *}
  public static final short ADS_DATABASE_TABLE = ADS_DEFAULT;
  public static final short ADS_NTX = 1;
  public static final short ADS_CDX = 2;
  public static final short ADS_ADT = 3;
  public static final short ADS_VFP = 4;

  //{* character set types *}
  public static final short ADS_ANSI = 1;
  public static final short ADS_OEM = 2;

  private Ace32Native ace32;
  private Ace32Wrapper wrapper;

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
