package edina.geocrosswalk.web.ws.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.SearchCommand;

public class SearchCommandValidatorTest{
    private static final ResourceBundle resources = ResourceBundle.getBundle("config/ws");
    SearchCommand searchCommand;
    SearchCommandValidator commandValidator;
    BoundingBoxValidator bboxValidator;
    NameValidator nameValidator;
    OperatorValidator operatorValidator;
    BindException errors;

    @Before
    public void setUp() throws Exception {
        
    	commandValidator = new SearchCommandValidator();
        
        nameValidator = new NameValidator();
        bboxValidator = new BoundingBoxValidator();

        operatorValidator = new OperatorValidator();
        searchCommand = new SearchCommand();
        errors = new BindException(searchCommand, "searchCommand");
        
        String regex = resources.getString("name.validation.regex");
        String maxRowSize = resources.getString("max.row.size");
        String countryList = resources.getString("country.validation.list");
        String gazetteerList = resources.getString("gazetteer.validation.list");
        String srsList = resources.getString("srs.validation.list");
        
        commandValidator.setCountryList(countryList);
        commandValidator.setGazetteerList(gazetteerList);
        commandValidator.setSrsList(srsList);
        commandValidator.setMaxRowSize(new Integer(maxRowSize));
       
        nameValidator.setNameValidationRegex(regex);
        commandValidator.setNameValidator(nameValidator);
        commandValidator.setBboxValidator(bboxValidator);
        commandValidator.setOperatorValidator(operatorValidator);
    }
      
    
    /*
	 * Tests supports method
	 */
	@Test
	public void testSupports() {
		assertTrue(commandValidator.supports(SearchCommand.class));
	}
    
    @Test
    public void testValid(){
        this.searchCommand.setName("glas");
        this.searchCommand.setFormat(GXWFormat.GXW);
        this.commandValidator.validate(this.searchCommand, errors);
        
        this.searchCommand.setFormat(GXWFormat.JSON);
        this.commandValidator.validate(this.searchCommand, errors);
        
        this.searchCommand.setFormat(GXWFormat.TXT);
        this.commandValidator.validate(this.searchCommand, errors); 
    }
    
    @Test
    public void testEmpty(){
        try{
            this.commandValidator.validate(this.searchCommand, errors);
        }
        catch(ValidationException ex){
            assertEquals(ex.getMessage(), NameValidator.NAME_REQUIRED);
        }
    }

	/*
	 * Tests validation of multiple names.
	 */
	@Test
	public void testMultipleNames() {
		try {
			// testing blank name param - validator should ignore..
			searchCommand.setName(" ,Edinburgh,Dover");
			commandValidator.validate(searchCommand, errors);
			assertFalse(errors.hasErrors());
		}
		catch (ValidationException e) {
			fail("Unexpected validation error");
		}
		try {
			// testing one invalid name param
			searchCommand.setName(" <,Edinburgh,Dover");
			commandValidator.validate(searchCommand, errors);
		}
		catch (ValidationException e) {
			assertTrue(errors.hasErrors());
			assertEquals(errors.getGlobalError().getDefaultMessage(), "Invalid name parameter");
		}

	}
    
	@Test
	public void testGazetteers() {
		searchCommand.setName("Edinburgh");

		searchCommand.setGazetteer("none");
		commandValidator.validate(searchCommand, errors);
		assertFalse(errors.hasErrors());

		searchCommand.setGazetteer("deep");
		commandValidator.validate(searchCommand, errors);
		assertFalse(errors.hasErrors());

	}
  
}
