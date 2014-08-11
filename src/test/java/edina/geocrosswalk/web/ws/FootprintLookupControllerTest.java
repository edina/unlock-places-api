package edina.geocrosswalk.web.ws;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.domain.IFeature;
import edina.geocrosswalk.service.DefaultResult;
import edina.geocrosswalk.service.IFormatProviderService;
import edina.geocrosswalk.service.ISpatialService;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Test cases for <code>FootprintLookupController</code>.
 * 
 * @author Joe Vernon
 * 
 */
public class FootprintLookupControllerTest {

	private FootprintLookupController controller;
	private ISpatialService mockSpatialService;
	private IFormatProviderService mockFormatService;
	private FootprintLookupCommand command;
	private DefaultResult result;
	private List<IFeature> features;
	private MockHttpServletRequest mockRequest;
	private MockHttpServletResponse mockResponse;
	private BindException errors;
	private ModelAndView mav;
	private String output = "This is the output";
	private String contentType = "text/xml;charset=utf-8";
	private String srs = "4326";
	private String aFeatureId = "9656";

	@Before
	public void onSetUp() {
		mockRequest = new MockHttpServletRequest();
		mockResponse = new MockHttpServletResponse();
		controller = new FootprintLookupController();
		command = new FootprintLookupCommand();
		command.setFormat(GXWFormat.GXW);
		command.setIdentifier(aFeatureId);
		command.setStartRow(0);
		command.setMaxRows(10);
		command.setKey("a_valid_key");
		command.setSrs(srs);
		errors = new BindException(command, "footprintCommand");
		mav = new ModelAndView();
		result = new DefaultResult();
		result.setTotalFootprintsCount(new Integer(100));
		result.setTotalResultsCount(new Integer(100));
		features = new ArrayList<IFeature>();
		result.setFeatures(features);
	}

}