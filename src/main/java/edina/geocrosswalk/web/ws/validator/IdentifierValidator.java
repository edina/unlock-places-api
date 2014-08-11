package edina.geocrosswalk.web.ws.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates [identifier] search parameters.
 * 
 * @author Joe Vernon
 * @author Brian O'Hare
 * 
 */
public class IdentifierValidator implements Validator {

	private static Logger logger = Logger.getLogger(IdentifierValidator.class);
	private static final Integer ZERO = new Integer(0);
	private static final Long MAX_LONG_VALUE = Long.MAX_VALUE;
	private static final String REQUIRED_CODE = "required";
	private static final String INVALID_CODE = "invalid";
	private static final String IDENTIFIER_REQUIRED = "Missing identifier parameter";
	private static final String INVALID_IDENTIFIER = "Invalid identifier";
	private String identifierValidationRegex;


	/*
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return String.class.isAssignableFrom(clazz);
	}


	/*
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		String identifier = (String) obj;

		// set a global error if identifier parameter is null...
		if (StringUtils.isBlank(identifier)) {
			logger.error("Empty [identifier] parameter");
			errors.reject(REQUIRED_CODE, IDENTIFIER_REQUIRED);
			return;
		}

		String[] identifiers = identifier.split(",");

		for (String element : identifiers) {
			logger.info("Validating:\n    |" + element + "|\n");
			if (StringUtils.isBlank(element)) {
				// ignore it..
				continue;
			}

			logger.debug("Identifier Regex: "+getIdentifierValidationRegex());
			Pattern pattern = Pattern.compile(getIdentifierValidationRegex());
			Matcher matcher = pattern.matcher(element);
			boolean found = false;
			
			while (matcher.find()) {
				found = true;
				break;
			}
			if (found) {
				logger.warn(INVALID_IDENTIFIER);
				errors.reject(REQUIRED_CODE, INVALID_IDENTIFIER);
				return;
			}
		}
	}


	/**
	 * Gets the identifier validation regex.
	 * 
	 * @return the identifierValidationRegex
	 */
	public String getIdentifierValidationRegex() {
		return identifierValidationRegex;
	}


	/**
	 * Sets the identifier validation regex.
	 * 
	 * @param identifierValidationRegex the identifierValidationRegex to set
	 */
	public void setIdentifierValidationRegex(String identifierValidationRegex) {
		this.identifierValidationRegex = identifierValidationRegex;
	}
}