package com.smart.altairservices.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class LastFileController {
    @Value("${api.directory}")
    private String apiDir;
    
    @Value("${reports.html}")
    private String htmlReportsDir;
    
    @Value("${reports.xml}")
    private String xmlReportsDir;
    
    @GetMapping("/updated-file")
    @ResponseBody
    public Object execute() {
        return latestReportFile();
    }
    
    private Object latestReportFile() {
        Optional<File> latestHtmlFile = getLatestFile("htmlextra");
        Optional<File> latestXmlFile = getLatestFile("xml");
        
        if (latestHtmlFile.isPresent() && latestXmlFile.isPresent()) {
            String relativeHtmlPath = "/api/reports/htmlextra/" + latestHtmlFile.get().getName();
            String relativeXmlPath = "/api/reports/xml/" + latestXmlFile.get().getName();
            String formattedDate = formatDate(latestHtmlFile.get().lastModified());
            
            
            return Map.of(
                    "fileName", latestHtmlFile.get().getName(),
                    "htmlFilePath", relativeHtmlPath,
                    "xmlFilePath", relativeXmlPath,
                    "fileDate", formattedDate
            );
        } else {
            return Map.of("error", "No se ha encontrado un archivo.");
        }
    }
    
    
    
    private Optional<File> getLatestFile(String report) {
        String reportDirectory;
        switch (report) {
            case "htmlextra":
                reportDirectory = htmlReportsDir + "/";
                break;
            case "xml":
                reportDirectory = xmlReportsDir + "/";
                break;
            default:
                return Optional.empty();
        }
        
        File resource = new File(reportDirectory);
        try {
            File[] files = resource.listFiles();
            
            if (files != null && files.length > 0) {
                Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
                return Optional.of(files[0]);
            } else {
                return Optional.empty();
            }
        } catch (SecurityException e) {
            System.out.println("Error: " + e);
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
