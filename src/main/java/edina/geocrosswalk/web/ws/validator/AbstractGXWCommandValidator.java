package edina.geocrosswalk.web.ws.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.output.Format;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.AbstractGXWCommand;
import edina.geocrosswalk.web.ws.SearchCommand;

/**
 * Abstract base class for Unlock web service command validators.
 * 
 * @author Joe Vernon
 * @author Brian O'Hare
 * 
 */
public abstract class AbstractGXWCommandValidator implements Validator {

	private static Logger logger = Logger.getLogger(AbstractGXWCommandValidator.class);
	private static final Integer ZERO = 0;
	private static final Integer START_ROW = 1;
	private static final Integer DEFAULT_ROWS = 20;
	private static final String DEFAULT_SRS = "4326";
	private static final String NONE = "none";
	private static final String EMPTY_STRING = "";
	private static final String UNITED_STATES = "united states";
	protected static final String UNITED_KINGDOM = "united kingdom";
	protected static final String BRITISH_NATIONAL_GRID = "27700";

	private Integer maxRowSize;
	private String gazetteerList; // list of valid gazetteers from XML config
	private String countryList; // list of valid countries from XML config
	private String srsList; // list of valid SRS codes from XML config

	/*
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public boolean supports(Class arg0) {
		// to be overridden
		return false;
	}

	/*
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */

	/**
	 * Set the default <code>callback</code> parameter
	 * 
	 */
	public String validateCallback(String callback) {
		if (StringUtils.isBlank(callback)) {
			logger.debug("Invalid 'callback' parameter: " + callback + " - setting default (" + NONE + ")");
			return null;
		} else {
			return callback;
		}
	}

	/**
	 * Set the default <code>country</code> parameter
	 */
	public String validateCountry(String country) {

		List<String> validCountries = Arrays.asList(getCountryList().split(","));

		if (StringUtils.isBlank(country)) {
			logger.debug("Invalid 'country' parameter - setting default (" + NONE + ")");
			// return EMPTY_STRING;
			return null;
		}

		else if (containsIgnoreCase(validCountries, country)) {
			// To catch some common abbreviations
			if (country.toLowerCase().equals("uk"))
				country = UNITED_KINGDOM;
			if (country.toLowerCase().equals("us"))
				country = UNITED_STATES;
			if (country.toLowerCase().equals("usa"))
				country = UNITED_STATES;
			// Returns lower case, for database entries are lower case.
			logger.debug("Using country filter: " + country.toLowerCase());
			return country.toLowerCase();
		}

		else {
			logger.debug("Invalid country specified: " + country);
			throw new ValidationException("A valid country is required to filter results.");
		}
	}

	// There's got to be a better way of doing this.
	public boolean containsIgnoreCase(List<String> l, String s) {
		Iterator<String> it = l.iterator();
		while (it.hasNext()) {
			if (it.next().equalsIgnoreCase(s))
				return true;
		}
		return false;
	}

	/**
	 * Set the default <code>count</code> parameter
	 * 
	 */
	public String validateCount(String count) {
		if (StringUtils.isBlank(count)) {
			return "true";
		}
		if (count.equals("no") || count.equals("false")) {
			return "false";
		} else {
			return "true";
		}
	}

	/**
	 * Set the default <code>realSpatial</code> parameter
	 * 
	 */
	public String validateRealSpatial(String realSpatial) {
		if (StringUtils.isBlank(realSpatial)) {
			return "false";
		}
		if (realSpatial.equals("yes") || realSpatial.equals("true")) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * Set the default <code>duplicates</code> parameter
	 * 
	 */
	public String validateDuplicates(String duplicates) {
		if (StringUtils.isBlank(duplicates)) {
			return "true";
		}
		if (duplicates.equals("no") || duplicates.equals("false")) {
			return "false";
		} else {
			return "true";
		}
	}

	/**
	 * Set the default <code>childTypes</code> parameter
	 * 
	 */
	public String validateChildTypes(String childTypes) {
		if (StringUtils.isBlank(childTypes)) {
			return "false";
		}
		if (childTypes.equals("yes") || childTypes.equals("true")) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * Checks if variantId is blank and deep is specified. Also checks format suitable for deep.
	 * @param variantId
	 * @param gazetteer
	 * @param format
	 * @param searchCommand
	 * @return
	 */
	public String validateVariantId(String variantId, String gazetteer, GXWFormat format, SearchCommand searchCommand) {

		// searchVariants only applies with deep gaz selected
		if (!gazetteer.equalsIgnoreCase("deep")) {
			return EMPTY_STRING;
		}

		if (StringUtils.isBlank(variantId)) {
			return EMPTY_STRING;
		}

		// only json and gxw is supported
		switch (format) {
		case JSON:
		case GXW:
			break;
		default:	
			searchCommand.setFormat(GXWFormat.GXW);
		}

		return variantId;
	}

	/**
	 * Ensure deep gazetteer selected and validate searchVariants param
	 * 
	 * @param searchVariants
	 * @param gazetteer
	 * @return "true" or "false"
	 */
	public String validateSearchVariants(String searchVariants, String gazetteer) {

		// searchVariants only applies with deep gaz selected
		if (!gazetteer.equalsIgnoreCase("deep")) {
			return null;
		}

		if (StringUtils.isBlank(searchVariants)) {
			return "false";
		}
		if (searchVariants.equals("yes") || searchVariants.equals("true")) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * Ensure deep gaz selected otherwise this param shouldn't be set
	 * 
	 * @param deepSrc
	 * @param gazetteer
	 * @return validated deepSrc
	 */
	public String validateDeepSrc(String deepSrc, String gazetteer) { 
		// deepSrc only applies with deep gaz selected
		if (!gazetteer.equalsIgnoreCase("deep")) {
			return null;
		}
		if (StringUtils.isBlank(deepSrc)) {
			return null;
		}

		return deepSrc;
	}

	protected void validateGenericParameters(AbstractGXWCommand command) {

		command.setStartRow(validateStartRow(command.getStartRow()));
		command.setMaxRows(validateMaxRowSize(command.getMaxRows()));
		command.setFormat(validateOutputFormat(command.getFormat()));
		command.setGazetteer(validateGazetteer(command.getGazetteer(), command.getKey()));
		command.setCountry(validateCountry(command.getCountry()));
		command.setCallback(validateCallback(command.getCallback()));
		command.setSrs(validateSrs(command.getSrs()));
		command.setCount(validateCount(command.getCount()));

		if (command.getSrs().equals(BRITISH_NATIONAL_GRID)) {
			command.setCountry(UNITED_KINGDOM);
		}
	}

	/**
	 * Validate the <code>startRow</code> parameter.
	 * 
	 * @param startRow
	 *            the <code>startRow</code> parameter to validate.
	 * @return a validated <code>startRow</code> parameter Integer.
	 */
	public Integer validateStartRow(Integer startRow) {
		if (startRow == null || startRow <= ZERO) {
			logger.debug("Invalid 'startRow' parameter: " + startRow + " - setting default (" + START_ROW + " row).");
			return START_ROW;
		} else {
			return startRow;
		}
	}

	/**
	 * Set the default <code>maxRows</code> size if parameter not within allowed
	 * range.
	 * 
	 * @param maxRows
	 *            the <code>maxRows</code> parameter to validate.
	 * @return a validated <code>maxRows</code> parameter Integer.
	 */
	public Integer validateMaxRowSize(Integer maxRows) {
		if (maxRows == null || maxRows <= ZERO || maxRows > getMaxRowSize()) {
			logger.debug("Invalid 'maxRows' parameter: " + maxRows + " - setting default (" + DEFAULT_ROWS + " rows).");
			return DEFAULT_ROWS;
		} else {
			return maxRows;
		}
	}

	/**
	 * Validates the <code>gazetteer</code> parameter to check for valid
	 * gazetteer choice.
	 * 
	 * @param gazetteer
	 *            the <code>gazetteer</code> parameter to validate.
	 * @return a validated <code>gazetteer</code> parameter.
	 */
	public String validateGazetteer(String gazetteer, String key) {
		List<String> validGazetteers = new ArrayList<String>();

		if (StringUtils.isBlank(gazetteer)) {
			logger.debug("Invalid 'gazetteer' parameter - setting default (" + NONE + ")");
			return "open";
		}

		else {
			gazetteer = gazetteer.toLowerCase();
			String[] gazetteers = StringUtils.splitPreserveAllTokens(gazetteer, ',');
			for (String gaz : gazetteers) {
				if (ArrayUtils.contains(getGazetteerList().split(","), gaz)) {
					validGazetteers.add(gaz);
				} else {
					logger.debug("Ignoring invalid gazetteer parameter: '" + gaz + "'.");
				}
			}
			if (validGazetteers.size() == 0) {
				return NONE;
			} else if (validGazetteers.contains("open")) {
				logger.debug("User searching all open gazetteers");
				return "open";
			}
		}
		String outputGazetteers = validGazetteers.toString().substring(1, validGazetteers.toString().length() - 1);
		logger.debug("Using gazetteer(s): " + outputGazetteers);
		/*
		 * if(validGazetteers.contains("os")){ if(StringUtils.isBlank(key)){
		 * logger
		 * .debug("User tried to search OS gazetteer without providing a key");
		 * throw new
		 * ValidationException("A valid key is required to search OS gazetteer"
		 * ); } }
		 */
		return outputGazetteers;
	}

	/**
	 * Validates the <code>srs</code> parameter to check for valid spatial
	 * reference system.
	 * 
	 * @param srs
	 *            the <code>srs</code> parameter to validate.
	 * @return a validated <code>srs</code> parameter.
	 */
	public String validateSrs(String srs) {
		List<String> validSrsList = Arrays.asList(getSrsList().split(","));

		if (validSrsList.contains(srs)) {
			return srs;
		}
		if (StringUtils.isBlank(srs)) {
			return DEFAULT_SRS;
		} else {
			logger.info("User entered an invalid SRS code:" + srs);
			throw new ValidationException("Invalid SRS. A valid SRS code (" + getSrsList() + ") is required to search for features.");
		}
	}

	/**
	 * Validates the output format parameter.
	 * 
	 * @param format
	 *            the format of the output
	 * @return the valid format.
	 */
	public GXWFormat validateOutputFormat(GXWFormat format) {
		if (format == null) {
			logger.debug("Invalid 'format' parameter - using default (GXW XML format).");
			return GXWFormat.GXW;
		} else {
			return format;
		}
	}

	/**
	 * Validate the <code>startYear</code> parameter.
	 * 
	 * @param startYear
	 *            the <code>startYear</code> parameter to validate.
	 * @return a validated <code>startYear</code> parameter Integer.
	 */
	public Integer validateStartYear(Integer startYear) {
		if (startYear == null || startYear <= ZERO) {
			// logger.debug("Invalid 'startYear' parameter: " + startYear +
			// " - setting default (" + DEFAULT_START_YEAR + ").");
			// return DEFAULT_START_YEAR;
			logger.debug("Invalid 'startYear' parameter: " + startYear + " - setting default (null).");
			return null;
		}

		else {
			return startYear;
		}
	}

	/**
	 * Validate the <code>endYear</code> parameter.
	 * 
	 * @param endYear
	 *            the <code>endYear</code> parameter to validate.
	 * @return a validated <code>endYear</code> parameter Integer.
	 */
	public Integer validateEndYear(Integer endYear) {
		if (endYear == null || endYear <= ZERO) {
			// logger.debug("Invalid 'endYear' parameter: " + endYear +
			// " - setting default (" + DEFAULT_END_YEAR + ").");
			// return DEFAULT_END_YEAR;
			logger.debug("Invalid 'endYear' parameter: " + endYear + " - setting default (null).");
			return null;
		} else {
			return endYear;
		}
	}

	/**
	 * Validate the <code>buffer</code> parameter.
	 * 
	 * @param buffer
	 *            the <code>buffer</code> parameter to validate.
	 * @return a validated <code>buffer</code> parameter Integer.
	 */
	public Integer validateBuffer(Integer buffer) {
		if (buffer == null || buffer <= ZERO) {
			logger.debug("Invalid 'buffer' parameter: " + buffer + " - setting default (" + ZERO + ").");
			return ZERO;
		} else {
			return buffer;
		}
	}

	/**
	 * Tests the errors object for parameter binding errors.
	 * 
	 * @param errors
	 *            the errors object to check.
	 */
	public void checkErrors(Errors errors) {
		if (errors.hasFieldErrors()) {
			FieldError fieldError = errors.getFieldError();
			String field = fieldError.getField();
			String value = (String) fieldError.getRejectedValue();
			if (StringUtils.isBlank(value)) {
				value = "null";
			}
			logger.warn("Invalid [" + field + "]" + " parameter: " + value);
			throw new ValidationException("Invalid [" + field + "]" + " parameter: " + value);
		} else if (errors.hasGlobalErrors()) {
			ObjectError error = errors.getGlobalError();
			String message = error.getDefaultMessage();
			if (StringUtils.isBlank(message)) {
				message = "Error processing request";
			}
			throw new ValidationException(message);
		}
	}

	/**
	 * Gets the default max row size.
	 * 
	 * @return the default maxRowSize
	 */
	public Integer getMaxRowSize() {
		return maxRowSize;
	}

	/**
	 * Sets the default maximum row size.
	 * 
	 * @param maxRowSize
	 *            the maxRowSize to set
	 */
	public void setMaxRowSize(Integer maxRowSize) {
		this.maxRowSize = maxRowSize;
	}

	/**
	 * Sets a list of available gazetteers.
	 * 
	 * @param gazetteerList
	 *            the gazetteerList to set
	 */
	public void setGazetteerList(String gazetteerList) {
		this.gazetteerList = gazetteerList;
	}

	/**
	 * Gets the list of available gazetteers.
	 * 
	 * @return the gazetteerList
	 */
	public String getGazetteerList() {
		return gazetteerList;
	}

	/**
	 * @param countryList
	 *            a list of valid countries
	 */
	public void setCountryList(String countryList) {
		this.countryList = countryList;
	}

	/**
	 * @return the list of valid countries
	 */
	public String getCountryList() {
		return countryList;
	}

	/**
	 * @param srsList
	 *            a list of valid spatial reference systems
	 */
	public void setSrsList(String srsList) {
		this.srsList = srsList;
	}

	/**
	 * @return the list of valid spatial reference systems
	 */
	public String getSrsList() {
		return srsList;
	}
}