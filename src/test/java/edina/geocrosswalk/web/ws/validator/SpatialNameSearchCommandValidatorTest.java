package edina.geocrosswalk.web.ws.validator;

import static org.junit.Assert.*;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import edina.geocrosswalk.web.ws.SpatialNameSearchCommand;

/**
 * Test cases for {@link SpatialNameSearchCommandValidator}.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class SpatialNameSearchCommandValidatorTest {
	
	private static Logger logger = Logger.getLogger(SpatialNameSearchCommandValidator.class);
	private static final ResourceBundle resources = ResourceBundle.getBundle("config/ws");
	private SpatialNameSearchCommandValidator commandValidator;
	private NameValidator nameValidator;
	private BoundingBoxValidator boundinBoxValidator;
	private OperatorValidator operatorValidator;
	private SrsBboxValidator srsBboxValidator;
	private SpatialNameSearchCommand command;
	private BindException errors;
	
	@Before
	public void onSetUp() {
		commandValidator = new SpatialNameSearchCommandValidator();
		nameValidator = new NameValidator();
		boundinBoxValidator = new BoundingBoxValidator();
		operatorValidator = new OperatorValidator();
		srsBboxValidator = new SrsBboxValidator();
		String[] supportedOperators = resources.getString("supported.spatial.operators").split(",");
		String nameRegex = resources.getString("name.validation.regex");
		String maxRowSize = resources.getString("max.row.size");
		String countryList = resources.getString("country.validation.list");
		String gazetteerList = resources.getString("gazetteer.validation.list");
		String srsList = resources.getString("srs.validation.list");
		operatorValidator.setSupportedOperators(supportedOperators);
		nameValidator.setNameValidationRegex(nameRegex);
		commandValidator.setBboxValidator(boundinBoxValidator);
		commandValidator.setNameValidator(nameValidator);
		commandValidator.setOperatorValidator(operatorValidator);
		commandValidator.setSrsBboxValidator(srsBboxValidator);
		commandValidator.setMaxRowSize(new Integer(maxRowSize));
		commandValidator.setCountryList(countryList);
		commandValidator.setGazetteerList(gazetteerList);
		commandValidator.setSrsList(srsList);		
		command = new SpatialNameSearchCommand();
		errors = new BindException(command, "spatialNameCommand");
		
	}
	
	@Test
	public void testSupports() {
		assertTrue(commandValidator.supports(SpatialNameSearchCommand.class));
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
		command.setName("Edinburgh");
		command.setOperator("within");
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
		command.setOperator("within");
		command.setName("Edinburgh");
		
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
		command.setName("Edinburgh");
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
	public void testInvalidName() {
		Double minx = new Double(-3.35081720352173);
		Double miny = new Double(55.87272644042972);
		Double maxx = new Double(-3.01274991035461);
		Double maxy = new Double(55.9947509765625);
		
		command.setMinx(minx);
		command.setMiny(miny);
		command.setMaxx(maxx);
		command.setMaxy(maxy);
		command.setName("Edin<<burgh");
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
