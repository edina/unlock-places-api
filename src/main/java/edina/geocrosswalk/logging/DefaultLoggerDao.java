package edina.geocrosswalk.logging;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * DAO to log authentication events.
 * 
 * @author Brian O'Hare
 * 
 * @version $Rev: $, $Date: $
 */
public class DefaultLoggerDao extends NamedParameterJdbcDaoSupport implements ILoggerDao {

	private static Logger logger = Logger.getLogger(DefaultLoggerDao.class);

	private static final String IP = "ip";
	private static final String KEY = "key";
	private static final String QUERY = "query";
	private static final String URI = "uri";
	private static final String TIMESTAMP = "timestamp";
	private static final String AUTHENTICATED = "authenticated";
	private static final String ROLE = "role";

	private String saveLoggerDetailsSql;
	private String loadLoggerDetailsSql;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.logging.ILoggerDao#saveAuthenticationDetails(edina
	 * .geocrosswalk.logging.ILoggerDetails)
	 */
	public void saveLoggerDetails(ILoggerDetails loggerDetails) {

		String key = StringUtils.defaultString(loggerDetails.getKey());
		Date timestamp = loggerDetails.getTimestamp();
		String ip = StringUtils.defaultString(loggerDetails.getIp());
		String uri = StringUtils.defaultString(loggerDetails.getUri());
		String query = StringUtils.defaultString(loggerDetails.getQuery());
		Integer authenticated = new Integer(loggerDetails.getAuthenticated());
		String status = StringUtils.defaultString(loggerDetails.getStatus());

		getJdbcTemplate().update(getSaveLoggerDetailsSql(), new Object[] { ip, key, timestamp, uri, query, authenticated, status });
	}

	/*
	 * Maps a result set to a DefaultLoggerDetails object.
	 */
	private class LoggerDetailsRowMapper implements ParameterizedRowMapper<ILoggerDetails> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.springframework.jdbc.core.simple.ParameterizedRowMapper#mapRow
		 * (java.sql.ResultSet, int)
		 */
		public ILoggerDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
			DefaultLoggerDetails loggerDetails = new DefaultLoggerDetails();
			String ip = rs.getString(IP);
			String key = rs.getString(KEY);
			String uri = rs.getString(URI);
			String query = rs.getString(QUERY);
			Integer authenticated = rs.getInt(AUTHENTICATED);
			Date timestamp = rs.getDate(TIMESTAMP);
			String status = rs.getString(ROLE);

			loggerDetails.setIp(ip);
			loggerDetails.setKey(key);
			loggerDetails.setUri(uri);
			loggerDetails.setQuery(query);
			loggerDetails.setAuthenticated(authenticated);
			loggerDetails.setStatus(status);
			loggerDetails.setTimestamp(timestamp);

			return loggerDetails;
		}
	}

	/**
	 * Gets the saveLoggerDetailsSql
	 * 
	 * @return the saveLoggerDetailsSql
	 */
	public String getSaveLoggerDetailsSql() {
		return saveLoggerDetailsSql;
	}

	/**
	 * Sets the saveLoggerDetailsSql
	 * 
	 * @param saveLoggerDetailsSql
	 *            the saveLoggerDetailsSql to set
	 */
	public void setSaveLoggerDetailsSql(String saveLoggerDetailsSql) {
		this.saveLoggerDetailsSql = saveLoggerDetailsSql;
	}
}
