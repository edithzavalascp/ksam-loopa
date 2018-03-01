package org.ksam.api;

import org.ksam.logic.autonomicmanager.LoopManager;
import org.ksam.logic.me.configuration.MEConfigurationManager;
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
	    MEConfigurationManager.setLoop(new LoopManager("simpleAM"));
	};
    }
}
