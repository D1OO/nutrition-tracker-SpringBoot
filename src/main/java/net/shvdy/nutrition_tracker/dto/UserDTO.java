package net.shvdy.nutrition_tracker.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotNull
    @Email
    private String username;

    @NotNull
    @Pattern(regexp = "^[0-9a-zA-Z]{8,15}$", message = "{validation.error.password}")
    private String password;

    @Valid
    UserProfileDTO userProfile;

}
