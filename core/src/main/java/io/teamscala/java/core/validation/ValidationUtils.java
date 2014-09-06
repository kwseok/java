package io.teamscala.java.core.validation;

import org.springframework.core.Conventions;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;

/**
 * Validation Utility class.
 */
public abstract class ValidationUtils extends org.springframework.validation.ValidationUtils {

    /**
     * Invoke the given {@link Validator}/{@link SmartValidator} for the supplied object and
     * {@link Errors} instance.
     *
     * @param validator       the <code>Validator</code> to be invoked (must not be <code>null</code>)
     * @param target          the object to bind the parameters to
     * @param validationHints one or more hint objects to be passed to the validation engine
     * @return {@link Errors}
     * @throws IllegalArgumentException if either of the <code>Validator</code> or <code>Errors</code> arguments is
     *                                  <code>null</code>, or if the supplied <code>Validator</code> does not {@link Validator#supports(Class) support}
     *                                  the validation of the supplied object's type
     */
    public static Errors invokeValidator(Validator validator, Object target, Object... validationHints) {
        return invokeValidator(validator, target, (String) null, validationHints);
    }

    /**
     * Invoke the given {@link Validator}/{@link SmartValidator} for the supplied object and {@link Errors} instance.
     *
     * @param validator       the <code>Validator</code> to be invoked (must not be <code>null</code>)
     * @param target          the object to bind the parameters to
     * @param objectName      the name of the target object
     * @param validationHints one or more hint objects to be passed to the validation engine
     * @return {@link Errors}
     * @throws IllegalArgumentException if either of the <code>Validator</code> or <code>Errors</code> arguments is
     *                                  <code>null</code>, or if the supplied <code>Validator</code> does not {@link Validator#supports(Class) support}
     *                                  the validation of the supplied object's type
     */
    public static Errors invokeValidator(Validator validator, Object target, String objectName, Object... validationHints) {
        if (objectName == null && target != null)
            objectName = Conventions.getVariableName(target);

        DataBinder binder = new DataBinder(target, objectName);
        Errors errors = binder.getBindingResult();
        invokeValidator(validator, target, errors, validationHints);
        return errors;
    }
}
