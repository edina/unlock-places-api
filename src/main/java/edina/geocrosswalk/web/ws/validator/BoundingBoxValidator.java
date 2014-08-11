package edina.geocrosswalk.web.ws.validator;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edina.geocrosswalk.domain.BoundingBox;

/**
 * Validates bbox parameters.
 * 
 * @author Joe Vernon
 */
public class BoundingBoxValidator implements Validator {

	private static Logger logger = Logger.getLogger(BoundingBoxValidator.class);
	private static final String REQUIRED_CODE = "required.bbox";
	private static final String BOUNDING_BOX_REQUIRED = "A bounding box is required";
	private static final String BOUNDING_BOX_EMPTY = "Search bounding box 'minx, maxx, miny, maxy' was empty";
	private static final String INVALID_CODE = "invalid.geometry";
	private static final String INVALID_GEOMETRY = "Invalid bounding box";
	
	/*
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return BoundingBox.class.isAssignableFrom(clazz);
	}


	/*
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object object, Errors errors) {
		
		BoundingBox bbox = (BoundingBox) object;
		
		if (bbox == null) {
			logger.warn(BOUNDING_BOX_EMPTY);
			errors.reject(REQUIRED_CODE, BOUNDING_BOX_REQUIRED);
			return;
		}
		
		if (!bbox.isValid()) {
			logger.warn(INVALID_GEOMETRY);
			errors.reject(INVALID_CODE, INVALID_GEOMETRY);
			return;
		}
	}
}
