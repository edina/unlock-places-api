package edina.geocrosswalk.web.ws.validator;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.NameAndFeatureSearchCommand;

/**
 * Validator to validate <code>NameSearchCommand</code> objects.
 * 
 * @author Joe Vernon
 * @author Brian O'Hare
 * 
 */
public class NameAndFeatureSearchCommandValidator extends AbstractGXWCommandValidator {

	private static Logger logger = Logger.getLogger(NameAndFeatureSearchCommandValidator.class);
	private Validator nameValidator;
	private Validator featureValidator;


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return NameAndFeatureSearchCommand.class.isAssignableFrom(clazz);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		
		checkErrors(errors);
		
		NameAndFeatureSearchCommand nameAndFeatureCommand = (NameAndFeatureSearchCommand) target;
		String name = nameAndFeatureCommand.getName();
		String featureType = nameAndFeatureCommand.getFeatureType();
		Integer maxRows = nameAndFeatureCommand.getMaxRows();
		Integer startRow = nameAndFeatureCommand.getStartRow();
		GXWFormat format = nameAndFeatureCommand.getFormat();
		String gazetteer = nameAndFeatureCommand.getGazetteer();
		String key = nameAndFeatureCommand.getKey();
		String country = nameAndFeatureCommand.getCountry();
		String count = nameAndFeatureCommand.getCount();
		String srs = nameAndFeatureCommand.getSrs();
		
		logger.debug("In validator, validating...\nmaxRows=" + maxRows + "\nstartRow=" + startRow + "\nformat="
				+ format.toString());

		// validate the name parameter
		ValidationUtils.invokeValidator(getNameValidator(), name, errors);

		// validate the feature type parameter
		ValidationUtils.invokeValidator(getFeatureValidator(), featureType, errors);
		
		// check for name or feature validation errors
		checkErrors(errors);

		// validate the generic parameters.
		nameAndFeatureCommand.setStartRow(validateStartRow(startRow));
		nameAndFeatureCommand.setMaxRows(validateMaxRowSize(maxRows));
		nameAndFeatureCommand.setFormat(validateOutputFormat(format));
		nameAndFeatureCommand.setGazetteer(validateGazetteer(gazetteer,key));
		nameAndFeatureCommand.setCountry(validateCountry(country));
		nameAndFeatureCommand.setSrs(validateSrs(srs));
		nameAndFeatureCommand.setCount(validateCount(count));
		
		if(nameAndFeatureCommand.getSrs().equals(BRITISH_NATIONAL_GRID)){
			nameAndFeatureCommand.setCountry(UNITED_KINGDOM);
		}
	}


	/**
	 * Gets the name validator.
	 * 
	 * @return the name validator
	 */
	public Validator getNameValidator() {
		return nameValidator;
	}


	/**
	 * Sets the name validator.
	 * 
	 * @param nameValidator the nameValidator to set
	 */
	public void setNameValidator(Validator nameValidator) {
		this.nameValidator = nameValidator;
	}


	/**
	 * Gets the feature validator.
	 * 
	 * @return the featureValidator
	 */
	public Validator getFeatureValidator() {
		return featureValidator;
	}


	/**
	 * Sets the feature validator.
	 * 
	 * @param featureValidator the featureValidator to set
	 */
	public void setFeatureValidator(Validator featureValidator) {
		this.featureValidator = featureValidator;
	}
}
