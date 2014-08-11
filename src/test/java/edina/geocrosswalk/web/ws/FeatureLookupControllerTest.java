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

import edina.geocrosswalk.domain.IFeature;
import edina.geocrosswalk.service.DefaultResult;
import edina.geocrosswalk.service.IFormatProviderService;
import edina.geocrosswalk.service.ISpatialService;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Test cases for <code>FeatureLookupController</code>.
 * 
 * @author Joe Vernon
 * 
 */
public class FeatureLookupControllerTest {

	private FeatureLookupController controller;
	private ISpatialService mockSpatialService;
	private IFormatProviderService mockFormatService;
	private FeatureLookupCommand command;
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
	private String key = null;
	private String searchVariants = "false";
	private String deepSrc = null;
	@Before
	public void onSetUp() {
		mockRequest = new MockHttpServletRequest();
		mockResponse = new MockHttpServletResponse();
		controller = new FeatureLookupController();
		command = new FeatureLookupCommand();
		command.setFormat(GXWFormat.GXW);
		command.setIdentifier(aFeatureId);
		command.setStartRow(0);
		command.setMaxRows(10);
		command.setKey(key);
		command.setSrs(srs);
		command.setSearchVariants(searchVariants);
		command.setDeepSrc(deepSrc);
		errors = new BindException(command, "footprintCommand");
		mav = new ModelAndView();
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
		expect(mockSpatialService.getFeaturesForIdentifier(aFeatureId, srs, searchVariants, deepSrc)).andReturn(result).once();
		expect(mockFormatService.getFormatForFeatures(result, GXWFormat.GXW, null, key)).andReturn(output).once();
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