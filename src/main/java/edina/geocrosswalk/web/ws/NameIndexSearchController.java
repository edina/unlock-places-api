package edina.geocrosswalk.web.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.service.IFormatProviderService;
import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Controller for generating place name lookup.
 */
public class NameIndexSearchController extends AbstractGXWCommandController {
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

		ModelAndView mav = null;

		if (command instanceof NameStartsWithSearchCommand) {
			NameStartsWithSearchCommand nameStartsWithCmd = (NameStartsWithSearchCommand) command;

			String country = nameStartsWithCmd.getCountry();
			String callback = nameStartsWithCmd.getCallback();
			// fetch names from database
			List<String> possibleCompletions = getSpatialService().getAutoCompleteNames(nameStartsWithCmd.getName(), country);

			IFormatProviderService formatProvider = getFormatProviderService();
			ModelMap model = new ModelMap();
			GXWView view = new GXWView();
			
			view.setContentType(formatProvider.getContentTypeForFormat(nameStartsWithCmd.getFormat(), callback));
			model.addAttribute(FORMAT, nameStartsWithCmd.getFormat());

			// set output
			model.addAttribute(RESULT_DOCUMENT, formatProvider.getOutputForAutoComplete(possibleCompletions, nameStartsWithCmd.getFormat(), callback));
			mav = new ModelAndView(view, model);
		}

		return mav;		
		
	}
}
