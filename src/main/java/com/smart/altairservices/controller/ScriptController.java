package com.smart.altairservices.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@PropertySource("classpath:path.properties")
public class ScriptController {
  
  @Value("${node.path}")
  private String
      nodePath;
  
  @Value("${newman.path}")
  private String
      newmanPath;
  
  @GetMapping("/execute-script")
  public String executeScript() {
    try {
      // Configuración de la zona horaria
      String timezone = "America/Bogota";
      System.setProperty("user.timezone", timezone);
      
      // Obtención de la fecha y hora actual
      String month = executeCommand("date +%b").trim().toUpperCase();
      String day = executeCommand("date +%d").trim();
      String time = executeCommand("date +%H%M").trim();
      
      // Obtención del directorio raiz (ruta relativa)
      Path rootDir = Paths.get(".").toAbsolutePath().getParent();
      
      Path apiDir = rootDir.resolve("src/main/resources/api");
      // Debug
      // System.out.println("APIDIR VARIABLE: " + apiDir);
      
      // Nomenclatura de los archivos de los reportes
      String reportPath = apiDir.resolve("reports")
          .resolve(time + "-CollectionName-Test-" + day + "-" + month + ".html")
          .toString();
      // Debug
      // System.out.println("REPORTPATH VARIABLE: " + reportPath);
      
      // Actualización de Crontab (si es necesario)
      executeCommand(apiDir.resolve("scripts").resolve("CronConfiguration.sh").toString());
      
      // Ejecución de Newman
      String newmanCommand = nodePath + " " + newmanPath + " run " + apiDir.resolve("Test" +
          ".postman_collection.json") + " -r htmlextra --reporter-htmlextra-export " + reportPath;
      System.out.println("NEWMAN COMMAND: " + newmanCommand);
      executeCommand(newmanCommand);
      return "El reporte se ha generado correctamente";
    } catch (IOException | InterruptedException e) {
      return "Error al ejecutar el script: " + e.getMessage();
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
      // System.out.println("Exit Code: " + exitCode);
      // System.out.println("Output: " + output);
      
      if (exitCode == 0) {
        return output.toString();
      } else {
        System.out.println("Ha ocurrido un error ejecutando un comando.");
        return null;
      }
    } catch (IOException | InterruptedException e) {
      System.out.println("Error: " + e);
    }
    return null;
  }
  
}