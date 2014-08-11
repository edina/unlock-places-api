package edina.geocrosswalk.service.plugins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Default implementation of <code>IFormatProviderPluginRegistry</code>.
 * 
 * @author Brian O'Hare
 */
public class DefaultFormatProviderPluginRegistry implements Map<GXWFormat, IFormatProviderPlugin>,
		IFormatProviderPluginRegistry<IFormatProviderPlugin> {

	private static Logger s_logger = Logger.getLogger(DefaultFormatProviderPluginRegistry.class);

	private Map<GXWFormat, IFormatProviderPlugin> registry;


	public DefaultFormatProviderPluginRegistry() {
		registry = new EnumMap<GXWFormat, IFormatProviderPlugin>(GXWFormat.class);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.plugins.IFormatProviderPluginRegistry#getPlugin
	 * (edina.geocrosswalk.service.plugins.GXWFormat)
	 */
	public IFormatProviderPlugin getPlugin(GXWFormat format) {
		return registry.get(format);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @seeedina.geocrosswalk.service.plugins.IFormatProviderPluginRegistry#
	 * getFormatProviderPlugins()
	 */
	public List<IFormatProviderPlugin> getFormatProviderPlugins() {
		return new ArrayList<IFormatProviderPlugin>(registry.values());
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @seeedina.geocrosswalk.service.plugins.IFormatProviderPluginRegistry#
	 * getRegisteredPlugins()
	 */
	public Map<GXWFormat, IFormatProviderPlugin> getRegisteredPlugins() {
		return registry;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @seeedina.geocrosswalk.service.plugins.IFormatProviderPluginRegistry#
	 * registerPlugin(edina.geocrosswalk.service.plugins.GXWFormat,
	 * edina.geocrosswalk.service.plugins.IFormatProviderPlugin)
	 */
	public IFormatProviderPlugin registerPlugin(GXWFormat format, IFormatProviderPlugin plugin) {
		if (!plugin.supports(format)) { throw new IllegalStateException(
				"Cannot register a plugin using an Unlock format that it does not support"); }
		return registry.put(format, plugin);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @seeedina.geocrosswalk.service.plugins.IFormatProviderPluginRegistry#
	 * setRegisteredPlugins(java.util.Map)
	 */
	public void setRegisteredPlugins(Map<GXWFormat, IFormatProviderPlugin> plugins) {
		if (plugins == null) { throw new IllegalArgumentException("Plugins to register must not be null"); }
		for (GXWFormat format : plugins.keySet()) {
			registerPlugin(format, plugins.get(format));
		}
		s_logger.debug("Registry contains " + registry.size() + " plugins");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		registry.clear();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return registry.containsKey(key);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		return registry.containsValue(value);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#entrySet()
	 */
	public Set<java.util.Map.Entry<GXWFormat, IFormatProviderPlugin>> entrySet() {
		return registry.entrySet();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public IFormatProviderPlugin get(Object key) {
		return registry.get(key);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		return registry.isEmpty();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#keySet()
	 */
	public Set<GXWFormat> keySet() {
		return registry.keySet();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public IFormatProviderPlugin put(GXWFormat key, IFormatProviderPlugin value) {
		return registry.put(key, value);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends GXWFormat, ? extends IFormatProviderPlugin> m) {
		registry.putAll(m);

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public IFormatProviderPlugin remove(Object key) {
		return registry.remove(key);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#size()
	 */
	public int size() {
		return registry.size();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#values()
	 */
	public Collection<IFormatProviderPlugin> values() {
		return registry.values();
	}
}