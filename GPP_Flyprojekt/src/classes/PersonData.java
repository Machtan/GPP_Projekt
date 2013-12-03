package classes;

import interfaces.IValidatable;

/**
 * The PersonData enum holds the names and values of the fields holding personal
 * data for each person on a flight reservation, and a short error message shown
 * as a tooltip on the warning if a field is deemed invalid by the validator.
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */
public enum PersonData implements IValidatable {
    // An identifier for each necessary field for person data, along with the
    // string to identify it by, and a tooltip to show when the data is deemed
    // invalid
    NAME("Navn", "Det indtastede navn er ugyldigt"), 
    NATIONALITY("Nationalitet", "Den indtastede nationalitet er ugyldig"), 
    CPR("CPR-nummer", "Det indtastede CPR-nummer er ugyldigt");
    
    // The name of the field
    private final String fieldName;
    
    // The error tooltip shown if the field is invalid
    private final String errorTip;
    
    /**
     * Initialise with the corresponding field name string.
     * @param fieldName The name of the field.
     * @param errorTip The error tip to be shown for this type of field
     */
    PersonData(String fieldName, String errorTip) {
        this.fieldName = fieldName;
        this.errorTip = errorTip;
    }
    
    /**
     * Returns a PersonData from the identifier string
     * @param data
     * @return
     * @throws IllegalArgumentException 
     */
    public PersonData getTypeFromString(String data) throws IllegalArgumentException {
        for (PersonData key : PersonData.values()) {
            if (data.equals(key.toString())) {
                return key;
            }
        }
        throw new IllegalArgumentException("No value found for the key '"+data+"'");
    }

    /**
     * @return The name of the data field as a string.
     */
    @Override
    public String toString()
    {
        return fieldName;
    }
    
    /**
     * @return The error tip for this field
     */
    @Override
    public String getErrorTip() {
        return errorTip;
    }
}
