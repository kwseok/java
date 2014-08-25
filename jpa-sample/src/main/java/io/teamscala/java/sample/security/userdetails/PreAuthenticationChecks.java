package io.teamscala.java.sample.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

public class PreAuthenticationChecks implements UserDetailsChecker {

	@Override
	public void check(UserDetails toCheck) {
		
	}

}
