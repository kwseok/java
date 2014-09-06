package io.teamscala.java.sample.security.userdetails;

import io.teamscala.java.sample.security.exception.DisabledDetailException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

public class PostAuthenticationChecks implements UserDetailsChecker {

    @Override
    public void check(UserDetails user) {
        /*
        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }
        */

        if (!user.isEnabled()) {
            if (user.getClass().isAssignableFrom(SecurityDetails.class)) {
                SecurityDetails details = (SecurityDetails) user;

                // 테스트로 사용자 이름이 test 인 사용자는 reject 시킨다.
                if (details.getUser().getUsername().equals("test")) {
                    // 특정 사유
                    String reason = "그냥 REJECT!!!";
                    throw new DisabledDetailException("User is diabled", reason);
                }
            }
            throw new DisabledException("User is disabled");
        }

        /*
        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired");
        }

        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials have expired");
        }
        */
    }
}