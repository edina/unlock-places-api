package edina.geocrosswalk.web.ws;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.domain.BoundingBox;
import edina.geocrosswalk.domain.IFeature;
import edina.geocrosswalk.service.DefaultResult;
import edina.geocrosswalk.service.IFormatProviderService;
import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.ISpatialService;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Test cases for <code>SearchController</code>.
 * 
 * @author Colin Gormley
 * 
 */
public class SearchControllerTest {

	private SearchController controller;
	private ISpatialService mockSpatialService;
	private IFormatProviderService mockFormatService;
	private SearchCommand command, deepCommand;
	private DefaultResult result, deepResult;
	private List<IFeature> features;
	private MockHttpServletRequest mockRequest;
	private MockHttpServletResponse mockResponse;
	private BindException errors;
	private String output = "This is the output";
	private String contentType = "text/xml;charset=utf-8";
	private ModelAndView mav = new ModelAndView();

	private String callback = null;
	private String key = null;
	private String country = null;
	private String count = "true";
	private String gazetteer = "os";
	private String srs = "4326";
	private String name = "edinburgh";
	private String featureType = null;
	private Integer startYear = null;
	private Integer endYear = null;
	private Integer spatialMask = null;
	private String realSpatial = "false";
	private Integer buffer = new Integer(0);
	private String duplicates = "true";
	private String childTypes = "false";
	private String searchVariants = "false";
	private String deepSrc = null;
	private BoundingBox bbox = new BoundingBox(null, null, null, null);
	private Document deepDocument;

	@Before
	public void onSetUp() {
		mockRequest = new MockHttpServletRequest();
		mockResponse = new MockHttpServletResponse();
		controller = new SearchController();
		command = new SearchCommand();
		command.setFormat(GXWFormat.GXW);
		command.setName(name);
		command.setFeatureType(featureType);
		command.setStartRow(0);
		command.setMaxRows(10);
		command.setKey(key);
		command.setCount(count);
		command.setCountry(country);
		command.setGazetteer(gazetteer);
		command.setSrs(srs);
		command.setStartYear(startYear);
		command.setEndYear(endYear);
		command.setSpatialMask(spatialMask);
		command.setRealSpatial(realSpatial);
		command.setBuffer(buffer);
		command.setDuplicates(duplicates);
		command.setChildTypes(childTypes);

		// New deep parameters
		command.setSearchVariants(searchVariants);
		command.setDeepSrc(deepSrc);

		errors = new BindException(command, "searchCommand");
		result = new DefaultResult();
		result.setTotalResultsCount(new Integer(100));
		features = new ArrayList<IFeature>();
		result.setFeatures(features);

		deepCommand = new SearchCommand();

		String attestation = "<?xml version='1.0' encoding='UTF-8'?><attestations><attestation variantID='epns-deep-02-b-name-w665'>"
				+ "<date begin='1242' end='1242' subtype='simple'>1242</date><source id='bu37' style=''>Fees</source><item>873</item></attestation></attestations>";

		try {
			deepDocument = new SAXBuilder().build(new StringReader(attestation));
		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		deepResult = new DefaultResult();
		deepResult.setDeepAttestations(deepDocument);
	}

	@Test
	public void testHandle() throws Exception {
		mockSpatialService = createMock(ISpatialService.class);
		mockFormatService = createMock(IFormatProviderService.class);
		controller.setSpatialService(mockSpatialService);
		controller.setFormatProviderService(mockFormatService);

		expect(mockSpatialService.getFeatures(name, // name
				featureType, // featureType
				bbox, // bbox
				null, // operator
				0, // startRow
				10, // maxRows
				gazetteer,// gazetteer
				count, // count
				country, // country
				srs, // srs
				startYear, // startYear
				endYear, // endYear
				spatialMask, // spatialMask
				realSpatial, // realSpatial
				buffer, // buffer
				duplicates, // duplicates
				childTypes, // childTypes
				searchVariants, // searchVariants
				deepSrc // deepSrc
				)).andReturn(result).once();

		expect(mockFormatService.getFormatForFeatures(result, GXWFormat.GXW, callback, key)).andReturn(output).once();
		expect(mockFormatService.getContentTypeForFormat(GXWFormat.GXW, callback)).andReturn(contentType).once();
		replay(mockSpatialService);
		replay(mockFormatService);
		mav = controller.handle(mockRequest, mockResponse, command, errors);
		ModelMap modelMap = mav.getModelMap();

		assertNotNull(modelMap.get("format"));
		assertNotNull(modelMap.get("resultDocument"));

		verify(mockSpatialService);
		verify(mockFormatService);

	}

	@Test
	public void testHandleDeepVariantID() throws Exception {

		// Test Deep variantID search
		String variantID = "epns-deep-02-b-name-w665";
		deepCommand.setVariantId(variantID);
		deepCommand.setGazetteer("deep");

		mockSpatialService = createMock(ISpatialService.class);
		mockFormatService = createMock(IFormatProviderService.class);
		controller.setSpatialService(mockSpatialService);
		controller.setFormatProviderService(mockFormatService);

		IResult deepAttestation = mockSpatialService.getDeepAttestation(variantID);

		expect(deepAttestation).andReturn(deepResult).once();
		expect(mockFormatService.getFormatForDeepAttestation(deepResult, GXWFormat.GXW, callback)).andReturn(output).once();
		expect(mockFormatService.getContentTypeForFormat(GXWFormat.GXW, callback)).andReturn(contentType).once();
		replay(mockSpatialService);
		replay(mockFormatService);

		mav = controller.handle(mockRequest, mockResponse, deepCommand, errors);
		ModelMap modelMap = mav.getModelMap();

		assertNotNull(modelMap.get("format"));
		assertNotNull(modelMap.get("resultDocument"));

		verify(mockSpatialService);
		verify(mockFormatService);
	}
}