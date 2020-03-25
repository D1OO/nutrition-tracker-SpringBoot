package net.shvdy.sbproject.dto;

import lombok.*;
import net.shvdy.sbproject.entity.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersDTO {
    private List<User> users;
}
