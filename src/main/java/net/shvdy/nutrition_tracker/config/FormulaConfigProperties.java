package net.shvdy.nutrition_tracker.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties("formula")
@PropertySource({"classpath:dailycaloriesnormformula.properties"})
@Setter
public class FormulaConfigProperties {

    public double coef1;
    public double weightModifier;
    public double heightModifier;
    public double ageModifier;
}
