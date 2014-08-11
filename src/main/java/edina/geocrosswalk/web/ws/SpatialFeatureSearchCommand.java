package edina.geocrosswalk.web.ws;

import edina.geocrosswalk.domain.BoundingBox;


/**
 * Command object for <code>SpatialFeatureSearchController</code>.
 * 
 * @author Joe Vernon
 * 
 */
public class SpatialFeatureSearchCommand extends AbstractGXWCommand {

	private String featureType;
	private String operator;
	private Double minx;
	private Double maxx;
	private Double miny;
	private Double maxy;
	private String srs_in;
	private String searchVariants;
	private String deepSrc; 

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
	 * @param minx the minx to set
	 */
	public void setMinx(Double minx) {
		this.minx = minx;
	}


	/**
	 * Sets the maxx
	 *
	 * @param maxx the maxx to set
	 */
	public void setMaxx(Double maxx) {
		this.maxx = maxx;
	}


	/**
	 * Sets the miny
	 *
	 * @param miny the miny to set
	 */
	public void setMiny(Double miny) {
		this.miny = miny;
	}


	/**
	 * Sets the maxy
	 *
	 * @param maxy the maxy to set
	 */
	public void setMaxy(Double maxy) {
		this.maxy = maxy;
	}


	/**
	 * @param operator the operator to set
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
	 * @param featureType the featureType to set
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
	 * @return the bbox
	 */
	public BoundingBox getBoundingBox() {
		return new BoundingBox(getMinx(), getMaxx(), getMiny(), getMaxy());
	}


	public String getSrs_in() {
		return srs_in;
	}


	public void setSrs_in(String srs_in) {
		this.srs_in = srs_in;
	}


	public String getSearchVariants() {
		return searchVariants;
	}


	public void setSearchVariants(String searchVariants) {
		this.searchVariants = searchVariants;
	}


	public String getDeepSrc() {
		return deepSrc;
	}


	public void setDeepSrc(String deepSrc) {
		this.deepSrc = deepSrc;
	}

}
