package edina.geocrosswalk.web.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.domain.BoundingBox;
import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Controller to handle the feature type and bounding-box search API.
 * 
 * @author Joe Vernon
 * 
 */
public class SpatialFeatureSearchController extends AbstractGXWCommandController {
	
	/*
	 * (non-Javadoc)
	 * @see edina.geocrosswalk.web.ws.AbstractGXWCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
	 */
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(GXWFormat.class, FORMAT, new FormatParamEditor());
		String[] requiredFields = new String[]{"minx","maxx","miny","maxy","operator","featureType"};
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

		SpatialFeatureSearchCommand spatialFeatureCommand = (SpatialFeatureSearchCommand) command;

		String featureType = spatialFeatureCommand.getFeatureType();

		BoundingBox bbox = spatialFeatureCommand.getBoundingBox();
		String operator = spatialFeatureCommand.getOperator();
		GXWFormat format = spatialFeatureCommand.getFormat();
		Integer startRow = spatialFeatureCommand.getStartRow();
		Integer maxRows = spatialFeatureCommand.getMaxRows();
		String gazetteer = spatialFeatureCommand.getGazetteer();
		String callback = spatialFeatureCommand.getCallback();
		String key = spatialFeatureCommand.getKey();
		String count = spatialFeatureCommand.getCount();
		String country = spatialFeatureCommand.getCountry();
		String srs = spatialFeatureCommand.getSrs();
		String srs_in = spatialFeatureCommand.getSrs_in();
		String searchVariants = spatialFeatureCommand.getSearchVariants();
		String deepSrc = spatialFeatureCommand.getDeepSrc();
		
		IResult result = getSpatialService().getFeaturesForFeatureTypeAndBBox(featureType, bbox, operator, startRow, maxRows, gazetteer, count, country, srs, srs_in, searchVariants, deepSrc);

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
