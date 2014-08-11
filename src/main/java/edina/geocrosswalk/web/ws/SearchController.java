package edina.geocrosswalk.web.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.domain.BoundingBox;
import edina.geocrosswalk.service.IFormatProviderService;
import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Controller to handle the new generic search functionality
 * 
 * @author Joe Vernon
 * 
 */
public class SearchController extends AbstractGXWCommandController {
	
	/*
	 * (non-Javadoc)
	 * @see edina.geocrosswalk.web.ws.AbstractGXWCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
	 */
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(GXWFormat.class, FORMAT, new FormatParamEditor());
		//String[] requiredFields = new String[]{"minx","maxx","miny","maxy","operator","featureType"};
		String[] requiredFields = new String[]{};
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

		SearchCommand searchCommand = (SearchCommand) command;

		String name = searchCommand.getName();
		String variantId = searchCommand.getVariantId();
		String featureType = searchCommand.getFeatureType();
		BoundingBox bbox = searchCommand.getBoundingBox();
		String operator = searchCommand.getOperator();
		GXWFormat format = searchCommand.getFormat();
		Integer startRow = searchCommand.getStartRow();
		Integer maxRows = searchCommand.getMaxRows();
		String gazetteer = searchCommand.getGazetteer();
		String callback = searchCommand.getCallback();
		String key = searchCommand.getKey();
		String count = searchCommand.getCount();
		String country = searchCommand.getCountry();
		String srs = searchCommand.getSrs();
		Integer startYear = searchCommand.getStartYear();
		Integer endYear = searchCommand.getEndYear();
		Integer spatialMask = searchCommand.getSpatialMask();
		String realSpatial = searchCommand.getRealSpatial();
		Integer buffer = searchCommand.getBuffer();
		String duplicates = searchCommand.getDuplicates();
		String childTypes = searchCommand.getChildTypes();
		String searchVariants = searchCommand.getSearchVariants();
		String deepSrc = searchCommand.getDeepSrc();
		
		String clearCache = searchCommand.getClearCache();
		
		String remoteAddr = request.getRemoteAddr();
		if (clearCache != null) {
			if (remoteAddr.equals("127.0.0.1") && clearCache.equalsIgnoreCase("true")) {	
				getSpatialService().clearGetFeaturesCache();
				logger.debug("Cleared getFeaturesCache");
				return null;
			}
		}
		
		
		ModelAndView mav = null;
		IFormatProviderService formatProviderService = getFormatProviderService();
		
		if (StringUtils.isNotBlank(variantId)) {
			IResult result = getSpatialService().getDeepAttestation(variantId);

			// get the output
			String output = formatProviderService.getFormatForDeepAttestation(result, format, callback);
			mav = setUpModelAndView(format, callback, formatProviderService, output);

		} else {
			IResult result = getSpatialService().getFeatures(name, featureType, bbox, operator, startRow, maxRows, gazetteer, count, country, srs,
					startYear, endYear, spatialMask, realSpatial, buffer, duplicates, childTypes, searchVariants, deepSrc);

			// get the output
			String output = formatProviderService.getFormatForFeatures(result, format, callback, key);
			mav = setUpModelAndView(format, callback, formatProviderService, output);
		}

		return mav;
	}

	/**
	 * Private helper to set up model and view
	 * @param format
	 * @param callback
	 * @param formatProviderService
	 * @param output
	 * @return
	 */
	private ModelAndView setUpModelAndView(GXWFormat format, String callback, IFormatProviderService formatProviderService, String output) {
		ModelAndView mav;
		String contentType = formatProviderService.getContentTypeForFormat(format, callback);
		ModelMap model = new ModelMap();
		GXWView view = new GXWView();
		view.setContentType(contentType);
		model.addAttribute(FORMAT, format);
		model.addAttribute(RESULT_DOCUMENT, output);
		mav = new ModelAndView(view, model);
		return mav;
	}
}