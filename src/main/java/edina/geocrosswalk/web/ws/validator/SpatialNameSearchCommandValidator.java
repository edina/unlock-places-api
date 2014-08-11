package edina.geocrosswalk.web.ws.validator;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edina.geocrosswalk.domain.BoundingBox;
import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.SpatialNameSearchCommand;

/**
 * Validator to validate <code>SpatialNameSearchCommand</code> objects.
 * 
 * @author Joe Vernon
 * 
 */
public class SpatialNameSearchCommandValidator extends AbstractGXWCommandValidator {
	
	private Validator nameValidator;
	private Validator bboxValidator;
	private Validator operatorValidator;
	private SrsBboxValidator srsBboxValidator;
	private static final String INVALID_GEOMETRY = "The coordinates are not in the projection system that you have supplied.";
	private static final String REQUIRED_CODE = "required.srsbbox";
	private static final Logger logger = Logger.getLogger(SpatialNameSearchCommandValidator.class);

	/*
	 * @see
	 * edina.crosswalk.ws.validation.AbstractCrosswalkCommandValidator#supports
	 * (java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return SpatialNameSearchCommand.class.isAssignableFrom(clazz);
	}

	/*
	 * @see
	 * edina.crosswalk.ws.validation.AbstractCrosswalkCommandValidator#validate
	 * (java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object command, Errors errors) {
		
		// check for parameter binding errors
		checkErrors(errors);
		
		SpatialNameSearchCommand spatialNameCommand = (SpatialNameSearchCommand) command;
		String name = spatialNameCommand.getName();
		String operator = spatialNameCommand.getOperator();
		BoundingBox bbox = spatialNameCommand.getBoundingBox();
		Integer maxRows = spatialNameCommand.getMaxRows();
		Integer startRow = spatialNameCommand.getStartRow();
		GXWFormat format = spatialNameCommand.getFormat();
		String gazetteer = spatialNameCommand.getGazetteer();
		String key = spatialNameCommand.getKey();
		String country = spatialNameCommand.getCountry();
		String count = spatialNameCommand.getCount();
		String srs = spatialNameCommand.getSrs();
		String srs_in = spatialNameCommand.getSrs_in();
		String srs_out = srs;
		
		if(srs_in == null){
			srs_in = "4326";
		}
		
		// validate the name parameter
		ValidationUtils.invokeValidator(getNameValidator(), name, errors);

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
		
		// check for validation errors
		checkErrors(errors);

		// validate the paging params - set defaults if invalid
		spatialNameCommand.setMaxRows(validateMaxRowSize(maxRows));
		spatialNameCommand.setStartRow(validateStartRow(startRow));
		spatialNameCommand.setFormat(validateOutputFormat(format));
		spatialNameCommand.setGazetteer(validateGazetteer(gazetteer,key));
		spatialNameCommand.setCountry(validateCountry(country));
		spatialNameCommand.setCount(validateCount(count));
		spatialNameCommand.setSrs(validateSrs(srs));
		if(spatialNameCommand.getSrs().equals(BRITISH_NATIONAL_GRID)){
			spatialNameCommand.setCountry(UNITED_KINGDOM);
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
	 * Gets the nameValidator
	 * 
	 * @return the nameValidator
	 */
	public Validator getNameValidator() {
		return nameValidator;
	}


	/**
	 * Sets the nameValidator
	 * 
	 * @param nameValidator the nameValidator to set
	 */
	public void setNameValidator(Validator nameValidator) {
		this.nameValidator = nameValidator;
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