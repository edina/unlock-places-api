package edina.geocrosswalk.web.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Controller to handle the partial name search API.
 * 
 * @author Joe Vernon
 * 
 */
public class NameStartsWithSearchController extends AbstractGXWCommandController {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.web.ws.AbstractGXWCommandController#handle(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		NameStartsWithSearchCommand fullTextNameCommand = (NameStartsWithSearchCommand) command;
		
		String name = fullTextNameCommand.getName();
		GXWFormat format = fullTextNameCommand.getFormat();
		Integer startRow = fullTextNameCommand.getStartRow();
		Integer maxRows = fullTextNameCommand.getMaxRows();
		String gazetteer = fullTextNameCommand.getGazetteer();
		String count = fullTextNameCommand.getCount();
		String callback = fullTextNameCommand.getCallback();
		String key = fullTextNameCommand.getKey();
		String country = fullTextNameCommand.getCountry();
		String srs = fullTextNameCommand.getSrs();
		String searchVariants = fullTextNameCommand.getSearchVariants();
		String deepSrc = fullTextNameCommand.getDeepSrc();
		
		IResult result = getSpatialService().getFeaturesForNameStartsWith(name, startRow, maxRows, gazetteer, count, country, srs, searchVariants, deepSrc);

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