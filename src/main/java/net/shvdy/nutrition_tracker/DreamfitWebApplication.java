package net.shvdy.nutrition_tracker;

import net.shvdy.nutrition_tracker.config.FormulaConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FormulaConfigProperties.class)
public class DreamfitWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(DreamfitWebApplication.class, args);
    }
}
