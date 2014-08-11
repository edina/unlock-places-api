package edina.geocrosswalk.web.ws;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ModelMap;

import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Test cases for <code>GXWView</code>.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class GXWViewTest {

	private static Logger logger = Logger.getLogger(GXWViewTest.class);
	private GXWView view;
	private ModelMap model;
	private MockHttpServletRequest mockRequest;
	private MockHttpServletResponse mockResponse;
	private String resultDocument;
	private String contentType;
	private int status;


	@Before
	public void onSetUp() {
		view = new GXWView();
		model = new ModelMap();
		resultDocument = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><unlock><test/></unlock>";
		contentType = "text/xml;charset-utf-8";
		model.addAttribute("resultDocument", resultDocument);
		view.setContentType(contentType);
		mockResponse = new MockHttpServletResponse();
		mockRequest = new MockHttpServletRequest();
		status = HttpServletResponse.SC_OK;
		model.addAttribute("status", status);
		
	}


	@Test
	public void testRenderMergedOutputModelXML() throws Exception {
		model.addAttribute("format", GXWFormat.GXW);
		view.renderMergedOutputModel(model, mockRequest, mockResponse);
		logger.info("\n" + mockResponse.getContentAsString());
		assertNotNull(mockResponse.getContentAsString());
		assertEquals(status, mockResponse.getStatus());
	}
	
	@Test
	public void testRenderMergedOutputModelContentDisposition() throws Exception {
		GXWFormat[] format = GXWFormat.values();
		for (GXWFormat gxwFormat : format) {
			model.addAttribute("format", gxwFormat);
			view.renderMergedOutputModel(model, mockRequest, mockResponse);
			logger.info("\n" + mockResponse.getContentAsString());
			assertNotNull(mockResponse.getContentAsString());
			assertEquals(status, mockResponse.getStatus());
			
			// Ensure that the content disposition header is only set for KML results
			String contentDisposition = (String) mockResponse.getHeader("Content-Disposition");
			if (gxwFormat.equals(GXWFormat.KML)){
				assertEquals(contentDisposition, "attachment; filename=unlock_search_result.kml");
			}	
			else{
				assertNull(contentDisposition);
			}
			// Reset so values from last iteration don't persist.
			model.remove(format);
			mockResponse = new MockHttpServletResponse();
		}
	}
	
	
	@Test
	public void testRenderMergedOutputModelText() throws Exception {
		model.addAttribute("format", GXWFormat.TXT);
		view.renderMergedOutputModel(model, mockRequest, mockResponse);
		logger.info("\n" + mockResponse.getContentAsString());
		assertEquals(resultDocument, mockResponse.getContentAsString());
		assertEquals(status, mockResponse.getStatus());
	}
}
