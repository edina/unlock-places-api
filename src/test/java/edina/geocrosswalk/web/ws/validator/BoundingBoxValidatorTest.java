package edina.geocrosswalk.web.ws.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;

import edina.geocrosswalk.domain.BoundingBox;

/**
 * Test cases for {@link BoundingBoxValidator}.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class BoundingBoxValidatorTest {
	
	private BoundingBoxValidator validator;
	private BindException errors;
	private BoundingBox box;
	
	@Before
	public void onSetUp() {
		validator = new BoundingBoxValidator();
		box = new BoundingBox(-3.35081720352173,-3.01274991035461,55.87272644042972,55.9947509765625);
		errors = new BindException(box, "boundingBox");
	}
	
	@Test
	public void testValidate() {
		// test a valid box
		validator.validate(box, errors);
		assertFalse(errors.hasErrors());
		
		// test null box
		box = null;
		validator.validate(box, errors);
		assertTrue(errors.hasErrors());
		ObjectError error = errors.getGlobalError();
		assertNotNull(error);
		assertEquals("A bounding box is required", error.getDefaultMessage());
	}
	
	@Test
	public void testInvalidBox() {
		box = new BoundingBox(-2.35081720352173,-3.01274991035461,55.87272644042972,55.9947509765625);
		validator.validate(box, errors);
		assertTrue(errors.hasErrors());
		ObjectError error = errors.getGlobalError();
		assertNotNull(error);
		assertEquals("Invalid bounding box", error.getDefaultMessage());
	}

}
