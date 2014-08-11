package edina.geocrosswalk.web.ws.validator;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;

/**
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class OperatorValidator extends AbstractGXWCommandValidator {
	
	private static Logger logger = Logger.getLogger(FeatureValidator.class);

	private static final String REQUIRED_CODE = "operator.required";
	private static final String UNSUPPORTED_CODE = "operator.unsupported";
	private static final String OPERATOR_REQUIRED = "Spatial operator required";
	private static final String OPERATOR_EMPTY = "Search parameter 'operator' was empty";
	private static final String OPERATOR_UNSUPPORTED = "unsupported operator";
	
	private String[] supportedOperators;

	/* (non-Javadoc)
	 * @see edina.geocrosswalk.web.ws.validator.AbstractGXWCommandValidator#supports(java.lang.Class)
	 */

	public boolean supports(Class clazz) {
		return String.class.isAssignableFrom(clazz);
	}

	/* (non-Javadoc)
	 * @see edina.geocrosswalk.web.ws.validator.AbstractGXWCommandValidator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		String operator = (String) obj;
		if (StringUtils.isBlank(operator)) {
			//errors.reject(REQUIRED_CODE, OPERATOR_REQUIRED);
			logger.warn(OPERATOR_EMPTY);
			return;
		}
		List<String> supportedOperatorList = Arrays.asList(getSupportedOperators());
		if (!supportedOperatorList.contains(operator)) {
			errors.reject(UNSUPPORTED_CODE, OPERATOR_UNSUPPORTED + ": " + operator);
			return;
		}
	}

	/**
	 * Gets the supportedOperators
	 *
	 * @return the supportedOperators
	 */
	public String[] getSupportedOperators() {
		return supportedOperators;
	}

	/**
	 * Sets the supportedOperators
	 *
	 * @param supportedOperators the supportedOperators to set
	 */
	public void setSupportedOperators(String[] supportedOperators) {
		this.supportedOperators = supportedOperators;
	}

}
