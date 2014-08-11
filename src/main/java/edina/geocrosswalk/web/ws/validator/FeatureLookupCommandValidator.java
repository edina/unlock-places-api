package edina.geocrosswalk.web.ws.validator;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.FeatureLookupCommand;

/**
 * Validator to validate <code>FeatureLookupCommand</code> objects.
 * 
 * @author Joe Vernon
 * 
 */
public class FeatureLookupCommandValidator extends AbstractGXWCommandValidator {

	private static Logger logger = Logger.getLogger(NameSearchCommandValidator.class);
	private Validator identifierValidator;


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return FeatureLookupCommand.class.isAssignableFrom(clazz);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		
		checkErrors(errors);
		
		FeatureLookupCommand featureCommand = (FeatureLookupCommand) target;
		GXWFormat format = featureCommand.getFormat();
		String gazetteer = featureCommand.getGazetteer();
		String key = featureCommand.getKey();
		String srs = featureCommand.getSrs();
		String count = featureCommand.getCount();

		// validate the name parameter
		ValidationUtils.invokeValidator(getIdentifierValidator(), featureCommand.getIdentifier(), errors);

		checkErrors(errors);

		// validate the generic parameters.
		featureCommand.setFormat(validateOutputFormat(format));
		featureCommand.setGazetteer(validateGazetteer(gazetteer,key));
		featureCommand.setCount(validateCount(count));
		featureCommand.setSrs(validateSrs(srs));
		if(featureCommand.getSrs().equals(BRITISH_NATIONAL_GRID)){
			featureCommand.setCountry(UNITED_KINGDOM);
		}
	}


	/**
	 * Gets the identifier Validator.
	 * 
	 * @return the identifierValidator
	 */
	public Validator getIdentifierValidator() {
		return identifierValidator;
	}


	/**
	 * Sets the identifierValidator.
	 * 
	 * @param identifierValidator the identifierValidator to set
	 */
	public void setIdentifierValidator(Validator identifierValidator) {
		this.identifierValidator = identifierValidator;
	}
}
