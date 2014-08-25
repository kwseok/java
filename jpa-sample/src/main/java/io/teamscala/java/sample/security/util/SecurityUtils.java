package io.teamscala.java.sample.security.util;

import io.teamscala.java.sample.models.User;
import io.teamscala.java.sample.security.userdetails.SecurityDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

	public static User getUser() {
		SecurityDetails principal = getPrincipal();
		return (principal != null ? principal.getUser() : null);
	}

	public static SecurityDetails getPrincipal() {
		Authentication authentication = getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof SecurityDetails) {
				return (SecurityDetails) principal;
			}
		}
		return null;
	}

	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
}
