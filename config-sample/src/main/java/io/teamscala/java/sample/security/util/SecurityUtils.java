package io.teamscala.java.sample.security.util;

import io.teamscala.java.sample.models.User;
import io.teamscala.java.sample.security.userdetails.SecurityDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtils {

    public static Optional<User> getUser() {
        return getPrincipal().flatMap(a -> Optional.ofNullable(a.getUser()));
    }

    public static Optional<SecurityDetails> getPrincipal() {
        return getAuthentication().flatMap(a -> {
            if (a.isAuthenticated()) {
                Object principal = a.getPrincipal();
                if (principal instanceof SecurityDetails) {
                    return Optional.of((SecurityDetails) principal);
                }
            }
            return Optional.empty();
        });
    }

    public static Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

}
