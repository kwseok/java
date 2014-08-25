package io.teamscala.java.core.validation.constraints.impl;

import io.teamscala.java.core.util.StringUtils;
import io.teamscala.java.core.validation.constraints.Length;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 다국어 변환 길이 체크 검증자.
 * 
 * @author 이영진
 */
public class LengthValidator implements ConstraintValidator<Length, String> {

	private static final Log log = LoggerFactory.make();

	private int min;
	private int max;
	private String encoding;

	@Override
	public void initialize(Length parameters) {
		min = parameters.min();
		max = parameters.max();
		encoding = parameters.encoding();
		validateParameters();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) return true;
		try {
			int length = value.getBytes(encoding).length;
			if (length < min || length > max) return false;

			List<String> invalids = StringUtils.checkTranscoding(value, encoding);
			if (invalids.size() > 0) {
				log.debugf("Invalid characters {0}, for encoding {1}", invalids, encoding);
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			log.error(e);
			return false;
		}
		return true;
	}

	private void validateParameters() {
		if ( min < 0 ) throw log.getMinCannotBeNegativeException();
		if ( max < 0 ) throw log.getMaxCannotBeNegativeException();
		if ( max < min ) throw log.getLengthCannotBeNegativeException();
	}
}