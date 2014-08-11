package edina.geocrosswalk.web.ws.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.ClosestMatchSearchCommand;
import edina.geocrosswalk.web.ws.UniqueNameSearchCommand;

/**
 * Validator to validate <code>UniqueNameSearchCommand</code> objects.
 * 
 * @author Joe Vernon
 * 
 */
public class UniqueNameSearchCommandValidator extends AbstractGXWCommandValidator {

	private Validator nameValidator;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return UniqueNameSearchCommand.class.isAssignableFrom(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		
		checkErrors(errors);

		UniqueNameSearchCommand uniqueNameSearchCommand = (UniqueNameSearchCommand) target;
		Integer maxRows = uniqueNameSearchCommand.getMaxRows();
		Integer startRow = uniqueNameSearchCommand.getStartRow();
		GXWFormat format = uniqueNameSearchCommand.getFormat();
		String gazetteer = uniqueNameSearchCommand.getGazetteer();
		String key = uniqueNameSearchCommand.getKey();
		String count = uniqueNameSearchCommand.getCount();
		String country = uniqueNameSearchCommand.getCountry();
		String srs = uniqueNameSearchCommand.getSrs();
		
		// validate the name parameter
		ValidationUtils.invokeValidator(getNameValidator(), uniqueNameSearchCommand.getName(), errors);

		// check errors object and throw ValidationException if errors found
		checkErrors(errors);
		
		// validate the generic parameters.
		uniqueNameSearchCommand.setStartRow(validateStartRow(startRow));
		uniqueNameSearchCommand.setMaxRows(validateMaxRowSize(maxRows));
		uniqueNameSearchCommand.setFormat(validateOutputFormat(format));
		uniqueNameSearchCommand.setGazetteer(validateGazetteer(gazetteer, key));
		uniqueNameSearchCommand.setCount(validateCount(count));
		uniqueNameSearchCommand.setCountry(validateCountry(country));
		uniqueNameSearchCommand.setSrs(validateSrs(srs));
		if(uniqueNameSearchCommand.getSrs().equals(BRITISH_NATIONAL_GRID)){
			uniqueNameSearchCommand.setCountry(UNITED_KINGDOM);
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