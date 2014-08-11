package edina.geocrosswalk.web.ws.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates [name] search parameters.
 * 
 * @author Joe Vernon
 * @author Brian O'Hare
 */
public class NameValidator implements Validator {

	private static Logger logger = Logger.getLogger(NameValidator.class);
	private static final String REQUIRED_CODE = "required";
	public static final String NAME_REQUIRED = "Missing name parameter";
	private static final String NAME_EMPTY = "Search parameter 'name' was empty";
	private static final String INVALID_NAME = "Invalid name parameter";
	private String nameValidationRegex;


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
	public void validate(Object object, Errors errors) {
		String name = (String) object;
 
		// set a global error if name parameter is null...
		if (StringUtils.isBlank(name)) {
			logger.warn(NAME_EMPTY);
			errors.reject(REQUIRED_CODE, NAME_REQUIRED);
			return;
		}

		/*
		 * Validate the name search string.
		 * 
		 * Will allow wildcard searches of the formdour | ?ber but not | ? and
		 * will reject requests if they contain any characters except a-zA-Z_0-9
		 * space or ?
		 * 
		 * see service.properties for regex string
		 */
		String[] names = name.split(",");

		for (String element : names) {
			logger.info("Validating:\n    |" + element + "|\n");
			if (StringUtils.isBlank(element)) {
				// ignore it..
				continue;
			}

			Pattern pattern = Pattern.compile(getNameValidationRegex());
			Matcher matcher = pattern.matcher(element);
			boolean found = false;
			while (matcher.find()) {
				found = true;
				break;
			}
			if (found) {
				logger.warn(INVALID_NAME);
				errors.reject(REQUIRED_CODE, INVALID_NAME);
				return;
			}
		}
	}


	/**
	 * Gets the name validation regex.
	 * 
	 * @return the nameValidationRegex
	 */
	public String getNameValidationRegex() {
		return nameValidationRegex;
	}


	/**
	 * Sets the name validation regex.
	 * 
	 * @param nameValidationRegex the nameValidationRegex to set
	 */
	public void setNameValidationRegex(String nameValidationRegex) {
		this.nameValidationRegex = nameValidationRegex;
	}
}
