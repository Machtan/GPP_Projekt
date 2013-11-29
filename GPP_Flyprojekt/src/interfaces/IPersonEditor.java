package interfaces;

import classes.PersonData;
import java.util.HashMap;

/**
 * The IPersonEditor interface <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 29-Nov-2013
 */
public interface IPersonEditor {
    public void editPerson(HashMap<PersonData, String> person);
}
