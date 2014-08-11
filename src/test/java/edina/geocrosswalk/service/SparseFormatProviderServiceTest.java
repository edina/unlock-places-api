package edina.geocrosswalk.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.postgis.Geometry;
import org.postgis.PGgeometry;
import org.springframework.test.annotation.ExpectedException;
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
import edina.geocrosswalk.service.plugins.ISparseFormatProviderPlugin;

/**
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
@ContextConfiguration(locations = { "classpath:spring/testPluginsContext.xml" })
public class SparseFormatProviderServiceTest extends AbstractJUnit4SpringContextTests {

	private SparseFormatProviderService sparseFormatProviderService;
	DefaultResult result;
	Feature testFeature;
	List<IFeature> testFeatures;
	IFormatProviderPluginRegistry<ISparseFormatProviderPlugin> registry;
	String callback = null;
	String key = null;

	@Before
	public void onSetUp() throws Exception {
		sparseFormatProviderService = new SparseFormatProviderService();
		registry = (IFormatProviderPluginRegistry<ISparseFormatProviderPlugin>) applicationContext.getBean("sparseFormatProviderPluginRegistry");
		sparseFormatProviderService.setFormatPluginRegistry(registry);
	}
	
	

    @Test
    public void testNumberOfSparseFormatterPlugins() throws Exception {
        List<ISparseFormatProviderPlugin> plugins = sparseFormatProviderService.getFormatPluginRegistry()
        .getFormatProviderPlugins();
        assertEquals(3, plugins.size());
    }




	@Test
	public void testGetDistanceBetweenFeatures() throws Exception {

		DefaultResult result = new DefaultResult();
		result.setDistanceBetweenFeatures(100);
		String output = sparseFormatProviderService.getFormat(result, GXWFormat.GXW, callback, key);
		assertNotNull(output);
	}


	@Test
	public void testGetContentTypeForFormat() {
		String contentType = sparseFormatProviderService.getContentTypeForFormat(GXWFormat.GXW, callback);
		assertEquals("text/xml;charset=utf-8", contentType);
	}
	@Test(expected=IllegalArgumentException.class)
	 public void testIncorrectTypeForFormat() {
	    String contentType = sparseFormatProviderService.getContentTypeForFormat(GXWFormat.HTML, callback);
	 }

}
