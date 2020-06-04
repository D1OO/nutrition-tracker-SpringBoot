package net.shvdy.nutrition_tracker.dto;

import lombok.*;
import net.shvdy.nutrition_tracker.entity.UserProfile;

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

    public int getDailyCalsNorm() {
        try {
            return (int) ((66 + 13.75 * weight + 5 * height - 6.755 * age) * lifestyle.getFactor());
        } catch (NullPointerException e) {
            return -1;
        }
    }
}

