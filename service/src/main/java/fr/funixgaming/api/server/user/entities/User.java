package fr.funixgaming.api.server.user.entities;

import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.core.crud.entities.ApiEntity;
import fr.funixgaming.api.server.converters.EncryptionString;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "api_users")
public class User extends ApiEntity implements UserDetails {
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    @Convert(converter = EncryptionString.class)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private Boolean banned;

    @OneToMany(mappedBy = "user")
    private Set<UserToken> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        switch (this.role) {
            case MODERATOR -> {
                return Set.of(
                        new SimpleGrantedAuthority(UserRole.USER.getRole()),
                        new SimpleGrantedAuthority(UserRole.MODERATOR.getRole())
                );
            }
            case ADMIN -> {
                final Set<SimpleGrantedAuthority> roles = new HashSet<>();

                for (final UserRole role : UserRole.values()) {
                    roles.add(new SimpleGrantedAuthority(role.getRole()));
                }
                return roles;
            }
        }

        return Collections.singletonList(new SimpleGrantedAuthority(UserRole.USER.getRole()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return !banned;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !banned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return banned;
    }
}
