package edina.geocrosswalk.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;

/**
 * DAO to load the <code>UserDetails</code> for a given apikey. Used by spring
 * security during actual calls to the web service.
 * 
 * @author Brian O'Hare
 * 
 * @version $Rev: $, $Date: $
 */
public class AuthenticationUserDetailsDao extends NamedParameterJdbcDaoSupport {

	private static final String USERNAME = "username";
	private static final String ACTIVATED = "activated";
	private static final String IP = "ip";
	private static final String EXPIRES = "expires";
	private static final String TOKEN = "token";
	private static final String ROLE_DIGIMAP_USER = "ROLE_DIGIMAP_USER";

	private String userDetailsSql;


	/**
	 * Gets the <code>UserDetails</code> for the given key.
	 * 
	 * @param ip the ip
	 * 
	 * @return the <code>UserDetails</code>.
	 */
	public AuthenticationUserDetails getUserDetails(String ip) {
		String sql = getUserDetailsSql() + " :" + IP;
		SqlParameterSource namedParameters = new MapSqlParameterSource(IP, ip);
		List<UserDetails> details = getNamedParameterJdbcTemplate().query(sql, namedParameters,
				new UserDetailsRowMapper());
		if (details.size() == 0) {
			return null;
		}
		else {
			AuthenticationUserDetails userDetails = (AuthenticationUserDetails) details.get(0);
			return userDetails;
		}
	}


	/**
	 * Gets the userDetailsSql
	 * 
	 * @return the userDetailsSql
	 */
	public String getUserDetailsSql() {
		return userDetailsSql;
	}


	/**
	 * Sets the userDetailsSql
	 * 
	 * @param userDetailsSql the userDetailsSql to set
	 */
	public void setUserDetailsSql(String getUserDetailsSql) {
		this.userDetailsSql = getUserDetailsSql;
	}

	/*
	 * Static inner class to map ResultSet columns to UserDetails object
	 * properties.
	 */
	private static final class UserDetailsRowMapper implements ParameterizedRowMapper<UserDetails> {

		public UserDetails mapRow(ResultSet rs, int num) throws SQLException {
			String ip = rs.getString(IP).trim();
			String token = rs.getString(TOKEN).trim();
			String username = rs.getString(USERNAME).trim();
			boolean activated = rs.getBoolean(ACTIVATED);
			Date expires = rs.getDate(EXPIRES);
			GrantedAuthorityImpl authority = new GrantedAuthorityImpl(ROLE_DIGIMAP_USER);
			GrantedAuthority[] authorities = new GrantedAuthority[] { authority };
			AuthenticationUserDetails userDetails = new AuthenticationUserDetails(token, username, expires, ip,
					activated, authorities);
			return userDetails;
		}

	}

}
