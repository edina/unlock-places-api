package edina.geocrosswalk.web.ws.validator;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.SupportedFeatureTypesCommand;

/**
 * Validator to validate <code>SupportedFeatureTypesCommand</code> objects.
 * 
 * @author Joe Vernon
 * @author Brian O'Hare
 * 
 */
public class SupportedFeatureTypesCommandValidator extends AbstractGXWCommandValidator {

	private static Logger logger = Logger.getLogger(SupportedFeatureTypesCommandValidator.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return SupportedFeatureTypesCommand.class.isAssignableFrom(clazz);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		
		checkErrors(errors);
		
		SupportedFeatureTypesCommand featureTypesCommand = (SupportedFeatureTypesCommand) target;

		GXWFormat format = featureTypesCommand.getFormat();
		String gazetteer = featureTypesCommand.getGazetteer();
		String key = featureTypesCommand.getKey();
		
		// validate the generic parameters.
		featureTypesCommand.setFormat(validateOutputFormat(format));
		featureTypesCommand.setGazetteer(validateGazetteer(gazetteer,key));
	}
}
