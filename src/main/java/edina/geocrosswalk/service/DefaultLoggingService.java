package edina.geocrosswalk.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import edina.geocrosswalk.logging.ILoggerDao;
import edina.geocrosswalk.logging.ILoggerDetails;

/**
 * Default implementation of {@link ILoggingService}.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class DefaultLoggingService implements ILoggingService {

	private ILoggerDao loggerDao;


	/*
	 * (non-Javadoc)
	 * 
	 * @seeedina.geocrosswalk.service.ILoggingService#saveLoggerDetails(edina.
	 * geocrosswalk.logging.ILoggerDetails)
	 */
	public void saveLoggerDetails(ILoggerDetails loggerDetails) {
		if (loggerDetails == null) { throw new IllegalArgumentException("Logger details must not be null"); }
		getLoggerDao().saveLoggerDetails(loggerDetails);
	}


	/**
	 * Gets the loggerDao
	 * 
	 * @return the loggerDao
	 */
	public ILoggerDao getLoggerDao() {
		return loggerDao;
	}


	/**
	 * Sets the loggerDao
	 * 
	 * @param loggerDao the loggerDao to set
	 */
	public void setLoggerDao(ILoggerDao loggerDao) {
		this.loggerDao = loggerDao;
	}

}
