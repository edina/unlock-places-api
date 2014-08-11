package edina.geocrosswalk.web.ws;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.domain.BoundingBox;
import edina.geocrosswalk.domain.IFeature;
import edina.geocrosswalk.service.DefaultResult;
import edina.geocrosswalk.service.IFormatProviderService;
import edina.geocrosswalk.service.ISpatialService;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Test cases for <code>SpatialNameSearchController</code>.
 * 
 * @author Joe Vernon
 */
public class SpatialNameSearchControllerTest {

	private SpatialNameSearchController controller;
	private ISpatialService mockSpatialService;
	private IFormatProviderService mockFormatService;
	private SpatialNameSearchCommand command;
	private DefaultResult result;
	private List<IFeature> features;
	private MockHttpServletRequest mockRequest;
	private MockHttpServletResponse mockResponse;
	private BindException errors;
	private ModelAndView mav;
	private String output = "This is the output";
	private String contentType = "text/xml;charset=utf-8";
    private String callback = null;
    private String key = null;
    private String name = "Edinburgh";
    private String operator = "within";
    private String count = "true";
    private String country = null;
    private String srs = "4326";
    private String srs_in = "4326";
    private Double minx = -3.35081720352173;
    private Double miny = 55.87272644042972;
    private Double maxx = -3.01274991035461;
    private Double maxy = 55.9947509765625;
    private String searchVariants = "false";
	private String deepSrc = null;
	
	@Before
	public void onSetUp() {
		mockRequest = new MockHttpServletRequest();
		mockResponse = new MockHttpServletResponse();
		controller = new SpatialNameSearchController();
		command = new SpatialNameSearchCommand();
		command.setFormat(GXWFormat.GXW);
		command.setName(name);
		command.setMinx(minx);
		command.setMiny(miny);
		command.setMaxx(maxx);
		command.setMaxy(maxy);
		command.setOperator(operator);
		command.setStartRow(0);
		command.setMaxRows(10);
		command.setKey(key);
		command.setSrs(srs);
		command.setSrs_in(srs_in);
		command.setCountry(country);
		command.setCount(count);
		command.setSearchVariants(searchVariants);
		command.setDeepSrc(deepSrc);
		errors = new BindException(command, "spatialNameSearchCommand");
		mav = new ModelAndView();
		result = new DefaultResult();
		result.setTotalResultsCount(new Integer(100));
		features = new ArrayList<IFeature>();
		result.setFeatures(features);
	}

	@Test
	public void testHandle() throws Exception {
		BoundingBox boundingBox = new BoundingBox(-3.35081720352173,-3.01274991035461,55.87272644042972,55.9947509765625);
		mockSpatialService = createMock(ISpatialService.class);
		mockFormatService = createMock(IFormatProviderService.class);
		controller.setSpatialService(mockSpatialService);
		controller.setFormatProviderService(mockFormatService);
		expect(
				mockSpatialService.getFeaturesForNameAndBBox(name, boundingBox, operator, 0, 10, null, count, country, srs, srs_in, searchVariants, deepSrc)).andReturn(result).once();
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
	
	
	@Test
	public void testInitBinder() throws Exception {
		ServletRequestDataBinder binder = new ServletRequestDataBinder(command);
		controller.initBinder(mockRequest, binder);
		PropertyEditor editor = binder.findCustomEditor(GXWFormat.class, "format");
		assertNotNull(editor);
		assertTrue(editor instanceof FormatParamEditor);
	}
}