package edina.geocrosswalk.web.ws;

/**
 * Command object for <code>NameAndFeatureSearchController</code>.
 * 
 * @author Joe Vernon
 * 
 */
public class NameAndFeatureSearchCommand extends AbstractGXWCommand {

	private String name;
	private String featureType;
	private String searchVariants;
	private String deepSrc; 

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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Gets the feature type.
	 * 
	 * @return the featureType
	 */
	public String getFeatureType() {
		return featureType;
	}


	/**
	 * Sets the feature type.
	 * 
	 * @param featureType the featureType to set
	 */
	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}


	public String getDeepSrc() {
		return deepSrc;
	}


	public void setDeepSrc(String deepSrc) {
		this.deepSrc = deepSrc;
	}


	public String getSearchVariants() {
		return searchVariants;
	}


	public void setSearchVariants(String searchVariants) {
		this.searchVariants = searchVariants;
	}

}
