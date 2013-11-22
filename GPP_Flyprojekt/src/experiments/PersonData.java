package experiments;

/**
 * The PersonData enum holds the names and values of the fields holding personal
 * data for each person on a flight reservation
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */
public enum PersonData {
    // An identifier for each necessary field for person data, along with the
    // string to identify it by when searching for persons(?) or in the database
    NAME("Navn"), NATIONALITY("Nationalitet"), CPR("CPR-nummer");
    
    // The name of the field
    private final String fieldName;
    
    /**
     * Initialise with the corresponding field name string.
     * @param fieldName The name of the field.
     */
    PersonData(String fieldName)
    {
        this.fieldName = fieldName;
    }
    
    /**
     * @return The name of the data field as a string.
     */
    public String toString()
    {
        return fieldName;
    }
}
