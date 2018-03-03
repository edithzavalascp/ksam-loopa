package org.ksam.api;

import org.ksam.logic.autonomicmanager.SimpleLoopManager;
import org.ksam.logic.me.configuration.MEConfigurationManager;
import org.ksam.logic.sensors.MonitoringDataManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static MEConfigurationManager meConfigM;
    public static MonitoringDataManager mdM;

    public static void main(String[] args) {
	SpringApplication.run(Application.class, args);
	meConfigM = new MEConfigurationManager(new SimpleLoopManager(args[0]));
	mdM = new MonitoringDataManager();
    }

}
