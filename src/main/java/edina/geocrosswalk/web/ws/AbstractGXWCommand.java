package edina.geocrosswalk.web.ws;

import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Abstract command object for Unlock Web Services.
 * 
 * @author Brian O'Hare
 * @author Joe Vernon
 * 
 */
public abstract class AbstractGXWCommand {

	private GXWFormat format = GXWFormat.GXW;
	private Integer maxRows;
	private Integer startRow;
	private String key;
	private String gazetteer;
	private String count;
	private String callback;
	private String country;
	private String srs;
	private Integer startYear;
	private Integer endYear;
	private Integer spatialMask;
	private String realSpatial;
	private Integer buffer;
	private String duplicates;
	private String childTypes;
	
	/**
	 * Gets the format
	 * 
	 * @return the format
	 */
	public GXWFormat getFormat() {
		return format;
	}

	/**
	 * Sets the format
	 * 
	 * @param format the format to set
	 */
	public void setFormat(GXWFormat format) {
		this.format = format;
	}

	/**
	 * Gets the maxResults
	 * 
	 * @return the maxResults
	 */
	public Integer getMaxRows() {
		return maxRows;
	}

	/**
	 * Sets the maxResults
	 * 
	 * @param maxResults the maxResults to set
	 */
	public void setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
	}

	/**
	 * Gets the startRow
	 * 
	 * @return the startRow
	 */
	public Integer getStartRow() {
		return startRow;
	}

	/**
	 * Sets the startRow
	 * 
	 * @param startRow the startRow to set
	 */
	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	/**
	 * Gets the key
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the key
	 * 
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @param gazetteer the gazetteer to set
	 */
	public void setGazetteer(String gazetteer) {
		this.gazetteer = gazetteer;
	}

	/**
	 * @return the gazetteer
	 */
	public String getGazetteer() {
		return gazetteer;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(String count) {
		this.count = count;
	}

	/**
	 * @return the count
	 */
	public String getCount() {
		return count;
	}

	/**
	 * @param callback the callback to set
	 */
	public void setCallback(String callback) {
		this.callback = callback;
	}

	/**
	 * @return the callback
	 */
	public String getCallback() {
		return callback;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param srs the srs to set
	 */
	public void setSrs(String srs) {
		this.srs = srs;
	}

	/**
	 * @return the srs
	 */
	public String getSrs() {
		return srs;
	}

	/**
	 * @param startYear the startDate to set
	 */
	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	/**
	 * @return the startYear
	 */
	public Integer getStartYear() {
		return startYear;
	}

	/**
	 * @param endDate the endYear to set
	 */
	public void setEndYear(Integer endDate) {
		this.endYear = endDate;
	}

	/**
	 * @return the endYear
	 */
	public Integer getEndYear() {
		return endYear;
	}

	/**
	 * @param spatialMask the spatialMask to set
	 */
	public void setSpatialMask(Integer spatialMask) {
		this.spatialMask = spatialMask;
	}

	/**
	 * @return the spatialMask
	 */
	public Integer getSpatialMask() {
		return spatialMask;
	}

	/**
	 * @param realSpatial the realSpatial to set
	 */
	public void setRealSpatial(String realSpatial) {
		this.realSpatial = realSpatial;
	}

	/**
	 * @return the realSpatial
	 */
	public String getRealSpatial() {
		return realSpatial;
	}

	/**
	 * @param buffer the buffer to set
	 */
	public void setBuffer(Integer buffer) {
		this.buffer = buffer;
	}

	/**
	 * @return the buffer
	 */
	public Integer getBuffer() {
		return buffer;
	}

	/**
	 * @param duplicates the duplicates to set
	 */
	public void setDuplicates(String duplicates) {
		this.duplicates = duplicates;
	}

	/**
	 * @return the duplicates
	 */
	public String getDuplicates() {
		return duplicates;
	}

	/**
	 * @param childTypes the childTypes to set
	 */
	public void setChildTypes(String childTypes) {
		this.childTypes = childTypes;
	}

	/**
	 * @return the childTypes
	 */
	public String getChildTypes() {
		return childTypes;
	}
}