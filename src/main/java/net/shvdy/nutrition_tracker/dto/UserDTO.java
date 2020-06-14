package net.shvdy.nutrition_tracker.dto;

import lombok.*;
import net.shvdy.nutrition_tracker.model.entity.RoleType;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @NotNull
    @Email
    private String username;
    @NotNull
    @Pattern(regexp = "^[0-9a-zA-Z]{8,15}$", message = "{validation.error.password}")
    private String password;
    Set<RoleType> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    @Valid
    UserProfileDTO userProfile;

}
