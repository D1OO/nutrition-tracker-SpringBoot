package net.shvdy.nutrition_tracker.dto;

import lombok.*;
import net.shvdy.nutrition_tracker.entity.RoleType;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String username;
    private String password;
    Set<RoleType> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    UserProfileDTO userProfile;
}
