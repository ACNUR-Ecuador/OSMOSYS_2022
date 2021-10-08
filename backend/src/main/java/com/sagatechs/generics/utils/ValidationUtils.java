package com.sagatechs.generics.utils;

import org.apache.commons.collections4.CollectionUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Named
@ApplicationScoped
public class ValidationUtils {

	@SuppressWarnings("WeakerAccess")
	public <T> Set<ConstraintViolation<T>> validate(T object) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		return validator.validate(object);
	}

	@SuppressWarnings("WeakerAccess")
	public <T> List<String> getErrorMessagesFromValidation(T object) {

		Set<ConstraintViolation<T>> violations = this.validate(object);
		List<String> msgs = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(violations)) {
			for (ConstraintViolation<T> violation : violations) {
				msgs.add(createViolationMessage(violation));
			}
		}

		return msgs;
	}

	@SuppressWarnings("unused")
	public <T> String getErrorMessageFromValidation(T object) {

		List<String> msgs = getErrorMessagesFromValidation(object);

		if (CollectionUtils.isEmpty(msgs)) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			for (String msg : msgs) {
				sb.append(msg).append(" ");
			}
			return sb.toString();
		}
	}

	private static <T> String createViolationMessage(ConstraintViolation<T> violation) {
		StringBuilder sb = new StringBuilder();
		sb.append(violation.getMessage()).append(".");
		if (violation.getInvalidValue() == null) {
			sb.append(violation.getInvalidValue());
		}
		return sb.toString();
	}

}
