package edina.geocrosswalk.web.ws;

/**
 * Command object for <code>FootprintLookupController</code>.
 * 
 * @author Joe Vernon
 * 
 */
public class FootprintLookupCommand extends AbstractGXWCommand {

	// Support for either 'id' or 'identifier' parameters
	
	private String identifier;
	private String clearCache;
	
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
	
	public String getClearCache() {
		return clearCache;
	}

	public void setClearCache(String clearCache) {
		this.clearCache = clearCache;
	}


}