package edina.geocrosswalk.domain.dao;

import java.util.ResourceBundle;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.util.Assert;

/**
 * Abstract DAO for Unlock's Domain DAO's.
 * 
 * @author Brian O'Hare
 * @author Joe Vernon
 * 
 */
public class AbstractGXWDao extends NamedParameterJdbcDaoSupport {

	protected static final ResourceBundle sqlResources = ResourceBundle.getBundle("sql/sql");
	private static final String ROLE_DIGIMAP_USER = "ROLE_DIGIMAP_USER";
	private String schemaName;
	
	

	/**
	 * Checks whether this user is authorised, ie has ROLE_DIGIMAP_USER granted authority.
	 * 
	 * @return <code>true</code> if authorised, <code>false</code> otherwise.
	 */
	protected boolean isAuthorised() {
		boolean authorised = false;
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		GrantedAuthority[] roles = auth.getAuthorities();
		GrantedAuthority role = roles[0];
		if (role != null) {
			if (role.equals(ROLE_DIGIMAP_USER)) {
				authorised = true;
			}
		}
		return authorised;
	}

    public String getSchemaName() {
        Assert.hasText(schemaName, "Must have schema name (set in database.properties)");
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
}
