package edina.geocrosswalk.web.ws.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates Post Code search parameters.
 * 
 * @author Joe Vernon
 */
public class PostCodeValidator implements Validator {
	
	private static Logger logger = Logger.getLogger(PostCodeValidator.class);
	private static final String REQUIRED_CODE = "postcode.required";
	private static final String INVALID_CODE = "invalid.postcode";
	private static final String POST_CODE_REQUIRED = "Missing postcode parameter";
	private static final String INVALID_POST_CODE = "Invalid postcode";
	private String postCodeValidationRegex;

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
		String postcode = (String) obj;
		if (StringUtils.isBlank(postcode)) {
			errors.reject(REQUIRED_CODE, POST_CODE_REQUIRED);
			return;
		}

		/*
		 * Validate the postcode search string. Allows spaces and word character
		 * classes but prevents wildcards (*?) at beginning of search term see
		 * service.properties for regex string
		 */
		
		Pattern pattern = Pattern.compile(getPostCodeValidationRegex());
		Matcher matcher = pattern.matcher(postcode);
		boolean found = false;
		while (matcher.find()) {
			found = true;
			break;
		}
		if (found) {
			logger.warn(INVALID_CODE);
			errors.reject(REQUIRED_CODE, INVALID_POST_CODE);
			return;
		}
	}

	/**
	 * Gets the post code validation regex.
	 * 
	 * @return the postCodeValidationRegex
	 */
	public String getPostCodeValidationRegex() {
		return postCodeValidationRegex;
	}

	/**
	 * Sets the post code validation regex.
	 * 
	 * @param postCodeValidationRegex the postCodeValidationRegex to set
	 */
	public void setPostCodeValidationRegex(String postCodeValidationRegex) {
		this.postCodeValidationRegex = postCodeValidationRegex;
	}
}
