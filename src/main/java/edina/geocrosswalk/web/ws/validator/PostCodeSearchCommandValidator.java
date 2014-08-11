package edina.geocrosswalk.web.ws.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.PostCodeSearchCommand;

/**
 * Validator to validate <code>NameSearchCommand</code> objects.
 * 
 * @author Joe Vernon
 * @author Brian O'Hare
 * 
 */
public class PostCodeSearchCommandValidator extends AbstractGXWCommandValidator {

	private static Logger logger = Logger.getLogger(PostCodeSearchCommandValidator.class);
	private Validator postCodeValidator;


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return PostCodeSearchCommand.class.isAssignableFrom(clazz);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {

		checkErrors(errors);
		
		PostCodeSearchCommand postCodeCommand = (PostCodeSearchCommand) target;
		String postCode = postCodeCommand.getPostCode();
		Integer maxRows = postCodeCommand.getMaxRows();
		Integer startRow = postCodeCommand.getStartRow();
		GXWFormat format = postCodeCommand.getFormat();
		String gazetteer = postCodeCommand.getGazetteer();
		String key = postCodeCommand.getKey();
		String country = postCodeCommand.getCountry();
		String srs = postCodeCommand.getSrs();
		
		logger.debug("In validator, validating...\nmaxRows=" + maxRows + "\nstartRow=" + startRow + "\nformat="
				+ format.toString());

		// convert postCode to upper-case. Fix for bug: 2197
		if(postCode != null){
			postCode = postCode.toUpperCase();
		}
		
		// validate the postCode parameter
		ValidationUtils.invokeValidator(getPostCodeValidator(), postCode, errors);

		checkErrors(errors);

		// validate the generic parameters.
		postCodeCommand.setStartRow(validateStartRow(startRow));
		postCodeCommand.setMaxRows(validateMaxRowSize(maxRows));
		postCodeCommand.setFormat(validateOutputFormat(format));
		postCodeCommand.setGazetteer(validateGazetteer(gazetteer,key));
		postCodeCommand.setCountry(validateCountry(country));
		postCodeCommand.setSrs(validateSrs(srs));
		if(postCodeCommand.getSrs().equals(BRITISH_NATIONAL_GRID)){
			postCodeCommand.setCountry(UNITED_KINGDOM);
		}
	}


	/**
	 * Gets the name commandValidator.
	 * 
	 * @return the nameValidator
	 */
	public Validator getPostCodeValidator() {
		return postCodeValidator;
	}


	/**
	 * Sets the name commandValidator.
	 * 
	 * @param nameValidator the nameValidator to set
	 */
	public void setPostCodeValidator(Validator postCodeValidator) {
		this.postCodeValidator = postCodeValidator;
	}
}
