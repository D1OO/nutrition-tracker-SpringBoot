package net.shvdy.nutrition_tracker.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import net.shvdy.nutrition_tracker.entity.UserProfile;
import net.shvdy.nutrition_tracker.service.Mapper;

import java.util.List;

/**
 * 25.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class UserProfileDTO {
    private String firstName;
    private String firstNameUa;
    private String lastName;
    private int age;
    private int height;
    private int weight;
    private UserProfile.Lifestyle lifestyle;

    UserDTO user;
    List<FoodDTO> userFood;
    List<DailyRecordDTO> dailyRecords;

    @Override
    public String toString() {
        try {
            return Mapper.JACKSON.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: " + e);
            return "";
        }
    }
}

