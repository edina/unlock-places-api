package edina.geocrosswalk.service.plugins;

import java.util.List;
import java.util.Map;

/**
 * Defines a registry of <code>IFormatProviderPlugin</code>s.
 * 
 * 
 * @author Brian O'Hare
 * 
 */
public interface IFormatProviderPluginRegistry<T extends ProviderPlugin> {

	/**
	 * Sets the registered plugins for this registry.
	 * 
	 * @param plugins the plugins to register.
	 */
	public void setRegisteredPlugins(Map<GXWFormat, T> plugins);


	/**
	 * Gets the registered plugins.
	 * 
	 * @return a <code>Map</code> of registered plugins.
	 */
	public Map<GXWFormat, T> getRegisteredPlugins();


	/**
	 * Gets a <code>List</code> of registered plugins.
	 * 
	 * @return
	 */
	public List<T> getFormatProviderPlugins();


	/**
	 * Registers a <code>IFormatProvider</code> plugin using the provided
	 * <code>GXWFormat</code> as a key.
	 * 
	 * @param format the format
	 * @param plugin the plugin
	 * @return the registered plugin.
	 */
	public T registerPlugin(GXWFormat format, T plugin);


	/**
	 * Gets the <code>IFormatProviderPlugin</code> for the provided
	 * <code>GXWFormat</code>.
	 * 
	 * @param format the format.
	 * @return the plugin.
	 */
	public T getPlugin(GXWFormat format);
}
