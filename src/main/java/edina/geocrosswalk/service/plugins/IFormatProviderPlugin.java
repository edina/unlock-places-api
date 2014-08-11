package edina.geocrosswalk.service.plugins;

import java.util.List;

import edina.geocrosswalk.service.IResult;

/**
 * Interface defining geocrosswalk format providers. Anticipated geocrosswalk
 * return formats include:
 * <ul>
 * <li>GXW</li>
 * <li>KML</li>
 * <li>JSON</li>
 * <li>GeoRSS</li>
 * <li>TXT</li>
 * <li>HTML</li>
 * </ul>
 * 
 * @author Brian O'Hare
 * @version $Rev$, $Date:$
 */
public interface IFormatProviderPlugin extends ProviderPlugin {

	/**
	 * Gets a DOM Document for the provided list of features.
	 * 
	 * @param features the features
	 * @return the document
	 */
	public String getXmlForFeatures(IResult result, String callback, String key) throws Exception;


	/**
	 * Gets a DOM Document for the provided <code>IFootprint</code>.
	 * 
	 * @param footprint the footprint.
	 * @return the document.
	 */
	public String getXmlForFootprint(IResult result, String callback, String key) throws Exception;


	/**
	 * Gets a DOM Document for the provided list of features.
	 * 
	 * @param features the features
	 * @return the document
	 */
	public String getXmlForFeatureTypes(IResult result, String callback) throws Exception;
	
	
	/**
	 * Get output string for auto-complete.
	 * 
	 * @param names List of auto-complete options.
	 * @param names Name of callback (only applicable to json).
	 * @return Output string representation of results.
	 */
    public String getOutputForAutoComplete(List<String> names, String callback);

    /**
     * Get output for deepAttestation
     * @param result
     * @param callback
     * @return
     * @throws Exception 
     */
    public String getOutputForDeepAttestation(IResult result, String callback) throws Exception;
    
	/**
	 * Gets the feature format template for this plugin.
	 * 
	 * @return the name of the feature format template.
	 */
	public String getFeatureFormatTemplate();


	/**
	 * Gets the footprint format template for this plugin.
	 * 
	 * @return the name of the footprint format template.
	 */
	public String getFootprintFormatTemplate();
}
