package io.teamscala.java.sample.web.controllers;

import io.teamscala.java.core.web.servlet.mvc.ControllerSupport;
import org.springframework.context.MessageSource;
import org.springframework.validation.Validator;

import javax.inject.Inject;

public abstract class BaseController extends ControllerSupport {

    @Inject
    void install(MessageSource messageSource, Validator validator) {
        setMessageSource(messageSource);
        setValidator(validator);
    }
}
