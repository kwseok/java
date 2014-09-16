package io.teamscala.java.sample.security.userdetails;

import io.teamscala.java.core.web.util.WebUtils;
import io.teamscala.java.sample.security.exception.DisabledDetailException;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import javax.inject.Inject;

/**
 * AuthenticationProvider 에서 호출되며, 패스워드 인증 후 체크를 담당하는 클래스.
 */
public class PostAuthenticationChecks implements UserDetailsChecker {

    @Inject
    MessageSource messageSource;

    @Override
    public void check(UserDetails user) {
        if (!user.isEnabled()) {
            throw new DisabledDetailException("User is diabled.", getMessage("login.error.failure"));
        }
    }

    /**
     * 메세지를 반환한다.
     *
     * @param messageCode 메세지 코드
     * @return 메세지
     */
    private String getMessage(String messageCode) {
        return messageSource.getMessage(messageCode, null, WebUtils.getCurrentLocale());
    }

}
