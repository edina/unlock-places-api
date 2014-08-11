package edina.geocrosswalk.web.ws.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.NameStartsWithSearchCommand;

/**
 * Validator to validate <code>NameStartsWithSearchCommand</code> objects.
 * 
 * @author Joe Vernon
 * 
 */
public class NameStartsWithSearchCommandValidator extends AbstractGXWCommandValidator {

	private Validator nameValidator;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return NameStartsWithSearchCommand.class.isAssignableFrom(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		
		checkErrors(errors);

		NameStartsWithSearchCommand fullTextNameCommand = (NameStartsWithSearchCommand) target;
		Integer maxRows = fullTextNameCommand.getMaxRows();
		Integer startRow = fullTextNameCommand.getStartRow();
		GXWFormat format = fullTextNameCommand.getFormat();
		String gazetteer = fullTextNameCommand.getGazetteer();
		String key = fullTextNameCommand.getKey();
		String count = fullTextNameCommand.getCount();
		String country = fullTextNameCommand.getCountry();
		String srs = fullTextNameCommand.getSrs();
		
		// validate the name parameter
		ValidationUtils.invokeValidator(getNameValidator(), fullTextNameCommand.getName(), errors);

		// check errors object and throw ValidationException if errors found
		checkErrors(errors);
		
		// validate the generic parameters.
		fullTextNameCommand.setStartRow(validateStartRow(startRow));
		fullTextNameCommand.setMaxRows(validateMaxRowSize(maxRows));
		fullTextNameCommand.setFormat(validateOutputFormat(format));
		fullTextNameCommand.setGazetteer(validateGazetteer(gazetteer, key));
		fullTextNameCommand.setCount(validateCount(count));
		fullTextNameCommand.setCountry(validateCountry(country));
		fullTextNameCommand.setSrs(validateSrs(srs));
		if(fullTextNameCommand.getSrs().equals(BRITISH_NATIONAL_GRID)){
			fullTextNameCommand.setCountry(UNITED_KINGDOM);
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