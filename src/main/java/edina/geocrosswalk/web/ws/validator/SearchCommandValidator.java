package edina.geocrosswalk.web.ws.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edina.geocrosswalk.domain.BoundingBox;
import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.SearchCommand;

/**
 * Validator to validate <code>SearchCommand</code> objects.
 * 
 * @author Joe Vernon
 * 
 */
public class SearchCommandValidator extends AbstractGXWCommandValidator {

	private Validator nameValidator;
	private Validator featureValidator;
	private Validator bboxValidator;
	private Validator operatorValidator;

	/*
	 * @see
	 * edina.crosswalk.ws.validation.AbstractCrosswalkCommandValidator#supports
	 * (java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return SearchCommand.class.isAssignableFrom(clazz);
	}

	/*
	 * @see
	 * edina.crosswalk.ws.validation.AbstractCrosswalkCommandValidator#validate
	 * (java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {

		SearchCommand searchCommand = (SearchCommand) target;
		String name = searchCommand.getName();
		String variantId = searchCommand.getVariantId();
		String featureType = searchCommand.getFeatureType();
		String operator = searchCommand.getOperator();
		BoundingBox bbox = searchCommand.getBoundingBox();
		Integer maxRows = searchCommand.getMaxRows();
		Integer startRow = searchCommand.getStartRow();
		GXWFormat format = searchCommand.getFormat();
		String gazetteer = searchCommand.getGazetteer();
		String key = searchCommand.getKey();
		String country = searchCommand.getCountry();
		String count = searchCommand.getCount();
		String srs = searchCommand.getSrs();
		Integer startYear = searchCommand.getStartYear();
		Integer endYear = searchCommand.getEndYear();
		String realSpatial = searchCommand.getRealSpatial();
		String duplicates = searchCommand.getDuplicates();
		Integer buffer = searchCommand.getBuffer();
		String childTypes = searchCommand.getChildTypes();

		String searchVariants = searchCommand.getSearchVariants();
		String deepSrc = searchCommand.getDeepSrc();

		// validate the name parameter if there is one
		if (name == null) {
			name = "";
		} else {
			ValidationUtils.invokeValidator(getNameValidator(), name, errors);
		}

		// validate the feature parameter if there is one

		// Nasty hack - we should still validate a feature param if it exists
		// but as it's not compulsory the request should fail if not present
		if (featureType != null)
			ValidationUtils.invokeValidator(getFeatureValidator(), featureType, errors);

		// validate the minimum bounding box
		ValidationUtils.invokeValidator(getBboxValidator(), bbox, errors);

		// validate the operator
		ValidationUtils.invokeValidator(getOperatorValidator(), operator, errors);

		// validate the paging params - set defaults if invalid
		searchCommand.setMaxRows(validateMaxRowSize(maxRows));
		searchCommand.setStartRow(validateStartRow(startRow));
		searchCommand.setFormat(validateOutputFormat(format));

		gazetteer = validateGazetteer(gazetteer, key);
		searchCommand.setGazetteer(gazetteer);
		searchCommand.setCountry(validateCountry(country));
		searchCommand.setCount(validateCount(count));
		searchCommand.setRealSpatial(validateRealSpatial(realSpatial));
		searchCommand.setDuplicates(validateDuplicates(duplicates));

		searchCommand.setSrs(validateSrs(srs));
		if (searchCommand.getSrs().equals(BRITISH_NATIONAL_GRID)) {
			searchCommand.setCountry(UNITED_KINGDOM);
		}
		searchCommand.setStartYear(validateStartYear(startYear));
		searchCommand.setEndYear(validateEndYear(endYear));
		searchCommand.setBuffer(validateBuffer(buffer));
		searchCommand.setChildTypes(validateChildTypes(childTypes));

		searchCommand.setVariantId(validateVariantId(variantId, gazetteer, format, searchCommand));
	
		searchCommand.setSearchVariants(validateSearchVariants(searchVariants, gazetteer));
		searchCommand.setDeepSrc(validateDeepSrc(deepSrc, gazetteer));
		// check for validation errors
		checkErrors(errors);
	}

	/**
	 * Gets the bboxValidator
	 * 
	 * @return the bboxValidator
	 */
	public Validator getBboxValidator() {
		return bboxValidator;
	}

	/**
	 * Sets the bboxValidator
	 * 
	 * @param bboxValidator
	 *            the bboxValidator to set
	 */
	public void setBboxValidator(Validator bboxValidator) {
		this.bboxValidator = bboxValidator;
	}

	/**
	 * Gets the nameValidator
	 * 
	 * @return the nameValidator
	 */
	public Validator getNameValidator() {
		return nameValidator;
	}

	/**
	 * Sets the nameValidator
	 * 
	 * @param nameValidator
	 *            the nameValidator to set
	 */
	public void setNameValidator(Validator nameValidator) {
		this.nameValidator = nameValidator;
	}

	/**
	 * @param featureValidator
	 *            the featureValidator to set
	 */
	public void setFeatureValidator(Validator featureValidator) {
		this.featureValidator = featureValidator;
	}

	/**
	 * @return the featureValidator
	 */
	public Validator getFeatureValidator() {
		return featureValidator;
	}

	/**
	 * Gets the operatorValidator
	 * 
	 * @return the operatorValidator
	 */
	public Validator getOperatorValidator() {
		return operatorValidator;
	}

	/**
	 * Sets the operatorValidator
	 * 
	 * @param operatorValidator
	 *            the operatorValidator to set
	 */
	public void setOperatorValidator(Validator operatorValidator) {
		this.operatorValidator = operatorValidator;
	}
}