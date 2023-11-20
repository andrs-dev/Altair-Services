package com.smart.altairservices.service;

import com.smart.altairservices.Directories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AllFilesService {
    private final Directories directories;
    @Autowired
    public AllFilesService(Directories directories) {
        this.directories = directories;
    }
    
    public List<Map<String, Object>> reportFiles() {
        List<Map<String, Object>> result = new ArrayList<>();
        addFilesToList(result);
        
        result.sort(Comparator.comparing((Map<String, Object> fileInfo) -> LocalDateTime.parse((String) fileInfo.get("fileDate"), DateTimeFormatter.ofPattern("dd-MMM-yyyy, HH:mm", new Locale("es", "ES")))).reversed());
        return result;
    }
    
    private void addFilesToList(List<Map<String, Object>> result) {
        String reportDirectory = directories.htmlReportsDir + "/";
        File resource = new File(reportDirectory);
        try {
            File[] files = resource.listFiles();
            
            if (files != null) {
                for (File file : files) {
                    String relativePath = "/api/reports/htmlextra/" + file.getName();
                    String formattedDate = formatDate(file.lastModified());
                    
                    Map<String, Object> fileInfo = Map.of(
                            "fileName", file.getName(),
                            "filePath", relativePath,
                            "fileDate", formattedDate
                    );
                    result.add(fileInfo);
                }
            }
        } catch (SecurityException e) {
            System.out.println("Error: " + e);
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