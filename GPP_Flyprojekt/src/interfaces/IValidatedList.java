package interfaces;

import java.util.HashMap;

/**
 * The IValidatedList interface is the necessary interface for a validated list
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 04-Dec-2013
 */
public interface IValidatedList {
    
    /**
     * Returns a HashMap of the data contained in the list
     * @return 
     */
    public HashMap<IValidatable, String> getData();
    
    /**
     * Adds data from the given HashMap to the corresponding fields on the list
     * @param <T>
     * @param data 
     */
    public <T extends IValidatable> void addData(HashMap<T, String> data);
    
    /**
     * Clears all fields and overwrites them with the corresponding text in 
     * the given hashMap
     * @param <T>
     * @param data 
     */
    public <T extends IValidatable> void setData(HashMap<T, String> data);
    
    /**
     * Sets the validator responsible for validating the fields of this list
     * @param validator The validator to be used
     */
    public void setValidator(IValidator validator);
    
    /**
     * Returns whether all fields in the list are empty
     * @return 
     */
    public boolean isEmpty();
    
    /**
     * Returns whether all fields in the list are valid
     * @return 
     */
    public boolean areFieldsValid();
    
    /**
     * Updates all the status labels
     */
    public void updateStatus();
    
    /**
     * Returns an array of the fields in the list that are invalid
     * @return 
     */
    public IValidatable[] getInvalidFields();

}
