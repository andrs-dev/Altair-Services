package com.smart.altairservices.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@PropertySource("classpath:path.properties")
public class ScriptService {
  
  @Value("${node.path}")
  private String
      nodePath;
  
  @Value("${newman.path}")
  private String
      newmanPath;
  
  private final ResourceLoader resourceLoader;
  
  @Autowired
  public ScriptService(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }
  
  // Task 08:00 am
  @Scheduled(cron = "0 0 08 * * ?")
  public void task1() {
    executeScript();
  }
  
  // Task 10:30 am
  @Scheduled(cron = "0 30 10 * * ?")
  public void task2() {
    executeScript();
  }
  
  // Task 01:00 pm
  @Scheduled(cron = "0 0 13 * * ?")
  public void task3() {
    executeScript();
  }
  
  // Task 04:30 pm
  @Scheduled(cron = "0 30 16 * * ?")
  public void task4() {
    executeScript();
  }
  @Scheduled(cron = "0 35 16 * * ?")
  public void task5() {
    executeScript();
  }
  
  public void executeScript() {
    try {
      // Configuración de la zona horaria
      String timezone = "America/Bogota";
      System.setProperty("user.timezone", timezone);
      String libraries = nodePath + " " + newmanPath;
      String collectionName = "Test.postman_collection";
      
      // Obtención de la fecha y hora actual
      String month = executeCommand("date +%b").trim().toUpperCase();
      String day = executeCommand("date +%d").trim();
      String time = executeCommand("date +%H%M").trim();
      
      // Obtención del directorio raiz (ruta relativa)
      Resource apiDir = resourceLoader.getResource("classpath:static/api/");
      Resource htmlReportsDir = resourceLoader.getResource("classpath:static/api/reports/htmlextra/");
      Resource xmlReportsDir = resourceLoader.getResource("classpath:static/api/reports/xml/");
      Resource bashScripts = resourceLoader.getResource("classpath:static/api/bash-scripts/");
      
       System.out.println("APIDIR VARIABLE: " + apiDir);
      
      // Nomenclatura de los archivos de los reportes
      String htmlReportPath = htmlReportsDir.getFile() + "/" + time + "-" + collectionName + "-" + day + "-" + month +
          ".html";
       System.out.println("REPORTPATH VARIABLE: " + htmlReportPath);
      
      String xmlReportPath = xmlReportsDir.getFile() + "/" + time + "-" + collectionName + "-" + day + "-" + month +
          ".xml";
       System.out.println("XMLPATH VARIABLE: " + xmlReportPath);
      
      // Actualización de Crontab local (si es necesario)
      executeCommand(bashScripts.getFile() + "/CronConfiguration.sh");
      
      // Creacion de los reportes en dos formatos: html y xml
      String htmlReportCommand = libraries + " run " + apiDir.getFile() + "/Test.postman_collection.json"
          + " -r htmlextra --reporter-htmlextra-export " + htmlReportPath;
      executeCommand(htmlReportCommand);
      // Debug
      //System.out.println("HTML COMMAND: " + htmlReportCommand);
      
      String xmlReportCommand = libraries + " run " + apiDir.getFile() +
          "/Test.postman_collection.json" + " -r cli,junit --reporter-junit-export " + xmlReportPath;
      executeCommand(xmlReportCommand);
      // Debug
      //System.out.println("XML COMMAND: " + xmlReportCommand);
      
      
      System.out.println("Reporte generado correctamente.");
    } catch (IOException | InterruptedException e) {
      System.out.println("Error al ejecutar el script: " + e.getMessage());
    }
  }
  
  private String executeCommand(String command) throws IOException, InterruptedException {
    try {
      Process process = Runtime.getRuntime().exec(command);
      process.waitFor();
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      StringBuilder output = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
      }
      
      int exitCode = process.exitValue();
      
      if (exitCode == 0 || exitCode == 1) {
        return output.toString();
      } else {
        System.out.println("Algo salió mal con el comando: " + command);
        return null;
      }
    } catch (IOException | InterruptedException e) {
      System.out.println("Error: " + e);
    }
    return null;
  }
}
