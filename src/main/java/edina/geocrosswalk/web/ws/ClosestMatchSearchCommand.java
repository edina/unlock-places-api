package edina.geocrosswalk.web.ws;

/**
 * Command object for <code>ClosestMatchSearchController</code>.
 * 
 * @author Joe Vernon
 * 
 */
public class ClosestMatchSearchCommand extends AbstractGXWCommand {

	private String name;
	private String deepSrc;
	private String searchVariants;
	private String clearCache;


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
	
	public String getClearCache() {
		return clearCache;
	}

	public void setClearCache(String clearCache) {
		this.clearCache = clearCache;
	}


}
