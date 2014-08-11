package edina.geocrosswalk.web.ws.validator;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.postgis.PGbox2d;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edina.geocrosswalk.domain.BoundingBox;
import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.SpatialFeatureSearchCommand;

/**
 * Validator to validate <code>SpatialFeatureSearchCommand</code> objects.
 * 
 * @author Joe Vernon
 * 
 */
public class SpatialFeatureSearchCommandValidator extends AbstractGXWCommandValidator {

	private static Logger logger = Logger.getLogger(SpatialFeatureSearchCommandValidator.class);

	private static final String OPERATOR_REQUIRED = "Operator parameter required";
	private static final String UNSUPPORTED_OPERATOR = "Unsupported spatial operator";
	private static final String WITHIN = "within";
	private static final String INTERSECT = "intersect";
	private Validator bboxValidator;
	private Validator featureValidator;
	private Validator operatorValidator;
	private SrsBboxValidator srsBboxValidator;
	private static final String INVALID_GEOMETRY = "The coordinates are not in the projection system that you have supplied.";
	private static final String REQUIRED_CODE = "required.srsbbox";


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return SpatialFeatureSearchCommand.class.isAssignableFrom(clazz);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		
		checkErrors(errors);
		
		SpatialFeatureSearchCommand spatialFeatureCommand = (SpatialFeatureSearchCommand) target;
		BoundingBox bbox = spatialFeatureCommand.getBoundingBox();
		String featureType = spatialFeatureCommand.getFeatureType();
		String operator = spatialFeatureCommand.getOperator();
		Integer maxRows = spatialFeatureCommand.getMaxRows();
		Integer startRow = spatialFeatureCommand.getStartRow();
		GXWFormat format = spatialFeatureCommand.getFormat();
		String gazetteer = spatialFeatureCommand.getGazetteer();
		String key = spatialFeatureCommand.getKey();
		String country = spatialFeatureCommand.getCountry();
		String count = spatialFeatureCommand.getCount();
		String srs = spatialFeatureCommand.getSrs();
		String srs_in = spatialFeatureCommand.getSrs_in();
		String srs_out = srs;
		
		if(srs_in == null){
			srs_in = "4326";
		}
		
		// validate the feature type 
		ValidationUtils.invokeValidator(getFeatureValidator(), featureType, errors);
		
		// validate the operator
		ValidationUtils.invokeValidator(getOperatorValidator(), operator, errors);
		
		// validate the minimum bounding box
		ValidationUtils.invokeValidator(getBboxValidator(), bbox, errors);
		
		if(srsBboxValidator.validate(srs_in, bbox)==false){
			logger.debug("I am in");
			String msg = INVALID_GEOMETRY;
			HashMap<String, String> fb = srsBboxValidator.getValidCSystems(bbox);
			if(fb.size()>0){
				msg += "The coordinates are valid for these projection systems:\n";
				for(String mykey : fb.keySet()){
					msg += fb.get(mykey)+"("+mykey+"),\n";
				}
			}
			errors.reject(REQUIRED_CODE, msg);
		}
		
		checkErrors(errors);

		// validate the paging params.
		spatialFeatureCommand.setStartRow(validateStartRow(startRow));
		spatialFeatureCommand.setMaxRows(validateMaxRowSize(maxRows));
		spatialFeatureCommand.setFormat(validateOutputFormat(format));
		spatialFeatureCommand.setGazetteer(validateGazetteer(gazetteer,key));
		spatialFeatureCommand.setCountry(validateCountry(country));
		spatialFeatureCommand.setCount(validateCount(count));
		spatialFeatureCommand.setSrs(validateSrs(srs));
		if(spatialFeatureCommand.getSrs().equals(BRITISH_NATIONAL_GRID)){
			spatialFeatureCommand.setCountry(UNITED_KINGDOM);
		}
	}


	/**
	 * Gets the bboxValidator
	 * 
	 * @return the bboxValidator
	 */
	public Validator getBboxValidator() {
		return bboxValidator;
	}


	/**
	 * Sets the bboxValidator
	 * 
	 * @param bboxValidator the bboxValidator to set
	 */
	public void setBboxValidator(Validator bboxValidator) {
		this.bboxValidator = bboxValidator;
	}


	/**
	 * Gets the feature validator.
	 * 
	 * @return the featureValidator
	 */
	public Validator getFeatureValidator() {
		return featureValidator;
	}


	/**
	 * Sets the feature validator.
	 * 
	 * @param featureValidator the featureValidator to set
	 */
	public void setFeatureValidator(Validator featureValidator) {
		this.featureValidator = featureValidator;
	}


	/**
	 * Gets the operatorValidator
	 *
	 * @return the operatorValidator
	 */
	public Validator getOperatorValidator() {
		return operatorValidator;
	}


	/**
	 * Sets the operatorValidator
	 *
	 * @param operatorValidator the operatorValidator to set
	 */
	public void setOperatorValidator(Validator operatorValidator) {
		this.operatorValidator = operatorValidator;
	}
	
	public SrsBboxValidator getSrsBboxValidator() {
		return srsBboxValidator;
	}

	public void setSrsBboxValidator(SrsBboxValidator srsBboxValidator) {
		this.srsBboxValidator = srsBboxValidator;
	}
}
