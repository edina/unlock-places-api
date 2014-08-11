package edina.geocrosswalk.web.ws.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates feature parameters.
 * 
 * @author Joe Vernon
 */
public class FeatureValidator implements Validator {

	private static Logger logger = Logger.getLogger(FeatureValidator.class);
	private static final String REQUIRED_CODE = "featuretype.required";
	private static final String INVALID_CODE = "featuretype.invalid";
	private static final String FEATURE_TYPE_REQUIRED = "Missing featureType parameter";
	private static final String FEATURE_TYPE_EMPTY = "Search parameter 'featureType' was empty";
	private static final String INVALID_FEATURE_TYPE = "Invalid feature type";
	private String featureValidationRegex;


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
	/*
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		String featureType = (String) obj;
		// set a global error if parameter is null...
		if (StringUtils.isBlank(featureType)) {
			errors.reject(REQUIRED_CODE, FEATURE_TYPE_REQUIRED);
			logger.warn(FEATURE_TYPE_EMPTY);
			return;
		}
		/*
		 * Validate the search string, see service.properties for regex string
		 */
		Pattern pattern = Pattern.compile(getFeatureValidationRegex());
		Matcher matcher = pattern.matcher(featureType);
		boolean found = false;
		while (matcher.find()) {
			found = true;
			break;
		}
		if (found) {
			logger.error("Rejecting featureType search string: " + featureType);
			errors.reject(INVALID_CODE, INVALID_FEATURE_TYPE);
			return;
		}
	}


	/**
	 * Gets the feature validation regex.
	 * 
	 * @return the featureValidationRegex
	 */
	public String getFeatureValidationRegex() {
		return featureValidationRegex;
	}


	/**
	 * Sets the feature validation regex.
	 * 
	 * @param featureValidationRegex the featureValidationRegex to set
	 */
	public void setFeatureValidationRegex(String featureValidationRegex) {
		this.featureValidationRegex = featureValidationRegex;
	}
}
