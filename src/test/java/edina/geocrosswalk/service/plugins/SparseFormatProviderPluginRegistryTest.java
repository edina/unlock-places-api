package edina.geocrosswalk.service.plugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edina.geocrosswalk.service.AbstractGXWServiceTest;

/**
 * Test cases for <code>IFormatProviderPluginRegistry</code>.
 * 
 * @author Brian O'Hare
 * @author Joe Vernon
 * 
 */
public class SparseFormatProviderPluginRegistryTest extends AbstractGXWServiceTest {

	private SparseFormatProviderPluginRegistry pluginRegistry;
	SparseGXWFormatProviderPlugin testPlugin;


	@Before
	public void onSetUp() {
		pluginRegistry = (SparseFormatProviderPluginRegistry) applicationContext
				.getBean("sparseFormatProviderPluginRegistry");
		testPlugin = new SparseGXWFormatProviderPlugin();
	}


	@Test
	public void testGetFormatProviderPlugins() {
		List<ISparseFormatProviderPlugin> plugins = pluginRegistry.getFormatProviderPlugins();
		assertNotNull(plugins);
		//CG changed from 5
		assertEquals(3, plugins.size());
	}


	@Test
	public void testGetPlugin() {
		ProviderPlugin plugin = pluginRegistry.getPlugin(GXWFormat.GXW);
		assertNotNull(plugin);
		assertEquals(testPlugin, plugin);
	}


	@Test
	public void testGetRegisteredPlugins() {
		Map<GXWFormat, ISparseFormatProviderPlugin> plugins = pluginRegistry.getRegisteredPlugins();
		assertNotNull(plugins);
		// CG changed from 5
		assertEquals(3, plugins.size());
		ProviderPlugin plugin = plugins.get(GXWFormat.GXW);
		assertNotNull(plugin);
		assertEquals(testPlugin, plugin);
	}


	@Test
	public void testRegisterPlugin() {
		pluginRegistry.clear();
		assertEquals(0, pluginRegistry.size());
		pluginRegistry.registerPlugin(GXWFormat.GXW, testPlugin);
		assertEquals(1, pluginRegistry.size());
	}


	@Test
	public void testSetRegisteredPlugins() {
		pluginRegistry.clear();
		assertEquals(0, pluginRegistry.size());
		HashMap<GXWFormat, ISparseFormatProviderPlugin> plugins = new HashMap<GXWFormat, ISparseFormatProviderPlugin>();
		plugins.put(GXWFormat.GXW, testPlugin);
		pluginRegistry.setRegisteredPlugins(plugins);
		assertEquals(1, pluginRegistry.size());
	}


	@Test
	public void testMapMethods() {
		assertTrue(pluginRegistry.containsKey(GXWFormat.GXW));
		assertTrue(pluginRegistry.containsValue(testPlugin));
		assertEquals(1, pluginRegistry.entrySet().size());
		assertEquals(testPlugin, pluginRegistry.get(GXWFormat.GXW));
		assertFalse(pluginRegistry.isEmpty());
		assertEquals(1, pluginRegistry.keySet().size());
		pluginRegistry.clear();
		assertEquals(0, pluginRegistry.size());
		Map<GXWFormat, ISparseFormatProviderPlugin> plugins = new HashMap<GXWFormat, ISparseFormatProviderPlugin>();
		plugins.put(GXWFormat.GXW, testPlugin);
		pluginRegistry.putAll(plugins);
		assertEquals(1, pluginRegistry.size());
		pluginRegistry.clear();
		assertEquals(0, pluginRegistry.size());
		pluginRegistry.put(GXWFormat.GXW, testPlugin);
		assertEquals(1, pluginRegistry.size());
		pluginRegistry.remove(GXWFormat.GXW);
		assertEquals(0, pluginRegistry.size()); 
		Collection<ISparseFormatProviderPlugin> coll = pluginRegistry.values();
		assertNotNull(coll);
		assertEquals(0, coll.size());
	}

}
