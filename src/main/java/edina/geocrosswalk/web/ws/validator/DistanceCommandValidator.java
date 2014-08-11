package edina.geocrosswalk.web.ws.validator;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;

import edina.geocrosswalk.web.ws.DistanceCommand;

/**
 * Validator to validate <code>DistanceCommand</code> objects.
 * 
 * 
 */
public class DistanceCommandValidator extends AbstractGXWCommandValidator {
    
    private static final String REQUIRED_CODE = "required";
    private static final String INDENTIFIERS_REQUIRED = "Missing identifier parameters";

    private static Logger logger = Logger.getLogger(DistanceCommandValidator.class);



	/*
	 * @see
	 * edina.crosswalk.ws.validation.AbstractCrosswalkCommandValidator#supports
	 * (java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return DistanceCommand.class.isAssignableFrom(clazz);
	}

	/*
	 * @see
	 * edina.crosswalk.ws.validation.AbstractCrosswalkCommandValidator#validate
	 * (java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		
		// check for parameter binding errors
	    checkErrors(errors);
	    
		DistanceCommand distanceCommand = (DistanceCommand) target;
		Integer unlockIdA = distanceCommand.getIdA();
		Integer unlockIdB = distanceCommand.getIdB();
		
        if (unlockIdA == null || unlockIdB == null ) {
            logger.warn(INDENTIFIERS_REQUIRED);
            errors.reject(REQUIRED_CODE, INDENTIFIERS_REQUIRED);
            return;
        }
		
		// check for validation errors
		checkErrors(errors);

	}

	

}