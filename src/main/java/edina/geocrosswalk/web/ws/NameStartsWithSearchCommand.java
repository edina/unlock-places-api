package edina.geocrosswalk.web.ws;

/**
 * Command object for <code>NameStartsWithSearchController</code>.
 * 
 * @author Joe Vernon
 * 
 */
public class NameStartsWithSearchCommand extends AbstractGXWCommand {

	private String name;
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