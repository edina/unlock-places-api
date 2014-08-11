package edina.geocrosswalk.web.ws.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;

import edina.geocrosswalk.domain.BoundingBox;
import edina.geocrosswalk.web.ws.DistanceCommand;

/**
 * Test cases for {@link DistanceCommandValidator
 * }.
 * 
 */
public class DistanceValidatorTest {

    private DistanceCommandValidator validator;
    private BindException errors;
    DistanceCommand distanceCommand;

    @Before
    public void onSetUp() {
        validator = new DistanceCommandValidator();

        distanceCommand = new DistanceCommand();
        distanceCommand.setIdA(1234);
        distanceCommand.setIdB(1365);
        errors = new BindException(distanceCommand, "distanceCommand");
    }

    @Test
    public void testValidate() {
        // test valid
        validator.validate(distanceCommand, errors);
        assertFalse(errors.hasErrors());

        // test null
        distanceCommand = new DistanceCommand();
        validator.validate(distanceCommand, errors);
        assertTrue(errors.hasErrors());
        ObjectError error = errors.getGlobalError();
        assertNotNull(error);
        assertEquals("Missing identifier parameters", error.getDefaultMessage());
    }

    @Test
    public void testMissingParamer() {
        distanceCommand = new DistanceCommand();
        distanceCommand.setIdA(1234);
        distanceCommand.setIdB(null);

        validator.validate(distanceCommand, errors);
        assertTrue(errors.hasErrors());
        ObjectError error = errors.getGlobalError();
        assertNotNull(error);
        assertEquals("Missing identifier parameters", error.getDefaultMessage());
    }

}
