package edina.geocrosswalk.web.ws.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import edina.geocrosswalk.domain.BoundingBox;

public class SrsBboxValidatorTest {
	
	private BoundingBox bbox;
	private SrsBboxValidator validator;
	private String srs;
	private static final Logger logger = Logger.getLogger(SrsBboxValidatorTest.class);
	
	@Before
	public void onSetUp(){
		validator = new SrsBboxValidator();
		srs = "4326";
		bbox = new BoundingBox(-3.35081720352173,-3.01274991035461,55.87272644042972,55.9947509765625);
	}
	
	@Test
	public void testValidate(){
		logger.debug(validator.validate(srs, bbox));
		Boolean result = validator.validate(srs, bbox);
		assertTrue(result);
	}
	
	@Test
	public void testInvalidSrs(){
		srs = "27700";
		assertFalse(validator.validate(srs, bbox));
	}
	
	@Test
	public void testGetValidsCSystems(){
		HashMap<String,String> fb = new HashMap<String,String>();
		fb.put("4326", " WGS84");
		assertEquals(validator.getValidCSystems(bbox), fb);
	}

}
