package edina.geocrosswalk.web.ws;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Test cases for {@link FormatParamEditor}.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class FormatParamEditorTest {
	
	private FormatParamEditor editor;
	
	@Before
	public void onSetUp() {
		editor = new FormatParamEditor();
	}
	
	@Test
	public void testSetAsText() {
		// test correct use
		editor.setAsText("GXW");
		assertEquals(GXWFormat.GXW, editor.getValue());
		
		// test null
		editor.setAsText(null);
		assertEquals(GXWFormat.GXW, editor.getValue());
		
		// test unknown format
		editor.setAsText("BLAH");
		assertEquals(GXWFormat.GXW, editor.getValue());
	}

}
