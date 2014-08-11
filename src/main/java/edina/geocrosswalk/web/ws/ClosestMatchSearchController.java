package edina.geocrosswalk.web.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Controller for closest match search
 * 
 * @author Joe Vernon
 * 
 */
public class ClosestMatchSearchController extends AbstractGXWCommandController {

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
		ClosestMatchSearchCommand closestMatchCommand = (ClosestMatchSearchCommand) command;
		String name = closestMatchCommand.getName();
		GXWFormat format = closestMatchCommand.getFormat();
		Integer startRow = closestMatchCommand.getStartRow();
		Integer maxRows = closestMatchCommand.getMaxRows();
		String gazetteer = closestMatchCommand.getGazetteer();
		String count = closestMatchCommand.getCount();
		String callback = closestMatchCommand.getCallback();
		String key = closestMatchCommand.getKey();
		String country = closestMatchCommand.getCountry();
		String srs = closestMatchCommand.getSrs();

		// Deep params
		String searchVariants = closestMatchCommand.getSearchVariants();
		String deepSrc = closestMatchCommand.getDeepSrc();
		
		// Check if cache is to be cleared
		String clearCache = closestMatchCommand.getClearCache();

		String remoteAddr = request.getRemoteAddr();
		if (clearCache != null) {
			if (remoteAddr.equals("127.0.0.1") && clearCache.equalsIgnoreCase("true")) {	
				getSpatialService().clearGetFeaturesForClosestMatchSearchCache();
				logger.debug("Cleared getFeaturesForClosestMatchSearchCache");
				return null;
			}
		}
		
		IResult result = getSpatialService().getFeaturesForClosestMatchSearch(name, startRow, maxRows, gazetteer, count, country, srs, searchVariants, deepSrc);

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