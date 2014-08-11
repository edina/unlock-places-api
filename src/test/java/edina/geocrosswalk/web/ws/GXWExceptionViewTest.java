package edina.geocrosswalk.web.ws;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ModelMap;

/**
 * Test cases for {@link GXWExceptionView}.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class GXWExceptionViewTest {

	private GXWExceptionView view;
	private ModelMap model;
	private MockHttpServletRequest mockRequest;
	private MockHttpServletResponse mockResponse;
	private String error;
	private Integer status;
	private String contentType;
	
	
	@Before
	public void onSetUp() {
		view = new GXWExceptionView();
		model = new ModelMap();
		contentType = "text/xml;charset-utf-8";
		error = "this is an error message";
		status = new Integer(500);
		model.addAttribute("error", error);
		model.addAttribute("status", status);
		view.setContentType(contentType);
		mockResponse = new MockHttpServletResponse();
		mockRequest = new MockHttpServletRequest();
	}
	
	
	@Test
	public void testRenderMergedOutputModel() throws Exception {
		view.renderMergedOutputModel(model, mockRequest, mockResponse);
		assertEquals(error, mockResponse.getContentAsString());
	}
}
