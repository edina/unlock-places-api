package edina.geocrosswalk.security;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

/**
 * Custom implementation of <code>UserDetails</code>.
 * 
 * @author Brian O'Hare
 * 
 * @version $Rev: $, $Date: $
 * 
 * @see UserDetails
 */
public class AuthenticationUserDetails implements UserDetails {

	private static final long serialVersionUID = -4722174525763633394L;
	private String token;
	private String username;
	private Date expires;
	private String ip;
	private boolean activated;
	private GrantedAuthority[] authorities;


	/**
	 * Constructor.
	 * 
	 * @param token the authentication token
	 * @param username the username
	 * @param expires the expiry date
	 * @param ip the ip address
	 * @param activated account activation status
	 * @param authorities the granted authorities
	 */
	public AuthenticationUserDetails(String token, String username, Date expires, String ip, boolean activated,
			GrantedAuthority[] authorities) {
		if (StringUtils.isBlank(token)) { throw new IllegalStateException("Token must not be null"); }
		if (StringUtils.isBlank(username)) { throw new IllegalStateException("Username must not be null"); }
		if (expires == null) { throw new IllegalStateException("Date of expiry must not be null"); }
		if (authorities == null) { throw new IllegalStateException("GrantedAuthorities must not be null"); }

		this.token = token;
		this.username = username;
		this.expires = expires;
		this.ip = ip;
		this.activated = activated;
		this.authorities = authorities;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#getAuthorities()
	 */
	public GrantedAuthority[] getAuthorities() {
		return authorities;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#getPassword()
	 */
	public String getPassword() {
		return token;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#getUsername()
	 */
	public String getUsername() {
		return username;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#isAccountNonExpired()
	 */
	public boolean isAccountNonExpired() {
		if (expires.before(new Date(System.currentTimeMillis()))) {
			return false;
		}
		else {
			return true;
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#isAccountNonLocked()
	 */
	public boolean isAccountNonLocked() {
		return true;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	public boolean isCredentialsNonExpired() {
		return true;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#isEnabled()
	 */
	public boolean isEnabled() {
		return activated;
	}


	/**
	 * Gets the activated
	 * 
	 * @return the activated
	 */
	public boolean isActivated() {
		return activated;
	}


	/**
	 * Gets the expires
	 * 
	 * @return the expires
	 */
	public Date getExpires() {
		return expires;
	}


	/**
	 * Gets the token
	 * 
	 * @return the token
	 */
	public String getToken() {
		return token;
	}


	/**
	 * Gets the ip
	 * 
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

}
