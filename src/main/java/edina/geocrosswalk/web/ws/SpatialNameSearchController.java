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
 * Controller to handle the name and bounding-box search api.
 * 
 * @author Joe Vernon
 * 
 */
public class SpatialNameSearchController extends AbstractGXWCommandController {
	
	/*
	 * (non-Javadoc)
	 * @see edina.geocrosswalk.web.ws.AbstractGXWCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
	 */
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(GXWFormat.class, FORMAT, new FormatParamEditor());
		String[] requiredFields = new String[]{"minx","maxx","miny","maxy","operator","name"};
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

		SpatialNameSearchCommand spatialNameCommand = (SpatialNameSearchCommand) command;

		String name = spatialNameCommand.getName();
		BoundingBox bbox = spatialNameCommand.getBoundingBox();
		String operator = spatialNameCommand.getOperator();
		GXWFormat format = spatialNameCommand.getFormat();
		Integer startRow = spatialNameCommand.getStartRow();
		Integer maxRows = spatialNameCommand.getMaxRows();
		String gazetteer = spatialNameCommand.getGazetteer();
		String callback = spatialNameCommand.getCallback();
		String key = spatialNameCommand.getKey();
		String count = spatialNameCommand.getCount();
		String country = spatialNameCommand.getCountry();
		String srs = spatialNameCommand.getSrs();
		String srs_in = spatialNameCommand.getSrs_in();
		String searchVariants = spatialNameCommand.getSearchVariants();
		String deepSrc = spatialNameCommand.getDeepSrc();
		
		// run the query
		IResult result = getSpatialService().getFeaturesForNameAndBBox(name, bbox, operator, startRow, maxRows, gazetteer, count, country, srs, srs_in, searchVariants, deepSrc);

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
