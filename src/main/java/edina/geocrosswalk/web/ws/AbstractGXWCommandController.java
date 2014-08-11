package edina.geocrosswalk.web.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

import edina.geocrosswalk.service.IFormatProviderService;
import edina.geocrosswalk.service.ISpatialService;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Base class for Unlock API command controllers.
 * 
 * @author Brian O'Hare
 * 
 */
public abstract class AbstractGXWCommandController extends AbstractCommandController {

	protected static final String START_YEAR = "startYear";
	protected static final String END_YEAR = "endYear";
	protected static final String FORMAT = "format";
	protected static final String BBOX = "bbox";
	protected static final String START_ROW = "startRow";
	protected static final String MAX_ROWS = "maxRows";
	protected static final String FEATURE_TYPE = "featureType";
	protected static final String RESULT_DOCUMENT = "resultDocument";
	private ISpatialService spatialService;
	private IFormatProviderService formatProviderService;
	protected static final String RESPONSE_STATUS ="status";
	private VelocityEngine velocityEngine;
	private String errorTemplate;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.BaseCommandController#initBinder(
	 * javax.servlet.http.HttpServletRequest,
	 * org.springframework.web.bind.ServletRequestDataBinder)
	 */
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(GXWFormat.class, FORMAT, new FormatParamEditor());
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.AbstractCommandController#handle(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object,
	 * org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception {
		// to be overridden.
		return null;
	}


	/**
	 * Gets the <code>ISpatialService</code>.
	 * 
	 * @return the searchService
	 */
	public ISpatialService getSpatialService() {
		return spatialService;
	}


	/**
	 * Sets the <code>ISpatialService</code>.
	 * 
	 * @param searchService the searchService to set
	 */
	public void setSpatialService(ISpatialService spatialService) {
		this.spatialService = spatialService;
	}


	/**
	 * Gets the <code>IFormatProviderService</code>.
	 * 
	 * @return the formatProviderService
	 */
	public IFormatProviderService getFormatProviderService() {
		return formatProviderService;
	}


	/**
	 * Sets the <code>IFormatProviderService</code>.
	 * 
	 * @param formatProviderService the formatProviderService to set
	 */
	public void setFormatProviderService(IFormatProviderService formatProviderService) {
		this.formatProviderService = formatProviderService;
	}


	/**
	 * Gets a configured <code>VelocityEngine</code>.
	 * 
	 * @return the velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}


	/**
	 * Sets a configured <code>VelocityEngine</code>.
	 * 
	 * @param velocityEngine the velocityEngine to set
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}


	/**
	 * Gets the error template.
	 * 
	 * @return the errorTemplate
	 */
	public String getErrorTemplate() {
		return errorTemplate;
	}


	/**
	 * Sets the error template.
	 * 
	 * @param errorTemplate the errorTemplate to set
	 */
	public void setErrorTemplate(String errorTemplate) {
		this.errorTemplate = errorTemplate;
	}
}
