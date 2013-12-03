package classes;

import interfaces.IValidatable;

/**
 * The ReservationData enum represents the specific data needed for a 
 * reservation that isn't present elsewhere in the reservation editor.
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 03-Dec-2013
 */
public enum ReservationData implements IValidatable {
    
    PHONENUMBER("Tlf", "Det givne telefonnummer er ugyldigt"),
    CARDNUMBER("Kortnummer", "Det givne kortnummer er ugyldigt");
    
    // The name of the field
    private final String fieldName;
    
    // The error tooltip shown if the field is invalid
    private final String errorTip;
    
    /**
     * Constructor for the ReservationData class
     */
    ReservationData (String fieldName, String errorTip) {
        this.fieldName = fieldName;
        this.errorTip = errorTip;
    }
    
    @Override
    public String toString() {
        return this.fieldName;
    }

    @Override
    public String getErrorTip() {
        return this.errorTip;
    }
    
}
