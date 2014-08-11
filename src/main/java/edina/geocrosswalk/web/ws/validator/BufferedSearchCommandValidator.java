package edina.geocrosswalk.web.ws.validator;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edina.geocrosswalk.web.ws.BufferedSearchCommand;
import edina.geocrosswalk.web.ws.DistanceCommand;

/**
 * Validator to validate <code>DistanceCommand</code> objects.
 * 
 * 
 */
public class BufferedSearchCommandValidator extends AbstractGXWCommandValidator {
    
    private static final String REQUIRED_CODE = "required";
    private static final String INDENTIFIER_REQUIRED = "Missing identifier parameter";
    private static final String DISTANCE_REQUIRED = "Missing distance parameter";

    private static Logger logger = Logger.getLogger(BufferedSearchCommandValidator.class);

    private Validator featureValidator;

	/*
	 * @see
	 * edina.crosswalk.ws.validation.AbstractCrosswalkCommandValidator#supports
	 * (java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return BufferedSearchCommand.class.isAssignableFrom(clazz);
	}

	/*
	 * @see
	 * edina.crosswalk.ws.validation.AbstractCrosswalkCommandValidator#validate
	 * (java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		
		// check for parameter binding errors
	    checkErrors(errors);
	    
		BufferedSearchCommand bufferedSearchCommand = (BufferedSearchCommand) target;
		String identifier = bufferedSearchCommand.getIdentifier();
		Integer distance = bufferedSearchCommand.getDistance();
		String featureType = bufferedSearchCommand.getFeatureType();

        if ( !StringUtils.hasText(identifier) ) {
            logger.warn(INDENTIFIER_REQUIRED);
            errors.reject(REQUIRED_CODE, INDENTIFIER_REQUIRED);
        }
        
        if(distance == null){
            logger.warn(DISTANCE_REQUIRED);
            errors.reject(REQUIRED_CODE, DISTANCE_REQUIRED);
        }
        
      
        ValidationUtils.invokeValidator(getFeatureValidator(), featureType, errors);
        
        validateGenericParameters(bufferedSearchCommand);
        
		// check for validation errors
		checkErrors(errors);

	}

    public Validator getFeatureValidator() {
        return featureValidator;
    }

    public void setFeatureValidator(Validator featureValidator) {
        this.featureValidator = featureValidator;
    }

	

}