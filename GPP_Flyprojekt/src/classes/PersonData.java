package classes;

/**
 * The PersonData enum holds the names and values of the fields holding personal
 * data for each person on a flight reservation, and a short error message shown
 * as a tooltip on the warning if a field is deemed invalid by the validator.
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */
public enum PersonData {
    // An identifier for each necessary field for person data, along with the
    // string to identify it by when searching for persons(?) or in the database
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
     */
    PersonData(String fieldName, String errorTip) {
        this.fieldName = fieldName;
        this.errorTip = errorTip;
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
    public String getErrorTip() {
        return errorTip;
    }
}
