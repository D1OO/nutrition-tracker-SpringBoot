package net.shvdy.nutrition_tracker.model.entity;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

import static net.shvdy.nutrition_tracker.model.entity.RoleType.ROLE_USER;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String password;

    @NotNull
    @Column(name = "email")
    private String username;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = RoleType.class)
    @Enumerated(EnumType.STRING)
    private Set<RoleType> authorities = Set.of(ROLE_USER);

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    UserProfile userProfile;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired = true;
    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;
    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired = true;
    @Column(name = "enabled")
    private boolean enabled = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

}