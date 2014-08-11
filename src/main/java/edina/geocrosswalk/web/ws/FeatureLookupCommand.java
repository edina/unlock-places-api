package edina.geocrosswalk.web.ws;

/**
 * Command object for <code>FeatureLookupController</code>.
 * 
 * @author Joe Vernon
 * 
 */
public class FeatureLookupCommand extends AbstractGXWCommand {

	// Support for either 'id' or 'identifier' parameters
	
	private String identifier;
	private String searchVariants;
	private String deepSrc; 
	
	/**
	 * Sets the identifier to retrieve footprint for.
	 * 
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Gets the identifier to retrieve footprint for.
	 * 
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the id to retrieve footprint for.
	 * 
	 * @param id the identifier to set
	 */
	public void setId(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Gets the id to retrieve footprint for.
	 * 
	 * @return the id
	 */
	public String getId() {
		return identifier;
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