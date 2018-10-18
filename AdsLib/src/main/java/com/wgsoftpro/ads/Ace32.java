package com.wgsoftpro.ads;

import com.wgsoftpro.ads.ResourceExtractor.AdsResourceExtractor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


@Slf4j
public class Ace32 {
  private static String installPath;
  private static boolean isLibraryLoaded;
  private static String libraryLoadErrorDescription;

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

  /* locking compatibility */
  public static final short ADS_COMPATIBLE_LOCKING = 0;
  public static final short ADS_PROPRIETARY_LOCKING = 1;

  /* Supported file types */
  public static final short ADS_DATABASE_TABLE = ADS_DEFAULT;
  public static final short ADS_NTX = 1;
  public static final short ADS_CDX = 2;
  public static final short ADS_ADT = 3;
  public static final short ADS_VFP = 4;

  /* character set types */
  public static final short ADS_ANSI = 1;
  public static final short ADS_OEM = 2;

  /* Logical constants */
  public static final short ADS_FALSE = 0;
  public static final short ADS_TRUE = 1;
  public static final int MSEC_PER_DAY = 24 * 60 * 60 * 1000;

  /* data types */
  public static final short ADS_TYPE_UNKNOWN = 0;
  public static final short ADS_LOGICAL = 1; ///* 1 byte logical value */
  public static final short ADS_NUMERIC = 2; ///* DBF character style numeric */
  /* Date field.  With ADS_NTX, ADS_CDX, and
   * ADS_VFP< this is an 8 byte field of the form
   * CCYYMMDD.  With ADS_ADT, it is a 4 byte
   * Julian date. */
  public static final short ADS_DATE = 3;
  public static final short ADS_STRING = 4;// /* Character data */
  public static final short ADS_MEMO = 5;// /* Variable length character data */

  /* the following are extended data types */
  public static final short ADS_BINARY = 6; /* BLOB - any data */
  public static final short ADS_IMAGE = 7; /* BLOB - bitmap */
  public static final short ADS_VARCHAR = 8; /* variable length character field */
  public static final short ADS_COMPACTDATE = 9; /* DBF date represented with 3 bytes */
  public static final short ADS_DOUBLE = 10; /* IEEE 8 byte floating point */
  public static final short ADS_INTEGER = 11; /* IEEE 4 byte signed long integer */

  /* the following are supported with the ADT format */
  public static final short ADS_SHORTINT = 12; /* IEEE 2 byte signed short integer */
  /* 4 byte long integer representing
   * milliseconds since midnight */
  public static final short ADS_TIME = 13;
  /* 8 bytes.  High order 4 bytes are a
   * long integer representing Julian date.
   * Low order 4 bytes are a long integer
   * representing milliseconds since
   * midnight */
  public static final short ADS_TIMESTAMP = 14;
  public static final short ADS_AUTOINC = 15; /* 4 byte auto-increment value */
  public static final short ADS_RAW = 16; /* Untranslated data */
  public static final short ADS_CURDOUBLE = 17; /* IEEE 8 byte floating point currency */
  public static final short ADS_MONEY = 18; /* 8 byte, 4 implied decimal Currency Field */
  public static final short ADS_LONGLONG = 19; /* 8 byte integer */
  public static final short ADS_CISTRING = 20; /* CaSe INSensiTIVE character data */
  public static final short ADS_ROWVERSION = 21; /* 8 byte integer, incremented for every update, unique to entire table */
  public static final short ADS_MODTIME = 22; /* 8 byte timestamp, updated when record is updated */
  public static final short ADS_VARCHAR_FOX = 23; /* Visual FoxPro varchar field */
  public static final short ADS_VARBINARY_FOX = 24; /* Visual FoxPro varbinary field */
  public static final short ADS_SYSTEM_FIELD = 25; /* For internal usage */
  public static final short ADS_NCHAR = 26; /* Unicode Character data */
  public static final short ADS_NVARCHAR = 27; /* Unpadded Unicode Character data */
  public static final short ADS_NMEMO = 28; /* Variable Length Unicode Data */

  /* Options for returning string values */
  public static final short ADS_NONE = 0x0000;
  public static final short ADS_LTRIM = 0x0001;
  public static final short ADS_RTRIM = 0x0002;
  public static final short ADS_TRIM = 0x0003;
  public static final short ADS_GET_UTF8 = 0x0004;
  public static final short ADS_DONT_CHECK_CONV_ERR = 0x0008;
  public static final short ADS_GET_FORMAT_ANSI = 0x0010;
  public static final short ADS_GET_FORMAT_WEB = 0x0030;

  //private Ace32Native ace32;
  //private Ace32Wrapper wrapper;
  //private static volatile int isInit = 0;

  //static {
  //  prepareLib();
    //Ace32Native.init(getAce32LibName());
  //}

  public Ace32() {
    //prepareLib();
    //System.out.println(getAce32LibName());
    Ace32Native.init(getAce32LibName());

  }

  public String test() {
    return "Welcome+";
  }

  @SneakyThrows
  public static void prepareLib() {
    // URL baseUrl = getClass().getResource(".");
    // System.out.println(baseUrl);
    FileSystem fileSystem = null;
    String resourcePath = isWindowsOS() ? "/Libraries/Win/64/" : "/Libraries/Nix/64/";
    try {
      URI uri = Ace32.class.getResource(resourcePath).toURI();
      //System.out.println(uri);

      Path myPath;
      if (uri.getScheme().equals("jar")) {
        fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
        myPath = fileSystem.getPath(resourcePath);

      } else {
        myPath = Paths.get(uri);
      }

      //  Path path = Paths.get(uri);
      List<Path> flist = getEntries(myPath);
      flist.forEach(item -> {
        unPackResource(item);
      });
    } finally {
      if (fileSystem != null) {
        fileSystem.close();
      }
    }
  }

  private void getFile(Path path) {

  }

  public static List<Path> getEntries(final Path dir) throws IOException {
    final List<Path> entries = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
      for (final Iterator<Path> it = stream.iterator(); it.hasNext(); ) {
        entries.add(it.next());
      }
    }
    return entries;
  }

  @SneakyThrows
  public static void unPackResource(Path path) {
    try (InputStream in = Files.newInputStream(path)) {
      byte[] buffer = new byte[1024];
      int read = -1;
      //File temp = new File(SystemProperties.getProperty("io.temp"));
      Path newFile = Paths.get(System.getProperty("java.io.tmpdir") + "/" + path.getFileName());
      //if (Files.notExists(newFile)) {
      try (OutputStream os = Files.newOutputStream(newFile); BufferedOutputStream bos = new BufferedOutputStream(os)) {
        while ((read = in.read(buffer)) != -1) {
          bos.write(buffer, 0, read);
        }
      }
      //}
      //bos.close();
    }
    //System.load(temp.getAbsolutePath());
  }

  public static boolean isWindowsOS() {
    String os = System.getProperty("os.name").toUpperCase();
    return os.contains("WINDOWS");
  }


  //private static String getAce32FullLibName() {
    //if (isWindowsOS()) {
    //  return System.getProperty("java.io.tmpdir") + "/" + "ace64";
    //} else {
    //  return System.getProperty("java.io.tmpdir") + "/" + "libace.so.11.10.0.24";
    //}
    //return installPath
  //}

  private static String getAce32LibName() {
    if (AdsResourceExtractor.IsWindows()) {
      return  installPath+"/ace64";
    } else {
      return  installPath+"/libace.so.11.10.0.24";
    }
  }

  private static String getAce32ShortLibName() {
    if (AdsResourceExtractor.IsWindows()) {
      return  "ace64.dll";
    } else {
      return  "libace.so.11.10.0.24";
    }
  }

  static {
    try {
      installPath = AdsResourceExtractor.ExtractResources();
      //if (!AdsResourceExtractor.IsWindows() && !(new EndUserLibraryUtils()).SetCurrentDirectory(installPath)) {
      //  throw new Exception("ADS lib loading error");
      //}
      log.error(" INSTALL PATH ="+installPath);
      AdsResourceExtractor.LoadLibrary(getAce32ShortLibName());
      log.error("loaded");
      isLibraryLoaded = true;
    } catch (Exception ex) {
      libraryLoadErrorDescription = ex.getMessage();
      log.error("ACE32 LOAD ERROR " + libraryLoadErrorDescription);
      isLibraryLoaded = false;
    }

  }

}
