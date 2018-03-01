package com.gmail.vkorshun.checkusingimg;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by vkorshun on 16.04.2017.
 */
public class CheckUsing {

  private List<Path> fileList;
  private List<Path> imgList;
  private String dirPath;
  private Path cuttentPath = null;

  public CheckUsing(String dirPath) {
    this.dirPath = dirPath;
    fileList = readFileNames(dirPath);
    imgList = readFileNames(dirPath + "\\assets\\images");
  }

  private List<Path> readFileNames(String path) {
    final List<Path> files = new ArrayList<>();
    try {
      dirPath = path;
      Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          if (!attrs.isDirectory()) {
            files.add(file);
          }
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
          if (dir.compareTo(Paths.get(dirPath)) == 0) {
            return FileVisitResult.CONTINUE;
          }
          return FileVisitResult.SKIP_SUBTREE;
        }
      });
    } catch (IOException e) {
      return null;
    }
    return files;
  }

  private boolean findFile(Path file, Path img) {
    Pattern p = Pattern.compile(img.getFileName().toString());
    String fileSod = "";
    StringBuilder sb = new StringBuilder();
    try {
      try {
        Stream<String> st = Files.lines(file);
        st.forEach(line -> {
          sb.append(line);
        });
      } catch (Exception e) {
        //System.out.println(file);
        //e.printStackTrace();
      }
      fileSod = sb.toString();
      if (!fileSod.isEmpty()) {
        Matcher m = p.matcher("[\"|/]" + sb.toString() + "\"");
        return m.find();
      } else {
        return false;
      }
    } catch (Exception ex) {
      System.out.println(String.format("Error in file  %s : %s", file.getFileName().toString(), ex.getMessage()));
      return false;
    }
  }

  public void checkFiles() {
    for (Path im : imgList) {
      boolean found = false;
      for (Path p : fileList) {
        if (findFile(p, im)) {
          //System.out.println(String.format("File %s is not using", im.getFileName().toString()));
          found = true;
          try {
            System.out.println(String.format("Ok %s ", im.getFileName().toString()));
          } catch (Exception e) {
            e.printStackTrace();
          }
          break;
        }
      }
      if (!found) {
        try {
          Path pNotUsed = Paths.get(dirPath+"\\notused\\");
          if (!Files.exists(pNotUsed)) {
            Files.createDirectories(pNotUsed);
          }
          Files.move(im, Paths.get(dirPath+"\\notused\\"+im.getFileName()),StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }


  public List<Path> getFileList() {
    return fileList;
  }

  public void setFileList(List<Path> fileList) {
    this.fileList = fileList;
  }

  public List<Path> getImgList() {
    return imgList;
  }

  public void setImgList(List<Path> imgList) {
    this.imgList = imgList;
  }

  public String getDirPath() {
    return dirPath;
  }

  public void setDirPath(String dirPath) {
    this.dirPath = dirPath;
  }

  public static void main(String[] args) {
    System.out.println(String.format("params %d", args.length));
    CheckUsing ch = new CheckUsing(args[0]);
   /* if (ch.findFile(Paths.get("g:\\persha\\main.html"), Paths.get("g:\\persha\\car1.jpg"))) {
      System.out.println("Ok");
    } else {
      System.out.println("No");
    }*/
    ch.checkFiles();

  }

}
