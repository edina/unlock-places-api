package edina.geocrosswalk.web.ws.validator;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import edina.geocrosswalk.web.ws.SpatialFeatureSearchCommand;

/**
 * Test cases for {@link SpatialFeatureSearchCommandValidator}.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class SpatialFeatureSearchCommandValidatorTest {
	
	private static Logger logger = Logger.getLogger(SpatialFeatureSearchCommandValidator.class);
	private static final ResourceBundle resources = ResourceBundle.getBundle("config/ws");
	private SpatialFeatureSearchCommandValidator commandValidator;
	private BoundingBoxValidator bboxValidator;
	private FeatureValidator featureValidator;
	private OperatorValidator operatorValidator;
	private SrsBboxValidator srsBboxValidator;
	private SpatialFeatureSearchCommand command;
	private BindException errors;
	
	@Before
	public void onSetUp() {
		commandValidator = new SpatialFeatureSearchCommandValidator();
		bboxValidator = new BoundingBoxValidator();
		featureValidator = new FeatureValidator();
		operatorValidator = new OperatorValidator();
		srsBboxValidator = new SrsBboxValidator();
		String featureRegex = resources.getString("feature.validation.regex");
		String maxRowSize = resources.getString("max.row.size");
		String countryList = resources.getString("country.validation.list");
		String gazetteerList = resources.getString("gazetteer.validation.list");
		String srsList = resources.getString("srs.validation.list");
		String[] supportedOperators = resources.getString("supported.spatial.operators").split(",");

		operatorValidator.setSupportedOperators(supportedOperators);
		featureValidator.setFeatureValidationRegex(featureRegex);
		
		commandValidator.setCountryList(countryList);
		commandValidator.setGazetteerList(gazetteerList);
		commandValidator.setSrsList(srsList);
		commandValidator.setMaxRowSize(new Integer(maxRowSize));
		commandValidator.setBboxValidator(bboxValidator);
		commandValidator.setFeatureValidator(featureValidator);
		commandValidator.setOperatorValidator(operatorValidator);
		commandValidator.setSrsBboxValidator(srsBboxValidator);
		command = new SpatialFeatureSearchCommand();
		errors = new BindException(command, "spatialFeatureCommand");	
	}
	
	@Test
	public void testSupports() {
		assertTrue(commandValidator.supports(SpatialFeatureSearchCommand.class));
	}
	
	@Test
	public void testValidSpatialCommand() {
		Double minx = new Double(-3.35081720352173);
		Double miny = new Double(55.87272644042972);
		Double maxx = new Double(-3.01274991035461);
		Double maxy = new Double(55.9947509765625);
		
		command.setMinx(minx);
		command.setMiny(miny);
		command.setMaxx(maxx);
		command.setMaxy(maxy);
		command.setFeatureType("cities");
		command.setOperator("within");
		command.setCountry(null);
		commandValidator.validate(command, errors);
		assertFalse(errors.hasErrors());
	}
	
	@Test
	public void testMissingCoordinateParameters() {
		Double minx = new Double(-3.35081720352173);
		Double miny = new Double(55.87272644042972);
		Double maxx = new Double(-3.01274991035461);
		Double maxy = new Double(55.9947509765625);
		
		command.setMinx(null);
		command.setMiny(miny);
		command.setMaxx(maxx);
		command.setMaxy(maxy);
		command.setFeatureType("cities");
		command.setOperator("within");
		command.setFeatureType("cities");
		
		FieldError fieldError = new FieldError("spatialFeatureCommand","minx","Invalid minx value");
		errors.addError(fieldError);
		try {
			commandValidator.validate(command, errors);
		}
		catch (ValidationException ex) {
			logger.info("Caught expected validation exception: " + ex.getMessage());
		}
	}
	
	@Test
	public void testInvalidSpatialOperator() {
		Double minx = new Double(-3.35081720352173);
		Double miny = new Double(55.87272644042972);
		Double maxx = new Double(-3.01274991035461);
		Double maxy = new Double(55.9947509765625);
		
		command.setMinx(minx);
		command.setMiny(miny);
		command.setMaxx(maxx);
		command.setMaxy(maxy);
		command.setFeatureType("cities");
		command.setOperator("bogus operator");
		try {
			commandValidator.validate(command, errors);
		}
		catch (ValidationException ex) {
			logger.info("Caught expected validation exception: " + ex.getMessage());
			assertEquals(1, errors.getErrorCount());
		}
	}
	
	@Test
	public void testInvalidFeatureType() {
		Double minx = new Double(-3.35081720352173);
		Double miny = new Double(55.87272644042972);
		Double maxx = new Double(-3.01274991035461);
		Double maxy = new Double(55.9947509765625);
		
		command.setMinx(minx);
		command.setMiny(miny);
		command.setMaxx(maxx);
		command.setMaxy(maxy);
		command.setFeatureType("");
		command.setOperator("within");
		try {
			commandValidator.validate(command, errors);
		}
		catch (ValidationException ex) {
			logger.info("Caught expected validation exception: " + ex.getMessage());
			assertEquals(1, errors.getErrorCount());
		}
	}
	
	
}
