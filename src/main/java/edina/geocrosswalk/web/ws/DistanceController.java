package edina.geocrosswalk.web.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.domain.BoundingBox;
import edina.geocrosswalk.service.IResult;
import edina.geocrosswalk.service.ISparseFormatProviderService;
import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Calculates the distance between 2 unlock id in meters
 * 
 * 
 */
public class DistanceController extends AbstractGXWCommandController {
    
    
    ISparseFormatProviderService sparseFormatProviderService;
	
	/*
	 * (non-Javadoc)
	 * @see edina.geocrosswalk.web.ws.AbstractGXWCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
	 */
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(GXWFormat.class, FORMAT, new FormatParamEditor());
		String[] requiredFields = new String[]{"idA","idB"};
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

	    DistanceCommand distanceCommand = (DistanceCommand) command;

		Integer unlockIdA = distanceCommand.getIdA();
		Integer unlockIdB = distanceCommand.getIdB();
		String callback = distanceCommand.getCallback();
		String key = distanceCommand.getCallback();
		

		GXWFormat format = distanceCommand.getFormat();

		
		IResult result = getSpatialService().getDistanceBewteenFeatures(unlockIdA, unlockIdB);

		// get the output
		String output = getSparseFormatProviderService().getFormat(result, format, callback, key);
		String contentType = getSparseFormatProviderService().getContentTypeForFormat(format, callback);
		ModelMap model = new ModelMap();
		GXWView view = new GXWView();
		view.setContentType(contentType);
		model.addAttribute(FORMAT, format);
		model.addAttribute(RESULT_DOCUMENT, output);
		ModelAndView mav = new ModelAndView(view, model);

		return mav;
	}

    public ISparseFormatProviderService getSparseFormatProviderService() {
        return sparseFormatProviderService;
    }

    public void setSparseFormatProviderService(ISparseFormatProviderService sparseFormatProviderService) {
        this.sparseFormatProviderService = sparseFormatProviderService;
    }
}