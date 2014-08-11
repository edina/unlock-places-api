package edina.geocrosswalk.web.ws.validator;

import static org.junit.Assert.*;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;

import edina.geocrosswalk.web.ws.NameAndFeatureSearchCommand;

/**
 * Test cases for {@link NameAndFeatureSearchCommandValidator}.
 * 
 * @author Joe Vernon
 * @author Brian O'Hare
 * 
 * @version $Rev:$, $Date:$
 */
public class NameAndFeatureSearchCommandValidatorTest {
	
	private static Logger logger = Logger.getLogger(NameAndFeatureSearchCommandValidator.class);
	private static final ResourceBundle resources = ResourceBundle.getBundle("config/ws");
	private NameAndFeatureSearchCommandValidator commandValidator;
	private NameAndFeatureSearchCommand command;
	private NameValidator nameValidator;
	private FeatureValidator featureValidator;
	private BindException errors;
	
	@Before
	public void onSetUp() {
		commandValidator = new NameAndFeatureSearchCommandValidator();
		command = new NameAndFeatureSearchCommand();
		nameValidator = new NameValidator();
		featureValidator = new FeatureValidator();
		errors = new BindException(command, "nameAndFeatureCommand");
		
		String nameRegex = resources.getString("name.validation.regex");
		String featureRegex = resources.getString("feature.validation.regex");
		String maxRowSize = resources.getString("max.row.size");
		String countryList = resources.getString("country.validation.list");
		String gazetteerList = resources.getString("gazetteer.validation.list");
		String srsList = resources.getString("srs.validation.list");

		nameValidator.setNameValidationRegex(nameRegex);
		featureValidator.setFeatureValidationRegex(featureRegex);
		commandValidator.setNameValidator(nameValidator);
		commandValidator.setFeatureValidator(featureValidator);
		commandValidator.setCountryList(countryList);
		commandValidator.setGazetteerList(gazetteerList);
		commandValidator.setSrsList(srsList);
		commandValidator.setMaxRowSize(new Integer(maxRowSize));
	}
	
	@Test
	public void testSupports() {
		assertTrue(commandValidator.supports(NameAndFeatureSearchCommand.class));
	}
	
	@Test
	public void testInvalidName() {
		command.setName("Aber<<dour");
		command.setFeatureType("cities");
		try {
			commandValidator.validate(command, errors);
		}
		catch (ValidationException ex) {
			logger.info("Caught expected validation exception: " + ex.getMessage());
			assertEquals("Invalid name parameter", ex.getMessage());
		}
	}
	
	@Test
	public void testInvalidFeature() {
		command.setName("Aberdour");
		command.setFeatureType("cit<ies");
		try {
			commandValidator.validate(command, errors);
		}
		catch (ValidationException ex) {
			logger.info("Caught expected validation exception: " + ex.getMessage());
			assertEquals("Invalid feature type", ex.getMessage());
			assertTrue(errors.hasErrors());
			assertEquals(1, errors.getErrorCount());
		}
	}
	
	@Test
	public void testValidCommand() {
		command.setName("Aberdour");
		command.setFeatureType("cities");
		commandValidator.validate(command, errors);
		assertFalse(errors.hasErrors());
	}
}