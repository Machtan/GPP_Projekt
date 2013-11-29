package interfaces;

import classes.PersonData;
import java.util.HashMap;

/**
 * The IPersonDataList interface <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 29-Nov-2013
 */
public interface IPersonDataList {
    void addPerson(final HashMap<PersonData, String> person);
    void setEditor(final IPersonEditor editor);
}
