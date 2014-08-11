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
public class DefaultFormatProviderPluginRegistryTest extends AbstractGXWServiceTest {

	private DefaultFormatProviderPluginRegistry pluginRegistry;
	GXWFormatProviderPlugin testPlugin;


	@Before
	public void onSetUp() {
		pluginRegistry = (DefaultFormatProviderPluginRegistry) applicationContext
				.getBean("formatProviderPluginRegistry");
		testPlugin = new GXWFormatProviderPlugin();
	}


	@Test
	public void testGetFormatProviderPlugins() {
		List<IFormatProviderPlugin> plugins = pluginRegistry.getFormatProviderPlugins();
		assertNotNull(plugins);
		assertEquals(5, plugins.size());
	}


	@Test
	public void testGetPlugin() {
		ProviderPlugin plugin = pluginRegistry.getPlugin(GXWFormat.GXW);
		assertNotNull(plugin);
		assertEquals(testPlugin, plugin);
	}


	@Test
	public void testGetRegisteredPlugins() {
		Map<GXWFormat, IFormatProviderPlugin> plugins = pluginRegistry.getRegisteredPlugins();
		assertNotNull(plugins);
		assertEquals(5, plugins.size());
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
		Map<GXWFormat, IFormatProviderPlugin> plugins = new HashMap<GXWFormat, IFormatProviderPlugin>();
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
		Map<GXWFormat, IFormatProviderPlugin> plugins = new HashMap<GXWFormat, IFormatProviderPlugin>();
		plugins.put(GXWFormat.GXW, testPlugin);
		pluginRegistry.putAll(plugins);
		assertEquals(1, pluginRegistry.size());
		pluginRegistry.clear();
		assertEquals(0, pluginRegistry.size());
		pluginRegistry.put(GXWFormat.GXW, testPlugin);
		assertEquals(1, pluginRegistry.size());
		pluginRegistry.remove(GXWFormat.GXW);
		assertEquals(0, pluginRegistry.size()); 
		Collection<IFormatProviderPlugin> coll = pluginRegistry.values();
		assertNotNull(coll);
		assertEquals(0, coll.size());
	}

}
