package edina.geocrosswalk.logging;

import java.util.Date;

/**
 * DTO to pass data for logging to <code>DefaultAuthenticationLoggerDao</code>.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class DefaultLoggerDetails implements ILoggerDetails {

	private String key;
	private Date timestamp;
	private String ip;
	private String uri;
	private String query;
	private int authenticated;
	private String status;


	/*
	 * (non-Javadoc)
	 * 
	 * @see edina.geocrosswalk.logging.ILoggerDetails#getKey()
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


	/*
	 * (non-Javadoc)
	 * 
	 * @see edina.geocrosswalk.logging.ILoggerDetails#getTimestamp()
	 */
	public Date getTimestamp() {
		return timestamp;
	}


	/**
	 * Sets the timestamp
	 * 
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see edina.geocrosswalk.logging.ILoggerDetails#getIp()
	 */
	public String getIp() {
		return ip;
	}


	/**
	 * Sets the ip
	 * 
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see edina.geocrosswalk.logging.ILoggerDetails#getUri()
	 */
	public String getUri() {
		return uri;
	}


	/**
	 * Sets the uri
	 * 
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see edina.geocrosswalk.logging.ILoggerDetails#getQuery()
	 */
	public String getQuery() {
		return query;
	}


	/**
	 * Sets the query
	 * 
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see edina.geocrosswalk.logging.ILoggerDetails#getAuthenticated()
	 */
	public int getAuthenticated() {
		return authenticated;
	}


	/**
	 * Sets the authenticated
	 * 
	 * @param authenticated the authenticated to set
	 */
	public void setAuthenticated(int authenticated) {
		this.authenticated = authenticated;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see edina.geocrosswalk.logging.ILoggerDetails#getStatus()
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * Sets the status
	 * 
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
