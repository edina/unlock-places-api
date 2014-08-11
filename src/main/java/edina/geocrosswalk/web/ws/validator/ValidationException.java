package edina.geocrosswalk.web.ws.validator;

import org.springframework.validation.Errors;

/**
 * Unchecked exception thrown when there are Unlock API validation errors.
 * 
 * @author Brian O'Hare
 * 
 */
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1753437078444168958L;
	private Errors errors;
	
	/**
	 * Constructor 
	 * @param message	the message
	 */
	public ValidationException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message	the error message
	 * @param errors	the errors
	 */
	public ValidationException(String message, Errors errors) {
		super(message);
		this.errors = errors;
	}
	
	/**
	 * Gets the <code>Errors</code> associated with this exception.
	 * 
	 * @return the errors
	 */
	public Errors getErrors() {
		return errors;
	}
}