package net.shvdy.nutrition_tracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
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
        //        modelMapper.addMappings(new PropertyMap<Food, FoodDTO>() {
//            @Override
//            protected void configure() {
//                map().setProfileId(source.getUserProfile().getProfileId());
//            }
//        });
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper jackson() {
        return new ObjectMapper();
    }

}
