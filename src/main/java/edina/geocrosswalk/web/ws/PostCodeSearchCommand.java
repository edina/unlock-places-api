package edina.geocrosswalk.web.ws;

/**
 * Command object for <code>PostCodeSearchController</code>.
 * 
 * @author Joe Vernon
 * 
 */
public class PostCodeSearchCommand extends AbstractGXWCommand {

	private String postCode;


	/**
	 * Gets the postcode.
	 * 
	 * @return the postcode
	 */
	public String getPostCode() {
		return postCode;
	}


	/**
	 * Sets the postcode.
	 * 
	 * @param postcode the postcode to set
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
}
