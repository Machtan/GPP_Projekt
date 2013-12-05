package interfaces;

import classes.PersonData;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The IPersonDataList interface <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 29-Nov-2013
 */
public interface IPersonDataList {
    
    /**
     * Adds a person to the list and updates the layout
     * @param person The person to be added
     */
    void addPerson(final HashMap<PersonData, String> person);

    /**
     * Adds a list of persons to the list and updates the layout
     * @param persons 
     */
    void addPersons(ArrayList<HashMap<PersonData, String>> persons);
    
    /**
     * Sets the target for the edit button on the list
     * @param editor The IValidatedList to load the data into
     */
    void setEditor(final IValidatedList editor);
    
    /**
     * Deletes the entry for the person and updates the layout
     * @param person 
     */
    public void deletePerson(HashMap<PersonData, String> person);
    
    /**
     * Returns a list of the persons in the list (Duh)
     * @return persons!
     */
    public ArrayList<HashMap<PersonData, String>> getPersons();
}
