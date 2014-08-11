package edina.geocrosswalk.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 * Service interface to return a <code>UserDetails</code> object from the
 * persistence layer.
 * 
 * TODO: look at caching of user details for subsequent requests
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class AuthenticationUserDetailsService implements UserDetailsService {

	private AuthenticationUserDetailsDao userDao;


	/**
	 * Loads a <code>UserDetails</code> object for the given ip address.
	 * 
	 * @param the the ip address of the user.
	 * @return the <code>UserDetails</code>.
	 */
	public UserDetails loadUserByUsername(String ip) throws UsernameNotFoundException, DataAccessException {
		AuthenticationUserDetails userDetails = (AuthenticationUserDetails) getUserDao().getUserDetails(ip);
		return userDetails;
	}


	/**
	 * Gets the userDao
	 * 
	 * @return the userDao
	 */
	public AuthenticationUserDetailsDao getUserDao() {
		return userDao;
	}


	/**
	 * Sets the userDao
	 * 
	 * @param userDao the userDao to set
	 */
	public void setUserDao(AuthenticationUserDetailsDao userDao) {
		this.userDao = userDao;
	}

}
