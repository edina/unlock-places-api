package edina.geocrosswalk.web.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.web.ws.validator.ValidationException;

/**
 * Test cases for {@link GXWExceptionResolver}.
 * 
 * @author Brian O'Hare
 * @version $Rev:$, $Date:$
 */
public class GXWExceptionResolverTest {
	
    private GXWExceptionResolver resolver;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private VelocityEngine velocityEngine;
	private JavaMailSender mailSender;
	private String[] emailRecipients;
	
	@Before
	public void onSetUp() {
		resolver = new GXWExceptionResolver();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("file.resource.loader.path", "src/test/resources/templates/");
		resolver.setVelocityEngine(velocityEngine);
		resolver.setValidationErrorTemplate("error.vm");
		mailSender = new JavaMailSenderImpl();
		// mailrelay.ed.ac.uk -- not set for testing
		((JavaMailSenderImpl)mailSender).setHost("no such host");
		resolver.setMailSender(mailSender);
		resolver.setEmailTemplate("email.vm");
		emailRecipients = new String[]{"brian.ohare@ed.ac.uk", "joe.vernon@ed.ac.uk"};
		resolver.setEmailRecipients(emailRecipients);
	}
	
	@Test
	public void testResolveValidationException() {
		ValidationException ex = new ValidationException("test exception");
		String handler = "test handler";
		ModelAndView mav = resolver.resolveException(request, response, handler, ex);
		assertNotNull(mav);
		assertTrue(mav.getView() instanceof GXWExceptionView);
		Integer status = (Integer) mav.getModel().get("status");
		assertEquals(new Integer(400), status);
	}
	
	@Test
	public void testResolveException() {
		Exception ex = new Exception("test exception");
		String handler = "test handler";
		ModelAndView mav = null;
		try {
			mav = resolver.resolveException(request, response, handler, ex);
		}
		catch (MailSendException mex) {
			// expected
		}
		assertNotNull(mav);
		assertTrue(mav.getView() instanceof GXWExceptionView);
		Integer status = (Integer) mav.getModel().get("status");
		assertEquals(new Integer(500), status);	
	}

}
