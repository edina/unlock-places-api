package edina.geocrosswalk.domain.dao;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Abstract base class for geoxwalk dao layer test cases.
 * 
 * @author Brian O'Hare
 */
@ContextConfiguration(locations = { "classpath:spring/testDomainContext.xml" })
public abstract class AbstractGXWDaoIntegrationTest extends AbstractJUnit4SpringContextTests {

}
