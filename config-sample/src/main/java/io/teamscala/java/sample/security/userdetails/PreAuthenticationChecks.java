package io.teamscala.java.sample.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * AuthenticationProvider 에서 호출되며, 패스워드 인증 전 체크를 담당하는 클래스.
 */
public class PreAuthenticationChecks implements UserDetailsChecker {

    @Override
    public void check(UserDetails toCheck) {
    }

}
