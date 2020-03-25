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
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    UserProfile userProfile;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    private String password;
    @NotNull
    @Column(name = "email")
    private String username;
    @Column(name = "first_name_ua")
    private String firstNameUa;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    @NotNull
    private String firstName;
    @NotNull
    @ElementCollection(fetch = FetchType.EAGER, targetClass = RoleType.class)
    @Enumerated(EnumType.STRING)
    private Set<RoleType> authorities;

    @NotNull
    private boolean accountNonExpired;
    @NotNull
    private boolean accountNonLocked;
    @NotNull
    private boolean credentialsNonExpired;
    @NotNull
    private boolean enabled;
}