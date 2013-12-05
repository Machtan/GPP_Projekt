package classes;

/**
 * The PersonEditor class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 22-Nov-2013
 */
public class PersonEditor extends ValidatedListPanel {
    public PersonEditor (int width, int height) {
        super(new Validator(), width, height, PersonData.values());
    }
}
