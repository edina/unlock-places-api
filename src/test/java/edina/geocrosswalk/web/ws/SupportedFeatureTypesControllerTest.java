package edina.geocrosswalk.web.ws;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.domain.IFeatureType;
import edina.geocrosswalk.service.DefaultResult;
import edina.geocrosswalk.service.IFormatProviderService;
import edina.geocrosswalk.service.ISpatialService;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Test cases for <code>SupportedFeatureTypesController</code>. 
 * 
 * @author Joe Vernon
 * 
 */
public class SupportedFeatureTypesControllerTest {
	
	private SupportedFeatureTypesController controller;
	private ISpatialService mockSpatialService;
	private IFormatProviderService mockFormatService;
	private SupportedFeatureTypesCommand command;
	private DefaultResult result;
	private List<IFeatureType> featureTypes;
	private MockHttpServletRequest mockRequest;
	private MockHttpServletResponse mockResponse;
	private BindException errors;
	private String output = "This is the output";
	private String contentType = "text/xml;charset=utf-8";
	private ModelAndView mav = new ModelAndView();

	@Before
	public void onSetUp(){
		mockRequest = new MockHttpServletRequest();
		mockResponse = new MockHttpServletResponse();
		controller = new SupportedFeatureTypesController();
		command = new SupportedFeatureTypesCommand();
		command.setFormat(GXWFormat.GXW);
		command.setKey("a_valid_key");
		command.setGazetteer("os");
		errors = new BindException(command, "supportedFeatureTypesCommand");
		result = new DefaultResult();
		result.setTotalResultsCount(new Integer(100));
		featureTypes = new ArrayList<IFeatureType>();
		result.setFeatureTypes(featureTypes);
	}
	
	@Test 
	public void testHandle() throws Exception {
		mockSpatialService = createMock(ISpatialService.class);
		mockFormatService = createMock(IFormatProviderService.class);
		controller.setSpatialService(mockSpatialService);
		controller.setFormatProviderService(mockFormatService);
		expect(mockSpatialService.getFeatureTypes("os")).andReturn(result).once();
		expect(mockFormatService.getFormatForFeatureTypes(result, GXWFormat.GXW, null)).andReturn(output).once();
		expect(mockFormatService.getContentTypeForFormat(GXWFormat.GXW, null)).andReturn(contentType).once();
		replay(mockSpatialService);
		replay(mockFormatService);
		mav = controller.handle(mockRequest, mockResponse, command, errors);
		assertNotNull(mav.getModelMap().get("format"));
		assertNotNull(mav.getModelMap().get("resultDocument"));
		verify(mockSpatialService);
		verify(mockFormatService);
	}
}
