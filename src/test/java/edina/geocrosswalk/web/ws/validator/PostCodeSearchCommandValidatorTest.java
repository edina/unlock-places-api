package edina.geocrosswalk.web.ws.validator;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;

import edina.geocrosswalk.web.ws.PostCodeSearchCommand;

/**
 * Test cases for {@link PostCodeSearchCommandValidator}.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class PostCodeSearchCommandValidatorTest {
	
	private static Logger logger = Logger.getLogger(PostCodeSearchCommandValidatorTest.class);
	private static final ResourceBundle resources = ResourceBundle.getBundle("config/ws");
	private PostCodeSearchCommandValidator commandValidator;
	private PostCodeValidator postCodeValidator;
	private PostCodeSearchCommand command;
	private BindException errors;
	
	@Before
	public void onSetUp() {
		commandValidator = new PostCodeSearchCommandValidator();
		postCodeValidator = new PostCodeValidator();
		String postRegex = resources.getString("postcode.validation.regex");
		postCodeValidator.setPostCodeValidationRegex(postRegex);
		logger.info("Postcode regex: " + postRegex);
		String maxRowSize = resources.getString("max.row.size");
		String countryList = resources.getString("country.validation.list");
		String gazetteerList = resources.getString("gazetteer.validation.list");
		String srsList = resources.getString("srs.validation.list");
		commandValidator.setCountryList(countryList);
		commandValidator.setGazetteerList(gazetteerList);
		commandValidator.setSrsList(srsList);	
		commandValidator.setPostCodeValidator(postCodeValidator);
		commandValidator.setMaxRowSize(new Integer(maxRowSize));
		command = new PostCodeSearchCommand();
		errors = new BindException(command, "postCodeSearchCommand");
	}
	
	@Test
	public void testSupports() {
		assertTrue(commandValidator.supports(PostCodeSearchCommand.class));
	}
	
	
	@Test
	public void testNullPostcode() {
		try {
			commandValidator.validate(command, errors);
			fail("Should have caught a validation exception for a missing postcode.");
		}
		catch (ValidationException ex) {
			logger.info(ex.getMessage());
			assertTrue(errors.hasErrors());
			assertEquals(1, errors.getErrorCount());
		}
	}
	
	@Test
	public void testInvalidPostCode() {
		command.setPostCode("%£");
		try {
			commandValidator.validate(command, errors);
		}
		catch (ValidationException ex) {
			logger.info(ex.getMessage());
		}
	}
	
	@Test
	public void testValidPostcode() {
		command.setPostCode("EH1 1AA");
		try {
			commandValidator.validate(command, errors);
			assertFalse(errors.hasErrors());
		}
		catch (ValidationException ex) {
			fail("Unexpected validation exception");
		}
	}
	
	
	@Test
	public void testValidPostcodeSector() {
		command.setPostCode("EH1 1");
		try {
			commandValidator.validate(command, errors);
			assertFalse(errors.hasErrors());
		}
		catch (ValidationException ex) {
			fail("Unexpected validation exception");
		}
	}
	
	
	
	

}
