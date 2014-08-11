package edina.geocrosswalk.security;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationServiceException;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;

/**
 * Custom implementation of <code>AuthenticationProvider</code> to authenticate
 * geocrosswalk api key's.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 * 
 * @see AuthenticationProvider
 */
public class DefaultDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private static Logger s_logger = Logger.getLogger(DefaultDaoAuthenticationProvider.class);

	private UserDetailsService userDetailsService;


	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.springframework.security.providers.dao.
	 * AbstractUserDetailsAuthenticationProvider
	 * #additionalAuthenticationChecks(org
	 * .springframework.security.userdetails.UserDetails,
	 * org.springframework.security
	 * .providers.UsernamePasswordAuthenticationToken)
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.springframework.security.providers.dao.
	 * AbstractUserDetailsAuthenticationProvider#retrieveUser(java.lang.String,
	 * org
	 * .springframework.security.providers.UsernamePasswordAuthenticationToken)
	 */
	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		UserDetails userDetails = null;
		try {
			userDetails = getUserDetailsService().loadUserByUsername(username);
		}
		catch (DataAccessException ex) {
			s_logger.error(ex);
			throw new AuthenticationServiceException("Error retrieving user details", ex);
		}
		if (userDetails == null) { throw new BadCredentialsException("IP address not registered"); }
		return userDetails;
	}


	/**
	 * @return the userDetailsService
	 */
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}


	/**
	 * @param userDetailsService the userDetailsService to set
	 */
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

}
