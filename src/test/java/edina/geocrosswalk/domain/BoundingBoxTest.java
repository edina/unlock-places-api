package edina.geocrosswalk.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link BoundingBox}.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class BoundingBoxTest {
	
	public BoundingBox bbox1;
	public BoundingBox bbox2;
	public BoundingBox bbox3;
	
	@Before
	public void onSetUp() {
		bbox1 = new BoundingBox(-3.35081720352173,-3.01274991035461,55.87272644042972, 55.9947509765625);
		
		bbox2 = new BoundingBox(-3.35081720352173,-3.01274991035461,55.87272644042972,55.9947509765625);
		
		bbox3 = new BoundingBox(-3.35081720352,-3.51274991035461,55.87272644042972,55.9947509765625);
	}
	
	@Test
	public void testEquality() {
		assertEquals(bbox1, bbox2);
		assertEquals(bbox1.hashCode(), bbox2.hashCode());
		assertNotSame(bbox3, bbox1);
	}
	
	@Test
	public void testValidity() {
		assertTrue(bbox1.isValid());
		assertFalse(bbox3.isValid());
	}
	
	@Test
	public void testNulls() {
		assertNotNull(bbox1.getMaxx());
		assertNotNull(bbox1.getMaxy());
		assertNotNull(bbox1.getMinx());
		assertNotNull(bbox1.getMiny());
	}
}
