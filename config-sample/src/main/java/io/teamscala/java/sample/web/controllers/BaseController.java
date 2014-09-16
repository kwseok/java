package io.teamscala.java.sample.web.controllers;

import io.teamscala.java.core.web.servlet.mvc.ControllerSupport;
import org.springframework.context.MessageSource;
import org.springframework.validation.Validator;

import javax.inject.Inject;

public abstract class BaseController extends ControllerSupport {
	
	public static final String JSON_VIEW = "jsonView";
	public static final String JSON_VIEW_RESULT = "jsonViewResult";
	public static final String RESULT = "result";

	@Inject
    void install(MessageSource messageSource, Validator validator) {
		setMessageSource(messageSource);
		setValidator(validator);
	}

}
