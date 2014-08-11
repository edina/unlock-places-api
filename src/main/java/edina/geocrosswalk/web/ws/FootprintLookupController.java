package edina.geocrosswalk.web.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Controller to handle the footprint lookup API.
 * 
 * @author Joe Vernon
 * 
 */
public class FootprintLookupController extends AbstractGXWCommandController {

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
		FootprintLookupCommand footprintCommand = (FootprintLookupCommand) command;
		String identifier = footprintCommand.getIdentifier();
		GXWFormat format = footprintCommand.getFormat();
		String callback = footprintCommand.getCallback();
		String srs = footprintCommand.getSrs();
		String key = footprintCommand.getKey();
		
		
		String clearCache = footprintCommand.getClearCache();
		
		String remoteAddr = request.getRemoteAddr();
		if (clearCache != null) {
			if (remoteAddr.equals("127.0.0.1") && clearCache.equalsIgnoreCase("true")) {	
				getSpatialService().clearGetFootprintsForIdentifierCache();
				logger.debug("Cleared GetFootprintsForIdentifierCache");
				return null;
			}
		}
			
		IResult result = getSpatialService().getFootprintsForIdentifier(identifier, srs);
		
		// get the output
		String output = getFormatProviderService().getFormatForFootprint(result, format, callback, key);
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
