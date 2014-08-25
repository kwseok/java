package io.teamscala.java.sample.security.userdetails;

import io.teamscala.java.sample.models.QUser;
import io.teamscala.java.sample.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SecurityDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = User.find
				.query()
				.where(QUser.user.username.eq(username))
				.limit(1)
				.uniqueResult(QUser.user);

		if (user == null)
			throw new UsernameNotFoundException(username + " not found.");

		return new SecurityDetails(user);
	}
}