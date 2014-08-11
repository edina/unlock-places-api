package edina.geocrosswalk.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Filter to do geocrosswalk web service request authentication and authorization.
 * 
 * @author Brian O'Hare
 * 
 * @version $Rev:$, $Date:$
 */
public class SecurityEnforcementFilter extends GenericFilterBean {

	private static Logger s_logger = Logger.getLogger(SecurityEnforcementFilter.class);
	private static final String ROLE_ANONYMOUS_USER = "ROLE_ANONYMOUS_USER";
	private static final String KEY = "key";
	private String anonymousKey;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		String clientIp = servletRequest.getRemoteAddr();
		logger.debug("Remote Address IP is: "+clientIp);
		String forwardClientIp = servletRequest.getHeader("X-Forwarded-For");   // Required for proxy
		logger.debug("X-Forward-For IP is: "+forwardClientIp);
		if(forwardClientIp != null)
			clientIp = forwardClientIp.split(",")[0];  // Handle multiple chained proxies
		logger.debug("So the IP we will use is: "+clientIp);
		String queryString = servletRequest.getQueryString();
		String uri = servletRequest.getRequestURI();

		/*
		 * Pull the key from the request if there.
		 */
		String key = ServletRequestUtils.getStringParameter(request, KEY);

		// If request is anonymous add an AnonymousAuthenticationToken to the SecurityContext
		if (StringUtils.isBlank(key)) {
			RequestDetails detailsWithoutKey = new RequestDetails(clientIp, uri, queryString, new Date(),
					getAnonymousKey());
			GrantedAuthorityImpl authority = new GrantedAuthorityImpl(ROLE_ANONYMOUS_USER);
			GrantedAuthority[] authorities = new GrantedAuthority[] { authority };
			AnonymousAuthenticationToken anonymousAuthentication = new AnonymousAuthenticationToken(getAnonymousKey(),
					clientIp, authorities);
			anonymousAuthentication.setDetails(detailsWithoutKey);
			SecurityContext context = SecurityContextHolder.getContext();
			context.setAuthentication(anonymousAuthentication);
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		s_logger.debug("Key: " + key);
		RequestDetails detailsWithKey = new RequestDetails(clientIp, uri, queryString, new Date(), key);
		// set up the authentication token
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(clientIp, key);
		auth.setDetails(detailsWithKey);
		
		/*
		 * Get the security context and add our authentication object to it.
		 */
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(auth);
		filterChain.doFilter(servletRequest, servletResponse);
		return;
	}


	/**
	 * Gets the key expected by the <code>AnonymousAuthenticationProvider</code>
	 * .
	 * 
	 * @return the anonymousKey
	 */
	public String getAnonymousKey() {
		return anonymousKey;
	}


	/**
	 * Sets the key expected by the <code>AnonymousAuthenticationProvider</code>
	 * .
	 * 
	 * @param anonymousKey the anonymousKey to set
	 */
	public void setAnonymousKey(String anonymousKey) {
		this.anonymousKey = anonymousKey;
	}

}
