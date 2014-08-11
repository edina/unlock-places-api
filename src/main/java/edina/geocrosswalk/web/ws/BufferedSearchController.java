package edina.geocrosswalk.web.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.domain.BoundingBox;
import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.ISparseFormatProviderService;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Calculates the distance between 2 unlock id in meters
 * 
 * 
 */
public class BufferedSearchController extends AbstractGXWCommandController {
    

	
	/*
	 * (non-Javadoc)
	 * @see edina.geocrosswalk.web.ws.AbstractGXWCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
	 */
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(GXWFormat.class, FORMAT, new FormatParamEditor());
		String[] requiredFields = new String[]{"identifier","distance","featureType"};
		binder.setRequiredFields(requiredFields);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.web.ws.AbstractGXWCommandController#handle(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception {

	    BufferedSearchCommand bufferedSearchCommand = (BufferedSearchCommand) command;

		String identifier  = bufferedSearchCommand.getIdentifier();
		Integer bufferedDistance = bufferedSearchCommand.getDistance();
		String callback = bufferedSearchCommand.getCallback();
		String key = bufferedSearchCommand.getCallback();
		String gazetteer = bufferedSearchCommand.getGazetteer();
		String featureTypes = bufferedSearchCommand.getFeatureType();
		Integer maxRows = bufferedSearchCommand.getMaxRows();
		GXWFormat format = bufferedSearchCommand.getFormat();

		
		IResult result = getSpatialService().getBufferedSearchFeatures(identifier, maxRows, featureTypes, gazetteer, bufferedDistance);

		// get the output
		String output = getFormatProviderService().getFormatForFeatures(result, format, callback, key);
		String contentType = getFormatProviderService().getContentTypeForFormat(format, callback);
		ModelMap model = new ModelMap();
		GXWView view = new GXWView();
		view.setContentType(contentType);
		model.addAttribute(FORMAT, format);
		model.addAttribute(RESULT_DOCUMENT, output);
		ModelAndView mav = new ModelAndView(view, model);

		return mav;
	}


}