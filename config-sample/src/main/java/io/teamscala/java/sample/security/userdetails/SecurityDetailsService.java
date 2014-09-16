package io.teamscala.java.sample.security.userdetails;

import io.teamscala.java.sample.models.QUser;
import io.teamscala.java.sample.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 사용자 인증 조회를 위한 서비스.
 */
public class SecurityDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QUser u = QUser.user;
        User user = User.find.where(u.username.eq(username)).limit(1).uniqueResult(u);
        if (user == null) throw new UsernameNotFoundException(username + " not found.");
        return new SecurityDetails(user);
    }

}
