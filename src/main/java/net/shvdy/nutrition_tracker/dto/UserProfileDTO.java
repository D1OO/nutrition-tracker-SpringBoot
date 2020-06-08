package net.shvdy.nutrition_tracker.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import net.shvdy.nutrition_tracker.model.entity.UserProfile;
import net.shvdy.nutrition_tracker.model.service.Mapper;

import javax.validation.constraints.*;
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
    @NotNull
    @Pattern(regexp = "^[A-Z]((?![ .,'-]$)[a-z .,'-]){2,24}$", message = "{validation.incorrect}")
    private String firstName;
    @Pattern(regexp = "^[А-Я]((?![ .,'-]$)[а-я .,'-]){2,24}$|^$", message = "Incorrect value")
    private String firstNameUa;
    @NotNull
    @Pattern(regexp = "^[A-Z]((?![ .,'-]$)[a-z .,'-]){2,24}$", message = "Incorrect value")
    private String lastName;
    @Positive
    @Max(120)
    private Integer age;
    @Min(10)
    @Max(255)
    private Integer height;
    @Positive
    @Max(255)
    private Integer weight;
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

