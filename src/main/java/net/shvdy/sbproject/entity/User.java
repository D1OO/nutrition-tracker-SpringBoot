/**
 * User
 * <p>
 * version 1.0
 * <p>
 * 06.03.2020
 * <p>
 * Copyright(r) shvdy.net
 */

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
@ToString
@Entity
@Table(name = "user",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User implements UserDetails {
    @Column(name = "email", nullable = false)
    @NotNull
    private String username;
    @Column(nullable = false)
    @NotNull
    private String password;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "first_name", nullable = false)
    @NotNull
    private String firstName;
    @Column(name = "first_name_ua")
    private String firstNameUa;
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "role", nullable = false)
    @ElementCollection(fetch = FetchType.EAGER, targetClass = RoleType.class)
    @Enumerated(EnumType.STRING)
    private Set<RoleType> authorities;

    @Column(nullable = false)
    private boolean accountNonExpired;
    @Column(nullable = false)
    private boolean accountNonLocked;
    @Column(nullable = false)
    private boolean credentialsNonExpired;
    @Column(nullable = false)
    private boolean enabled;
}