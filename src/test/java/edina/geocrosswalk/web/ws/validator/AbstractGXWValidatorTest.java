package edina.geocrosswalk.web.ws.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.validation.Errors;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.SearchCommand;

/**
 * Abstract base class for Unlock command validator test cases.
 * 
 * @author Brian O'Hare
 * 
 */
public class AbstractGXWValidatorTest {

	private static Logger logger = Logger.getLogger(AbstractGXWValidatorTest.class);
	private static final ResourceBundle resources = ResourceBundle.getBundle("config/ws");
	private MockCommandValidator validator;

	@Before
	public void onSetUp() {
		validator = new MockCommandValidator();
		String maxRowSize = resources.getString("max.row.size");
		String gazetteerList = resources.getString("gazetteer.validation.list");
		validator.setMaxRowSize(new Integer(maxRowSize));
		validator.setGazetteerList(gazetteerList);
	}

	@Test
	public void validateMaxRows() {
		Integer maxRows = validator.validateMaxRowSize(null);
		assertEquals(new Integer(20), maxRows);
		maxRows = validator.validateMaxRowSize(234);
		assertEquals(new Integer(20), maxRows);
		maxRows = validator.validateMaxRowSize(15);
		assertEquals(new Integer(15), maxRows);
	}

	@Test
	public void validateSearchVariants() {
		String searchVariants = "true";
		String gazetteer = "deep";
		String result = validator.validateSearchVariants(searchVariants, gazetteer);
		assertNotNull(result);
		assertEquals(result, "true");

		searchVariants = "";
		result = validator.validateSearchVariants(searchVariants, gazetteer);
		assertNotNull(result);
		assertEquals(result, "false");

		searchVariants = "test";
		result = validator.validateSearchVariants(searchVariants, gazetteer);
		assertNotNull(result);
		assertEquals(result, "false");

		searchVariants = "true";
		result = validator.validateSearchVariants(searchVariants, gazetteer);
		assertNotNull(result);
		assertEquals(result, "true");

		searchVariants = "yes";
		result = validator.validateSearchVariants(searchVariants, gazetteer);
		assertNotNull(result);
		assertEquals(result, "true");

		gazetteer = "unlock";
		result = validator.validateSearchVariants(searchVariants, gazetteer);
		assertNull(result);
	}

	@Test
	public void validateVariantId() {
		String gazetteer = "deep";
		String variantId = "test";
		GXWFormat format = GXWFormat.GXW;
		SearchCommand searchCommand = new SearchCommand();
		searchCommand.setFormat(format);
		String validateVariantId = validator.validateVariantId(variantId, gazetteer, format, searchCommand);
		assertNotNull(validateVariantId);
		assertEquals(variantId, validateVariantId);

		gazetteer = "unlock";
		validateVariantId = validator.validateVariantId(variantId, gazetteer, format, searchCommand);
		assertNotNull(validateVariantId);
		assertEquals("", validateVariantId);

		format = GXWFormat.JSON;
		searchCommand.setFormat(format);
		validateVariantId = validator.validateVariantId(variantId, gazetteer, format, searchCommand);
		assertNotNull(validateVariantId);
		assertEquals("", validateVariantId);
		assertEquals(searchCommand.getFormat(), format);

		gazetteer = "deep";
		variantId = "";
		validateVariantId = validator.validateVariantId(variantId, gazetteer, format, searchCommand);
		assertNotNull(validateVariantId);
		assertEquals(variantId, validateVariantId);
		assertEquals(searchCommand.getFormat(), format);

		// Check invalid format gets changed to GXW
		format = GXWFormat.KML;
		variantId = "test";
		searchCommand.setFormat(format);
		validateVariantId = validator.validateVariantId(variantId, gazetteer, format, searchCommand);
		assertNotNull(validateVariantId);
		assertEquals(variantId, validateVariantId);
		assertEquals(searchCommand.getFormat(), GXWFormat.GXW);
	}

	@Test
	public void deepSrc() {
		String gazetteer = "deep";
		String deepSrc = "test";
		String deepSrcValidated = validator.validateDeepSrc(deepSrc, gazetteer);
		assertNotNull(deepSrcValidated);
		assertEquals(deepSrc, deepSrcValidated);

		gazetteer = "unlock";
		deepSrcValidated = validator.validateDeepSrc(deepSrc, gazetteer); 
		assertNull(deepSrcValidated);
		
		deepSrc = "";
		deepSrcValidated = validator.validateDeepSrc(deepSrc, gazetteer);
		assertNull(deepSrcValidated);
	}

	@Test
	public void validateStartRow() {
		Integer startRow = validator.validateStartRow(-1);
		assertEquals(new Integer(1), startRow);
		startRow = validator.validateStartRow(null);
		assertEquals(new Integer(1), startRow);
		startRow = validator.validateStartRow(2);
		assertEquals(new Integer(2), startRow);
	}

	@Test
	public void validateFormat() {
		GXWFormat format = validator.validateOutputFormat(null);
		assertEquals(GXWFormat.GXW, format);
		format = validator.validateOutputFormat(GXWFormat.GEORSS);
		assertEquals(GXWFormat.GEORSS, format);
	}


	/*
	 * Mock implementation of a geocrosswalk command validator for testing of
	 * common parameters.
	 */
	private class MockCommandValidator extends AbstractGXWCommandValidator {

		/*
		 * (non-Javadoc)
		 * 
		 * @seeedina.geocrosswalk.web.ws.validator.AbstractGXWCommandValidator#
		 * getMaxRowSize()
		 */

		public Integer getMaxRowSize() {
			return super.getMaxRowSize();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeedina.geocrosswalk.web.ws.validator.AbstractGXWCommandValidator#
		 * setMaxRowSize(java.lang.Integer)
		 */
		@Override
		public void setMaxRowSize(Integer maxRowSize) {
			super.setMaxRowSize(maxRowSize);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeedina.geocrosswalk.web.ws.validator.AbstractGXWCommandValidator#
		 * validateMaxRowSize(java.lang.Integer)
		 */
		@Override
		public Integer validateMaxRowSize(Integer maxRows) {
			return super.validateMaxRowSize(maxRows);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeedina.geocrosswalk.web.ws.validator.AbstractGXWCommandValidator#
		 * validateOutputFormat(edina.geocrosswalk.service.plugins.GXWFormat)
		 */
		@Override
		public GXWFormat validateOutputFormat(GXWFormat format) {
			return super.validateOutputFormat(format);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeedina.geocrosswalk.web.ws.validator.AbstractGXWCommandValidator#
		 * validateStartRow(java.lang.Integer)
		 */
		@Override
		public Integer validateStartRow(Integer startRow) {
			return super.validateStartRow(startRow);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeedina.geocrosswalk.web.ws.validator.AbstractGXWCommandValidator#
		 * getGazetteerList()
		 */
		public String getGazetteerList() {
			return super.getGazetteerList();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeedina.geocrosswalk.web.ws.validator.AbstractGXWCommandValidator#
		 * setGazetteerList(java.lang.String)
		 */
		public void setGazetteerList(String gazetteerList) {
			super.setGazetteerList(gazetteerList);
		}

		public void validate(Object target, Errors errors) {

		}

	}

}
