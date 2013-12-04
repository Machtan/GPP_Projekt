package interfaces;

import classes.PersonData;
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
     * Sets the target for the edit button on the list
     * @param editor The IValidatedList to load the data into
     */
    void setEditor(final IValidatedList editor);
}
