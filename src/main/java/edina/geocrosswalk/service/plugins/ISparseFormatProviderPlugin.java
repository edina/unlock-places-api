package edina.geocrosswalk.service.plugins;

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
 */
public interface ISparseFormatProviderPlugin extends ProviderPlugin {

	/**
	 * Gets a DOM Document for a sparsely populated template
	 * 
	 * @param features the features
	 * @return the document
	 */
	public String getXmlForDistance(IResult result, String callback, String key) throws Exception;


	public String getTemplate();

}
