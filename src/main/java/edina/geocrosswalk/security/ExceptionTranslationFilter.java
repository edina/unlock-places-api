package edina.geocrosswalk.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationTrustResolver;
import org.springframework.security.AuthenticationTrustResolverImpl;
import org.springframework.security.SpringSecurityException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.ui.FilterChainOrder;
import org.springframework.security.ui.SpringSecurityFilter;
import org.springframework.security.util.ThrowableAnalyzer;
import org.springframework.security.util.ThrowableCauseExtractor;

/**
 * Custom ExceptionTranslationFilter to handle authentication and authorization
 * exceptions for geocrosswalk.
 * 
 * @author Brian O'Hare
 * 
 */
public class ExceptionTranslationFilter extends SpringSecurityFilter {

	private static final String ERROR_MESSAGE = "errorMessage";
	private static final String CONTENT_TYPE = "text/xml;charset=utf-8";
	private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();
	private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
	private VelocityEngine velocityEngine;
	private String errorTemplate;


	@Override
	protected void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			chain.doFilter(request, response);

			if (logger.isDebugEnabled()) {
				logger.debug("Chain processed normally");
			}
			return;
		}
		catch (IOException ex) {
			throw ex;
		}
		catch (Exception ex) {
			// Try to extract a SpringSecurityException from the stacktrace
			Throwable[] causeChain = this.throwableAnalyzer.determineCauseChain(ex);
			SpringSecurityException ase = (SpringSecurityException) this.throwableAnalyzer.getFirstThrowableOfType(
					SpringSecurityException.class, causeChain);

			if (ase != null) {
				handleException(request, response, chain, ase);
			}
			else {
				// Rethrow ServletExceptions and RuntimeExceptions as-is
				if (ex instanceof ServletException) {
					throw (ServletException) ex;
				}
				else if (ex instanceof RuntimeException) { throw (RuntimeException) ex; }
				// Wrap other Exceptions. These are not expected to happen
				throw new RuntimeException(ex);
			}
		}

	}


	/*
	 * Handles the exceptions.
	 */
	private void handleException(ServletRequest request, ServletResponse response, FilterChain chain,
			SpringSecurityException exception) throws IOException, ServletException {
		if (exception instanceof AuthenticationException) {
			if (logger.isDebugEnabled()) {
				logger.debug("Authentication exception occurred - rendering error template.", exception);
			}
			int status = HttpServletResponse.SC_UNAUTHORIZED;
			sendErrorResponse(request, response, (AuthenticationException) exception, status);

		}
		else if (exception instanceof AccessDeniedException) {
			if (authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication())) {
				if (logger.isDebugEnabled()) {
					logger.debug("User is anonymous, proceeding...");
				}
				// allow anonymous requests through
				chain.doFilter(request, response);
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("Access is denied (user is not anonymous), rendering error template.", exception);
				}
				int status = HttpServletResponse.SC_FORBIDDEN;
				sendErrorResponse(request, response, (AccessDeniedException) exception, status);
			}
		}
	}


	private void sendErrorResponse(ServletRequest request, ServletResponse response, SpringSecurityException exception,
			int status) {

		VelocityEngine velocityEngine = getVelocityEngine();
		Template errorTemplate = null;
		try {
			errorTemplate = velocityEngine.getTemplate(getErrorTemplate());
			VelocityContext context = new VelocityContext();
			context.put(ERROR_MESSAGE, exception.getMessage());
			StringWriter writer = new StringWriter();
			errorTemplate.merge(context, writer);
			PrintWriter out = response.getWriter();
			response.setContentType(CONTENT_TYPE);
			((HttpServletResponse) response).setStatus(status);
			out.write(writer.toString());
			response.flushBuffer();
		}
		catch (Exception e) {
			logger.error(e);
		}

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.ui.SpringSecurityFilter#getOrder()
	 */
	public int getOrder() {
		return FilterChainOrder.EXCEPTION_TRANSLATION_FILTER;
	}


	/**
	 * Gets the velociy engine for displaying security related error messages.
	 * 
	 * @return the velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}


	/**
	 * Sets the velocity engine for displaying security related error messages.
	 * 
	 * @param velocityEngine the velocityEngine to set
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}


	/**
	 * Gets the velocity error template.
	 * 
	 * @return the errorTemplate
	 */
	public String getErrorTemplate() {
		return errorTemplate;
	}


	/**
	 * Sets the velocity error template.
	 * 
	 * @param errorTemplate the errorTemplate to set
	 */
	public void setErrorTemplate(String errorTemplate) {
		this.errorTemplate = errorTemplate;
	}

	/**
	 * Default implementation of <code>ThrowableAnalyzer</code> which is capable
	 * of also unwrapping <code>ServletException</code>s.
	 */
	private static final class DefaultThrowableAnalyzer extends ThrowableAnalyzer {
		/**
		 * @see org.springframework.security.util.ThrowableAnalyzer#initExtractorMap()
		 */
		protected void initExtractorMap() {
			super.initExtractorMap();

			registerExtractor(ServletException.class, new ThrowableCauseExtractor() {
				public Throwable extractCause(Throwable throwable) {
					ThrowableAnalyzer.verifyThrowableHierarchy(throwable, ServletException.class);
					return ((ServletException) throwable).getRootCause();
				}
			});
		}

	}

}
