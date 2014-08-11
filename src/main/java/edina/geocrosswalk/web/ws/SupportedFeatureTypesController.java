package edina.geocrosswalk.web.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Controller to handle the supported feature type API.
 * 
 * @author Joe Vernon
 * 
 */
public class SupportedFeatureTypesController extends AbstractGXWCommandController {
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
		
		SupportedFeatureTypesCommand featuresCommand = (SupportedFeatureTypesCommand) command;
		GXWFormat format = featuresCommand.getFormat();
		String gazetteer = featuresCommand.getGazetteer();
		String callback = featuresCommand.getCallback();

		IResult result = getSpatialService().getFeatureTypes(gazetteer);

		// get the output
		String output = getFormatProviderService().getFormatForFeatureTypes(result, format, callback);
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
