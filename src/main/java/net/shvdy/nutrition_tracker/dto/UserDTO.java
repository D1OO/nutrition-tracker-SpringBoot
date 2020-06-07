package net.shvdy.nutrition_tracker.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import net.shvdy.nutrition_tracker.model.entity.RoleType;
import net.shvdy.nutrition_tracker.model.service.Mapper;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class UserDTO {
    private String username;
    private String password;
    Set<RoleType> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

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
