package edina.geocrosswalk.service;

import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Interface defining requirements for sparse format provider services.
 * 
 */
public interface ISparseFormatProviderService {

	/**
	 * Gets a <code>String</code> representation of the output format for the
	 * supplied features result.
	 * 
	 * @param result
	 * @param format
	 * @return
	 */
	String getFormat(IResult result, GXWFormat format, String callback, String key) throws Exception;


	/**
	 * Returns the content-type associated with a geocrosswalk format.
	 * 
	 * @param format the format
	 * @return the content type.
	 */
	String getContentTypeForFormat(GXWFormat format, String callback);
}