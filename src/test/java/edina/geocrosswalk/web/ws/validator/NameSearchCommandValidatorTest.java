package edina.geocrosswalk.web.ws.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import edina.geocrosswalk.web.ws.NameSearchCommand;

/**
 * Test cases for {@link NameSearchCommandValidator}.
 * 
 * @author Brian O'Hare
 * 
 */

public class NameSearchCommandValidatorTest {

	private static Logger logger = Logger.getLogger(NameSearchCommandValidator.class);
	private static final ResourceBundle resources = ResourceBundle.getBundle("config/ws");
	NameSearchCommand searchCommand;
	NameSearchCommandValidator commandValidator;
	NameValidator nameValidator;
	BindException errors;


	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		commandValidator = new NameSearchCommandValidator();
		nameValidator = new NameValidator();
		searchCommand = new NameSearchCommand();
		errors = new BindException(searchCommand, "searchCommand");
		String regex = resources.getString("name.validation.regex");
		String maxRowSize = resources.getString("max.row.size");
		String countryList = resources.getString("country.validation.list");
		String gazetteerList = resources.getString("gazetteer.validation.list");
		String srsList = resources.getString("srs.validation.list");
		
		commandValidator.setCountryList(countryList);
		commandValidator.setGazetteerList(gazetteerList);
		commandValidator.setSrsList(srsList);
		commandValidator.setMaxRowSize(new Integer(maxRowSize));
		nameValidator.setNameValidationRegex(regex);
		commandValidator.setNameValidator(nameValidator);
	}


	/*
	 * Tests supports method
	 */
	@Test
	public void testSupports() {
		assertTrue(commandValidator.supports(NameSearchCommand.class));
	}


	/*
	 * Tests empty name parameter
	 */
	@Test
	public void testEmptyName() {
		try {
			commandValidator.validate(searchCommand, errors);
		}
		catch (ValidationException e) {
			logger.info("Caught expected validation exception: " + e.getMessage());
			assertTrue(errors.hasErrors());
			assertEquals(errors.getGlobalError().getDefaultMessage(), "Missing name parameter");
		}
	}


	/*
	 * Tests validation of multiple names.
	 */
	@Test
	public void testMultipleNames() {
		try {
			// testing blank name param - validator should ignore..
			searchCommand.setName(" ,Edinburgh,Dover");
			commandValidator.validate(searchCommand, errors);
			assertFalse(errors.hasErrors());
		}
		catch (ValidationException e) {
			fail("Unexpected validation error");
		}
		try {
			// testing one invalid name param
			searchCommand.setName(" <,Edinburgh,Dover");
			commandValidator.validate(searchCommand, errors);
		}
		catch (ValidationException e) {
			assertTrue(errors.hasErrors());
			logger.info("Caught expected validation exception: " + e.getMessage());
			assertEquals(errors.getGlobalError().getDefaultMessage(), "Invalid name parameter");
		}

	}


	/*
	 * Test wildcard search.
	 */
	@Test
	public void testWildcardOnlySearch() {
		try {
			searchCommand.setName("*");
			commandValidator.validate(searchCommand, errors);
		}
		catch (ValidationException e) {
			assertTrue(errors.hasErrors());
			logger.info("Caught expected validation exception: " + e.getMessage());
			assertEquals(errors.getGlobalError().getDefaultMessage(), "Invalid name parameter");
		}
	}


	/*
	 * Tests validation of name with wildcard in middle of name eg 'Aber?dour'
	 */
	@Test
	public void testNameContainsWildcard() {
		try {
			searchCommand.setName("Aber?dour");
			commandValidator.validate(searchCommand, errors);
			assertFalse(errors.hasErrors());
		}
		catch (ValidationException e) {
			fail("Unexpected validation exception");
		}
	}


	/*
	 * Test whether a name contains invalid characters
	 */
	@Test
	public void testNameContainsInvalidChars() {
		try {
			searchCommand.setName("Aber<<dou");
			commandValidator.validate(searchCommand, errors);
		}
		catch (ValidationException e) {
			assertTrue(errors.hasErrors());
			logger.info("Caught expected validation exception: " + e.getMessage());
			assertEquals(errors.getGlobalError().getDefaultMessage(), "Invalid name parameter");
		}
	}
	
	@Test
	public void testFieldError() {
		FieldError fieldError = new FieldError("nameCommand", "maxRows", "xxx", true, null, null, "Invalid parameter value");
		errors.addError(fieldError);
		try {
			searchCommand.setName("Edinburgh");
			commandValidator.validate(searchCommand, errors);
		}
		catch (ValidationException e) {
			assertTrue(errors.hasErrors());
			logger.info("Caught expected validation exception: " + e.getMessage());
			
		}
	}
	
	@Test
	public void testGlobalError() {
		ObjectError globalError = new ObjectError("nameCommand", "Error validating parameters");
		errors.addError(globalError);
		try {
			searchCommand.setName("Edinburgh");
			commandValidator.validate(searchCommand, errors);
		}
		catch (ValidationException e) {
			assertTrue(errors.hasErrors());
			logger.info("Caught expected validation exception: " + e.getMessage());	
		}
	}

}
