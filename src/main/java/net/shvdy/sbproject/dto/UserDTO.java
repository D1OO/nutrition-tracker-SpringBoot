package net.shvdy.sbproject.dto;

import lombok.*;
import net.shvdy.sbproject.entity.RoleType;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String username;
    private String password;
    private String firstName;
    private String firstNameUa;
    private String lastName;

    Set<RoleType> authorities;
    UserProfileDTO userProfile;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
}
