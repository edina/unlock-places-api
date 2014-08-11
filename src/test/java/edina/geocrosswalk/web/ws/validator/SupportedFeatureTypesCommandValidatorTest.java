package edina.geocrosswalk.web.ws.validator;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.SupportedFeatureTypesCommand;

/**
 * Test cases for {@link SupportedFeatureTypesCommandValidator}.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class SupportedFeatureTypesCommandValidatorTest {
	
	private static Logger logger = Logger.getLogger(SupportedFeatureTypesCommandValidator.class);
	private SupportedFeatureTypesCommandValidator commandValidator;
	private SupportedFeatureTypesCommand command;
	private BindException errors;
	
	@Before
	public void onSetUp() {
		commandValidator = new SupportedFeatureTypesCommandValidator();
		command = new SupportedFeatureTypesCommand();
		errors = new BindException(command, "supportedFeatureTypesCommand");
	}
	
	@Test
	public void testSupports() {
		assertTrue(commandValidator.supports(SupportedFeatureTypesCommand.class));
	}
	
	@Test
	public void testValidation() {
		commandValidator.validate(command, errors);
		assertEquals(GXWFormat.GXW, command.getFormat());
//		assertEquals("none", command.getGazetteer());
		// CG changed to open
		assertEquals("open", command.getGazetteer());
		assertFalse(errors.hasErrors());
	}

}
