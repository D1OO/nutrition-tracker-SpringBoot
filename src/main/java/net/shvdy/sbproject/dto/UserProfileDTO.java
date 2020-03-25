package net.shvdy.sbproject.dto;

import lombok.*;
import net.shvdy.sbproject.entity.UserProfile;

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

    UserDTO user;
    List<FoodDTO> userFood;
    private Long userId;
    private int age;
    private int height;
    private int weight;
    private UserProfile.Lifestyle lifestyle;

    // If user's profile isn't filled,
    // Thymeleaf will check on '-1' value and display 'fill profile to proceed' window,
    // otherwise, if normal value is returned - user's 'food diary' will be loaded

    // 'Optional<Integer> can't be used, because Thymeleaf can't call .isPresent() or .get(),
    // nor any other method except Thymeleaf's '#...' predefined expressions or variable's class methods like this one
    public int getDailyCalsNorm() {
        try {
            return (int) ((66 + 13.75 * weight + 5 * height - 6.755 * age) * lifestyle.getFactor());
        } catch (NullPointerException e) {
            return -1;
        }
    }
}
