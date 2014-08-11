package edina.geocrosswalk.logging;

import java.util.Date;

/**
 * Interface defining logging details.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public interface ILoggerDetails {

	/**
	 * Gets the key
	 * 
	 * @return the key
	 */
	public abstract String getKey();


	/**
	 * Gets the timestamp
	 * 
	 * @return the timestamp
	 */
	public abstract Date getTimestamp();


	/**
	 * Gets the ip
	 * 
	 * @return the ip
	 */
	public abstract String getIp();


	/**
	 * Gets the uri
	 * 
	 * @return the uri
	 */
	public abstract String getUri();


	/**
	 * Gets the query
	 * 
	 * @return the query
	 */
	public abstract String getQuery();


	/**
	 * Gets the authenticated
	 * 
	 * @return the authenticated
	 */
	public abstract int getAuthenticated();


	/**
	 * Gets the status
	 * 
	 * @return the status
	 */
	public abstract String getStatus();

}
