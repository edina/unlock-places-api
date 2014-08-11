package edina.geocrosswalk.web.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Controller for unique features name search
 * 
 * @author Joe Vernon
 * 
 */
public class UniqueNameSearchController extends AbstractGXWCommandController {

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
		UniqueNameSearchCommand uniqueNameCommand = (UniqueNameSearchCommand) command;
		String name = uniqueNameCommand.getName();
		GXWFormat format = uniqueNameCommand.getFormat();
		Integer startRow = uniqueNameCommand.getStartRow();
		Integer maxRows = uniqueNameCommand.getMaxRows();
		String gazetteer = uniqueNameCommand.getGazetteer();
		String count = uniqueNameCommand.getCount();
		String country = uniqueNameCommand.getCountry();
		String srs = uniqueNameCommand.getSrs();
		String callback = uniqueNameCommand.getCallback();
		String key = uniqueNameCommand.getKey();
		String searchVariants = uniqueNameCommand.getSearchVariants();
		String deepSrc = uniqueNameCommand.getDeepSrc();
		
		IResult result = getSpatialService().getFeaturesForUniqueNameSearch(name, startRow, maxRows, gazetteer, count, country, srs, searchVariants, deepSrc);
		
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