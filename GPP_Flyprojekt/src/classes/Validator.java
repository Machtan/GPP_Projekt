package classes;

//import interfaces.IValidator;

import interfaces.IValidatable;
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
        try {
            int day = Integer.parseInt(cpr.substring(0, 2));
            int month = Integer.parseInt(cpr.substring(2, 4));
            int year = Integer.parseInt(cpr.substring(4, 6));
            System.out.println(String.format("CPR-date: %s/%s/%s", day, month, year));
            if ((month < 1) || (month > 12)) {return false;}
            Calendar cal1900 = new GregorianCalendar(1900+year, month-1, 1);
            Calendar cal2000 = new GregorianCalendar(2000+year, month-1, 1);
            // Return false if the month cannot be that long in neither 1900 nor 2000
            if (!(
                    (day <= cal1900.getMaximum(Calendar.DAY_OF_MONTH)) ||
                    (day <= cal2000.getMaximum(Calendar.DAY_OF_MONTH))
                ))
                {return false;}
            Integer.parseInt(cpr.substring(7));
            // If nothing fails, assume that all is good
            return true;
        } catch (NumberFormatException ex) {
            //ex.printStackTrace();
            return false;
        }
    }
    
    private static boolean validateCardnumber(String cardNumber) {
        try {
            String value = cardNumber.replaceAll(" ", "");
            if (value.equals("")) {return false;}
            for (int i = 0; i < value.length(); i++) {
                Integer.parseInt(value.substring(i, i+1));
            }
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
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
    public boolean validate(IValidatable field, String value) throws IValidator.NoValidatorException {
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
            }
        } else if (ReservationData.class.isInstance(field)) {
            switch((ReservationData)field) {
                case CARDNUMBER:
                    return validateCardnumber(value);
                case PHONENUMBER:
                    return validatePhonenumber(value);
            }
        }
        // If it cannot be validated, tell the programmer
        throw new IValidator.NoValidatorException(field); 
    }

    @Override
    public String getNoValidatorTip() {
        return "Feltet kan ikke valideres. Kontakt din systemadministrator";
    }

    public static boolean validatePhonenumber(String value) {
        value = value.replaceAll(" ", "");
        if (value.startsWith("+")) {
            value = value.replaceFirst("[+]", "00");
        }
        if (value.equals("")) {
            return false;
        }
        try {
            for (int i = 0; i < value.length(); i++) {
                Integer.parseInt(value.substring(i, i+1));
            }
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    
}
