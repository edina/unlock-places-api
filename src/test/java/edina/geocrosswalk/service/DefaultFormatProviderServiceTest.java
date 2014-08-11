package edina.geocrosswalk.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Before;
import org.junit.Test;
import org.postgis.Geometry;
import org.postgis.PGgeometry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import edina.geocrosswalk.domain.Feature;
import edina.geocrosswalk.domain.FeatureType;
import edina.geocrosswalk.domain.Footprint;
import edina.geocrosswalk.domain.IFeature;
import edina.geocrosswalk.domain.IFeatureType;
import edina.geocrosswalk.domain.IFootprint;
import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.service.plugins.IFormatProviderPlugin;
import edina.geocrosswalk.service.plugins.IFormatProviderPluginRegistry;

/**
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
@ContextConfiguration(locations = { "classpath:spring/testPluginsContext.xml" })
public class DefaultFormatProviderServiceTest extends AbstractJUnit4SpringContextTests {

	private DefaultFormatProviderService formatProviderService;
	DefaultResult result;
	Feature testFeature;
	List<IFeature> testFeatures;
	IFormatProviderPluginRegistry<IFormatProviderPlugin> registry;
	String callback = null;
	String key = null;

	@SuppressWarnings("unchecked")
    @Before
	public void onSetUp() throws Exception {
		formatProviderService = new DefaultFormatProviderService();
		registry = (IFormatProviderPluginRegistry<IFormatProviderPlugin>) applicationContext.getBean("formatProviderPluginRegistry");
		formatProviderService.setFormatPluginRegistry(registry);
		result = new DefaultResult();
		testFeature = new Feature();
		testFeature.setIdentifier(8);
		testFeature.setNameOptimised("durhamcounty");
		testFeature.setName("Durham County");
		testFeature.setGazetteer("Boundary-Line");
		testFeature.setFeatureType("county");
		testFeature.setXmin(new Double("-2.354266881942749"));
		testFeature.setXmax(new Double("-1.2380768060684204"));
		testFeature.setYmin(new Double("54.45132827758789"));
		testFeature.setYmax(new Double("54.918609619140625"));
		testFeatures = new LinkedList<IFeature>();
		testFeatures.add(testFeature);
		result.setFeatures(testFeatures);
		result.setTotalResultsCount(100);
	}


	@Test
	public void testGetFormatForFeatures() throws Exception {
		assertNotNull(formatProviderService.getFormatPluginRegistry());
		List<IFormatProviderPlugin> plugins = formatProviderService.getFormatPluginRegistry()
				.getFormatProviderPlugins();
		assertNotNull(plugins);
		assertEquals(5, plugins.size());
		String output = formatProviderService.getFormatForFeatures(result, GXWFormat.GXW, callback, key);
		assertNotNull(output);
	}


	@Test
	public void testGetContentTypeForFormat() {
		String contentType = formatProviderService.getContentTypeForFormat(GXWFormat.GXW, callback);
		assertEquals("text/xml;charset=utf-8", contentType);
	}
	
	@Test
	public void testGetFormatForFootprint() throws Exception {
		Footprint footprint = new Footprint();
		PGgeometry geometry = new PGgeometry("POINT(6 10)");
		footprint.setGeometry(geometry);
		footprint.setIdentifier(8);
		List<IFootprint> footprints = new ArrayList<IFootprint>();
		footprints.add(footprint);
		result.setFootprints(footprints);
		String output = formatProviderService.getFormatForFootprint(result, GXWFormat.GXW, callback, key);
		assertNotNull(output);
	}
	
	@Test
	public void testGetFormatForFeatureTypes() throws Exception {
		FeatureType featureType = new FeatureType();
		featureType.setFeatureCode("BAABBLAAH");
		featureType.setHierarchyLevel(2);
		featureType.setName("TestFeature");
		List<IFeatureType> featureTypes = new ArrayList<IFeatureType>();
		featureTypes.add(featureType);
		result.setFeatureTypes(featureTypes);
		String output = formatProviderService.getFormatForFeatureTypes(result, GXWFormat.GXW, callback);
		assertNotNull(output);
	}

	@Test
	public void testGetFormatForDeepAttestation() throws Exception {
		Document deepDocument = null;
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

		result.setDeepAttestations(deepDocument);
		String output = formatProviderService.getFormatForDeepAttestation(result, GXWFormat.GXW, null);
		assertNotNull(output);
	}
	
}
