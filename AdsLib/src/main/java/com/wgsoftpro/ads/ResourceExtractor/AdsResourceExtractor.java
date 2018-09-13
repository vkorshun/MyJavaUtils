package com.wgsoftpro.ads.ResourceExtractor;

import java.applet.Applet;
import java.io.*;
import java.net.URL;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class AdsResourceExtractor {
  private static boolean isWindows;
  private static boolean isSunOS;
  private static boolean isMacOS;
  private static boolean isFreeBSD;
  private static boolean isAIX;
  private static boolean isAndroid;
  private static boolean isARM;
  private static boolean isSPARC;
  private static boolean isX86;
  private static boolean isARM64;
  private static String path = "";
  private static boolean preLoad = false;
  private static boolean isExtractableResources = true;
  private static boolean loadFromPath = true;
  private static String version = "11.10.0.24";
  private static final Map winResourcesNames = Collections.unmodifiableMap(new HashMap() {
    {
      this.put("required", new String[]{"ace64.dll", "adscollate.adm", "adscollate.adt", "adsloc64.dll", "adslocal.cfg", "aicu64.dll", "ansi.chr", "axcws64.dll", "extend.chr", "icudt40l.dat"});
      //this.put("optional", new String[]{"CAGUI.dll", "KM.EKeyAlmaz1C.dll", "KM.EKeyCrystal1.dll", "KM.FileSystem.dll", "KM.TCTEllipseSCard.dll", "KM.PKCS11.dll", "PKCS11.EKeyAlmaz1C.dll", "PKCS11.EKeyCrystal1.dll", "DSTU4145Parameters.cap", "ECDHParameters.cap", "GOST28147SBox.cap", "PRNGParameters.cap", "RSAParameters.cap"});
    }
  });
  private static final Map nixResourcesNames = Collections.unmodifiableMap(new HashMap() {
    {
      this.put("required", new String[]{"libaicu.so", "libosi.so", "libadsloc.so.11.10.0.24", "libace.so.11.10.0.24", "icudt40l.dat", "extend.chr", "ansi.chr", "adslocal.cfg", "adscollate.adt", "adscollate.adm"});
//      this.put("optional", new String[]{"cagui.so", "libusb.so", "km.ekc1.so", "ekc1.so", "km.fs.so", "km.ncmg301.so", "ncmg301.so", "km.pkcs11.so", "pkcs11.ekc1.so", "pkcs11.eka1c.so", "osplm.ini", "DSTU4145Parameters.cap", "ECDHParameters.cap", "GOST28147SBox.cap", "PRNGParameters.cap", "RSAParameters.cap"});
    }
  });
  /*private static final Map macResourcesNames = Collections.unmodifiableMap(new HashMap() {
    {
      this.put("required", new String[]{"libEUSignJavaUtils.dylib", "osi.dylib", "libEUSignJava.dylib", "euscp.dylib", "cspb.dylib", "cspib.dylib", "cspe.dylib", "pkif.dylib", "km.dylib", "cac.dylib", "ldapc.dylib"});
      this.put("optional", new String[]{"cagui.dylib", "cagui.bundle.zip", "libusb.dylib", "km.ekc1.dylib", "ekc1.dylib", "km.fs.dylib", "km.pkcs11.dylib", "pkcs11.ekc1.dylib", "pkcs11.eka1c.dylib", "osplm.ini", "DSTU4145Parameters.cap", "ECDHParameters.cap", "GOST28147SBox.cap", "PRNGParameters.cap", "RSAParameters.cap"});
    }
  });
  private static final Map androidResourcesNames = Collections.unmodifiableMap(new HashMap() {
    {
      this.put("required", new String[]{"libEUSignJavaUtils.so", "libosi.so", "libEUSignJava.so", "libeuscp.so", "libcspb.so", "libcspib.so", "libcspe.so", "libpkif.so", "libkm.so", "libcac.so", "libldapc.so"});
      this.put("optional", new String[]{"libcagui.so", "libusb.so", "libkm.ekc1.so", "libekc1.so", "libkm.fs.so", "libkm.pkcs11.so", "libpkcs11.ekc1.so", "libpkcs11.eka1c.so", "osplm.ini", "DSTU4145Parameters.cap", "ECDHParameters.cap", "GOST28147SBox.cap", "PRNGParameters.cap", "RSAParameters.cap"});
    }
  });*/
  public static URL codebaseURL = null;

  public AdsResourceExtractor() {
  }

  private static boolean GetIsAndroidSafe() {
    boolean var0 = false;

    try {
      var0 |= System.getProperty("java.vendor").toLowerCase().indexOf("android") >= 0;
    } catch (Exception var4) {
      ;
    }

    try {
      var0 |= System.getProperty("java.runtime.name").toLowerCase().indexOf("android") >= 0;
    } catch (Exception var3) {
      ;
    }

    try {
      var0 |= System.getProperty("java.specification.vendor").toLowerCase().indexOf("android") >= 0;
    } catch (Exception var2) {
      ;
    }

    return var0;
  }

  private static String GetJarLibraryPath() {
    try {
      CodeSource var0 = AdsResourceExtractor.class.getProtectionDomain().getCodeSource();
      File var1 = new File(var0.getLocation().toURI().getPath());
      String var2 = var1.getParentFile().getPath();
      if (var2.charAt(var2.length() - 1) == '/') {
        var2 = var2.substring(0, var2.length() - 1);
      }

      return var2;
    } catch (Exception var3) {
      return null;
    }
  }

  private static String GetResourcesLocation() {
    String path;
    if (isWindows) {
      path = "/Libraries/Win";
    } else {
      path = "/Libraries/Nix";
    }

    if (!isX86) {
      path = path + "/64";
    }

    return path;
  }

  private static String GetLibrariesZipFileName() {
    String var0 = "AdsJavaLibs";
    if (isWindows) {
      var0 = var0 + ".Win";
    } else {
      var0 = var0 + ".Nix";
    }

    if (!isX86) {
      var0 = var0 + ".64";
    }

    return var0 + ".zip";

  }

  private static Map GetResources() {
    if (isWindows) {
      return winResourcesNames;
    } else {
      return nixResourcesNames;
    }
  }

  private static String[] GetResourcesNames() {
    Map var0 = GetResources();
    String[] var1 = (String[]) ((String[]) var0.get("required"));
    //String[] var2 = (String[])((String[])var0.get("optional"));
    //String[] var3 = new String[var1.length + var2.length];
    //System.arraycopy(var1, 0, var3, 0, var1.length);
    //System.arraycopy(var2, 0, var3, var1.length, var2.length);
    return var1;
  }

  private static boolean IsResourceExistsInJar(String var0) {
    InputStream var1 = AdsResourceExtractor.class.getResourceAsStream(var0);
    return var1 != null;
  }

  private static boolean IsResourceExistsInFileSystem(String var0) {
    File var1 = new File(var0);
    return var1.exists();
  }

  /*private static boolean CheckFileHash(String var0, String var1) {
    byte[] var2 = new byte[1024];

    try {
      FileInputStream var7 = new FileInputStream(new File(var0 + "/" + var1));
      MessageDigest var8 = MessageDigest.getInstance("SHA-256");

      int var5;
      while((var5 = var7.read(var2)) != -1) {
        var8.update(var2, 0, var5);
      }

      byte[] var3 = var8.digest();
      var7.close();
      InputStream var10 = EndUserResourceExtractor.class.getResourceAsStream("/Hashes/" + var1 + ".hash");
      byte[] var4 = new byte[var3.length];

      for(int var6 = 0; (var5 = var10.read(var2)) != -1; var6 += var5) {
        if (var6 + var5 > var4.length) {
          var10.close();
          return false;
        }

        System.arraycopy(var2, 0, var4, var6, var5);
      }

      var10.close();
      return Arrays.equals(var3, var4);
    } catch (Exception var9) {
      return false;
    }
  }*/

  private static boolean CheckRequiredResources(String var0) {
    Map var1 = GetResources();
    String[] var2 = (String[]) ((String[]) var1.get("required"));

    for (int var3 = 0; var3 < var2.length; ++var3) {
      if (!IsResourceExistsInFileSystem(var0 + '/' + var2[var3])) {
        return false;
      }
    }

    return true;
  }

  // v1 fileName
  private static void WriteResourceToFile(String resName, String fileName) throws Exception {
    byte[] buf = new byte[1024];
    //boolean var3 = true;
    InputStream in = AdsResourceExtractor.class.getResourceAsStream(resName);
    if (in != null) {
      File file = new File(fileName);
      BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

      int nRead;
      while ((nRead = in.read(buf)) != -1) {
        out.write(buf, 0, nRead);
      }

      out.close();
    }
  }

  private static void WriteResourceToFile(URL url, String fileName) throws Exception {
    byte[] buf = new byte[1024];
    InputStream in = url.openStream();
    File file = new File(fileName);
    BufferedOutputStream var6 = new BufferedOutputStream(new FileOutputStream(file));

    int nRead;
    while ((nRead = in.read(buf)) != -1) {
      var6.write(buf, 0, nRead);
    }

    var6.close();
    in.close();
  }

  private static void WriteResourcesToPath(URL var0, String var1) throws Exception {
    String var2 = var1 + "/" + GetLibrariesZipFileName();
    WriteResourceToFile(var0, var2);
    byte[] var3 = new byte[1024];
    //if (!CheckFileHash(var1, GetLibrariesZipFileName())) {
    //  throw new IOException();
    //} else {
    FileInputStream var5 = new FileInputStream(new File(var2));
    ZipInputStream var6 = new ZipInputStream(var5);

    for (ZipEntry var7 = var6.getNextEntry(); var7 != null; var7 = var6.getNextEntry()) {
      String var8 = var7.getName();
      File var9 = new File(var1 + '/' + var8);
      BufferedOutputStream var10 = new BufferedOutputStream(new FileOutputStream(var9));

      int var4;
      while ((var4 = var6.read(var3)) != -1) {
        var10.write(var3, 0, var4);
      }

      var10.close();
      var6.closeEntry();
    }

    var6.close();
    //}
  }

  public static void UnzipFile(String var0) throws Exception {
    byte[] var1 = new byte[1024];
    File var2 = new File(var0);
    ZipFile var3 = new ZipFile(var2);
    String var4 = var0.substring(0, var0.length() - 4);
    (new File(var4)).mkdir();
    Enumeration var5 = var3.entries();

    while (true) {
      while (var5.hasMoreElements()) {
        ZipEntry var6 = (ZipEntry) var5.nextElement();
        File var7 = new File(var4, var6.getName());
        if (var6.isDirectory()) {
          var7.mkdir();
        } else {
          BufferedInputStream var8 = new BufferedInputStream(var3.getInputStream(var6));
          BufferedOutputStream var9 = new BufferedOutputStream(new FileOutputStream(var7), var1.length);

          int var10;
          while ((var10 = var8.read(var1, 0, var1.length)) != -1) {
            var9.write(var1, 0, var10);
          }

          var9.close();
          var8.close();
        }
      }

      return;
    }
  }

  private static void PreLoadResource(String var0) throws Exception {
    if (loadFromPath) {
      if (!IsResourceExistsInFileSystem(var0)) {
        return;
      }
    } else if (!IsResourceExistsInJar(var0)) {
      return;
    }

    if (var0.indexOf(".dll") >= 0 || var0.indexOf(".so") >= 0 || var0.indexOf(".dylib") >= 0) {
      if (loadFromPath) {
        System.load(var0);
      } else {
        String var1 = var0.substring(var0.lastIndexOf(47) + 1, var0.lastIndexOf("."));
        if (var1.startsWith("lib")) {
          var1 = var1.substring(3);
        }

        System.loadLibrary(var1);
      }

      /*if (!isWindows && (isMacOS && var0.indexOf("libEUSignJavaUtils.dylib") >= 0 || var0.indexOf("libEUSignJavaUtils.so") >= 0)) {
        try {
          (new EndUserLibraryUtils()).SetCurrentDirectory(GetInstallPath());
        } catch (Exception var2) {
          ;
        }
      }*/

    }
  }

  private static void RemoveDirectory(File var0) throws IOException {
    if (var0.isDirectory()) {
      File[] var1 = var0.listFiles();

      for (int var2 = 0; var2 < var1.length; ++var2) {
        RemoveDirectory(var1[var2]);
      }
    }

    if (!var0.delete()) {
      throw new FileNotFoundException();
    }
  }

  public static boolean IsWindows() {
    return isWindows;
  }

  public static void SetPath(String var0) {
    path = var0;
  }

  public static String GetPath() {
    return path;
  }

  public static void SetPreLoad(boolean var0) {
    preLoad = var0;
  }

  public static boolean GetPreLoad() {
    return preLoad;
  }

  public static String GetInstallPath() {
    String var0;
    if (path.equals("")) {
      var0 = System.getProperty("java.io.tmpdir");
    } else {
      var0 = path;
    }

    if (!isExtractableResources) {
      return var0;
    } else {
      if (var0.charAt(var0.length() - 1) != '/') {
        var0 = var0 + "/";
      }

      var0 = var0 + "AdsLibs";
      if (isX86) {
        var0 = var0 + "-x86-";
      } else {
        var0 = var0 + "-x64-";
      }

      var0 = var0 + version;
      return var0;
    }
  }

  public static String ExtractResources() throws Exception {
    String installPath = GetInstallPath();
    String resourcesLocation = GetResourcesLocation();
    String[] resourcesNames = GetResourcesNames();
    File dir = new File(installPath);
    if (isExtractableResources && (!dir.exists() || !CheckRequiredResources(installPath))) {
      dir.mkdir();

      try {
        int i;
        if (IsResourceExistsInJar(resourcesLocation + "/" + resourcesNames[0])) {
          for (i = 0; i < resourcesNames.length; ++i) {
            WriteResourceToFile(resourcesLocation + "/" + resourcesNames[i], installPath + "/" + resourcesNames[i]);
          }
        } else {
          WriteResourcesToPath(new URL(codebaseURL, GetLibrariesZipFileName()), installPath);
        }

        for (i = 0; i < resourcesNames.length; ++i) {
          String archName = installPath + "/" + resourcesNames[i];
          if (archName.endsWith(".zip") && IsResourceExistsInFileSystem(archName)) {
            UnzipFile(archName);
          }
        }
      } catch (Exception ex) {
        RemoveDirectory(dir);
        throw ex;
      }
    }

    if (preLoad) {
      String path = loadFromPath ? installPath : resourcesLocation;

      for (int i = 0; i < resourcesNames.length; ++i) {
        PreLoadResource(path + "/" + resourcesNames[i]);
      }
    }

    return installPath;
  }

/*  public static boolean SetUSBDevice(int var0, byte[] var1) {
    if (!isWindows) {
      try {
        return (new EndUserLibraryUtils()).SetUSBDevice(var0, var1);
      } catch (Exception var3) {
        return false;
      }
    } else {
      return false;
    }
  }

  public static InputStream GetSettingsFile(String var0) {
    try {
      return EndUserResourceExtractor.class.getResourceAsStream("/Settings/" + var0);
    } catch (Exception var2) {
      return null;
    }
  } */

  public static String MapLibraryName(String name) {
      return System.mapLibraryName(name);
  }

  public static void LoadLibrary(String name) {
    if (loadFromPath) {
      String fileName = GetInstallPath() + "/" + MapLibraryName(name);
      System.load(fileName);
    } else {
      System.loadLibrary(name);
    }

  }

  static {
    try {
      String os_name = System.getProperty("os.name").toLowerCase();
      String os_archext = System.getProperty("os.arch").toLowerCase();
      isWindows = os_name.indexOf("win") >= 0;
      isSunOS = os_name.indexOf("sunos") >= 0;
      isMacOS = os_name.indexOf("mac") >= 0;
      isFreeBSD = os_name.indexOf("freebsd") >= 0;
      isAIX = os_name.indexOf("aix") >= 0;
      isAndroid = GetIsAndroidSafe();
      isARM64 = os_archext.indexOf("aarch64") >= 0;
      isARM = !isARM64 && os_archext.indexOf("arm") >= 0;
      isSPARC = os_archext.indexOf("sparc") >= 0;
      isX86 = "x86".compareTo(os_archext) == 0 || "i386".compareTo(os_archext) == 0 || "i686".compareTo(os_archext) == 0;
    } catch (Exception ex) {
      isWindows = true;
      isSPARC = false;
      isAIX = false;
      isARM = false;
      isARM64 = false;
      isX86 = true;
      isAndroid = false;
    }

  }

}
