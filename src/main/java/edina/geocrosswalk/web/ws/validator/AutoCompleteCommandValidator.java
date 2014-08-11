package edina.geocrosswalk.web.ws.validator;

import org.springframework.validation.Errors;

import edina.geocrosswalk.web.ws.NameStartsWithSearchCommand;

/**
 * Autocomplete command validator. Extend NameStartsWithSearchCommandValidator
 * to throw an error if format not applicable.
 */
public class AutoCompleteCommandValidator extends NameStartsWithSearchCommandValidator {
    
    final static String TOO_SHORT_MSG = "search term must be at least 4 characters";
    final static String WRONG_FORMAT_MSG = "auto complete only supports json, txt or gxw";
    
    /*
     * (non-Javadoc)
     * @see edina.geocrosswalk.web.ws.validator.NameStartsWithSearchCommandValidator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object target, Errors errors) {
        // perform name starts with validation
        super.validate(target, errors);

        NameStartsWithSearchCommand command = (NameStartsWithSearchCommand)target;
        
        // any name less than 4 characters is rejected
        if(command.getName() == null || command.getName().length() < 4){
            throw new IllegalArgumentException(TOO_SHORT_MSG);
        }
        
        // only txt, json and gxw is supported
        switch(command.getFormat()){
            case TXT:
            case JSON:
            case GXW:
                break;
            default:
                throw new IllegalArgumentException(WRONG_FORMAT_MSG);
        }
    }
}
