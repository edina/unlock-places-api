package edina.geocrosswalk.web.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Controller to handle the name lookup API.
 * 
 * @author Brian O'Hare
 * @author Joe Vernon
 * 
 */
public class NameSearchController extends AbstractGXWCommandController {

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
		NameSearchCommand nameCommand = (NameSearchCommand) command;
		
		String name = nameCommand.getName();
		GXWFormat format = nameCommand.getFormat();
		Integer startRow = nameCommand.getStartRow();
		Integer maxRows = nameCommand.getMaxRows();
		String gazetteer = nameCommand.getGazetteer();
		String count = nameCommand.getCount();
		String callback = nameCommand.getCallback();
		String key = nameCommand.getKey();
		String country = nameCommand.getCountry();
		String srs = nameCommand.getSrs();
		String searchVariants = nameCommand.getSearchVariants();
		String deepSrc = nameCommand.getDeepSrc();

		IResult result = getSpatialService().getFeaturesForName(name, startRow, maxRows, gazetteer, count, country, srs, searchVariants, deepSrc);
		
		logger.debug("Name parameter to be used: " + name);
		
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
