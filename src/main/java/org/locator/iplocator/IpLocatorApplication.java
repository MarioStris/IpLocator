package org.locator.iplocator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

@Slf4j
@SpringBootApplication
public class IpLocatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpLocatorApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logVersion(ApplicationReadyEvent event) {
        String version = event.getApplicationContext().getEnvironment().getProperty("info.app.version");
        log.info("Application is ready (version: " + version + ")");
    }
}
