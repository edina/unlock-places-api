package edina.geocrosswalk.web.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Controller to handle the name and feature type lookup API.
 * 
 * @author Joe Vernon
 * 
 */
public class NameAndFeatureSearchController extends AbstractGXWCommandController {

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
		NameAndFeatureSearchCommand nameAndFeatureCommand = (NameAndFeatureSearchCommand) command;
		String name = nameAndFeatureCommand.getName();
		String featureType = nameAndFeatureCommand.getFeatureType();
		GXWFormat format = nameAndFeatureCommand.getFormat();
		Integer startRow = nameAndFeatureCommand.getStartRow();
		Integer maxRows = nameAndFeatureCommand.getMaxRows();
		String gazetteer = nameAndFeatureCommand.getGazetteer();
		String callback = nameAndFeatureCommand.getCallback();
		String key = nameAndFeatureCommand.getKey();
		String country = nameAndFeatureCommand.getCountry();
		String count = nameAndFeatureCommand.getCount();
		String srs = nameAndFeatureCommand.getSrs();
		String searchVariants = nameAndFeatureCommand.getSearchVariants();
		String deepSrc = nameAndFeatureCommand.getDeepSrc();
		
		IResult result = getSpatialService().getFeaturesForNameAndFeature(name, featureType, startRow, maxRows,
				gazetteer, count, country, srs, searchVariants, deepSrc);
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
