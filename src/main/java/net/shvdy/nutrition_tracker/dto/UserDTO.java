package net.shvdy.nutrition_tracker.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import net.shvdy.nutrition_tracker.model.entity.RoleType;
import net.shvdy.nutrition_tracker.model.service.Mapper;

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
@Log4j2
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
