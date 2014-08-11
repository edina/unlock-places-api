package edina.geocrosswalk.security;

import java.util.Date;

/**
 * Holds the details of a request to Unlock web services.
 * 
 * @author Brian O'Hare
 * 
 */
public class RequestDetails {

	private String ip;
	private String uri;
	private String queryString;
	private Date timestamp;
	private String key;


	public RequestDetails(String ip, String uri, String queryString, Date timestamp, String key) {
		this.ip = ip;
		this.uri = uri;
		this.queryString = queryString;
		this.timestamp = timestamp;
		this.key = key;
	}


	/**
	 * Gets the clients ip
	 * 
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}


	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}


	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}


	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}


	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}


	/**
	 * Details of the request.
	 */
	public String toString() {
		String sep = "\n";
		StringBuilder buf = new StringBuilder();
		buf.append("IP: " + getIp());
		buf.append(sep);
		buf.append("Uri: " + getUri());
		buf.append(sep);
		buf.append("Query: " + getQueryString());
		buf.append(sep);
		buf.append("Key: " + getKey());
		buf.append(sep);
		buf.append("Timestamp: " + getTimestamp());
		return buf.toString();
	}

}
