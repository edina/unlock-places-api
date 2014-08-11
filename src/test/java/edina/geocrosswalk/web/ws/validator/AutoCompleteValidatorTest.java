package edina.geocrosswalk.web.ws.validator;

import static org.junit.Assert.assertEquals;

import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;

import edina.geocrosswalk.service.plugins.GXWFormat;
import edina.geocrosswalk.web.ws.NameStartsWithSearchCommand;

public class AutoCompleteValidatorTest{
    private static final ResourceBundle resources = ResourceBundle.getBundle("config/ws");
    NameStartsWithSearchCommand searchCommand;
    AutoCompleteCommandValidator commandValidator;
    NameValidator nameValidator;
    BindException errors;

    @Before
    public void setUp() throws Exception {
        commandValidator = new AutoCompleteCommandValidator();
        nameValidator = new NameValidator();
        searchCommand = new NameStartsWithSearchCommand();
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

    @Test
    public void testTooShort(){
        try{
            this.searchCommand.setName("gla");
            this.commandValidator.validate(this.searchCommand, errors);
        }
        catch(IllegalArgumentException ex){
            assertEquals(ex.getMessage(), AutoCompleteCommandValidator.TOO_SHORT_MSG);
        }
    }   
    
    @Test
    public void testHTML(){
        try{
            this.searchCommand.setName("glas");
            this.searchCommand.setFormat(GXWFormat.HTML);
            this.commandValidator.validate(this.searchCommand, errors);
        }
        catch(IllegalArgumentException ex){
            assertEquals(ex.getMessage(), AutoCompleteCommandValidator.WRONG_FORMAT_MSG);
        }
    }
    
    @Test
    public void testGeoRSS(){
        try{
            this.searchCommand.setName("glas");
            this.searchCommand.setFormat(GXWFormat.GEORSS);
            this.commandValidator.validate(this.searchCommand, errors);
        }
        catch(IllegalArgumentException ex){
            assertEquals(ex.getMessage(), AutoCompleteCommandValidator.WRONG_FORMAT_MSG);
        }
    }
    
    @Test
    public void testKml(){
        try{
            this.searchCommand.setName("glas");
            this.searchCommand.setFormat(GXWFormat.KML);
            this.commandValidator.validate(this.searchCommand, errors);
        }
        catch(IllegalArgumentException ex){
            assertEquals(ex.getMessage(), AutoCompleteCommandValidator.WRONG_FORMAT_MSG);
        }
    }
}
