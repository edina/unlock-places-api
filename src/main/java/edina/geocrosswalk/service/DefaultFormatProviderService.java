package edina.geocrosswalk.service;

import java.util.List;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.service.plugins.IFormatProviderPlugin;
import edina.geocrosswalk.service.plugins.IFormatProviderPluginRegistry;

/**
 * Default implementation of <code>IFormatProviderService</code>.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class DefaultFormatProviderService implements IFormatProviderService {

	IFormatProviderPluginRegistry<IFormatProviderPlugin> formatPluginRegistry;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.IFormatProviderService#getFormatForFeatures
	 * (edina.geocrosswalk.service.IResult,
	 * edina.geocrosswalk.service.plugins.GXWFormat)
	 */
	public String getFormatForFeatures(IResult result, GXWFormat format, String callback, String key) throws Exception {
		IFormatProviderPlugin plugin = getFormatPluginRegistry().getPlugin(format);
		String output = plugin.getXmlForFeatures(result, callback, key);
		return output;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.IFormatProviderService#getFormatForFeatures
	 * (edina.geocrosswalk.service.IResult,
	 * edina.geocrosswalk.service.plugins.GXWFormat)
	 */
	public String getFormatForFootprint(IResult result, GXWFormat format, String callback, String key) throws Exception {
		IFormatProviderPlugin plugin = getFormatPluginRegistry().getPlugin(format);
		String output = plugin.getXmlForFootprint(result, callback, key);
		return output;
	}

	/*
	 * (non-Javadoc)
	 * @see edina.geocrosswalk.service.IFormatProviderService#getOutputForAutoComplete(java.util.List, edina.geocrosswalk.service.plugins.GXWFormat)
	 */
    public String getOutputForAutoComplete(List<String> names, GXWFormat format, String callback) throws Exception {
        return getFormatPluginRegistry().getPlugin(format).getOutputForAutoComplete(names, callback);
    }

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


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.IFormatProviderService#getFormatForFeatures
	 * (edina.geocrosswalk.service.IResult,
	 * edina.geocrosswalk.service.plugins.GXWFormat)
	 */
	public String getFormatForFeatureTypes(IResult result, GXWFormat format, String callback) throws Exception {
		IFormatProviderPlugin plugin = getFormatPluginRegistry().getPlugin(format);
		String output = plugin.getXmlForFeatureTypes(result, callback);
		return output;
	}


	/**
	 * @return the formatPluginRegistry
	 */
	public IFormatProviderPluginRegistry<IFormatProviderPlugin> getFormatPluginRegistry() {
		return formatPluginRegistry;
	}


	/**
	 * @param formatPluginRegistry the formatPluginRegistry to set
	 */
	public void setFormatPluginRegistry(IFormatProviderPluginRegistry<IFormatProviderPlugin> formatPluginRegistry) {
		this.formatPluginRegistry = formatPluginRegistry;
	}


	/* (non-Javadoc)
	 * @see edina.geocrosswalk.service.IFormatProviderService#getFormatForDeepAttestation(edina.geocrosswalk.service.IResult, edina.geocrosswalk.service.plugins.GXWFormat, java.lang.String)
	 */
	public String getFormatForDeepAttestation(IResult result, GXWFormat format, String callback) throws Exception {
		return getFormatPluginRegistry().getPlugin(format).getOutputForDeepAttestation(result, callback);
	}
}
