package edina.geocrosswalk.logging;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.event.authentication.AbstractAuthenticationFailureEvent;
import org.springframework.security.event.authentication.AuthenticationSuccessEvent;

import edina.geocrosswalk.security.RequestDetails;
import edina.geocrosswalk.service.ILoggingService;

/**
 * <code>ApplicationListener</code> to listen for authentiction events and log the results.
 * 
 * @author Brian O'Hare
 * 
 * @version $Rev: $, $Date: $
 */
public class AuthenticationEventListener implements ApplicationListener {

	private static Logger s_logger = Logger.getLogger(AuthenticationEventListener.class);
	private static final String UNAUTHORISED = "UNAUTHORISED";

	private ILoggingService loggingService;


	/**
	 * Called when an authentication event occurs.
	 * 
	 * @param event the <code>ApplicationEvent</code> to process.
	 */
	public void onApplicationEvent(ApplicationEvent event) {

		if (event instanceof AuthenticationSuccessEvent) {
			s_logger.info("Registered authentication success event");
			AuthenticationSuccessEvent e = (AuthenticationSuccessEvent) event;
			Authentication auth = e.getAuthentication();
			int authenticated = auth.isAuthenticated() ? 1 : 0;
			DefaultLoggerDetails loggerDetails = new DefaultLoggerDetails();
			RequestDetails request = (RequestDetails) auth.getDetails();
			loggerDetails.setIp(request.getIp());
			loggerDetails.setUri(request.getUri());
			loggerDetails.setKey(request.getKey());
			loggerDetails.setQuery(request.getQueryString());
			loggerDetails.setAuthenticated(authenticated);
			loggerDetails.setTimestamp(request.getTimestamp());
			GrantedAuthority[] authorities = auth.getAuthorities();
			GrantedAuthority authority = authorities[0];
			if (authority != null) {
				loggerDetails.setStatus(authority.toString());
			}
			getLoggingService().saveLoggerDetails(loggerDetails);
			s_logger.debug("Logged request details.");
			s_logger.info(loggerDetails.toString());
		}

		if (event instanceof AbstractAuthenticationFailureEvent) {
			s_logger.info("Registered authentication failure event");
			AbstractAuthenticationFailureEvent e = (AbstractAuthenticationFailureEvent) event;
			s_logger.info("Failure event is: " + e.getException());
			Authentication auth = e.getAuthentication();
			int authenticated = auth.isAuthenticated() ? 1 : 0;
			RequestDetails request = (RequestDetails) auth.getDetails();
			DefaultLoggerDetails loggerDetails = new DefaultLoggerDetails();
			loggerDetails.setIp(request.getIp());
			loggerDetails.setUri(request.getUri());
			loggerDetails.setKey(request.getKey());
			loggerDetails.setQuery(request.getQueryString());
			loggerDetails.setAuthenticated(authenticated);
			loggerDetails.setTimestamp(request.getTimestamp());
			// authentication status
			loggerDetails.setStatus(UNAUTHORISED);
			getLoggingService().saveLoggerDetails(loggerDetails);
			s_logger.debug("Logged request details.");

			s_logger.info(auth.toString());
		}
	}


	/**
	 * Gets the loggingService
	 * 
	 * @return the loggingService
	 */
	public ILoggingService getLoggingService() {
		return loggingService;
	}


	/**
	 * Sets the loggingService
	 * 
	 * @param loggingService the loggingService to set
	 */
	public void setLoggingService(ILoggingService loggingService) {
		this.loggingService = loggingService;
	}

}
