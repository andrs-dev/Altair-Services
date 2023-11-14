package com.smart.altairservices.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;

@Service
public class ScriptService {
  
  @Value("${root.path}")
  private String rootPath;
  private String path;
  /*private final WebClient webClient;
  
  @Autowired
  public ScriptService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("http://localhost:8080/execute-script").build();
  }
  
  public Mono<String> executeScript() {
    // Realizar una solicitud HTTP al servicio REST
    return webClient.get()
        .uri("/execute-script")
        .retrieve()
        .bodyToMono(String.class);
  }*/
  
  @PostConstruct
  public void init() {
    try {
      this.path = new File(rootPath).getCanonicalPath().concat("/src/main/resources/api/scripts/GenerateTestReport.sh");
    } catch (IOException e) {
      System.out.println("TaskSchedule error: " + e);
    }
  }
  
  // Task 08:00 am
  @Scheduled(cron = "0 0 08 * * ?")
  public void task1() {
    System.out.println(this.path);
  }
  
  // Task 10:30 am
  @Scheduled(cron = "0 30 10 * * ?")
  public void task2() {
    System.out.println(this.path);
  }
  
  // Task 01:00 pm
  @Scheduled(cron = "0 0 13 * * ?")
  public void task3() {
    System.out.println(this.path);
  }
  
  // Task 04:30 pm
  @Scheduled(cron = "* 30 16 * * ?")
  public void task4() {
    System.out.println(this.path);
  }
  
  // Task Everytime
 /* @Scheduled(cron = "0 * * * * ?")
  public void taskTest() {
    try {
      ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", this.path);
      Process process = processBuilder.start();
      //System.out.println(this.path);
      int exitCode = process.waitFor();
      if (exitCode == 0) {
        System.out.println("Script ejecutado exitosamente.");
      } else {
        System.err.println("Código de salida clase ScheduleExecutor: " + exitCode);
      }
    } catch (IOException | InterruptedException e) {
      System.out.println("GenerateTestReport Error: " + e);
    }
  }*/
  
  
}
