package edina.geocrosswalk.web.ws;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sf.cglib.transform.impl.AddDelegateTransformer;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.domain.IFeature;
import edina.geocrosswalk.service.DefaultResult;
import edina.geocrosswalk.service.IFormatProviderService;
import edina.geocrosswalk.service.ISparseFormatProviderService;
import edina.geocrosswalk.service.ISpatialService;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Test cases for {@link DistanceController} 
 * 
 * 
 */
public class DistanceControllerTest {

	private DistanceController controller;
	private ISpatialService mockSpatialService;
	private ISparseFormatProviderService mockSparseFormatService;
	
	
	private DistanceCommand command;
	private DefaultResult result;
	private Integer distanceBetweenFeatures;
	private MockHttpServletRequest mockRequest;
	private MockHttpServletResponse mockResponse;
	private BindException errors;
	private String output = "This is the output";
	private String contentType = "text/xml;charset=utf-8";
	private ModelAndView mav = new ModelAndView();
    private String callback = null;
    private String key = null;

	@Before
	public void onSetUp() {
		mockRequest = new MockHttpServletRequest();
		mockResponse = new MockHttpServletResponse();
		controller = new DistanceController();
		command = new DistanceCommand();
		command.setFormat(GXWFormat.GXW);
		command.setIdA(20);
		command.setIdB(120);
		errors = new BindException(command, "nameCommand");
		result = new DefaultResult();
		distanceBetweenFeatures = 100;
		result.setDistanceBetweenFeatures(distanceBetweenFeatures);
	}

	@Test
	public void testHandle() throws Exception {
		mockSpatialService = createMock(ISpatialService.class);
		mockSparseFormatService = createMock(ISparseFormatProviderService.class);

		controller.setSpatialService(mockSpatialService);
		controller.setSparseFormatProviderService(mockSparseFormatService);
		expect(mockSpatialService.getDistanceBewteenFeatures(20, 120)).andReturn(result).once();
		expect(mockSparseFormatService.getFormat(result, GXWFormat.GXW, callback, key)).andReturn(output).once();
		expect(mockSparseFormatService.getContentTypeForFormat(GXWFormat.GXW, callback)).andReturn(contentType).once();
		replay(mockSpatialService);
		replay(mockSparseFormatService);
		mav = controller.handle(mockRequest, mockResponse, command, errors);
		assertNotNull(mav.getModelMap().get("format"));
		assertNotNull(mav.getModelMap().get("resultDocument"));
		verify(mockSpatialService);
		verify(mockSparseFormatService);
	}
}