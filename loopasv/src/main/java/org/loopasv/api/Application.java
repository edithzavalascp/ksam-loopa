package org.loopasv.api;

import org.loopasv.logic.autonomicmanager.LoopaSvAutonomicManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner() {
    return args -> {
      // Setup loop, services policies, etc.
      new LoopaSvAutonomicManager().start();
    };
  }
}
