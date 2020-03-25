package net.shvdy.sbproject.dto;

import lombok.*;

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

    UserProfileDTO userProfile;
}
