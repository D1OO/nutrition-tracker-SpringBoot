package net.shvdy.sbproject.entity;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

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
    private Set<RoleType> authorities;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    UserProfile userProfile;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired;
    @Column(name = "account_non_locked")
    private boolean accountNonLocked;
    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;
    @Column(name = "enabled")
    private boolean enabled;
}