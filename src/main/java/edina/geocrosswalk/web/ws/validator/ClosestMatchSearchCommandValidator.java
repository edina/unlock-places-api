package edina.geocrosswalk.web.ws.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.ClosestMatchSearchCommand;

/**
 * Validator to validate <code>ClosestMatchSearchCommand</code> objects.
 * 
 * @author Joe Vernon
 * 
 */
public class ClosestMatchSearchCommandValidator extends AbstractGXWCommandValidator {

	private Validator nameValidator;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return ClosestMatchSearchCommand.class.isAssignableFrom(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		
		ClosestMatchSearchCommand closestMatchSearchCommand = (ClosestMatchSearchCommand) target;
		Integer maxRows = closestMatchSearchCommand.getMaxRows();
		Integer startRow = closestMatchSearchCommand.getStartRow();
		GXWFormat format = closestMatchSearchCommand.getFormat();
		String gazetteer = closestMatchSearchCommand.getGazetteer();
		String key = closestMatchSearchCommand.getKey();
		String count = closestMatchSearchCommand.getCount();
		String country = closestMatchSearchCommand.getCountry();
		String srs = closestMatchSearchCommand.getSrs();
		
		// Deep params
		String searchVariants = closestMatchSearchCommand.getSearchVariants();
		String deepSrc = closestMatchSearchCommand.getDeepSrc();
		
		String clearCache = closestMatchSearchCommand.getClearCache();
		// validate the name parameter if there is one
		if (clearCache == null) {
			// validate the name parameter
			ValidationUtils.invokeValidator(getNameValidator(), closestMatchSearchCommand.getName(), errors);
		}
		
		// check errors object and throw ValidationException if errors found
		checkErrors(errors);
		
		// validate the generic parameters.
		closestMatchSearchCommand.setStartRow(validateStartRow(startRow));
		closestMatchSearchCommand.setMaxRows(validateMaxRowSize(maxRows));
		closestMatchSearchCommand.setFormat(validateOutputFormat(format));
		gazetteer = validateGazetteer(gazetteer, key);
		closestMatchSearchCommand.setGazetteer(gazetteer);
		closestMatchSearchCommand.setCount(validateCount(count));
		closestMatchSearchCommand.setCountry(validateCountry(country));
		closestMatchSearchCommand.setSrs(validateSrs(srs));
		
		
		// Add Deep params
		closestMatchSearchCommand.setSearchVariants(validateSearchVariants(searchVariants, gazetteer));
		closestMatchSearchCommand.setDeepSrc(validateDeepSrc(deepSrc, gazetteer));
		
		if(closestMatchSearchCommand.getSrs().equals(BRITISH_NATIONAL_GRID)){
			closestMatchSearchCommand.setCountry(UNITED_KINGDOM);
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