package io.teamscala.java.core.web.servlet.mvc;

import io.teamscala.java.core.validation.ValidationUtils;
import io.teamscala.java.core.web.util.WebUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Abstract controller support.
 *
 * @author 석기원
 */
public abstract class ControllerSupport implements InitializingBean {

	// Fields

	private MessageSource messageSource;
	private Validator validator;

	// Setters

	/**
	 * Set to the {@link MessageSource}
	 * 
	 * @param messageSource {@link MessageSource}
	 */
	public void setMessageSource(MessageSource messageSource) { this.messageSource = messageSource; }

	/**
	 * Set to the {@link Validator}
	 * 
	 * @param validator {@link Validator}
	 */
	public void setValidator(Validator validator) { this.validator = validator; }

	// Implementations for InitializingBean

	@Override
	public void afterPropertiesSet() throws Exception {
		if (messageSource == null) throw new IllegalArgumentException("'messageSource' is required");
		if (validator == null) throw new IllegalArgumentException("'validator' is required");
	}

	// Implementations utility.

	/** @return Get the current {@link HttpServletRequest} */
	public HttpServletRequest getRequest() { return WebUtils.getCurrentRequest(); }

	/** @return Get the current {@link Locale} */
	public Locale getLocale() { return WebUtils.getCurrentLocale(); }

	// Implementations for message source.

	/**
	 * 프로퍼티 파일에 정의된 메시지를 가져 온다, 
	 * 
	 * @param code 메시지 코드
	 * @return 메시지
	 */
	public String getMessage(String code){ return getMessage(code, null, null, null);		 }

	/**
	 * 프로퍼티 파일에 정의된 메시지를 가져 온다, 
	 * 
	 * @param code 메시지 코드
	 * @param locale 로케일
	 * @return 메시지
	 */
	public String getMessage(String code, Locale locale){ return getMessage(code, null, null, locale);		 }

	/**
	 * 프로퍼티 파일에 정의된 메시지를 가져 온다, 
	 * 
	 * @param code 메시지 코드
	 * @param args {0} 으로 정의 된 매개변수
	 * @return 메시지
	 */
	public String getMessage(String code, Object[] args){ return getMessage(code, args, null, null); }

	/**
	 * 프로퍼티 파일에 정의된 메시지를 가져 온다, 
	 * 
	 * @param code 메시지 코드
	 * @param args {0} 으로 정의 된 매개변수
	 * @param locale 로케일
	 * @return 메시지
	 */
	public String getMessage(String code, Object[] args, Locale locale){ return getMessage(code, args, null, null); }

	/**
	 * 프로퍼티 파일에 정의된 메시지를 가져 온다, 
	 * 
	 * @param code 메시지 코드
	 * @param defaultMessage 디폴트 메시지
	 * @return 메시지
	 */
	public String getMessage(String code, String defaultMessage){ return getMessage(code, null, defaultMessage, null); }

	/**
	 * 프로퍼티 파일에 정의된 메시지를 가져 온다, 
	 * 
	 * @param code 메시지 코드
	 * @param defaultMessage 디폴트 메시지
	 * @param locale 로케일
	 * @return 메시지
	 */
	public String getMessage(String code, String defaultMessage, Locale locale){ return getMessage(code, null, defaultMessage, locale); }

	/**
	 * 프로퍼티 파일에 정의된 메시지를 가져 온다, 
	 * 
	 * @param code 메시지 코드
	 * @param args {0} 으로 정의 된 매개변수
	 * @param defaultMessage 디폴트 메시지
	 * @return 메시지
	 */
	public String getMessage(String code, Object[] args, String defaultMessage){ return getMessage(code, args, defaultMessage, null); }

	/**
	 * 프로퍼티 파일에 정의된 메시지를 가져 온다, 
	 * 
	 * @param code 메시지 코드
	 * @param args {0} 으로 정의 된 매개변수
	 * @param defaultMessage 디폴트 메시지
	 * @param locale 로케일
	 * @return 메시지
	 */
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale){		
		if (locale == null) locale = getLocale();
		return messageSource.getMessage(code, args, defaultMessage, locale);		
	}

	/**
	 * 프로퍼티 파일에 정의된 메시지를 가져 온다, 
	 * 
	 * @param resolvable {@link MessageSourceResolvable}
	 * @return 메시지
	 */
	public String getMessage(MessageSourceResolvable resolvable) { return getMessage(resolvable, null); }

	/**
	 * 프로퍼티 파일에 정의된 메시지를 가져 온다, 
	 * 
	 * @param resolvable {@link MessageSourceResolvable}
	 * @param locale 로케일
	 * @return 메시지
	 */
	public String getMessage(MessageSourceResolvable resolvable, Locale locale) {
		if (locale == null) locale = getLocale();
		return messageSource.getMessage(resolvable, locale);
	}

	// Implementations for validation.

	/**
	 * Valid.
	 * 
	 * @param target the target object
	 * @param validationHints one or more hint objects to be passed to the validation engine
	 * @return {@link Errors}
	 */
	public Errors valid(Object target, Object... validationHints) {
		return ValidationUtils.invokeValidator(validator, target, validationHints);
	}

	/**
	 * Valid.
	 * 
	 * @param target the target object
	 * @param objectName the name of the target object
	 * @param validationHints one or more hint objects to be passed to the validation engine
	 * @return {@link Errors}
	 */
	public Errors valid(Object target, String objectName, Object... validationHints) {
		return ValidationUtils.invokeValidator(validator, target, objectName, validationHints);
	}

	/**
	 * Valid.
	 * 
	 * @param target the target object
	 * @param errors {@link Errors}
	 * @param validationHints one or more hint objects to be passed to the validation engine
	 * @return {@link Errors}
	 */
	public Errors valid(Object target, Errors errors, Object... validationHints) {
		ValidationUtils.invokeValidator(validator, target, errors, validationHints);
		return errors;
	}

	/**
	 * Throw if invalid.
	 * 
	 * @param errors {@link Errors}
	 * @return {@link Errors}
	 * @throws BindException if there were any errors in the bind operation
	 */
	public Errors throwIfInvalid(Errors errors) throws BindException {
		if (errors instanceof BindingResult) {
			if (errors.hasErrors())
				throw new BindException((BindingResult) errors);

			return errors;
		}
		throw new IllegalStateException(
				"Errors is not instance of " + BindingResult.class.getName() +
				" : " + errors.toString());
	}

	/**
	 * Throw If invalid.
	 * 
	 * @param target the target object
	 * @param validationHints one or more hint objects to be passed to the validation engine
	 * @return {@link Errors}
	 * @throws BindException if there were any errors in the bind operation
	 */
	public Errors throwIfInvalid(Object target, Object... validationHints) throws BindException {
		return throwIfInvalid(valid(target, validationHints));
	}

	/**
	 * Throw If invalid.
	 * 
	 * @param target the target object
	 * @param objectName the name of the target object
	 * @param validationHints one or more hint objects to be passed to the validation engine
	 * @return {@link Errors}
	 * @throws BindException if there were any errors in the bind operation
	 */
	public Errors throwIfInvalid(Object target, String objectName, Object... validationHints) throws BindException {
		return throwIfInvalid(valid(target, objectName, validationHints));
	}

	/**
	 * Throw If invalid.
	 * 
	 * @param target the target object
	 * @param errors {@link Errors}
	 * @param validationHints one or more hint objects to be passed to the validation engine
	 * @return {@link Errors}
	 * @throws BindException if there were any errors in the bind operation
	 */
	public Errors throwIfInvalid(Object target, Errors errors, Object... validationHints) throws BindException {
		return throwIfInvalid(valid(target, errors, validationHints));
	}
}
