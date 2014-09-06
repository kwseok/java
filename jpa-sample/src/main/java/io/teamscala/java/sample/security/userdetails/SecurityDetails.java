package io.teamscala.java.sample.security.userdetails;

import io.teamscala.java.sample.models.User;
import io.teamscala.java.sample.models.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SecurityDetails implements UserDetails {

    /**
     * 사용자 객체.
     */
    private User user;

    /**
     * 사용자 권한.
     */
    private List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();

    // Constructors

    public SecurityDetails(User user) {
        Assert.notNull(user, "User must not be null");

        this.user = user;
        for (UserRole role : user.getRoles()) {
            roles.add(new SimpleGrantedAuthority(role.getType().name()));
        }
    }

    // Override methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.<GrantedAuthority>unmodifiableCollection(roles);
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
        return true;
    }

    // Generated Getters and Setters...

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
