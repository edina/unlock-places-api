package edina.geocrosswalk.web.ws;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
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
 * Test cases for <code>ClosestMatchSearchController</code>.
 * 
 * @author Joe Vernon
 * 
 */
public class ClosestMatchSearchControllerTest {

	private ClosestMatchSearchController controller;
	private ISpatialService mockSpatialService;
	private IFormatProviderService mockFormatService;
	private ClosestMatchSearchCommand command;
	private DefaultResult result;
	private List<IFeature> features;
	private MockHttpServletRequest mockRequest;
	private MockHttpServletResponse mockResponse;
	private BindException errors;
	private String output = "This is the output";
	private String contentType = "text/xml;charset=utf-8";
	private ModelAndView mav = new ModelAndView();
    private String callback = null;
    private String key = null;
    private String country = "united kingdom" ;
    private String count = "true";
	private String gazetteer = "os";
    private String srs = "4326";
	private String name = "edinburgh";

	@Before
	public void onSetUp() {
		mockRequest = new MockHttpServletRequest();
		mockResponse = new MockHttpServletResponse();
		controller = new ClosestMatchSearchController();
		command = new ClosestMatchSearchCommand();
		command.setFormat(GXWFormat.GXW);
		command.setName(name);
		command.setStartRow(0);
		command.setMaxRows(10);
		command.setKey(key);
		command.setCount(count);
		command.setCountry(country);
		command.setGazetteer(gazetteer);
		command.setSrs(srs);
		errors = new BindException(command, "closestMatchCommand");
		result = new DefaultResult();
		result.setTotalResultsCount(new Integer(100));
		features = new ArrayList<IFeature>();
		result.setFeatures(features);
	}

	@Test
	public void testHandle() throws Exception {
		mockSpatialService = createMock(ISpatialService.class);
		mockFormatService = createMock(IFormatProviderService.class);
		controller.setSpatialService(mockSpatialService);
		controller.setFormatProviderService(mockFormatService);
		expect(mockSpatialService.getFeaturesForClosestMatchSearch(name, 0, 10, gazetteer, count, country, srs, null, null)).andReturn(result).once();
		expect(mockFormatService.getFormatForFeatures(result, GXWFormat.GXW, callback, key)).andReturn(output).once();
		expect(mockFormatService.getContentTypeForFormat(GXWFormat.GXW, callback)).andReturn(contentType).once();
		replay(mockSpatialService);
		replay(mockFormatService);
		mav = controller.handle(mockRequest, mockResponse, command, errors);
		assertNotNull(mav.getModelMap().get("format"));
		assertNotNull(mav.getModelMap().get("resultDocument"));
		verify(mockSpatialService);
		verify(mockFormatService);
	}
}