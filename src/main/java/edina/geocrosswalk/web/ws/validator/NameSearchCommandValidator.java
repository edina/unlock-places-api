package edina.geocrosswalk.web.ws.validator;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.NameSearchCommand;

/**
 * Validator to validate <code>NameSearchCommand</code> objects.
 * 
 * @author Joe Vernon
 * @author Brian O'Hare
 * 
 */
public class NameSearchCommandValidator extends AbstractGXWCommandValidator {
	private static Logger logger = Logger.getLogger(NameSearchCommandValidator.class);

	private Validator nameValidator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return NameSearchCommand.class.isAssignableFrom(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		
		checkErrors(errors);

		NameSearchCommand nameCommand = (NameSearchCommand) target;
		Integer maxRows = nameCommand.getMaxRows();
		Integer startRow = nameCommand.getStartRow();
		GXWFormat format = nameCommand.getFormat();
		String gazetteer = nameCommand.getGazetteer();
		String key = nameCommand.getKey();
		String count = nameCommand.getCount();
		String callback = nameCommand.getCallback();
		String country = nameCommand.getCountry();
		String srs = nameCommand.getSrs();
		
		// validate the name parameter
		ValidationUtils.invokeValidator(getNameValidator(), nameCommand.getName(), errors);

		// check errors object and throw ValidationException if errors found
		checkErrors(errors);
		
		// validate the generic parameters.
		nameCommand.setStartRow(validateStartRow(startRow));
		nameCommand.setMaxRows(validateMaxRowSize(maxRows));
		nameCommand.setFormat(validateOutputFormat(format));
		nameCommand.setGazetteer(validateGazetteer(gazetteer, key));
		nameCommand.setCount(validateCount(count));
		nameCommand.setCallback(validateCallback(callback));
		nameCommand.setCountry(validateCountry(country));
		nameCommand.setSrs(validateSrs(srs));
		if(nameCommand.getSrs().equals(BRITISH_NATIONAL_GRID)){
			nameCommand.setCountry(UNITED_KINGDOM);
		}
	}


	/**
	 * Gets the name commandValidator.
	 * 
	 * @return the nameValidator
	 */
	public Validator getNameValidator() {
		return nameValidator;
	}


	/**
	 * Sets the name commandValidator.
	 * 
	 * @param nameValidator the nameValidator to set
	 */
	public void setNameValidator(Validator nameValidator) {
		this.nameValidator = nameValidator;
	}
}
