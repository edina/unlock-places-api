package edina.geocrosswalk.service;

import java.util.List;

import edina.geocrosswalk.logging.ILoggerDetails;

/**
 * Defines requirements for logging services.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public interface ILoggingService {

	/**
	 * Saves the provided logging details.
	 * 
	 * @param loggerDetails the details to save.
	 */
	public void saveLoggerDetails(ILoggerDetails loggerDetails);
}
