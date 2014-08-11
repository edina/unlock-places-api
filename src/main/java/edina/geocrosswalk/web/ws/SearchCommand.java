package edina.geocrosswalk.web.ws;

import edina.geocrosswalk.domain.BoundingBox;

/**
 * Command object for <code>SearchController</code>.
 * 
 * @author Joe Vernon
 * 
 */
public class SearchCommand extends AbstractGXWCommand {

	private String name;
	private String featureType;
	private String operator;
	private Double minx;
	private Double maxx;
	private Double miny;
	private Double maxy;
	private String searchVariants;
	private String deepSrc;
	private String variantId;
	private String clearCache;

	/**
	 * Gets the minx
	 * 
	 * @return the minx
	 */
	public Double getMinx() {
		return minx;
	}

	/**
	 * Gets the maxx
	 * 
	 * @return the maxx
	 */
	public Double getMaxx() {
		return maxx;
	}

	/**
	 * Gets the miny
	 * 
	 * @return the miny
	 */
	public Double getMiny() {
		return miny;
	}

	/**
	 * Gets the maxy
	 * 
	 * @return the maxy
	 */
	public Double getMaxy() {
		return maxy;
	}

	/**
	 * Sets the minx
	 * 
	 * @param minx
	 *            the minx to set
	 */
	public void setMinx(Double minx) {
		this.minx = minx;
	}

	/**
	 * Sets the maxx
	 * 
	 * @param maxx
	 *            the maxx to set
	 */
	public void setMaxx(Double maxx) {
		this.maxx = maxx;
	}

	/**
	 * Sets the miny
	 * 
	 * @param miny
	 *            the miny to set
	 */
	public void setMiny(Double miny) {
		this.miny = miny;
	}

	/**
	 * Sets the maxy
	 * 
	 * @param maxy
	 *            the maxy to set
	 */
	public void setMaxy(Double maxy) {
		this.maxy = maxy;
	}

	/**
	 * Gets the name to search for.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name to search for.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Gets the boundingBox
	 * 
	 * @return the boundingBox
	 */
	public BoundingBox getBoundingBox() {
		return new BoundingBox(getMinx(), getMaxx(), getMiny(), getMaxy());
	}

	/**
	 * @param featureType
	 *            the featureType to set
	 */
	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	/**
	 * @return the featureType
	 */
	public String getFeatureType() {
		return featureType;
	}

	/**
	 * @return searchVariants
	 */
	public String getSearchVariants() {
		return searchVariants;
	}

	/**
	 * @param searchVariants
	 */
	public void setSearchVariants(String searchVariants) {
		this.searchVariants = searchVariants;
	}

	/**
	 * @return deepSrc
	 */
	public String getDeepSrc() {
		return deepSrc;
	}

	/**
	 * @param deepSrc
	 */
	public void setDeepSrc(String deepSrc) {
		this.deepSrc = deepSrc;
	}

	/**
	 * @return variantId
	 */
	public String getVariantId() {
		return variantId;
	}

	/**
	 * @param variantId
	 */
	public void setVariantId(String variantId) {
		this.variantId = variantId;
	}

	public String getClearCache() {
		return clearCache;
	}

	public void setClearCache(String clearCache) {
		this.clearCache = clearCache;
	}


}