package edina.geocrosswalk.web.ws.validator;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.FootprintLookupCommand;

/**
 * Validator to validate <code>FootprintLookupCommand</code> objects.
 * 
 * @author Joe Vernon
 * 
 */
public class FootprintLookupCommandValidator extends AbstractGXWCommandValidator {

	private static Logger logger = Logger.getLogger(NameSearchCommandValidator.class);
	private Validator identifierValidator;


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return FootprintLookupCommand.class.isAssignableFrom(clazz);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		
		checkErrors(errors);
		
		FootprintLookupCommand footprintCommand = (FootprintLookupCommand) target;
		GXWFormat format = footprintCommand.getFormat();
		String gazetteer = footprintCommand.getGazetteer();
		String key = footprintCommand.getKey();
		String srs = footprintCommand.getSrs();
		
		
		String clearCache = footprintCommand.getClearCache();
		// validate the name parameter if there is one
		if (clearCache == null) {
			// validate the identifier parameter
			ValidationUtils.invokeValidator(getIdentifierValidator(), footprintCommand.getIdentifier(), errors);
		}
		

		checkErrors(errors);

		// validate the generic parameters.
		footprintCommand.setFormat(validateOutputFormat(format));
		footprintCommand.setGazetteer(validateGazetteer(gazetteer,key));
		footprintCommand.setSrs(validateSrs(srs));
		if(footprintCommand.getSrs().equals(BRITISH_NATIONAL_GRID)){
			footprintCommand.setCountry(UNITED_KINGDOM);
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
