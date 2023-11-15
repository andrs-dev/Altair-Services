package com.smart.altairservices.controller;

import org.apache.logging.log4j.util.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

@RestController
public class FileController {
  @GetMapping("/updated-file")
  @ResponseBody
  public String getLastFile() {
    Path rootDir = Paths.get(".").toAbsolutePath().getParent();
    Path reportsDir = rootDir.resolve("src/main/resources/api/reports/htmlextra");
    File directory = new File(reportsDir.toString());
    File[] files = directory.listFiles();
    if (files != null && files.length > 0) {
      Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
      
      // Devuelve el nombre del último archivo modificado/creado
      return files[0].getName();
    } else {
      return "No files founded";
    }
  }
}
