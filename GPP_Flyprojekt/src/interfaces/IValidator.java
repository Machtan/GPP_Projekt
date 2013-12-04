package interfaces;

import classes.Utils;
import java.util.Arrays;
import java.util.HashSet;

/**
 * The IValidator interface is the interface for a class which can validate the
 * fields on a person based on a given identifier
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 02-Dec-2013
 */
public interface IValidator {
    public class NoValidatorException extends Exception {
        HashSet<IValidatable> fields;
        
        public NoValidatorException(IValidatable... fields) {
            super();
            this.fields = new HashSet(Arrays.asList(fields));
        }
        
        /**
         * Adds a new invalid field to the exception 
         * @param field 
         */
        public void addInvalidField(IValidatable field) {
            this.fields.add(field);
        }
        
        @Override
        public String getMessage() {
            return "No validator found for the field(s) '"+Utils.joinList(fields, ", ")+"'";
        }
    }
    public boolean validate(IValidatable field, String value) throws NoValidatorException;
    /**
     * This should return the tooltip to be shown when no validation is found 
     * for a field to be validated
     * @return The tooltip
     */
    public String getNoValidatorTip();
}
