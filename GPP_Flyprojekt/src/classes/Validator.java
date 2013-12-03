package classes;

//import interfaces.IValidator;

import interfaces.IValidator;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * The Validator class validates fields on persons based on their PersonData
 * identifier. 
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 02-Dec-2013
 */
public class Validator implements IValidator {
    
    /**
     * Returns whether the given CPR-number is valid.
     * This is only a stub method, and should NOT be left unmodified
     * @param cpr The CPR-number as a string
     * @return Whether the given CPR-number is valid
     */
    private static boolean validateCPR(String cpr) {
        if (cpr.length() != 10) {return false;}
        int day = Integer.decode(cpr.substring(0, 2));
        int month = Integer.decode(cpr.substring(2, 4));
        int year = Integer.decode(cpr.substring(4, 6));
        if ((month < 1) || (month > 12)) {return false;}
        Calendar cal = new GregorianCalendar(1900+year, month-1, 1);
        if (day > cal.getMaximum(Calendar.DAY_OF_MONTH)) {return false;}
        // If nothing fails, assume that all is good
        return true;
    }
    
    /**
     * Returns whether the given name is valid
     * <TODO> This might be the place to check for terror suspects ;)
     * @param name The name to be validated
     * @return Whether the given name is valid
     */
    private static boolean validateName(String name) {
        return !((name.length() == 0) || (name.length() > 50));
    }
    
    /**
     * Returns whether the given nationality is valid
     * <TODO> This method needs to check with a list of all valid nationalities.
     * Perhaps this could be used alongside a digital database of passports, so
     * as to ensure that nationalities and the like wasn't misspelt, and to 
     * easier get it for the passenger in question.
     * @param nationality The nationality to be validated
     * @return Whether the given nationality is valid
     */
    private static boolean validateNationality(String nationality) {
        return !nationality.equals("");
    }
    
    @Override
    public <T> boolean validate(T field, String value) {
        if (PersonData.class.isInstance(field)) {
            switch((PersonData)field) {
                case NAME: {
                    return validateName(value);
                }
                case CPR: {
                    return validateCPR(value);
                }
                case NATIONALITY: {
                    return validateNationality(value);
                }   
                default: return false;
            }
        } else {
            return false;
        }
    }
    
}
