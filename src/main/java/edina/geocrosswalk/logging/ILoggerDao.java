package edina.geocrosswalk.logging;

/**
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public interface ILoggerDao {

	/**
	 * Writes the logger details to the database.
	 * 
	 * @param loggerDetails the DTO to use for logging.
	 */
	public abstract void saveLoggerDetails(ILoggerDetails loggerDetails);
}
