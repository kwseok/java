package io.teamscala.java.sample.security.userdetails;

import io.teamscala.java.sample.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.teamscala.java.sample.models.User.Level;

/**
 * 인증 정보를 저장할 엔티티.
 */
public class SecurityDetails implements UserDetails {

    private static final long serialVersionUID = 8695432671407631944L;

    // Fields

    private User user;

    /**
     * 사용자 권한.
     */
    private final List<SimpleGrantedAuthority> roles;

    // Constructors

    /**
     * 생성자.
     *
     * @param user 유저 정보
     */
    public SecurityDetails(User user) {
        this.user = user;

        List<SimpleGrantedAuthority> roles = new ArrayList<>();

        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (Level.A.equals(user.getLevel())) {
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        if (user.isApproved()) {
            switch (user.getServiceType()) {
                case B: roles.add(new SimpleGrantedAuthority("ROLE_SERVICE_BASIC")); break;
                case P: roles.add(new SimpleGrantedAuthority("ROLE_SERVICE_PREMIUM")); break;
            }
        }

        this.roles = Collections.unmodifiableList(roles);
    }

    // Generated Getters and Setters...

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    // Implements for UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !Level.N.equals(user.getLevel());
    }
}
