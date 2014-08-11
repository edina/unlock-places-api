package edina.geocrosswalk.service;

import java.util.List;

import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Interface defining requirements for format provider services.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public interface IFormatProviderService {

	/**
	 * Gets a <code>String</code> representation of the output format for the
	 * supplied features result.
	 * 
	 * @param result
	 * @param format
	 * @return
	 */
	String getFormatForFeatures(IResult result, GXWFormat format, String callback, String key) throws Exception;

	/**
	 * Gets a <code>String</code> representation of the output format for the
	 * supplied footprint result.
	 * 
	 * @param result
	 * @param format
	 * @return
	 */
	String getFormatForFootprint(IResult result, GXWFormat format, String callback, String key) throws Exception;

	/**
	 * Gets a <code>String</code> representation of the output format for the
	 * supplied feature types result.
	 * 
	 * @param result
	 * @param format
	 * @return
	 */
	String getFormatForFeatureTypes(IResult result, GXWFormat format, String callback) throws Exception;
	
	/**
	 * Gets a <code>String</code> representation of the output format for the
     * supplied auto complete list.
     * 
	 * @param names List of auto complete names.
	 * @param format Outpur format. 
	 * @return String representation of result.
	 * @throws Exception
	 */
    String getOutputForAutoComplete(List<String> names, GXWFormat format, String callback) throws Exception;
    
    /**
     * Gets a <code>String</code> representation of the deepAttestation output format for the
	 * supplied feature types result.
     * @param result
     * @param format
     * @param callback
     * @return
     * @throws Exception
     */
    String getFormatForDeepAttestation(IResult result, GXWFormat format, String callback) throws Exception;
    
	/**
	 * Returns the content-type associated with a geocrosswalk format.
	 * 
	 * @param format the format
	 * @param name of callback function (json only)
	 * @return the content type.
	 */
	String getContentTypeForFormat(GXWFormat format, String callback);
}