package edina.geocrosswalk.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edina.geocrosswalk.logging.DefaultLoggerDetails;
import edina.geocrosswalk.logging.ILoggerDao;
import edina.geocrosswalk.logging.ILoggerDetails;

/**
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class DefaultLoggingServiceTest {

	private DefaultLoggingService loggingService;
	private ILoggerDao mockLoggerDao;
	private DefaultLoggerDetails details;
	private List<ILoggerDetails> results;


	@Before
	public void onSetUp() {
		loggingService = new DefaultLoggingService();
		mockLoggerDao = createMock(ILoggerDao.class);
		loggingService.setLoggerDao(mockLoggerDao);
		details = new DefaultLoggerDetails();
		details.setIp("129.215.169.2");
		details.setKey("some_valid_key");
		details.setAuthenticated(1);
		details.setQuery("name=Edinburgh");
		details.setStatus("ROLE_DIGIMAP_USER");
		details.setTimestamp(new Date());
		details.setUri("/geocrosswalk/ws/nameSearch");
		results = new ArrayList<ILoggerDetails>();
		results.add(details);
	}


	@Test
	public void testSaveLoggerDetails() {
		mockLoggerDao.saveLoggerDetails(details);
		expectLastCall();
		replay(mockLoggerDao);
		loggingService.saveLoggerDetails(details);
		verify(mockLoggerDao);
	}


	@Test
	public void testNullDetails() {
		try {
			loggingService.saveLoggerDetails(null);
			fail("Should have caught IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
	}

}
