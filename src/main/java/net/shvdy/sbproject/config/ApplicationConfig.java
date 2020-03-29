package net.shvdy.sbproject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shvdy.sbproject.dto.FoodDTO;
import net.shvdy.sbproject.entity.Food;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 22.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Food, FoodDTO>() {
            @Override
            protected void configure() {
                map().setProfileId(source.getUserProfile().getProfileId());
            }
        });
        return modelMapper;
    }

    //Jackson
    @Bean
    public ObjectMapper jackson() {
        return new ObjectMapper();
    }
}
