package net.shvdy.nutrition_tracker.dto;

import lombok.*;
import net.shvdy.nutrition_tracker.model.entity.UserProfile;

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

    private UserProfile.Lifestyle lifestyle = UserProfile.Lifestyle.MODERATELY_ACTIVE;

    UserDTO user;
    List<FoodDTO> userFood;
    List<DailyRecordDTO> dailyRecords;

}

