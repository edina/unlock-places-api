package edina.geocrosswalk.service;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.service.plugins.IFormatProviderPlugin;
import edina.geocrosswalk.service.plugins.IFormatProviderPluginRegistry;
import edina.geocrosswalk.service.plugins.ISparseFormatProviderPlugin;

/**
 * Default implementation of <code>ISparseFormatProviderService</code>.
 * 

 */
public class SparseFormatProviderService implements ISparseFormatProviderService {

	IFormatProviderPluginRegistry<ISparseFormatProviderPlugin> formatPluginRegistry;




	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.IFormatProviderService#getContentTypeForFormat
	 * (edina.geocrosswalk.service.plugins.GXWFormat)
	 */
	public String getContentTypeForFormat(GXWFormat format, String callback) {
		return getFormatPluginRegistry().getPlugin(format).getContentType(callback);
	}



	/**
	 * @return the formatPluginRegistry
	 */
	public IFormatProviderPluginRegistry<ISparseFormatProviderPlugin> getFormatPluginRegistry() {
		return formatPluginRegistry;
	}


	/**
	 * @param formatPluginRegistry the formatPluginRegistry to set
	 */
	public void setFormatPluginRegistry(IFormatProviderPluginRegistry<ISparseFormatProviderPlugin> formatPluginRegistry) {
		this.formatPluginRegistry = formatPluginRegistry;
	}


    public String getFormat(IResult result, GXWFormat format, String callback, String key) throws Exception {
        ISparseFormatProviderPlugin plugin = getFormatPluginRegistry().getPlugin(format);
        String output = plugin.getXmlForDistance(result, callback, key);
        return output;
    }

}
