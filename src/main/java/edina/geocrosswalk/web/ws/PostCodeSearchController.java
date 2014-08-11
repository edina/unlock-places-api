package edina.geocrosswalk.web.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Controller to handle the post code search API.
 * 
 * @author Joe Vernon
 * 
 */
public class PostCodeSearchController extends AbstractGXWCommandController {

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
		
		PostCodeSearchCommand postCodeCommand = (PostCodeSearchCommand) command;
		String postCode = postCodeCommand.getPostCode();
		GXWFormat format = postCodeCommand.getFormat();
		Integer startRow = postCodeCommand.getStartRow();
		Integer maxRows = postCodeCommand.getMaxRows();
		String gazetteer = postCodeCommand.getGazetteer();
		String callback = postCodeCommand.getCallback();
		String key = postCodeCommand.getKey();
		String count = postCodeCommand.getCount();
		String country = postCodeCommand.getCountry();
		String srs = postCodeCommand.getSrs();
		
		IResult result = getSpatialService().getFeaturesForPostCode(postCode, startRow, maxRows, gazetteer, count, country, srs);
		

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
