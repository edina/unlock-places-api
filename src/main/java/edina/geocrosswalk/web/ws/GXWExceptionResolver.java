package edina.geocrosswalk.web.ws;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.web.ws.validator.ValidationException;

/**
 * Handles exceptions thrown by Unlock Web Service controllers.
 * 
 * @author Brian O'Hare
 * 
 */
public class GXWExceptionResolver implements HandlerExceptionResolver {

	private static Logger logger = Logger.getLogger(GXWExceptionResolver.class);
	private static final String EMAIL_FROM = "gxw@ed.ac.uk";
	private static final String EMAIL_SUBJECT = "GeoCrossWalk Web Services Application Error";
	private static final String ERROR_MESSAGE = "errorMessage";
	private VelocityEngine velocityEngine;
	private String validationErrorTemplate;
	private String emailTemplate;
	private JavaMailSender mailSender;
	private String[] emailRecipients;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerExceptionResolver#resolveException
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object,
	 * java.lang.Exception)
	 */
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		logger.debug(handler.toString());
		GXWExceptionView exView = new GXWExceptionView();
		ModelMap map = new ModelMap();
		Integer status = null;
		VelocityEngine velocityEngine = getVelocityEngine();
		Template errorTemplate = null;
		// catch validation exceptions
		if (ex instanceof ValidationException) {
			try {
				errorTemplate = velocityEngine.getTemplate(getValidationErrorTemplate());
				VelocityContext context = new VelocityContext();
				String message = StringUtils.defaultIfEmpty(ex.getMessage(), "No detailed message available");
				context.put(ERROR_MESSAGE, message);
				StringWriter writer = new StringWriter();
				errorTemplate.merge(context, writer);
				map.addAttribute("error", writer.toString());
				status = new Integer(HttpServletResponse.SC_BAD_REQUEST);
				map.addAttribute("status", status);
			}
			catch (Exception e) {
				logger.error(e);
			}
			return new ModelAndView(exView, map);
		}
		// catch application exceptions
		if (ex instanceof Exception) {
			try {
				errorTemplate = velocityEngine.getTemplate(getValidationErrorTemplate());
				VelocityContext context = new VelocityContext();
				String cause = ex.getClass().getCanonicalName();
				String message = StringUtils.defaultIfEmpty(ex.getMessage(), "No detailed message available");
				context.put(ERROR_MESSAGE, cause + " : " + message);
				StringWriter writer = new StringWriter();
				errorTemplate.merge(context, writer);
				map.addAttribute("error", writer.toString());
				status = new Integer(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				map.addAttribute("status", status);
				sendExceptionNotification(ex);
			}
			catch (Exception e) {
				logger.error(e);
			}
			return new ModelAndView(exView, map);
		}
		return null;
	}
	
	private void sendExceptionNotification(final Exception ex) {
	      MimeMessagePreparator preparator = new MimeMessagePreparator() {
	         @SuppressWarnings("unchecked")
			public void prepare(MimeMessage mimeMessage) throws Exception {
	            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
	            message.setTo(getEmailRecipients());
	            message.setFrom(EMAIL_FROM);
	            message.setSubject(EMAIL_SUBJECT);
	            // set highest priority
	            message.setPriority(1);
	            Map model = new HashMap();
	            StringWriter sw = new StringWriter();
	            ex.printStackTrace(new PrintWriter(sw));
	            model.put("exception", ex);
	            model.put("stack", sw.toString());
	            model.put("date", new Date());
	            String text = VelocityEngineUtils.mergeTemplateIntoString (
	               velocityEngine, getEmailTemplate(), model);
	            message.setText(text, false);
	         }
	      };
	      getMailSender().send(preparator);
	   }



	/**
	 * Gets the velocity engine.
	 * 
	 * @return the velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}


	/**
	 * Sets the velocity engine.
	 * 
	 * @param velocityEngine the velocityEngine to set
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}


	/**
	 * Gets the validation error template.
	 * 
	 * @return the validationErrorTemplate
	 */
	public String getValidationErrorTemplate() {
		return validationErrorTemplate;
	}


	/**
	 * Sets the validation error template.
	 * 
	 * @param validationErrorTemplate the validationErrorTemplate to set
	 */
	public void setValidationErrorTemplate(String validationErrorTemplate) {
		this.validationErrorTemplate = validationErrorTemplate;
	}


	/**
	 * Gets the emailTemplate
	 *
	 * @return the emailTemplate
	 */
	public String getEmailTemplate() {
		return emailTemplate;
	}


	/**
	 * Sets the emailTemplate
	 *
	 * @param emailTemplate the emailTemplate to set
	 */
	public void setEmailTemplate(String emailTemplate) {
		this.emailTemplate = emailTemplate;
	}


	/**
	 * Gets the mailSender
	 *
	 * @return the mailSender
	 */
	public JavaMailSender getMailSender() {
		return mailSender;
	}


	/**
	 * Sets the mailSender
	 *
	 * @param mailSender the mailSender to set
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * @return the emailRecipients
	 */
	public String[] getEmailRecipients() {
	    return emailRecipients;
	}

	/**
	 * @param emailRecipients the emailRecipients to set
	 */
	public void setEmailRecipients(String[] emailRecipients) {
	    this.emailRecipients = emailRecipients;
	}
}
