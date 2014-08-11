package edina.geocrosswalk.web.ws.validator;

import static org.junit.Assert.*;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.FeatureLookupCommand;
import edina.geocrosswalk.web.ws.FootprintLookupCommand;

/**
 * Test cases for {@link FeatureLookupCommandValidator}.
 * 
 * @author Joe Vernon
 * 
 */
public class FeatureLookupCommandValidatorTest {

	private static Logger logger = Logger.getLogger(FeatureLookupCommandValidator.class);
	private static final ResourceBundle resources = ResourceBundle.getBundle("config/ws");
	FeatureLookupCommand command;
	FeatureLookupCommandValidator commandValidator;
	IdentifierValidator identifierValidator;
	BindException errors; 


	@Before
	public void onSetUp() {
		command = new FeatureLookupCommand();
		errors = new BindException(command, "footprintCommand");
		commandValidator = new FeatureLookupCommandValidator();
		identifierValidator = new IdentifierValidator();
		String regex = resources.getString("identifier.validation.regex");
		identifierValidator.setIdentifierValidationRegex(regex);

		String maxRowSize = resources.getString("max.row.size");
		String countryList = resources.getString("country.validation.list");
		String gazetteerList = resources.getString("gazetteer.validation.list");
		String srsList = resources.getString("srs.validation.list");
		
		commandValidator.setMaxRowSize(new Integer(maxRowSize));
		commandValidator.setIdentifierValidator(identifierValidator);
		commandValidator.setCountryList(countryList);
		commandValidator.setGazetteerList(gazetteerList);
		commandValidator.setSrsList(srsList);
		commandValidator.setMaxRowSize(new Integer(maxRowSize));
	}


	@Test
	public void testValidIdentifier() {
		String identifier = new String("5689129");
		command.setFormat(GXWFormat.GEORSS);
		command.setIdentifier(identifier);
		command.setId(identifier);
		commandValidator.validate(command, errors);
		assertNotNull(command.getId());
		assertFalse(errors.hasErrors());
	}


	@Test
	public void testNullIdentifier() {
		command.setIdentifier(null);
		try {
			commandValidator.validate(command, errors);
		}
		catch (ValidationException e) {
			logger.info("Caught expected validation exception: " + errors.getGlobalError().getDefaultMessage());
			assertTrue(errors.hasErrors());
			assertEquals("Missing identifier parameter", errors.getGlobalError().getDefaultMessage());
		}
	}


	@Test
	public void testNegativeIdentifier() {
		command.setIdentifier(new String("-1"));
		try {
			commandValidator.validate(command, errors);
		}
		catch (ValidationException e) {
			logger.info("Caught expected validation exception: " + errors.getGlobalError().getDefaultMessage());
			assertTrue(errors.hasErrors());
			assertEquals("Invalid identifier", errors.getGlobalError().getDefaultMessage());
		}
	}


	@Test
	public void testMaxBoundsIdentifier() {
		command.setIdentifier(new String("2147483647"));
		try {
			commandValidator.validate(command, errors);
		}
		catch (ValidationException e) {
			logger.info("Caught expected validation exception: " + errors.getGlobalError().getDefaultMessage());
			assertTrue(errors.hasErrors());
			assertEquals("Invalid identifier", errors.getGlobalError().getDefaultMessage());
		}
	}

}
