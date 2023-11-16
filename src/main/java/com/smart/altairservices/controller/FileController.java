package com.smart.altairservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class FileController {
  private final ResourceLoader resourceLoader;
  
  @Autowired
  public FileController(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }
  
  @GetMapping("/updated-file")
  @ResponseBody
  public Object execute() {
    return retrieveLatestHtmlFileDetails();
  }
  
  private Object retrieveLatestHtmlFileDetails() {
    Optional<File> latestHtmlFile = getLatestHtmlFile("htmlextra");
    Optional<File> latestXmlFile = getLatestHtmlFile("xml");
    
    if (latestHtmlFile.isPresent() && latestXmlFile.isPresent()) {
      // Obtiene la ruta relativa desde la carpeta resources
      String relativeHtmlPath = Paths.get("api/reports/htmlextra", latestHtmlFile.get().getName()).toString();
      String relativeXmlPath = Paths.get("api/reports/xml", latestXmlFile.get().getName()).toString();
      String formattedDate = formatDate(latestHtmlFile.get().lastModified());
      
      return Map.of(
          "fileName", latestHtmlFile.get().getName(),
          "htmlFilePath", "/" + relativeHtmlPath,
          "xmlFilePath", "/" + relativeXmlPath,
          "fileDate", formattedDate
      );
    } else {
      return Map.of("error", "No files found");
    }
  }
  
  private Optional<File> getLatestHtmlFile(String report) {
    Resource resource = resourceLoader.getResource("classpath:static/api/reports/" + report + "/");
    
    try {
      File directory = resource.getFile();
      File[] files = directory.listFiles();
      
      if (files != null && files.length > 0) {
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        return Optional.of(files[0]);
      } else {
        return Optional.empty();
      }
    } catch (IOException e) {
      System.out.println("Error: " + e); // Manejo del error si es necesario
      return Optional.empty();
    }
  }
  
  private static String formatDate(long lastModified) {
    // Convertir el timestamp a LocalDateTime
    Instant instant = Instant.ofEpochMilli(lastModified);
    LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("America/Bogota"));
    
    // Crear un formateador con el patrón deseado y la localidad española
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy, HH:mm", new Locale("es", "ES"));
    
    // Formatear la fecha utilizando el formateador
    return dateTime.format(formatter);
  }
}
